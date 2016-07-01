package net.polybugger.apollot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.polybugger.apollot.db.ApolloDbAdapter;

public class FinalGradeCalculationActivity extends AppCompatActivity implements
        PassingGradePercentageUpdateDialogFragment.Listener,
        PassingGradeMarkUpdateDialogFragment.Listener {

    public static float DEFAULT_PASSING_GRADE_PERCENTAGE = (float) 75.0;
    public static float DEFAULT_ONE_TO_FIVE_PASSING_GRADE_MARK = (float) 3.0;
    public static float DEFAULT_FOUR_TO_ONE_PASSING_GRADE_MARK = (float) 1.0;

    private LinearLayout mPassingGradePercentageLinearLayout;
    private TextView mPassingGradePercentageTextView;
    private CheckBox mAToFCheckBox;
    private ImageButton mAToFImageButton;
    private CheckBox mOneToFiveCheckBox;
    private ImageButton mOneToFiveImageButton;
    private CheckBox mFourToOneCheckBox;
    private ImageButton mFourToOneImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApolloDbAdapter.setAppContext(getApplicationContext());

        setContentView(R.layout.activity_final_grade_calculation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPassingGradePercentageLinearLayout = (LinearLayout) findViewById(R.id.passing_grade_percentage_linear_layout);
        mPassingGradePercentageTextView = (TextView) findViewById(R.id.passing_grade_percentage_text_view);
        mAToFCheckBox = (CheckBox) findViewById(R.id.a_to_f_check_box);
        mAToFImageButton = (ImageButton) findViewById(R.id.a_to_f_edit_button);
        mOneToFiveCheckBox = (CheckBox) findViewById(R.id.one_to_five_check_box);
        mOneToFiveImageButton = (ImageButton) findViewById(R.id.one_to_five_edit_button);
        mFourToOneCheckBox = (CheckBox) findViewById(R.id.four_to_one_check_box);
        mFourToOneImageButton = (ImageButton) findViewById(R.id.four_to_one_edit_button);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final float passingGradePercentage = sharedPref.getFloat(getString(R.string.passing_grade_percentage_key), DEFAULT_PASSING_GRADE_PERCENTAGE);
        mPassingGradePercentageTextView.setText(String.format("%.2f%%", passingGradePercentage));

        mPassingGradePercentageLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                PassingGradePercentageUpdateDialogFragment df = (PassingGradePercentageUpdateDialogFragment) fm.findFragmentByTag(PassingGradePercentageUpdateDialogFragment.TAG);
                if(df == null) {
                    df = PassingGradePercentageUpdateDialogFragment.newInstance(passingGradePercentage, getString(R.string.update_passing_grade_percentage), getString(R.string.save_changes));
                    df.show(fm, PassingGradePercentageUpdateDialogFragment.TAG);
                }
            }
        });

        mAToFCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                boolean selected = checkBox.isChecked();
                sharedPref.edit().putBoolean(getString(R.string.a_to_f_selected_key), selected).apply();
            }
        });
        mAToFCheckBox.setChecked(sharedPref.getBoolean(getString(R.string.a_to_f_selected_key), true));
        mAToFImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FinalGradeCalculationActivity.this, AToFActivity.class));
            }
        });

        mOneToFiveCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                boolean selected = checkBox.isChecked();
                sharedPref.edit().putBoolean(getString(R.string.one_to_five_selected_key), selected).apply();
            }
        });
        mOneToFiveCheckBox.setChecked(sharedPref.getBoolean(getString(R.string.one_to_five_selected_key), false));
        mOneToFiveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                PassingGradeMarkUpdateDialogFragment df = (PassingGradeMarkUpdateDialogFragment) fm.findFragmentByTag(PassingGradeMarkUpdateDialogFragment.TAG);
                if(df == null) {
                    df = PassingGradeMarkUpdateDialogFragment.newInstance(GradeSystemType.ONE_TO_FIVE, getString(R.string.update_passing_grade_mark), getString(R.string.save_changes));
                    df.show(fm, PassingGradeMarkUpdateDialogFragment.TAG);
                }
            }
        });

        mFourToOneCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                boolean selected = checkBox.isChecked();
                sharedPref.edit().putBoolean(getString(R.string.four_to_one_selected_key), selected).apply();
            }
        });
        mFourToOneCheckBox.setChecked(sharedPref.getBoolean(getString(R.string.four_to_one_selected_key), false));
        mFourToOneImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                PassingGradeMarkUpdateDialogFragment df = (PassingGradeMarkUpdateDialogFragment) fm.findFragmentByTag(PassingGradeMarkUpdateDialogFragment.TAG);
                if(df == null) {
                    df = PassingGradeMarkUpdateDialogFragment.newInstance(GradeSystemType.FOUR_TO_ONE, getString(R.string.update_passing_grade_mark), getString(R.string.save_changes));
                    df.show(fm, PassingGradeMarkUpdateDialogFragment.TAG);
                }
            }
        });
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
    public void onConfirmUpdatePassingGradePercentage(Float passingGradePercentage) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.edit().putFloat(getString(R.string.passing_grade_percentage_key), passingGradePercentage).apply();
        mPassingGradePercentageTextView.setText(String.format("%.2f%%", passingGradePercentage));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.passing_grade_percentage_updated), Snackbar.LENGTH_SHORT).show();
            }
        }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
    }

    @Override
    public void onConfirmUpdatePassingGradeMark(Float passingGradeMark, GradeSystemType gradeSystemType) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        switch(gradeSystemType) {
            case ONE_TO_FIVE:
                sharedPref.edit().putFloat(getString(R.string.one_to_five_passing_grade_mark_key), passingGradeMark).apply();
                break;
            case FOUR_TO_ONE:
                sharedPref.edit().putFloat(getString(R.string.four_to_one_passing_grade_mark_key), passingGradeMark).apply();
                break;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.passing_grade_mark_updated), Snackbar.LENGTH_SHORT).show();
            }
        }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
    }

    public enum GradeSystemType {
        ONE_TO_FIVE(0),
        A_TO_F(1),
        FOUR_TO_ONE(2);

        private static final GradeSystemType[] sValues = GradeSystemType.values();

        public static GradeSystemType fromInt(int x) {
            if(x < 0 || x > 2)
                return ONE_TO_FIVE;
            return sValues[x];
        }

        private int mValue;

        private GradeSystemType(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }
}
