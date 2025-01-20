package com.example.gestiontaller.views.chief_mechanic;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.R;
import com.example.gestiontaller.adapters.UsersAdapter;
import com.example.gestiontaller.data_classes.RepairTask;
import com.example.gestiontaller.data_classes.User;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChiefMechanic_AsignMechanicsToTask extends AppCompatActivity {
    private DatabaseReference database;
    private ArrayList<User> mechanics = new ArrayList<User>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chief_mechanic_asign_mechanics_to_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);
        ListView mechanicsList = findViewById(R.id.mechanicsList);

        //Mechanics list adapter data fetch
        UsersAdapter adapter = new UsersAdapter(this, mechanics);
        mechanicsList.setAdapter(adapter);

        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    User temp = new User((HashMap<String, Object>) postSnapshot.getValue());
                    if(temp.getJobRol()==4){
                        mechanics.add(temp);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {            }
        });


        //Task selector (Drop down menu) item adapter
        ArrayList<String> tasksID = new ArrayList<>();
        ArrayAdapter<String> taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tasksID);
        AutoCompleteTextView tasks = findViewById(R.id.taskNumber);
        tasks.setAdapter(taskAdapter);
        database.child("tasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    RepairTask temp = new RepairTask((HashMap<String, Object>) postSnapshot.getValue());
                    tasksID.add(temp.getTaskNumber());
                }
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AppCompatButton confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> mechanicsNames = getSelectedUsers(adapter, mechanicsList);
                database.child("tasks").child(tasks.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        RepairTask temp = new RepairTask((HashMap<String, Object>) snapshot.getValue());
                        temp.setMechanics(mechanicsNames);
                        database.child("tasks").child(tasks.getText().toString()).setValue(temp);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    public ArrayList<String> getSelectedUsers(UsersAdapter adapter, ListView m){
        ArrayList<String> temp = new ArrayList<String>();
        for (int i = 0; i < adapter.getCount(); i++) {
            View viewTemp = m.getChildAt(i);
            CheckBox isSelected = viewTemp.findViewById(R.id.selectedCheckBox);
            if(isSelected.isChecked()){
                temp.add(mechanics.get(i).getFullName());
            }
        }
        return temp;
    }

}