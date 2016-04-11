package net.polybugger.apollot.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.MediumTest;

import net.polybugger.apollot.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class ClassItemContractTest {

    private SQLiteDatabase mDb;
    private Context mContext;
    private long mClass0Id;
    private long mClass1Id;
    private long mClassItem0Id;
    private String mClassItem0Description;
    private ClassItemTypeContract.ClassItemTypeEntry mClassItem0ItemType;
    private Date mClassItem0ItemDate;
    private boolean mClassItem0CheckAttendance;
    private boolean mClassItem0RecordScores;
    private Float mClassItem0PerfectScore;
    private boolean mClassItem0RecordSubmissions;
    private Date mClassItem0SubmissionDueDate;
    private long mClassItem1Id;
    private String mClassItem1Description;
    private ClassItemTypeContract.ClassItemTypeEntry mClassItem1ItemType;
    private Date mClassItem1ItemDate;
    private boolean mClassItem1CheckAttendance;
    private boolean mClassItem1RecordScores;
    private Float mClassItem1PerfectScore;
    private boolean mClassItem1RecordSubmissions;
    private Date mClassItem1SubmissionDueDate;

    @Before
    public void setUp() throws Exception {
        mContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_");
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE, mContext.getResources().getConfiguration().locale);
        ApolloDbAdapter.setAppContext(mContext);
        mDb = ApolloDbAdapter.open();
        mDb.setForeignKeyConstraintsEnabled(false);
        mClass0Id = ClassContract._getEntryByCode(mDb, mContext.getString(R.string.default_class_0_code)).getId();
        mClass1Id = 999;
        mDb.execSQL(ClassItemContract.DELETE_ALL_SQL + ClassItemContract.TABLE_NAME + String.valueOf(mClass0Id));
        ApolloDbAdapter._insertDummyClass0Items(mDb, mClass0Id);

        mClassItem0Id = 1;
        mClassItem0Description = mContext.getString(R.string.default_class_0_class_item_0_description);
        mClassItem0ItemType = ClassItemTypeContract._getEntryByDescription(mDb, mContext.getString(R.string.default_class_0_class_item_0_item_type));
        try {
            mClassItem0ItemDate = sdf.parse(mContext.getString(R.string.default_class_0_class_item_0_item_date));
        }
        catch(Exception e) {
            mClassItem0ItemDate = null;
        }
        mClassItem0CheckAttendance = mContext.getResources().getBoolean(R.bool.default_class_0_class_item_0_check_attendance);
        mClassItem0RecordScores = mContext.getResources().getBoolean(R.bool.default_class_0_class_item_0_record_scores);
        try {
            mClassItem0PerfectScore = Float.parseFloat(mContext.getString(R.string.default_class_0_class_item_0_perfect_score));
        }
        catch(Exception e) {
            mClassItem0PerfectScore = null;
        }
        mClassItem0RecordSubmissions = mContext.getResources().getBoolean(R.bool.default_class_0_class_item_0_record_submissions);
        try {
            mClassItem0SubmissionDueDate = sdf.parse(mContext.getString(R.string.default_class_0_class_item_0_submission_due_date));
        }
        catch(Exception e) {
            mClassItem0SubmissionDueDate = null;
        }

        mClassItem1Id = 2;
        mClassItem1Description = mContext.getString(R.string.default_class_0_class_item_1_description);
        mClassItem1ItemType = ClassItemTypeContract._getEntryByDescription(mDb, mContext.getString(R.string.default_class_0_class_item_1_item_type));
        try {
            mClassItem1ItemDate = sdf.parse(mContext.getString(R.string.default_class_0_class_item_1_submission_due_date)); // changing because it's same with class item 0
        }
        catch(Exception e) {
            mClassItem1ItemDate = null;
        }
        mClassItem1CheckAttendance = mContext.getResources().getBoolean(R.bool.default_class_0_class_item_1_check_attendance);
        mClassItem1RecordScores = mContext.getResources().getBoolean(R.bool.default_class_0_class_item_1_record_scores);
        try {
            mClassItem1PerfectScore = Float.parseFloat(mContext.getString(R.string.default_class_0_class_item_1_perfect_score));
        }
        catch(Exception e) {
            mClassItem1PerfectScore = null;
        }
        mClassItem1RecordSubmissions = mContext.getResources().getBoolean(R.bool.default_class_0_class_item_1_record_submissions);
        try {
            mClassItem1SubmissionDueDate = sdf.parse(mContext.getString(R.string.default_class_0_class_item_1_submission_due_date));
        }
        catch(Exception e) {
            mClassItem1SubmissionDueDate = null;
        }
    }

    @After
    public void tearDown() throws Exception {
        mDb.execSQL(ClassItemContract.DELETE_ALL_SQL + ClassItemContract.TABLE_NAME + String.valueOf(mClass0Id));
        ApolloDbAdapter._insertDummyClass0Items(mDb, mClass0Id);
        ApolloDbAdapter.close();
    }

    @Test
    public void test_methods() throws Exception {
        ClassItemContract.ClassItemEntry entry = ClassItemContract._getEntry(mDb, mClassItem0Id, mClass0Id);
        assertEquals(mClassItem0Id, entry.getId());
        assertEquals(mClass0Id, entry.getClassId());
        assertEquals(mClassItem0Description, entry.getDescription());
        assertNotNull(mClassItem0ItemType);
        assertTrue(mClassItem0ItemType.equals(entry.getItemType()));
        assertEquals(mClassItem0ItemDate, entry.getItemDate());
        assertEquals(mClassItem0CheckAttendance, entry.isCheckAttendance());
        assertEquals(mClassItem0RecordScores, entry.isRecordScores());
        assertEquals(mClassItem0PerfectScore, entry.getPerfectScore());
        assertEquals(mClassItem0RecordSubmissions, entry.isRecordSubmissions());
        assertEquals(mClassItem0SubmissionDueDate, entry.getSubmissionDueDate());

        assertNotEquals(mClassItem0Id, mClassItem1Id);
        assertNotEquals(mClass0Id, mClass1Id);
        assertNotEquals(mClassItem0Description, mClassItem1Description);
        assertNotNull(mClassItem1ItemType);
        assertFalse(mClassItem1ItemType.equals(mClassItem0ItemType));
        assertNotEquals(mClassItem0ItemDate, mClassItem1ItemDate);
        assertNotEquals(mClassItem0CheckAttendance, mClassItem1CheckAttendance);
        assertNotEquals(mClassItem0RecordScores, mClassItem1RecordScores);
        assertNotEquals(mClassItem0PerfectScore, mClassItem1PerfectScore);
        assertNotEquals(mClassItem0RecordSubmissions, mClassItem1RecordSubmissions);
        assertNotEquals(mClassItem0SubmissionDueDate, mClassItem1SubmissionDueDate);

        entry.setId(mClassItem1Id);
        entry.setClassId(mClass1Id);
        entry.setDescription(mClassItem1Description);
        entry.setItemType(mClassItem1ItemType);
        entry.setItemDate(mClassItem1ItemDate);
        entry.setCheckAttendance(mClassItem1CheckAttendance);
        entry.setRecordScores(mClassItem1RecordScores);
        entry.setPerfectScore(mClassItem1PerfectScore);
        entry.setRecordSubmissions(mClassItem1RecordSubmissions);
        entry.setSubmissionDueDate(mClassItem1SubmissionDueDate);
        assertEquals(mClassItem1Id, entry.getId());
        assertEquals(mClass1Id, entry.getClassId());
        assertEquals(mClassItem1Description, entry.getDescription());
        assertTrue(mClassItem1ItemType.equals(entry.getItemType()));
        assertEquals(mClassItem1ItemDate, entry.getItemDate());
        assertEquals(mClassItem1CheckAttendance, entry.isCheckAttendance());
        assertEquals(mClassItem1RecordScores, entry.isRecordScores());
        assertEquals(mClassItem1PerfectScore, entry.getPerfectScore());
        assertEquals(mClassItem1RecordSubmissions, entry.isRecordSubmissions());
        assertEquals(mClassItem1SubmissionDueDate, entry.getSubmissionDueDate());
    }

    @Test
    public void test_getEntries() throws Exception {
        ArrayList<ClassItemContract.ClassItemEntry> entries = ClassItemContract._getEntries(mDb, mClass0Id);
        ClassItemContract.ClassItemEntry entry0 = ClassItemContract._getEntry(mDb, mClassItem0Id, mClass0Id);
        mClassItem1Id = 2;
        ClassItemContract.ClassItemEntry entry1 = ClassItemContract._getEntry(mDb, mClassItem1Id, mClass0Id);

        assertNotNull(entries);
        assertNotNull(entry0);
        assertTrue(entries.contains(entry0));
        assertNotNull(entry1);
        assertTrue(entries.contains(entry1));
    }

    @Test
    public void test_delete() throws Exception {
        int rowsDeleted = ClassItemContract._delete(mDb, mClassItem0Id, mClass0Id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = ClassItemContract._delete(mDb, mClassItem0Id, mClass0Id);
        assertEquals(0, rowsDeleted);
        ClassItemContract.ClassItemEntry entry = ClassItemContract._getEntry(mDb, mClassItem0Id, mClass0Id);
        assertNull(entry);
    }

    @Test
    public void test_update() throws Exception {
        int rowsUpdated = ClassItemContract._update(mDb, mClassItem0Id, mClass0Id, mClassItem1Description, mClassItem1ItemType, mClassItem1ItemDate, mClassItem1CheckAttendance, mClassItem1RecordScores, mClassItem1PerfectScore, mClassItem1RecordSubmissions, mClassItem1SubmissionDueDate);
        assertEquals(1, rowsUpdated);

        ClassItemContract.ClassItemEntry entry = ClassItemContract._getEntry(mDb, mClassItem0Id, mClass0Id);
        assertNotNull(entry);
        assertEquals(mClassItem1Description, entry.getDescription());
        assertTrue(mClassItem1ItemType.equals(entry.getItemType()));
        assertEquals(mClassItem1ItemDate, entry.getItemDate());
        assertEquals(mClassItem1CheckAttendance, entry.isCheckAttendance());
        assertEquals(mClassItem1RecordScores, entry.isRecordScores());
        assertEquals(mClassItem1PerfectScore, entry.getPerfectScore());
        assertEquals(mClassItem1RecordSubmissions, entry.isRecordSubmissions());
        assertEquals(mClassItem1SubmissionDueDate, entry.getSubmissionDueDate());
    }
}
