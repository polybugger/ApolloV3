package net.polybugger.apollot.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ApolloDbAdapter {

    public static final String DATABASE_NAME = "ApolloDb.sqlite3";
    public static final int DATABASE_VERSION = 3;

    private static Context sAppContext = null;
    private static ApolloDbHelper sDbHelper = null;
    private static int sOpenCounter = 0;
    private static SQLiteDatabase sDb = null;

    private ApolloDbAdapter() { }

    // http://www.dmytrodanylyk.com/concurrent-database-access/
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

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
