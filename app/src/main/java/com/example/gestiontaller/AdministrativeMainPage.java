package com.example.gestiontaller;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AdministrativeMainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrative_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AppCompatButton newEntry, asignCars, newRepair,asignMechanics, notifyClient, checkStock, orderPieces;
        newEntry = findViewById(R.id.newEntryBt);
        newEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoNewEntry = new Intent(AdministrativeMainPage.this, Administrative_newCarEntry.class);
                startActivity(gotoNewEntry);
            }
        });
        asignCars = findViewById(R.id.asignCarsBt);
        asignCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoAsignCars = new Intent(AdministrativeMainPage.this, Administrative_asignCarsToChiefMechanic.class);
                startActivity(gotoAsignCars);
            }
        });
        newRepair = findViewById(R.id.newRepairBt);
        newRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoNewRepair = new Intent(AdministrativeMainPage.this, Administrative_NewRepairJob.class);
                startActivity(gotoNewRepair);
            }
        });
        asignMechanics = findViewById(R.id.asignMechanicsBt);
        asignMechanics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoAsignMechanics = new Intent(AdministrativeMainPage.this, Administrative_AsignMechanicsToRepairJob.class);
                startActivity(gotoAsignMechanics);
            }
        });
        notifyClient = findViewById(R.id.notifyClientBt);
        notifyClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                ArrayList<User> clients = new ArrayList<User>();
                ArrayAdapter<User> adapter = new ArrayAdapter<>(AdministrativeMainPage.this, android.R.layout.simple_spinner_dropdown_item, clients);

                ArrayList<String> clientNames = new ArrayList<String>();
                ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(AdministrativeMainPage.this, android.R.layout.simple_spinner_dropdown_item, clientNames);

                database.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot : snapshot.getChildren()){
                            User temp = new User((HashMap<String, Object>) postSnapshot.getValue());
                            if(temp.getJobRol()==0){
                                clients.add(temp);
                                clientNames.add(temp.getFullName());
                            }
                        }
                        adapter.notifyDataSetChanged();
                        namesAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Spinner clientSelector = new Spinner(AdministrativeMainPage.this);
                clientSelector.setAdapter(namesAdapter);
                new MaterialAlertDialogBuilder(AdministrativeMainPage.this)
                        .setTitle("Selecciona un cliente")
                        .setView(clientSelector)
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for (int j = 0; j < adapter.getCount(); j++) {
                                    User u = adapter.getItem(j);
                                    if(u.getFullName().equals(clientSelector.getSelectedItem().toString())){
                                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                                        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                                        //Direccion no va
                                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{u.getMail()});
                                        intent.putExtra(Intent.EXTRA_SUBJECT, "Notificacion para: " + u.getFullName());
                                        startActivity(intent);
                                        break;
                                    }
                                }
                            }
                        })
                        .show();


            }
        });
        checkStock = findViewById(R.id.checkStockBt);
        checkStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoCheckStock = new Intent(AdministrativeMainPage.this, Administrative_CheckPartsStock.class);
                startActivity(gotoCheckStock);
            }
        });
        orderPieces = findViewById(R.id.newPiecesOrderBt);
        orderPieces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoOrderPieces = new Intent(AdministrativeMainPage.this, Administrative_OrderParts.class);
                startActivity(gotoOrderPieces);
            }
        });


    }
}