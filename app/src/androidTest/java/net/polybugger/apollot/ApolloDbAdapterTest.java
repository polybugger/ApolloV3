package net.polybugger.apollot;

import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import net.polybugger.apollot.db.ApolloDbAdapter;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class ApolloDbAdapterTest extends AndroidTestCase {

    private SQLiteDatabase db;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        ApolloDbAdapter.setAppContext(context);
        db = ApolloDbAdapter.open();
    }

    @Override
    public void tearDown() throws Exception {
        ApolloDbAdapter.close();
        super.tearDown();
    }

    @Test
    public void sample() throws Exception {
        assertEquals(true, true);
    }
}
