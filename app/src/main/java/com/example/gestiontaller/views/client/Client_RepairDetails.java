package com.example.gestiontaller.views.client;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.R;
import com.example.gestiontaller.data_classes.RepairJob;
import com.example.gestiontaller.data_classes.User;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Client_RepairDetails extends AppCompatActivity {
    private RepairJob selectedRepair;
    private DatabaseReference database;
    private User chief;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_repair_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = FirebaseDatabase.getInstance().getReference();

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        //Cargamos los datos de los objetos
        selectedRepair = (RepairJob) getIntent().getSerializableExtra("repair");
        loadChief();

        //Inicializacion de variables
        TextInputEditText date = findViewById(R.id.dateContent);
        date.setText(selectedRepair.getStartDate());

        TextInputEditText licensePlate = findViewById(R.id.licensePlateContent);
        licensePlate.setText(selectedRepair.getCar());

        TextInputEditText repairDesc = findViewById(R.id.descContent);
        repairDesc.setText(selectedRepair.getDescription());

        TextInputEditText repairDiagnostic = findViewById(R.id.diagnosticContent);
        repairDiagnostic.setText(selectedRepair.getDiagnostic());


        //Boton para responder a propuestas de reparacion
        AppCompatButton respond = findViewById(R.id.respond_diagnostic);
        respond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Manda al usuario a la aplicacion de correo para avisar al mecanico jefe a cargo de esa reparacion
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{chief.getMail()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Respuesta a propuesta de reparacion numero: " + selectedRepair.getRepairNumber());
                startActivity(intent);
            }
        });
    }

    /**
     * Metodo que busca en la BBDD remota al mecanico a cargo de la reparacion seleccionada
     */
    private void loadChief(){
        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    User temp = new User((HashMap<String, Object>) postSnapshot.getValue());
                    if(temp.getFullName().equals(selectedRepair.getChiefMechanic()) && temp.getJobRol()==3){
                        returnChief(temp);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     * Inicializa la variable chief, para su posterior uso
     * @param user valor que se le va a dar a chief
     */
    private void returnChief(User user){
        chief = user;
    }
}