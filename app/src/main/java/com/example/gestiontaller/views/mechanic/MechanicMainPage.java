package com.example.gestiontaller.views.mechanic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.R;
import com.example.gestiontaller.adapters.TasksAdapter;
import com.example.gestiontaller.data_classes.RepairTask;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.example.gestiontaller.data_classes.User;
import com.example.gestiontaller.graphics.ExitDialog;
import com.example.gestiontaller.views.administrative.AdministrativeMainPage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MechanicMainPage extends AppCompatActivity {
    private User currentUser;
    private DatabaseReference database;
    private ArrayList<RepairTask> taskList = new ArrayList<>();
    private TasksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mechanic_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Se recuperan los datos de este usuario para su posterior uso
        currentUser = (User) getIntent().getSerializableExtra("user");

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        TextView greeting = findViewById(R.id.greetings);
        greeting.setText(getResources().getString(R.string.greetings) + " " + currentUser.getFullName());

        ImageButton exit = findViewById(R.id.exitBtn);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExitDialog.exitDialog(currentUser, MechanicMainPage.this);
            }
        });

        ListView tasks = findViewById(R.id.taskList);
        adapter = new TasksAdapter(this, taskList);
        tasks.setAdapter(adapter);

        //Se actualizan la lista de tareas asignadas a este mecanico
        taskAdapterUpdate();


        tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Llevara al usuario a una pagina con los datos de esta tarea;
                // Esta nueva ventana junta en si misma:
                //      -Modificar Tarea
                //      -Solicitar piezas
                //      -Incluir comentarios

                Intent gotoSelectedTask = new Intent(MechanicMainPage.this, Mechanic_SelectedTask.class);
                gotoSelectedTask.putExtra("task", adapter.getItem(i));
                startActivity(gotoSelectedTask);
            }
        });

    }

    /**
     * Actualizara la lista de tareas de este mecanico
     */
    private void taskAdapterUpdate(){
        database.child("tasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taskList.removeAll(taskList);
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    RepairTask temp = new RepairTask((HashMap<String, Object>) postSnapshot.getValue());
                    for (int i = 0; i < temp.getMechanics().size(); i++) {
                        if(temp.getMechanics().get(i).equals(currentUser.getFullName())){
                            taskList.add(temp);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}