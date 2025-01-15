package com.example.gestiontaller;

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

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChiefMechanic_CheckAsignedRepairs extends AppCompatActivity {
    private DatabaseReference database;
    private FirebaseUser currentUid;
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

        currentUid = getIntent().getParcelableExtra("user");
        Log.i("User2", currentUid.getUid());

        ListView assignedRepairs = findViewById(R.id.assignedRepairs);
        ArrayList<RepairJob> repairs = new ArrayList<RepairJob>();
        RepairListAdapter adapter = new RepairListAdapter(ChiefMechanic_CheckAsignedRepairs.this, repairs);
        assignedRepairs.setAdapter(adapter);

        database.child("users").child(currentUid.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful() && task.getResult().getValue() != null) {
                    currentUser = new User((HashMap<String, Object>) task.getResult().getValue());

                    // Solo añade el ValueEventListener después de inicializar currentUser
                    database.child("repairJobs").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            repairs.clear();
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
                } else {
                    //Por si quieres hacer algo o mostrar un mensaje de error, aunque ya está controlado el sincronismo
                    // puede darse error en la comunicación con Firebase

                }
            }
        });

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

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Info", "pausado");
        finish();
    }

}