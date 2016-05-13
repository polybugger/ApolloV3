package net.polybugger.apollot.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.MediumTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.polybugger.apollot.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class ClassScheduleContractTest {

    private SQLiteDatabase mDb;
    private Context mContext;
    private ClassContract.ClassEntry mClass0;
    private long mClassSchedule0Id;
    private Date mTimeStart0;
    private Date mTimeEnd0;
    private int mDays0;
    private String mRoom0;
    private String mBuilding0;
    private String mCampus0;
    private ClassContract.ClassEntry mClass1;
    private long mClassSchedule1Id;
    private Date mTimeStart1;
    private Date mTimeEnd1;
    private int mDays1;
    private String mRoom1;
    private String mBuilding1;
    private String mCampus1;

    @Before
    public void setUp() throws Exception {
        mContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_");
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.TIME_DISPLAY_TEMPLATE, mContext.getResources().getConfiguration().locale);
        ApolloDbAdapter.setAppContext(mContext);
        mDb = ApolloDbAdapter.open();
        mDb.setForeignKeyConstraintsEnabled(false);
        mDb.execSQL(ClassScheduleContract.DELETE_ALL_SQL);
        try {
            mTimeStart0 = sdf.parse(mContext.getString(R.string.default_class_0_class_schedule_0_time_start));
        }
        catch(Exception e) {
            mTimeStart0 = null;
        }
        try {
            mTimeEnd0 = sdf.parse(mContext.getString(R.string.default_class_0_class_schedule_0_time_end));
        }
        catch(Exception e) {
            mTimeEnd0 = null;
        }
        //mDays0 = mContext.getResources().getInteger(R.integer.default_class_0_class_schedule_0_days);
        mRoom0 = mContext.getString(R.string.default_class_0_class_schedule_0_room);
        mBuilding0 = mContext.getString(R.string.default_class_0_class_schedule_0_building);
        mCampus0 = mContext.getString(R.string.default_class_0_class_schedule_0_campus);
        mClass0 = ClassContract._getEntryByCode(mDb, mContext.getString(R.string.default_class_0_code));
        //ApolloDbAdapter._insertDummyClass0Schedules(mDb, mClass0.getId());
        mClassSchedule0Id = ClassScheduleContract._getEntriesByClassId(mDb, mClass0.getId()).get(0).getId();
        try {
            mTimeStart1 = sdf.parse(mContext.getString(R.string.default_class_1_class_schedule_0_time_start));
        }
        catch(Exception e) {
            mTimeStart1 = null;
        }
        try {
            mTimeEnd1 = sdf.parse(mContext.getString(R.string.default_class_1_class_schedule_0_time_end));
        }
        catch(Exception e) {
            mTimeEnd1 = null;
        }
        //mDays1 = mContext.getResources().getInteger(R.integer.default_class_1_class_schedule_0_days);
        mRoom1 = mContext.getString(R.string.default_class_1_class_schedule_0_room);
        mBuilding1 = mContext.getString(R.string.default_class_1_class_schedule_0_building);
        mCampus1 = mContext.getString(R.string.default_class_1_class_schedule_0_campus);
        mClass1 = ClassContract._getEntryByCode(mDb, mContext.getString(R.string.default_class_1_code));
        //ApolloDbAdapter._insertDummyClass0Schedules(mDb, mClass1.getId());
        mClassSchedule1Id = ClassScheduleContract._getEntriesByClassId(mDb, mClass1.getId()).get(0).getId();
    }

    @After
    public void tearDown() throws Exception {
        mDb.execSQL(ClassScheduleContract.DELETE_ALL_SQL);
        //ApolloDbAdapter._insertDummyClass0Schedules(mDb, mClass0.getId());
        //ApolloDbAdapter._insertDummyClass0Schedules(mDb, mClass1.getId());
        ApolloDbAdapter.close();
    }

    @Test
    public void test_methods() throws Exception {
        ClassScheduleContract.ClassScheduleEntry entry = ClassScheduleContract._getEntry(mDb, mClassSchedule0Id);
        assertEquals(mClassSchedule0Id, entry.getId());
        assertEquals(mClass0.getId(), entry.getClassId());
        assertEquals(mTimeStart0, entry.getTimeStart());
        assertEquals(mTimeEnd0, entry.getTimeEnd());
        assertEquals(mDays0, entry.getDays());
        assertEquals(mRoom0, entry.getRoom());
        assertEquals(mBuilding0, entry.getBuilding());
        assertEquals(mCampus0, entry.getCampus());

        assertNotEquals(mClassSchedule0Id, mClassSchedule1Id);
        assertNotEquals(mClass0.getId(), mClass1.getId());
        assertNotEquals(mTimeStart0, mTimeStart1);
        assertNotEquals(mTimeEnd0, mTimeEnd1);
        assertNotEquals(mDays0, mDays1);
        assertNotEquals(mRoom0, mRoom1);
        assertNotEquals(mBuilding0, mBuilding1);
        assertNotEquals(mCampus0, mCampus1);

        entry.setId(mClassSchedule1Id);
        entry.setClassId(mClass1.getId());
        entry.setTimeStart(mTimeStart1);
        entry.setTimeEnd(mTimeEnd1);
        entry.setDays(mDays1);
        entry.setRoom(mRoom1);
        entry.setBuilding(mBuilding1);
        entry.setCampus(mCampus1);
        assertEquals(mClassSchedule1Id, entry.getId());
        assertEquals(mClass1.getId(), entry.getClassId());
        assertEquals(mTimeStart1, entry.getTimeStart());
        assertEquals(mTimeEnd1, entry.getTimeEnd());
        assertEquals(mDays1, entry.getDays());
        assertEquals(mRoom1, entry.getRoom());
        assertEquals(mBuilding1, entry.getBuilding());
        assertEquals(mCampus1, entry.getCampus());
    }

    @Test
    public void test_getEntry() throws Exception {
        ClassScheduleContract.ClassScheduleEntry entry = ClassScheduleContract._getEntry(mDb, mClassSchedule0Id);
        assertNotNull(entry);
        assertEquals(mClassSchedule0Id, entry.getId());
        assertEquals(mClass0.getId(), entry.getClassId());
        assertEquals(mTimeStart0, entry.getTimeStart());
        assertEquals(mTimeEnd0, entry.getTimeEnd());
        assertEquals(mDays0, entry.getDays());
        assertEquals(mRoom0, entry.getRoom());
        assertEquals(mBuilding0, entry.getBuilding());
        assertEquals(mCampus0, entry.getCampus());

        ClassScheduleContract.ClassScheduleEntry entryByClassId = ClassScheduleContract._getEntriesByClassId(mDb, mClass0.getId()).get(0);
        assertNotNull(entryByClassId);
        assertEquals(mClassSchedule0Id, entryByClassId.getId());
        assertEquals(mClass0.getId(), entryByClassId.getClassId());
        assertEquals(mTimeStart0, entryByClassId.getTimeStart());
        assertEquals(mTimeEnd0, entryByClassId.getTimeEnd());
        assertEquals(mDays0, entryByClassId.getDays());
        assertEquals(mRoom0, entryByClassId.getRoom());
        assertEquals(mBuilding0, entryByClassId.getBuilding());
        assertEquals(mCampus0, entryByClassId.getCampus());
    }

    @Test
    public void test_getEntries() throws Exception {
        ArrayList<ClassScheduleContract.ClassScheduleEntry> entries = ClassScheduleContract._getEntries(mDb);
        ClassScheduleContract.ClassScheduleEntry entry0 = ClassScheduleContract._getEntry(mDb, mClassSchedule0Id);
        ClassScheduleContract.ClassScheduleEntry entry1 = ClassScheduleContract._getEntry(mDb, mClassSchedule1Id);

        assertNotNull(entries);
        assertNotNull(entry0);
        assertTrue(entries.contains(entry0));
        assertNotNull(entry1);
        assertTrue(entries.contains(entry1));
    }

    @Test
    public void test_delete() throws Exception {
        int rowsDeleted = ClassScheduleContract._delete(mDb, mClassSchedule0Id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = ClassScheduleContract._delete(mDb, mClassSchedule0Id);
        assertEquals(0, rowsDeleted);
        ClassScheduleContract.ClassScheduleEntry entry = ClassScheduleContract._getEntry(mDb, mClassSchedule0Id);
        assertNull(entry);
    }

    @Test
    public void test_update() throws Exception {
        int rowsUpdated = ClassScheduleContract._update(mDb, mClassSchedule0Id, mClass1.getId(), mTimeStart1, mTimeEnd1, mDays1, mRoom1, mBuilding1, mCampus1);
        assertEquals(1, rowsUpdated);

        ClassScheduleContract.ClassScheduleEntry entry = ClassScheduleContract._getEntry(mDb, mClassSchedule0Id);
        assertNotNull(entry);
        assertEquals(mClassSchedule0Id, entry.getId());
        assertEquals(mClass1.getId(), entry.getClassId());
        assertEquals(mTimeStart1, entry.getTimeStart());
        assertEquals(mTimeEnd1, entry.getTimeEnd());
        assertEquals(mDays1, entry.getDays());
        assertEquals(mRoom1, entry.getRoom());
        assertEquals(mBuilding1, entry.getBuilding());
        assertEquals(mCampus1, entry.getCampus());
    }
}
