package com.example.gestiontaller.views.mechanic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private ArrayList<User> mechanics = new ArrayList<User>();
    private UsersAdapter adapter;

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

        //Se recogen los datos de la tarea seleccionada
        RepairTask task = (RepairTask) getIntent().getSerializableExtra("task");


        //Inicializacion de variables y asignacion de datos
        TextInputEditText taskNum = findViewById(R.id.taskContent);
        taskNum.setText(task.getTaskNumber());

        TextInputEditText repairNum = findViewById(R.id.repairContent);
        repairNum.setText(task.getRepairNumber());

        TextInputEditText taskDesc = findViewById(R.id.descContent);
        taskDesc.setText(task.getDescription());




        adapter = new UsersAdapter(this, mechanics, false);
        ListView mechanicsList = findViewById(R.id.mechanicsListView);
        mechanicsList.setAdapter(adapter);

        //Actualizacion de lista de mecanicos asignados a esta tarea
        mechanicsListUpdate(task);


        //Asignacion de funciones de los botones
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
                if(!taskDesc.getText().toString().isEmpty()) {
                    task.setDescription(taskDesc.getText().toString());

                    //Se actualiza el objeto en la base de datos con la nueva descripcion de la tarea
                    database.child("tasks").child(task.getTaskNumber()).setValue(task);
                }else{
                    Snackbar.make(findViewById(R.id.main), "No se puede dejar la descripcion vacia", Snackbar.LENGTH_SHORT).show();

                }
            }
        });

        TextInputEditText comment = findViewById(R.id.commentsContent);
        AppCompatButton commentTask = findViewById(R.id.commentTask);
        commentTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!comment.getText().toString().isEmpty()){
                    task.setComment(comment.getText().toString());

                    //Se actualiza el objeto en la base de datos con el comentario editado
                    database.child("tasks").child(task.getTaskNumber()).setValue(task);
                }else{
                    Snackbar.make(findViewById(R.id.main), "No se puede dejar el comentario vacio", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Actualiza los datos de la lista de mecanicos asignados a esta tarea
     */
    private void mechanicsListUpdate(RepairTask task){
        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Busco a los mecanicos asignados a esta tarea para mostrarlos
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
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}