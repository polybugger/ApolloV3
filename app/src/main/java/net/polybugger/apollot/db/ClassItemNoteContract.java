package net.polybugger.apollot.db;

import android.provider.BaseColumns;

public class ClassItemNoteContract {

    public static final String TABLE_NAME = "ClassItemNotes_";
    public static final String CREATE_TABLE_SQL1 = "CREATE TABLE IF NOT EXISTS ";
    public static final String CREATE_TABLE_SQL2 = " (" +
            ClassItemNoteEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassItemNoteEntry.CLASS_ID + " INTEGER NOT NULL REFERENCES " +
                ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + "), " +
            ClassItemNoteEntry.ITEM_ID + " INTEGER NOT NULL REFERENCES ";
    public static final String CREATE_TABLE_SQL3 = " (" + ClassItemContract.ClassItemEntry._ID + "), " +
            ClassItemNoteEntry.NOTE + " TEXT NOT NULL, " +
            ClassItemNoteEntry.DATE_CREATED + " TEXT NOT NULL)";

    private ClassItemNoteContract() { }

    public static class ClassItemNoteEntry implements BaseColumns {

        public static final String CLASS_ID = "ClassId";
        public static final String ITEM_ID = "ItemId";
        public static final String NOTE = "Note";
        public static final String DATE_CREATED = "DateCreated";

    }
}
