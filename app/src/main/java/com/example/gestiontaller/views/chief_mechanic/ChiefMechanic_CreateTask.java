package com.example.gestiontaller.views.chief_mechanic;

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
import com.example.gestiontaller.data_classes.RepairJob;
import com.example.gestiontaller.data_classes.RepairTask;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChiefMechanic_CreateTask extends AppCompatActivity {
    private DatabaseReference database;
    private int taskNumber;
    private ArrayList<String> repairNumbers = new ArrayList<String>();
    private final RepairJob[] repairData = new RepairJob[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chief_mechanic_create_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = FirebaseDatabase.getInstance().getReference();

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        database.child("tasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int contador = 0;
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    contador++;
                }
                if(contador>0){
                    taskNumber = contador+1;
                }else if(contador<=0){
                    taskNumber = 1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AutoCompleteTextView repair = findViewById(R.id.repairTextField);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, repairNumbers);
        repair.setAdapter(adapter);
        database.child("repairJobs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    repairData[0] = new RepairJob((HashMap<String, Object>) postSnapshot.getValue());
                    repairNumbers.add(repairData[0].getRepairNumber());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        TextInputEditText date = findViewById(R.id.dateTextField);
        TextInputEditText desc = findViewById(R.id.description);

        AppCompatButton confirm = findViewById(R.id.confirmBtt);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!date.getText().toString().isEmpty() && !desc.getText().toString().isEmpty() && !repair.getText().toString().isEmpty()){
                    database.child("repairJobs").child(repair.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            repairData[0] = new RepairJob((HashMap<String, Object>) task.getResult().getValue());
                        }
                    });
                    RepairTask newTask = new RepairTask(Integer.toString(taskNumber), repairData[0].getChiefMechanic(), date.getText().toString(), desc.getText().toString());
                    database.child("tasks").child(Integer.toString(taskNumber)).setValue(newTask);
                    finish();
                }
            }
        });

        AppCompatButton cancel = findViewById(R.id.cancelBtt);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}