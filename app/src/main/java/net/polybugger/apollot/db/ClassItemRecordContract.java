package net.polybugger.apollot.db;

import android.provider.BaseColumns;

public class ClassItemRecordContract {

    public static final String TABLE_NAME = "ClassItemRecords_";
    public static final String CREATE_TABLE_SQL1 = "CREATE TABLE IF NOT EXISTS ";
    public static final String CREATE_TABLE_SQL2 = " (" +
            ClassItemRecordEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassItemRecordEntry.CLASS_ID + " INTEGER NOT NULL REFERENCES " +
                ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + "), " +
            ClassItemRecordEntry.ITEM_ID + " INTEGER NOT NULL REFERENCES ";
    public static final String CREATE_TABLE_SQL3 = " (" + ClassItemContract.ClassItemEntry._ID + "), " +
            ClassItemRecordEntry.STUDENT_ID + " INTEGER NOT NULL REFERENCES ";
    public static final String CREATE_TABLE_SQL4 = " (" + ClassStudentContract.ClassStudentEntry.STUDENT_ID + "), " +
            ClassItemRecordEntry.ATTENDANCE + " INTEGER NULL, " +
            ClassItemRecordEntry.SCORE + " REAL NULL, " +
            ClassItemRecordEntry.SUBMISSION_DATE + " TEXT NULL, " +
            ClassItemRecordEntry.REMARKS + " TEXT NULL, " +
            "UNIQUE (" + ClassItemRecordEntry.ITEM_ID + ", " + ClassItemRecordEntry.STUDENT_ID + "))";

    private ClassItemRecordContract() { }

    public static class ClassItemRecordEntry implements BaseColumns {

        public static final String CLASS_ID = "ClassId";
        public static final String ITEM_ID = "ItemId";
        public static final String STUDENT_ID = "StudentId";
        public static final String ATTENDANCE = "Attendance";
        public static final String SCORE = "Score";
        public static final String SUBMISSION_DATE = "SubmissionDate";
        public static final String REMARKS = "Remarks";

    }
}
