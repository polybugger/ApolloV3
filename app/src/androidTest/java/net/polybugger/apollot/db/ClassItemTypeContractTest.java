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
public class ClassItemTypeContractTest {

    private Context mContext;
    private long mItemTyp0Id;
    private String mItemTyp0Description;
    private String mItemTyp0Color;
    private long mItemTyp1Id;
    private String mItemTyp1Description;
    private String mItemTyp1Color;

    @Before
    public void setUp() throws Exception {
        mContext = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_");
        ApolloDbAdapter.setAppContext(mContext);
        SQLiteDatabase db = ApolloDbAdapter.open();
        db.setForeignKeyConstraintsEnabled(false);
        db.execSQL(ClassItemTypeContract.DELETE_ALL_SQL);
        ApolloDbAdapter._insertDefaultClassItemTypes(db);
        mItemTyp0Description = mContext.getString(R.string.default_class_item_type_0);
        mItemTyp0Color = mContext.getString(R.string.default_class_item_type_color_0);
        mItemTyp0Id = ClassItemTypeContract.getEntryByDescription(mItemTyp0Description).getId();
        mItemTyp1Description = mContext.getString(R.string.default_class_item_type_1);
        mItemTyp1Color = mContext.getString(R.string.default_class_item_type_color_1);
        mItemTyp1Id = ClassItemTypeContract.getEntryByDescription(mItemTyp1Description).getId();
        ApolloDbAdapter.close();
    }

    @After
    public void tearDown() throws Exception {
        SQLiteDatabase db = ApolloDbAdapter.open();
        db.setForeignKeyConstraintsEnabled(false);
        db.execSQL(ClassItemTypeContract.DELETE_ALL_SQL);
        ApolloDbAdapter._insertDefaultClassItemTypes(db);
        ApolloDbAdapter.close();
    }

    @Test
    public void test_methods() throws Exception {
        ClassItemTypeContract.ClassItemTypeEntry entry = ClassItemTypeContract.getEntry(mItemTyp0Id);
        assertEquals(mItemTyp0Id, entry.getId());
        assertEquals(mItemTyp0Description, entry.getDescription());
        assertEquals(mItemTyp0Color, entry.getColor());

        assertNotEquals(mItemTyp0Id, mItemTyp1Id);
        assertNotEquals(mItemTyp0Description, mItemTyp1Description);
        assertNotEquals(mItemTyp0Color, mItemTyp1Color);

        entry.setId(mItemTyp1Id);
        entry.setDescription(mItemTyp1Description);
        entry.setColor(mItemTyp1Color);
        assertEquals(mItemTyp1Id, entry.getId());
        assertEquals(mItemTyp1Description, entry.getDescription());
        assertEquals(mItemTyp1Color, entry.getColor());
    }

    @Test
    public void test_getEntry() throws Exception {
        ClassItemTypeContract.ClassItemTypeEntry entry = ClassItemTypeContract.getEntry(mItemTyp0Id);
        assertEquals(mItemTyp0Id, entry.getId());
        assertEquals(mItemTyp0Description, entry.getDescription());
        assertEquals(mItemTyp0Color, entry.getColor());

        ClassItemTypeContract.ClassItemTypeEntry entryByDescription = ClassItemTypeContract.getEntryByDescription(mItemTyp0Description);
        assertEquals(mItemTyp0Id, entryByDescription.getId());
        assertEquals(mItemTyp0Description, entryByDescription.getDescription());
        assertEquals(mItemTyp0Color, entryByDescription.getColor());
    }

    @Test
    public void test_getEntries() throws Exception {
        ArrayList<ClassItemTypeContract.ClassItemTypeEntry> entries = ClassItemTypeContract.getEntries();
        ClassItemTypeContract.ClassItemTypeEntry entry0 = ClassItemTypeContract.getEntryByDescription(mContext.getString(R.string.default_class_item_type_0));
        ClassItemTypeContract.ClassItemTypeEntry entry0a = ClassItemTypeContract.getEntryByDescription(mContext.getString(R.string.default_class_item_type_0a));
        ClassItemTypeContract.ClassItemTypeEntry entry1 = ClassItemTypeContract.getEntryByDescription(mContext.getString(R.string.default_class_item_type_1));
        ClassItemTypeContract.ClassItemTypeEntry entry1a = ClassItemTypeContract.getEntryByDescription(mContext.getString(R.string.default_class_item_type_1a));
        ClassItemTypeContract.ClassItemTypeEntry entry2 = ClassItemTypeContract.getEntryByDescription(mContext.getString(R.string.default_class_item_type_2));
        ClassItemTypeContract.ClassItemTypeEntry entry3 = ClassItemTypeContract.getEntryByDescription(mContext.getString(R.string.default_class_item_type_3));
        ClassItemTypeContract.ClassItemTypeEntry entry4 = ClassItemTypeContract.getEntryByDescription(mContext.getString(R.string.default_class_item_type_4));
        ClassItemTypeContract.ClassItemTypeEntry entry5 = ClassItemTypeContract.getEntryByDescription(mContext.getString(R.string.default_class_item_type_5));
        ClassItemTypeContract.ClassItemTypeEntry entry6 = ClassItemTypeContract.getEntryByDescription(mContext.getString(R.string.default_class_item_type_6));
        ClassItemTypeContract.ClassItemTypeEntry entry7 = ClassItemTypeContract.getEntryByDescription(mContext.getString(R.string.default_class_item_type_7));
        ClassItemTypeContract.ClassItemTypeEntry entry8 = ClassItemTypeContract.getEntryByDescription(mContext.getString(R.string.default_class_item_type_8));
        ClassItemTypeContract.ClassItemTypeEntry entry9 = ClassItemTypeContract.getEntryByDescription(mContext.getString(R.string.default_class_item_type_9));
        ClassItemTypeContract.ClassItemTypeEntry entry10 = ClassItemTypeContract.getEntryByDescription(mContext.getString(R.string.default_class_item_type_10));

        assertNotNull(entries);
        assertNotNull(entry0);
        assertTrue(entries.contains(entry0));
        assertNotNull(entry0a);
        assertTrue(entries.contains(entry0a));
        assertNotNull(entry1);
        assertTrue(entries.contains(entry1));
        assertNotNull(entry1a);
        assertTrue(entries.contains(entry1a));
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
    }

    @Test
    public void test_delete() throws Exception {
        int rowsDeleted = ClassItemTypeContract.delete(mItemTyp0Id);
        assertEquals(1, rowsDeleted);
        rowsDeleted = ClassItemTypeContract.delete(mItemTyp0Id);
        assertEquals(0, rowsDeleted);
        ClassItemTypeContract.ClassItemTypeEntry entry = ClassItemTypeContract.getEntry(mItemTyp0Id);
        assertNull(entry);
    }

    @Test
    public void test_update() throws Exception {
        int rowsUpdated = ClassItemTypeContract.update(mItemTyp0Id, mItemTyp1Description, mItemTyp1Color);
        assertEquals(1, rowsUpdated);

        ClassItemTypeContract.ClassItemTypeEntry entry = ClassItemTypeContract.getEntry(mItemTyp0Id);
        assertNotNull(entry);
        assertEquals(mItemTyp0Id, entry.getId());
        assertEquals(mItemTyp1Description, entry.getDescription());
        assertEquals(mItemTyp1Color, entry.getColor());
    }
}
