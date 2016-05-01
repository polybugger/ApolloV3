package net.polybugger.apollot.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.StyleableRes;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

public class ClassItemContract {

    public static final String TABLE_NAME = "ClassItems_";
    public static final String CREATE_TABLE_SQL1 = "CREATE TABLE IF NOT EXISTS ";
    public static final String CREATE_TABLE_SQL2 = " (" +
            ClassItemEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassItemEntry.CLASS_ID + " INTEGER NOT NULL REFERENCES " +
                ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + ") ON DELETE CASCADE, " +
            ClassItemEntry.DESCRIPTION + " TEXT NOT NULL, " +
            ClassItemEntry.ITEM_TYPE_ID + " INTEGER NULL REFERENCES " +
                ClassItemTypeContract.TABLE_NAME + " (" + ClassItemTypeContract.ClassItemTypeEntry._ID + ") ON DELETE SET NULL, " +
            ClassItemEntry.ITEM_DATE + " TEXT NULL, " +
            ClassItemEntry.CHECK_ATTENDANCE + " INTEGER NOT NULL DEFAULT 0, " +
            ClassItemEntry.RECORD_SCORES + " INTEGER NOT NULL DEFAULT 0, " +
            ClassItemEntry.PERFECT_SCORE + " REAL NULL, " +
            ClassItemEntry.RECORD_SUBMISSIONS + " INTEGER NOT NULL DEFAULT 0, " +
            ClassItemEntry.SUBMISSION_DUE_DATE + " TEXT NULL)";
    public static final String SELECT_TABLE_SQL1 = "SELECT " +
            "ci." + ClassItemEntry._ID + ", " + // 0
            "ci." + ClassItemEntry.CLASS_ID + ", " + // 1
            "ci." + ClassItemEntry.DESCRIPTION + ", " + // 2
            "ci." + ClassItemEntry.ITEM_TYPE_ID + ", " + // 3 nullable
                "cit." + ClassItemTypeContract.ClassItemTypeEntry.DESCRIPTION + ", " + // 4
                "cit." + ClassItemTypeContract.ClassItemTypeEntry.COLOR + ", " + // 5
            "ci." + ClassItemEntry.ITEM_DATE + ", " + // 6 nullable
            "ci." + ClassItemEntry.CHECK_ATTENDANCE + ", " + // 7
            "ci." + ClassItemEntry.RECORD_SCORES + ", " + // 8
            "ci." + ClassItemEntry.PERFECT_SCORE + ", " + // 9 nullable
            "ci." + ClassItemEntry.RECORD_SUBMISSIONS + ", " + // 10
            "ci." + ClassItemEntry.SUBMISSION_DUE_DATE + // 11 nullable
            " FROM ";
    public static final String SELECT_TABLE_SQL2 = " AS ci INNER JOIN " +
            ClassContract.TABLE_NAME + " AS c ON ci." + ClassItemEntry.CLASS_ID + "=c." + ClassContract.ClassEntry._ID +
            " LEFT OUTER JOIN " + ClassItemTypeContract.TABLE_NAME + " AS cit ON ci." + ClassItemEntry.ITEM_TYPE_ID + "=cit." + ClassItemTypeContract.ClassItemTypeEntry._ID;
    public static final String DELETE_ALL_SQL = "DELETE FROM ";

    private ClassItemContract() { }

