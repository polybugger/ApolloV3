package net.polybugger.apollot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassItemContract;
import net.polybugger.apollot.db.ClassItemNoteContract;
import net.polybugger.apollot.db.ClassItemRecordContract;
import net.polybugger.apollot.db.ClassNoteContract;

public class ClassItemActivity extends AppCompatActivity implements ClassItemActivityFragment.Listener,
        ClassItemInsertUpdateDialogFragment.Listener,
        DatePickerDialogFragment.Listener,
        ClassItemRecordInsertUpdateDialogFragment.Listener,
        ClassNoteInsertUpdateDialogFragment.Listener,
        ClassNoteDeleteDialogFragment.Listener,
        ClassItemDeleteDialogFragment.Listener {

    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";
    public static final String CLASS_ITEM_ARG = "net.polybugger.apollot.class_item_arg";

    private static final int INFO_TAB = 0;
    private static final int RECORDS_TAB = 1;

    public static boolean REQUERY = false;

    private ClassContract.ClassEntry mClass;
    private ClassItemContract.ClassItemEntry mClassItem;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getIntent().getExtras();
        if(args == null || !args.containsKey(CLASS_ARG) || !args.containsKey(CLASS_ITEM_ARG)) {
            super.onBackPressed();
            return;
        }

        ApolloDbAdapter.setAppContext(getApplicationContext());

        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentByTag(ClassItemActivityFragment.TAG) == null)
            fm.beginTransaction().add(ClassItemActivityFragment.newInstance(), ClassItemActivityFragment.TAG).commit();

        mClass = (ClassContract.ClassEntry) args.getSerializable(CLASS_ARG);
        mClassItem = (ClassItemContract.ClassItemEntry) args.getSerializable(CLASS_ITEM_ARG);

        setContentView(R.layout.activity_class_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(mClassItem.getDescription());

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                ClassNoteInsertUpdateDialogFragment df = (ClassNoteInsertUpdateDialogFragment) fm.findFragmentByTag(ClassNoteInsertUpdateDialogFragment.TAG);
                if(df == null) {
                    df = ClassNoteInsertUpdateDialogFragment.newInstance(null, getString(R.string.new_class_item_note), getString(R.string.add), getFragmentTag(INFO_TAB), null, false);
                    df.show(fm, ClassNoteInsertUpdateDialogFragment.TAG);
                }
            }
        });

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(final int position) {
                CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) mFab.getLayoutParams();
                switch(position) {
                    case INFO_TAB:
                        p.setBehavior(new FABScrollBehavior(ClassItemActivity.this, null));
                        mFab.setLayoutParams(p);
                        mFab.setVisibility(View.VISIBLE);
                        mFab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FragmentManager fm = getSupportFragmentManager();
                                ClassNoteInsertUpdateDialogFragment df = (ClassNoteInsertUpdateDialogFragment) fm.findFragmentByTag(ClassNoteInsertUpdateDialogFragment.TAG);
                                if(df == null) {
                                    df = ClassNoteInsertUpdateDialogFragment.newInstance(null, getString(R.string.new_class_item_note), getString(R.string.add), getFragmentTag(INFO_TAB), null, false);
                                    df.show(fm, ClassNoteInsertUpdateDialogFragment.TAG);
                                }
                            }
                        });
                        break;
                    case RECORDS_TAB:
                        p.setBehavior(null);
                        mFab.setLayoutParams(p);
                        mFab.setVisibility(View.GONE);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_class_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_delete:
                FragmentManager fm = getSupportFragmentManager();
                ClassItemDeleteDialogFragment df = (ClassItemDeleteDialogFragment) fm.findFragmentByTag(ClassItemDeleteDialogFragment.TAG);
                if(df == null) {
                    df = ClassItemDeleteDialogFragment.newInstance(mClassItem);
                    df.show(fm, ClassItemDeleteDialogFragment.TAG);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        ClassItemsFragment.REQUERY_CLASS_ITEM = true;
        ClassItemsFragment.CLASS_ITEM = mClassItem;
        TodayItemsFragment.REQUERY = true;
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(REQUERY) {
            ClassItemActivityFragment f = (ClassItemActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassItemActivityFragment.TAG);
            if(f != null)
                f.getClassItem(mClassItem, getFragmentTag(INFO_TAB));
            REQUERY = false;
        }
    }

    public ClassContract.ClassEntry getClassEntry() {
        return mClass;
    }

    public ClassItemContract.ClassItemEntry getClassItemEntry() {
        return mClassItem;
    }

    @Override
    public void onConfirmInsertUpdateClassItem(ClassItemContract.ClassItemEntry entry, String fragmentTag) {
        if(entry.getId() != -1) {
            ClassItemActivityFragment rf = (ClassItemActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassItemActivityFragment.TAG);
            if(rf != null)
                rf.updateClassItem(entry, fragmentTag);
        }
    }

    @Override
    public void onUpdateClassItem(ClassItemContract.ClassItemEntry classItem, int rowsUpdated, String fragmentTag) {
        if(rowsUpdated > 0) {
            mClassItem = classItem;
            setTitle(mClassItem.getDescription());
            FragmentManager fm = getSupportFragmentManager();
            ClassItemInfoFragment f1 = (ClassItemInfoFragment) fm.findFragmentByTag(fragmentTag);
            if(f1 != null)
                f1.updateClassItem(classItem, rowsUpdated, fragmentTag);
            ClassItemRecordsFragment f2 = (ClassItemRecordsFragment) fm.findFragmentByTag(getFragmentTag(RECORDS_TAB));
            if(f2 != null)
                f2.updateClassItem(classItem);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.class_item_updated), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    @Override
    public void onGetClassItem(ClassItemContract.ClassItemEntry classItem, String fragmentTag) {
        mClassItem = classItem;
        FragmentManager fm = getSupportFragmentManager();
        ClassItemInfoFragment f1 = (ClassItemInfoFragment) fm.findFragmentByTag(fragmentTag);
        if(f1 != null)
            f1.updateClassItem(classItem, 1, fragmentTag);
        ClassItemRecordsFragment f2 = (ClassItemRecordsFragment) fm.findFragmentByTag(getFragmentTag(RECORDS_TAB));
        if(f2 != null)
            f2.updateClassItem(classItem);
    }

    @Override
    public void onGetClassItemRecords(ArrayList<ClassItemRecordsFragment.ClassItemRecordSummary> arrayList, String fragmentTag) {
        ClassItemRecordsFragment f = (ClassItemRecordsFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if(f != null)
            f.onGetClassItemRecords(arrayList, fragmentTag);
    }

    @Override
    public void onInsertClassItemRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, long id, String fragmentTag) {
        if(classItemRecord.getId() == -1) {
            ClassItemRecordsFragment f = (ClassItemRecordsFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
            if(f != null)
                f.onInsertClassItemRecord(classItemRecord, classItem, id, fragmentTag);
        }
    }

    @Override
    public void onUpdateClassItemRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, int rowsUpdated, String fragmentTag) {
        if(rowsUpdated > 0) {
            ClassItemRecordsFragment f = (ClassItemRecordsFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
            if(f != null)
                f.onUpdateClassItemRecord(classItemRecord, classItem, rowsUpdated, fragmentTag);
        }
    }

    @Override
    public void onGetClassItemSummaryInfo(ClassItemInfoFragment.ClassItemSummaryInfo classItemSummaryInfo, String fragmentTag) {
        ClassItemInfoFragment f1 = (ClassItemInfoFragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(INFO_TAB));
        if(f1 != null)
            f1.updateClassItemSummaryInfo(classItemSummaryInfo, getFragmentTag(INFO_TAB));
    }

    @Override
    public void onGetClassItemNotes(ArrayList<ClassItemNoteContract.ClassItemNoteEntry> classItemNotes, String fragmentTag) {
        ClassItemInfoFragment f1 = (ClassItemInfoFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if(f1 != null)
            f1.populateClassItemNotes(classItemNotes, fragmentTag);

    }

    @Override
    public void onInsertClassItemNote(ClassItemNoteContract.ClassItemNoteEntry itemNote, long id, String fragmentTag) {
        if(itemNote.getId() == -1) {
            ClassItemInfoFragment f1 = (ClassItemInfoFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
            if(f1 != null)
                f1.insertClassItemNote(itemNote, id, fragmentTag);
        }

    }

    @Override
    public void onUpdateClassItemNote(ClassItemNoteContract.ClassItemNoteEntry itemNote, int rowsUpdated, String fragmentTag) {
        if(rowsUpdated > 0) {
            ClassItemInfoFragment f1 = (ClassItemInfoFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
            if(f1 != null)
                f1.updateClassItemNote(itemNote, rowsUpdated, fragmentTag);
        }
    }

    @Override
    public void onDeleteClassItemNote(ClassItemNoteContract.ClassItemNoteEntry itemNote, int rowsDeleted, String fragmentTag) {
        if(rowsDeleted > 0) {
            ClassItemInfoFragment f1 = (ClassItemInfoFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
            if(f1 != null)
                f1.deleteClassItemNote(itemNote, rowsDeleted, fragmentTag);
        }
    }

    @Override
    public void onDeleteClassItem(ClassItemContract.ClassItemEntry entry, int rowsDeleted) {
        if(rowsDeleted == 1) {
            ClassItemsFragment.DELETE_CLASS_ITEM = true;
            ClassItemsFragment.CLASS_ITEM = entry;
            onBackPressed();
        }
    }

    @Override
    public void onSetButtonDate(Date date, String dialogFragmentTag, int buttonId) {
        Fragment f = getSupportFragmentManager().findFragmentByTag(dialogFragmentTag);
        if(f != null) {
            if(f instanceof ClassItemInsertUpdateDialogFragment)
                ((ClassItemInsertUpdateDialogFragment) f).setButtonDate(date, buttonId);
            if(f instanceof ClassItemRecordInsertUpdateDialogFragment)
                ((ClassItemRecordInsertUpdateDialogFragment) f).setButtonDate(date, buttonId);
            if(f instanceof ClassNoteInsertUpdateDialogFragment)
                ((ClassNoteInsertUpdateDialogFragment) f).setButtonDate(date, buttonId);
        }
    }

    @Override
    public void onConfirmInsertUpdateClassItemRecord(ClassItemRecordContract.ClassItemRecordEntry entry, ClassItemContract.ClassItemEntry classItem, String fragmentTag) {
        ClassItemActivityFragment f = (ClassItemActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassItemActivityFragment.TAG);
        if(f != null) {
            if(entry.getId() == -1)
                f.insertClassItemRecord(entry, classItem, fragmentTag);
            else
                f.updateClassItemRecord(entry, classItem, fragmentTag);
        }
    }

    @Override
    public void onConfirmInsertUpdateClassNote(ClassNoteContract.ClassNoteEntry entry, String fragmentTag, ClassItemNoteContract.ClassItemNoteEntry itemNote, boolean isClassNote) {
        ClassItemActivityFragment rf = (ClassItemActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassItemActivityFragment.TAG);
        if(rf != null) {
            itemNote.setClassId(mClassItem.getClassId()); // capture class id here
            itemNote.setClassItemId(mClassItem.getId()); // capture class item id here
            if(itemNote.getId() == -1)
                rf.insertClassItemNote(itemNote, fragmentTag);
            else
                rf.updateClassItemNote(itemNote, fragmentTag);
        }
    }

    @Override
    public void onConfirmDeleteClassNote(ClassNoteContract.ClassNoteEntry entry, String fragmentTag, ClassItemNoteContract.ClassItemNoteEntry itemNote, boolean isClassNote) {
        ClassItemActivityFragment rf = (ClassItemActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassItemActivityFragment.TAG);
        if(rf != null)
            rf.deleteClassItemNote(itemNote, fragmentTag);
    }

    @Override
    public void onConfirmDeleteClassItem(ClassItemContract.ClassItemEntry entry) {
        ClassItemActivityFragment rf = (ClassItemActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassItemActivityFragment.TAG);
        if(rf != null)
            rf.deleteClassItem(entry);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            switch(position) {
                case INFO_TAB:
                    return ClassItemInfoFragment.newInstance(mClass, mClassItem);
                case RECORDS_TAB:
                    return ClassItemRecordsFragment.newInstance(mClass, mClassItem);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case INFO_TAB:
                    return getString(R.string.activity_info_tab);
                case RECORDS_TAB:
                    return getString(R.string.records_tab);
            }
            return null;
        }
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + R.id.view_pager + ":" + position;
    }
}
