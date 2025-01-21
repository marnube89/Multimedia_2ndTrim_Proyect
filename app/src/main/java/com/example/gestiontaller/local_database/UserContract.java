package com.example.gestiontaller.local_database;

import android.provider.BaseColumns;

public class UserContract {
    private UserContract(){}

    public static abstract class UserEntry implements BaseColumns{
        public static final String TABLE_NAME = "users";

        public static final String _USER_UID = "uid";
        public static final String USER_FULL_NAME = "fullName";
        public static final String USER_MAIL = "mail";
        public static final String USER_TLF = "tlfNumber";
        public static final String USER_JOB_ROL = "jobRol";
        public static final String USER_PASS = "password";
        public static final String ACTIVE_ACCOUNT = "activeAccount";
    }
}
