package net.polybugger.apollot.db;

import android.provider.BaseColumns;

public class ClassPasswordContract {

    public static final String TABLE_NAME = "ClassPasswords";
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            ClassPasswordEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassPasswordEntry.CLASS_ID + " INTEGER NOT NULL UNIQUE REFERENCES " +
                ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + "), " +
            ClassPasswordEntry.PASSWORD + " TEXT NOT NULL)";

    private ClassPasswordContract() { }

    public static class ClassPasswordEntry implements BaseColumns {

        public static final String CLASS_ID = "ClassId";
        public static final String PASSWORD = "Password";

    }
}
