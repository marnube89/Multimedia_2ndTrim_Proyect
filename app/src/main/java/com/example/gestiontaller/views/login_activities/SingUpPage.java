package com.example.gestiontaller.views.login_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.graphics.CustomGraphics;
import com.example.gestiontaller.views.client.ClientMainPage;
import com.example.gestiontaller.R;
import com.example.gestiontaller.data_classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SingUpPage extends AppCompatActivity {
    private DatabaseReference realTimeDatabase;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        realTimeDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sing_up_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

        //GoTo pagina de inicio de sesion desde pagina de registro
        Button goTo_singIn = findViewById(R.id.goTo_signUp);
        goTo_singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoSingIn = new Intent(SingUpPage.this, SignInPage.class);
                startActivity(gotoSingIn);
                finish();
            }
        });

        Button signUp_bt = findViewById(R.id.signUp_button);
        TextInputEditText fullName = findViewById(R.id.fullName_field);
        TextInputLayout fullNameLayout = findViewById(R.id.fullName_textField);
        TextInputEditText mail = findViewById(R.id.mail_field);
        TextInputLayout mailLayout = findViewById(R.id.mail_textField);
        TextInputEditText tlf = findViewById(R.id.tlf_field);
        TextInputLayout tlfLayout = findViewById(R.id.tlf_textField);
        TextInputEditText pass = findViewById(R.id.pass_field);
        TextInputLayout passLayout = findViewById(R.id.pass_textField);
        TextInputEditText passConfirm = findViewById(R.id.confirmPass_field);
        TextInputLayout passConfirmLayout = findViewById(R.id.confirm_pass_textField);

        signUp_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullName.getText().toString().isEmpty() || mail.getText().toString().isEmpty() || pass.getText().toString().isEmpty() || passConfirm.getText().toString().isEmpty() || tlf.getText().toString().isEmpty()){

                    //Todo esto son comprovaciones de campos vacios y sus mensajes de error

                    if(fullName.getText().toString().isEmpty()){
                        fullNameLayout.setError("Campo vacio");
                    }else{
                        fullNameLayout.setErrorEnabled(false);
                    }

                    if(mail.getText().toString().isEmpty()){
                        mailLayout.setError("Campo vacio");
                    }else{
                        mailLayout.setErrorEnabled(false);
                    }

                    if(tlf.getText().toString().isEmpty()){
                        tlfLayout.setError("Campo vacio");
                    }else{
                        tlfLayout.setErrorEnabled(false);
                    }

                    if(pass.getText().toString().isEmpty()){
                        passLayout.setError("Campo vacio");
                    }else{
                        passLayout.setErrorEnabled(false);
                    }

                    if(passConfirm.getText().toString().isEmpty()){
                        passConfirmLayout.setError("Campo vacio");
                    }else{
                        passConfirmLayout.setErrorEnabled(false);
                    }

                }else{
                    if(pass.getText().toString().equals(passConfirm.getText().toString())){
                        //Esta creacion de usuario solo creara clientes (jobRol 0)

                        //Registro
                        //* Este registro solo sera para los clientes, el admin sera quien se encargue de registrar y modificar a los empleados
                        User newUser = new User(fullName.getText().toString(), mail.getText().toString(), Long.parseLong(tlf.getText().toString()), 0, pass.getText().toString());
                        auth.createUserWithEmailAndPassword(newUser.getMail(), newUser.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Si se consigue crear un usuario con esos credenciales se almacenaran sus datos en la base de datos
                                if(task.isSuccessful()){
                                    FirebaseUser currentNewUser = auth.getCurrentUser();
                                    newUser.setUid(currentNewUser.getUid());
                                    realTimeDatabase.child("users").child(newUser.getUid()).setValue(newUser);

                                    //Llevara al usuario directamente a la mainPage de cliente
                                    Intent goTo_client = new Intent(SingUpPage.this, ClientMainPage.class);
                                    goTo_client.putExtra("user", newUser);
                                    startActivity(goTo_client);
                                }else{
                                    Snackbar.make(findViewById(R.id.main), "Creacion de usuario fallida", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        passConfirmLayout.setError("Las contraseñas no coinciden");
                    }
                }
            }
        });
    }
}