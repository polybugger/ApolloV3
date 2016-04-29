package net.polybugger.apollot.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    public static final String SELECT_TABLE_SQL1 = "SELECT " +
            "cs." + ClassStudentEntry._ID + ", " + // 0
            "cs." + ClassStudentEntry.CLASS_ID + ", " + // 1
            "cs." + ClassStudentEntry.STUDENT_ID + ", " + // 2
            "s." + StudentContract.StudentEntry.LAST_NAME + ", " + // 3
            "s." + StudentContract.StudentEntry.FIRST_NAME + ", " + // 4 nullable
            "s." + StudentContract.StudentEntry.MIDDLE_NAME + ", " + // 5 nullable
            "s." + StudentContract.StudentEntry.GENDER + ", " + // 6 nullable
            "s." + StudentContract.StudentEntry.EMAIL_ADDRESS + ", " + // 7 nullable
            "s." + StudentContract.StudentEntry.CONTACT_NUMBER + ", " + // 8 nullable
            "cs." + ClassStudentEntry.DATE_CREATED + // 9 nullable
            " FROM ";
    public static final String SELECT_TABLE_SQL2 = " AS cs INNER JOIN " +
            ClassContract.TABLE_NAME + " AS c ON cs." + ClassStudentEntry.CLASS_ID + "=c." + ClassContract.ClassEntry._ID +
            " INNER JOIN " + StudentContract.TABLE_NAME + " AS s ON cs." + ClassStudentEntry.STUDENT_ID + "=s." + StudentContract.StudentEntry._ID;
    public static final String DELETE_ALL_SQL = "DELETE FROM ";

    private ClassStudentContract() { }

    public static long _insert(SQLiteDatabase db, long classId, long studentId, Date dateCreated) {
        String tableName = TABLE_NAME + String.valueOf(classId);
        db.execSQL(CREATE_TABLE_SQL1 + tableName + CREATE_TABLE_SQL2);
        ContentValues values = new ContentValues();
        values.put(ClassStudentEntry.CLASS_ID, classId);
        values.put(ClassStudentEntry.STUDENT_ID, studentId);
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_TIME_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        values.put(ClassStudentEntry.DATE_CREATED, (dateCreated != null) ? sdf.format(dateCreated) : null);
        return db.insert(tableName, null, values);
    }

    public static long _insert(SQLiteDatabase db, long classId, StudentContract.StudentEntry student, Date dateCreated) {
        return _insert(db, classId, student.getId(), dateCreated);
    }

    public static int _delete(SQLiteDatabase db, long id, long classId) {
        return db.delete(TABLE_NAME + String.valueOf(classId), ClassStudentEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static ClassStudentEntry _getEntry(SQLiteDatabase db, long id, long classId) {
        String tableName = TABLE_NAME + String.valueOf(classId);
        db.execSQL(CREATE_TABLE_SQL1 + tableName + CREATE_TABLE_SQL2);
        ClassStudentEntry entry = null;
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_TIME_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        Date dateCreated;
        Cursor cursor = db.query(tableName + " AS cs INNER JOIN " +
                        ClassContract.TABLE_NAME + " AS c ON cs." + ClassStudentEntry.CLASS_ID + "=c." + ClassContract.ClassEntry._ID +
                        " INNER JOIN " + StudentContract.TABLE_NAME + " AS s ON cs." + ClassStudentEntry.STUDENT_ID + "=s." + StudentContract.StudentEntry._ID,
                new String[]{"cs." + ClassStudentEntry._ID, // 0
                        "cs." + ClassStudentEntry.CLASS_ID, // 1
                        "cs." + ClassStudentEntry.STUDENT_ID, // 2
                        "s." + StudentContract.StudentEntry.LAST_NAME, // 3
                        "s." + StudentContract.StudentEntry.FIRST_NAME, // 4 nullable
                        "s." + StudentContract.StudentEntry.MIDDLE_NAME, // 5 nullable
                        "s." + StudentContract.StudentEntry.GENDER, // 6 nullable
                        "s." + StudentContract.StudentEntry.EMAIL_ADDRESS, // 7 nullable
                        "s." + StudentContract.StudentEntry.CONTACT_NUMBER, // 8 nullable
                        "cs." + ClassStudentEntry.DATE_CREATED}, // 9 nullable
                "cs." + ClassStudentEntry._ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            try {
                dateCreated = sdf.parse(cursor.getString(9));
            }
            catch(Exception e) {
                dateCreated = null;
            }
            entry = new ClassStudentEntry(cursor.getLong(0),
                    cursor.getLong(1),
                    new StudentContract.StudentEntry(cursor.getLong(2),
                            cursor.getString(3),
                            cursor.isNull(4) ? null : cursor.getString(4),
                            cursor.isNull(5) ? null : cursor.getString(5),
                            cursor.isNull(6) ? null : GenderEnum.fromInt(cursor.getInt(6)),
                            cursor.isNull(7) ? null : cursor.getString(7),
                            cursor.isNull(8) ? null : cursor.getString(8)),
                    dateCreated); // 9
        }
        cursor.close();
        return entry;
    }

    public static long _getCount(SQLiteDatabase db, long classId) {
        String tableName = TABLE_NAME + String.valueOf(classId);
        db.execSQL(CREATE_TABLE_SQL1 + tableName + CREATE_TABLE_SQL2);
        long count = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast())
            count = cursor.getLong(0);
        cursor.close();
        return count;
    }

    public static class ClassStudentEntry implements BaseColumns {

        public static final String CLASS_ID = "ClassId";
        public static final String STUDENT_ID = "StudentId";
        public static final String DATE_CREATED = "DateCreated";

        private long mId; // 0
        private long mClassId; // 1
        private StudentContract.StudentEntry mStudent; // 2
        private Date mDateCreated; // 3 nullable

        public ClassStudentEntry(long id, long classId, StudentContract.StudentEntry student, Date dateCreated) {
            mId = id;
            mClassId = classId;
            mStudent = student;
            mDateCreated = dateCreated;
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

        public StudentContract.StudentEntry getStudent() {
            return mStudent;
        }

        public void setStudent(StudentContract.StudentEntry student) {
            mStudent = student;
        }

        public Date getDateCreated() {
            return mDateCreated;
        }

        public void setDateCreated(Date dateCreated) {
            mDateCreated = dateCreated;
        }

        @Override
        public boolean equals(Object object) {
            if(!(object instanceof ClassStudentEntry))
                return false;
            if(object == this)
                return true;
            ClassStudentEntry entry = (ClassStudentEntry) object;
            return new EqualsBuilder()
                    .append(mId, entry.mId)
                    .append(mClassId, entry.mClassId)
                    .append(mStudent.getId(), entry.mStudent.getId()).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(59, 61)
                    .append(mId)
                    .append(mClassId)
                    .append(mStudent.getId()).toHashCode();
        }
    }
}
