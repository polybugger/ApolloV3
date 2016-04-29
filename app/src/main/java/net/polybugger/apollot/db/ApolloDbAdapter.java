package net.polybugger.apollot.db;

// TODO primes used: academicTerm 2,3, itemType 5,7, class 11,13, student 17,19, schedule 23,29, note 31,37, password 41,43, classItem 47,53, classStudent 59,61
// TODO use next hash 67,71

// TODO application tag in manifest needs to be google indexed

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.polybugger.apollot.R;

public class ApolloDbAdapter {

    public static final String EN_LANGUAGE = "en";
    public static final String JA_LANGUAGE = "ja";
    public static final String DATABASE_NAME = "ApolloDb.sqlite3";
    public static final int DATABASE_VERSION = 3;

    private static Context sAppContext = null;
    private static ApolloDbHelper sDbHelper = null;
    private static int sOpenCounter = 0;
    private static SQLiteDatabase sDb = null;

    private ApolloDbAdapter() { }

    public static void setAppContext(Context context) {
        if(sDbHelper == null) {
            sAppContext = context.getApplicationContext();
            sDbHelper = new ApolloDbHelper(sAppContext);
        }
    }

    public static Context getAppContext() {
        return sAppContext;
    }

    public static synchronized SQLiteDatabase open() throws SQLException {
        if(sOpenCounter == 0)
            sDb = sDbHelper.getWritableDatabase();
        sOpenCounter++;
        return sDb;
    }

    public static synchronized void close() {
        if(sOpenCounter > 0) {
            if(sOpenCounter == 1)
                sDb.close();
            sOpenCounter--;
        }
    }

    private static class ApolloDbHelper extends SQLiteOpenHelper {

        public ApolloDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            db.setForeignKeyConstraintsEnabled(true);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(AcademicTermContract.CREATE_TABLE_SQL);
            AcademicTermContract._insertDefaultAcademicTerms(db, sAppContext);

            db.execSQL(ClassItemTypeContract.CREATE_TABLE_SQL);
            ClassItemTypeContract._insertDefaultClassItemTypes(db, sAppContext);

            db.execSQL(ClassContract.CREATE_TABLE_SQL);
            long class0Id = ClassContract._insertDummyClass(db, R.array.default_class_0, sAppContext);
            long class1Id = ClassContract._insertDummyClass(db, R.array.default_class_1, sAppContext);
            long class2Id = ClassContract._insertDummyClass(db, R.array.default_class_2, sAppContext);
            long class3Id = ClassContract._insertDummyClass(db, R.array.default_class_3, sAppContext);

            db.execSQL(ClassScheduleContract.CREATE_TABLE_SQL);
            ClassScheduleContract._insertDummyClassSchedule(db, class0Id, R.array.default_class_0_class_schedule_0, sAppContext);
            ClassScheduleContract._insertDummyClassSchedule(db, class1Id, R.array.default_class_1_class_schedule_0, sAppContext);
            ClassScheduleContract._insertDummyClassSchedule(db, class2Id, R.array.default_class_0_class_schedule_0, sAppContext);
            ClassScheduleContract._insertDummyClassSchedule(db, class2Id, R.array.default_class_1_class_schedule_0, sAppContext);

