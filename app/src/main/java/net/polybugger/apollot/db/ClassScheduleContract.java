package net.polybugger.apollot.db;

import android.provider.BaseColumns;

public class ClassScheduleContract {

    public static final String TABLE_NAME = "ClassSchedules";

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
