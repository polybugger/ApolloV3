package net.polybugger.apollot.db;

import android.provider.BaseColumns;

public class ClassItemTypeContract {

    public static String TABLE_NAME = "ClassItemTypes";
    public static String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            ClassItemTypeEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassItemTypeEntry.DESCRIPTION + " TEXT NOT NULL, " +
            ClassItemTypeEntry.COLOR + " TEXT NULL)";

    private ClassItemTypeContract() { }

    public static class ClassItemTypeEntry implements BaseColumns {

        public static String DESCRIPTION = "Description";
        public static String COLOR = "Color";

    }
}
