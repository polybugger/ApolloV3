package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.polybugger.apollot.db.AcademicTermContract;

import org.apache.commons.lang3.StringUtils;

public class AcademicTermInsertUpdateDialogFragment extends DialogFragment {

    public interface Listener {
        void onConfirmInsertUpdateAcademicTerm(AcademicTermContract.AcademicTermEntry entry);
    }

    public static final String TAG = "net.polybugger.apollot.insert_update_academic_term_dialog_fragment";
    public static final String ENTRY_ARG = "net.polybugger.apollot.entry_arg";
    public static final String TITLE_ARG = "net.polybugger.apollot.title_arg";
    public static final String BUTTON_TEXT_ARG = "net.polybugger.apollot.button_text_arg";

    private Listener mListener;
    private AcademicTermContract.AcademicTermEntry mEntry;
    private LinearLayout mBackgroundLayout;
    private EditText mEditText;
    private TextView mErrorTextView;

    public static AcademicTermInsertUpdateDialogFragment newInstance(AcademicTermContract.AcademicTermEntry entry, String title, String buttonText) {
        AcademicTermInsertUpdateDialogFragment df = new AcademicTermInsertUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ENTRY_ARG, entry);
        args.putString(TITLE_ARG, title);
        args.putString(BUTTON_TEXT_ARG, buttonText);
        df.setArguments(args);
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        mEntry = (AcademicTermContract.AcademicTermEntry) args.getSerializable(ENTRY_ARG);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_academic_term_insert_update, null);
        mBackgroundLayout = (LinearLayout) view.findViewById(R.id.background_layout);
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
        if(mEntry == null)
            mEntry = new AcademicTermContract.AcademicTermEntry(-1, "", null);
        else {
            mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(mEntry.getColor(), getActivity()));
            mEditText.setText(mEntry.getDescription());
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
                        String description = mEditText.getText().toString();
                        if(StringUtils.isEmpty(description)) {
                            mErrorTextView.setText(R.string.please_enter_an_academic_term);
                            mEditText.requestFocus();
                            return;
                        }
                        mEntry.setDescription(description);
                        mListener.onConfirmInsertUpdateAcademicTerm(mEntry);
                        dismiss();
                    }
                });
            }
        });
        return alertDialog;
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
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }
}
