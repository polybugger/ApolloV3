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

            /*
            db.execSQL(StudentContract.CREATE_TABLE_SQL);
            _insertDummyStudents(db);

            db.execSQL(ClassScheduleContract.CREATE_TABLE_SQL);
            _insertDummyClass0Schedules(db, class0Id);
            _insertDummyClass1Schedules(db, class1Id);

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

    public static void _insertDummyStudents(SQLiteDatabase db) {
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

    public static void _insertDummyClass0Schedules(SQLiteDatabase db, long classId) {
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.TIME_DB_TEMPLATE, sAppContext.getResources().getConfiguration().locale);
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

    public static void _insertDummyClass1Schedules(SQLiteDatabase db, long classId) {
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.TIME_DB_TEMPLATE, sAppContext.getResources().getConfiguration().locale);
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