            db.execSQL(StudentContract.CREATE_TABLE_SQL);
            long student0Id = StudentContract._insertDummyStudent(db, R.array.default_student_0, sAppContext);
            long student1Id = StudentContract._insertDummyStudent(db, R.array.default_student_1, sAppContext);
            long student2Id = StudentContract._insertDummyStudent(db, R.array.default_student_2, sAppContext);
            long student3Id = StudentContract._insertDummyStudent(db, R.array.default_student_3, sAppContext);
            long student4Id = StudentContract._insertDummyStudent(db, R.array.default_student_4, sAppContext);
            long student5Id = StudentContract._insertDummyStudent(db, R.array.default_student_5, sAppContext);
            long student6Id = StudentContract._insertDummyStudent(db, R.array.default_student_6, sAppContext);
            long student7Id = StudentContract._insertDummyStudent(db, R.array.default_student_7, sAppContext);
            long student8Id = StudentContract._insertDummyStudent(db, R.array.default_student_8, sAppContext);
            long student9Id = StudentContract._insertDummyStudent(db, R.array.default_student_9, sAppContext);
            long student10Id = StudentContract._insertDummyStudent(db, R.array.default_student_10, sAppContext);
            long student11Id = StudentContract._insertDummyStudent(db, R.array.default_student_11, sAppContext);
            long student12Id = StudentContract._insertDummyStudent(db, R.array.default_student_12, sAppContext);
            long student13Id = StudentContract._insertDummyStudent(db, R.array.default_student_13, sAppContext);
            long student14Id = StudentContract._insertDummyStudent(db, R.array.default_student_14, sAppContext);
            long student15Id = StudentContract._insertDummyStudent(db, R.array.default_student_15, sAppContext);
            long student16Id = StudentContract._insertDummyStudent(db, R.array.default_student_16, sAppContext);
            long student17Id = StudentContract._insertDummyStudent(db, R.array.default_student_17, sAppContext);
            long student18Id = StudentContract._insertDummyStudent(db, R.array.default_student_18, sAppContext);
            long student19Id = StudentContract._insertDummyStudent(db, R.array.default_student_19, sAppContext);
            long student20Id = StudentContract._insertDummyStudent(db, R.array.default_student_20, sAppContext);
            long student21Id = StudentContract._insertDummyStudent(db, R.array.default_student_21, sAppContext);
            long student22Id = StudentContract._insertDummyStudent(db, R.array.default_student_22, sAppContext);
            long student23Id = StudentContract._insertDummyStudent(db, R.array.default_student_23, sAppContext);
            long student24Id = StudentContract._insertDummyStudent(db, R.array.default_student_24, sAppContext);
            long student25Id = StudentContract._insertDummyStudent(db, R.array.default_student_25, sAppContext);
            long student26Id = StudentContract._insertDummyStudent(db, R.array.default_student_26, sAppContext);
            long student27Id = StudentContract._insertDummyStudent(db, R.array.default_student_27, sAppContext);
            long student28Id = StudentContract._insertDummyStudent(db, R.array.default_student_28, sAppContext);
            long student29Id = StudentContract._insertDummyStudent(db, R.array.default_student_29, sAppContext);
            long student30Id = StudentContract._insertDummyStudent(db, R.array.default_student_30, sAppContext);
            long student31Id = StudentContract._insertDummyStudent(db, R.array.default_student_31, sAppContext);
            long student32Id = StudentContract._insertDummyStudent(db, R.array.default_student_32, sAppContext);
            long student33Id = StudentContract._insertDummyStudent(db, R.array.default_student_33, sAppContext);
            long student34Id = StudentContract._insertDummyStudent(db, R.array.default_student_34, sAppContext);
            long student35Id = StudentContract._insertDummyStudent(db, R.array.default_student_35, sAppContext);
            long student36Id = StudentContract._insertDummyStudent(db, R.array.default_student_36, sAppContext);
            long student37Id = StudentContract._insertDummyStudent(db, R.array.default_student_37, sAppContext);
            long student38Id = StudentContract._insertDummyStudent(db, R.array.default_student_38, sAppContext);
            long student39Id = StudentContract._insertDummyStudent(db, R.array.default_student_39, sAppContext);
            long student40Id = StudentContract._insertDummyStudent(db, R.array.default_student_40, sAppContext);
            long student41Id = StudentContract._insertDummyStudent(db, R.array.default_student_41, sAppContext);
            long student42Id = StudentContract._insertDummyStudent(db, R.array.default_student_42, sAppContext);

