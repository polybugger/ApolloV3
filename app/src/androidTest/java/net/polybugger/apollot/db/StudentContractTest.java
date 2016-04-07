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

    @Test
    public void test_getEntryId() throws Exception {
        StudentContract.StudentEntry _entry = StudentContract._getEntry(mDb, mStudent0Id);
        assertNotNull(_entry);
        assertEquals(mStudent0Id, _entry.getId());
        assertEquals(mStudent0LastName, _entry.getLastName());
        assertEquals(mStudent0FirstName, _entry.getFirstName());
        assertEquals(mStudent0MiddleName, _entry.getMiddleName());
        assertEquals(mStudent0Gender, _entry.getGender());
        assertEquals(mStudent0EmailAddress, _entry.getEmailAddress());
        assertEquals(mStudent0ContactNumber, _entry.getContactNumber());

        StudentContract.StudentEntry entry = StudentContract.getEntry(mStudent0Id);
        assertNotNull(entry);
        assertEquals(mStudent0Id, entry.getId());
        assertEquals(mStudent0LastName, entry.getLastName());
        assertEquals(mStudent0FirstName, entry.getFirstName());
        assertEquals(mStudent0MiddleName, entry.getMiddleName());
        assertEquals(mStudent0Gender, entry.getGender());
        assertEquals(mStudent0EmailAddress, entry.getEmailAddress());
        assertEquals(mStudent0ContactNumber, entry.getContactNumber());

        assertTrue(_entry.equals(entry));
    }

    @Test
    public void test_getEntries() throws Exception {
        ArrayList<StudentContract.StudentEntry> _entries = StudentContract._getEntries(mDb);
        StudentContract.StudentEntry _entry0 = StudentContract._getEntry(mDb, mStudent0Id);
        StudentContract.StudentEntry _entry1 = StudentContract._getEntry(mDb, mStudent1Id);

        assertNotNull(_entries);
        assertNotNull(_entry0);
        assertTrue(_entries.contains(_entry0));
        assertNotNull(_entry1);
        assertTrue(_entries.contains(_entry1));

        ArrayList<StudentContract.StudentEntry> entries = StudentContract.getEntries();
        assertNotNull(entries);
        assertArrayEquals(_entries.toArray(), entries.toArray());
    }

    @Test
    public void test_delete() throws Exception {
        int rowsDeleted = StudentContract._delete(mDb, mStudent0Id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = StudentContract._delete(mDb, mStudent0Id);
        assertEquals(0, rowsDeleted);
        StudentContract.StudentEntry _entry = StudentContract._getEntry(mDb, mStudent0Id);
        assertNull(_entry);

        rowsDeleted = StudentContract.delete(mStudent1Id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = StudentContract.delete(mStudent1Id);
        assertEquals(0, rowsDeleted);
        StudentContract.StudentEntry entry = StudentContract.getEntry(mStudent0Id);
        assertNull(entry);
    }

    @Test
    public void test_update() throws Exception {
        StudentContract.StudentEntry _entry0 = StudentContract._getEntry(mDb, mStudent0Id);
        assertNotNull(_entry0);
        assertNotEquals(mStudent1Id, _entry0.getId());
        assertNotEquals(mStudent1LastName, _entry0.getLastName());
        assertNotEquals(mStudent1FirstName, _entry0.getFirstName());
        assertNotEquals(mStudent1MiddleName, _entry0.getMiddleName());
        assertNotEquals(mStudent1Gender, _entry0.getGender());
        assertNotEquals(mStudent1EmailAddress, _entry0.getEmailAddress());
        assertNotEquals(mStudent1ContactNumber, _entry0.getContactNumber());

        int rowsUpdated = StudentContract._update(mDb, mStudent0Id, mStudent1LastName, mStudent1FirstName, mStudent1MiddleName, mStudent1Gender, mStudent1EmailAddress, mStudent1ContactNumber);
        assertEquals(1, rowsUpdated);
        StudentContract.StudentEntry _entry0Updated = StudentContract._getEntry(mDb, mStudent0Id);
        assertNotNull(_entry0Updated);
        assertEquals(mStudent0Id, _entry0Updated.getId());
        assertEquals(mStudent1LastName, _entry0Updated.getLastName());
        assertEquals(mStudent1FirstName, _entry0Updated.getFirstName());
        assertEquals(mStudent1MiddleName, _entry0Updated.getMiddleName());
        assertEquals(mStudent1Gender, _entry0Updated.getGender());
        assertEquals(mStudent1EmailAddress, _entry0Updated.getEmailAddress());
        assertEquals(mStudent1ContactNumber, _entry0Updated.getContactNumber());

        assertNotEquals(mStudent0LastName, _entry0Updated.getLastName());
        assertNotEquals(mStudent0FirstName, _entry0Updated.getFirstName());
        assertNotEquals(mStudent0MiddleName, _entry0Updated.getMiddleName());
        assertNotEquals(mStudent0Gender, _entry0Updated.getGender());
        assertNotEquals(mStudent0EmailAddress, _entry0Updated.getEmailAddress());
        assertNotEquals(mStudent0ContactNumber, _entry0Updated.getContactNumber());

        rowsUpdated = StudentContract.update(mStudent0Id, mStudent0LastName, mStudent0FirstName, mStudent0MiddleName, mStudent0Gender, mStudent0EmailAddress, mStudent0ContactNumber);
        assertEquals(1, rowsUpdated);
        _entry0Updated = StudentContract.getEntry(mStudent0Id);
        assertNotNull(_entry0Updated);
        assertEquals(mStudent0Id, _entry0Updated.getId());
        assertEquals(mStudent0LastName, _entry0Updated.getLastName());
        assertEquals(mStudent0FirstName, _entry0Updated.getFirstName());
        assertEquals(mStudent0MiddleName, _entry0Updated.getMiddleName());
        assertEquals(mStudent0Gender, _entry0Updated.getGender());
        assertEquals(mStudent0EmailAddress, _entry0Updated.getEmailAddress());
        assertEquals(mStudent0ContactNumber, _entry0Updated.getContactNumber());
    }

    @Test
    public void test_insert() throws Exception {
        StudentContract.StudentEntry _entry0 = StudentContract._getEntry(mDb, mStudent0Id);
        assertNotNull(_entry0);
        assertEquals(mStudent0Id, _entry0.getId());

        int rowsDeleted = StudentContract._delete(mDb, mStudent0Id);
        assertEquals(1, rowsDeleted);
        _entry0 = StudentContract._getEntry(mDb, mStudent0Id);
        assertNull(_entry0);

        long student2Id = StudentContract._insert(mDb, mStudent0LastName, mStudent0FirstName, mStudent0MiddleName, mStudent0Gender, mStudent0EmailAddress, mStudent0ContactNumber);
        StudentContract.StudentEntry _entry2 = StudentContract._getEntry(mDb, student2Id);
        assertNotNull(_entry2);
        assertEquals(student2Id, _entry2.getId());
        assertEquals(mStudent0LastName, _entry2.getLastName());
        assertEquals(mStudent0FirstName, _entry2.getFirstName());
        assertEquals(mStudent0MiddleName, _entry2.getMiddleName());
        assertEquals(mStudent0Gender, _entry2.getGender());
        assertEquals(mStudent0EmailAddress, _entry2.getEmailAddress());
        assertEquals(mStudent0ContactNumber, _entry2.getContactNumber());
    }

    private void _insertDummyStudents() {
        mStudent0Id = StudentContract._insert(mDb, mStudent0LastName, mStudent0FirstName, mStudent0MiddleName, mStudent0Gender, mStudent0EmailAddress, mStudent0ContactNumber);

        mStudent1Id = StudentContract._insert(mDb, mStudent1LastName, mStudent1FirstName, mStudent1MiddleName, mStudent1Gender, mStudent1EmailAddress, mStudent1ContactNumber);
    }
}
