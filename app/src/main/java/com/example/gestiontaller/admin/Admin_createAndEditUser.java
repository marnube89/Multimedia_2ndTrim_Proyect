package com.example.gestiontaller.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.R;
import com.example.gestiontaller.aux_classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_createAndEditUser extends AppCompatActivity {
private DatabaseReference database;
private FirebaseAuth auth;
private FirebaseUser admin;
private boolean isModding = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        admin = auth.getCurrentUser();
        Intent temp = getIntent();
        isModding = temp.getBooleanExtra("isModding", false);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_create_and_edit_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        TextInputEditText fullname = findViewById(R.id.fullName_TextField);
        TextInputEditText mail = findViewById(R.id.mail_TextField);
        TextInputEditText tlf = findViewById(R.id.tlf_TextField);
        TextInputEditText pass = findViewById(R.id.pass_TextField);
        AutoCompleteTextView rol = findViewById(R.id.jobRol_Text);



        if(isModding){
            Intent data = getIntent();
            User dataRetrieved = (User) data.getSerializableExtra("user");
            fullname.setText(dataRetrieved.getFullName());
            mail.setText(dataRetrieved.getMail());
            tlf.setText(dataRetrieved.getTlfNumber().toString());
            pass.setText(dataRetrieved.getPassword());
            if(dataRetrieved.getJobRol()==2){
                rol.setText("Administrativo");
            } else if (dataRetrieved.getJobRol()==3) {
                rol.setText("Mecanico");
            } else if (dataRetrieved.getJobRol()==4) {
                rol.setText("Jefe Mecanico");
            }
        }

        AppCompatButton confirm = findViewById(R.id.addNewUserBt);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer rolNumber = 2;
                if(rol.getText().toString().equals("Mecanico")){
                    rolNumber = 4;
                }else if(rol.getText().toString().equals("Jefe Mecanico")){
                    rolNumber = 3;
                }

                if(!(fullname.getText().toString().isEmpty()) && !(mail.getText().toString().isEmpty()) && !(tlf.getText().toString().isEmpty()) && !(pass.getText().toString().isEmpty()) && !(rol.getText().toString().isEmpty())){

                    if(!isModding){
                        User userTemp = new User(fullname.getText().toString(), mail.getText().toString(), Long.parseLong(tlf.getText().toString()), rolNumber, pass.getText().toString());
                        auth.createUserWithEmailAndPassword(userTemp.getMail(), userTemp.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    userTemp.setUid(auth.getCurrentUser().getUid());
                                    database.child("users").child(userTemp.getUid()).setValue(userTemp);
                                    //auth.updateCurrentUser(admin);
                                }else{
                                    Snackbar.make(findViewById(R.id.main), "mal mal", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else{

                        User moddedUser = new User(fullname.getText().toString(), mail.getText().toString(), Long.parseLong(tlf.getText().toString()), rolNumber, pass.getText().toString());
                        User userTemp = (User) temp.getSerializableExtra("user");
                        database.child("users").child(userTemp.getUid()).setValue(moddedUser);


                    }

                }else{
                    //no tengo tiempo
                }
            }
        });
        AppCompatButton cancel = findViewById(R.id.cancelOperation);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

}