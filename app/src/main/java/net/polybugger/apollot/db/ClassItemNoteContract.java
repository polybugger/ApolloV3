package net.polybugger.apollot.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    public static final String SELECT_TABLE_SQL1 = "SELECT " +
            "cin." + ClassItemNoteEntry._ID + ", " + // 0
            "cin." + ClassItemNoteEntry.CLASS_ID + ", " + // 1
            "cin." + ClassItemNoteEntry.ITEM_ID + ", " + // 2
            "cin." + ClassItemNoteEntry.NOTE + ", " + // 3
            "cin." + ClassItemNoteEntry.DATE_CREATED + // 4
            " FROM ";
    public static final String SELECT_TABLE_SQL2 = " AS cin INNER JOIN " +
            ClassContract.TABLE_NAME + " AS c ON cin." + ClassItemNoteEntry.CLASS_ID + "=c." + ClassContract.ClassEntry._ID +
            " INNER JOIN ";
    public static final String SELECT_TABLE_SQL3 = " AS ci ON cin." +
            ClassItemNoteEntry.ITEM_ID + "=ci." + ClassItemContract.ClassItemEntry._ID;

    private ClassItemNoteContract() { }

    public static long _insert(SQLiteDatabase db, long classId, long itemId, String note, Date dateCreated) {
        ContentValues values = new ContentValues();
        values.put(ClassItemNoteEntry.CLASS_ID, classId);
        values.put(ClassItemNoteEntry.ITEM_ID, itemId);
        values.put(ClassItemNoteEntry.NOTE, note);
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        values.put(ClassItemNoteEntry.DATE_CREATED, (dateCreated != null) ? sdf.format(dateCreated) : null);
        return db.insert(TABLE_NAME + String.valueOf(classId), null, values);
    }

    public static int _update(SQLiteDatabase db, long id, long classId, long itemId, String note, Date dateCreated) {
        ContentValues values = new ContentValues();
        values.put(ClassItemNoteEntry.CLASS_ID, classId);
        values.put(ClassItemNoteEntry.ITEM_ID, itemId);
        values.put(ClassItemNoteEntry.NOTE, note);
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        values.put(ClassItemNoteEntry.DATE_CREATED, (dateCreated != null) ? sdf.format(dateCreated) : null);
        return db.update(TABLE_NAME + String.valueOf(classId), values, ClassItemNoteEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static class ClassItemNoteEntry implements BaseColumns {

        public static final String CLASS_ID = "ClassId";
        public static final String ITEM_ID = "ItemId";
        public static final String NOTE = "Note";
        public static final String DATE_CREATED = "DateCreated";

    }
}
