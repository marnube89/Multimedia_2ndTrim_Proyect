package com.example.gestiontaller.views.chief_mechanic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.R;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.example.gestiontaller.data_classes.User;
import com.example.gestiontaller.graphics.ExitDialog;
import com.example.gestiontaller.views.administrative.AdministrativeMainPage;

public class MechanicChiefMainPage extends AppCompatActivity {
    private User currentUser;

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

        currentUser = (User) getIntent().getSerializableExtra("user");

        ImageButton exit = findViewById(R.id.exitBtn);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExitDialog.exitDialog(currentUser, MechanicChiefMainPage.this);
            }
        });

        TextView greeting = findViewById(R.id.greetings);
        greeting.setText(getResources().getString(R.string.greetings) + " " + currentUser.getFullName());


        //Inicializacion y asignacion de funciones a los botones del menu
        AppCompatButton newTasks, checkRepairs, asignMechanicsToTask;

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
                gotoCheckRepairs.putExtra("user", currentUser);
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

}