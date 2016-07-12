package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PassingGradePercentageUpdateDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onConfirmUpdatePassingGradePercentage(Float passingGradePercentage);
    }

    public static final String TAG = "net.polybugger.apollot.update_passing_grade_percentage_dialog_fragment";
    public static final String PASSING_GRADE_PERCENTAGE_ARG = "net.polybugger.apollot.passing_grade_percentage_arg";
    public static final String TITLE_ARG = "net.polybugger.apollot.title_arg";
    public static final String BUTTON_TEXT_ARG = "net.polybugger.apollot.button_text_arg";

    private Listener mListener;
    private Float mPassingGradePercentage;
    private EditText mEditText;
    private TextView mErrorTextView;

    public static PassingGradePercentageUpdateDialogFragment newInstance(Float passingGradePercentage, String title, String buttonText) {
        PassingGradePercentageUpdateDialogFragment df = new PassingGradePercentageUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putFloat(PASSING_GRADE_PERCENTAGE_ARG, passingGradePercentage);
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
        mPassingGradePercentage = args.getFloat(PASSING_GRADE_PERCENTAGE_ARG);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_passing_grade_percentage_update, null);
        mEditText = (EditText) view.findViewById(R.id.percentage_edit_text);
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

        if(mPassingGradePercentage == null)
            mPassingGradePercentage = 0.0f;
        else {
            mEditText.setText(String.format("%.2f", mPassingGradePercentage));
            mEditText.setSelection(mEditText.getText().length());
        }
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
                        try {
                            mPassingGradePercentage = Float.parseFloat(mEditText.getText().toString());
                        }
                        catch(Exception e) {
                            mErrorTextView.setText(R.string.please_enter_a_passing_grade_percentage);
                            mEditText.requestFocus();
                            return;
                        }
                        if(mPassingGradePercentage == 0) {
                            mErrorTextView.setText(R.string.please_enter_a_passing_grade_percentage);
                            mEditText.requestFocus();
                            return;
                        }
                        mListener.onConfirmUpdatePassingGradePercentage(mPassingGradePercentage);
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
