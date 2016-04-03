package net.polybugger.apollot.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ApolloDbAdapter {

    public static final String DATABASE_NAME = "ApolloDb.sqlite3";
    public static final int DATABASE_VERSION = 3;

    private static Context sAppContext;
    private static ApolloDbHelper sDbHelper;
    private static int sOpenCounter;
    private static SQLiteDatabase sDb;

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
        sOpenCounter++;
        if(sOpenCounter == 1) {
            sDb = sDbHelper.getWritableDatabase();
        }
        return sDb;
    }

    public static synchronized void close() {
        sOpenCounter--;
        if(sOpenCounter == 0) {
            sDb.close();
        }
    }

    private static class ApolloDbHelper extends SQLiteOpenHelper {

        public ApolloDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            final String CREATE_TABLE_ACADEMIC_TERMS =
                    "CREATE TABLE " + AcademicTermContract.TABLE_NAME + " (" +
                            AcademicTermContract.AcademicTermEntry._ID + " INTEGER PRIMARY KEY, " +
                            AcademicTermContract.AcademicTermEntry.DESCRIPTION + " TEXT NOT NULL, " +
                            AcademicTermContract.AcademicTermEntry.COLOR + " TEXT NULL)";
            db.execSQL(CREATE_TABLE_ACADEMIC_TERMS);

            final String CREATE_TABLE_CLASSES =
                    "CREATE TABLE " + ClassContract.TABLE_NAME + " (" +
                            ClassContract.ClassEntry._ID + " INTEGER PRIMARY KEY, " +
                            ClassContract.ClassEntry.CODE + " TEXT NOT NULL, " +
                            ClassContract.ClassEntry.DESCRIPTION + " TEXT NULL, " +
                            ClassContract.ClassEntry.ACADEMIC_TERM_ID + " INTEGER NULL REFERENCES " + AcademicTermContract.TABLE_NAME + " (" + AcademicTermContract.AcademicTermEntry._ID + "), " +
                            ClassContract.ClassEntry.YEAR + " INTEGER NULL, " +
                            ClassContract.ClassEntry.CURRENT + " INTEGER NOT NULL DEFAULT 1, " +
                            ClassContract.ClassEntry.DATE_CREATED + " TEXT NULL)";
            db.execSQL(CREATE_TABLE_CLASSES);

            final String CREATE_TABLE_CLASS_SCHEDULES =
                    "CREATE TABLE " + ClassScheduleContract.TABLE_NAME + " (" +
                            ClassScheduleContract.ClassScheduleEntry._ID + " INTEGER PRIMARY KEY, " +
                            ClassScheduleContract.ClassScheduleEntry.CLASS_ID + " INTEGER NOT NULL REFERENCES " + ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + "), " +
                            ClassScheduleContract.ClassScheduleEntry.TIME_START + " TEXT NOT NULL, " +
                            ClassScheduleContract.ClassScheduleEntry.TIME_END + " TEXT NULL, " +
                            ClassScheduleContract.ClassScheduleEntry.DAYS + " INTEGER NOT NULL DEFAULT 0, " +
                            ClassScheduleContract.ClassScheduleEntry.ROOM + " TEXT NULL, " +
                            ClassScheduleContract.ClassScheduleEntry.BUILDING + " TEXT NULL, " +
                            ClassScheduleContract.ClassScheduleEntry.CAMPUS + " TEXT NULL)";
            db.execSQL(CREATE_TABLE_CLASS_SCHEDULES);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
