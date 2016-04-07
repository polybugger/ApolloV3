package net.polybugger.apollot.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.polybugger.apollot.R;

public class ApolloDbAdapter {

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
        if(sOpenCounter == 1)
            sDb.close();
        if(sOpenCounter > 0)
            sOpenCounter--;
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
            db.execSQL(ClassItemTypeContract.CREATE_TABLE_SQL);
            db.execSQL(StudentContract.CREATE_TABLE_SQL);
            db.execSQL(ClassContract.CREATE_TABLE_SQL);
            db.execSQL(ClassScheduleContract.CREATE_TABLE_SQL);
            db.execSQL(ClassNoteContract.CREATE_TABLE_SQL);
            db.execSQL(ClassPasswordContract.CREATE_TABLE_SQL);

            _insertDefaultAcademicTerms(db);
            _insertDefaultClassItemTypes(db);

            _insertDummyStudents(db);

            long class0Id = _insertDummyClass0(db);
            _insertDummyClass0Schedules(db, class0Id);
            long class1Id = _insertDummyClass1(db);
            _insertDummyClass1Schedules(db, class1Id);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    private static void _insertDefaultAcademicTerms(SQLiteDatabase db) {
        AcademicTermContract._insert(db, sAppContext.getString(R.string.default_academic_term_0), sAppContext.getString(R.string.default_academic_term_color_0));
        AcademicTermContract._insert(db, sAppContext.getString(R.string.default_academic_term_1), sAppContext.getString(R.string.default_academic_term_color_1));
        AcademicTermContract._insert(db, sAppContext.getString(R.string.default_academic_term_2), sAppContext.getString(R.string.default_academic_term_color_2));
        AcademicTermContract._insert(db, sAppContext.getString(R.string.default_academic_term_3), sAppContext.getString(R.string.default_academic_term_color_3));
        AcademicTermContract._insert(db, sAppContext.getString(R.string.default_academic_term_4), sAppContext.getString(R.string.default_academic_term_color_4));
        AcademicTermContract._insert(db, sAppContext.getString(R.string.default_academic_term_5), sAppContext.getString(R.string.default_academic_term_color_5));
        AcademicTermContract._insert(db, sAppContext.getString(R.string.default_academic_term_6), ColorEnum.TRANSPARENT.getValue());
        AcademicTermContract._insert(db, sAppContext.getString(R.string.default_academic_term_7), ColorEnum.TRANSPARENT.getValue());
        AcademicTermContract._insert(db, sAppContext.getString(R.string.default_academic_term_8), ColorEnum.TRANSPARENT.getValue());
        AcademicTermContract._insert(db, sAppContext.getString(R.string.default_academic_term_9), ColorEnum.TRANSPARENT.getValue());
        AcademicTermContract._insert(db, sAppContext.getString(R.string.default_academic_term_10), ColorEnum.TRANSPARENT.getValue());
        AcademicTermContract._insert(db, sAppContext.getString(R.string.default_academic_term_11), ColorEnum.TRANSPARENT.getValue());
        AcademicTermContract._insert(db, sAppContext.getString(R.string.default_academic_term_12), ColorEnum.TRANSPARENT.getValue());
    }

