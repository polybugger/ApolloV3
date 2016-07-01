package net.polybugger.apollot;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassStudentContract;

public class ClassStudentActivity extends AppCompatActivity implements
        ClassStudentActivityFragment.Listener {

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
                    return ClassStudentInfoFragment.newInstance(mClass, mClassStudent);
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
