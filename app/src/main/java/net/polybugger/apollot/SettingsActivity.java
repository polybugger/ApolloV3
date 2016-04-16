package net.polybugger.apollot;

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

public class SettingsActivity extends AppCompatActivity implements UnlockPasswordChangeDialogFragment.Listener {

    private Switch mLockEnabledSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View academicTermsSettingsItem = findViewById(R.id.academic_terms_settings_item);
        academicTermsSettingsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        View classActivitiesSettingsItem = findViewById(R.id.class_activities_settings_item);
        classActivitiesSettingsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        View defaultTimeStartSettingsItem = findViewById(R.id.default_time_start_settings_item);
        defaultTimeStartSettingsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        View finalGradeCalculationSettingsItem = findViewById(R.id.final_grade_calculation_settings_item);
        finalGradeCalculationSettingsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        View studentNameDisplayFormatSettingsItem = findViewById(R.id.student_name_display_format_settings_item);
        studentNameDisplayFormatSettingsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mLockEnabledSwitch = (Switch) findViewById(R.id.lock_enabled_switch);
        mLockEnabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPref.edit().putBoolean(getString(R.string.lock_enabled_key), isChecked).apply();
            }
        });

        View lockEnabledSettingsItem = findViewById(R.id.lock_enabled_settings_item);
        lockEnabledSettingsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLockEnabledSwitch.setChecked(!mLockEnabledSwitch.isChecked());
            }
        });

        View unlockPasswordSettingsItem = findViewById(R.id.unlock_password_settings_item);
        unlockPasswordSettingsItem.setOnClickListener(new View.OnClickListener() {
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

        View aboutThisAppSettingsItem = findViewById(R.id.about_this_app_settings_item);
        aboutThisAppSettingsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean lockEnabled = sharedPref.getBoolean(getString(R.string.lock_enabled_key), false);

        mLockEnabledSwitch.setChecked(lockEnabled);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
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
    public void onChangePassword(final String message) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_SHORT).show();
            }
        }, 500);
    }
}
