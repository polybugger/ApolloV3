package net.polybugger.apollot;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private Switch mLockEnabledSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout academicTermsLinearLayout = (LinearLayout) findViewById(R.id.academic_terms_settings_item_layout);
        academicTermsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        LinearLayout classActivitiesLinearLayout = (LinearLayout) findViewById(R.id.class_activities_settings_item_layout);
        classActivitiesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        LinearLayout defaultTimeStartLinearLayout = (LinearLayout) findViewById(R.id.default_time_start_settings_item_layout);
        defaultTimeStartLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        LinearLayout finalGradeCalculationLinearLayout = (LinearLayout) findViewById(R.id.final_grade_calculation_settings_item_layout);
        finalGradeCalculationLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        LinearLayout studentNameDisplayFormatLinearLayout = (LinearLayout) findViewById(R.id.student_name_display_format_settings_item_layout);
        studentNameDisplayFormatLinearLayout.setOnClickListener(new View.OnClickListener() {
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

        LinearLayout lockEnabledLinearLayout = (LinearLayout) findViewById(R.id.lock_enabled_settings_item_layout);
        lockEnabledLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLockEnabledSwitch.setChecked(!mLockEnabledSwitch.isChecked());
            }
        });

        LinearLayout unlockPasswordLinearLayout = (LinearLayout) findViewById(R.id.unlock_password_settings_item_layout);
        unlockPasswordLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        LinearLayout aboutLinearLayout = (LinearLayout) findViewById(R.id.about_settings_item_layout);
        aboutLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
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

}
