package com.example.gestiontaller.views.client;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.R;
import com.example.gestiontaller.data_classes.User;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Client_Profile extends AppCompatActivity {
    private User current;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        current = (User) getIntent().getSerializableExtra("user");

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        TextInputEditText name = findViewById(R.id.nameContent);
        TextInputLayout nameLayout = findViewById(R.id.name_textField);
        name.setText(current.getFullName());
        TextInputEditText tlf = findViewById(R.id.phoneContent);
        TextInputLayout tlfLayout = findViewById(R.id.tlf_textField);
        tlf.setText(Long.toString(current.getTlfNumber()));
        TextInputEditText mail = findViewById(R.id.mailContent);
        mail.setText(current.getMail());
        TextInputEditText pass = findViewById(R.id.passContent);
        pass.setText(current.getPassword());

        AppCompatButton save = findViewById(R.id.saveBt);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!name.getText().toString().isEmpty() && !tlf.getText().toString().isEmpty()){
                    current.setFullName(name.getText().toString());
                    current.setTlfNumber(Long.parseLong(tlf.getText().toString()));

                    database.child("users").child(current.getUid()).setValue(current);
                }else{
                    if(name.getText().toString().isEmpty()){
                        nameLayout.setError("Campo Vacio");
                    }else{
                        nameLayout.setErrorEnabled(false);
                    }
                    if(tlf.getText().toString().isEmpty()){
                        tlfLayout.setError("Campo Vacio");
                    }else{
                        tlfLayout.setErrorEnabled(false);
                    }
                }
            }
        });



    }
}