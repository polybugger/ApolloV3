package net.polybugger.apollot.db;

import android.provider.BaseColumns;

public class ClassScheduleContract {

    public static final String TABLE_NAME = "ClassSchedules";
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            ClassScheduleEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassScheduleEntry.CLASS_ID + " INTEGER NOT NULL REFERENCES " +
                ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + "), " +
            ClassScheduleEntry.TIME_START + " TEXT NOT NULL, " +
            ClassScheduleEntry.TIME_END + " TEXT NULL, " +
            ClassScheduleEntry.DAYS + " INTEGER NOT NULL DEFAULT 0, " +
            ClassScheduleEntry.ROOM + " TEXT NULL, " +
            ClassScheduleEntry.BUILDING + " TEXT NULL, " +
            ClassScheduleEntry.CAMPUS + " TEXT NULL)";

    private ClassScheduleContract() { }

    public static final class ClassScheduleEntry implements BaseColumns {

        public static final String CLASS_ID = "ClassId";
        public static final String TIME_START = "TimeStart";
        public static final String TIME_END = "TimeEnd";
        public static final String DAYS = "Days";
        public static final String ROOM = "Room";
        public static final String BUILDING = "Building";
        public static final String CAMPUS = "Campus";

    }

}
