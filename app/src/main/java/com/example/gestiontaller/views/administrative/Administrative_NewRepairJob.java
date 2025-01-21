package com.example.gestiontaller.views.administrative;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

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
import com.example.gestiontaller.data_classes.RepairJob;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Administrative_NewRepairJob extends AppCompatActivity {
    private DatabaseReference database;
    private int repairNumber = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrative_new_repair_job);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        //Genera un nuevo numero de reparacion
        generateRepairNumber();

        TextView repairNumberTittle = findViewById(R.id.repairNumberTittle);
        repairNumberTittle.setText("Reparacion Nº: " + repairNumber);
        AppCompatButton confirm = findViewById(R.id.confirm);
        AutoCompleteTextView car = findViewById(R.id.carSelectorTextField);
        TextInputEditText date = findViewById(R.id.date_TextEdit);
        TextInputEditText desc = findViewById(R.id.description);
        final String[] chiefMechanic = {""};

        ArrayList<String> carsInShop = new ArrayList<String>();
        ArrayAdapter<String> licensePlate = new ArrayAdapter<String>(Administrative_NewRepairJob.this, android.R.layout.simple_spinner_dropdown_item, carsInShop);
        car.setAdapter(licensePlate);

        //Carga los datos del adaptador
        loadCarsInShop(carsInShop, licensePlate);

        //Confirma y guarda los datos en la base de datos
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!car.getText().toString().isEmpty() && !date.getText().toString().isEmpty() && !desc.getText().toString().isEmpty()){
                    database.child("carInShop").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot postSnapshot: snapshot.getChildren()){
                                CarInShop userTemp = new CarInShop((HashMap<String, Object>) postSnapshot.getValue());
                                if(userTemp.getLicensePlate().equals(car.getText().toString())){
                                    RepairJob repairTemp = new RepairJob(Integer.toString(repairNumber), userTemp.getLicensePlate(), date.getText().toString(), userTemp.getChiefMechanic(), desc.getText().toString());
                                    database.child("repairJobs").child(repairTemp.getRepairNumber()).setValue(repairTemp);
                                    finish();
                                    break;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });

        //Cancela la operacion
        AppCompatButton cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Carga los datos en el adaptador
     * @param carsInShop lista de matriculas
     */
    private void loadCarsInShop(ArrayList<String> carsInShop, ArrayAdapter<String> adapter) {
        database.child("carInShop").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(carsInShop.size()>0){
                    carsInShop.removeAll(carsInShop);
                }

                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    CarInShop carTemp = new CarInShop((HashMap<String, Object>) postSnapshot.getValue());
                    carsInShop.add(carTemp.getLicensePlate());
                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Info", "Fin de lectura");
            }
        });
    }

    private void generateRepairNumber() {
        database.child("repairJobs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int contador = 0;
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    contador++;
                }
                if(contador>0){
                    repairNumber = contador+1;
                }else if(contador<=0){
                    repairNumber = 1;
                }
                Log.i("numeroRepair", Integer.toString(repairNumber));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}