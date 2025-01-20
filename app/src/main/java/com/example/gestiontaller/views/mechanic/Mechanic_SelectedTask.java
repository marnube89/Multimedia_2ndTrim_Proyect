package com.example.gestiontaller.views.mechanic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.gestiontaller.views.administrative.Administrative_OrderParts;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Mechanic_SelectedTask extends AppCompatActivity {
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mechanic_selected_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        RepairTask task = (RepairTask) getIntent().getSerializableExtra("task");

        TextInputEditText taskNum = findViewById(R.id.taskContent);
        taskNum.setText(task.getTaskNumber());

        TextInputEditText repairNum = findViewById(R.id.repairContent);
        repairNum.setText(task.getRepairNumber());

        TextInputEditText taskDesc = findViewById(R.id.descContent);
        taskDesc.setText(task.getDescription());

        ArrayList<User> mechanics = new ArrayList<User>();
        UsersAdapter adapter = new UsersAdapter(this, mechanics, false);
        ListView mechanicsList = findViewById(R.id.mechanicsListView);
        mechanicsList.setAdapter(adapter);

        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //I check for the mechanics assigned to this task
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    User temp = new User((HashMap<String, Object>) postSnapshot.getValue());
                    for (int i = 0; i < task.getMechanics().size(); i++) {
                        if(temp.getFullName().equals(task.getMechanics().get(i)) && temp.getJobRol()==4){
                            mechanics.add(temp);
                            break;
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AppCompatButton orderPieces = findViewById(R.id.orderPieces);
        orderPieces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoOrderParts = new Intent(Mechanic_SelectedTask.this, Administrative_OrderParts.class);
                startActivity(gotoOrderParts);
            }
        });

        AppCompatButton editDesc = findViewById(R.id.editDesc);
        editDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.setDescription(taskDesc.getText().toString());
                database.child("tasks").child(task.getTaskNumber()).setValue(task);
            }
        });

        TextInputEditText comment = findViewById(R.id.commentsContent);
        AppCompatButton commentTask = findViewById(R.id.commentTask);
        commentTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!comment.getText().toString().isEmpty()){
                    task.setDescription(comment.getText().toString());
                    database.child("tasks").child(task.getTaskNumber()).setValue(task);
                }else{
                    Snackbar.make(findViewById(R.id.main), "No se puede dejar el comentario vacio", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }
}