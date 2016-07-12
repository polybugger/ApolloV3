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

import org.apache.commons.lang3.StringUtils;

public class UnlockPasswordChangeDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onChangeUnlockPassword(final String message);
    }

    public static final String TAG = "net.polybugger.apollot.change_unlock_password_dialog_fragment";

    private Listener mListener;
    private EditText mPasswordEditText;
    private TextView mPasswordErrorTextView;
    private EditText mNewPasswordEditText;

    public static UnlockPasswordChangeDialogFragment newInstance() {
        return new UnlockPasswordChangeDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_unlock_password_change, null);
        mPasswordEditText = (EditText) view.findViewById(R.id.password_edit_text);
        mPasswordErrorTextView = (TextView) view.findViewById(R.id.password_error_text_view);
        mNewPasswordEditText = (EditText) view.findViewById(R.id.new_password_edit_text);
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
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.unlock_password)
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.change, null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                        String savedPassword = sharedPref.getString(getString(R.string.unlock_password_key), getString(R.string.default_unlock_password));
                        String password = mPasswordEditText.getText().toString();
                        if(!StringUtils.equals(savedPassword, password)) {
                            mPasswordErrorTextView.setText(R.string.incorrect_password);
                            mPasswordEditText.requestFocus();
                            return;
                        }
                        String newPassword = mNewPasswordEditText.getText().toString();
                        sharedPref.edit().putString(getString(R.string.unlock_password_key), newPassword).apply();
                        String message = StringUtils.isEmpty(newPassword) ? getString(R.string.unlock_password_cleared) : getString(R.string.unlock_password_changed);
                        mListener.onChangeUnlockPassword(message);
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
