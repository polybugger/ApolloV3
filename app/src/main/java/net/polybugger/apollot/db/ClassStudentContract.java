package net.polybugger.apollot.db;

import android.provider.BaseColumns;

public class ClassStudentContract {

    public static final String TABLE_NAME = "ClassStudents_";
    public static final String CREATE_TABLE_SQL1 = "CREATE TABLE IF NOT EXISTS ";
    public static final String CREATE_TABLE_SQL2 = " (" +
            ClassStudentEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassStudentEntry.CLASS_ID + " INTEGER NOT NULL REFERENCES " +
                ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + "), " +
            ClassStudentEntry.STUDENT_ID + " INTEGER NOT NULL UNIQUE REFERENCES " +
                StudentContract.TABLE_NAME + " (" + StudentContract.StudentEntry._ID + "), " +
            ClassStudentEntry.DATE_CREATED + " TEXT NULL)";

    private ClassStudentContract() { }

    public static class ClassStudentEntry implements BaseColumns {

        public static final String CLASS_ID = "ClassId";
        public static final String STUDENT_ID = "StudentId";
        public static final String DATE_CREATED = "DateCreated";

    }


}
