package net.polybugger.apollot.db;

import android.provider.BaseColumns;

public class ClassNoteContract {

    public static final String TABLE_NAME = "ClassNotes";
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            ClassNoteEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassNoteEntry.CLASS_ID + " INTEGER NOT NULL REFERENCES " +
                ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + "), " +
            ClassNoteEntry.NOTE + " TEXT NOT NULL, " +
            ClassNoteEntry.DATE_CREATED + " TEXT NOT NULL)";

    private ClassNoteContract() { }

    public static class ClassNoteEntry implements BaseColumns {

        public static final String CLASS_ID = "ClassId";
        public static final String NOTE = "Note";
        public static final String DATE_CREATED = "DateCreated";

    }

}
