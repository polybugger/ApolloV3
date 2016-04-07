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

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class StudentContractTest {

    private SQLiteDatabase mDb;
    private Context mContext;
    private long mStudent0Id;
    private String mStudent0LastName;
    private String mStudent0FirstName;
    private String mStudent0MiddleName;
    private GenderEnum mStudent0Gender;
    private String mStudent0EmailAddress;
    private String mStudent0ContactNumber;
    private long mStudent1Id;
    private String mStudent1LastName;
    private String mStudent1FirstName;
    private String mStudent1MiddleName;
    private GenderEnum mStudent1Gender;
    private String mStudent1EmailAddress;
    private String mStudent1ContactNumber;

    @Before
    public void setUp() throws Exception {
        mContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_");
        ApolloDbAdapter.setAppContext(mContext);
        mDb = ApolloDbAdapter.open();
        mStudent0LastName = mContext.getString(R.string.default_student_0_last_name);
        mStudent0FirstName = mContext.getString(R.string.default_student_0_first_name);
        mStudent0MiddleName = mContext.getString(R.string.default_student_0_middle_name);
        mStudent0Gender = GenderEnum.fromInt(mContext.getResources().getInteger(R.integer.default_student_0_gender));
        mStudent0EmailAddress = mContext.getString(R.string.default_student_0_email_address);
        mStudent0ContactNumber = mContext.getString(R.string.default_student_0_contact_number);
        mStudent1LastName = mContext.getString(R.string.default_student_1_last_name);
        mStudent1FirstName = mContext.getString(R.string.default_student_1_first_name);
        mStudent1MiddleName = mContext.getString(R.string.default_student_1_middle_name);
        mStudent1Gender = GenderEnum.fromInt(mContext.getResources().getInteger(R.integer.default_student_1_gender));
        mStudent1EmailAddress = mContext.getString(R.string.default_student_1_email_address);
        mStudent1ContactNumber = mContext.getString(R.string.default_student_1_contact_number);
        mDb.setForeignKeyConstraintsEnabled(false);
        mDb.execSQL(StudentContract.DELETE_ALL_SQL);
        _insertDummyStudents();
    }

    @After
    public void tearDown() throws Exception {
        mDb.execSQL(StudentContract.DELETE_ALL_SQL);
        _insertDummyStudents();
        ApolloDbAdapter.close();
    }

    @Test
    public void testEntryConstructorAndMethods() throws Exception {
        StudentContract.StudentEntry entry = new StudentContract.StudentEntry(mStudent0Id, mStudent0LastName, mStudent0FirstName, mStudent0MiddleName, mStudent0Gender, mStudent0EmailAddress, mStudent0ContactNumber);
        assertEquals(mStudent0Id, entry.getId());
        assertEquals(mStudent0LastName, entry.getLastName());
        assertEquals(mStudent0FirstName, entry.getFirstName());
        assertEquals(mStudent0MiddleName, entry.getMiddleName());
        assertEquals(mStudent0Gender, entry.getGender());
        assertEquals(mStudent0EmailAddress, entry.getEmailAddress());
        assertEquals(mStudent0ContactNumber, entry.getContactNumber());

        assertNotEquals(mStudent0Id, mStudent1Id);
        assertNotEquals(mStudent0LastName, mStudent1LastName);
        assertNotEquals(mStudent0FirstName, mStudent1FirstName);
        assertNotEquals(mStudent0MiddleName, mStudent1MiddleName);
        assertNotEquals(mStudent0Gender, mStudent1Gender);
        assertNotEquals(mStudent0EmailAddress, mStudent1EmailAddress);
        assertNotEquals(mStudent0ContactNumber, mStudent1ContactNumber);

        entry.setId(mStudent1Id);
        assertEquals(mStudent1Id, entry.getId());
        entry.setLastName(mStudent1LastName);
        assertEquals(mStudent1LastName, entry.getLastName());
        entry.setFirstName(mStudent1FirstName);
        assertEquals(mStudent1FirstName, entry.getFirstName());
        entry.setMiddleName(mStudent1MiddleName);
        assertEquals(mStudent1MiddleName, entry.getMiddleName());
        entry.setGender(mStudent1Gender);
        assertEquals(mStudent1Gender, entry.getGender());
        entry.setEmailAddress(mStudent1EmailAddress);
        assertEquals(mStudent1EmailAddress, entry.getEmailAddress());
        entry.setContactNumber(mStudent1ContactNumber);
        assertEquals(mStudent1ContactNumber, entry.getContactNumber());
    }

    /*
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
    */

    private void _insertDummyStudents() {
        mStudent0Id = StudentContract._insert(mDb, mStudent0LastName, mStudent0FirstName, mStudent0MiddleName, mStudent0Gender, mStudent0EmailAddress, mStudent0ContactNumber);

        mStudent1Id = StudentContract._insert(mDb, mStudent1LastName, mStudent1FirstName, mStudent1MiddleName, mStudent1Gender, mStudent1EmailAddress, mStudent1ContactNumber);
    }
}
