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
public class ClassNoteContractTest {

    private SQLiteDatabase mDb;
    private Context mContext;
    private ClassContract.ClassEntry mClass0;
    private long mClassNote0Id;
    private String mNote0;
    private Date mDateCreated0;
    private long mClassNote1Id;
    private String mNote1;
    private Date mDateCreated1;
    private ClassContract.ClassEntry mClass1;

    @Before
    public void setUp() throws Exception {
        mContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_");
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE, mContext.getResources().getConfiguration().locale);
        ApolloDbAdapter.setAppContext(mContext);
        mDb = ApolloDbAdapter.open();
        mDb.setForeignKeyConstraintsEnabled(false);
        mDb.execSQL(ClassNoteContract.DELETE_ALL_SQL);
        mClass0 = ClassContract._getEntryByCode(mDb, mContext.getString(R.string.default_class_0_code));
        ApolloDbAdapter._insertDummyClass0Notes(mDb, mClass0.getId());
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
        mClass1 = ClassContract._getEntryByCode(mDb, mContext.getString(R.string.default_class_1_code));
        ArrayList<ClassNoteContract.ClassNoteEntry> entries = ClassNoteContract._getEntriesByClassId(mDb, mClass0.getId());
        mClassNote0Id = entries.get(0).getId();
        mClassNote1Id = entries.get(1).getId();
    }

    @After
    public void tearDown() throws Exception {
        mDb.execSQL(ClassNoteContract.DELETE_ALL_SQL);
        ApolloDbAdapter._insertDummyClass0Notes(mDb, mClass0.getId());
        ApolloDbAdapter.close();
    }

    @Test
    public void test_methods() throws Exception {
        ClassNoteContract.ClassNoteEntry entry = ClassNoteContract._getEntry(mDb, mClassNote0Id);
        assertEquals(mClassNote0Id, entry.getId());
        assertEquals(mClass0.getId(), entry.getClassId());
        assertEquals(mNote0, entry.getNote());
        assertEquals(mDateCreated0, entry.getDateCreated());

        assertNotEquals(mClassNote0Id, mClassNote1Id);
        assertNotEquals(mClass0.getId(), mClass1.getId());
        assertNotEquals(mNote0, mNote1);
        assertNotEquals(mDateCreated0, mDateCreated1);

        entry.setId(mClassNote1Id);
        entry.setClassId(mClass1.getId());
        entry.setNote(mNote1);
        entry.setDateCreated(mDateCreated1);
        assertEquals(mClassNote1Id, entry.getId());
        assertEquals(mClass1.getId(), entry.getClassId());
        assertEquals(mNote1, entry.getNote());
        assertEquals(mDateCreated1, entry.getDateCreated());
    }

    @Test
    public void test_getEntry() throws Exception {
        ClassNoteContract.ClassNoteEntry entry = ClassNoteContract._getEntry(mDb, mClassNote0Id);
        assertNotNull(entry);
        assertEquals(mClassNote0Id, entry.getId());
        assertEquals(mClass0.getId(), entry.getClassId());
        assertEquals(mNote0, entry.getNote());
        assertEquals(mDateCreated0, entry.getDateCreated());

        ClassNoteContract.ClassNoteEntry entryByClassId = ClassNoteContract._getEntriesByClassId(mDb, mClass0.getId()).get(0);
        assertNotNull(entry);
        assertEquals(mClassNote0Id, entryByClassId.getId());
        assertEquals(mClass0.getId(), entryByClassId.getClassId());
        assertEquals(mNote0, entryByClassId.getNote());
        assertEquals(mDateCreated0, entryByClassId.getDateCreated());
    }

    @Test
    public void test_getEntries() throws Exception {
        ArrayList<ClassNoteContract.ClassNoteEntry> entries = ClassNoteContract._getEntries(mDb);
        ClassNoteContract.ClassNoteEntry entry0 = ClassNoteContract._getEntry(mDb, mClassNote0Id);
        ClassNoteContract.ClassNoteEntry entry1 = ClassNoteContract._getEntry(mDb, mClassNote1Id);

        assertNotNull(entries);
        assertNotNull(entry0);
        assertTrue(entries.contains(entry0));
        assertNotNull(entry1);
        assertTrue(entries.contains(entry1));
    }

    @Test
    public void test_delete() throws Exception {
        int rowsDeleted = ClassNoteContract._delete(mDb, mClassNote0Id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = ClassNoteContract._delete(mDb, mClassNote0Id);
        assertEquals(0, rowsDeleted);
        ClassNoteContract.ClassNoteEntry entry = ClassNoteContract._getEntry(mDb, mClassNote0Id);
        assertNull(entry);
    }

    @Test
    public void test_update() throws Exception {
        int rowsUpdated = ClassNoteContract._update(mDb, mClassNote0Id, mClass1.getId(), mNote1, mDateCreated1);
        assertEquals(1, rowsUpdated);

        ClassNoteContract.ClassNoteEntry entry = ClassNoteContract._getEntry(mDb, mClassNote0Id);
        assertNotNull(entry);
        assertEquals(mClassNote0Id, entry.getId());
        assertEquals(mClass1.getId(), entry.getClassId());
        assertEquals(mNote1, entry.getNote());
        assertEquals(mDateCreated1, entry.getDateCreated());
    }
}
