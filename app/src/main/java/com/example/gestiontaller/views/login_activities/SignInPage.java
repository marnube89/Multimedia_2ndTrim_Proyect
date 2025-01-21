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
import com.example.gestiontaller.local_database.UserDbHelper;
import com.example.gestiontaller.views.client.ClientMainPage;
import com.example.gestiontaller.R;
import com.example.gestiontaller.views.admin.AdminMainPage;
import com.example.gestiontaller.views.administrative.AdministrativeMainPage;
import com.example.gestiontaller.data_classes.User;
import com.example.gestiontaller.views.chief_mechanic.MechanicChiefMainPage;
import com.example.gestiontaller.views.mechanic.MechanicMainPage;
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

public class SignInPage extends AppCompatActivity {
    private FirebaseAuth database;
    private DatabaseReference realTimeDatabase;
    private UserDbHelper dbHelper;

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

        dbHelper = new UserDbHelper(this);

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);


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

                                //Tras recoger el uid del usuario que acaba de iniciar sesion, se pueden extraer los datos del usuario como hashmap
                                userDataFetch(uid);
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

    /**
     * Recogera los datos del usuario con la uid pasada y despues llamara a userSelector()
     * @param uid identificador del usuario
     */
    private void userDataFetch(String uid){
        realTimeDatabase.child("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    User userTemp = new User((HashMap) task.getResult().getValue());

                    //Comprueba si la cuenta esta inavilitada o no
                    if(userTemp.isActiveAccount()){
                        //Guarda al usuario en la base de datos local
                        dbHelper.saveCurrentUser(userTemp);

                        //Te llevara a la main page correspondiente
                        userSelector(userTemp);
                    }else{
                        Snackbar.make(findViewById(R.id.main), "Cuenta inactiva", Snackbar.LENGTH_SHORT).show();
                    }

                }
            }

        });
    }

    /**
     * Llevara al usuario a su correspondiente menu principal
     * @param userTemp datos del usuario, estos se pasaran a cada menu para sus comprovaciones de datos
     */
    private void userSelector(User userTemp){
        //Mediante esta comprobacion, que queda a modificacion, se puede comprobar el tipo de usuario que esta intentando entrar a la app para mostrarle el correspondiente menu
        switch(userTemp.getJobRol()){
            case 0:
                Intent gotoCliente = new Intent(SignInPage.this, ClientMainPage.class);
                gotoCliente.putExtra("user",userTemp);
                startActivity(gotoCliente);
                break;
            case 1:
                Intent gotoAdmin = new Intent(SignInPage.this, AdminMainPage.class);
                gotoAdmin.putExtra("user",userTemp);
                startActivity(gotoAdmin);
                break;
            case 2:
                Intent gotoAdministrative = new Intent(SignInPage.this, AdministrativeMainPage.class);
                gotoAdministrative.putExtra("user",userTemp);
                startActivity(gotoAdministrative);
                break;
            case 3:
                Intent gotoChiefMechanic = new Intent(SignInPage.this, MechanicChiefMainPage.class);
                gotoChiefMechanic.putExtra("user",userTemp);
                startActivity(gotoChiefMechanic);
                break;
            case 4:
                Intent gotoMechanic = new Intent(SignInPage.this, MechanicMainPage.class);
                gotoMechanic.putExtra("user",userTemp);
                startActivity(gotoMechanic);
                break;
        }
    }
}