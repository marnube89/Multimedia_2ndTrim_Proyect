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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Administrative_newCarEntry extends AppCompatActivity {
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrative_new_car_entry);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        AutoCompleteTextView clientTextField = findViewById(R.id.clientTextField);
        ArrayList<String> clients = new ArrayList<String>();
        ArrayAdapter<String> clientName = new ArrayAdapter<String>(Administrative_newCarEntry.this, android.R.layout.simple_spinner_dropdown_item, clients);
        clientTextField.setAdapter(clientName);

        //Carga de clientes
        loadClients(clients, clientName);

        AppCompatButton confirm = findViewById(R.id.Confirm);
        TextInputEditText car = findViewById(R.id.carTextField);
        TextInputEditText date = findViewById(R.id.dateTextField);

        //Se guardan los datos en la base de datos
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(car.getText().toString().isEmpty()) && !(date.getText().toString().isEmpty()) && !(clientTextField.getText().toString().isEmpty())){
                    CarInShop carTemp = new CarInShop(car.getText().toString(), "", date.getText().toString(), clientTextField.getText().toString());
                    database.child("carInShop").child(carTemp.getLicensePlate()).setValue(carTemp);
                    finish();
                }else{
                    //validacion de campos vacios
                }
            }
        });

        //Cancelar operacion
        AppCompatButton cancel = findViewById(R.id.Cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Carga una lista con los clientes registrados y actualiza su adaptador
     * @param clients lista de nombres de clientes
     * @param adapter adaptador a actualizar
     */
    private void loadClients(ArrayList<String> clients, ArrayAdapter<String> adapter) {
        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(clients.size()>0){
                    clients.removeAll(clients);
                }

                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    User userTemp = new User((HashMap<String, Object>) postSnapshot.getValue());
                    if(userTemp.getJobRol()==0){
                        clients.add(userTemp.getFullName());
                    }

                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Info", "Fin de lectura");
            }
        });
    }
}