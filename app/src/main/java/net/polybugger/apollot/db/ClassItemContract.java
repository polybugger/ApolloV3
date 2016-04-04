package net.polybugger.apollot.db;

import android.provider.BaseColumns;

public class ClassItemContract {

    public static final String TABLE_NAME = "ClassItems_";
    public static final String CREATE_TABLE_SQL1 = "CREATE TABLE IF NOT EXISTS ";
    public static final String CREATE_TABLE_SQL2 = " (" +
            ClassItemEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassItemEntry.CLASS_ID + " INTEGER NOT NULL REFERENCES " +
                ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + "), " +
            ClassItemEntry.DESCRIPTION + " TEXT NOT NULL, " +
            ClassItemEntry.ITEM_TYPE_ID + " INTEGER NULL REFERENCES " +
                ClassItemTypeContract.TABLE_NAME + " (" + ClassItemTypeContract.ClassItemTypeEntry._ID + "), " +
            ClassItemEntry.ITEM_DATE + " TEXT NULL, " +
            ClassItemEntry.CHECK_ATTENDANCE + " INTEGER NOT NULL DEFAULT 0, " +
            ClassItemEntry.RECORD_SCORES + " INTEGER NOT NULL DEFAULT 0, " +
            ClassItemEntry.PERFECT_SCORE + " REAL NULL, " +
            ClassItemEntry.RECORD_SUBMISSIONS + " INTEGER NOT NULL DEFAULT 0, " +
            ClassItemEntry.SUBMISSION_DUE_DATE + " TEXT NULL)";

    private ClassItemContract() { }

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

    }
}
