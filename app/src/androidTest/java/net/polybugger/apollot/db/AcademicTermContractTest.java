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
public class AcademicTermContractTest {

    private SQLiteDatabase mDb;
    private Context mContext;
    private long mAcademicTerm0Id;
    private String mAcademicTerm0Description;
    private String mAcademicTerm0Color;
    private long mAcademicTerm1Id;
    private String mAcademicTerm1Description;
    private String mAcademicTerm1Color;

    @Before
    public void setUp() throws Exception {
        mContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_");
        ApolloDbAdapter.setAppContext(mContext);
        mDb = ApolloDbAdapter.open();
        mDb.setForeignKeyConstraintsEnabled(false);
        mDb.execSQL(AcademicTermContract.DELETE_ALL_SQL);
        AcademicTermContract._insertDefaultAcademicTerms(mDb, mContext);
        mAcademicTerm0Description = mContext.getString(R.string.default_academic_term_0);
        mAcademicTerm0Color = mContext.getString(R.string.default_academic_term_color_0);
        mAcademicTerm0Id = AcademicTermContract._getEntryByDescription(mDb, mAcademicTerm0Description).getId();
        mAcademicTerm1Description = mContext.getString(R.string.default_academic_term_1);
        mAcademicTerm1Color = mContext.getString(R.string.default_academic_term_color_1);
        mAcademicTerm1Id = AcademicTermContract._getEntryByDescription(mDb, mAcademicTerm1Description).getId();
    }

    @After
    public void tearDown() throws Exception {
        mDb.execSQL(AcademicTermContract.DELETE_ALL_SQL);
        AcademicTermContract._insertDefaultAcademicTerms(mDb, mContext);
        ApolloDbAdapter.close();
    }

    @Test
    public void test_methods() throws Exception {
        AcademicTermContract.AcademicTermEntry entry = AcademicTermContract._getEntry(mDb, mAcademicTerm0Id);
        assertEquals(mAcademicTerm0Id, entry.getId());
        assertEquals(mAcademicTerm0Description, entry.getDescription());
        assertEquals(mAcademicTerm0Color, entry.getColor());

        assertNotEquals(mAcademicTerm0Id, mAcademicTerm1Id);
        assertNotEquals(mAcademicTerm0Description, mAcademicTerm1Description);
        assertNotEquals(mAcademicTerm0Color, mAcademicTerm1Color);

        entry.setId(mAcademicTerm1Id);
        entry.setDescription(mAcademicTerm1Description);
        entry.setColor(mAcademicTerm1Color);
        assertEquals(mAcademicTerm1Id, entry.getId());
        assertEquals(mAcademicTerm1Description, entry.getDescription());
        assertEquals(mAcademicTerm1Color, entry.getColor());
    }

    @Test
    public void test_getEntryByDescription() throws Exception {
        AcademicTermContract.AcademicTermEntry entryByDescription = AcademicTermContract._getEntryByDescription(mDb, mAcademicTerm0Description);
        assertEquals(mAcademicTerm0Id, entryByDescription.getId());
        assertEquals(mAcademicTerm0Description, entryByDescription.getDescription());
        assertEquals(mAcademicTerm0Color, entryByDescription.getColor());
    }

    @Test
    public void test_getEntries() throws Exception {
        ArrayList<AcademicTermContract.AcademicTermEntry> entries = AcademicTermContract._getEntries(mDb);
        AcademicTermContract.AcademicTermEntry entry0 = AcademicTermContract._getEntryByDescription(mDb, mContext.getString(R.string.default_academic_term_0));
        AcademicTermContract.AcademicTermEntry entry1 = AcademicTermContract._getEntryByDescription(mDb, mContext.getString(R.string.default_academic_term_1));
        AcademicTermContract.AcademicTermEntry entry2 = AcademicTermContract._getEntryByDescription(mDb, mContext.getString(R.string.default_academic_term_2));
        AcademicTermContract.AcademicTermEntry entry3 = AcademicTermContract._getEntryByDescription(mDb, mContext.getString(R.string.default_academic_term_3));
        AcademicTermContract.AcademicTermEntry entry4 = AcademicTermContract._getEntryByDescription(mDb, mContext.getString(R.string.default_academic_term_4));
        AcademicTermContract.AcademicTermEntry entry5 = AcademicTermContract._getEntryByDescription(mDb, mContext.getString(R.string.default_academic_term_5));
        AcademicTermContract.AcademicTermEntry entry6 = AcademicTermContract._getEntryByDescription(mDb, mContext.getString(R.string.default_academic_term_6));
        AcademicTermContract.AcademicTermEntry entry7 = AcademicTermContract._getEntryByDescription(mDb, mContext.getString(R.string.default_academic_term_7));
        AcademicTermContract.AcademicTermEntry entry8 = AcademicTermContract._getEntryByDescription(mDb, mContext.getString(R.string.default_academic_term_8));
        AcademicTermContract.AcademicTermEntry entry9 = AcademicTermContract._getEntryByDescription(mDb, mContext.getString(R.string.default_academic_term_9));
        AcademicTermContract.AcademicTermEntry entry10 = AcademicTermContract._getEntryByDescription(mDb, mContext.getString(R.string.default_academic_term_10));
        AcademicTermContract.AcademicTermEntry entry11 = AcademicTermContract._getEntryByDescription(mDb, mContext.getString(R.string.default_academic_term_11));
        AcademicTermContract.AcademicTermEntry entry12 = AcademicTermContract._getEntryByDescription(mDb, mContext.getString(R.string.default_academic_term_12));

        assertNotNull(entries);
        assertNotNull(entry0);
        assertTrue(entries.contains(entry0));
        assertNotNull(entry1);
        assertTrue(entries.contains(entry1));
        assertNotNull(entry2);
        assertTrue(entries.contains(entry2));
        assertNotNull(entry3);
        assertTrue(entries.contains(entry3));
        assertNotNull(entry4);
        assertTrue(entries.contains(entry4));
        assertNotNull(entry5);
        assertTrue(entries.contains(entry5));
        assertNotNull(entry6);
        assertTrue(entries.contains(entry6));
        assertNotNull(entry7);
        assertTrue(entries.contains(entry7));
        assertNotNull(entry8);
        assertTrue(entries.contains(entry8));
        assertNotNull(entry9);
        assertTrue(entries.contains(entry9));
        assertNotNull(entry10);
        assertTrue(entries.contains(entry10));
        assertNotNull(entry11);
        assertTrue(entries.contains(entry11));
        assertNotNull(entry12);
        assertTrue(entries.contains(entry12));
    }

    @Test
    public void test_delete() throws Exception {
        int rowsDeleted = AcademicTermContract._delete(mDb, mAcademicTerm0Id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = AcademicTermContract._delete(mDb, mAcademicTerm0Id);
        assertEquals(0, rowsDeleted);
        AcademicTermContract.AcademicTermEntry entry = AcademicTermContract._getEntry(mDb, mAcademicTerm0Id);
        assertNull(entry);
    }

    @Test
    public void test_update() throws Exception {
        int rowsUpdated = AcademicTermContract._update(mDb, mAcademicTerm0Id, mAcademicTerm1Description, mAcademicTerm1Color);
        assertEquals(1, rowsUpdated);

        AcademicTermContract.AcademicTermEntry entry = AcademicTermContract._getEntry(mDb, mAcademicTerm0Id);
        assertNotNull(entry);
        assertEquals(mAcademicTerm0Id, entry.getId());
        assertEquals(mAcademicTerm1Description, entry.getDescription());
        assertEquals(mAcademicTerm1Color, entry.getColor());
    }
}
