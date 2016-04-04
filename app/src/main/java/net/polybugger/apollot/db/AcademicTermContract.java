package net.polybugger.apollot.db;

import android.provider.BaseColumns;

public class AcademicTermContract {

    public static final String TABLE_NAME = "AcademicTerms";
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            AcademicTermEntry._ID + " INTEGER PRIMARY KEY, " +
            AcademicTermEntry.DESCRIPTION + " TEXT NOT NULL, " +
            AcademicTermEntry.COLOR + " TEXT NULL)";

    private AcademicTermContract() { }

    public static class AcademicTermEntry implements BaseColumns {

        public static String DESCRIPTION = "Description";
        public static String COLOR = "Color";

    }

}

