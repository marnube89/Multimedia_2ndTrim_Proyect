package com.example.gestiontaller.views.client;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.R;
import com.example.gestiontaller.data_classes.RepairJob;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.google.android.material.textfield.TextInputEditText;

public class Client_RepairDetails extends AppCompatActivity {
    private RepairJob selectedRepair;

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

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        selectedRepair = (RepairJob) getIntent().getSerializableExtra("repair");

        TextInputEditText date = findViewById(R.id.dateContent);
        date.setText(selectedRepair.getStartDate());

        TextInputEditText licensePlate = findViewById(R.id.licensePlateContent);
        licensePlate.setText(selectedRepair.getCar());

        TextInputEditText repairDesc = findViewById(R.id.descContent);
        repairDesc.setText(selectedRepair.getDescription());

        TextInputEditText repairDiagnostic = findViewById(R.id.diagnosticContent);
        repairDiagnostic.setText(selectedRepair.getDiagnostic());
    }
}