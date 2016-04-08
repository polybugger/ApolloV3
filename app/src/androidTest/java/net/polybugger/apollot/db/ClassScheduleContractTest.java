package net.polybugger.apollot.db;

import static org.junit.Assert.assertArrayEquals;
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
    private ClassContract.ClassEntry mClass1;
    private long mClassSchedule0Id;
    private Date mTimeStart0;
    private Date mTimeEnd0;
    private int mDays0;
    private String mRoom0;
    private String mBuilding0;
    private String mCampus0;
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
        ApolloDbAdapter.setAppContext(mContext);
        mDb = ApolloDbAdapter.open();
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.TIME_DISPLAY_TEMPLATE, mContext.getResources().getConfiguration().locale);
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
        mDays0 = mContext.getResources().getInteger(R.integer.default_class_0_class_schedule_0_days);
        mRoom0 = mContext.getString(R.string.default_class_0_class_schedule_0_room);
        mBuilding0 = mContext.getString(R.string.default_class_0_class_schedule_0_building);
        mCampus0 = mContext.getString(R.string.default_class_0_class_schedule_0_campus);
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
        mDays1 = mContext.getResources().getInteger(R.integer.default_class_1_class_schedule_0_days);
        mRoom1 = mContext.getString(R.string.default_class_1_class_schedule_0_room);
        mBuilding1 = mContext.getString(R.string.default_class_1_class_schedule_0_building);
        mCampus1 = mContext.getString(R.string.default_class_1_class_schedule_0_campus);

        mDb.setForeignKeyConstraintsEnabled(false);
        mDb.execSQL(ClassScheduleContract.DELETE_ALL_SQL);
        _insertDummyClassSchedules();
    }

    @After
    public void tearDown() throws Exception {
        mDb.execSQL(ClassScheduleContract.DELETE_ALL_SQL);
        _insertDummyClassSchedules();
        ApolloDbAdapter.close();
    }

    @Test
    public void testEntryConstructorAndMethods() throws Exception {
        ClassScheduleContract.ClassScheduleEntry entry = new ClassScheduleContract.ClassScheduleEntry(mClassSchedule0Id, mClass0.getId(), mTimeStart0, mTimeEnd0, mDays0, mRoom0, mBuilding0, mCampus0);
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
        assertEquals(mClassSchedule1Id, entry.getId());
        entry.setClassId(mClass1.getId());
        assertEquals(mClass1.getId(), entry.getClassId());
        entry.setTimeStart(mTimeStart1);
        assertEquals(mTimeStart1, entry.getTimeStart());
        entry.setTimeEnd(mTimeEnd1);
        assertEquals(mTimeEnd1, entry.getTimeEnd());
        entry.setDays(mDays1);
        assertEquals(mDays1, entry.getDays());
        entry.setRoom(mRoom1);
        assertEquals(mRoom1, entry.getRoom());
        entry.setBuilding(mBuilding1);
        assertEquals(mBuilding1, entry.getBuilding());
        entry.setCampus(mCampus1);
        assertEquals(mCampus1, entry.getCampus());
    }

    @Test
    public void test_getEntryId() throws Exception {
        ClassScheduleContract.ClassScheduleEntry _entry = ClassScheduleContract._getEntry(mDb, mClassSchedule0Id);
        assertNotNull(_entry);
        assertEquals(mClassSchedule0Id, _entry.getId());
        assertEquals(mClass0.getId(), _entry.getClassId());
        assertEquals(mTimeStart0, _entry.getTimeStart());
        assertEquals(mTimeEnd0, _entry.getTimeEnd());
        assertEquals(mDays0, _entry.getDays());
        assertEquals(mRoom0, _entry.getRoom());
        assertEquals(mBuilding0, _entry.getBuilding());
        assertEquals(mCampus0, _entry.getCampus());

        ClassScheduleContract.ClassScheduleEntry entry = ClassScheduleContract.getEntry(mClassSchedule0Id);
        assertNotNull(entry);
        assertEquals(mClassSchedule0Id, entry.getId());
        assertEquals(mClass0.getId(), entry.getClassId());
        assertEquals(mTimeStart0, entry.getTimeStart());
        assertEquals(mTimeEnd0, entry.getTimeEnd());
        assertEquals(mDays0, entry.getDays());
        assertEquals(mRoom0, entry.getRoom());
        assertEquals(mBuilding0, entry.getBuilding());
        assertEquals(mCampus0, entry.getCampus());

        assertTrue(_entry.equals(entry));
    }

    @Test
    public void test_getEntries() throws Exception {
        ArrayList<ClassScheduleContract.ClassScheduleEntry> _entries = ClassScheduleContract._getEntries(mDb);
        ClassScheduleContract.ClassScheduleEntry _entry0 = ClassScheduleContract._getEntry(mDb, mClassSchedule0Id);
        ClassScheduleContract.ClassScheduleEntry _entry1 = ClassScheduleContract._getEntry(mDb, mClassSchedule1Id);

        assertNotNull(_entries);
        assertNotNull(_entry0);
        assertTrue(_entries.contains(_entry0));
        assertNotNull(_entry1);
        assertTrue(_entries.contains(_entry1));

        ArrayList<ClassScheduleContract.ClassScheduleEntry> entries = ClassScheduleContract.getEntries();
        assertNotNull(entries);
        assertArrayEquals(_entries.toArray(), entries.toArray());
    }

    @Test
    public void test_delete() throws Exception {
        int rowsDeleted = ClassScheduleContract._delete(mDb, mClassSchedule0Id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = ClassScheduleContract._delete(mDb, mClassSchedule0Id);
        assertEquals(0, rowsDeleted);
        ClassScheduleContract.ClassScheduleEntry _entry = ClassScheduleContract._getEntry(mDb, mClassSchedule0Id);
        assertNull(_entry);

        rowsDeleted = ClassScheduleContract.delete(mClassSchedule1Id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = ClassScheduleContract.delete(mClassSchedule1Id);
        assertEquals(0, rowsDeleted);
        ClassScheduleContract.ClassScheduleEntry entry = ClassScheduleContract._getEntry(mDb, mClassSchedule1Id);
        assertNull(entry);
    }

    @Test
    public void test_update() throws Exception {
        ClassScheduleContract.ClassScheduleEntry _entry0 = ClassScheduleContract._getEntry(mDb, mClassSchedule0Id);
        assertNotNull(_entry0);
        assertNotEquals(mClass1.getId(), _entry0.getClassId());
        assertNotEquals(mTimeStart1, _entry0.getTimeStart());
        assertNotEquals(mTimeEnd1, _entry0.getTimeEnd());
        assertNotEquals(mDays1, _entry0.getDays());
        assertNotEquals(mRoom1, _entry0.getRoom());
        assertNotEquals(mBuilding1, _entry0.getBuilding());
        assertNotEquals(mCampus1, _entry0.getCampus());

        int rowsUpdated = ClassScheduleContract._update(mDb, mClassSchedule0Id, mClass1.getId(), mTimeStart1, mTimeEnd1, mDays1, mRoom1, mBuilding1, mCampus1);
        assertEquals(1, rowsUpdated);
        ClassScheduleContract.ClassScheduleEntry _entry0Updated = ClassScheduleContract._getEntry(mDb, mClassSchedule0Id);
        assertNotNull(_entry0Updated);
        assertEquals(mClass1.getId(), _entry0Updated.getClassId());
        assertEquals(mTimeStart1, _entry0Updated.getTimeStart());
        assertEquals(mTimeEnd1, _entry0Updated.getTimeEnd());
        assertEquals(mDays1, _entry0Updated.getDays());
        assertEquals(mRoom1, _entry0Updated.getRoom());
        assertEquals(mBuilding1, _entry0Updated.getBuilding());
        assertEquals(mCampus1, _entry0Updated.getCampus());

        assertNotEquals(mClass0.getId(), _entry0Updated.getClassId());
        assertNotEquals(mTimeStart0, _entry0Updated.getTimeStart());
        assertNotEquals(mTimeEnd0, _entry0Updated.getTimeEnd());
        assertNotEquals(mDays0, _entry0Updated.getDays());
        assertNotEquals(mRoom0, _entry0Updated.getRoom());
        assertNotEquals(mBuilding0, _entry0Updated.getBuilding());
        assertNotEquals(mCampus0, _entry0Updated.getCampus());

        rowsUpdated = ClassScheduleContract.update(mClassSchedule0Id, mClass0.getId(), mTimeStart0, mTimeEnd0, mDays0, mRoom0, mBuilding0, mCampus0);
        assertEquals(1, rowsUpdated);
        _entry0Updated = ClassScheduleContract.getEntry(mClassSchedule0Id);
        assertNotNull(_entry0Updated);
        assertEquals(mClass0.getId(), _entry0Updated.getClassId());
        assertEquals(mTimeStart0, _entry0Updated.getTimeStart());
        assertEquals(mTimeEnd0, _entry0Updated.getTimeEnd());
        assertEquals(mDays0, _entry0Updated.getDays());
        assertEquals(mRoom0, _entry0Updated.getRoom());
        assertEquals(mBuilding0, _entry0Updated.getBuilding());
        assertEquals(mCampus0, _entry0Updated.getCampus());
    }

    @Test
    public void test_insert() throws Exception {
        ClassScheduleContract.ClassScheduleEntry _entry0 = ClassScheduleContract._getEntry(mDb, mClassSchedule0Id);
        assertNotNull(_entry0);
        assertEquals(mClassSchedule0Id, _entry0.getId());

        int rowsDeleted = ClassScheduleContract._delete(mDb, mClassSchedule0Id);
        assertEquals(1, rowsDeleted);
        _entry0 = ClassScheduleContract._getEntry(mDb, mClassSchedule0Id);
        assertNull(_entry0);

        long classSchedule2Id = ClassScheduleContract._insert(mDb, mClass0.getId(), mTimeStart0, mTimeEnd0, mDays0, mRoom0, mBuilding0, mCampus0);
        ClassScheduleContract.ClassScheduleEntry _entry2 = ClassScheduleContract._getEntry(mDb, classSchedule2Id);
        assertNotNull(_entry2);
        assertEquals(classSchedule2Id, _entry2.getId());
        assertEquals(mClass0.getId(), _entry2.getClassId());
        assertEquals(mTimeStart0, _entry2.getTimeStart());
        assertEquals(mTimeEnd0, _entry2.getTimeEnd());
        assertEquals(mDays0, _entry2.getDays());
        assertEquals(mRoom0, _entry2.getRoom());
        assertEquals(mBuilding0, _entry2.getBuilding());
        assertEquals(mCampus0, _entry2.getCampus());
    }

    private void _insertDummyClassSchedules() {
        mClass0 = ClassContract._getEntry(mDb, mContext.getString(R.string.default_class_0_code));
        mClass1 = ClassContract._getEntry(mDb, mContext.getString(R.string.default_class_1_code));
        mClassSchedule0Id = ClassScheduleContract._insert(mDb, mClass0.getId(), mTimeStart0, mTimeEnd0, mDays0, mRoom0, mBuilding0, mCampus0);
        mClassSchedule1Id = ClassScheduleContract._insert(mDb, mClass1.getId(), mTimeStart1, mTimeEnd1, mDays1, mRoom1, mBuilding1, mCampus1);
    }
}
