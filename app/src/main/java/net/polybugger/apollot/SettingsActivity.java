package net.polybugger.apollot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import net.polybugger.apollot.db.ApolloDbAdapter;

public class SettingsActivity extends AppCompatActivity implements DefaultTimeStartDialogFragment.Listener,
        StudentNameDisplayDialogFragment.Listener,
        UnlockPasswordChangeDialogFragment.Listener {

    private Switch mLockEnabledSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApolloDbAdapter.setAppContext(getApplicationContext());

        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);

        findViewById(R.id.academic_terms_settings_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, SettingsAcademicTermsActivity.class));
            }
        });

        findViewById(R.id.class_activities_settings_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, SettingsClassItemTypesActivity.class));
            }
        });

        findViewById(R.id.default_time_start_settings_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                DefaultTimeStartDialogFragment df = (DefaultTimeStartDialogFragment) fm.findFragmentByTag(DefaultTimeStartDialogFragment.TAG);
                if(df == null) {
                    df = DefaultTimeStartDialogFragment.newInstance();
                    df.show(fm, DefaultTimeStartDialogFragment.TAG);
                }
            }
        });

        findViewById(R.id.final_grade_calculation_settings_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, FinalGradeCalculationActivity.class));
            }
        });

        findViewById(R.id.student_name_display_settings_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                StudentNameDisplayDialogFragment df = (StudentNameDisplayDialogFragment) fm.findFragmentByTag(StudentNameDisplayDialogFragment.TAG);
                if(df == null) {
                    df = StudentNameDisplayDialogFragment.newInstance();
                    df.show(fm, StudentNameDisplayDialogFragment.TAG);
                }
            }
        });

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean lockEnabled = sharedPref.getBoolean(getString(R.string.lock_enabled_key), getResources().getBoolean(R.bool.default_lock_enabled));
        mLockEnabledSwitch = (Switch) findViewById(R.id.lock_enabled_switch);
        mLockEnabledSwitch.setChecked(lockEnabled);
        mLockEnabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPref.edit().putBoolean(getString(R.string.lock_enabled_key), isChecked).apply();
            }
        });

        findViewById(R.id.lock_enabled_settings_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLockEnabledSwitch.setChecked(!mLockEnabledSwitch.isChecked());
            }
        });

        findViewById(R.id.unlock_password_settings_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                UnlockPasswordChangeDialogFragment df = (UnlockPasswordChangeDialogFragment) fm.findFragmentByTag(UnlockPasswordChangeDialogFragment.TAG);
                if(df == null) {
                    df = UnlockPasswordChangeDialogFragment.newInstance();
                    df.show(fm, UnlockPasswordChangeDialogFragment.TAG);
                }
            }
        });

        findViewById(R.id.about_this_app_settings_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChangeDefaultTimeStart(final String message) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_SHORT).show();
            }
        }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
    }

    @Override
    public void onChangeStudentNameDisplay(final String message) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_SHORT).show();
            }
        }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
    }

    @Override
    public void onChangeUnlockPassword(final String message) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_SHORT).show();
            }
        }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
    }
}