    public static long _insert(SQLiteDatabase db, long classId, String description, ClassItemTypeContract.ClassItemTypeEntry itemType, Date itemDate, boolean checkAttendance, boolean recordScores, Float perfectScore, boolean recordSubmissions, Date submissionDueDate) {
        String tableName = TABLE_NAME + String.valueOf(classId);
        db.execSQL(CREATE_TABLE_SQL1 + tableName + CREATE_TABLE_SQL2);
        ContentValues values = new ContentValues();
        values.put(ClassItemEntry.CLASS_ID, classId);
        values.put(ClassItemEntry.DESCRIPTION, description);
        values.put(ClassItemEntry.ITEM_TYPE_ID, itemType != null ? itemType.getId() : null);
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        values.put(ClassItemEntry.ITEM_DATE, (itemDate != null) ? sdf.format(itemDate) : null);
        values.put(ClassItemEntry.CHECK_ATTENDANCE, checkAttendance);
        values.put(ClassItemEntry.RECORD_SCORES, recordScores);
        values.put(ClassItemEntry.PERFECT_SCORE, perfectScore);
        values.put(ClassItemEntry.RECORD_SUBMISSIONS, recordSubmissions);
        values.put(ClassItemEntry.SUBMISSION_DUE_DATE, (submissionDueDate != null) ? sdf.format(submissionDueDate) : null);
        return db.insert(tableName, null, values);
    }

