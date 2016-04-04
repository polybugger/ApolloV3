package net.polybugger.apollot.db;

import android.provider.BaseColumns;

public class ClassGradeBreakdownContract {

    public static final String TABLE_NAME = "ClassGradeBreakdown_";
    public static final String CREATE_TABLE_SQL1 = "CREATE TABLE IF NOT EXISTS ";
    public static final String CREATE_TABLE_SQL2 = " (" +
            ClassGradeBreakdownEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassGradeBreakdownEntry.CLASS_ID + " INTEGER NOT NULL REFERENCES " +
                ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + "), " +
            ClassGradeBreakdownEntry.ITEM_TYPE_ID + " INTEGER NOT NULL UNIQUE REFERENCES " +
                ClassItemTypeContract.TABLE_NAME + " (" + ClassItemTypeContract.ClassItemTypeEntry._ID + "), " +
            ClassGradeBreakdownEntry.PERCENTAGE + " REAL NULL)";

    private ClassGradeBreakdownContract() { }

    public static class ClassGradeBreakdownEntry implements BaseColumns {

        public static final String CLASS_ID = "ClassId";
        public static final String ITEM_TYPE_ID = "ItemTypeId";
        public static final String PERCENTAGE = "Percentage";

    }
}
