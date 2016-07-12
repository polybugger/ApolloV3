package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AToFPercentageUpdateDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onConfirmUpdateAToFPercentage(Float percentage, AToFActivity.TagSummary tagSummary);
    }

    public static final String TAG = "net.polybugger.apollot.update_a_to_f_percentage_dialog_fragment";
    public static final String TAG_SUMMARY_ARG = "net.polybugger.apollot.tag_summary_arg";
    public static final String TITLE_ARG = "net.polybugger.apollot.title_arg";
    public static final String BUTTON_TEXT_ARG = "net.polybugger.apollot.button_text_arg";

    private Listener mListener;
    private AToFActivity.TagSummary mTagSummary;
    private AlertDialog mAlertDialog;
    private TextView mTextView;
    private EditText mEditText;
    private TextView mErrorTextView;

    public static AToFPercentageUpdateDialogFragment newInstance(AToFActivity.TagSummary tagSummary, String title, String buttonText) {
        AToFPercentageUpdateDialogFragment df = new AToFPercentageUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAG_SUMMARY_ARG, tagSummary);
        args.putString(TITLE_ARG, title);
        args.putString(BUTTON_TEXT_ARG, buttonText);
        df.setArguments(args);
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        mTagSummary = (AToFActivity.TagSummary) args.getSerializable(TAG_SUMMARY_ARG);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_a_to_f_percentage_update, null);
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

        mTextView.setText(String.format("%s ≥", mTagSummary.mGradeMark.getGradeMark()));
        mEditText.setText(String.format("%.2f", mTagSummary.mGradeMark.getPercentage()));
        mEditText.setSelection(mEditText.getText().length());

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
                        Float percentage;
                        try {
                            percentage = Float.parseFloat(mEditText.getText().toString());
                        }
                        catch(Exception e) {
                            mErrorTextView.setText(R.string.please_enter_an_a_to_f_percentage);
                            mEditText.requestFocus();
                            return;
                        }
                        if(percentage == 0) {
                            mErrorTextView.setText(R.string.please_enter_an_a_to_f_percentage);
                            mEditText.requestFocus();
                            return;
                        }
                        mListener.onConfirmUpdateAToFPercentage(percentage, mTagSummary);
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
