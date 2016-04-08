package net.polybugger.apollot.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ClassNoteContract {

    public static final String TABLE_NAME = "ClassNotes";
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            ClassNoteEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassNoteEntry.CLASS_ID + " INTEGER NOT NULL REFERENCES " +
                ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + "), " +
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

    public static long insert(long classId, String note, Date dateCreated) {
        SQLiteDatabase db = ApolloDbAdapter.open();
        long id = _insert(db, classId, note, dateCreated);
        ApolloDbAdapter.close();
        return id;
    }

    public static int _update(SQLiteDatabase db, long id, long classId, String note, Date dateCreated) {
        ContentValues values = new ContentValues();
        values.put(ClassNoteEntry.CLASS_ID, classId);
        values.put(ClassNoteEntry.NOTE, note);
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        values.put(ClassNoteEntry.DATE_CREATED, (dateCreated != null) ? sdf.format(dateCreated) : null);
        return db.update(TABLE_NAME, values, ClassNoteEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static int update(long id, long classId, String note, Date dateCreated) {
        SQLiteDatabase db = ApolloDbAdapter.open();
        int rowsUpdated = _update(db, id, classId, note, dateCreated);
        ApolloDbAdapter.close();
        return rowsUpdated;
    }

    public static int _delete(SQLiteDatabase db, long id) {
        return db.delete(TABLE_NAME, ClassNoteEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static int delete(long id) {
        SQLiteDatabase db = ApolloDbAdapter.open();
        int rowsDeleted = _delete(db, id);
        ApolloDbAdapter.close();
        return rowsDeleted;
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

    public static ClassNoteEntry getEntry(long id) {
        SQLiteDatabase db = ApolloDbAdapter.open();
        ClassNoteEntry entry = _getEntry(db, id);
        ApolloDbAdapter.close();
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

    public static ArrayList<ClassNoteEntry> getEntries() {
        SQLiteDatabase db = ApolloDbAdapter.open();
        ArrayList<ClassNoteEntry> entries = _getEntries(db);
        ApolloDbAdapter.close();
        return entries;
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
