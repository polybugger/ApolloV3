package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.polybugger.apollot.db.StudentContract;
import net.polybugger.apollot.db.StudentNameDisplayEnum;

public class StudentNameDisplayDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onChangeStudentNameDisplay(final String message);
    }

    public static final String TAG = "net.polybugger.apollot.student_name_display_dialog_fragment";

    private Listener mListener;

    public static StudentNameDisplayDialogFragment newInstance() {
        return new StudentNameDisplayDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_student_name_display, null);
        RadioButton lastNameFirstNameRadioButton = (RadioButton) view.findViewById(R.id.last_name_first_name_radio_button);
        lastNameFirstNameRadioButton.setTag(StudentNameDisplayEnum.LAST_NAME_FIRST_NAME);
        RadioButton firstNameLastNameRadioButton = (RadioButton) view.findViewById(R.id.first_name_last_name_radio_button);
        firstNameLastNameRadioButton.setTag(StudentNameDisplayEnum.FIRST_NAME_LAST_NAME);
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        StudentNameDisplayEnum studentNameDisplay = StudentNameDisplayEnum.fromInt(sharedPref.getInt(getString(R.string.student_name_display_key), getResources().getInteger(R.integer.default_student_name_display)));
        switch(studentNameDisplay) {
            case LAST_NAME_FIRST_NAME:
                lastNameFirstNameRadioButton.setChecked(true);
                break;
            case FIRST_NAME_LAST_NAME:
                firstNameLastNameRadioButton.setChecked(true);
                break;
        }
        ((RadioGroup) view.findViewById(R.id.student_name_display_radio_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                StudentNameDisplayEnum studentNameDisplay = (StudentNameDisplayEnum) group.findViewById(checkedId).getTag();
                sharedPref.edit().putInt(getString(R.string.student_name_display_key), studentNameDisplay.getValue()).apply();
                StudentContract.STUDENT_NAME_DISPLAY = studentNameDisplay;
                mListener.onChangeStudentNameDisplay(getString(R.string.student_name_display_changed));
                dismiss();
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.student_name_display)
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity) {
            try {
                mListener = (Listener) context;
            }
            catch(ClassCastException e) {
                throw new ClassCastException(context.toString() + " must implement " + Listener.class.toString());
            }
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }
}
