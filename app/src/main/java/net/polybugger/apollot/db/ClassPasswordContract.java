package net.polybugger.apollot.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import net.polybugger.apollot.R;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ClassPasswordContract {

    public static final String TABLE_NAME = "ClassPasswords";
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            ClassPasswordEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassPasswordEntry.CLASS_ID + " INTEGER NOT NULL UNIQUE REFERENCES " +
                ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + "), " +
            ClassPasswordEntry.PASSWORD + " TEXT NOT NULL)";
    public static final String SELECT_TABLE_SQL = "SELECT " +
            ClassPasswordEntry._ID + ", " + // 0
            ClassPasswordEntry.CLASS_ID + ", " + // 1
            ClassPasswordEntry.PASSWORD + // 2
            " FROM " + TABLE_NAME;
    public static final String DELETE_ALL_SQL = "DELETE FROM " + TABLE_NAME;

    private ClassPasswordContract() { }

    public static long _insert(SQLiteDatabase db, long classId, String password) {
        ContentValues values = new ContentValues();
        values.put(ClassPasswordEntry.CLASS_ID, classId);
        values.put(ClassPasswordEntry.PASSWORD, password);
        return db.insert(TABLE_NAME, null, values);
    }

    public static int _delete(SQLiteDatabase db, long id) {
        return db.delete(TABLE_NAME, ClassPasswordEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static int _deleteByClassId(SQLiteDatabase db, long classId) {
        return db.delete(TABLE_NAME, ClassPasswordEntry.CLASS_ID + "=?", new String[]{String.valueOf(classId)});
    }

    public static ClassPasswordEntry _getEntry(SQLiteDatabase db, long id) {
        ClassPasswordEntry entry = null;
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{ClassPasswordEntry._ID, // 0
                        ClassPasswordEntry.CLASS_ID, // 1
                        ClassPasswordEntry.PASSWORD}, // 2
                ClassPasswordEntry._ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast())
            entry = new ClassPasswordEntry(cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2));
        cursor.close();
        return entry;
    }

    public static ClassPasswordEntry _getEntryByClassId(SQLiteDatabase db, long classId) {
        ClassPasswordEntry entry = null;
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{ClassPasswordEntry._ID, // 0
                        ClassPasswordEntry.CLASS_ID, // 1
                        ClassPasswordEntry.PASSWORD}, // 2
                ClassPasswordEntry.CLASS_ID + "=?",
                new String[]{String.valueOf(classId)},
                null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast())
            entry = new ClassPasswordEntry(cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2));
        cursor.close();
        return entry;
    }

    public static long _insertDummyClassPassword(SQLiteDatabase db, long classId, int classPasswordResourceId, Context context) {
        return _insert(db, classId, context.getString(classPasswordResourceId));
    }

    public static class ClassPasswordEntry implements BaseColumns {

        public static final String CLASS_ID = "ClassId";
        public static final String PASSWORD = "Password";

        private long mId; // 0
        private long mClassId; // 1
        private String mPassword; // 2

        public ClassPasswordEntry(long id, long classId, String password) {
            mId = id;
            mClassId = classId;
            mPassword = password;
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

        public String getPassword() {
            return mPassword;
        }

        public void setPassword(String password) {
            mPassword = password;
        }

        @Override
        public boolean equals(Object object) {
            if(!(object instanceof ClassPasswordEntry))
                return false;
            if(object == this)
                return true;
            ClassPasswordEntry entry = (ClassPasswordEntry) object;
            return new EqualsBuilder()
                    .append(mId, entry.mId)
                    .append(mClassId, entry.mClassId).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(41, 43)
                    .append(mId)
                    .append(mClassId).toHashCode();
        }
    }
}
