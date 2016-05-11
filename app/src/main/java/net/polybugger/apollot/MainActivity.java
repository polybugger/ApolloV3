package net.polybugger.apollot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;

import net.polybugger.apollot.db.AcademicTermContract;
import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.PastCurrentEnum;
import net.polybugger.apollot.db.StudentContract;
import net.polybugger.apollot.db.StudentNameDisplayEnum;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        MainActivityFragment.Listener,
        ClassInsertUpdateDialogFragment.Listener,
        UnlockPasswordDialogFragment.Listener {

    public static final long SNACKBAR_POST_DELAYED_MSEC = 500;

    private static boolean lockAuthenticated = false;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApolloDbAdapter.setAppContext(getApplicationContext());
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        StudentContract.STUDENT_NAME_DISPLAY = StudentNameDisplayEnum.fromInt(sharedPref.getInt(getString(R.string.student_name_display_key), getResources().getInteger(R.integer.default_student_name_display)));

        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentByTag(MainActivityFragment.TAG) == null)
            fm.beginTransaction().add(MainActivityFragment.newInstance(), MainActivityFragment.TAG).commit();

        boolean lockEnabled = sharedPref.getBoolean(getString(R.string.lock_enabled_key), false);
        if(lockEnabled && !lockAuthenticated) {
            UnlockPasswordDialogFragment df = (UnlockPasswordDialogFragment) fm.findFragmentByTag(UnlockPasswordDialogFragment.TAG);
            if(df == null) {
                df = UnlockPasswordDialogFragment.newInstance(null, UnlockPasswordDialogFragment.Option.UNLOCK_APP);
                df.show(fm, UnlockPasswordDialogFragment.TAG);
            }
        }
        else {
            onUnlockPassword(null);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int id = item.getItemId();

        switch(id) {
            case R.id.nav_day_activities:
                sharedPref.edit().putInt(getString(R.string.default_nav_drawer_menu_item_index_key), 0).apply();

                break;
            case R.id.nav_week_activities:
                sharedPref.edit().putInt(getString(R.string.default_nav_drawer_menu_item_index_key), 1).apply();

                break;
            case R.id.nav_current_classes:
                sharedPref.edit().putInt(getString(R.string.default_nav_drawer_menu_item_index_key), 2).apply();
                ft.replace(R.id.fragment_container, ClassesFragment.newInstance(PastCurrentEnum.CURRENT), ClassesFragment.TAG_CURRENT);
                mFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager fm = getSupportFragmentManager();
                        ClassInsertUpdateDialogFragment df = (ClassInsertUpdateDialogFragment) fm.findFragmentByTag(ClassInsertUpdateDialogFragment.TAG);
                        if(df == null) {
                            ClassContract.ClassEntry entry = new ClassContract.ClassEntry(-1, "", null, null, null, PastCurrentEnum.CURRENT, new Date());
                            df = ClassInsertUpdateDialogFragment.newInstance(entry, getString(R.string.new_class), getString(R.string.add));
                            df.show(fm, ClassInsertUpdateDialogFragment.TAG);
                        }
                    }
                });
                break;
            case R.id.nav_past_classes:
                sharedPref.edit().putInt(getString(R.string.default_nav_drawer_menu_item_index_key), 3).apply();
                ft.replace(R.id.fragment_container, ClassesFragment.newInstance(PastCurrentEnum.PAST), ClassesFragment.TAG_PAST);
                mFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager fm = getSupportFragmentManager();
                        ClassInsertUpdateDialogFragment df = (ClassInsertUpdateDialogFragment) fm.findFragmentByTag(ClassInsertUpdateDialogFragment.TAG);
                        if(df == null) {
                            ClassContract.ClassEntry entry = new ClassContract.ClassEntry(-1, "", null, null, null, PastCurrentEnum.PAST, new Date());
                            df = ClassInsertUpdateDialogFragment.newInstance(entry, getString(R.string.new_class), getString(R.string.add));
                            df.show(fm, ClassInsertUpdateDialogFragment.TAG);
                        }
                    }
                });
                break;
            case R.id.nav_students:
                sharedPref.edit().putInt(getString(R.string.default_nav_drawer_menu_item_index_key), 4).apply();

                break;
        }

        // ft.addToBackStack(null);
        ft.commit();

        // TODO fab is not anchored to any view, check if this creates any bugs
        if(mFab.getVisibility() == View.GONE)
            mFab.show();

        return true;
    }

    @Override
    public void onGetClassesSummary(ArrayList<ClassesFragment.ClassSummary> arrayList, PastCurrentEnum pastCurrent) {
        String tag = ClassesFragment.TAG_PAST;
        switch(pastCurrent) {
            case PAST:
                tag = ClassesFragment.TAG_PAST;
                break;
            case CURRENT:
                tag = ClassesFragment.TAG_CURRENT;
                break;
        }
        ClassesFragment f = (ClassesFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if(f != null)
            f.onGetClassesSummary(arrayList, pastCurrent);
    }

    @Override
    public void onGetAcademicTerms(ArrayList<AcademicTermContract.AcademicTermEntry> arrayList, String fragmentTag) {
        ClassInsertUpdateDialogFragment df = (ClassInsertUpdateDialogFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if(df != null)
            df.onGetAcademicTerms(arrayList, fragmentTag);
    }

    @Override
    public void onInsertClass(ClassContract.ClassEntry entry) {
        String tag = ClassesFragment.TAG_PAST;
        switch(entry.getPastCurrent()) {
            case PAST:
                tag = ClassesFragment.TAG_PAST;
                break;
            case CURRENT:
                tag = ClassesFragment.TAG_CURRENT;
                break;
        }
        ClassesFragment f = (ClassesFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if(f != null)
            f.insertClass(entry);

        Intent intent = new Intent(this, ClassActivity.class);
        Bundle args = new Bundle();
        args.putSerializable(ClassActivity.CLASS_ARG, entry);
        intent.putExtras(args);
        startActivity(intent);
    }

    @Override
    public void onUnlockClass(ClassContract.ClassEntry entry, boolean passwordMatched) {
        if(passwordMatched) {
            Intent intent = new Intent(this, ClassActivity.class);
            Bundle args = new Bundle();
            args.putSerializable(ClassActivity.CLASS_ARG, entry);
            intent.putExtras(args);
            startActivity(intent);
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
    public void onRequeryClassSummary(ClassesFragment.ClassSummary classSummary, String fragmentTag) {
        ClassesFragment f = (ClassesFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if(f != null)
            f.onRequeryClassSummary(classSummary, fragmentTag);
    }

    @Override
    public void onConfirmInsertClass(ClassContract.ClassEntry entry) {
        MainActivityFragment rf = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(MainActivityFragment.TAG);
        if(rf != null)
            rf.insertClass(entry);
    }

    @Override
    public void onUnlockPassword(ClassContract.ClassEntry _class) {
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenuView.setVerticalScrollBarEnabled(false);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int defaultNavDrawerMenuItemIndex = sharedPref.getInt(getString(R.string.default_nav_drawer_menu_item_index_key), 0);
        MenuItem defaultNavDrawerMenuItem = navigationView.getMenu().getItem(defaultNavDrawerMenuItemIndex);
        defaultNavDrawerMenuItem.setChecked(true);
        onNavigationItemSelected(defaultNavDrawerMenuItem);
    }
}
