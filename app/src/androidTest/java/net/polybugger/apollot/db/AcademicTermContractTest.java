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
    private long mId;
    private String mDescription;
    private String mColor;

    @Before
    public void setUp() throws Exception {
        mContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_");
        ApolloDbAdapter.setAppContext(mContext);
        mDb = ApolloDbAdapter.open();
        mId = 1;
        mDescription = mContext.getString(R.string.default_academic_term_0);
        mColor = mContext.getString(R.string.default_academic_term_color_0);

        mDb.setForeignKeyConstraintsEnabled(false);
        mDb.execSQL(AcademicTermContract.DELETE_ALL_SQL);
        _insertDefaultAcademicTerms(mDb);
    }

    @After
    public void tearDown() throws Exception {
        mDb.execSQL(AcademicTermContract.DELETE_ALL_SQL);
        _insertDefaultAcademicTerms(mDb);
        ApolloDbAdapter.close();
    }

    @Test
    public void testEntryConstructorAndMethods() throws Exception {
        AcademicTermContract.AcademicTermEntry entry = new AcademicTermContract.AcademicTermEntry(mId, mDescription, mColor);
        assertEquals(mId, entry.getId());
        assertEquals(mDescription, entry.getDescription());
        assertEquals(mColor, entry.getColor());

        long id = 2;
        String description = mContext.getString(R.string.default_academic_term_1);
        String color = mContext.getString(R.string.default_academic_term_color_1);
        assertNotEquals(mId, id);
        assertNotEquals(mDescription, description);
        assertNotEquals(mColor, color);

        entry.setId(id);
        assertEquals(id, entry.getId());
        entry.setDescription(description);
        assertEquals(description, entry.getDescription());
        entry.setColor(color);
        assertEquals(color, entry.getColor());
    }

    @Test
    public void test_getEntryId() throws Exception {
        AcademicTermContract.AcademicTermEntry _entry = AcademicTermContract._getEntry(mDb, mId);
        assertNotNull(_entry);
        assertEquals(mId, _entry.getId());
        assertEquals(mDescription, _entry.getDescription());
        assertEquals(mColor, _entry.getColor());

        AcademicTermContract.AcademicTermEntry entry = AcademicTermContract.getEntry(mId);
        assertNotNull(entry);
        assertEquals(mId, entry.getId());
        assertEquals(mDescription, entry.getDescription());
        assertEquals(mColor, entry.getColor());

        assertTrue(_entry.equals(entry));
    }

    @Test
    public void test_getEntryDescription() throws Exception {
        AcademicTermContract.AcademicTermEntry _entry = AcademicTermContract._getEntry(mDb, mDescription);
        assertNotNull(_entry);
        assertEquals(mId, _entry.getId());
        assertEquals(mDescription, _entry.getDescription());
        assertEquals(mColor, _entry.getColor());

        AcademicTermContract.AcademicTermEntry entry = AcademicTermContract.getEntry(mDescription);
        assertNotNull(entry);
        assertEquals(mId, entry.getId());
        assertEquals(mDescription, entry.getDescription());
        assertEquals(mColor, entry.getColor());

        assertTrue(_entry.equals(entry));
    }

    @Test
    public void test_getEntries() throws Exception {
        ArrayList<AcademicTermContract.AcademicTermEntry> _entries = AcademicTermContract._getEntries(mDb);
        AcademicTermContract.AcademicTermEntry _entry0 = AcademicTermContract._getEntry(mDb, mContext.getString(R.string.default_academic_term_0));
        AcademicTermContract.AcademicTermEntry _entry1 = AcademicTermContract._getEntry(mDb, mContext.getString(R.string.default_academic_term_1));
        AcademicTermContract.AcademicTermEntry _entry2 = AcademicTermContract._getEntry(mDb, mContext.getString(R.string.default_academic_term_2));
        AcademicTermContract.AcademicTermEntry _entry3 = AcademicTermContract._getEntry(mDb, mContext.getString(R.string.default_academic_term_3));
        AcademicTermContract.AcademicTermEntry _entry4 = AcademicTermContract._getEntry(mDb, mContext.getString(R.string.default_academic_term_4));
        AcademicTermContract.AcademicTermEntry _entry5 = AcademicTermContract._getEntry(mDb, mContext.getString(R.string.default_academic_term_5));
        AcademicTermContract.AcademicTermEntry _entry6 = AcademicTermContract._getEntry(mDb, mContext.getString(R.string.default_academic_term_6));
        AcademicTermContract.AcademicTermEntry _entry7 = AcademicTermContract._getEntry(mDb, mContext.getString(R.string.default_academic_term_7));
        AcademicTermContract.AcademicTermEntry _entry8 = AcademicTermContract._getEntry(mDb, mContext.getString(R.string.default_academic_term_8));
        AcademicTermContract.AcademicTermEntry _entry9 = AcademicTermContract._getEntry(mDb, mContext.getString(R.string.default_academic_term_9));
        AcademicTermContract.AcademicTermEntry _entry10 = AcademicTermContract._getEntry(mDb, mContext.getString(R.string.default_academic_term_10));
        AcademicTermContract.AcademicTermEntry _entry11 = AcademicTermContract._getEntry(mDb, mContext.getString(R.string.default_academic_term_11));
        AcademicTermContract.AcademicTermEntry _entry12 = AcademicTermContract._getEntry(mDb, mContext.getString(R.string.default_academic_term_12));

        assertNotNull(_entries);
        assertNotNull(_entry0);
        assertTrue(_entries.contains(_entry0));
        assertNotNull(_entry1);
        assertTrue(_entries.contains(_entry1));
        assertNotNull(_entry2);
        assertTrue(_entries.contains(_entry2));
        assertNotNull(_entry3);
        assertTrue(_entries.contains(_entry3));
        assertNotNull(_entry4);
        assertTrue(_entries.contains(_entry4));
        assertNotNull(_entry5);
        assertTrue(_entries.contains(_entry5));
        assertNotNull(_entry6);
        assertTrue(_entries.contains(_entry6));
        assertNotNull(_entry7);
        assertTrue(_entries.contains(_entry7));
        assertNotNull(_entry8);
        assertTrue(_entries.contains(_entry8));
        assertNotNull(_entry9);
        assertTrue(_entries.contains(_entry9));
        assertNotNull(_entry10);
        assertTrue(_entries.contains(_entry10));
        assertNotNull(_entry11);
        assertTrue(_entries.contains(_entry11));
        assertNotNull(_entry12);
        assertTrue(_entries.contains(_entry12));

        ArrayList<AcademicTermContract.AcademicTermEntry> entries = AcademicTermContract.getEntries();
        assertNotNull(entries);
        assertArrayEquals(_entries.toArray(), entries.toArray());
    }

    @Test
    public void test_delete() throws Exception {
        int rowsDeleted = AcademicTermContract._delete(mDb, mId);
        assertEquals(1, rowsDeleted);
        rowsDeleted = AcademicTermContract._delete(mDb, mId);
        assertEquals(0, rowsDeleted);
        AcademicTermContract.AcademicTermEntry _entry = AcademicTermContract._getEntry(mDb, mId);
        assertNull(_entry);

        long id = 2;
        rowsDeleted = AcademicTermContract.delete(id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = AcademicTermContract.delete(id);
        assertEquals(0, rowsDeleted);
        AcademicTermContract.AcademicTermEntry entry = AcademicTermContract.getEntry(id);
        assertNull(entry);
    }

    @Test
    public void test_update() throws Exception {
        String description = mContext.getString(R.string.default_academic_term_1);
        String color = mContext.getString(R.string.default_academic_term_color_1);
        int rowsUpdated = AcademicTermContract._update(mDb, mId, description, color);
        assertEquals(1, rowsUpdated);

        AcademicTermContract.AcademicTermEntry _entry = AcademicTermContract._getEntry(mDb, mId);
        assertNotNull(_entry);
        assertEquals(mId, _entry.getId());
        assertEquals(description, _entry.getDescription());
        assertEquals(color, _entry.getColor());

        rowsUpdated = AcademicTermContract.update(mId, mDescription, mColor);
        assertEquals(1, rowsUpdated);
        AcademicTermContract.AcademicTermEntry entry = AcademicTermContract.getEntry(mId);
        assertNotNull(entry);
        assertEquals(mId, entry.getId());
        assertEquals(mDescription, entry.getDescription());
        assertEquals(mColor, entry.getColor());
    }

    @Test
    public void test_insert() throws Exception {
        AcademicTermContract.AcademicTermEntry _entry = AcademicTermContract._getEntry(mDb, mId);
        assertNotNull(_entry);
        assertEquals(mId, _entry.getId());
        String description = _entry.getDescription();
        String color = _entry.getColor();

        int rowsDeleted = AcademicTermContract._delete(mDb, mId);
        assertEquals(1, rowsDeleted);
        _entry = AcademicTermContract._getEntry(mDb, mId);
        assertNull(_entry);

        long id = AcademicTermContract._insert(mDb, description, color);
        _entry = AcademicTermContract._getEntry(mDb, id);
        assertNotNull(_entry);
        assertEquals(id, _entry.getId());
        assertEquals(description, _entry.getDescription());
        assertEquals(color, _entry.getColor());

        AcademicTermContract.AcademicTermEntry entry = AcademicTermContract.getEntry(id);
        assertNotNull(entry);
        assertTrue(_entry.equals(entry));

        rowsDeleted = AcademicTermContract.delete(id);
        assertEquals(1, rowsDeleted);
        entry = AcademicTermContract.getEntry(id);
        assertNull(entry);
        id = AcademicTermContract.insert(description, color);
        entry = AcademicTermContract.getEntry(id);
        assertNotNull(entry);
        assertEquals(id, entry.getId());
        assertEquals(description, entry.getDescription());
        assertEquals(color, entry.getColor());
    }

    private void _insertDefaultAcademicTerms(SQLiteDatabase db) {
        AcademicTermContract._insert(db, mContext.getString(R.string.default_academic_term_0), mContext.getString(R.string.default_academic_term_color_0));
        AcademicTermContract._insert(db, mContext.getString(R.string.default_academic_term_1), mContext.getString(R.string.default_academic_term_color_1));
        AcademicTermContract._insert(db, mContext.getString(R.string.default_academic_term_2), mContext.getString(R.string.default_academic_term_color_2));
        AcademicTermContract._insert(db, mContext.getString(R.string.default_academic_term_3), mContext.getString(R.string.default_academic_term_color_3));
        AcademicTermContract._insert(db, mContext.getString(R.string.default_academic_term_4), mContext.getString(R.string.default_academic_term_color_4));
        AcademicTermContract._insert(db, mContext.getString(R.string.default_academic_term_5), mContext.getString(R.string.default_academic_term_color_5));
        AcademicTermContract._insert(db, mContext.getString(R.string.default_academic_term_6), ColorEnum.TRANSPARENT.getValue());
        AcademicTermContract._insert(db, mContext.getString(R.string.default_academic_term_7), ColorEnum.TRANSPARENT.getValue());
        AcademicTermContract._insert(db, mContext.getString(R.string.default_academic_term_8), ColorEnum.TRANSPARENT.getValue());
        AcademicTermContract._insert(db, mContext.getString(R.string.default_academic_term_9), ColorEnum.TRANSPARENT.getValue());
        AcademicTermContract._insert(db, mContext.getString(R.string.default_academic_term_10), ColorEnum.TRANSPARENT.getValue());
        AcademicTermContract._insert(db, mContext.getString(R.string.default_academic_term_11), ColorEnum.TRANSPARENT.getValue());
        AcademicTermContract._insert(db, mContext.getString(R.string.default_academic_term_12), ColorEnum.TRANSPARENT.getValue());
    }
}
