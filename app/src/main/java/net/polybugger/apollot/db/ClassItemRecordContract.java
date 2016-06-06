package net.polybugger.apollot.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ClassItemRecordContract {

    public static final String TABLE_NAME = "ClassItemRecords_";
    public static final String CREATE_TABLE_SQL1 = "CREATE TABLE IF NOT EXISTS ";
    public static final String CREATE_TABLE_SQL2 = " (" +
            ClassItemRecordEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassItemRecordEntry.CLASS_ID + " INTEGER NOT NULL REFERENCES " +
                ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + ") ON DELETE CASCADE, " +
            ClassItemRecordEntry.ITEM_ID + " INTEGER NOT NULL REFERENCES ";
    public static final String CREATE_TABLE_SQL3 = " (" + ClassItemContract.ClassItemEntry._ID + ") ON DELETE CASCADE, " +
            ClassItemRecordEntry.STUDENT_ID + " INTEGER NOT NULL REFERENCES ";
    public static final String CREATE_TABLE_SQL4 = " (" + ClassStudentContract.ClassStudentEntry.STUDENT_ID + ") ON DELETE CASCADE, " +
            ClassItemRecordEntry.ATTENDANCE + " INTEGER NULL, " +
            ClassItemRecordEntry.SCORE + " REAL NULL, " +
            ClassItemRecordEntry.SUBMISSION_DATE + " TEXT NULL, " +
            ClassItemRecordEntry.REMARKS + " TEXT NULL, " +
            "UNIQUE (" + ClassItemRecordEntry.ITEM_ID + ", " + ClassItemRecordEntry.STUDENT_ID + "))";

    private ClassItemRecordContract() { }

    public static long _insert(SQLiteDatabase db, long classId, long classItemId, ClassStudentContract.ClassStudentEntry classStudent, Boolean attendance, Float score, Date submissionDate, String remarks) {
        String tableName1 = TABLE_NAME + String.valueOf(classId);
        String tableName2 = ClassItemContract.TABLE_NAME + String.valueOf(classId);
        String tableName3 = ClassStudentContract.TABLE_NAME + String.valueOf(classId);
        db.execSQL(CREATE_TABLE_SQL1 + tableName1 + CREATE_TABLE_SQL2 + tableName2 + CREATE_TABLE_SQL3 + tableName3 + CREATE_TABLE_SQL4);
        ContentValues values = new ContentValues();
        values.put(ClassItemRecordEntry.CLASS_ID, classId);
        values.put(ClassItemRecordEntry.ITEM_ID, classItemId);
        values.put(ClassItemRecordEntry.STUDENT_ID, classStudent.getStudent().getId());
        values.put(ClassItemRecordEntry.ATTENDANCE, attendance);
        values.put(ClassItemRecordEntry.SCORE, score);
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        values.put(ClassItemRecordEntry.SUBMISSION_DATE, (submissionDate != null) ? sdf.format(submissionDate) : null);
        values.put(ClassItemRecordEntry.REMARKS, remarks);
        return db.insert(tableName1, null, values);
    }

    public static int _update(SQLiteDatabase db, long id, long classId, long classItemId, ClassStudentContract.ClassStudentEntry classStudent, Boolean attendance, Float score, Date submissionDate, String remarks) {
        ContentValues values = new ContentValues();
        // do not allow to change class id, item id, student id
        values.put(ClassItemRecordEntry.ATTENDANCE, attendance);
        values.put(ClassItemRecordEntry.SCORE, score);
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        values.put(ClassItemRecordEntry.SUBMISSION_DATE, (submissionDate != null) ? sdf.format(submissionDate) : null);
        values.put(ClassItemRecordEntry.REMARKS, remarks);
        return db.update(TABLE_NAME + String.valueOf(classId), values, ClassItemRecordEntry._ID + "=? AND " + ClassItemContract.ClassItemEntry._ID + "=? AND " + StudentContract.StudentEntry._ID + "=?",
                new String[]{String.valueOf(id), String.valueOf(classItemId), String.valueOf(classStudent.getStudent().getId())});
    }

    public static ArrayList<ClassItemRecordEntry> _getEntriesByClassItem(SQLiteDatabase db, long classId, long itemId) {
        String tableName1 = TABLE_NAME + String.valueOf(classId);
        String tableName2 = ClassItemContract.TABLE_NAME + String.valueOf(classId);
        String tableName3 = ClassStudentContract.TABLE_NAME + String.valueOf(classId);
        ArrayList<ClassItemRecordEntry> entries = new ArrayList<>();
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        Long recordId; Boolean attendance; Float score; Date submissionDate, dateCreated;
        db.execSQL(CREATE_TABLE_SQL1 + tableName1 + CREATE_TABLE_SQL2 + tableName2 + CREATE_TABLE_SQL3 + tableName3 + CREATE_TABLE_SQL4);
        Cursor cursor = db.query(tableName3  +
                        " AS cs INNER JOIN " + StudentContract.TABLE_NAME +
                        " AS s ON cs." + ClassStudentContract.ClassStudentEntry.STUDENT_ID + "=s." + StudentContract.StudentEntry._ID +
                        " LEFT OUTER JOIN (SELECT ci." + ClassItemRecordEntry._ID + ", " + ClassItemRecordEntry.STUDENT_ID + ", " + ClassItemRecordEntry.ATTENDANCE +
                        ", " + ClassItemRecordEntry.SCORE + ", " + ClassItemRecordEntry.SUBMISSION_DATE + ", " + ClassItemRecordEntry.REMARKS +
                        " FROM " + tableName2 + " AS ci INNER JOIN " + tableName1 +
                        " AS cr ON ci." + ClassItemContract.ClassItemEntry._ID + "=cr." + ClassItemRecordEntry.ITEM_ID +
                        " WHERE ci." + ClassItemContract.ClassItemEntry._ID + "=" + String.valueOf(itemId) +
                        ") as cir ON cs." + StudentContract.StudentEntry._ID + "=cir." + ClassItemRecordEntry.STUDENT_ID,
                new String[]{"cs." + ClassStudentContract.ClassStudentEntry._ID, // 0
                        "cs." + ClassStudentContract.ClassStudentEntry.STUDENT_ID, // 1
                        "s." + StudentContract.StudentEntry.LAST_NAME, // 2
                        "s." + StudentContract.StudentEntry.FIRST_NAME, // 3 nullable
                        "s." + StudentContract.StudentEntry.MIDDLE_NAME, // 4 nullable
                        "s." + StudentContract.StudentEntry.GENDER, // 5 nullable
                        "s." + StudentContract.StudentEntry.EMAIL_ADDRESS, // 6 nullable
                        "s." + StudentContract.StudentEntry.CONTACT_NUMBER, // 7 nullable
                        "cs." + ClassStudentContract.ClassStudentEntry.DATE_CREATED, // 8 nullable
                        "cir." + ClassItemRecordEntry._ID, // 9 nullable
                        "cir." + ClassItemRecordEntry.ATTENDANCE, // 10  nullable
                        "cir." + ClassItemRecordEntry.SCORE, // 11  nullable
                        "cir." + ClassItemRecordEntry.SUBMISSION_DATE, // 12 nullable
                        "cir." + ClassItemRecordEntry.REMARKS}, // 13 nullable
                null, null, null, null,
                StudentContract.StudentEntry.LAST_NAME + " COLLATE NOCASE ASC, " + StudentContract.StudentEntry.FIRST_NAME + " COLLATE NOCASE ASC");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            recordId = cursor.isNull(9) ? -1 : cursor.getLong(9); // -1 for nonexistent id
            attendance = cursor.isNull(10) ? null : cursor.getInt(10) != 0;
            score = cursor.isNull(11) ? null : cursor.getFloat(11);
            try {
                submissionDate = sdf.parse(cursor.getString(12));
            }
            catch(Exception e) {
                submissionDate = null;
            }
            try {
                dateCreated = sdf.parse(cursor.getString(8));
            }
            catch(Exception e) {
                dateCreated = null;
            }
            entries.add(new ClassItemRecordEntry(recordId,
                    classId,
                    itemId,
                    new ClassStudentContract.ClassStudentEntry(cursor.getLong(0),
                            classId,
                            new StudentContract.StudentEntry(cursor.getLong(1),
                                    cursor.getString(2),
                                    cursor.isNull(4) ? null : cursor.getString(3),
                                    cursor.isNull(5) ? null : cursor.getString(4),
                                    cursor.isNull(6) ? null : GenderEnum.fromInt(cursor.getInt(5)),
                                    cursor.isNull(7) ? null : cursor.getString(6),
                                    cursor.isNull(8) ? null : cursor.getString(7)),
                            dateCreated),
                    attendance,
                    score,
                    submissionDate,
                    cursor.getString(13)));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }


    public static class ClassItemRecordEntry implements BaseColumns, Serializable {

        public static final String CLASS_ID = "ClassId";
        public static final String ITEM_ID = "ItemId";
        public static final String STUDENT_ID = "StudentId";
        public static final String ATTENDANCE = "Attendance";
        public static final String SCORE = "Score";
        public static final String SUBMISSION_DATE = "SubmissionDate";
        public static final String REMARKS = "Remarks";

        private long mId;
        private long mClassId;
        private long mClassItemId;
        private ClassStudentContract.ClassStudentEntry mClassStudent;
        private Boolean mAttendance;
        private Float mScore;
        private Date mSubmissionDate;
        private String mRemarks;

        public ClassItemRecordEntry(long id, long classId, long classItemId, ClassStudentContract.ClassStudentEntry classStudent, Boolean attendance, Float score, Date submissionDate, String remarks) {
            mId = id;
            mClassId = classId;
            mClassItemId = classItemId;
            mClassStudent = classStudent;
            mAttendance = attendance;
            mScore = score;
            mSubmissionDate = submissionDate;
            mRemarks = remarks;
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

        public long getClassItemId() {
            return mClassItemId;
        }

        public void setClassItemId(long classItemId) {
            mClassItemId = classItemId;
        }

        public ClassStudentContract.ClassStudentEntry getClassStudent() {
            return mClassStudent;
        }

        public void setClassStudent(ClassStudentContract.ClassStudentEntry classStudent) {
            mClassStudent = classStudent;
        }

        public Boolean getAttendance() {
            return mAttendance;
        }

        public void setAttendance(Boolean attendance) {
            mAttendance = attendance;
        }

        public Float getScore() {
            return mScore;
        }

        public void setScore(Float score) {
            mScore = score;
        }

        public Date getSubmissionDate() {
            return mSubmissionDate;
        }

        public void setSubmissionDate(Date submissionDate) {
            mSubmissionDate = submissionDate;
        }

        public String getRemarks() {
            return mRemarks;
        }

        public void setRemarks(String remarks) {
            mRemarks = remarks;
        }

        @Override
        public boolean equals(Object object) {
            if(!(object instanceof ClassItemRecordEntry))
                return false;
            if(object == this)
                return true;
            ClassItemRecordEntry entry = (ClassItemRecordEntry) object;
            return new EqualsBuilder()
                    .append(mId, entry.mId)
                    .append(mClassId, entry.mClassId)
                    .append(mClassStudent.getStudent().getId(), entry.mClassStudent.getStudent().getId()).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(73, 79)
                    .append(mId)
                    .append(mClassId)
                    .append(mClassStudent.getStudent().getId()).toHashCode();
        }
    }
}
