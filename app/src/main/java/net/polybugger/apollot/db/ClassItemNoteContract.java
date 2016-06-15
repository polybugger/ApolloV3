package net.polybugger.apollot.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClassItemNoteContract {

    public static final String TABLE_NAME = "ClassItemNotes_";
    public static final String CREATE_TABLE_SQL1 = "CREATE TABLE IF NOT EXISTS ";
    public static final String CREATE_TABLE_SQL2 = " (" +
            ClassItemNoteEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassItemNoteEntry.CLASS_ID + " INTEGER NOT NULL REFERENCES " +
                ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + ") ON DELETE CASCADE, " +
            ClassItemNoteEntry.ITEM_ID + " INTEGER NOT NULL REFERENCES ";
    public static final String CREATE_TABLE_SQL3 = " (" + ClassItemContract.ClassItemEntry._ID + ") ON DELETE CASCADE, " +
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
        String tableName1 = TABLE_NAME + String.valueOf(classId);
        String tableName2 = ClassItemContract.TABLE_NAME + String.valueOf(classId);
        db.execSQL(CREATE_TABLE_SQL1 + tableName1 + CREATE_TABLE_SQL2 + tableName2 + CREATE_TABLE_SQL3);
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
        //values.put(ClassItemNoteEntry.CLASS_ID, classId);
        //values.put(ClassItemNoteEntry.ITEM_ID, itemId);
        values.put(ClassItemNoteEntry.NOTE, note);
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        values.put(ClassItemNoteEntry.DATE_CREATED, (dateCreated != null) ? sdf.format(dateCreated) : null);
        return db.update(TABLE_NAME + String.valueOf(classId), values, ClassItemNoteEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static int _delete(SQLiteDatabase db, long id, long classId) {
        return db.delete(TABLE_NAME + String.valueOf(classId), ClassItemNoteEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static ArrayList<ClassItemNoteEntry> _getEntriesByClassId(SQLiteDatabase db, long classId) {
        String tableName1 = TABLE_NAME + String.valueOf(classId);
        String tableName2 = ClassItemContract.TABLE_NAME + String.valueOf(classId);
        db.execSQL(CREATE_TABLE_SQL1 + tableName1 + CREATE_TABLE_SQL2 + tableName2 + CREATE_TABLE_SQL3);

        ArrayList<ClassItemNoteEntry> entries = new ArrayList<>();
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        Date dateCreated;
        Cursor cursor = db.query(tableName1,
                new String[]{ClassItemNoteEntry._ID, // 0
                        ClassItemNoteEntry.CLASS_ID, // 1
                        ClassItemNoteEntry.ITEM_ID, // 2
                        ClassItemNoteEntry.NOTE, // 3
                        ClassItemNoteEntry.DATE_CREATED}, // 4
                ClassItemNoteEntry.CLASS_ID + "=?",
                new String[]{String.valueOf(classId)},
                null, null, "date(" + ClassItemNoteEntry.DATE_CREATED + ") DESC");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            try {
                dateCreated = sdf.parse(cursor.getString(4));
            }
            catch(Exception e) {
                dateCreated = null;
            }
            entries.add(new ClassItemNoteEntry(cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getLong(2),
                    cursor.getString(3),
                    dateCreated));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public static class ClassItemNoteEntry implements BaseColumns {

        public static final String CLASS_ID = "ClassId";
        public static final String ITEM_ID = "ItemId";
        public static final String NOTE = "Note";
        public static final String DATE_CREATED = "DateCreated";

        private long mId; // 0
        private long mClassId; // 1
        private long mClassItemId; // 2
        private String mNote; // 3
        private Date mDateCreated; // 4

        public ClassItemNoteEntry(long id, long classId, long classItemId, String note, Date dateCreated) {
            mId = id;
            mClassId = classId;
            mClassItemId = classItemId;
            mNote = note;
            mDateCreated = dateCreated;
        }

        public long getId() {
            return mId;
        }

        public void setId(long id) {
            mId = id;
        }

        public long getClassId() {
            return mClassId;
        }

        public void setClassId(long classId) {
            mClassId = classId;
        }

        public long getClassItemId() {
            return mClassItemId;
        }

        public void setClassItemId(long classItemId) {
            mClassItemId = classItemId;
        }

        public String getNote() {
            return mNote;
        }

        public void setNote(String note) {
            mNote = note;
        }

        public Date getDateCreated() {
            return mDateCreated;
        }

        public void setDateCreated(Date dateCreated) {
            mDateCreated = dateCreated;
        }

        public String getDateCreatedNote(Context context) {
            final SimpleDateFormat sdf;
            if(StringUtils.equalsIgnoreCase(context.getResources().getConfiguration().locale.getLanguage(), ApolloDbAdapter.JA_LANGUAGE))
                sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE_JA, context.getResources().getConfiguration().locale);
            else
                sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE, context.getResources().getConfiguration().locale);

            StringBuilder note = new StringBuilder("<b>");
            note.append(sdf.format(mDateCreated));
            note.append("</b>");
            note.append(" - ");
            note.append(mNote);
            return note.toString();
        }

        @Override
        public boolean equals(Object object) {
            if(!(object instanceof ClassItemNoteEntry))
                return false;
            if(object == this)
                return true;
            ClassItemNoteEntry entry = (ClassItemNoteEntry) object;
            return new EqualsBuilder()
                    .append(mId, entry.mId).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(83, 89)
                    .append(mId).toHashCode();
        }
    }
}
