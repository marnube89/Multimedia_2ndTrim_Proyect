package com.example.gestiontaller.chief_mechanic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.R;
import com.example.gestiontaller.aux_classes.CustomGraphics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MechanicChiefMainPage extends AppCompatActivity {
    private FirebaseUser current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mechanic_chief_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        current = FirebaseAuth.getInstance().getCurrentUser();

        AppCompatButton completeTasks, newTasks, checkRepairs, asignMechanicsToTask;
        completeTasks = findViewById(R.id.completeTasks);
        completeTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoCompleteTasks = new Intent(MechanicChiefMainPage.this, ChiefMechanic_CompleteTasks.class);
                startActivity(gotoCompleteTasks);
            }
        });

        newTasks = findViewById(R.id.newTasks);
        newTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoNewTask = new Intent(MechanicChiefMainPage.this, ChiefMechanic_CreateTask.class);
                startActivity(gotoNewTask);
            }
        });

        checkRepairs = findViewById(R.id.checkRepairs);
        checkRepairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoCheckRepairs = new Intent(MechanicChiefMainPage.this, ChiefMechanic_CheckAsignedRepairs.class);
                gotoCheckRepairs.putExtra("user", current);
                startActivity(gotoCheckRepairs);
            }
        });

        asignMechanicsToTask = findViewById(R.id.asignMechanicsToTask);
        asignMechanicsToTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoAsignMechanics = new Intent(MechanicChiefMainPage.this, ChiefMechanic_AsignMechanicsToTask.class);
                startActivity(gotoAsignMechanics);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("User", FirebaseAuth.getInstance().getCurrentUser().getUid());
    }
}