package net.polybugger.apollot.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class StudentContract {

    public static final String TABLE_NAME = "Students";
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            StudentEntry._ID + " INTEGER PRIMARY KEY, " +
            StudentEntry.LAST_NAME + " TEXT NOT NULL, " +
            StudentEntry.FIRST_NAME + " TEXT NULL, " +
            StudentEntry.MIDDLE_NAME + " TEXT NULL, " +
            StudentEntry.GENDER + " INTEGER NULL, " +
            StudentEntry.EMAIL_ADDRESS + " TEXT NULL, " +
            StudentEntry.CONTACT_NUMBER + " TEXT NULL)";
    public static final String SELECT_TABLE_SQL = "SELECT " +
            StudentEntry._ID + ", " + // 0
            StudentEntry.LAST_NAME + ", " + // 1
            StudentEntry.FIRST_NAME + ", " + // 2
            StudentEntry.MIDDLE_NAME + ", " + // 3
            StudentEntry.GENDER + ", " + // 4
            StudentEntry.EMAIL_ADDRESS + ", " + // 5
            StudentEntry.CONTACT_NUMBER + // 6
            " FROM " + TABLE_NAME;
    public static final String DELETE_ALL_SQL = "DELETE FROM " + TABLE_NAME;

    private StudentContract() { }

    public static long _insert(SQLiteDatabase db, String lastName, String firstName, String middleName, GenderEnum gender, String emailAddress, String contactNumber) {
        ContentValues values = new ContentValues();
        values.put(StudentEntry.LAST_NAME, lastName);
        values.put(StudentEntry.FIRST_NAME, firstName);
        values.put(StudentEntry.MIDDLE_NAME, middleName);
        values.put(StudentEntry.GENDER, gender != null ? gender.getValue() : null);
        values.put(StudentEntry.EMAIL_ADDRESS, emailAddress);
        values.put(StudentEntry.CONTACT_NUMBER, contactNumber);
        return db.insert(TABLE_NAME, null, values);
    }

    public static long insert(String lastName, String firstName, String middleName, GenderEnum gender, String emailAddress, String contactNumber) {
        SQLiteDatabase db = ApolloDbAdapter.open();
        long id = _insert(db, lastName, firstName, middleName, gender, emailAddress, contactNumber);
        ApolloDbAdapter.close();
        return id;
    }

    public static int _update(SQLiteDatabase db, long id, String lastName, String firstName, String middleName, GenderEnum gender, String emailAddress, String contactNumber) {
        ContentValues values = new ContentValues();
        values.put(StudentEntry.LAST_NAME, lastName);
        values.put(StudentEntry.FIRST_NAME, firstName);
        values.put(StudentEntry.MIDDLE_NAME, middleName);
        values.put(StudentEntry.GENDER, gender != null ? gender.getValue() : null);
        values.put(StudentEntry.EMAIL_ADDRESS, emailAddress);
        values.put(StudentEntry.CONTACT_NUMBER, contactNumber);
        return db.update(TABLE_NAME, values, StudentEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static int update(long id, String lastName, String firstName, String middleName, GenderEnum gender, String emailAddress, String contactNumber) {
        SQLiteDatabase db = ApolloDbAdapter.open();
        int rowsUpdated = _update(db, id, lastName, firstName, middleName, gender, emailAddress, contactNumber);
        ApolloDbAdapter.close();
        return rowsUpdated;
    }

    public static int _delete(SQLiteDatabase db, long id) {
        return db.delete(TABLE_NAME, StudentEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static int delete(long id) {
        SQLiteDatabase db = ApolloDbAdapter.open();
        int rowsDeleted = _delete(db, id);
        ApolloDbAdapter.close();
        return rowsDeleted;
    }

    public static StudentEntry _getEntry(SQLiteDatabase db, long id) {
        StudentEntry entry = null;
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{StudentEntry._ID, // 0
                        StudentEntry.LAST_NAME, // 1
                        StudentEntry.FIRST_NAME, // 2
                        StudentEntry.MIDDLE_NAME, // 3
                        StudentEntry.GENDER, // 4
                        StudentEntry.EMAIL_ADDRESS, // 5
                        StudentEntry.CONTACT_NUMBER}, // 6
                StudentEntry._ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            entry = new StudentEntry(cursor.getLong(0),
                    cursor.getString(1),
                    cursor.isNull(2) ? null : cursor.getString(2),
                    cursor.isNull(3) ? null : cursor.getString(3),
                    cursor.isNull(4) ? null : GenderEnum.fromInt(cursor.getInt(4)),
                    cursor.isNull(5) ? null : cursor.getString(5),
                    cursor.isNull(6) ? null : cursor.getString(6));
        }
        cursor.close();
        return entry;
    }

    public static StudentEntry getEntry(long id) {
        SQLiteDatabase db = ApolloDbAdapter.open();
        StudentEntry entry = _getEntry(db, id);
        ApolloDbAdapter.close();
        return entry;
    }

    public static ArrayList<StudentEntry> _getEntries(SQLiteDatabase db) {
        ArrayList<StudentEntry> entries = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{StudentEntry._ID, // 0
                        StudentEntry.LAST_NAME, // 1
                        StudentEntry.FIRST_NAME, // 2
                        StudentEntry.MIDDLE_NAME, // 3
                        StudentEntry.GENDER, // 4
                        StudentEntry.EMAIL_ADDRESS, // 5
                        StudentEntry.CONTACT_NUMBER}, // 6
                null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            entries.add(new StudentEntry(cursor.getLong(0),
                    cursor.getString(1),
                    cursor.isNull(2) ? null : cursor.getString(2),
                    cursor.isNull(3) ? null : cursor.getString(3),
                    cursor.isNull(4) ? null : GenderEnum.fromInt(cursor.getInt(4)),
                    cursor.isNull(5) ? null : cursor.getString(5),
                    cursor.isNull(6) ? null : cursor.getString(6)));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public static ArrayList<StudentEntry> getEntries() {
        SQLiteDatabase db = ApolloDbAdapter.open();
        ArrayList<StudentEntry> entries = _getEntries(db);
        ApolloDbAdapter.close();
        return entries;
    }

    public static class StudentEntry implements BaseColumns, Serializable {

        public static final String LAST_NAME = "LastName";
        public static final String FIRST_NAME = "FirstName";
        public static final String MIDDLE_NAME = "MiddleName";
        public static final String GENDER = "Gender";
        public static final String EMAIL_ADDRESS = "EmailAddress";
        public static final String CONTACT_NUMBER = "ContactNumber";

        private long mId; // 0
        private String mLastName; // 1
        private String mFirstName; // 2 nullable
        private String mMiddleName; // 3 nullable
        private GenderEnum mGender; // 4 nullable
        private String mEmailAddress; // 5 nullable
        private String mContactNumber; // 6 nullable

        public StudentEntry(long id, String lastName, String firstName, String middleName, GenderEnum gender, String emailAddress, String contactNumber) {
            mId = id;
            mLastName = lastName;
            mFirstName = firstName;
            mMiddleName = middleName;
            mGender = gender;
            mEmailAddress = emailAddress;
            mContactNumber = contactNumber;
        }

        public long getId() {
            return mId;
        }

        public void setId(long id) {
            mId = id;
        }

        public String getLastName() {
            return mLastName;
        }

        public void setLastName(String lastName) {
            mLastName = lastName;
        }

        public String getFirstName() {
            return mFirstName;
        }

        public void setFirstName(String firstName) {
            mFirstName = firstName;
        }

        public String getMiddleName() {
            return mMiddleName;
        }

        public void setMiddleName(String middleName) {
            mMiddleName = middleName;
        }

        public GenderEnum getGender() {
            return mGender;
        }

        public void setGender(GenderEnum gender) {
            mGender = gender;
        }

        public String getEmailAddress() {
            return mEmailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            mEmailAddress = emailAddress;
        }

        public String getContactNumber() {
            return mContactNumber;
        }

        public void setContactNumber(String contactNumber) {
            mContactNumber = contactNumber;
        }

        @Override
        public boolean equals(Object object) {
            if(!(object instanceof StudentEntry))
                return false;
            if(object == this)
                return true;
            StudentEntry entry = (StudentEntry) object;
            return new EqualsBuilder()
                    .append(mId, entry.mId).isEquals();
        }

        @Override
        public int hashCode() {
            // TODO use next hash 23,29
            return new HashCodeBuilder(17, 19)
                    .append(mId).toHashCode();
        }
    }
}
