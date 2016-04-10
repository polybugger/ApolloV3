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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class ClassNoteContractTest {

    private SQLiteDatabase mDb;
    private Context mContext;
    private ClassContract.ClassEntry mClass0;
    private ClassContract.ClassEntry mClass1;
    private long mClassNote0Id;
    private String mNote0;
    private Date mDateCreated0;
    private long mClassNote1Id;
    private String mNote1;
    private Date mDateCreated1;

    @Before
    public void setUp() throws Exception {
        mContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_");
        ApolloDbAdapter.setAppContext(mContext);
        mDb = ApolloDbAdapter.open();
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE, mContext.getResources().getConfiguration().locale);
        mNote0 = mContext.getString(R.string.default_class_0_class_note_0_note);
        try {
            mDateCreated0 = sdf.parse(mContext.getString(R.string.default_class_0_class_note_0_date_created));
        }
        catch(Exception e) {
            mDateCreated0 = null;
        }
        mNote1 = mContext.getString(R.string.default_class_0_class_note_1_note);
        try {
            mDateCreated1 = sdf.parse(mContext.getString(R.string.default_class_0_class_note_1_date_created));
        }
        catch(Exception e) {
            mDateCreated1 = null;
        }

        mDb.setForeignKeyConstraintsEnabled(false);
        mDb.execSQL(ClassNoteContract.DELETE_ALL_SQL);
        _insertDummyClassNotes();
    }

    @After
    public void tearDown() throws Exception {
        mDb.execSQL(ClassNoteContract.DELETE_ALL_SQL);
        _insertDummyClassNotes();
        ApolloDbAdapter.close();
    }

    @Test
    public void testEntryConstructorAndMethods() throws Exception {
        ClassNoteContract.ClassNoteEntry entry = new ClassNoteContract.ClassNoteEntry(mClassNote0Id, mClass0.getId(), mNote0, mDateCreated0);
        assertEquals(mClassNote0Id, entry.getId());
        assertEquals(mClass0.getId(), entry.getClassId());
        assertEquals(mNote0, entry.getNote());
        assertEquals(mDateCreated0, entry.getDateCreated());

        assertNotEquals(mClassNote0Id, mClassNote1Id);
        assertNotEquals(mClass0.getId(), mClass1.getId());
        assertNotEquals(mNote0, mNote1);
        assertNotEquals(mDateCreated0, mDateCreated1);

        entry.setId(mClassNote1Id);
        assertEquals(mClassNote1Id, entry.getId());
        entry.setClassId(mClass1.getId());
        assertEquals(mClass1.getId(), entry.getClassId());
        entry.setNote(mNote1);
        assertEquals(mNote1, entry.getNote());
        entry.setDateCreated(mDateCreated1);
        assertEquals(mDateCreated1, entry.getDateCreated());
    }

    @Test
    public void test_getEntryId() throws Exception {
        ClassNoteContract.ClassNoteEntry _entry = ClassNoteContract._getEntry(mDb, mClassNote0Id);
        assertNotNull(_entry);
        assertEquals(mClassNote0Id, _entry.getId());
        assertEquals(mClass0.getId(), _entry.getClassId());
        assertEquals(mNote0, _entry.getNote());
        assertEquals(mDateCreated0, _entry.getDateCreated());

        ClassNoteContract.ClassNoteEntry entry = ClassNoteContract.getEntry(mClassNote0Id);
        assertNotNull(entry);
        assertEquals(mClassNote0Id, entry.getId());
        assertEquals(mClass0.getId(), entry.getClassId());
        assertEquals(mNote0, entry.getNote());
        assertEquals(mDateCreated0, entry.getDateCreated());

        assertTrue(_entry.equals(entry));
    }

    @Test
    public void test_getEntries() throws Exception {
        ArrayList<ClassNoteContract.ClassNoteEntry> _entries = ClassNoteContract._getEntries(mDb);
        ClassNoteContract.ClassNoteEntry _entry0 = ClassNoteContract._getEntry(mDb, mClassNote0Id);
        ClassNoteContract.ClassNoteEntry _entry1 = ClassNoteContract._getEntry(mDb, mClassNote1Id);

        assertNotNull(_entries);
        assertNotNull(_entry0);
        assertTrue(_entries.contains(_entry0));
        assertNotNull(_entry1);
        assertTrue(_entries.contains(_entry1));

        ArrayList<ClassNoteContract.ClassNoteEntry> entries = ClassNoteContract.getEntries();
        assertNotNull(entries);
        assertArrayEquals(_entries.toArray(), entries.toArray());
    }

    @Test
    public void test_delete() throws Exception {
        int rowsDeleted = ClassNoteContract._delete(mDb, mClassNote0Id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = ClassNoteContract._delete(mDb, mClassNote0Id);
        assertEquals(0, rowsDeleted);
        ClassNoteContract.ClassNoteEntry _entry = ClassNoteContract._getEntry(mDb, mClassNote0Id);
        assertNull(_entry);

        rowsDeleted = ClassNoteContract.delete(mClassNote1Id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = ClassNoteContract.delete(mClassNote1Id);
        assertEquals(0, rowsDeleted);
        ClassNoteContract.ClassNoteEntry entry = ClassNoteContract.getEntry(mClassNote1Id);
        assertNull(entry);
    }

    @Test
    public void test_update() throws Exception {
        ClassNoteContract.ClassNoteEntry _entry0 = ClassNoteContract._getEntry(mDb, mClassNote0Id);
        assertNotNull(_entry0);
        assertEquals(mClassNote0Id, _entry0.getId());
        assertNotEquals(mClass1.getId(), _entry0.getClassId());
        assertNotEquals(mNote1, _entry0.getNote());
        assertNotEquals(mDateCreated1, _entry0.getDateCreated());

        int rowsUpdated = ClassNoteContract._update(mDb, mClassNote0Id, mClass1.getId(), mNote1, mDateCreated1);
        assertEquals(1, rowsUpdated);
        ClassNoteContract.ClassNoteEntry _entry0Updated = ClassNoteContract._getEntry(mDb, mClassNote0Id);
        assertNotNull(_entry0Updated);
        assertEquals(mClassNote0Id, _entry0Updated.getId());
        assertEquals(mClass1.getId(), _entry0Updated.getClassId());
        assertEquals(mNote1, _entry0Updated.getNote());
        assertEquals(mDateCreated1, _entry0Updated.getDateCreated());
    }

    private void _insertDummyClassNotes() {
        mClass0 = ClassContract._getEntryByCode(mDb, mContext.getString(R.string.default_class_0_code));
        mClass1 = ClassContract._getEntryByCode(mDb, mContext.getString(R.string.default_class_1_code));
        mClassNote0Id = ClassNoteContract._insert(mDb, mClass0.getId(), mNote0, mDateCreated0);
        mClassNote1Id = ClassNoteContract._insert(mDb, mClass0.getId(), mNote1, mDateCreated1);
    }
}
