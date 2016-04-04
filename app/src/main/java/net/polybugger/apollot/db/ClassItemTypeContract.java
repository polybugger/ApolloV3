package net.polybugger.apollot.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.io.Serializable;
import java.util.ArrayList;

public class ClassItemTypeContract {

    public static final String TABLE_NAME = "ClassItemTypes";
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            ClassItemTypeEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassItemTypeEntry.DESCRIPTION + " TEXT NOT NULL, " +
            ClassItemTypeEntry.COLOR + " TEXT NULL)";
    public static final String SELECT_TABLE_SQL = "SELECT " +
            ClassItemTypeEntry._ID + ", " + // 0
            ClassItemTypeEntry.DESCRIPTION + ", " + // 1
            ClassItemTypeEntry.COLOR + // 2
            ", FROM " + TABLE_NAME;

    private ClassItemTypeContract() { }

    public static long _insert(SQLiteDatabase db, String description, String color) {
        ContentValues values = new ContentValues();
        values.put(ClassItemTypeEntry.DESCRIPTION, description);
        values.put(ClassItemTypeEntry.COLOR, color);
        return db.insert(TABLE_NAME, null, values);
    }

    public static long insert(String description, String color) {
        SQLiteDatabase db = ApolloDbAdapter.open();
        long id = _insert(db, description, color);
        ApolloDbAdapter.close();
        return id;
    }

    public static int _update(SQLiteDatabase db, long id, String description, String color) {
        ContentValues values = new ContentValues();
        values.put(ClassItemTypeEntry.DESCRIPTION, description);
        values.put(ClassItemTypeEntry.COLOR, color);
        return db.update(TABLE_NAME, values, ClassItemTypeEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static int update(long id, String description, String color) {
        SQLiteDatabase db = ApolloDbAdapter.open();
        int rowsUpdated = _update(db, id, description, color);
        ApolloDbAdapter.close();
        return rowsUpdated;
    }

    public static int _delete(SQLiteDatabase db, long id) {
        return db.delete(TABLE_NAME, ClassItemTypeEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static int delete(long id) {
        SQLiteDatabase db = ApolloDbAdapter.open();
        int rowsDeleted = _delete(db, id);
        ApolloDbAdapter.close();
        return rowsDeleted;
    }

    public static ArrayList<ClassItemTypeEntry> _getEntries(SQLiteDatabase db) {
        ArrayList<ClassItemTypeEntry> entries = new ArrayList<>();
        Cursor cursor = db.rawQuery(SELECT_TABLE_SQL, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            entries.add(new ClassItemTypeEntry(cursor.getLong(0), cursor.getString(1), cursor.getString(2)));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public static ArrayList<ClassItemTypeEntry> getEntries() {
        SQLiteDatabase db = ApolloDbAdapter.open();
        ArrayList<ClassItemTypeEntry> entries = _getEntries(db);
        ApolloDbAdapter.close();
        return entries;
    }

    public static class ClassItemTypeEntry implements BaseColumns, Serializable {

        public static final String DESCRIPTION = "Description";
        public static final String COLOR = "Color";

        private long mId;
        private String mDescription;
        private String mColor;

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

        public int getColorInt() {
            return (int) Long.parseLong(mColor != null ? mColor : ColorEnum.TRANSPARENT.toString(), 16);
        }

        @Override
        public String toString() {
            return mDescription;
        }

        public boolean equals(ClassItemTypeEntry classItemTypeEntry) {
            if(classItemTypeEntry != null && classItemTypeEntry.mId == mId)
                return true;
            return false;
        }

        @Override
        public boolean equals(Object object) {
            ClassItemTypeEntry classItemTypeEntry;
            if(object != null) {
                try {
                    classItemTypeEntry = (ClassItemTypeEntry) object;
                    if(classItemTypeEntry.mId == mId)
                        return true;
                }
                catch(ClassCastException e) {
                    throw new ClassCastException(object.toString() + " must be an instance of " + ClassItemTypeEntry.class.toString());
                }
            }
            return false;
        }
    }
}
