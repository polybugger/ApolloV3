package net.polybugger.apollot.db;

// TODO primes used: academicTerm 2,3, itemType 5,7, class 11,13, student 17,19, schedule 23,29, note 31,37, password 41,43, classItem 47,53, classStudent 59,61, classGradeBreakdown 67,71, classItemRecord 73,79, classItemNote 83,89, classItemSubTotalSummary 97,101
// TODO use next hash 103,107

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

            /*
            ClassScheduleContract._insertDummyClassSchedule(db, class0Id, R.array.default_class_1_class_schedule_0, sAppContext);
            ClassScheduleContract._insertDummyClassSchedule(db, class0Id, R.array.default_class_3_class_schedule_0, sAppContext);
            ClassScheduleContract._insertDummyClassSchedule(db, class0Id, R.array.default_class_0_class_schedule_0, sAppContext);
            ClassScheduleContract._insertDummyClassSchedule(db, class0Id, R.array.default_class_1_class_schedule_0, sAppContext);
            ClassScheduleContract._insertDummyClassSchedule(db, class0Id, R.array.default_class_3_class_schedule_0, sAppContext);
            ClassScheduleContract._insertDummyClassSchedule(db, class0Id, R.array.default_class_0_class_schedule_0, sAppContext);
            ClassScheduleContract._insertDummyClassSchedule(db, class0Id, R.array.default_class_1_class_schedule_0, sAppContext);
            */

            ClassScheduleContract._insertDummyClassSchedule(db, class1Id, R.array.default_class_1_class_schedule_0, sAppContext);
            ClassScheduleContract._insertDummyClassSchedule(db, class2Id, R.array.default_class_0_class_schedule_0, sAppContext);
            ClassScheduleContract._insertDummyClassSchedule(db, class2Id, R.array.default_class_1_class_schedule_0, sAppContext);
            ClassScheduleContract._insertDummyClassSchedule(db, class3Id, R.array.default_class_3_class_schedule_0, sAppContext);

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

            ClassStudentContract._insert(db, class0Id, student0Id, new Date());
            ClassStudentContract._insert(db, class0Id, student1Id, new Date());
            ClassStudentContract._insert(db, class0Id, student2Id, new Date());
            ClassStudentContract._insert(db, class0Id, student3Id, new Date());
            ClassStudentContract._insert(db, class0Id, student4Id, new Date());
            ClassStudentContract._insert(db, class0Id, student5Id, new Date());
            ClassStudentContract._insert(db, class0Id, student6Id, new Date());
            ClassStudentContract._insert(db, class0Id, student7Id, new Date());
            ClassStudentContract._insert(db, class0Id, student8Id, new Date());
            ClassStudentContract._insert(db, class0Id, student9Id, new Date());
            ClassStudentContract._insert(db, class0Id, student10Id, new Date());
            ClassStudentContract._insert(db, class0Id, student11Id, new Date());
            ClassStudentContract._insert(db, class0Id, student12Id, new Date());
            ClassStudentContract._insert(db, class0Id, student13Id, new Date());
            ClassStudentContract._insert(db, class0Id, student14Id, new Date());
            ClassStudentContract._insert(db, class0Id, student15Id, new Date());
            ClassStudentContract._insert(db, class0Id, student16Id, new Date());
            ClassStudentContract._insert(db, class0Id, student17Id, new Date());
            ClassStudentContract._insert(db, class0Id, student18Id, new Date());
            ClassStudentContract._insert(db, class0Id, student19Id, new Date());
            ClassStudentContract._insert(db, class0Id, student20Id, new Date());
            ClassStudentContract._insert(db, class0Id, student21Id, new Date());
            ClassStudentContract._insert(db, class0Id, student22Id, new Date());
            ClassStudentContract._insert(db, class0Id, student23Id, new Date());
            ClassStudentContract._insert(db, class0Id, student24Id, new Date());
            ClassStudentContract._insert(db, class0Id, student25Id, new Date());
            ClassStudentContract._insert(db, class0Id, student26Id, new Date());
            ClassStudentContract._insert(db, class0Id, student27Id, new Date());
            ClassStudentContract._insert(db, class0Id, student28Id, new Date());
            ClassStudentContract._insert(db, class0Id, student29Id, new Date());
            ClassStudentContract._insert(db, class0Id, student30Id, new Date());
            ClassStudentContract._insert(db, class0Id, student31Id, new Date());
            ClassStudentContract._insert(db, class0Id, student32Id, new Date());
            ClassStudentContract._insert(db, class0Id, student33Id, new Date());
            ClassStudentContract._insert(db, class0Id, student34Id, new Date());
            ClassStudentContract._insert(db, class0Id, student35Id, new Date());
            ClassStudentContract._insert(db, class0Id, student36Id, new Date());
            ClassStudentContract._insert(db, class0Id, student37Id, new Date());
            ClassStudentContract._insert(db, class0Id, student38Id, new Date());
            ClassStudentContract._insert(db, class0Id, student39Id, new Date());
            ClassStudentContract._insert(db, class0Id, student40Id, new Date());
            ClassStudentContract._insert(db, class0Id, student41Id, new Date());
            ClassStudentContract._insert(db, class0Id, student42Id, new Date());

            long class0Item0Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_0, sAppContext);
            long class0Item1Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_1, sAppContext);
            long class0Item2Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_2, sAppContext);
            long class0Item3Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_3, sAppContext);
            long class0Item4Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_4, sAppContext);
            long class0Item5Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_5, sAppContext);
            long class0Item6Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_6, sAppContext);
            long class0Item7Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_7, sAppContext);
            long class0Item8Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_8, sAppContext);
            long class0Item9Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_9, sAppContext);
            long class0Item10Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_10, sAppContext);
            long class0Item11Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_11, sAppContext);
            long class0Item12Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_12, sAppContext);
            long class0Item13Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_13, sAppContext);
            long class0Item14Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_14, sAppContext);
            long class0Item15Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_15, sAppContext);
            long class0Item16Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_16, sAppContext);
            long class0Item17Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_17, sAppContext);
            long class0Item18Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_18, sAppContext);
            long class0Item19Id = ClassItemContract._insertDummyClassItem(db, class0Id, R.array.default_class_0_class_item_19, sAppContext);

            ClassStudentContract._insert(db, class2Id, student0Id, new Date());
            ClassStudentContract._insert(db, class2Id, student1Id, new Date());
            ClassStudentContract._insert(db, class2Id, student2Id, new Date());
            ClassStudentContract._insert(db, class2Id, student3Id, new Date());
            ClassStudentContract._insert(db, class2Id, student4Id, new Date());
            ClassStudentContract._insert(db, class2Id, student5Id, new Date());
            ClassStudentContract._insert(db, class2Id, student6Id, new Date());
            ClassStudentContract._insert(db, class2Id, student7Id, new Date());

            long class1Item0Id = ClassItemContract._insertDummyClassItem(db, class1Id, R.array.default_class_0_class_item_0, sAppContext);
            long class1Item1Id = ClassItemContract._insertDummyClassItem(db, class1Id, R.array.default_class_0_class_item_1, sAppContext);

            db.execSQL(ClassPasswordContract.CREATE_TABLE_SQL);
            ClassPasswordContract._insertDummyClassPassword(db, class1Id, R.string.default_class_1_password, sAppContext);
            ClassPasswordContract._insertDummyClassPassword(db, class2Id, R.string.default_class_1_password, sAppContext);

            ClassGradeBreakdownContract._insertDummyClassGradeBreakdown(db, class0Id, R.array.default_class_0_class_grade_breakdown_0, sAppContext);
            ClassGradeBreakdownContract._insertDummyClassGradeBreakdown(db, class0Id, R.array.default_class_0_class_grade_breakdown_1, sAppContext);
            ClassGradeBreakdownContract._insertDummyClassGradeBreakdown(db, class0Id, R.array.default_class_0_class_grade_breakdown_2, sAppContext);
            ClassGradeBreakdownContract._insertDummyClassGradeBreakdown(db, class0Id, R.array.default_class_0_class_grade_breakdown_3, sAppContext);
            ClassGradeBreakdownContract._insertDummyClassGradeBreakdown(db, class0Id, R.array.default_class_0_class_grade_breakdown_4, sAppContext);

            db.execSQL(ClassNoteContract.CREATE_TABLE_SQL);
            ClassNoteContract._insertDummyClassNote(db, class0Id, R.array.default_class_0_note_0, sAppContext);
            ClassNoteContract._insertDummyClassNote(db, class0Id, R.array.default_class_0_note_1, sAppContext);
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
}
