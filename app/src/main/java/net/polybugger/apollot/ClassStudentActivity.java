package net.polybugger.apollot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;
import java.util.Date;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassItemContract;
import net.polybugger.apollot.db.ClassItemRecordContract;
import net.polybugger.apollot.db.ClassStudentContract;

public class ClassStudentActivity extends AppCompatActivity implements
        ClassStudentActivityFragment.Listener,
        ClassStudentInsertUpdateDialogFragment.Listener,
        ClassItemRecordInsertUpdateDialogFragment.Listener,
        DatePickerDialogFragment.Listener,
        ClassStudentDeleteDialogFragment.Listener {

    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";
    public static final String CLASS_STUDENT_ARG = "net.polybugger.apollot.class_student_arg";

    private static final int INFO_TAB = 0;
    private static final int RECORDS_TAB = 1;

    public static boolean REQUERY = false;

    private ClassContract.ClassEntry mClass;
    private ClassStudentContract.ClassStudentEntry mClassStudent;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getIntent().getExtras();
        if(args == null || !args.containsKey(CLASS_ARG) || !args.containsKey(CLASS_STUDENT_ARG)) {
            super.onBackPressed();
            return;
        }

        ApolloDbAdapter.setAppContext(getApplicationContext());

        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentByTag(ClassItemActivityFragment.TAG) == null)
            fm.beginTransaction().add(ClassStudentActivityFragment.newInstance(), ClassStudentActivityFragment.TAG).commit();

        mClass = (ClassContract.ClassEntry) args.getSerializable(CLASS_ARG);
        mClassStudent = (ClassStudentContract.ClassStudentEntry) args.getSerializable(CLASS_STUDENT_ARG);

        setContentView(R.layout.activity_class_student);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(mClassStudent.getStudent().getName(this));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_class_student, menu);
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
                ClassStudentDeleteDialogFragment df = (ClassStudentDeleteDialogFragment) fm.findFragmentByTag(ClassStudentDeleteDialogFragment.TAG);
                if(df == null) {
                    df = ClassStudentDeleteDialogFragment.newInstance(mClassStudent);
                    df.show(fm, ClassStudentDeleteDialogFragment.TAG);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public ClassContract.ClassEntry getClassEntry() {
        return mClass;
    }

    public ClassStudentContract.ClassStudentEntry getClassStudentEntry() {
        return mClassStudent;
    }

    @Override
    public void onConfirmInsertUpdateClassStudent(ClassStudentContract.ClassStudentEntry entry, String fragmentTag) {
        ClassStudentActivityFragment rf = (ClassStudentActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassStudentActivityFragment.TAG);
        if(rf != null) {
            if(entry.getId() != -1)
                rf.updateClassStudent(entry, fragmentTag);
        }
    }

    @Override
    public void onUpdateClassStudent(ClassStudentContract.ClassStudentEntry classStudent, int rowsUpdated, String fragmentTag) {
        mClassStudent = classStudent;
        setTitle(mClassStudent.getStudent().getName(this));
        ClassStudentInfoFragment f1 = (ClassStudentInfoFragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(INFO_TAB));
        if(f1 != null)
            f1.updateClassStudent(classStudent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.class_student_updated), Snackbar.LENGTH_SHORT).show();
            }
        }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
    }

    @Override
    public void onGetClassStudentRecords(ArrayList<ClassStudentRecordsFragment.ClassStudentRecordSummary> classStudentRecords, String fragmentTag) {
        ClassStudentRecordsFragment f2 = (ClassStudentRecordsFragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(RECORDS_TAB));
        if(f2 != null) {
            f2.onGetClassStudentRecords(classStudentRecords, fragmentTag);
        }
    }

    @Override
    public void onInsertClassStudentRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, long id, String fragmentTag) {
        if(classItemRecord.getId() == -1) {
            ClassStudentRecordsFragment f = (ClassStudentRecordsFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
            if(f != null)
                f.onInsertClassStudentRecord(classItemRecord, classItem, id, fragmentTag);
        }
    }

    @Override
    public void onUpdateClassStudentRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, int rowsUpdated, String fragmentTag) {
        if(rowsUpdated > 0) {
            ClassStudentRecordsFragment f = (ClassStudentRecordsFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
            if(f != null)
                f.onUpdateClassStudentRecord(classItemRecord, classItem, rowsUpdated, fragmentTag);
        }
    }

    @Override
    public void onGetClassItemSubTotalSummaries(ArrayList<ClassStudentInfoFragment.ClassItemSubTotalSummary> classItemSubTotalSummaries, String fragmentTag) {
        if(classItemSubTotalSummaries.size() > 0) {
            ClassStudentInfoFragment f1 = (ClassStudentInfoFragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(INFO_TAB));
            if(f1 != null)
                f1.populateClassItemSubTotalSummaries(classItemSubTotalSummaries);
        }
    }

    @Override
    public void onDeleteClassStudent(ClassStudentContract.ClassStudentEntry entry, int rowsDeleted) {
        if(rowsDeleted == 1) {
            ClassStudentsFragment.DELETE_CLASS_STUDENT = true;
            ClassStudentsFragment.CLASS_STUDENT = entry;
            onBackPressed();
        }
    }

    @Override
    public void onConfirmInsertUpdateClassItemRecord(ClassItemRecordContract.ClassItemRecordEntry entry, ClassItemContract.ClassItemEntry classItem, String fragmentTag) {
        ClassStudentActivityFragment f = (ClassStudentActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassStudentActivityFragment.TAG);
        if(f != null) {
            if(entry.getId() == -1)
                f.insertClassStudentRecord(entry, classItem, fragmentTag);
            else
                f.updateClassStudentRecord(entry, classItem, fragmentTag);
        }
    }

    @Override
    public void onSetButtonDate(Date date, String dialogFragmentTag, int buttonId) {
        Fragment f = getSupportFragmentManager().findFragmentByTag(dialogFragmentTag);
        if(f != null) {
            if(f instanceof ClassItemRecordInsertUpdateDialogFragment)
                ((ClassItemRecordInsertUpdateDialogFragment) f).setButtonDate(date, buttonId);
        }
    }

    @Override
    public void onConfirmDeleteClassStudent(ClassStudentContract.ClassStudentEntry entry) {
        ClassStudentActivityFragment f = (ClassStudentActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassStudentActivityFragment.TAG);
        if(f != null)
            f.deleteClassStudent(entry);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            switch(position) {
                case INFO_TAB:
                    return ClassStudentInfoFragment.newInstance(mClass, mClassStudent);
                case RECORDS_TAB:
                    return ClassStudentRecordsFragment.newInstance(mClass, mClassStudent);
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
                    return getString(R.string.class_student_info_tab);
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
