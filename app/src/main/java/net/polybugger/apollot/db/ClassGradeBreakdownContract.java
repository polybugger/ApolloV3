package net.polybugger.apollot.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.StyleableRes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ClassGradeBreakdownContract {

    public static final String TABLE_NAME = "ClassGradeBreakdown_";
    public static final String CREATE_TABLE_SQL1 = "CREATE TABLE IF NOT EXISTS ";
    public static final String CREATE_TABLE_SQL2 = " (" +
            ClassGradeBreakdownEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassGradeBreakdownEntry.CLASS_ID + " INTEGER NOT NULL REFERENCES " +
                ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + ") ON DELETE CASCADE, " +
            ClassGradeBreakdownEntry.ITEM_TYPE_ID + " INTEGER NOT NULL UNIQUE REFERENCES " +
                ClassItemTypeContract.TABLE_NAME + " (" + ClassItemTypeContract.ClassItemTypeEntry._ID + ") ON DELETE CASCADE, " +
            ClassGradeBreakdownEntry.PERCENTAGE + " REAL NULL)";

    private ClassGradeBreakdownContract() { }

    public static long _insert(SQLiteDatabase db, long classId, ClassItemTypeContract.ClassItemTypeEntry itemType, float percentage) {
        String tableName = TABLE_NAME + String.valueOf(classId);
        db.execSQL(CREATE_TABLE_SQL1 + tableName + CREATE_TABLE_SQL2);
        ContentValues values = new ContentValues();
        values.put(ClassGradeBreakdownEntry.CLASS_ID, classId);
        values.put(ClassGradeBreakdownEntry.ITEM_TYPE_ID, itemType.getId());
        values.put(ClassGradeBreakdownEntry.PERCENTAGE, percentage);
        return db.insert(tableName, null, values);
    }

    public static int _update(SQLiteDatabase db, long id, long classId, ClassItemTypeContract.ClassItemTypeEntry itemType, float percentage) {
        ContentValues values = new ContentValues();
        // do not allow to change class id and item type id
        values.put(ClassGradeBreakdownEntry.PERCENTAGE, percentage);
        return db.update(TABLE_NAME + String.valueOf(classId), values, ClassGradeBreakdownEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static int _delete(SQLiteDatabase db, long id, long classId) {
        return db.delete(TABLE_NAME + String.valueOf(classId), ClassGradeBreakdownEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static ClassGradeBreakdownEntry _getEntry(SQLiteDatabase db, long id, long classId) {
        String tableName = TABLE_NAME + String.valueOf(classId);
        db.execSQL(CREATE_TABLE_SQL1 + tableName + CREATE_TABLE_SQL2);
        ClassGradeBreakdownEntry entry = null;
        Cursor cursor = db.query(tableName + " AS cgb INNER JOIN " +
                        ClassContract.TABLE_NAME + " AS c ON cgb." + ClassGradeBreakdownEntry.CLASS_ID + "=c." + ClassContract.ClassEntry._ID +
                        " LEFT OUTER JOIN " + ClassItemTypeContract.TABLE_NAME + " AS cit ON cgb." + ClassGradeBreakdownEntry.ITEM_TYPE_ID + "=cit." + ClassItemTypeContract.ClassItemTypeEntry._ID,
                new String[]{"cgb." + ClassGradeBreakdownEntry._ID, // 0
                        "cgb." + ClassGradeBreakdownEntry.CLASS_ID, // 1
                        "cgb." + ClassGradeBreakdownEntry.ITEM_TYPE_ID, // 2
                        "cit." + ClassItemTypeContract.ClassItemTypeEntry.DESCRIPTION, // 3
                        "cit." + ClassItemTypeContract.ClassItemTypeEntry.COLOR, // 4
                        "cgb." + ClassGradeBreakdownEntry.PERCENTAGE}, // 5
                "cgb." + ClassGradeBreakdownEntry._ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            entry = new ClassGradeBreakdownEntry(cursor.getLong(0),
                    cursor.getLong(1),
                    new ClassItemTypeContract.ClassItemTypeEntry(cursor.getLong(2), cursor.getString(3), cursor.isNull(4) ? null : cursor.getString(4)),
                    cursor.isNull(5) ? null : cursor.getFloat(5));
        }
        cursor.close();
        return entry;
    }

    public static ArrayList<ClassGradeBreakdownEntry> _getEntries(SQLiteDatabase db, long classId) {
        String tableName = TABLE_NAME + String.valueOf(classId);
        db.execSQL(CREATE_TABLE_SQL1 + tableName + CREATE_TABLE_SQL2);
        ArrayList<ClassGradeBreakdownEntry> entries = new ArrayList<>();
        Cursor cursor = db.query(tableName + " AS cgb INNER JOIN " +
                        ClassContract.TABLE_NAME + " AS c ON cgb." + ClassGradeBreakdownEntry.CLASS_ID + "=c." + ClassContract.ClassEntry._ID +
                        " LEFT OUTER JOIN " + ClassItemTypeContract.TABLE_NAME + " AS cit ON cgb." + ClassGradeBreakdownEntry.ITEM_TYPE_ID + "=cit." + ClassItemTypeContract.ClassItemTypeEntry._ID,
                new String[]{"cgb." + ClassGradeBreakdownEntry._ID, // 0
                        "cgb." + ClassGradeBreakdownEntry.CLASS_ID, // 1
                        "cgb." + ClassGradeBreakdownEntry.ITEM_TYPE_ID, // 2
                        "cit." + ClassItemTypeContract.ClassItemTypeEntry.DESCRIPTION, // 3
                        "cit." + ClassItemTypeContract.ClassItemTypeEntry.COLOR, // 4
                        "cgb." + ClassGradeBreakdownEntry.PERCENTAGE}, // 5
                null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            entries.add(new ClassGradeBreakdownEntry(cursor.getLong(0),
                    cursor.getLong(1),
                    new ClassItemTypeContract.ClassItemTypeEntry(cursor.getLong(2), cursor.getString(3), cursor.isNull(4) ? null : cursor.getString(4)),
                    cursor.isNull(5) ? null : cursor.getFloat(5)));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public static long _insertDummyClassGradeBreakdown(SQLiteDatabase db, long classId, int classGradeBreakdownResourceId, Context context) {
        @StyleableRes final int ITEM_TYPE_INDEX = 0;
        @StyleableRes final int PERCENTAGE_INDEX = 1;
        Resources res = context.getResources();
        TypedArray ta = res.obtainTypedArray(classGradeBreakdownResourceId);
        ClassItemTypeContract.ClassItemTypeEntry itemType;
        try {
            itemType = ClassItemTypeContract._getEntryByDescription(db, res.getString(ta.getResourceId(ITEM_TYPE_INDEX, 0)));
        }
        catch(Exception e) {
            itemType = null;
        }
        Float percentage;
        try {
            percentage = Float.parseFloat(res.getString(ta.getResourceId(PERCENTAGE_INDEX, 0)));
        }
        catch(Exception e) {
            percentage = null;
        }
        ta.recycle();
        return _insert(db, classId, itemType, percentage);
    }

    public static class ClassGradeBreakdownEntry implements BaseColumns, Serializable {

        public static final String CLASS_ID = "ClassId";
        public static final String ITEM_TYPE_ID = "ItemTypeId";
        public static final String PERCENTAGE = "Percentage";

        private long mId;
        private long mClassId;
        private ClassItemTypeContract.ClassItemTypeEntry mItemType;
        private float mPercentage;

        public ClassGradeBreakdownEntry(long id, long classId, ClassItemTypeContract.ClassItemTypeEntry itemType, float percentage) {
            mId = id;
            mClassId = classId;
            mItemType = itemType;
            mPercentage = percentage;
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

        public ClassItemTypeContract.ClassItemTypeEntry getItemType() {
            return mItemType;
        }

        public void setItemType(ClassItemTypeContract.ClassItemTypeEntry itemType) {
            mItemType = itemType;
        }

        public float getPercentage() {
            return mPercentage;
        }

        public void setPercentage(float percentage) {
            mPercentage = percentage;
        }

        @Override
        public boolean equals(Object object) {
            if(!(object instanceof ClassGradeBreakdownEntry))
                return false;
            if(object == this)
                return true;
            ClassGradeBreakdownEntry entry = (ClassGradeBreakdownEntry) object;
            return new EqualsBuilder()
                    .append(mId, entry.mId)
                    .append(mClassId, entry.mClassId)
                    .append(mItemType.getId(), entry.mItemType.getId()).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(67, 71)
                    .append(mId)
                    .append(mClassId)
                    .append(mItemType.getId()).toHashCode();
        }
    }
}
