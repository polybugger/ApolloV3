package net.polybugger.apollot.db;

import static org.junit.Assert.assertArrayEquals;
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
        ApolloDbAdapter.setAppContext(mContext);
        mDb = ApolloDbAdapter.open();
        mClass0Code = mContext.getString(R.string.default_class_0_code);
        mClass0Description = mContext.getString(R.string.default_class_0_description);
        mClass0AcademicTerm = null;
        mClass0Year = (long) mContext.getResources().getInteger(R.integer.default_class_0_year);
        mClass0PastCurrent = PastCurrentEnum.fromInt(mContext.getResources().getInteger(R.integer.default_class_0_past_current));
        mClass0DateCreated = new Date();
        mClass1Code = mContext.getString(R.string.default_class_1_code);
        mClass1Description = mContext.getString(R.string.default_class_1_description);
        mClass1AcademicTerm = null;
        mClass1Year = (long) mContext.getResources().getInteger(R.integer.default_class_1_year);
        mClass1PastCurrent = PastCurrentEnum.fromInt(mContext.getResources().getInteger(R.integer.default_class_1_past_current));;
        Thread.sleep(1);
        mClass1DateCreated = new Date();
        mDb.setForeignKeyConstraintsEnabled(false);
        mDb.execSQL(ClassContract.DELETE_ALL_SQL);
        _insertDummyClasses();
    }

    @After
    public void tearDown() throws Exception {
        mDb.execSQL(ClassContract.DELETE_ALL_SQL);
        _insertDummyClasses();
        ApolloDbAdapter.close();
    }

    @Test
    public void testEntryConstructorAndMethods() throws Exception {
        ClassContract.ClassEntry entry = new ClassContract.ClassEntry(mClass0Id, mClass0Code, mClass0Description, mClass0AcademicTerm, mClass0Year, mClass0PastCurrent, mClass0DateCreated);
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
        assertEquals(mClass1Id, entry.getId());
        entry.setCode(mClass1Code);
        assertEquals(mClass1Code, entry.getCode());
        entry.setDescription(mClass1Description);
        assertEquals(mClass1Description, entry.getDescription());
        entry.setAcademicTerm(mClass1AcademicTerm);
        assertTrue(mClass1AcademicTerm.equals(entry.getAcademicTerm()));
        entry.setYear(mClass1Year);
        assertEquals(mClass1Year, entry.getYear());
        entry.setPastCurrent(mClass1PastCurrent);
        assertEquals(mClass1PastCurrent, entry.getPastCurrent());
        entry.setDateCreated(mClass1DateCreated);
        assertEquals(mClass1DateCreated, entry.getDateCreated());
    }

    @Test
    public void test_getEntryId() throws Exception {
        ClassContract.ClassEntry _entry = ClassContract._getEntry(mDb, mClass0Id);
        assertNotNull(_entry);
        assertEquals(mClass0Id, _entry.getId());
        assertEquals(mClass0Code, _entry.getCode());
        assertEquals(mClass0Description, _entry.getDescription());
        assertNotNull(mClass0AcademicTerm);
        assertTrue(mClass0AcademicTerm.equals(_entry.getAcademicTerm()));
        assertEquals(mClass0Year, _entry.getYear());
        assertEquals(mClass0PastCurrent, _entry.getPastCurrent());
        assertEquals(mClass0DateCreated, _entry.getDateCreated());

        ClassContract.ClassEntry entry = ClassContract.getEntry(mClass0Id);
        assertNotNull(entry);
        assertEquals(mClass0Id, entry.getId());
        assertEquals(mClass0Code, entry.getCode());
        assertEquals(mClass0Description, entry.getDescription());
        assertNotNull(mClass0AcademicTerm);
        assertTrue(mClass0AcademicTerm.equals(entry.getAcademicTerm()));
        assertEquals(mClass0Year, entry.getYear());
        assertEquals(mClass0PastCurrent, entry.getPastCurrent());
        assertEquals(mClass0DateCreated, entry.getDateCreated());

        assertTrue(_entry.equals(entry));
    }

    @Test
    public void test_getEntries() throws Exception {
        ArrayList<ClassContract.ClassEntry> _entries = ClassContract._getEntries(mDb);
        ClassContract.ClassEntry _entry0 = ClassContract._getEntry(mDb, mClass0Id);
        ClassContract.ClassEntry _entry1 = ClassContract._getEntry(mDb, mClass1Id);

        assertNotNull(_entries);
        assertNotNull(_entry0);
        assertTrue(_entries.contains(_entry0));
        assertNotNull(_entry1);
        assertTrue(_entries.contains(_entry1));

        ArrayList<ClassContract.ClassEntry> entries = ClassContract.getEntries();
        assertNotNull(entries);
        assertArrayEquals(_entries.toArray(), entries.toArray());
    }

    @Test
    public void test_delete() throws Exception {
        int rowsDeleted = ClassContract._delete(mDb, mClass0Id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = ClassContract._delete(mDb, mClass0Id);
        assertEquals(0, rowsDeleted);
        ClassContract.ClassEntry _entry = ClassContract._getEntry(mDb, mClass0Id);
        assertNull(_entry);

        rowsDeleted = ClassContract.delete(mClass1Id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = ClassContract.delete(mClass1Id);
        assertEquals(0, rowsDeleted);
        ClassContract.ClassEntry entry = ClassContract.getEntry(mClass1Id);
        assertNull(entry);
    }

    @Test
    public void test_update() throws Exception {
        ClassContract.ClassEntry _entry0 = ClassContract._getEntry(mDb, mClass0Id);
        assertNotNull(_entry0);
        assertNotEquals(mClass1Code, _entry0.getCode());
        assertNotEquals(mClass1Description, _entry0.getDescription());
        assertNotNull(mClass1AcademicTerm);
        assertFalse(mClass1AcademicTerm.equals(_entry0.getAcademicTerm()));
        assertNotEquals(mClass1Year, _entry0.getYear());
        assertNotEquals(mClass1PastCurrent, _entry0.getPastCurrent());
        assertNotEquals(mClass1DateCreated, _entry0.getDateCreated());

        int rowsUpdated = ClassContract._update(mDb, mClass0Id, mClass1Code, mClass1Description, mClass1AcademicTerm, mClass1Year, mClass1PastCurrent, mClass1DateCreated);
        assertEquals(1, rowsUpdated);
        ClassContract.ClassEntry _entry0Updated = ClassContract._getEntry(mDb, mClass0Id);
        assertNotNull(_entry0Updated);
        assertEquals(mClass1Code, _entry0Updated.getCode());
        assertEquals(mClass1Description, _entry0Updated.getDescription());
        assertTrue(mClass1AcademicTerm.equals(_entry0Updated.getAcademicTerm()));
        assertEquals(mClass1Year, _entry0Updated.getYear());
        assertEquals(mClass1PastCurrent, _entry0Updated.getPastCurrent());
        assertEquals(mClass1DateCreated, _entry0Updated.getDateCreated());

        assertNotEquals(mClass0Code, _entry0Updated.getCode());
        assertNotEquals(mClass0Description, _entry0Updated.getDescription());
        assertNotNull(mClass0AcademicTerm);
        assertFalse(mClass0AcademicTerm.equals(_entry0Updated.getAcademicTerm()));
        assertNotEquals(mClass0Year, _entry0Updated.getYear());
        assertNotEquals(mClass0PastCurrent, _entry0Updated.getPastCurrent());
        assertNotEquals(mClass0DateCreated, _entry0Updated.getDateCreated());

        rowsUpdated = ClassContract.update(mClass0Id, mClass0Code, mClass0Description, mClass0AcademicTerm, mClass0Year, mClass0PastCurrent, mClass0DateCreated);
        assertEquals(1, rowsUpdated);
        _entry0Updated = ClassContract.getEntry(mClass0Id);
        assertNotNull(_entry0Updated);
        assertEquals(mClass0Code, _entry0Updated.getCode());
        assertEquals(mClass0Description, _entry0Updated.getDescription());
        assertTrue(mClass0AcademicTerm.equals(_entry0Updated.getAcademicTerm()));
        assertEquals(mClass0Year, _entry0Updated.getYear());
        assertEquals(mClass0PastCurrent, _entry0Updated.getPastCurrent());
        assertEquals(mClass0DateCreated, _entry0Updated.getDateCreated());
    }

    @Test
    public void test_insert() throws Exception {
        ClassContract.ClassEntry _entry0 = ClassContract._getEntry(mDb, mClass0Id);
        assertNotNull(_entry0);
        assertEquals(mClass0Id, _entry0.getId());

        int rowsDeleted = ClassContract._delete(mDb, mClass0Id);
        assertEquals(1, rowsDeleted);
        _entry0 = ClassContract._getEntry(mDb, mClass0Id);
        assertNull(_entry0);

        long class2Id = ClassContract._insert(mDb, mClass0Code, mClass0Description, mClass0AcademicTerm, mClass0Year, mClass0PastCurrent, mClass0DateCreated);
        ClassContract.ClassEntry _entry2 = ClassContract._getEntry(mDb, class2Id);
        assertNotNull(_entry2);
        assertEquals(class2Id, _entry2.getId());
        assertEquals(mClass0Code, _entry2.getCode());
        assertEquals(mClass0Description, _entry2.getDescription());
        assertNotNull(mClass0AcademicTerm);
        assertTrue(mClass0AcademicTerm.equals(_entry2.getAcademicTerm()));
        assertEquals(mClass0Year, _entry2.getYear());
        assertEquals(mClass0PastCurrent, _entry2.getPastCurrent());
        assertEquals(mClass0DateCreated, _entry2.getDateCreated());
    }

    private void _insertDummyClasses() {
        mClass0AcademicTerm = AcademicTermContract._getEntryByDescription(mDb, mContext.getString(R.string.default_class_0_academic_term));
        mClass0Id = ClassContract._insert(mDb, mClass0Code, mClass0Description, mClass0AcademicTerm, mClass0Year, mClass0PastCurrent, mClass0DateCreated);

        mClass1AcademicTerm = AcademicTermContract._getEntryByDescription(mDb, mContext.getString(R.string.default_class_1_academic_term));
        mClass1Id = ClassContract._insert(mDb, mClass1Code, mClass1Description, mClass1AcademicTerm, mClass1Year, mClass1PastCurrent, mClass1DateCreated);
    }
}
