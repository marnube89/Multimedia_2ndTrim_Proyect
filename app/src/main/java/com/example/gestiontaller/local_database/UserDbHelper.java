package com.example.gestiontaller.local_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gestiontaller.data_classes.User;

public class UserDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "currentUser";


    public UserDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + UserContract.UserEntry.TABLE_NAME + " ("
                + UserContract.UserEntry._USER_UID + " TEXT PRIMARY KEY,"
                + UserContract.UserEntry.USER_FULL_NAME + " TEXT NOT NULL,"
                + UserContract.UserEntry.USER_MAIL + " TEXT NOT NULL,"
                + UserContract.UserEntry.USER_PASS + " TEXT NOT NULL,"
                + UserContract.UserEntry.USER_TLF + " LONG NOT NULL,"
                + UserContract.UserEntry.USER_JOB_ROL + " INTEGER NOT NULL)");

    }

    /**
     * Guardara el usuario que acaba de iniciar sesion e la base de datos local
     * @param user usuario a guardar
     */
    public void saveCurrentUser(User user){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contents = new ContentValues();
        contents.put(UserContract.UserEntry._USER_UID, user.getUid());
        contents.put(UserContract.UserEntry.USER_FULL_NAME,  user.getFullName());
        contents.put(UserContract.UserEntry.USER_JOB_ROL, user.getJobRol());
        contents.put(UserContract.UserEntry.USER_MAIL, user.getMail());
        contents.put(UserContract.UserEntry.USER_PASS, user.getPassword());
        contents.put(UserContract.UserEntry.USER_TLF, user.getTlfNumber());

        db.insert(UserContract.UserEntry.TABLE_NAME, null, contents);
    }

    /**
     * Cargara el usuario que este guardado en la base de datos para iniciar sesion con el
     * @return usuario almacenado
     */
    public User loadUser(){
        User user = null;
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + UserContract.UserEntry.TABLE_NAME, null);

        if(c.moveToNext()){
            user = new User();
            user.setUid(c.getString(0));
            user.setFullName(c.getString(1));
            user.setMail(c.getString(2));
            user.setPassword(c.getString(3));
            user.setTlfNumber(c.getLong(4));
            user.setJobRol(c.getInt(5));
        }

        return user;
    }

    /**
     * Borrara al usuario que le pasemos de la base de datos local
     * @param user usuario a borrar
     */
    public void deleteUser(User user){
        SQLiteDatabase db = getReadableDatabase();
        String where = UserContract.UserEntry._USER_UID + " = ?";
        String[] args = {user.getUid()};

        int rows = db.delete(UserContract.UserEntry.TABLE_NAME, where, args);
        Log.i("Rows" , Integer.toString(rows));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
