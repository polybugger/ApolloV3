package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.polybugger.apollot.db.StudentNameDisplayEnum;

public class StudentNameDisplayDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onChangeStudentNameDisplay(final String message);
    }

    public static final String TAG = "net.polybugger.apollot.student_name_display_dialog_fragment";

    private Listener mListener;
    private RadioButton mLastNameFirstNameRadioButton;
    private RadioButton mFirstNameLastNameRadioButton;
    private RadioGroup mStudentNameDisplayRadioGroup;

    public static StudentNameDisplayDialogFragment newInstance() {
        return new StudentNameDisplayDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_student_name_display, null);
        mLastNameFirstNameRadioButton = (RadioButton) view.findViewById(R.id.last_name_first_name_radio_button);
        mLastNameFirstNameRadioButton.setTag(StudentNameDisplayEnum.LAST_NAME_FIRST_NAME);
        mFirstNameLastNameRadioButton = (RadioButton) view.findViewById(R.id.first_name_last_name_radio_button);
        mFirstNameLastNameRadioButton .setTag(StudentNameDisplayEnum.FIRST_NAME_LAST_NAME);
        mStudentNameDisplayRadioGroup = (RadioGroup) view.findViewById(R.id.student_name_display_radio_group);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.student_name_display)
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Listener) activity;
        }
        catch(ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " + Listener.class.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mStudentNameDisplayRadioGroup.setOnCheckedChangeListener(null);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        StudentNameDisplayEnum defaultStudentNameDisplay = StudentNameDisplayEnum.fromInt(sharedPref.getInt(getString(R.string.student_name_display_key), getResources().getInteger(R.integer.default_student_name_display)));
        switch(defaultStudentNameDisplay) {
            case LAST_NAME_FIRST_NAME:
                mLastNameFirstNameRadioButton.setChecked(true);
                break;
            case FIRST_NAME_LAST_NAME:
                mFirstNameLastNameRadioButton.setChecked(true);
                break;
        }
        mStudentNameDisplayRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                StudentNameDisplayEnum studentNameDisplay = (StudentNameDisplayEnum) group.findViewById(checkedId).getTag();
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                sharedPref.edit().putInt(getString(R.string.student_name_display_key), studentNameDisplay.getValue()).apply();
                mListener.onChangeStudentNameDisplay(getString(R.string.student_name_display_changed));
                dismiss();
            }
        });
    }
}
