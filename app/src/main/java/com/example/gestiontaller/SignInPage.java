package com.example.gestiontaller;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
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

public class SignInPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        Button goTo_singUp = findViewById(R.id.goTo_signIn);
        goTo_singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoSingUp = new Intent(SignInPage.this, SingUpPage.class);
                startActivity(gotoSingUp);
                finish();
            }
        });
    }
}