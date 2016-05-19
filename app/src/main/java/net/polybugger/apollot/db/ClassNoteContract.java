package net.polybugger.apollot.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.StyleableRes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ClassNoteContract {

    public static final String TABLE_NAME = "ClassNotes";
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            ClassNoteEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassNoteEntry.CLASS_ID + " INTEGER NOT NULL REFERENCES " +
                ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + ") ON DELETE CASCADE, " +
            ClassNoteEntry.NOTE + " TEXT NOT NULL, " +
            ClassNoteEntry.DATE_CREATED + " TEXT NOT NULL)";
    public static final String SELECT_TABLE_SQL = "SELECT " +
            ClassNoteEntry._ID + ", " + // 0
            ClassNoteEntry.CLASS_ID + ", " + // 1
            ClassNoteEntry.NOTE + ", " + // 2
            ClassNoteEntry.DATE_CREATED + // 3
            " FROM " + TABLE_NAME;
    public static final String DELETE_ALL_SQL = "DELETE FROM " + TABLE_NAME;

    private ClassNoteContract() { }

    public static long _insert(SQLiteDatabase db, long classId, String note, Date dateCreated) {
        ContentValues values = new ContentValues();
        values.put(ClassNoteEntry.CLASS_ID, classId);
        values.put(ClassNoteEntry.NOTE, note);
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        values.put(ClassNoteEntry.DATE_CREATED, (dateCreated != null) ? sdf.format(dateCreated) : null);
        return db.insert(TABLE_NAME, null, values);
    }

    public static int _update(SQLiteDatabase db, long id, long classId, String note, Date dateCreated) {
        ContentValues values = new ContentValues();
        values.put(ClassNoteEntry.CLASS_ID, classId);
        values.put(ClassNoteEntry.NOTE, note);
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        values.put(ClassNoteEntry.DATE_CREATED, (dateCreated != null) ? sdf.format(dateCreated) : null);
        return db.update(TABLE_NAME, values, ClassNoteEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static int _delete(SQLiteDatabase db, long id) {
        return db.delete(TABLE_NAME, ClassNoteEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static ClassNoteEntry _getEntry(SQLiteDatabase db, long id) {
        ClassNoteEntry entry = null;
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        Date dateCreated;
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{ClassNoteEntry._ID, // 0
                        ClassNoteEntry.CLASS_ID, // 1
                        ClassNoteEntry.NOTE, // 2
                        ClassNoteEntry.DATE_CREATED}, // 3
                ClassNoteEntry._ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            try {
                dateCreated = sdf.parse(cursor.getString(3));
            }
            catch(Exception e) {
                dateCreated = null;
            }
            entry = new ClassNoteEntry(cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2),
                    dateCreated);
        }
        cursor.close();
        return entry;
    }

    public static ArrayList<ClassNoteEntry> _getEntries(SQLiteDatabase db) {
        ArrayList<ClassNoteEntry> entries = new ArrayList<>();
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        Date dateCreated;
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{ClassNoteEntry._ID, // 0
                        ClassNoteEntry.CLASS_ID, // 1
                        ClassNoteEntry.NOTE, // 2
                        ClassNoteEntry.DATE_CREATED}, // 3
                null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            try {
                dateCreated = sdf.parse(cursor.getString(3));
            }
            catch(Exception e) {
                dateCreated = null;
            }
            entries.add(new ClassNoteEntry(cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2),
                    dateCreated));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public static ArrayList<ClassNoteEntry> _getEntriesByClassId(SQLiteDatabase db, long classId) {
        ArrayList<ClassNoteEntry> entries = new ArrayList<>();
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        Date dateCreated;
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{ClassNoteEntry._ID, // 0
                        ClassNoteEntry.CLASS_ID, // 1
                        ClassNoteEntry.NOTE, // 2
                        ClassNoteEntry.DATE_CREATED}, // 3
                ClassNoteEntry.CLASS_ID + "=?",
                new String[]{String.valueOf(classId)},
                null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            try {
                dateCreated = sdf.parse(cursor.getString(3));
            }
            catch(Exception e) {
                dateCreated = null;
            }
            entries.add(new ClassNoteEntry(cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2),
                    dateCreated));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public static void _insertDummyClassNote(SQLiteDatabase db, long classId, int classNoteResourceId, Context context) {
        @StyleableRes final int NOTE_INDEX = 0;
        @StyleableRes final int DATE_CREATED_INDEX = 1;
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_TIME_DB_TEMPLATE, context.getResources().getConfiguration().locale);
        Resources res = context.getResources();
        TypedArray ta = res.obtainTypedArray(classNoteResourceId);
        String note = res.getString(ta.getResourceId(NOTE_INDEX, 0));
        Date dateCreated;
        try {
            dateCreated = sdf.parse(res.getString(ta.getResourceId(DATE_CREATED_INDEX, 0)));
        }
        catch(Exception e) {
            dateCreated = null;
        }
        ta.recycle();
        _insert(db, classId, note, dateCreated);
    }

    public static class ClassNoteEntry implements BaseColumns {

        public static final String CLASS_ID = "ClassId";
        public static final String NOTE = "Note";
        public static final String DATE_CREATED = "DateCreated";

        private long mId; // 0
        private long mClassId; // 1
        private String mNote; // 2
        private Date mDateCreated; // 3

        public ClassNoteEntry(long id, long classId, String note, Date dateCreated) {
            mId = id;
            mClassId = classId;
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

            StringBuilder note = new StringBuilder(sdf.format(mDateCreated));
            note.append(" - ");
            note.append(mNote);
            return note.toString();
        }

        @Override
        public boolean equals(Object object) {
            if(!(object instanceof ClassNoteEntry))
                return false;
            if(object == this)
                return true;
            ClassNoteEntry entry = (ClassNoteEntry) object;
            return new EqualsBuilder()
                    .append(mId, entry.mId).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(31, 37)
                    .append(mId).toHashCode();
        }
    }
}
