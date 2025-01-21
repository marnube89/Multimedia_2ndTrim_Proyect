package com.example.gestiontaller.graphics;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.example.gestiontaller.data_classes.User;
import com.example.gestiontaller.local_database.UserDbHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import javax.annotation.Nullable;

public class ExitDialog {
    private static UserDbHelper dbHelper;

    private ExitDialog(){}

    /**
     * Metodo que se mostrara para comfirmar el cierre de sesion de un usuario
     * @param user usuario actual
     * @param context contexto de la actividad
     */
    public static void exitDialog(@Nullable User user, Context context){

      new MaterialAlertDialogBuilder(context)
                .setTitle("Cerrar sesion")
                .setMessage("¿Estas seguro de que quieres cerrar sesion?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(user!=null){
                            dbHelper = new UserDbHelper(context);
                            dbHelper.deleteUser(user);
                        }
                        ((Activity) context).finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                   }
               }).show();
    }
}
