package com.example.gestiontaller.chief_mechanic;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.R;
import com.example.gestiontaller.aux_classes.CustomGraphics;
import com.example.gestiontaller.aux_classes.RepairJob;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChiefMechanic_Add_Complete_Diagnostic extends AppCompatActivity {
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chief_mechanic_add_complete_diagnostic);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        RepairJob data = (RepairJob) getIntent().getSerializableExtra("repair");

        TextView repairTittle = findViewById(R.id.repairNumberTittle);
        repairTittle.setText("Reparacion Nº: " + data.getRepairNumber());
        TextView licensePlate = findViewById(R.id.licensePlate);
        licensePlate.setText(data.getCar());
        TextView date = findViewById(R.id.date);
        date.setText(data.getStartDate());
        TextInputEditText desc = findViewById(R.id.description);
        desc.setText(data.getDescription());
        TextInputEditText diagnostic = findViewById(R.id.diagnostic);

        if(!data.getDiagnostic().isEmpty()){
            diagnostic.setText(data.getDiagnostic());
        }

        AppCompatButton addDiagnostic = findViewById(R.id.confirm);
        View view = findViewById(R.id.main);
        addDiagnostic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(diagnostic.getText().toString().isEmpty())){
                    data.setDiagnostic(diagnostic.getText().toString());
                    database.child("repairJobs").child(data.getRepairNumber()).setValue(data);
                    finish();
                }else{
                    Snackbar.make(view, "Introduce un diagnostico", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        AppCompatButton finishRepair = findViewById(R.id.complete);
        finishRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(diagnostic.getText().toString().isEmpty())){
                    database.child("repairJobs").child(data.getRepairNumber()).removeValue();
                    finish();
                }else{
                    Snackbar.make(view, "No se puede completar una reparacion sin diagnostico", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        AppCompatButton cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}