package net.polybugger.apollot;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.MediumTest;

import net.polybugger.apollot.db.ApolloDbAdapter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@MediumTest
public class ApolloDbAdapterTest {

    private SQLiteDatabase db;

    @Before
    public void setUp() throws Exception {
        RenamingDelegatingContext context = new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_");
        ApolloDbAdapter.setAppContext(context);
        db = ApolloDbAdapter.open();
    }

    @After
    public void tearDown() throws Exception {
        ApolloDbAdapter.close();
    }

    @Test
    public void testForeignKeyConstraintsEnabled() throws Exception {
        final String FOREIGN_KEYS_CONSTRAINTS_SQL = "PRAGMA foreign_keys";
        long foreignKeyConstraintsResult = 0;
        Cursor cursor = db.rawQuery(FOREIGN_KEYS_CONSTRAINTS_SQL, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast())
            foreignKeyConstraintsResult = cursor.isNull(0) ? 0 : cursor.getLong(0);
        cursor.close();
        assertTrue(foreignKeyConstraintsResult == 1);
    }

    @Test
    public void testOpenClose() throws Exception {
        ApolloDbAdapter.close();
        assertFalse(db.isOpen());

        db = ApolloDbAdapter.open();
        assertTrue(db.isOpen());

        db = ApolloDbAdapter.open();
        ApolloDbAdapter.close();
        assertTrue(db.isOpen());

        ApolloDbAdapter.close();
        assertFalse(db.isOpen());
    }
}