    private static void _insertDefaultClassItemTypes(SQLiteDatabase db) {
        ClassItemTypeContract._insert(db, sAppContext.getString(R.string.default_class_item_type_0), sAppContext.getString(R.string.default_class_item_type_color_0));
        ClassItemTypeContract._insert(db, sAppContext.getString(R.string.default_class_item_type_0a), sAppContext.getString(R.string.default_class_item_type_color_0a));
        ClassItemTypeContract._insert(db, sAppContext.getString(R.string.default_class_item_type_1), sAppContext.getString(R.string.default_class_item_type_color_1));
        ClassItemTypeContract._insert(db, sAppContext.getString(R.string.default_class_item_type_1a), sAppContext.getString(R.string.default_class_item_type_color_1a));
        ClassItemTypeContract._insert(db, sAppContext.getString(R.string.default_class_item_type_2), sAppContext.getString(R.string.default_class_item_type_color_2));
        ClassItemTypeContract._insert(db, sAppContext.getString(R.string.default_class_item_type_3), sAppContext.getString(R.string.default_class_item_type_color_3));
        ClassItemTypeContract._insert(db, sAppContext.getString(R.string.default_class_item_type_4), sAppContext.getString(R.string.default_class_item_type_color_4));
        ClassItemTypeContract._insert(db, sAppContext.getString(R.string.default_class_item_type_5), sAppContext.getString(R.string.default_class_item_type_color_5));
        ClassItemTypeContract._insert(db, sAppContext.getString(R.string.default_class_item_type_6), sAppContext.getString(R.string.default_class_item_type_color_6));
        ClassItemTypeContract._insert(db, sAppContext.getString(R.string.default_class_item_type_7), sAppContext.getString(R.string.default_class_item_type_color_7));
        ClassItemTypeContract._insert(db, sAppContext.getString(R.string.default_class_item_type_8), sAppContext.getString(R.string.default_class_item_type_color_8));
        ClassItemTypeContract._insert(db, sAppContext.getString(R.string.default_class_item_type_9), sAppContext.getString(R.string.default_class_item_type_color_9));
        ClassItemTypeContract._insert(db, sAppContext.getString(R.string.default_class_item_type_10), sAppContext.getString(R.string.default_class_item_type_color_10));
    }

