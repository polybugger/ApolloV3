package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Date;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassItemContract;
import net.polybugger.apollot.db.ClassStudentContract;
import net.polybugger.apollot.db.GenderEnum;
import net.polybugger.apollot.db.StudentContract;

import org.apache.commons.lang3.StringUtils;

public class ClassStudentInsertUpdateDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onConfirmInsertUpdateClassStudent(ClassStudentContract.ClassStudentEntry entry, String fragmentTag);
    }

    public static final String TAG = "net.polybugger.apollot.insert_update_class_item_dialog_fragment";
    public static final String ENTRY_ARG = "net.polybugger.apollot.entry_arg";
    public static final String TITLE_ARG = "net.polybugger.apollot.title_arg";
    public static final String BUTTON_TEXT_ARG = "net.polybugger.apollot.button_text_arg";
    public static final String FRAGMENT_TAG_ARG = "net.polybugger.apollot.fragment_tag_arg";

    private Listener mListener;
    private ClassStudentContract.ClassStudentEntry mEntry;
    private AlertDialog mAlertDialog;
    private EditText mLastNameEditText;
    private TextView mErrorTextView;
    private EditText mFirstNameEditText;
    private EditText mMiddleNameEditText;
    private RadioGroup mGenderRadioGroup;
    private RadioButton mMaleRadioButton;
    private RadioButton mFemaleRadioButton;
    private EditText mEmailAddressEditText;
    private EditText mContactNumberEditText;

    public static ClassStudentInsertUpdateDialogFragment newInstance(ClassStudentContract.ClassStudentEntry entry, String title, String buttonText, String fragmentTag) {
        ClassStudentInsertUpdateDialogFragment df = new ClassStudentInsertUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ENTRY_ARG, entry);
        args.putString(TITLE_ARG, title);
        args.putString(BUTTON_TEXT_ARG, buttonText);
        args.putString(FRAGMENT_TAG_ARG, fragmentTag);
        df.setArguments(args);
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        mEntry = (ClassStudentContract.ClassStudentEntry) args.getSerializable(ENTRY_ARG);
        final String fragmentTag = args.getString(FRAGMENT_TAG_ARG);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_class_student_insert_update, null);
        mLastNameEditText = (EditText) view.findViewById(R.id.last_name_edit_text);
        mErrorTextView = (TextView) view.findViewById(R.id.error_text_view);
        mFirstNameEditText = (EditText) view.findViewById(R.id.first_name_edit_text);
        mMiddleNameEditText = (EditText) view.findViewById(R.id.middle_name_edit_text);
        mGenderRadioGroup = (RadioGroup) view.findViewById(R.id.gender_radio_group);
        mMaleRadioButton = (RadioButton) view.findViewById(R.id.male_radio_button);
        mFemaleRadioButton = (RadioButton) view.findViewById(R.id.female_radio_button);

        mEmailAddressEditText = (EditText) view.findViewById(R.id.email_address_edit_text);
        mContactNumberEditText = (EditText) view.findViewById(R.id.contact_number_edit_text);

        if(mEntry == null) {
            mEntry = new ClassStudentContract.ClassStudentEntry(-1, -1, new StudentContract.StudentEntry(-1, null, null, null, null, null, null), new Date());
        }

        StudentContract.StudentEntry student = mEntry.getStudent();
        mLastNameEditText.setText(student.getLastName());
        mFirstNameEditText.setText(student.getFirstName());
        mMiddleNameEditText.setText(student.getMiddleName());
        GenderEnum gender = student.getGender();
        mGenderRadioGroup.clearCheck();
        if(gender != null) {
            if(gender == GenderEnum.MALE)
                mMaleRadioButton.setChecked(true);
            if(gender == GenderEnum.FEMALE)
                mFemaleRadioButton.setChecked(true);
        }
        mEmailAddressEditText.setText(student.getEmailAddress());
        mContactNumberEditText.setText(student.getContactNumber());

        if(StringUtils.equalsIgnoreCase(getContext().getResources().getConfiguration().locale.getLanguage(), ApolloDbAdapter.JA_LANGUAGE))
            mMiddleNameEditText.setVisibility(View.GONE);
        else
            mMiddleNameEditText.setVisibility(View.VISIBLE);

        mAlertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(args.getString(TITLE_ARG))
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(args.getString(BUTTON_TEXT_ARG), null)
                .create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String lastName = mLastNameEditText.getText().toString();
                        if(StringUtils.isEmpty(lastName)) {
                            mErrorTextView.setText(R.string.please_enter_a_last_name);
                            mLastNameEditText.requestFocus();
                            return;
                        }
                        StudentContract.StudentEntry student = mEntry.getStudent();
                        student.setLastName(lastName);
                        student.setFirstName(mFirstNameEditText.getText().toString());
                        student.setMiddleName(mMiddleNameEditText.getText().toString());
                        if(mMaleRadioButton.isChecked()) student.setGender(GenderEnum.MALE);
                        if(mFemaleRadioButton.isChecked()) student.setGender(GenderEnum.FEMALE);
                        student.setEmailAddress(mEmailAddressEditText.getText().toString());
                        student.setContactNumber(mContactNumberEditText.getText().toString());
                        mListener.onConfirmInsertUpdateClassStudent(mEntry, fragmentTag);
                        dismiss();
                    }
                });
            }
        });
        return mAlertDialog;
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
