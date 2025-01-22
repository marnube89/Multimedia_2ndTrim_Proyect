package com.example.gestiontaller.views.chief_mechanic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.R;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.example.gestiontaller.data_classes.RepairJob;
import com.example.gestiontaller.adapters.RepairListAdapter;
import com.example.gestiontaller.data_classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChiefMechanic_CheckAsignedRepairs extends AppCompatActivity {
    private DatabaseReference database;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chief_mechanic_check_asigned_repairs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = FirebaseDatabase.getInstance().getReference();
        currentUser = (User) getIntent().getSerializableExtra("user");

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        //Carga de lista de reparaciones
        ListView assignedRepairs = findViewById(R.id.assignedRepairs);
        ArrayList<RepairJob> repairs = new ArrayList<RepairJob>();
        RepairListAdapter adapter = new RepairListAdapter(ChiefMechanic_CheckAsignedRepairs.this, repairs);
        assignedRepairs.setAdapter(adapter);

        loadRepairs(adapter, repairs);

        //Goto a los detalles de la reparacion
        assignedRepairs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RepairJob repair = (RepairJob) adapterView.getItemAtPosition(i);
                Intent gotoDiagnostic = new Intent(ChiefMechanic_CheckAsignedRepairs.this, ChiefMechanic_Add_Complete_Diagnostic.class);
                gotoDiagnostic.putExtra("repair", repair);
                startActivity(gotoDiagnostic);

            }
        });

    }

    /**
     * Carga las reparaciones asignadas a este usuario y actualiza el adaptador
     * @param adapter adaptador a actualizar
     * @param repairs lista de reparaciones
     */
    public void loadRepairs(RepairListAdapter adapter, ArrayList<RepairJob> repairs){
        database.child("repairJobs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                repairs.removeAll(repairs);
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    RepairJob temp = new RepairJob((HashMap<String, Object>) postSnapshot.getValue());
                    if (temp.getChiefMechanic().equals(currentUser.getFullName())) {
                        repairs.add(temp);
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
            }
        });
    }

}