    private static long _insertDummyClass0(SQLiteDatabase db) {
        AcademicTermContract.AcademicTermEntry academicTerm = AcademicTermContract._getEntry(db, sAppContext.getString(R.string.default_class_0_academic_term));
        return ClassContract._insert(db, sAppContext.getString(R.string.default_class_0_code),
                sAppContext.getString(R.string.default_class_0_description),
                academicTerm,
                (long) sAppContext.getResources().getInteger(R.integer.default_class_0_year),
                PastCurrentEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_class_0_past_current)), new Date());
    }

    private static void _insertDummyClass0Schedules(SQLiteDatabase db, long classId) {
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.TIME_DISPLAY_TEMPLATE, sAppContext.getResources().getConfiguration().locale);
        Date timeStart, timeEnd;
        try {
            timeStart = sdf.parse(sAppContext.getString(R.string.default_class_0_class_schedule_0_time_start));
        }
        catch(Exception e) {
            timeStart = null;
        }
        try {
            timeEnd = sdf.parse(sAppContext.getString(R.string.default_class_0_class_schedule_0_time_end));
        }
        catch(Exception e) {
            timeEnd = null;
        }
        ClassScheduleContract._insert(db, classId, timeStart, timeEnd,
                sAppContext.getResources().getInteger(R.integer.default_class_0_class_schedule_0_days),
                sAppContext.getString(R.string.default_class_0_class_schedule_0_room),
                sAppContext.getString(R.string.default_class_0_class_schedule_0_building),
                sAppContext.getString(R.string.default_class_0_class_schedule_0_campus));
    }

    private static long _insertDummyClass1(SQLiteDatabase db) {
        AcademicTermContract.AcademicTermEntry academicTerm = AcademicTermContract._getEntry(db, sAppContext.getString(R.string.default_class_1_academic_term));
        return ClassContract._insert(db, sAppContext.getString(R.string.default_class_1_code),
                sAppContext.getString(R.string.default_class_1_description),
                academicTerm,
                (long) sAppContext.getResources().getInteger(R.integer.default_class_1_year),
                PastCurrentEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_class_1_past_current)), new Date());
    }

    private static void _insertDummyClass1Schedules(SQLiteDatabase db, long classId) {
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.TIME_DISPLAY_TEMPLATE, sAppContext.getResources().getConfiguration().locale);
        Date timeStart, timeEnd;
        try {
            timeStart = sdf.parse(sAppContext.getString(R.string.default_class_1_class_schedule_0_time_start));
        }
        catch(Exception e) {
            timeStart = null;
        }
        try {
            timeEnd = sdf.parse(sAppContext.getString(R.string.default_class_1_class_schedule_0_time_end));
        }
        catch(Exception e) {
            timeEnd = null;
        }
        ClassScheduleContract._insert(db, classId, timeStart, timeEnd,
                sAppContext.getResources().getInteger(R.integer.default_class_1_class_schedule_0_days),
                sAppContext.getString(R.string.default_class_1_class_schedule_0_room),
                sAppContext.getString(R.string.default_class_1_class_schedule_0_building),
                sAppContext.getString(R.string.default_class_1_class_schedule_0_campus));
    }

    private static void _insertDummyStudents(SQLiteDatabase db) {
        StudentContract._insert(db, sAppContext.getString(R.string.default_student_0_last_name),
                sAppContext.getString(R.string.default_student_0_first_name),
                sAppContext.getString(R.string.default_student_0_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_0_gender)),
                sAppContext.getString(R.string.default_student_0_email_address),
                sAppContext.getString(R.string.default_student_0_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_1_last_name),
                sAppContext.getString(R.string.default_student_1_first_name),
                sAppContext.getString(R.string.default_student_1_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_1_gender)),
                sAppContext.getString(R.string.default_student_1_email_address),
                sAppContext.getString(R.string.default_student_1_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_2_last_name),
                sAppContext.getString(R.string.default_student_2_first_name),
                sAppContext.getString(R.string.default_student_2_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_2_gender)),
                sAppContext.getString(R.string.default_student_2_email_address),
                sAppContext.getString(R.string.default_student_2_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_3_last_name),
                sAppContext.getString(R.string.default_student_3_first_name),
                sAppContext.getString(R.string.default_student_3_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_3_gender)),
                sAppContext.getString(R.string.default_student_3_email_address),
                sAppContext.getString(R.string.default_student_3_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_4_last_name),
                sAppContext.getString(R.string.default_student_4_first_name),
                sAppContext.getString(R.string.default_student_4_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_4_gender)),
                sAppContext.getString(R.string.default_student_4_email_address),
                sAppContext.getString(R.string.default_student_4_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_5_last_name),
                sAppContext.getString(R.string.default_student_5_first_name),
                sAppContext.getString(R.string.default_student_5_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_5_gender)),
                sAppContext.getString(R.string.default_student_5_email_address),
                sAppContext.getString(R.string.default_student_5_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_6_last_name),
                sAppContext.getString(R.string.default_student_6_first_name),
                sAppContext.getString(R.string.default_student_6_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_6_gender)),
                sAppContext.getString(R.string.default_student_6_email_address),
                sAppContext.getString(R.string.default_student_6_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_7_last_name),
                sAppContext.getString(R.string.default_student_7_first_name),
                sAppContext.getString(R.string.default_student_7_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_7_gender)),
                sAppContext.getString(R.string.default_student_7_email_address),
                sAppContext.getString(R.string.default_student_7_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_8_last_name),
                sAppContext.getString(R.string.default_student_8_first_name),
                sAppContext.getString(R.string.default_student_8_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_8_gender)),
                sAppContext.getString(R.string.default_student_8_email_address),
                sAppContext.getString(R.string.default_student_8_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_9_last_name),
                sAppContext.getString(R.string.default_student_9_first_name),
                sAppContext.getString(R.string.default_student_9_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_9_gender)),
                sAppContext.getString(R.string.default_student_9_email_address),
                sAppContext.getString(R.string.default_student_9_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_10_last_name),
                sAppContext.getString(R.string.default_student_10_first_name),
                sAppContext.getString(R.string.default_student_10_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_10_gender)),
                sAppContext.getString(R.string.default_student_10_email_address),
                sAppContext.getString(R.string.default_student_10_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_11_last_name),
                sAppContext.getString(R.string.default_student_11_first_name),
                sAppContext.getString(R.string.default_student_11_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_11_gender)),
                sAppContext.getString(R.string.default_student_11_email_address),
                sAppContext.getString(R.string.default_student_11_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_12_last_name),
                sAppContext.getString(R.string.default_student_12_first_name),
                sAppContext.getString(R.string.default_student_12_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_12_gender)),
                sAppContext.getString(R.string.default_student_12_email_address),
                sAppContext.getString(R.string.default_student_12_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_13_last_name),
                sAppContext.getString(R.string.default_student_13_first_name),
                sAppContext.getString(R.string.default_student_13_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_13_gender)),
                sAppContext.getString(R.string.default_student_13_email_address),
                sAppContext.getString(R.string.default_student_13_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_14_last_name),
                sAppContext.getString(R.string.default_student_14_first_name),
                sAppContext.getString(R.string.default_student_14_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_14_gender)),
                sAppContext.getString(R.string.default_student_14_email_address),
                sAppContext.getString(R.string.default_student_14_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_15_last_name),
                sAppContext.getString(R.string.default_student_15_first_name),
                sAppContext.getString(R.string.default_student_15_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_15_gender)),
                sAppContext.getString(R.string.default_student_15_email_address),
                sAppContext.getString(R.string.default_student_15_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_16_last_name),
                sAppContext.getString(R.string.default_student_16_first_name),
                sAppContext.getString(R.string.default_student_16_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_16_gender)),
                sAppContext.getString(R.string.default_student_16_email_address),
                sAppContext.getString(R.string.default_student_16_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_17_last_name),
                sAppContext.getString(R.string.default_student_17_first_name),
                sAppContext.getString(R.string.default_student_17_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_17_gender)),
                sAppContext.getString(R.string.default_student_17_email_address),
                sAppContext.getString(R.string.default_student_17_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_18_last_name),
                sAppContext.getString(R.string.default_student_18_first_name),
                sAppContext.getString(R.string.default_student_18_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_18_gender)),
                sAppContext.getString(R.string.default_student_18_email_address),
                sAppContext.getString(R.string.default_student_18_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_19_last_name),
                sAppContext.getString(R.string.default_student_19_first_name),
                sAppContext.getString(R.string.default_student_19_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_19_gender)),
                sAppContext.getString(R.string.default_student_19_email_address),
                sAppContext.getString(R.string.default_student_19_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_20_last_name),
                sAppContext.getString(R.string.default_student_20_first_name),
                sAppContext.getString(R.string.default_student_20_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_20_gender)),
                sAppContext.getString(R.string.default_student_20_email_address),
                sAppContext.getString(R.string.default_student_20_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_21_last_name),
                sAppContext.getString(R.string.default_student_21_first_name),
                sAppContext.getString(R.string.default_student_21_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_21_gender)),
                sAppContext.getString(R.string.default_student_21_email_address),
                sAppContext.getString(R.string.default_student_21_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_22_last_name),
                sAppContext.getString(R.string.default_student_22_first_name),
                sAppContext.getString(R.string.default_student_22_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_22_gender)),
                sAppContext.getString(R.string.default_student_22_email_address),
                sAppContext.getString(R.string.default_student_22_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_23_last_name),
                sAppContext.getString(R.string.default_student_23_first_name),
                sAppContext.getString(R.string.default_student_23_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_23_gender)),
                sAppContext.getString(R.string.default_student_23_email_address),
                sAppContext.getString(R.string.default_student_23_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_24_last_name),
                sAppContext.getString(R.string.default_student_24_first_name),
                sAppContext.getString(R.string.default_student_24_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_24_gender)),
                sAppContext.getString(R.string.default_student_24_email_address),
                sAppContext.getString(R.string.default_student_24_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_25_last_name),
                sAppContext.getString(R.string.default_student_25_first_name),
                sAppContext.getString(R.string.default_student_25_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_25_gender)),
                sAppContext.getString(R.string.default_student_25_email_address),
                sAppContext.getString(R.string.default_student_25_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_26_last_name),
                sAppContext.getString(R.string.default_student_26_first_name),
                sAppContext.getString(R.string.default_student_26_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_26_gender)),
                sAppContext.getString(R.string.default_student_26_email_address),
                sAppContext.getString(R.string.default_student_26_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_27_last_name),
                sAppContext.getString(R.string.default_student_27_first_name),
                sAppContext.getString(R.string.default_student_27_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_27_gender)),
                sAppContext.getString(R.string.default_student_27_email_address),
                sAppContext.getString(R.string.default_student_27_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_28_last_name),
                sAppContext.getString(R.string.default_student_28_first_name),
                sAppContext.getString(R.string.default_student_28_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_28_gender)),
                sAppContext.getString(R.string.default_student_28_email_address),
                sAppContext.getString(R.string.default_student_28_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_29_last_name),
                sAppContext.getString(R.string.default_student_29_first_name),
                sAppContext.getString(R.string.default_student_29_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_29_gender)),
                sAppContext.getString(R.string.default_student_29_email_address),
                sAppContext.getString(R.string.default_student_29_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_30_last_name),
                sAppContext.getString(R.string.default_student_30_first_name),
                sAppContext.getString(R.string.default_student_30_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_30_gender)),
                sAppContext.getString(R.string.default_student_30_email_address),
                sAppContext.getString(R.string.default_student_30_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_31_last_name),
                sAppContext.getString(R.string.default_student_31_first_name),
                sAppContext.getString(R.string.default_student_31_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_31_gender)),
                sAppContext.getString(R.string.default_student_31_email_address),
                sAppContext.getString(R.string.default_student_31_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_32_last_name),
                sAppContext.getString(R.string.default_student_32_first_name),
                sAppContext.getString(R.string.default_student_32_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_32_gender)),
                sAppContext.getString(R.string.default_student_32_email_address),
                sAppContext.getString(R.string.default_student_32_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_33_last_name),
                sAppContext.getString(R.string.default_student_33_first_name),
                sAppContext.getString(R.string.default_student_33_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_33_gender)),
                sAppContext.getString(R.string.default_student_33_email_address),
                sAppContext.getString(R.string.default_student_33_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_34_last_name),
                sAppContext.getString(R.string.default_student_34_first_name),
                sAppContext.getString(R.string.default_student_34_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_34_gender)),
                sAppContext.getString(R.string.default_student_34_email_address),
                sAppContext.getString(R.string.default_student_34_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_35_last_name),
                sAppContext.getString(R.string.default_student_35_first_name),
                sAppContext.getString(R.string.default_student_35_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_35_gender)),
                sAppContext.getString(R.string.default_student_35_email_address),
                sAppContext.getString(R.string.default_student_35_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_36_last_name),
                sAppContext.getString(R.string.default_student_36_first_name),
                sAppContext.getString(R.string.default_student_36_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_36_gender)),
                sAppContext.getString(R.string.default_student_36_email_address),
                sAppContext.getString(R.string.default_student_36_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_37_last_name),
                sAppContext.getString(R.string.default_student_37_first_name),
                sAppContext.getString(R.string.default_student_37_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_37_gender)),
                sAppContext.getString(R.string.default_student_37_email_address),
                sAppContext.getString(R.string.default_student_37_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_38_last_name),
                sAppContext.getString(R.string.default_student_38_first_name),
                sAppContext.getString(R.string.default_student_38_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_38_gender)),
                sAppContext.getString(R.string.default_student_38_email_address),
                sAppContext.getString(R.string.default_student_38_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_39_last_name),
                sAppContext.getString(R.string.default_student_39_first_name),
                sAppContext.getString(R.string.default_student_39_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_39_gender)),
                sAppContext.getString(R.string.default_student_39_email_address),
                sAppContext.getString(R.string.default_student_39_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_40_last_name),
                sAppContext.getString(R.string.default_student_40_first_name),
                sAppContext.getString(R.string.default_student_40_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_40_gender)),
                sAppContext.getString(R.string.default_student_40_email_address),
                sAppContext.getString(R.string.default_student_40_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_41_last_name),
                sAppContext.getString(R.string.default_student_41_first_name),
                sAppContext.getString(R.string.default_student_41_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_41_gender)),
                sAppContext.getString(R.string.default_student_41_email_address),
                sAppContext.getString(R.string.default_student_41_contact_number));

        StudentContract._insert(db, sAppContext.getString(R.string.default_student_42_last_name),
                sAppContext.getString(R.string.default_student_42_first_name),
                sAppContext.getString(R.string.default_student_42_middle_name),
                GenderEnum.fromInt(sAppContext.getResources().getInteger(R.integer.default_student_42_gender)),
                sAppContext.getString(R.string.default_student_42_email_address),
                sAppContext.getString(R.string.default_student_42_contact_number));
    }

}
