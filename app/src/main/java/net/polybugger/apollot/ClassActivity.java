package net.polybugger.apollot;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassGradeBreakdownContract;
import net.polybugger.apollot.db.ClassItemContract;
import net.polybugger.apollot.db.ClassNoteContract;
import net.polybugger.apollot.db.ClassScheduleContract;

public class ClassActivity extends AppCompatActivity implements ClassActivityFragment.Listener,
        UnlockPasswordDialogFragment.Listener,
        ClassInsertUpdateDialogFragment.Listener,
        ClassScheduleDeleteDialogFragment.Listener,
        ClassScheduleInsertUpdateDialogFragment.Listener,
        TimePickerDialogFragment.Listener,
        DaysPickerDialogFragment.Listener,
        ClassGradeBreakdownDeleteDialogFragment.Listener,
        ClassGradeBreakdownInsertUpdateDialogFragment.Listener,
        ClassNoteInsertUpdateDialogFragment.Listener,
        DatePickerDialogFragment.Listener,
        ClassNoteDeleteDialogFragment.Listener,
        ClassItemInsertUpdateDialogFragment.Listener {

    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";

    public static boolean REQUERY_CLASS = false;

    private static final int INFO_TAB = 0;
    private static final int ITEMS_TAB = 1;
    private static final int STUDENTS_TAB = 2;

    private ClassContract.ClassEntry mClass;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton mFab;
    private MenuItem mLockMenuItem;
    private MenuItem mUnlockMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getIntent().getExtras();
        if(args == null || !args.containsKey(CLASS_ARG)) {
            super.onBackPressed();
            return;
        }

        ApolloDbAdapter.setAppContext(getApplicationContext());

        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentByTag(ClassActivityFragment.TAG) == null)
            fm.beginTransaction().add(ClassActivityFragment.newInstance(), ClassActivityFragment.TAG).commit();

        mClass = (ClassContract.ClassEntry) args.getSerializable(CLASS_ARG);

        setContentView(R.layout.activity_class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(mClass.getTitle());

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FloatingActionBarMenuDialogFragment df = (FloatingActionBarMenuDialogFragment) fm.findFragmentByTag(FloatingActionBarMenuDialogFragment.TAG);
                if(df == null) {
                    df = FloatingActionBarMenuDialogFragment.newInstance(getFragmentTag(INFO_TAB), FloatingActionBarMenuDialogFragment.FABMode.CLASS_INFO_FRAGMENT);
                    df.show(fm, FloatingActionBarMenuDialogFragment.TAG);
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
                switch(position) {
                    case INFO_TAB:
                        mFab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FragmentManager fm = getSupportFragmentManager();
                                FloatingActionBarMenuDialogFragment df = (FloatingActionBarMenuDialogFragment) fm.findFragmentByTag(FloatingActionBarMenuDialogFragment.TAG);
                                if(df == null) {
                                    df = FloatingActionBarMenuDialogFragment.newInstance(getFragmentTag(position), FloatingActionBarMenuDialogFragment.FABMode.CLASS_INFO_FRAGMENT);
                                    df.show(fm, FloatingActionBarMenuDialogFragment.TAG);
                                }
                            }
                        });
                        break;
                    case ITEMS_TAB:
                        mFab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FragmentManager fm = getSupportFragmentManager();
                                ClassItemInsertUpdateDialogFragment df = (ClassItemInsertUpdateDialogFragment) fm.findFragmentByTag(ClassItemInsertUpdateDialogFragment.TAG);
                                if(df == null) {
                                    df = ClassItemInsertUpdateDialogFragment.newInstance(null, getString(R.string.new_class_item), getString(R.string.add), getFragmentTag(ITEMS_TAB));
                                    df.show(fm, ClassItemInsertUpdateDialogFragment.TAG);
                                }
                            }
                        });
                        break;
                    case STUDENTS_TAB:
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
        getMenuInflater().inflate(R.menu.activity_class, menu);
        mUnlockMenuItem = menu.findItem(R.id.action_unlock);
        mLockMenuItem = menu.findItem(R.id.action_lock);
        if(mClass.isLocked()) {
            mUnlockMenuItem.setVisible(true);
            mLockMenuItem.setVisible(false);
        }
        else {
            mUnlockMenuItem.setVisible(false);
            mLockMenuItem.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        UnlockPasswordDialogFragment df;

        int id = item.getItemId();

        switch(id) {
            case R.id.action_lock:
                if(!mClass.isLocked()) {
                    df = (UnlockPasswordDialogFragment) fm.findFragmentByTag(UnlockPasswordDialogFragment.TAG);
                    if(df == null) {
                        df = UnlockPasswordDialogFragment.newInstance(mClass, UnlockPasswordDialogFragment.Option.APPLY_LOCK);
                        df.show(fm, UnlockPasswordDialogFragment.TAG);
                    }
                }
                return true;
            case R.id.action_unlock:
                if(mClass.isLocked()) {
                    df = (UnlockPasswordDialogFragment) fm.findFragmentByTag(UnlockPasswordDialogFragment.TAG);
                    if(df == null) {
                        df = UnlockPasswordDialogFragment.newInstance(mClass, UnlockPasswordDialogFragment.Option.REMOVE_LOCK);
                        df.show(fm, UnlockPasswordDialogFragment.TAG);
                    }
                }
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        ClassesFragment.REQUERY_CLASS = true;
        ClassesFragment.CLASS = mClass;
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(REQUERY_CLASS) {
            ClassActivityFragment f = (ClassActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassActivityFragment.TAG);
            if(f != null)
                f.requeryClass(mClass);
            REQUERY_CLASS = false;
        }
        else {

        }
    }

    @Override
    public void onUnlockPassword(ClassContract.ClassEntry _class) {
        // not used
    }

    @Override
    public void onUnlockClass(ClassContract.ClassEntry _class, boolean passwordMatched) {
        if(passwordMatched) {
            mClass = _class;
            if(mClass.isLocked()) {
                mUnlockMenuItem.setVisible(true);
                mLockMenuItem.setVisible(false);
            }
            else {
                mUnlockMenuItem.setVisible(false);
                mLockMenuItem.setVisible(true);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.class_password_removed), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.incorrect_password), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    @Override
    public void onLockClass(ClassContract.ClassEntry _class) {
        mClass = _class;
        if(mClass.isLocked()) {
            mUnlockMenuItem.setVisible(true);
            mLockMenuItem.setVisible(false);
        }
        else {
            mUnlockMenuItem.setVisible(false);
            mLockMenuItem.setVisible(true);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.class_password_applied), Snackbar.LENGTH_SHORT).show();
            }
        }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
    }

    @Override
    public void onUpdateClass(ClassContract.ClassEntry _class, int rowsUpdated) {
        if(rowsUpdated > 0) {
            FragmentManager fm = getSupportFragmentManager();
            mClass = _class;
            ClassInfoFragment f1 = (ClassInfoFragment) fm.findFragmentByTag(getFragmentTag(INFO_TAB));
            if(f1 != null) {
                f1.updateClass(_class, rowsUpdated);
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.class_updated), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    @Override
    public void onGetClassSchedules(ArrayList<ClassScheduleContract.ClassScheduleEntry> classSchedules, String fragmentTag) {
        FragmentManager fm = getSupportFragmentManager();
        ClassInfoFragment f1 = (ClassInfoFragment) fm.findFragmentByTag(fragmentTag);
        if(f1 != null) {
            f1.populateClassSchedules(classSchedules, fragmentTag);
        }
    }

    @Override
    public void onInsertClassSchedule(ClassScheduleContract.ClassScheduleEntry classSchedule, long id, String fragmentTag) {
        if(classSchedule.getId() == -1) {
            FragmentManager fm = getSupportFragmentManager();
            ClassInfoFragment f1 = (ClassInfoFragment) fm.findFragmentByTag(fragmentTag);
            if(f1 != null) {
                f1.insertClassSchedule(classSchedule, id, fragmentTag);
            }
        }
    }

    @Override
    public void onUpdateClassSchedule(ClassScheduleContract.ClassScheduleEntry classSchedule, int rowsUpdated, String fragmentTag) {
        if(rowsUpdated > 0) {
            FragmentManager fm = getSupportFragmentManager();
            ClassInfoFragment f1 = (ClassInfoFragment) fm.findFragmentByTag(fragmentTag);
            if(f1 != null) {
                f1.updateClassSchedule(classSchedule, rowsUpdated, fragmentTag);
            }
        }
    }

    @Override
    public void onDeleteClassSchedule(ClassScheduleContract.ClassScheduleEntry classSchedule, int rowsDeleted, String fragmentTag) {
        if(rowsDeleted > 0) {
            FragmentManager fm = getSupportFragmentManager();
            ClassInfoFragment f1 = (ClassInfoFragment) fm.findFragmentByTag(fragmentTag);
            if(f1 != null) {
                f1.deleteClassSchedule(classSchedule, rowsDeleted, fragmentTag);
            }
        }
    }

    @Override
    public void onGetGradeBreakdowns(ArrayList<ClassGradeBreakdownContract.ClassGradeBreakdownEntry> gradeBreakdowns, String fragmentTag) {
        FragmentManager fm = getSupportFragmentManager();
        ClassInfoFragment f1 = (ClassInfoFragment) fm.findFragmentByTag(fragmentTag);
        if(f1 != null) {
            f1.populateGradeBreakdowns(gradeBreakdowns, fragmentTag);
        }
    }

    @Override
    public void onInsertClassGradeBreakdown(ClassGradeBreakdownContract.ClassGradeBreakdownEntry classGradeBreakdown, long id, String fragmentTag) {
        if(classGradeBreakdown.getId() == -1) {
            FragmentManager fm = getSupportFragmentManager();
            ClassInfoFragment f1 = (ClassInfoFragment) fm.findFragmentByTag(fragmentTag);
            if(f1 != null) {
                f1.insertClassGradeBreakdown(classGradeBreakdown, id, fragmentTag);
            }
        }
    }

    @Override
    public void onUpdateClassGradeBreakdown(ClassGradeBreakdownContract.ClassGradeBreakdownEntry classGradeBreakdown, int rowsUpdated, String fragmentTag) {
        if(rowsUpdated > 0) {
            FragmentManager fm = getSupportFragmentManager();
            ClassInfoFragment f1 = (ClassInfoFragment) fm.findFragmentByTag(fragmentTag);
            if(f1 != null) {
                f1.updateClassGradeBreakdown(classGradeBreakdown, rowsUpdated, fragmentTag);
            }
        }
    }

    @Override
    public void onDeleteClassGradeBreakdown(ClassGradeBreakdownContract.ClassGradeBreakdownEntry classGradeBreakdown, int rowsDeleted, String fragmentTag) {
        if(rowsDeleted > 0) {
            FragmentManager fm = getSupportFragmentManager();
            ClassInfoFragment f1 = (ClassInfoFragment) fm.findFragmentByTag(fragmentTag);
            if(f1 != null) {
                f1.deleteClassGradeBreakdown(classGradeBreakdown, rowsDeleted, fragmentTag);
            }
        }
    }

    @Override
    public void onInsertClassNote(ClassNoteContract.ClassNoteEntry classNote, long id, String fragmentTag) {
        if(classNote.getId() == -1) {
            FragmentManager fm = getSupportFragmentManager();
            ClassInfoFragment f1 = (ClassInfoFragment) fm.findFragmentByTag(fragmentTag);
            if(f1 != null) {
                f1.insertClassNote(classNote, id, fragmentTag);
            }
        }
    }

    @Override
    public void onUpdateClassNote(ClassNoteContract.ClassNoteEntry classNote, int rowsUpdated, String fragmentTag) {
        if(rowsUpdated > 0) {
            FragmentManager fm = getSupportFragmentManager();
            ClassInfoFragment f1 = (ClassInfoFragment) fm.findFragmentByTag(fragmentTag);
            if(f1 != null) {
                f1.updateClassNote(classNote, rowsUpdated, fragmentTag);
            }
        }
    }

    @Override
    public void onDeleteClassNote(ClassNoteContract.ClassNoteEntry classNote, int rowsDeleted, String fragmentTag) {
        if(rowsDeleted > 0) {
            FragmentManager fm = getSupportFragmentManager();
            ClassInfoFragment f1 = (ClassInfoFragment) fm.findFragmentByTag(fragmentTag);
            if(f1 != null) {
                f1.deleteClassNote(classNote, rowsDeleted, fragmentTag);
            }
        }
    }

    @Override
    public void onGetClassNotes(ArrayList<ClassNoteContract.ClassNoteEntry> classNotes, String fragmentTag) {
        FragmentManager fm = getSupportFragmentManager();
        ClassInfoFragment f1 = (ClassInfoFragment) fm.findFragmentByTag(fragmentTag);
        if(f1 != null) {
            f1.populateClassNotes(classNotes, fragmentTag);
        }
    }

    // TODO requery other fragments if necessary
    @Override
    public void onRequeryClass(ClassContract.ClassEntry _class) {
        mClass = _class;
        FragmentManager fm = getSupportFragmentManager();
        ClassInfoFragment f1 = (ClassInfoFragment) fm.findFragmentByTag(getFragmentTag(INFO_TAB));
        if(f1 != null)
            f1.requeryClass(_class);
        ClassItemsFragment f2 = (ClassItemsFragment) fm.findFragmentByTag(getFragmentTag(ITEMS_TAB));
        if(f2 != null)
            f2.requeryClass(_class);
        ClassStudentsFragment f3 = (ClassStudentsFragment) fm.findFragmentByTag(getFragmentTag(STUDENTS_TAB));
        if(f3 != null)
            f3.requeryClass(_class);
    }

    @Override
    public void onGetClassItemsSummary(ArrayList<ClassItemsFragment.ClassItemSummary> arrayList, String fragmentTag) {
        ClassItemsFragment f = (ClassItemsFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if(f != null)
            f.onGetClassItemsSummary(arrayList, fragmentTag);
    }

    @Override
    public void onInsertClassItem(ClassItemContract.ClassItemEntry classItem, long id, String fragmentTag) {
        if(classItem.getId() == -1) {
            FragmentManager fm = getSupportFragmentManager();
            ClassItemsFragment f2 = (ClassItemsFragment) fm.findFragmentByTag(fragmentTag);
            if(f2 != null) {
                f2.insertClassItem(classItem, id, fragmentTag);
            }
        }
    }

    @Override
    public void onGetClassStudentsSummary(ArrayList<ClassStudentsFragment.ClassStudentSummary> arrayList, String fragmentTag) {
        ClassStudentsFragment f = (ClassStudentsFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if(f != null)
            f.onGetClassStudentsSummary(arrayList, fragmentTag);
    }

    @Override
    public void onConfirmInsertUpdateClass(ClassContract.ClassEntry _class) {
        ClassActivityFragment rf = (ClassActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassActivityFragment.TAG);
        if(rf != null)
            rf.updateClass(_class);
    }

    @Override
    public void onConfirmDeleteClassSchedule(ClassScheduleContract.ClassScheduleEntry entry, String fragmentTag) {
        ClassActivityFragment rf = (ClassActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassActivityFragment.TAG);
        if(rf != null)
            rf.deleteClassSchedule(entry, fragmentTag);
    }

    @Override
    public void onConfirmInsertUpdateClassSchedule(ClassScheduleContract.ClassScheduleEntry entry, String fragmentTag) {
        ClassActivityFragment rf = (ClassActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassActivityFragment.TAG);
        if(rf != null) {
            entry.setClassId(mClass.getId()); // capture class id here
            if(entry.getId() == -1)
                rf.insertClassSchedule(entry, fragmentTag);
            else
                rf.updateClassSchedule(entry, fragmentTag);
        }
    }

    @Override
    public void onSetButtonTime(Date time, String dialogFragmentTag, int buttonId) {
        ClassScheduleInsertUpdateDialogFragment df = (ClassScheduleInsertUpdateDialogFragment) getSupportFragmentManager().findFragmentByTag(dialogFragmentTag);
        if(df != null)
            df.setButtonTime(time, buttonId);
    }

    @Override
    public void onSetButtonDays(int days, String dialogFragmentTag, int buttonId) {
        ClassScheduleInsertUpdateDialogFragment df = (ClassScheduleInsertUpdateDialogFragment) getSupportFragmentManager().findFragmentByTag(dialogFragmentTag);
        if(df != null)
            df.setButtonDays(days, buttonId);
    }

    @Override
    public void onConfirmDeleteClassGradeBreakdown(ClassGradeBreakdownContract.ClassGradeBreakdownEntry entry, String fragmentTag) {
        ClassActivityFragment rf = (ClassActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassActivityFragment.TAG);
        if(rf != null)
            rf.deleteClassGradeBreakdown(entry, fragmentTag);
    }

    @Override
    public void onConfirmInsertUpdateClassGradeBreakdown(ClassGradeBreakdownContract.ClassGradeBreakdownEntry entry, String fragmentTag) {
        ClassActivityFragment rf = (ClassActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassActivityFragment.TAG);
        if(rf != null) {
            entry.setClassId(mClass.getId()); // capture class id here
            if(entry.getId() == -1)
                rf.insertClassGradeBreakdown(entry, fragmentTag);
            else
                rf.updateClassGradeBreakdown(entry, fragmentTag);
        }
    }

    public ClassContract.ClassEntry getClassEntry() {
        return mClass;
    }

    @Override
    public void onConfirmInsertUpdateClassNote(ClassNoteContract.ClassNoteEntry entry, String fragmentTag) {
        ClassActivityFragment rf = (ClassActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassActivityFragment.TAG);
        if(rf != null) {
            entry.setClassId(mClass.getId()); // capture class id here
            if(entry.getId() == -1)
                rf.insertClassNote(entry, fragmentTag);
            else
                rf.updateClassNote(entry, fragmentTag);
        }
    }

    @Override
    public void onSetButtonDate(Date date, String dialogFragmentTag, int buttonId) {
        Fragment f = getSupportFragmentManager().findFragmentByTag(dialogFragmentTag);
        if(f != null) {
            if(f instanceof ClassNoteInsertUpdateDialogFragment)
                ((ClassNoteInsertUpdateDialogFragment) f).setButtonDate(date, buttonId);
            else if(f instanceof ClassItemInsertUpdateDialogFragment)
                ((ClassItemInsertUpdateDialogFragment) f).setButtonDate(date, buttonId);
        }
    }

    @Override
    public void onConfirmDeleteClassNote(ClassNoteContract.ClassNoteEntry entry, String fragmentTag) {
        ClassActivityFragment rf = (ClassActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassActivityFragment.TAG);
        if(rf != null)
            rf.deleteClassNote(entry, fragmentTag);
    }

    @Override
    public void onConfirmInsertUpdateClassItem(ClassItemContract.ClassItemEntry entry, String fragmentTag) {
        ClassActivityFragment rf = (ClassActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassActivityFragment.TAG);
        if(rf != null) {
            entry.setClassId(mClass.getId()); // capture class id here
            if(entry.getId() == -1)
                rf.insertClassItem(entry, fragmentTag);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            switch(position) {
                case INFO_TAB:
                    return ClassInfoFragment.newInstance(mClass);
                case ITEMS_TAB:
                    return ClassItemsFragment.newInstance(mClass);
                case STUDENTS_TAB:
                    return ClassStudentsFragment.newInstance(mClass);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case INFO_TAB:
                    return getString(R.string.info_tab);
                case ITEMS_TAB:
                    return getString(R.string.items_tab);
                case STUDENTS_TAB:
                    return getString(R.string.students_tab);
            }
            return null;
        }
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + R.id.view_pager + ":" + position;
    }
}
