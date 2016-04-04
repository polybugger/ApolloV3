package net.polybugger.apollot.db;

import android.provider.BaseColumns;

public class StudentContract {

    public static final String TABLE_NAME = "Students";
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            StudentEntry._ID + " INTEGER PRIMARY KEY, " +
            StudentEntry.LAST_NAME + " TEXT NOT NULL, " +
            StudentEntry.FIRST_NAME + " TEXT NULL, " +
            StudentEntry.MIDDLE_NAME + " TEXT NULL, " +
            StudentEntry.GENDER + " INTEGER NULL, " +
            StudentEntry.EMAIL_ADDRESS + " TEXT NULL, " +
            StudentEntry.CONTACT_NO + " TEXT NULL)";

    private StudentContract() { }

    public static class StudentEntry implements BaseColumns {

        public static final String LAST_NAME = "LastName";
        public static final String FIRST_NAME = "FirstName";
        public static final String MIDDLE_NAME = "MiddleName";
        public static final String GENDER = "Gender";
        public static final String EMAIL_ADDRESS = "EmailAddress";
        public static final String CONTACT_NO = "ContactNumber";

    }
}