    public static int _update(SQLiteDatabase db, long id, long classId, String description, ClassItemTypeContract.ClassItemTypeEntry itemType, Date itemDate, boolean checkAttendance, boolean recordScores, Float perfectScore, boolean recordSubmissions, Date submissionDueDate) {
        ContentValues values = new ContentValues();
        // do not allow to change class id
        values.put(ClassItemEntry.DESCRIPTION, description);
        values.put(ClassItemEntry.ITEM_TYPE_ID, itemType != null ? itemType.getId() : null);
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        values.put(ClassItemEntry.ITEM_DATE, (itemDate != null) ? sdf.format(itemDate) : null);
        values.put(ClassItemEntry.CHECK_ATTENDANCE, checkAttendance);
        values.put(ClassItemEntry.RECORD_SCORES, recordScores);
        values.put(ClassItemEntry.PERFECT_SCORE, perfectScore);
        values.put(ClassItemEntry.RECORD_SUBMISSIONS, recordSubmissions);
        values.put(ClassItemEntry.SUBMISSION_DUE_DATE, (submissionDueDate != null) ? sdf.format(submissionDueDate) : null);
        return db.update(TABLE_NAME + String.valueOf(classId), values, ClassItemEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static int _delete(SQLiteDatabase db, long id, long classId) {
        return db.delete(TABLE_NAME + String.valueOf(classId), ClassItemEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static ClassItemEntry _getEntry(SQLiteDatabase db, long id, long classId) {
        String tableName = TABLE_NAME + String.valueOf(classId);
        db.execSQL(CREATE_TABLE_SQL1 + tableName + CREATE_TABLE_SQL2);
        ClassItemEntry entry = null;
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        Date itemDate, submissionDueDate;
        Cursor cursor = db.query(tableName + " AS ci INNER JOIN " +
                ClassContract.TABLE_NAME + " AS c ON ci." + ClassItemEntry.CLASS_ID + "=c." + ClassContract.ClassEntry._ID +
                " LEFT OUTER JOIN " + ClassItemTypeContract.TABLE_NAME + " AS cit ON ci." + ClassItemEntry.ITEM_TYPE_ID + "=cit." + ClassItemTypeContract.ClassItemTypeEntry._ID,
                new String[]{"ci." + ClassItemEntry._ID, // 0
                        "ci." + ClassItemEntry.CLASS_ID, // 1
                        "ci." + ClassItemEntry.DESCRIPTION, // 2
                        "ci." + ClassItemEntry.ITEM_TYPE_ID, // 3 nullable
                        "cit." + ClassItemTypeContract.ClassItemTypeEntry.DESCRIPTION, // 4
                        "cit." + ClassItemTypeContract.ClassItemTypeEntry.COLOR, // 5
                        "ci." + ClassItemEntry.ITEM_DATE, // 6 nullable
                        "ci." + ClassItemEntry.CHECK_ATTENDANCE, // 7
                        "ci." + ClassItemEntry.RECORD_SCORES, // 8
                        "ci." + ClassItemEntry.PERFECT_SCORE, // 9 nullable
                        "ci." + ClassItemEntry.RECORD_SUBMISSIONS, // 10
                        "ci." + ClassItemEntry.SUBMISSION_DUE_DATE}, // 11 nullable
                "ci." + ClassItemEntry._ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            try {
                itemDate = sdf.parse(cursor.getString(6));
            }
            catch(Exception e) {
                itemDate = null;
            }
            try {
                submissionDueDate = sdf.parse(cursor.getString(11));
            }
            catch(Exception e) {
                submissionDueDate = null;
            }
            entry = new ClassItemEntry(cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2),
                    cursor.isNull(3) ? null : new ClassItemTypeContract.ClassItemTypeEntry(cursor.getLong(3), cursor.getString(4), cursor.isNull(5) ? null : cursor.getString(5)),
                    itemDate, // 6
                    cursor.getInt(7) != 0,
                    cursor.getInt(8) != 0,
                    cursor.isNull(9) ? null : cursor.getFloat(9),
                    cursor.getInt(10) != 0,
                    submissionDueDate); // 11
        }
        cursor.close();
        return entry;
    }

    public static ArrayList<ClassItemEntry> _getEntries(SQLiteDatabase db, long classId) {
        String tableName = TABLE_NAME + String.valueOf(classId);
        db.execSQL(CREATE_TABLE_SQL1 + tableName + CREATE_TABLE_SQL2);
        ArrayList<ClassItemEntry> entries = new ArrayList<>();
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        Date itemDate, submissionDueDate;
        Cursor cursor = db.query(tableName + " AS ci INNER JOIN " +
                        ClassContract.TABLE_NAME + " AS c ON ci." + ClassItemEntry.CLASS_ID + "=c." + ClassContract.ClassEntry._ID +
                        " LEFT OUTER JOIN " + ClassItemTypeContract.TABLE_NAME + " AS cit ON ci." + ClassItemEntry.ITEM_TYPE_ID + "=cit." + ClassItemTypeContract.ClassItemTypeEntry._ID,
                new String[]{"ci." + ClassItemEntry._ID, // 0
                        "ci." + ClassItemEntry.CLASS_ID, // 1
                        "ci." + ClassItemEntry.DESCRIPTION, // 2
                        "ci." + ClassItemEntry.ITEM_TYPE_ID, // 3 nullable
                        "cit." + ClassItemTypeContract.ClassItemTypeEntry.DESCRIPTION, // 4
                        "cit." + ClassItemTypeContract.ClassItemTypeEntry.COLOR, // 5
                        "ci." + ClassItemEntry.ITEM_DATE, // 6 nullable
                        "ci." + ClassItemEntry.CHECK_ATTENDANCE, // 7
                        "ci." + ClassItemEntry.RECORD_SCORES, // 8
                        "ci." + ClassItemEntry.PERFECT_SCORE, // 9 nullable
                        "ci." + ClassItemEntry.RECORD_SUBMISSIONS, // 10
                        "ci." + ClassItemEntry.SUBMISSION_DUE_DATE}, // 11 nullable
                null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            try {
                itemDate = sdf.parse(cursor.getString(6));
            }
            catch(Exception e) {
                itemDate = null;
            }
            try {
                submissionDueDate = sdf.parse(cursor.getString(11));
            }
            catch(Exception e) {
                submissionDueDate = null;
            }
            entries.add(new ClassItemEntry(cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2),
                    cursor.isNull(3) ? null : new ClassItemTypeContract.ClassItemTypeEntry(cursor.getLong(3), cursor.getString(4), cursor.isNull(5) ? null : cursor.getString(5)),
                    itemDate, // 6
                    cursor.getInt(7) != 0,
                    cursor.getInt(8) != 0,
                    cursor.isNull(9) ? null : cursor.getFloat(9),
                    cursor.getInt(10) != 0,
                    submissionDueDate)); // 11
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public static LinkedHashMap<ClassItemTypeContract.ClassItemTypeEntry, Integer> _getItemSummaryCount(SQLiteDatabase db, long classId) {
        String tableName = TABLE_NAME + String.valueOf(classId);
        db.execSQL(CREATE_TABLE_SQL1 + tableName + CREATE_TABLE_SQL2);
        LinkedHashMap<ClassItemTypeContract.ClassItemTypeEntry, Integer> itemSummaryCount = new LinkedHashMap<>();
        ClassItemTypeContract.ClassItemTypeEntry itemType = null;
        Cursor cursor = db.query(tableName + " AS ci INNER JOIN " +
                        ClassContract.TABLE_NAME + " AS c ON ci." + ClassItemEntry.CLASS_ID + "=c." + ClassContract.ClassEntry._ID +
                        " LEFT OUTER JOIN " + ClassItemTypeContract.TABLE_NAME + " AS cit ON ci." + ClassItemEntry.ITEM_TYPE_ID + "=cit." + ClassItemTypeContract.ClassItemTypeEntry._ID,
                new String[]{"ci." + ClassItemEntry.ITEM_TYPE_ID, // 0 nullable
                        "cit." + ClassItemTypeContract.ClassItemTypeEntry.DESCRIPTION, // 1
                        "cit." + ClassItemTypeContract.ClassItemTypeEntry.COLOR}, // 2
                null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            itemType = cursor.isNull(0) ? null : new ClassItemTypeContract.ClassItemTypeEntry(cursor.getLong(0), cursor.getString(1), cursor.isNull(2) ? null : cursor.getString(2));
            if(itemType != null) {
                if(itemSummaryCount.containsKey(itemType)) {
                    int count = itemSummaryCount.get(itemType) + 1;
                    itemSummaryCount.put(itemType, count);
                }
                else
                    itemSummaryCount.put(itemType, 1);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return itemSummaryCount;
    }

    public static long _insertDummyClassItem(SQLiteDatabase db, long classId, int classItemResourceId, Context context) {
        @StyleableRes final int DESCRIPTION_INDEX = 0;
        @StyleableRes final int ITEM_TYPE_INDEX = 1;
        @StyleableRes final int ITEM_DATE_INDEX = 2;
        @StyleableRes final int CHECK_ATTENDANCE_INDEX = 3;
        @StyleableRes final int RECORD_SCORES_INDEX = 4;
        @StyleableRes final int PERFECT_SCORE_INDEX = 5;
        @StyleableRes final int RECORD_SUBMISSIONS_INDEX = 6;
        @StyleableRes final int SUBMISSION_DUE_DATE_INDEX = 7;
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, context.getResources().getConfiguration().locale);
        Resources res = context.getResources();
        TypedArray ta = res.obtainTypedArray(classItemResourceId);
        String description = res.getString(ta.getResourceId(DESCRIPTION_INDEX, 0));
        ClassItemTypeContract.ClassItemTypeEntry itemType;
        try {
            itemType = ClassItemTypeContract._getEntryByDescription(db, res.getString(ta.getResourceId(ITEM_TYPE_INDEX, 0)));
        }
        catch(Exception e) {
            itemType = null;
        }
        Date itemDate;
        try {
            itemDate = sdf.parse(res.getString(ta.getResourceId(ITEM_DATE_INDEX, 0)));
        }
        catch(Exception e) {
            itemDate = null;
        }
        boolean checkAttendance = res.getBoolean(ta.getResourceId(CHECK_ATTENDANCE_INDEX, 0));
        boolean recordScores = res.getBoolean(ta.getResourceId(RECORD_SCORES_INDEX, 0));
        Float perfectScore;
        try {
            perfectScore = Float.parseFloat(res.getString(ta.getResourceId(PERFECT_SCORE_INDEX, 0)));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        boolean recordSubmissions = res.getBoolean(ta.getResourceId(RECORD_SUBMISSIONS_INDEX, 0));
        Date submissionDueDate;
        try {
            submissionDueDate = sdf.parse(res.getString(ta.getResourceId(SUBMISSION_DUE_DATE_INDEX, 0)));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        ta.recycle();
        return _insert(db, classId,
                description, itemType, itemDate, checkAttendance, recordScores, perfectScore, recordSubmissions, submissionDueDate);
    }

    public static class ClassItemEntry implements BaseColumns {

        public static final String CLASS_ID = "ClassId";
        public static final String DESCRIPTION = "Description";
        public static final String ITEM_TYPE_ID = "ItemTypeId";
        public static final String ITEM_DATE = "ItemDate";
        public static final String CHECK_ATTENDANCE = "CheckAttendance";
        public static final String RECORD_SCORES = "RecordScores";
        public static final String PERFECT_SCORE = "PerfectScore";
        public static final String RECORD_SUBMISSIONS = "RecordSubmissions";
        public static final String SUBMISSION_DUE_DATE = "SubmissionDueDate";

        private long mId; // 0
        private long mClassId; // 1
        private String mDescription; // 2
        private ClassItemTypeContract.ClassItemTypeEntry mItemType; // 3 nullable
        private Date mItemDate; // 4 nullable
        private boolean mCheckAttendance; // 5
        private boolean mRecordScores; // 6
        private Float mPerfectScore; // 7 nullable
        private boolean mRecordSubmissions; // 8
        private Date mSubmissionDueDate; // 9 nullable

        public ClassItemEntry(long id, long classId, String description, ClassItemTypeContract.ClassItemTypeEntry itemType, Date itemDate, boolean checkAttendance, boolean recordScores, Float perfectScore, boolean recordSubmissions, Date submissionDueDate) {
            mId = id;
            mClassId = classId;
            mDescription = description;
            mItemType = itemType;
            mItemDate = itemDate;
            mCheckAttendance = checkAttendance;
            mRecordScores = recordScores;
            mPerfectScore = perfectScore;
            mRecordSubmissions = recordSubmissions;
            mSubmissionDueDate = submissionDueDate;
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

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String description) {
            mDescription = description;
        }

        public ClassItemTypeContract.ClassItemTypeEntry getItemType() {
            return mItemType;
        }

        public void setItemType(ClassItemTypeContract.ClassItemTypeEntry itemType) {
            mItemType = itemType;
        }

        public Date getItemDate() {
            return mItemDate;
        }

        public void setItemDate(Date itemDate) {
            mItemDate = itemDate;
        }

        public boolean isCheckAttendance() {
            return mCheckAttendance;
        }

        public void setCheckAttendance(boolean checkAttendance) {
            mCheckAttendance = checkAttendance;
        }

        public boolean isRecordScores() {
            return mRecordScores;
        }

        public void setRecordScores(boolean recordScores) {
            mRecordScores = recordScores;
        }

        public Float getPerfectScore() {
            return mPerfectScore;
        }

        public void setPerfectScore(Float perfectScore) {
            mPerfectScore = perfectScore;
        }

        public boolean isRecordSubmissions() {
            return mRecordSubmissions;
        }

        public void setRecordSubmissions(boolean recordSubmissions) {
            mRecordSubmissions = recordSubmissions;
        }

        public Date getSubmissionDueDate() {
            return mSubmissionDueDate;
        }

        public void setSubmissionDueDate(Date submissionDueDate) {
            mSubmissionDueDate = submissionDueDate;
        }

        @Override
        public String toString() {
            return mDescription;
        }

        @Override
        public boolean equals(Object object) {
            if(!(object instanceof ClassItemEntry))
                return false;
            if(object == this)
                return true;
            ClassItemEntry entry = (ClassItemEntry) object;
            return new EqualsBuilder()
                    .append(mId, entry.mId)
                    .append(mClassId, entry.mClassId).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(47, 53)
                    .append(mId)
                    .append(mClassId).toHashCode();
        }
    }
}
