package net.polybugger.apollot.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

            long class0Id = _insertDummyClass0(db);

            long class1Id = _insertDummyClass1(db);
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
        return ClassContract._insert(db, sAppContext.getString(R.string.default_class_0_code), sAppContext.getString(R.string.default_class_0_description), academicTerm, (long) sAppContext.getResources().getInteger(R.integer.default_class_0_year), PastCurrentEnum.CURRENT, new Date());
    }

    private static long _insertDummyClass1(SQLiteDatabase db) {
        AcademicTermContract.AcademicTermEntry academicTerm = AcademicTermContract._getEntry(db, sAppContext.getString(R.string.default_class_1_academic_term));
        return ClassContract._insert(db, sAppContext.getString(R.string.default_class_1_code), sAppContext.getString(R.string.default_class_1_description), academicTerm, (long) sAppContext.getResources().getInteger(R.integer.default_class_1_year), PastCurrentEnum.PAST, new Date());
    }

}
