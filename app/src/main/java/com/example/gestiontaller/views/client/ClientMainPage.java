package com.example.gestiontaller.views.client;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.R;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.example.gestiontaller.data_classes.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClientMainPage extends AppCompatActivity {
    private DatabaseReference database;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        currentUser = (User) getIntent().getSerializableExtra("user");

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);


    }
}