            /*
            db.execSQL(ClassNoteContract.CREATE_TABLE_SQL);
            _insertDummyClass0Notes(db, class0Id);

            db.execSQL(ClassPasswordContract.CREATE_TABLE_SQL);
            _insertDummyClass1Password(db, class1Id);

            _insertDummyClass0Items(db, class0Id);
            */
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


    public static void _insertDummyClass0Notes(SQLiteDatabase db, long classId) {
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, sAppContext.getResources().getConfiguration().locale);
        Date dateCreated;
        try {
            dateCreated = sdf.parse(sAppContext.getString(R.string.default_class_0_class_note_0_date_created));
        }
        catch(Exception e) {
            dateCreated = null;
        }
        ClassNoteContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_note_0_note),
                dateCreated);
        try {
            dateCreated = sdf.parse(sAppContext.getString(R.string.default_class_0_class_note_1_date_created));
        }
        catch(Exception e) {
            dateCreated = null;
        }
        ClassNoteContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_note_1_note),
                dateCreated);
    }

    public static long _insertDummyClass1Password(SQLiteDatabase db, long classId) {
        return ClassPasswordContract._insert(db, classId, sAppContext.getString(R.string.default_class_1_password));
    }

    public static void _insertDummyClass0Items(SQLiteDatabase db, long classId) {
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DB_TEMPLATE, sAppContext.getResources().getConfiguration().locale);
        Date itemDate, submissionDueDate;
        Float perfectScore;

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_0_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_0_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_0_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_0_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_0_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_0_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_0_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_0_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_1_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_1_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_1_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_1_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_1_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_1_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_1_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_1_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_2_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_2_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_2_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_2_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_2_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_2_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_2_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_2_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_3_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_3_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_3_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_3_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_3_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_3_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_3_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_3_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_4_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_4_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_4_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_4_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_4_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_4_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_4_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_4_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_5_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_5_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_5_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_5_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_5_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_5_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_5_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_5_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_6_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_6_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_6_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_6_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_6_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_6_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_6_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_6_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_7_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_7_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_7_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_7_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_7_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_7_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_7_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_7_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_8_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_8_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_8_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_8_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_8_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_8_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_8_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_8_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_9_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_9_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_9_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_9_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_9_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_9_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_9_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_9_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_10_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_10_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_10_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_10_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_10_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_10_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_10_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_10_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_11_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_11_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_11_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_11_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_11_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_11_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_11_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_11_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_12_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_12_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_12_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_12_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_12_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_12_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_12_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_12_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_13_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_13_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_13_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_13_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_13_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_13_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_13_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_13_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_14_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_14_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_14_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_14_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_14_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_14_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_14_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_14_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_15_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_15_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_15_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_15_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_15_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_15_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_15_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_15_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_16_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_16_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_16_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_16_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_16_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_16_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_16_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_16_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_17_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_17_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_17_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_17_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_17_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_17_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_17_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_17_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_18_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_18_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_18_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_18_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_18_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_18_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_18_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_18_record_submissions),
                submissionDueDate);

        try {
            itemDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_19_item_date));
        }
        catch(Exception e) {
            itemDate = null;
        }
        try {
            submissionDueDate = sdf.parse(sAppContext.getString(R.string.default_class_0_class_item_19_submission_due_date));
        }
        catch(Exception e) {
            submissionDueDate = null;
        }
        try {
            perfectScore = Float.parseFloat(sAppContext.getString(R.string.default_class_0_class_item_19_perfect_score));
        }
        catch(Exception e) {
            perfectScore = null;
        }
        ClassItemContract._insert(db, classId,
                sAppContext.getString(R.string.default_class_0_class_item_19_description),
                ClassItemTypeContract._getEntryByDescription(db, sAppContext.getString(R.string.default_class_0_class_item_19_item_type)),
                itemDate,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_19_check_attendance),
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_19_record_scores),
                perfectScore,
                sAppContext.getResources().getBoolean(R.bool.default_class_0_class_item_19_record_submissions),
                submissionDueDate);

    }
}
