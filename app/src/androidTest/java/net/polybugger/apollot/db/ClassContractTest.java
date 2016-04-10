package net.polybugger.apollot.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
public class ClassContractTest {

    private SQLiteDatabase mDb;
    private Context mContext;
    private long mClass0Id;
    private String mClass0Code;
    private String mClass0Description;
    private AcademicTermContract.AcademicTermEntry mClass0AcademicTerm;
    private Long mClass0Year;
    private PastCurrentEnum mClass0PastCurrent;
    private Date mClass0DateCreated;
    private long mClass1Id;
    private String mClass1Code;
    private String mClass1Description;
    private AcademicTermContract.AcademicTermEntry mClass1AcademicTerm;
    private Long mClass1Year;
    private PastCurrentEnum mClass1PastCurrent;
    private Date mClass1DateCreated;

    @Before
    public void setUp() throws Exception {
        mContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_");
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_TIME_DISPLAY_TEMPLATE, mContext.getResources().getConfiguration().locale);
        ApolloDbAdapter.setAppContext(mContext);
        mDb = ApolloDbAdapter.open();
        mDb.setForeignKeyConstraintsEnabled(false);
        mDb.execSQL(ClassContract.DELETE_ALL_SQL);
        mClass0Code = mContext.getString(R.string.default_class_0_code);
        mClass0Description = mContext.getString(R.string.default_class_0_description);
        mClass0AcademicTerm = AcademicTermContract._getEntryByDescription(mDb, mContext.getString(R.string.default_class_0_academic_term));
        mClass0Year = (long) mContext.getResources().getInteger(R.integer.default_class_0_year);
        mClass0PastCurrent = PastCurrentEnum.fromInt(mContext.getResources().getInteger(R.integer.default_class_0_past_current));
        try {
            mClass0DateCreated = sdf.parse(mContext.getString(R.string.default_class_0_date_created));
        }
        catch(Exception e) {
            mClass0DateCreated = null;
        }
        mClass0Id = ApolloDbAdapter._insertDummyClass0(mDb);
        mClass1Code = mContext.getString(R.string.default_class_1_code);
        mClass1Description = mContext.getString(R.string.default_class_1_description);
        mClass1AcademicTerm = AcademicTermContract._getEntryByDescription(mDb, mContext.getString(R.string.default_class_1_academic_term));
        mClass1Year = (long) mContext.getResources().getInteger(R.integer.default_class_1_year);
        mClass1PastCurrent = PastCurrentEnum.fromInt(mContext.getResources().getInteger(R.integer.default_class_1_past_current));
        try {
            mClass1DateCreated = sdf.parse(mContext.getString(R.string.default_class_1_date_created));
        }
        catch(Exception e) {
            mClass1DateCreated = null;
        }
        mClass1Id = ApolloDbAdapter._insertDummyClass1(mDb);
    }

    @After
    public void tearDown() throws Exception {
        mDb.execSQL(ClassContract.DELETE_ALL_SQL);
        mClass0Id = ApolloDbAdapter._insertDummyClass0(mDb);
        mClass1Id = ApolloDbAdapter._insertDummyClass1(mDb);
        ApolloDbAdapter.close();
    }

    @Test
    public void test_methods() throws Exception {
        ClassContract.ClassEntry entry = ClassContract._getEntry(mDb, mClass0Id);
        assertEquals(mClass0Id, entry.getId());
        assertEquals(mClass0Code, entry.getCode());
        assertEquals(mClass0Description, entry.getDescription());
        assertNotNull(mClass0AcademicTerm);
        assertTrue(mClass0AcademicTerm.equals(entry.getAcademicTerm()));
        assertEquals(mClass0Year, entry.getYear());
        assertEquals(mClass0PastCurrent, entry.getPastCurrent());
        assertEquals(mClass0DateCreated, entry.getDateCreated());

        assertNotEquals(mClass0Id, mClass1Id);
        assertNotEquals(mClass0Code, mClass1Code);
        assertNotEquals(mClass0Description, mClass1Description);
        assertNotNull(mClass1AcademicTerm);
        assertFalse(mClass1AcademicTerm.equals(mClass0AcademicTerm));
        assertNotEquals(mClass0Year, mClass1Year);
        assertNotEquals(mClass0PastCurrent, mClass1PastCurrent);
        assertNotEquals(mClass0DateCreated, mClass1DateCreated);

        entry.setId(mClass1Id);
        entry.setCode(mClass1Code);
        entry.setDescription(mClass1Description);
        entry.setAcademicTerm(mClass1AcademicTerm);
        entry.setYear(mClass1Year);
        entry.setPastCurrent(mClass1PastCurrent);
        entry.setDateCreated(mClass1DateCreated);
        assertEquals(mClass1Id, entry.getId());
        assertEquals(mClass1Code, entry.getCode());
        assertEquals(mClass1Description, entry.getDescription());
        assertTrue(mClass1AcademicTerm.equals(entry.getAcademicTerm()));
        assertEquals(mClass1Year, entry.getYear());
        assertEquals(mClass1PastCurrent, entry.getPastCurrent());
        assertEquals(mClass1DateCreated, entry.getDateCreated());
    }

    @Test
    public void test_getEntry() throws Exception {
        ClassContract.ClassEntry entry = ClassContract._getEntry(mDb, mClass0Id);
        assertNotNull(entry);
        assertEquals(mClass0Id, entry.getId());
        assertEquals(mClass0Code, entry.getCode());
        assertEquals(mClass0Description, entry.getDescription());
        assertNotNull(mClass0AcademicTerm);
        assertTrue(mClass0AcademicTerm.equals(entry.getAcademicTerm()));
        assertEquals(mClass0Year, entry.getYear());
        assertEquals(mClass0PastCurrent, entry.getPastCurrent());
        assertEquals(mClass0DateCreated, entry.getDateCreated());

        ClassContract.ClassEntry entryByCode = ClassContract._getEntryByCode(mDb, mClass0Code);
        assertNotNull(entryByCode);
        assertEquals(mClass0Id, entryByCode.getId());
        assertEquals(mClass0Code, entryByCode.getCode());
        assertEquals(mClass0Description, entryByCode.getDescription());
        assertNotNull(mClass0AcademicTerm);
        assertTrue(mClass0AcademicTerm.equals(entryByCode.getAcademicTerm()));
        assertEquals(mClass0Year, entryByCode.getYear());
        assertEquals(mClass0PastCurrent, entryByCode.getPastCurrent());
        assertEquals(mClass0DateCreated, entryByCode.getDateCreated());
    }

    @Test
    public void test_getEntries() throws Exception {
        ArrayList<ClassContract.ClassEntry> entries = ClassContract._getEntries(mDb);
        ClassContract.ClassEntry entry0 = ClassContract._getEntry(mDb, mClass0Id);
        ClassContract.ClassEntry entry1 = ClassContract._getEntry(mDb, mClass1Id);

        assertNotNull(entries);
        assertNotNull(entry0);
        assertTrue(entries.contains(entry0));
        assertNotNull(entry1);
        assertTrue(entries.contains(entry1));
    }

    @Test
    public void test_delete() throws Exception {
        int rowsDeleted = ClassContract._delete(mDb, mClass0Id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = ClassContract._delete(mDb, mClass0Id);
        assertEquals(0, rowsDeleted);
        ClassContract.ClassEntry entry = ClassContract._getEntry(mDb, mClass0Id);
        assertNull(entry);
    }

    @Test
    public void test_update() throws Exception {
        int rowsUpdated = ClassContract._update(mDb, mClass0Id, mClass1Code, mClass1Description, mClass1AcademicTerm, mClass1Year, mClass1PastCurrent, mClass1DateCreated);
        assertEquals(1, rowsUpdated);

        ClassContract.ClassEntry entry = ClassContract._getEntry(mDb, mClass0Id);
        assertNotNull(entry);
        assertEquals(mClass0Id, entry.getId());
        assertEquals(mClass1Code, entry.getCode());
        assertEquals(mClass1Description, entry.getDescription());
        assertNotNull(mClass1AcademicTerm);
        assertTrue(mClass1AcademicTerm.equals(entry.getAcademicTerm()));
        assertEquals(mClass1Year, entry.getYear());
        assertEquals(mClass1PastCurrent, entry.getPastCurrent());
        assertEquals(mClass1DateCreated, entry.getDateCreated());
    }
}
