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

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassScheduleContract;

public class ClassActivity extends AppCompatActivity implements ClassActivityFragment.Listener,
        UnlockPasswordDialogFragment.Listener,
        ClassInsertUpdateDialogFragment.Listener {

    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";

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

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
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
    public void onConfirmInsertUpdateClass(ClassContract.ClassEntry _class) {
        ClassActivityFragment rf = (ClassActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassActivityFragment.TAG);
        if(rf != null)
            rf.updateClass(_class);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case INFO_TAB:
                    mFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager fm = getSupportFragmentManager();
                            FloatingActionBarMenuDialogFragment df = (FloatingActionBarMenuDialogFragment) fm.findFragmentByTag(FloatingActionBarMenuDialogFragment.TAG);
                            if(df == null) {
                                df = FloatingActionBarMenuDialogFragment.newInstance();
                                df.show(fm, FloatingActionBarMenuDialogFragment.TAG);
                            }
                        }
                    });
                    return ClassInfoFragment.newInstance(mClass);
                case ITEMS_TAB:
                    return ClassInfoFragment.newInstance(mClass);
                case STUDENTS_TAB:
                    return ClassInfoFragment.newInstance(mClass);
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
