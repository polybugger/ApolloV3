package net.polybugger.apollot.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ClassItemTypeContract {

    public static final String TABLE_NAME = "ClassItemTypes";
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            ClassItemTypeEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassItemTypeEntry.DESCRIPTION + " TEXT NOT NULL, " +
            ClassItemTypeEntry.COLOR + " TEXT NULL)";
    public static final String SELECT_TABLE_SQL = "SELECT " +
            ClassItemTypeEntry._ID + ", " + // 0
            ClassItemTypeEntry.DESCRIPTION + ", " + // 1
            ClassItemTypeEntry.COLOR + // 2 nullable
            " FROM " + TABLE_NAME;
    public static final String DELETE_ALL_SQL = "DELETE FROM " + TABLE_NAME;

    private ClassItemTypeContract() { }

    public static long _insert(SQLiteDatabase db, String description, String color) {
        ContentValues values = new ContentValues();
        values.put(ClassItemTypeEntry.DESCRIPTION, description);
        values.put(ClassItemTypeEntry.COLOR, color);
        return db.insert(TABLE_NAME, null, values);
    }

    public static int _update(SQLiteDatabase db, long id, String description, String color) {
        ContentValues values = new ContentValues();
        values.put(ClassItemTypeEntry.DESCRIPTION, description);
        values.put(ClassItemTypeEntry.COLOR, color);
        return db.update(TABLE_NAME, values, ClassItemTypeEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static int _delete(SQLiteDatabase db, long id) {
        return db.delete(TABLE_NAME, ClassItemTypeEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static ClassItemTypeEntry _getEntry(SQLiteDatabase db, long id) {
        ClassItemTypeEntry entry = null;
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{ClassItemTypeEntry._ID, // 0
                        ClassItemTypeEntry.DESCRIPTION, // 1
                        ClassItemTypeEntry.COLOR}, // 2
                ClassItemTypeEntry._ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast())
            entry = new ClassItemTypeEntry(cursor.getLong(0),
                    cursor.getString(1),
                    cursor.isNull(2) ? null : cursor.getString(2));
        cursor.close();
        return entry;
    }

    public static ClassItemTypeEntry _getEntryByDescription(SQLiteDatabase db, String description) {
        ClassItemTypeEntry entry = null;
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{ClassItemTypeEntry._ID, // 0
                        ClassItemTypeEntry.DESCRIPTION, // 1
                        ClassItemTypeEntry.COLOR}, // 2
                ClassItemTypeEntry.DESCRIPTION + "=?",
                new String[]{description},
                null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast())
            entry = new ClassItemTypeEntry(cursor.getLong(0),
                    cursor.getString(1),
                    cursor.isNull(2) ? null : cursor.getString(2));
        cursor.close();
        return entry;
    }

    public static ArrayList<ClassItemTypeEntry> _getEntries(SQLiteDatabase db) {
        ArrayList<ClassItemTypeEntry> entries = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{ClassItemTypeEntry._ID, // 0
                        ClassItemTypeEntry.DESCRIPTION, // 1
                        ClassItemTypeEntry.COLOR}, // 2
                null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            entries.add(new ClassItemTypeEntry(cursor.getLong(0),
                    cursor.getString(1),
                    cursor.isNull(2) ? null : cursor.getString(2)));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public static class ClassItemTypeEntry implements BaseColumns, Serializable {

        public static final String DESCRIPTION = "Description";
        public static final String COLOR = "Color";

        private long mId; // 0
        private String mDescription; // 1
        private String mColor; // 2 nullable

        public ClassItemTypeEntry(long id, String description, String color) {
            mId = id;
            mDescription = description;
            mColor = color;
        }

        public long getId() {
            return mId;
        }

        public void setId(long id) {
            mId = id;
        }

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String description) {
            mDescription = description;
        }

        public String getColor() {
            return mColor;
        }

        public void setColor(String color) {
            this.mColor = color;
        }

        @Override
        public String toString() {
            return mDescription;
        }

        @Override
        public boolean equals(Object object) {
            if(!(object instanceof ClassItemTypeEntry))
                return false;
            if(object == this)
                return true;
            ClassItemTypeEntry entry = (ClassItemTypeEntry) object;
            return new EqualsBuilder()
                    .append(mId, entry.mId).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(5, 7)
                    .append(mId).toHashCode();
        }
    }
}
