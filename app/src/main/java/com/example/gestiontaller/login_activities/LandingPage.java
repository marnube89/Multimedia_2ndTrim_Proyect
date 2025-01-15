package com.example.gestiontaller.login_activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.gestiontaller.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LandingPage extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(currentUser!=null){
            Log.e("firebase", "usuarioNo");
        }else{
            Log.v("firebase", "usuarioSi");
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing_page);
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

        Button signIn_bt = findViewById(R.id.signIn_button);
        signIn_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn_Activity();
            }
        });

        Button singUp_bt = findViewById(R.id.signUp_button);
        singUp_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singUp_Activity();
            }
        });

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


    }

    private void signIn_Activity(){
        Intent signIn = new Intent(LandingPage.this, SignInPage.class);
        startActivity(signIn);
    }

    private void singUp_Activity(){
        Intent singUp = new Intent(LandingPage.this, SingUpPage.class);
        startActivity(singUp);
    }
}