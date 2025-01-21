package com.example.gestiontaller.views.login_activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestiontaller.R;
import com.example.gestiontaller.data_classes.User;
import com.example.gestiontaller.graphics.CustomGraphics;
import com.example.gestiontaller.local_database.UserDbHelper;
import com.example.gestiontaller.views.admin.AdminMainPage;
import com.example.gestiontaller.views.administrative.AdministrativeMainPage;
import com.example.gestiontaller.views.chief_mechanic.MechanicChiefMainPage;
import com.example.gestiontaller.views.client.ClientMainPage;
import com.example.gestiontaller.views.mechanic.MechanicMainPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LandingPage extends AppCompatActivity {
    private UserDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbHelper = new UserDbHelper(this);
        User current = dbHelper.loadUser();
        if(current!=null){
            currentUserLogin(current);
        }

        Button signIn_bt = findViewById(R.id.signIn_button);
        signIn_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //inicio de sesion
                signIn_Activity();
            }
        });

        Button singUp_bt = findViewById(R.id.signUp_button);
        singUp_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //registro
                singUp_Activity();
            }
        });

        CustomGraphics.setBackgroundAnim(findViewById(R.id.main));
        CustomGraphics.hideUserControls(this);

    }

    /**
     * Te lleva al inicio de sesion
     */
    private void signIn_Activity(){
        Intent signIn = new Intent(LandingPage.this, SignInPage.class);
        startActivity(signIn);
    }

    /**
     * Te lleva al registro
     */
    private void singUp_Activity(){
        Intent singUp = new Intent(LandingPage.this, SingUpPage.class);
        startActivity(singUp);
    }

    /**
     * Mandara al usuario a su correspondiente menu principal
     * @param userTemp datos del usuario
     */
    private void currentUserLogin(User userTemp){
        //Mediante esta comprobacion, que queda a modificacion, se puede comprobar el tipo de usuario que esta intentando entrar a la app para mostrarle el correspondiente menu
        switch(userTemp.getJobRol()){
            case 0:
                Intent gotoCliente = new Intent(LandingPage.this, ClientMainPage.class);
                gotoCliente.putExtra("user",userTemp);
                startActivity(gotoCliente);
                break;
            case 1:
                Intent gotoAdmin = new Intent(LandingPage.this, AdminMainPage.class);
                gotoAdmin.putExtra("user",userTemp);
                startActivity(gotoAdmin);
                break;
            case 2:
                Intent gotoAdministrative = new Intent(LandingPage.this, AdministrativeMainPage.class);
                gotoAdministrative.putExtra("user",userTemp);
                startActivity(gotoAdministrative);
                break;
            case 3:
                Intent gotoChiefMechanic = new Intent(LandingPage.this, MechanicChiefMainPage.class);
                gotoChiefMechanic.putExtra("user",userTemp);
                startActivity(gotoChiefMechanic);
                break;
            case 4:
                Intent gotoMechanic = new Intent(LandingPage.this, MechanicMainPage.class);
                gotoMechanic.putExtra("user",userTemp);
                startActivity(gotoMechanic);
                break;
        }
    }
}