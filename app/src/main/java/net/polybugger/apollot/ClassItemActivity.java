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

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassItemContract;

import java.util.ArrayList;
import java.util.Date;

public class ClassItemActivity extends AppCompatActivity implements ClassItemActivityFragment.Listener,
        ClassItemInsertUpdateDialogFragment.Listener,
        DatePickerDialogFragment.Listener {

    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";
    public static final String CLASS_ITEM_ARG = "net.polybugger.apollot.class_item_arg";

    private static final int INFO_TAB = 0;
    private static final int RECORDS_TAB = 1;

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
                /*
                FragmentManager fm = getSupportFragmentManager();
                FloatingActionBarMenuDialogFragment df = (FloatingActionBarMenuDialogFragment) fm.findFragmentByTag(FloatingActionBarMenuDialogFragment.TAG);
                if(df == null) {
                    df = FloatingActionBarMenuDialogFragment.newInstance(getFragmentTag(INFO_TAB), FloatingActionBarMenuDialogFragment.FABMode.CLASS_INFO_FRAGMENT);
                    df.show(fm, FloatingActionBarMenuDialogFragment.TAG);
                }
                */
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
                        /*
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
                        */
                        break;
                    case RECORDS_TAB:
                        p.setBehavior(null);
                        mFab.setLayoutParams(p);
                        mFab.setVisibility(View.GONE);
                        /*
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
                        */
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

    public ClassItemContract.ClassItemEntry getClassItemEntry() {
        return mClassItem;
    }

    @Override
    public void onConfirmInsertUpdateClassItem(ClassItemContract.ClassItemEntry entry, String fragmentTag) {
        if(entry.getId() != -1) {
            ClassItemActivityFragment rf = (ClassItemActivityFragment) getSupportFragmentManager().findFragmentByTag(ClassItemActivityFragment.TAG);
            if(rf != null) {
                rf.updateClassItem(entry, fragmentTag);
            }
        }
    }

    @Override
    public void onUpdateClassItem(ClassItemContract.ClassItemEntry classItem, int rowsUpdated, String fragmentTag) {
        if(rowsUpdated > 0) {
            FragmentManager fm = getSupportFragmentManager();
            ClassItemInfoFragment f1 = (ClassItemInfoFragment) fm.findFragmentByTag(fragmentTag);
            if(f1 != null) {
                f1.updateClassItem(classItem, rowsUpdated, fragmentTag);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.class_item_updated), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    @Override
    public void onGetClassItemRecords(ArrayList<ClassItemRecordsFragment.ClassItemRecordSummary> arrayList, String fragmentTag) {
        ClassItemRecordsFragment f = (ClassItemRecordsFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if(f != null)
            f.onGetClassItemRecords(arrayList, fragmentTag);
    }

    @Override
    public void onSetButtonDate(Date date, String dialogFragmentTag, int buttonId) {
        Fragment f = getSupportFragmentManager().findFragmentByTag(dialogFragmentTag);
        if(f != null) {
            if(f instanceof ClassItemInsertUpdateDialogFragment)
                ((ClassItemInsertUpdateDialogFragment) f).setButtonDate(date, buttonId);
            if(f instanceof ClassItemRecordsFragment)
                ((ClassItemRecordsFragment) f).setButtonDate(date, buttonId);
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
