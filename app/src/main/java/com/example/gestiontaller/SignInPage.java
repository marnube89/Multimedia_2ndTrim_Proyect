package com.example.gestiontaller;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignInPage extends AppCompatActivity {
    private FirebaseAuth database;
    private DatabaseReference realTimeDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database =FirebaseAuth.getInstance();
        realTimeDatabase = FirebaseDatabase.getInstance().getReference();


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        LinearLayout fondo = findViewById(R.id.main);
        AnimationDrawable gradient_anim = (AnimationDrawable) fondo.getBackground();
        gradient_anim.setEnterFadeDuration(2500);
        gradient_anim.setExitFadeDuration(5000);
        gradient_anim.start();
        //================================================
        //En caso de querer ocultar todos los controles visibles por el usuario en el telefono debemos introducir los dos
        //siguientes snnipets de codigo


        //Esta linea de codigo le indica al telefono que debe ocultar la barra de notificaciones, se realiza mediante el uso de
        //window flags
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Mediante el controlador de nuestra ventana y la decoracion que tiene dentro, podemos ocultar tambien los controles del usuario
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(
                getWindow(), getWindow().getDecorView()
        );
        controller.hide(WindowInsetsCompat.Type.systemBars());

        //================================================


        //GoTo pagina de registro desde pagina de inicio de sesion
        Button goTo_singUp = findViewById(R.id.goTo_signUp);
        goTo_singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoSingUp = new Intent(SignInPage.this, SingUpPage.class);
                startActivity(gotoSingUp);
                finish();
            }
        });

        Button signIn_bt = findViewById(R.id.signIn);
        TextInputEditText mail = findViewById(R.id.mail_textEdit);
        TextInputEditText pass = findViewById(R.id.pass_textEdit);
        TextInputLayout passLayout = findViewById(R.id.pass_textField);
        TextInputLayout mailLayout = findViewById(R.id.mail_textField);
        signIn_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(mail.getText().toString().isEmpty()) && !(pass.getText().toString().isEmpty())){
                    //Primero comprueba si el usuario que intenta iniciar sesion existe
                    database.signInWithEmailAndPassword(mail.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = database.getCurrentUser();
                                String uid = user.getUid();
                                //Despues tras recoger el uid del usuario que acaba de iniciar sesion, se pueden extraer los datos del usuario como hashmap
                                realTimeDatabase.child("users").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                       if(task.isSuccessful()){
                                           User userTemp = new User((HashMap) task.getResult().getValue());
                                           if(userTemp.isActiveAccount()){
                                               //Mediante esta comprobacion, que queda a modificacion, se puede comprobar el tipo de usuario que esta intentando entrar a la app para mostrarle el correspondiente menu
                                               switch(userTemp.getJobRol()){
                                                   case 0:
                                                       Intent gotoCliente = new Intent(SignInPage.this, ClientMainPage.class);
                                                       startActivity(gotoCliente);
                                                       break;
                                                   case 1:
                                                       Intent gotoAdmin = new Intent(SignInPage.this, AdminMainPage.class);
                                                       startActivity(gotoAdmin);
                                                       break;
                                                   case 2:
                                                       Intent gotoAdministrative = new Intent(SignInPage.this, AdministrativeMainPage.class);
                                                       startActivity(gotoAdministrative);
                                                       break;
                                                   case 3:
                                                       Intent gotoChiefMechanic = new Intent(SignInPage.this, MechanicChiefMainPage.class);
                                                       startActivity(gotoChiefMechanic);
                                                       break;
                                                   case 4:
                                                       Intent gotoMechanic = new Intent(SignInPage.this, MechanicMainPage.class);
                                                       startActivity(gotoMechanic);
                                                       break;
                                               }
                                           }else{
                                               Snackbar.make(findViewById(R.id.main), "Cuenta inactiva", Snackbar.LENGTH_SHORT).show();
                                           }

                                       }
                                    }

                                });

                                //Intent goToAdmin = new Intent(SignInPage.this, AdminMainPage.class);
                                //startActivity(goToAdmin);
                            }else{
                                //Si no encuentra un usuario valido mostrara el siguiente mensaje de error
                                mailLayout.setErrorEnabled(false);
                                passLayout.setError("Contraseña o usuario incorrectos");
                            }
                        }
                    });
                }else{
                    //Aqui se comprueba si los campos estan vacios para mostrar un mensaje de error correspondiente al campo que este vacio
                    if(mail.getText().toString().isEmpty()){
                        mailLayout.setError("Campo Vacio");
                    }else{
                        mailLayout.setErrorEnabled(false);
                    }
                    if(pass.getText().toString().isEmpty()){
                        passLayout.setError("Campo Vacio");
                    }else{
                        passLayout.setErrorEnabled(false);
                    }

                }

            }
        });
    }
}