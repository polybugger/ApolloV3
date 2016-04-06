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
public class ClassItemTypeContractTest {

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
        mDescription = mContext.getString(R.string.default_class_item_type_0);
        mColor = mContext.getString(R.string.default_class_item_type_color_0);

        mDb.setForeignKeyConstraintsEnabled(false);
        mDb.execSQL(ClassItemTypeContract.DELETE_ALL_SQL);
        _insertDefaultClassItemTypes();
    }

    @After
    public void tearDown() throws Exception {
        mDb.execSQL(ClassItemTypeContract.DELETE_ALL_SQL);
        _insertDefaultClassItemTypes();
        ApolloDbAdapter.close();
    }

    @Test
    public void testEntryConstructorAndMethods() throws Exception {
        ClassItemTypeContract.ClassItemTypeEntry entry = new ClassItemTypeContract.ClassItemTypeEntry(mId, mDescription, mColor);
        assertEquals(mId, entry.getId());
        assertEquals(mDescription, entry.getDescription());
        assertEquals(mColor, entry.getColor());

        long id = 2;
        String description = mContext.getString(R.string.default_class_item_type_1);
        String color = mContext.getString(R.string.default_class_item_type_color_1);
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
        ClassItemTypeContract.ClassItemTypeEntry _entry = ClassItemTypeContract._getEntry(mDb, mId);
        assertNotNull(_entry);
        assertEquals(mId, _entry.getId());
        assertEquals(mDescription, _entry.getDescription());
        assertEquals(mColor, _entry.getColor());

        ClassItemTypeContract.ClassItemTypeEntry entry = ClassItemTypeContract.getEntry(mId);
        assertNotNull(entry);
        assertEquals(mId, entry.getId());
        assertEquals(mDescription, entry.getDescription());
        assertEquals(mColor, entry.getColor());

        assertTrue(_entry.equals(entry));
    }

    @Test
    public void test_getEntryDescription() throws Exception {
        ClassItemTypeContract.ClassItemTypeEntry _entry = ClassItemTypeContract._getEntry(mDb, mDescription);
        assertNotNull(_entry);
        assertEquals(mId, _entry.getId());
        assertEquals(mDescription, _entry.getDescription());
        assertEquals(mColor, _entry.getColor());

        ClassItemTypeContract.ClassItemTypeEntry entry = ClassItemTypeContract.getEntry(mDescription);
        assertNotNull(entry);
        assertEquals(mId, entry.getId());
        assertEquals(mDescription, entry.getDescription());
        assertEquals(mColor, entry.getColor());

        assertTrue(_entry.equals(entry));
    }

    @Test
    public void test_getEntries() throws Exception {
        ArrayList<ClassItemTypeContract.ClassItemTypeEntry> _entries = ClassItemTypeContract._getEntries(mDb);
        ClassItemTypeContract.ClassItemTypeEntry _entry0 = ClassItemTypeContract._getEntry(mDb, mContext.getString(R.string.default_class_item_type_0));
        ClassItemTypeContract.ClassItemTypeEntry _entry1 = ClassItemTypeContract._getEntry(mDb, mContext.getString(R.string.default_class_item_type_1));
        ClassItemTypeContract.ClassItemTypeEntry _entry2 = ClassItemTypeContract._getEntry(mDb, mContext.getString(R.string.default_class_item_type_2));
        ClassItemTypeContract.ClassItemTypeEntry _entry3 = ClassItemTypeContract._getEntry(mDb, mContext.getString(R.string.default_class_item_type_3));
        ClassItemTypeContract.ClassItemTypeEntry _entry4 = ClassItemTypeContract._getEntry(mDb, mContext.getString(R.string.default_class_item_type_4));
        ClassItemTypeContract.ClassItemTypeEntry _entry5 = ClassItemTypeContract._getEntry(mDb, mContext.getString(R.string.default_class_item_type_5));
        ClassItemTypeContract.ClassItemTypeEntry _entry6 = ClassItemTypeContract._getEntry(mDb, mContext.getString(R.string.default_class_item_type_6));
        ClassItemTypeContract.ClassItemTypeEntry _entry7 = ClassItemTypeContract._getEntry(mDb, mContext.getString(R.string.default_class_item_type_7));
        ClassItemTypeContract.ClassItemTypeEntry _entry8 = ClassItemTypeContract._getEntry(mDb, mContext.getString(R.string.default_class_item_type_8));
        ClassItemTypeContract.ClassItemTypeEntry _entry9 = ClassItemTypeContract._getEntry(mDb, mContext.getString(R.string.default_class_item_type_9));
        ClassItemTypeContract.ClassItemTypeEntry _entry10 = ClassItemTypeContract._getEntry(mDb, mContext.getString(R.string.default_class_item_type_10));

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

        ArrayList<ClassItemTypeContract.ClassItemTypeEntry> entries = ClassItemTypeContract.getEntries();
        assertNotNull(entries);
        assertArrayEquals(_entries.toArray(), entries.toArray());
    }

    @Test
    public void test_delete() throws Exception {
        int rowsDeleted = ClassItemTypeContract._delete(mDb, mId);
        assertEquals(1, rowsDeleted);
        rowsDeleted = ClassItemTypeContract._delete(mDb, mId);
        assertEquals(0, rowsDeleted);
        ClassItemTypeContract.ClassItemTypeEntry _entry = ClassItemTypeContract._getEntry(mDb, mId);
        assertNull(_entry);

        long id = 2;
        rowsDeleted = ClassItemTypeContract.delete(id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = ClassItemTypeContract.delete(id);
        assertEquals(0, rowsDeleted);
        ClassItemTypeContract.ClassItemTypeEntry entry = ClassItemTypeContract.getEntry(id);
        assertNull(entry);
    }

    @Test
    public void test_update() throws Exception {
        String description = mContext.getString(R.string.default_class_item_type_1);
        String color = mContext.getString(R.string.default_class_item_type_color_1);
        int rowsUpdated = ClassItemTypeContract._update(mDb, mId, description, color);
        assertEquals(1, rowsUpdated);

        ClassItemTypeContract.ClassItemTypeEntry _entry = ClassItemTypeContract._getEntry(mDb, mId);
        assertNotNull(_entry);
        assertEquals(mId, _entry.getId());
        assertEquals(description, _entry.getDescription());
        assertEquals(color, _entry.getColor());

        rowsUpdated = ClassItemTypeContract.update(mId, mDescription, mColor);
        assertEquals(1, rowsUpdated);
        ClassItemTypeContract.ClassItemTypeEntry entry = ClassItemTypeContract.getEntry(mId);
        assertNotNull(entry);
        assertEquals(mId, entry.getId());
        assertEquals(mDescription, entry.getDescription());
        assertEquals(mColor, entry.getColor());
    }

    @Test
    public void test_insert() throws Exception {
        ClassItemTypeContract.ClassItemTypeEntry _entry = ClassItemTypeContract._getEntry(mDb, mId);
        assertNotNull(_entry);
        assertEquals(mId, _entry.getId());
        String description = _entry.getDescription();
        String color = _entry.getColor();

        int rowsDeleted = ClassItemTypeContract._delete(mDb, mId);
        assertEquals(1, rowsDeleted);
        _entry = ClassItemTypeContract._getEntry(mDb, mId);
        assertNull(_entry);

        long id = ClassItemTypeContract._insert(mDb, description, color);
        _entry = ClassItemTypeContract._getEntry(mDb, id);
        assertNotNull(_entry);
        assertEquals(id, _entry.getId());
        assertEquals(description, _entry.getDescription());
        assertEquals(color, _entry.getColor());

        ClassItemTypeContract.ClassItemTypeEntry entry = ClassItemTypeContract.getEntry(id);
        assertNotNull(entry);
        assertTrue(_entry.equals(entry));

        rowsDeleted = ClassItemTypeContract.delete(id);
        assertEquals(1, rowsDeleted);
        entry = ClassItemTypeContract.getEntry(id);
        assertNull(entry);
        id = ClassItemTypeContract.insert(description, color);
        entry = ClassItemTypeContract.getEntry(id);
        assertNotNull(entry);
        assertEquals(id, entry.getId());
        assertEquals(description, entry.getDescription());
        assertEquals(color, entry.getColor());
    }

    private void _insertDefaultClassItemTypes() {
        ClassItemTypeContract._insert(mDb, mContext.getString(R.string.default_class_item_type_0), mContext.getString(R.string.default_class_item_type_color_0));
        ClassItemTypeContract._insert(mDb, mContext.getString(R.string.default_class_item_type_1), mContext.getString(R.string.default_class_item_type_color_1));
        ClassItemTypeContract._insert(mDb, mContext.getString(R.string.default_class_item_type_2), mContext.getString(R.string.default_class_item_type_color_2));
        ClassItemTypeContract._insert(mDb, mContext.getString(R.string.default_class_item_type_3), mContext.getString(R.string.default_class_item_type_color_3));
        ClassItemTypeContract._insert(mDb, mContext.getString(R.string.default_class_item_type_4), mContext.getString(R.string.default_class_item_type_color_4));
        ClassItemTypeContract._insert(mDb, mContext.getString(R.string.default_class_item_type_5), mContext.getString(R.string.default_class_item_type_color_5));
        ClassItemTypeContract._insert(mDb, mContext.getString(R.string.default_class_item_type_6), mContext.getString(R.string.default_class_item_type_color_6));
        ClassItemTypeContract._insert(mDb, mContext.getString(R.string.default_class_item_type_7), mContext.getString(R.string.default_class_item_type_color_7));
        ClassItemTypeContract._insert(mDb, mContext.getString(R.string.default_class_item_type_8), mContext.getString(R.string.default_class_item_type_color_8));
        ClassItemTypeContract._insert(mDb, mContext.getString(R.string.default_class_item_type_9), mContext.getString(R.string.default_class_item_type_color_9));
        ClassItemTypeContract._insert(mDb, mContext.getString(R.string.default_class_item_type_10), mContext.getString(R.string.default_class_item_type_color_10));
    }
}
