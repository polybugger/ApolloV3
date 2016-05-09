package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
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

import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassItemTypeContract;

import org.apache.commons.lang3.StringUtils;

public class UnlockPasswordDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onUnlockPassword(ClassContract.ClassEntry entry);
    }

    public static final String TAG = "net.polybugger.apollot.unlock_password_dialog_fragment";
    public static final String ENTRY_ARG = "net.polybugger.apollot.entry_arg";

    private Listener mListener;
    private ClassContract.ClassEntry mEntry;
    private EditText mPasswordEditText;
    private TextView mPasswordErrorTextView;

    public static UnlockPasswordDialogFragment newInstance(ClassContract.ClassEntry entry) {
        UnlockPasswordDialogFragment df = new UnlockPasswordDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ENTRY_ARG, entry);
        df.setArguments(args);
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        mEntry = (ClassContract.ClassEntry) args.getSerializable(ENTRY_ARG);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_unlock_password, null);
        mPasswordEditText = (EditText) view.findViewById(R.id.password_edit_text);
        mPasswordErrorTextView = (TextView) view.findViewById(R.id.password_error_text_view);
        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                mPasswordErrorTextView.setText(" ");
            }
        });

        final AlertDialog alertDialog;
        if(mEntry == null) {
            setCancelable(false);
            mPasswordErrorTextView.setVisibility(View.VISIBLE);
            alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.unlock_password)
                    .setView(view)
                    .setPositiveButton(R.string.unlock, null)
                    .create();
        }
        else {
            mPasswordErrorTextView.setVisibility(View.GONE);
            alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.unlock_class)
                    .setView(view)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.unlock, null)
                    .create();
        }
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String password = mPasswordEditText.getText().toString();
                        if(mEntry == null) {
                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                            String savedPassword = sharedPref.getString(getString(R.string.unlock_password_key), getString(R.string.default_unlock_password));
                            if(!StringUtils.equals(savedPassword, password)) {
                                mPasswordErrorTextView.setText(R.string.incorrect_password);
                                mPasswordEditText.requestFocus();
                                return;
                            }
                            mListener.onUnlockPassword(mEntry);
                            dismiss();
                        }
                        else {
                            MainActivityFragment rf = (MainActivityFragment) getFragmentManager().findFragmentByTag(MainActivityFragment.TAG);
                            if(rf != null)
                                rf.unlockClass(mEntry, password);
                            dismiss();
                        }
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
