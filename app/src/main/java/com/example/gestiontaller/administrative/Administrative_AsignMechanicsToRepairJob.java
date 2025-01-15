package com.example.gestiontaller.administrative;

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

import com.example.gestiontaller.R;
import com.example.gestiontaller.aux_classes.RepairJob;
import com.example.gestiontaller.aux_classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Administrative_AsignMechanicsToRepairJob extends AppCompatActivity {
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrative_asign_mechanics_to_repair_job);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AutoCompleteTextView repair = findViewById(R.id.repairSelector);
        ArrayList<String> repairs = new ArrayList<String>();
        database.child("repairJobs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(repairs.size()>0){
                    repairs.removeAll(repairs);
                }

                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    RepairJob repairTemp = new RepairJob((HashMap<String, Object>) postSnapshot.getValue());
                    repairs.add(repairTemp.getRepairNumber());
                }
                ArrayAdapter<String> licensePlate = new ArrayAdapter<String>(Administrative_AsignMechanicsToRepairJob.this, android.R.layout.simple_spinner_dropdown_item, repairs);
                repair.setAdapter(licensePlate);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Info", "Fin de lectura");
            }
        });

        AutoCompleteTextView mechanic = findViewById(R.id.mechanicSelector);
        ArrayList<String> mechanics = new ArrayList<String>();
        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mechanics.size()>0){
                    mechanics.removeAll(mechanics);
                }

                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    User userTemp = new User((HashMap<String, Object>) postSnapshot.getValue());
                    if(userTemp.getJobRol()==4){
                        mechanics.add(userTemp.getFullName());
                    }

                }
                ArrayAdapter<String> chiefName = new ArrayAdapter<String>(Administrative_AsignMechanicsToRepairJob.this, android.R.layout.simple_spinner_dropdown_item, mechanics);
                mechanic.setAdapter(chiefName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Info", "Fin de lectura");
            }
        });

        AppCompatButton confirm = findViewById(R.id.confirmBt);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child("repairJobs").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.i("Mecanico", mechanic.getText().toString());
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            RepairJob repairTemp = new RepairJob((HashMap<String, Object>) postSnapshot.getValue());
                            if(repairTemp.getRepairNumber().equals(repair.getText().toString())){
                                ArrayList<String> mechanics = repairTemp.getMechanics();
                                mechanics.add(mechanic.getText().toString());
                                database.child("repairJobs").child(repair.getText().toString()).setValue(repairTemp);
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

        AppCompatButton cancel = findViewById(R.id.cancelBt);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}