package net.polybugger.apollot.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.StyleableRes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ClassScheduleContract {

    public static final String TABLE_NAME = "ClassSchedules";
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            ClassScheduleEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassScheduleEntry.CLASS_ID + " INTEGER NOT NULL REFERENCES " +
                ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + ") ON DELETE CASCADE, " +
            ClassScheduleEntry.TIME_START + " TEXT NOT NULL, " +
            ClassScheduleEntry.TIME_END + " TEXT NULL, " +
            ClassScheduleEntry.DAYS + " INTEGER NOT NULL DEFAULT 0, " +
            ClassScheduleEntry.ROOM + " TEXT NULL, " +
            ClassScheduleEntry.BUILDING + " TEXT NULL, " +
            ClassScheduleEntry.CAMPUS + " TEXT NULL)";
    public static final String SELECT_TABLE_SQL = "SELECT " +
            ClassScheduleEntry._ID + ", " + // 0
            ClassScheduleEntry.CLASS_ID + ", " + // 1
            ClassScheduleEntry.TIME_START + ", " + // 2
            ClassScheduleEntry.TIME_END + ", " + // 3 nullable
            ClassScheduleEntry.DAYS + ", " + // 4
            ClassScheduleEntry.ROOM + ", " + // 5 nullable
            ClassScheduleEntry.BUILDING + ", " + // 6 nullable
            ClassScheduleEntry.CAMPUS + // 7 nullable
            " FROM " + TABLE_NAME;
    public static final String DELETE_ALL_SQL = "DELETE FROM " + TABLE_NAME;

    private ClassScheduleContract() { }

    public static long _insert(SQLiteDatabase db, long classId, Date timeStart, Date timeEnd, int days, String room, String building, String campus) {
        ContentValues values = new ContentValues();
        values.put(ClassScheduleEntry.CLASS_ID, classId);
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.TIME_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        values.put(ClassScheduleEntry.TIME_START, (timeStart != null) ? sdf.format(timeStart) : null);
        values.put(ClassScheduleEntry.TIME_END, (timeEnd != null) ? sdf.format(timeEnd) : null);
        values.put(ClassScheduleEntry.DAYS, days);
        values.put(ClassScheduleEntry.ROOM, room);
        values.put(ClassScheduleEntry.BUILDING, building);
        values.put(ClassScheduleEntry.CAMPUS, campus);
        return db.insert(TABLE_NAME, null, values);
    }

    public static int _update(SQLiteDatabase db, long id, long classId, Date timeStart, Date timeEnd, int days, String room, String building, String campus) {
        ContentValues values = new ContentValues();
        values.put(ClassScheduleEntry.CLASS_ID, classId);
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.TIME_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        values.put(ClassScheduleEntry.TIME_START, (timeStart != null) ? sdf.format(timeStart) : null);
        values.put(ClassScheduleEntry.TIME_END, (timeEnd != null) ? sdf.format(timeEnd) : null);
        values.put(ClassScheduleEntry.DAYS, days);
        values.put(ClassScheduleEntry.ROOM, room);
        values.put(ClassScheduleEntry.BUILDING, building);
        values.put(ClassScheduleEntry.CAMPUS, campus);
        return db.update(TABLE_NAME, values, ClassScheduleEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static int _delete(SQLiteDatabase db, long id) {
        return db.delete(TABLE_NAME, ClassScheduleEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static ClassScheduleEntry _getEntry(SQLiteDatabase db, long id) {
        ClassScheduleEntry entry = null;
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.TIME_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        Date timeStart, timeEnd;
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{ClassScheduleEntry._ID, // 0
                        ClassScheduleEntry.CLASS_ID, // 1
                        ClassScheduleEntry.TIME_START, // 2
                        ClassScheduleEntry.TIME_END, // 3 nullable
                        ClassScheduleEntry.DAYS, // 4
                        ClassScheduleEntry.ROOM, // 5 nullable
                        ClassScheduleEntry.BUILDING, // 6 nullable
                        ClassScheduleEntry.CAMPUS}, // 7 nullable
                ClassScheduleEntry._ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            try {
                timeStart = sdf.parse(cursor.getString(2));
            }
            catch(Exception e) {
                timeStart = null;
            }
            try {
                timeEnd = sdf.parse(cursor.getString(3));
            }
            catch(Exception e) {
                timeEnd = null;
            }
            entry = new ClassScheduleEntry(cursor.getLong(0),
                    cursor.getLong(1),
                    timeStart, // 2
                    timeEnd, // 3
                    cursor.getInt(4),
                    cursor.isNull(5) ? null : cursor.getString(5),
                    cursor.isNull(6) ? null : cursor.getString(6),
                    cursor.isNull(7) ? null : cursor.getString(7));
        }
        cursor.close();
        return entry;
    }

    public static ArrayList<ClassScheduleEntry> _getEntries(SQLiteDatabase db) {
        ArrayList<ClassScheduleEntry> entries = new ArrayList<>();
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.TIME_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        Date timeStart, timeEnd;
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{ClassScheduleEntry._ID, // 0
                        ClassScheduleEntry.CLASS_ID, // 1
                        ClassScheduleEntry.TIME_START, // 2
                        ClassScheduleEntry.TIME_END, // 3 nullable
                        ClassScheduleEntry.DAYS, // 4
                        ClassScheduleEntry.ROOM, // 5 nullable
                        ClassScheduleEntry.BUILDING, // 6 nullable
                        ClassScheduleEntry.CAMPUS}, // 7 nullable
                null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            try {
                timeStart = sdf.parse(cursor.getString(2));
            }
            catch(Exception e) {
                timeStart = null;
            }
            try {
                timeEnd = sdf.parse(cursor.getString(3));
            }
            catch(Exception e) {
                timeEnd = null;
            }
            entries.add(new ClassScheduleEntry(cursor.getLong(0),
                    cursor.getLong(1),
                    timeStart, // 2
                    timeEnd, // 3
                    cursor.getInt(4),
                    cursor.isNull(5) ? null : cursor.getString(5),
                    cursor.isNull(6) ? null : cursor.getString(6),
                    cursor.isNull(7) ? null : cursor.getString(7)));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public static ArrayList<ClassScheduleEntry> _getEntriesByClassId(SQLiteDatabase db, long classId) {
        ArrayList<ClassScheduleEntry> entries = new ArrayList<>();
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.TIME_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        Date timeStart, timeEnd;
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{ClassScheduleEntry._ID, // 0
                        ClassScheduleEntry.CLASS_ID, // 1
                        ClassScheduleEntry.TIME_START, // 2
                        ClassScheduleEntry.TIME_END, // 3 nullable
                        ClassScheduleEntry.DAYS, // 4
                        ClassScheduleEntry.ROOM, // 5 nullable
                        ClassScheduleEntry.BUILDING, // 6 nullable
                        ClassScheduleEntry.CAMPUS}, // 7 nullable
                ClassScheduleEntry.CLASS_ID + "=?",
                new String[]{String.valueOf(classId)},
                null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            try {
                timeStart = sdf.parse(cursor.getString(2));
            }
            catch(Exception e) {
                timeStart = null;
            }
            try {
                timeEnd = sdf.parse(cursor.getString(3));
            }
            catch(Exception e) {
                timeEnd = null;
            }
            entries.add(new ClassScheduleEntry(cursor.getLong(0),
                    cursor.getLong(1),
                    timeStart, // 2
                    timeEnd, // 3
                    cursor.getInt(4),
                    cursor.isNull(5) ? null : cursor.getString(5),
                    cursor.isNull(6) ? null : cursor.getString(6),
                    cursor.isNull(7) ? null : cursor.getString(7)));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public static void _insertDummyClassSchedule(SQLiteDatabase db, long classId, int classScheduleResourceId, Context context) {
        @StyleableRes final int TIME_START_INDEX = 0;
        @StyleableRes final int TIME_END_INDEX = 1;
        @StyleableRes final int DAYS_INDEX = 2;
        @StyleableRes final int ROOM_INDEX = 3;
        @StyleableRes final int BUILDING_INDEX = 4;
        @StyleableRes final int CAMPUS_INDEX = 5;
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.TIME_DB_TEMPLATE, context.getResources().getConfiguration().locale);
        Resources res = context.getResources();
        TypedArray ta = res.obtainTypedArray(classScheduleResourceId);
        Date timeStart, timeEnd;
        try {
            timeStart = sdf.parse(res.getString(ta.getResourceId(TIME_START_INDEX, 0)));
        }
        catch(Exception e) {
            timeStart = null;
        }
        try {
            timeEnd = sdf.parse(res.getString(ta.getResourceId(TIME_END_INDEX, 0)));
        }
        catch(Exception e) {
            timeEnd = null;
        }
        int days = res.getInteger(ta.getResourceId(DAYS_INDEX, 0));
        String room, building, campus;
        room = res.getString(ta.getResourceId(ROOM_INDEX, 0));
        building = res.getString(ta.getResourceId(BUILDING_INDEX, 0));
        campus = res.getString(ta.getResourceId(CAMPUS_INDEX, 0));
        ta.recycle();
        ClassScheduleContract._insert(db, classId, timeStart, timeEnd,
                days, room, building, campus);
    }

    public static final class ClassScheduleEntry implements BaseColumns, Serializable {

        public static final String CLASS_ID = "ClassId";
        public static final String TIME_START = "TimeStart";
        public static final String TIME_END = "TimeEnd";
        public static final String DAYS = "Days";
        public static final String ROOM = "Room";
        public static final String BUILDING = "Building";
        public static final String CAMPUS = "Campus";

        private long mId; // 0
        private long mClassId; // 1
        private Date mTimeStart; // 2
        private Date mTimeEnd; // 3 nullable
        private int mDays; // 4
        private String mRoom; // 5 nullable
        private String mBuilding; // 6 nullable
        private String mCampus; // 7 nullable

        public ClassScheduleEntry(long id, long classId, Date timeStart, Date timeEnd, int days, String room, String building, String campus) {
            mId = id;
            mClassId = classId;
            mTimeStart = timeStart;
            mTimeEnd = timeEnd;
            mDays = days;
            mRoom = room;
            mBuilding = building;
            mCampus = campus;
        }

        public long getId() {
            return mId;
        }

        public void setId(long id) {
            mId = id;
        }

        public long getClassId() {
            return mClassId;
        }

        public void setClassId(long classId) {
            mClassId = classId;
        }

        public Date getTimeStart() {
            return mTimeStart;
        }

        public void setTimeStart(Date timeStart) {
            mTimeStart = timeStart;
        }

        public Date getTimeEnd() {
            return mTimeEnd;
        }

        public void setTimeEnd(Date timeEnd) {
            mTimeEnd = timeEnd;
        }

        public int getDays() {
            return mDays;
        }

        public void setDays(int days) {
            mDays = days;
        }

        public String getRoom() {
            return mRoom;
        }

        public void setRoom(String room) {
            mRoom = room;
        }

        public String getBuilding() {
            return mBuilding;
        }

        public void setBuilding(String building) {
            mBuilding = building;
        }

        public String getCampus() {
            return mCampus;
        }

        public void setCampus(String campus) {
            mCampus = campus;
        }

        @Override
        public boolean equals(Object object) {
            if(!(object instanceof ClassScheduleEntry))
                return false;
            if(object == this)
                return true;
            ClassScheduleEntry entry = (ClassScheduleEntry) object;
            return new EqualsBuilder()
                    .append(mId, entry.mId).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(23, 29)
                    .append(mId).toHashCode();
        }
    }
}
