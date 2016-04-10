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

import java.util.ArrayList;

import net.polybugger.apollot.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
        mDb.setForeignKeyConstraintsEnabled(false);
        mDb.execSQL(StudentContract.DELETE_ALL_SQL);
        ApolloDbAdapter._insertDummyStudents(mDb);
        mStudent0LastName = mContext.getString(R.string.default_student_0_last_name);
        mStudent0FirstName = mContext.getString(R.string.default_student_0_first_name);
        mStudent0MiddleName = mContext.getString(R.string.default_student_0_middle_name);
        mStudent0Gender = GenderEnum.fromInt(mContext.getResources().getInteger(R.integer.default_student_0_gender));
        mStudent0EmailAddress = mContext.getString(R.string.default_student_0_email_address);
        mStudent0ContactNumber = mContext.getString(R.string.default_student_0_contact_number);
        mStudent0Id = StudentContract._getEntryByLastName(mDb, mStudent0LastName).getId();
        mStudent1LastName = mContext.getString(R.string.default_student_1_last_name);
        mStudent1FirstName = mContext.getString(R.string.default_student_1_first_name);
        mStudent1MiddleName = mContext.getString(R.string.default_student_1_middle_name);
        mStudent1Gender = GenderEnum.fromInt(mContext.getResources().getInteger(R.integer.default_student_1_gender));
        mStudent1EmailAddress = mContext.getString(R.string.default_student_1_email_address);
        mStudent1ContactNumber = mContext.getString(R.string.default_student_1_contact_number);
        mStudent1Id = StudentContract._getEntryByLastName(mDb, mStudent1LastName).getId();
    }

    @After
    public void tearDown() throws Exception {
        mDb.execSQL(StudentContract.DELETE_ALL_SQL);
        ApolloDbAdapter._insertDummyStudents(mDb);
        ApolloDbAdapter.close();
    }

    @Test
    public void test_methods() throws Exception {
        StudentContract.StudentEntry entry = StudentContract._getEntry(mDb, mStudent0Id);
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
        entry.setLastName(mStudent1LastName);
        entry.setFirstName(mStudent1FirstName);
        entry.setMiddleName(mStudent1MiddleName);
        entry.setGender(mStudent1Gender);
        entry.setEmailAddress(mStudent1EmailAddress);
        entry.setContactNumber(mStudent1ContactNumber);
        assertEquals(mStudent1Id, entry.getId());
        assertEquals(mStudent1LastName, entry.getLastName());
        assertEquals(mStudent1FirstName, entry.getFirstName());
        assertEquals(mStudent1MiddleName, entry.getMiddleName());
        assertEquals(mStudent1Gender, entry.getGender());
        assertEquals(mStudent1EmailAddress, entry.getEmailAddress());
        assertEquals(mStudent1ContactNumber, entry.getContactNumber());
    }

    @Test
    public void test_getEntry() throws Exception {
        StudentContract.StudentEntry entry = StudentContract._getEntry(mDb, mStudent0Id);
        assertNotNull(entry);
        assertEquals(mStudent0Id, entry.getId());
        assertEquals(mStudent0LastName, entry.getLastName());
        assertEquals(mStudent0FirstName, entry.getFirstName());
        assertEquals(mStudent0MiddleName, entry.getMiddleName());
        assertEquals(mStudent0Gender, entry.getGender());
        assertEquals(mStudent0EmailAddress, entry.getEmailAddress());
        assertEquals(mStudent0ContactNumber, entry.getContactNumber());

        StudentContract.StudentEntry entryByLastName = StudentContract._getEntryByLastName(mDb, mStudent0LastName);
        assertNotNull(entryByLastName);
        assertEquals(mStudent0Id, entryByLastName.getId());
        assertEquals(mStudent0LastName, entryByLastName.getLastName());
        assertEquals(mStudent0FirstName, entryByLastName.getFirstName());
        assertEquals(mStudent0MiddleName, entryByLastName.getMiddleName());
        assertEquals(mStudent0Gender, entryByLastName.getGender());
        assertEquals(mStudent0EmailAddress, entryByLastName.getEmailAddress());
        assertEquals(mStudent0ContactNumber, entryByLastName.getContactNumber());
    }

    @Test
    public void test_getEntries() throws Exception {
        ArrayList<StudentContract.StudentEntry> entries = StudentContract._getEntries(mDb);
        StudentContract.StudentEntry entry0 = StudentContract._getEntry(mDb, mStudent0Id);
        StudentContract.StudentEntry entry1 = StudentContract._getEntry(mDb, mStudent1Id);

        assertNotNull(entries);
        assertNotNull(entry0);
        assertTrue(entries.contains(entry0));
        assertNotNull(entry1);
        assertTrue(entries.contains(entry1));
    }

    @Test
    public void test_delete() throws Exception {
        int rowsDeleted = StudentContract._delete(mDb, mStudent0Id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = StudentContract._delete(mDb, mStudent0Id);
        assertEquals(0, rowsDeleted);
        StudentContract.StudentEntry entry = StudentContract._getEntry(mDb, mStudent0Id);
        assertNull(entry);
    }

    @Test
    public void test_update() throws Exception {
        int rowsUpdated = StudentContract._update(mDb, mStudent0Id, mStudent1LastName, mStudent1FirstName, mStudent1MiddleName, mStudent1Gender, mStudent1EmailAddress, mStudent1ContactNumber);
        assertEquals(1, rowsUpdated);

        StudentContract.StudentEntry entry = StudentContract._getEntry(mDb, mStudent0Id);
        assertNotNull(entry);
        assertEquals(mStudent0Id, entry.getId());
        assertEquals(mStudent1LastName, entry.getLastName());
        assertEquals(mStudent1FirstName, entry.getFirstName());
        assertEquals(mStudent1MiddleName, entry.getMiddleName());
        assertEquals(mStudent1Gender, entry.getGender());
        assertEquals(mStudent1EmailAddress, entry.getEmailAddress());
        assertEquals(mStudent1ContactNumber, entry.getContactNumber());
    }
}
