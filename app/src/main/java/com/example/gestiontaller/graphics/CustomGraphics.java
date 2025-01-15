package com.example.gestiontaller.graphics;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.LinearLayout;

import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class CustomGraphics {

    public static void setBackgroundAnim(View view){
        LinearLayout fondo = (LinearLayout) view;
        AnimationDrawable gradient_anim = (AnimationDrawable) fondo.getBackground();
        gradient_anim.setEnterFadeDuration(2500);
        gradient_anim.setExitFadeDuration(5000);
        gradient_anim.start();
    }

    public static void hideUserControls(Activity a){
        //En caso de querer ocultar todos los controles visibles por el usuario en el telefono debemos introducir los dos
        //siguientes snnipets de codigo

        //Mediante el controlador de nuestra ventana y la decoracion que tiene dentro, podemos ocultar tambien los controles del usuario
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(
                a.getWindow(), a.getWindow().getDecorView()
        );
        controller.hide(WindowInsetsCompat.Type.navigationBars());
    }
}
