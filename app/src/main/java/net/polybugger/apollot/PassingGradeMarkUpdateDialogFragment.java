package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PassingGradeMarkUpdateDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onConfirmUpdatePassingGradeMark(Float passingGradeMark, FinalGradeCalculationActivity.GradeSystemType gradeSystemType);
    }

    public static final String TAG = "net.polybugger.apollot.update_passing_grade_mark_dialog_fragment";
    public static final String TYPE_ARG = "net.polybugger.apollot.type_arg";
    public static final String TITLE_ARG = "net.polybugger.apollot.title_arg";
    public static final String BUTTON_TEXT_ARG = "net.polybugger.apollot.button_text_arg";

    private Listener mListener;
    private Float mPassingGradeMark;
    private FinalGradeCalculationActivity.GradeSystemType mGradeSystemType;
    private TextView mTextView;
    private EditText mEditText;
    private TextView mErrorTextView;

    public static PassingGradeMarkUpdateDialogFragment newInstance(FinalGradeCalculationActivity.GradeSystemType gradeSystemType, String title, String buttonText) {
        PassingGradeMarkUpdateDialogFragment df = new PassingGradeMarkUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(TYPE_ARG, gradeSystemType);
        args.putString(TITLE_ARG, title);
        args.putString(BUTTON_TEXT_ARG, buttonText);
        df.setArguments(args);
        return df;
    }

    // TODO add checked foreground
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        mGradeSystemType = (FinalGradeCalculationActivity.GradeSystemType) args.getSerializable(TYPE_ARG);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_passing_grade_mark_update, null);
        mTextView = (TextView) view.findViewById(R.id.text_view);
        mEditText = (EditText) view.findViewById(R.id.edit_text);
        mErrorTextView = (TextView) view.findViewById(R.id.error_text_view);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                mErrorTextView.setText(" ");
            }
        });
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        switch(mGradeSystemType) {
            case ONE_TO_FIVE:
                mTextView.setText(R.string.one_to_five);
                mPassingGradeMark = sharedPref.getFloat(getString(R.string.one_to_five_passing_grade_mark_key), FinalGradeCalculationActivity.DEFAULT_ONE_TO_FIVE_PASSING_GRADE_MARK);
                break;
            case FOUR_TO_ONE:
                mTextView.setText(R.string.four_to_one);
                mPassingGradeMark = sharedPref.getFloat(getString(R.string.four_to_one_passing_grade_mark_key), FinalGradeCalculationActivity.DEFAULT_FOUR_TO_ONE_PASSING_GRADE_MARK);
                break;
        }
        mEditText.setText(String.format("%.2f", mPassingGradeMark));
        mEditText.setSelection(mEditText.getText().length());

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(args.getString(TITLE_ARG))
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(args.getString(BUTTON_TEXT_ARG), null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int instructionsResId= R.string.one_to_five_instructions;
                        switch(mGradeSystemType) {
                            case ONE_TO_FIVE:
                                instructionsResId = R.string.one_to_five_instructions;
                                break;
                            case FOUR_TO_ONE:
                                instructionsResId = R.string.four_to_one_instructions;
                                break;
                        }
                        try {
                            mPassingGradeMark = Float.parseFloat(mEditText.getText().toString());
                        }
                        catch(Exception e) {
                            mErrorTextView.setText(instructionsResId);
                            mEditText.requestFocus();
                            return;
                        }
                        if(mPassingGradeMark == 0) {
                            mErrorTextView.setText(instructionsResId);
                            mEditText.requestFocus();
                            return;
                        }
                        mListener.onConfirmUpdatePassingGradeMark(mPassingGradeMark, mGradeSystemType);
                        dismiss();
                    }
                });
            }
        });
        return alertDialog;
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
