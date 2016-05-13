package net.polybugger.apollot.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

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

@RunWith(AndroidJUnit4.class)
@MediumTest
public class ClassPasswordContractTest {

    private SQLiteDatabase mDb;
    private Context mContext;
    private ClassContract.ClassEntry mClass1;
    private long mPassword0Id;
    private String mPassword0;

    @Before
    public void setUp() throws Exception {
        mContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_");
        ApolloDbAdapter.setAppContext(mContext);
        mDb = ApolloDbAdapter.open();
        mDb.setForeignKeyConstraintsEnabled(false);
        mDb.execSQL(ClassPasswordContract.DELETE_ALL_SQL);
        mClass1 = ClassContract._getEntryByCode(mDb, mContext.getString(R.string.default_class_1_code));
        mPassword0 = mContext.getString(R.string.default_class_1_password);
        //mPassword0Id = ApolloDbAdapter._insertDummyClass1Password(mDb, mClass1.getId());
    }

    @After
    public void tearDown() throws Exception {
        mDb.execSQL(ClassPasswordContract.DELETE_ALL_SQL);
        //ApolloDbAdapter._insertDummyClass1Password(mDb, mClass1.getId());
        ApolloDbAdapter.close();
    }

    @Test
    public void test_methods() throws Exception {
        ClassPasswordContract.ClassPasswordEntry entry = ClassPasswordContract._getEntry(mDb, mPassword0Id);
        assertEquals(mPassword0Id, entry.getId());
        assertEquals(mClass1.getId(), entry.getClassId());
        assertEquals(mPassword0, entry.getPassword());

        long newId = 999;
        long newClassId = 999;
        String newPassword = "1234";
        assertNotEquals(mPassword0Id, newId);
        assertNotEquals(mClass1.getId(), newClassId);
        assertNotEquals(mPassword0, newPassword);

        entry.setId(newId);
        entry.setClassId(newClassId);
        entry.setPassword(newPassword);
        assertEquals(newId, entry.getId());
        assertEquals(newClassId, entry.getClassId());
        assertEquals(newPassword, entry.getPassword());
    }

    @Test
    public void test_getEntry() throws Exception {
        ClassPasswordContract.ClassPasswordEntry entry = ClassPasswordContract._getEntryByClassId(mDb, mClass1.getId());
        assertEquals(mPassword0Id, entry.getId());
        assertEquals(mClass1.getId(), entry.getClassId());
        assertEquals(mPassword0, entry.getPassword());
    }

    @Test
    public void test_delete() throws Exception {
        int rowsDeleted = ClassPasswordContract._delete(mDb, mPassword0Id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = ClassPasswordContract._delete(mDb, mPassword0Id);
        assertEquals(0, rowsDeleted);
        ClassPasswordContract.ClassPasswordEntry entry = ClassPasswordContract._getEntryByClassId(mDb, mClass1.getId());
        assertNull(entry);

        //mPassword0Id = ApolloDbAdapter._insertDummyClass1Password(mDb, mClass1.getId());
        rowsDeleted = ClassPasswordContract._deleteByClassId(mDb, mClass1.getId());
        assertEquals(1, rowsDeleted);
        rowsDeleted = ClassPasswordContract._deleteByClassId(mDb, mClass1.getId());
        assertEquals(0, rowsDeleted);
        entry = ClassPasswordContract._getEntryByClassId(mDb, mClass1.getId());
        assertNull(entry);
    }
}
