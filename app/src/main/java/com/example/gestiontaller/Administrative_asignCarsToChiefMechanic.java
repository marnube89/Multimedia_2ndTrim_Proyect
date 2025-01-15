package com.example.gestiontaller;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
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

        AutoCompleteTextView chiefMechanic = findViewById(R.id.chiefMechanicTextField);
        ArrayList<String> chiefMechanics = new ArrayList<String>();
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
                ArrayAdapter<String> chiefName = new ArrayAdapter<String>(Administrative_asignCarsToChiefMechanic.this, android.R.layout.simple_spinner_dropdown_item, chiefMechanics);
                chiefMechanic.setAdapter(chiefName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Info", "Fin de lectura");
            }
        });

        AutoCompleteTextView car = findViewById(R.id.carSelectorTextField);
        ArrayList<String> carsInShop = new ArrayList<String>();
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
                ArrayAdapter<String> licensePlate = new ArrayAdapter<String>(Administrative_asignCarsToChiefMechanic.this, android.R.layout.simple_spinner_dropdown_item, carsInShop);
                car.setAdapter(licensePlate);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Info", "Fin de lectura");
            }
        });

        AppCompatButton confirm = findViewById(R.id.confirmButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child("carInShop").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            CarInShop userTemp = new CarInShop((HashMap<String, Object>) postSnapshot.getValue());
                            if(userTemp.getLicensePlate().equals(car.getText().toString())){
                                userTemp.setChiefMechanic(chiefMechanic.getText().toString());
                                database.child("carInShop").child(car.getText().toString()).setValue(userTemp);
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
        });
    }
}