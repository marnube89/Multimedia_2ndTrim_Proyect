package com.example.gestiontaller.views.administrative;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.data_classes.CarInShop;
import com.example.gestiontaller.R;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.example.gestiontaller.data_classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Administrative_asignCarsToChiefMechanic extends AppCompatActivity {
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrative_asign_cars_to_chief_mechanic);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        AutoCompleteTextView chiefMechanic = findViewById(R.id.chiefMechanicTextField);
        ArrayList<String> chiefMechanics = new ArrayList<String>();
        ArrayAdapter<String> chiefName = new ArrayAdapter<String>(Administrative_asignCarsToChiefMechanic.this, android.R.layout.simple_spinner_dropdown_item, chiefMechanics);
        chiefMechanic.setAdapter(chiefName);

        //Carga todos los mecanicos jefe disponibles
        loadChiefMehanics(chiefMechanics, chiefName);

        AutoCompleteTextView car = findViewById(R.id.carSelectorTextField);
        ArrayList<String> carsInShop = new ArrayList<String>();
        ArrayAdapter<String> licensePlate = new ArrayAdapter<String>(Administrative_asignCarsToChiefMechanic.this, android.R.layout.simple_spinner_dropdown_item, carsInShop);
        car.setAdapter(licensePlate);

        //Carga todos los coches que hay en el taller ahora mismo
        loadCarsInShop(carsInShop, licensePlate);

        //Confirma y guarda los datos en la base de datos
        AppCompatButton confirm = findViewById(R.id.confirmButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!car.getText().toString().isEmpty() && !chiefMechanic.getText().toString().isEmpty()){
                    database.child("carInShop").child(car.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            CarInShop userTemp = new CarInShop((HashMap<String, Object>) task.getResult().getValue());
                            userTemp.setChiefMechanic(chiefMechanic.getText().toString());
                            database.child("carInShop").child(car.getText().toString()).setValue(userTemp);
                            finish();
                        }
                    });
                }else{
                    Snackbar.make(findViewById(R.id.main), "No se pueden dejar campos vacios", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Carga y actualiza el adaptador de coches disponibles
     * @param carsInShop lista de coches
     * @param licensePlate adaptador a actualizar
     */
    private void loadCarsInShop(ArrayList<String> carsInShop, ArrayAdapter<String> licensePlate) {
        database.child("carInShop").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(carsInShop.size()>0){
                    carsInShop.removeAll(carsInShop);
                }

                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    CarInShop userTemp = new CarInShop((HashMap<String, Object>) postSnapshot.getValue());
                    carsInShop.add(userTemp.getLicensePlate());
                }
                licensePlate.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Info", "Fin de lectura");
            }
        });
    }

    /**
     * Carga y actualiza el adaptador con los nombres de los mecanicos jefe
     * @param chiefMechanics lista de nombres
     * @param chiefName adaptador a actualizar
     */
    private void loadChiefMehanics(ArrayList<String> chiefMechanics, ArrayAdapter<String> chiefName) {
        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(chiefMechanics.size()>0){
                    chiefMechanics.removeAll(chiefMechanics);
                }

                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    User userTemp = new User((HashMap<String, Object>) postSnapshot.getValue());
                    if(userTemp.getJobRol()==3){
                        chiefMechanics.add(userTemp.getFullName());
                    }

                }
                chiefName.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Info", "Fin de lectura");
            }
        });
    }
}