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

import org.apache.commons.lang3.StringUtils;

public class UnlockPasswordDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onUnlockPassword(ClassContract.ClassEntry _class);
    }

    public static final String TAG = "net.polybugger.apollot.unlock_password_dialog_fragment";
    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";
    public static final String OPTION_ARG = "net.polybugger.apollot.option_arg";

    private Listener mListener;
    private ClassContract.ClassEntry mClass;
    private Option mOption;
    private EditText mPasswordEditText;
    private TextView mPasswordErrorTextView;

    public static UnlockPasswordDialogFragment newInstance(ClassContract.ClassEntry _class, Option option) {
        UnlockPasswordDialogFragment df = new UnlockPasswordDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(CLASS_ARG, _class);
        args.putSerializable(OPTION_ARG, option);
        df.setArguments(args);
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        mClass = (ClassContract.ClassEntry) args.getSerializable(CLASS_ARG);
        mOption = (Option) args.getSerializable(OPTION_ARG);

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
        switch(mOption) {
            case UNLOCK_APP:
                setCancelable(false);
                mPasswordErrorTextView.setVisibility(View.VISIBLE);
                alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.unlock_password)
                        .setView(view)
                        .setPositiveButton(R.string.unlock, null)
                        .create();
                break;
            case UNLOCK_CLASS:
                mPasswordErrorTextView.setVisibility(View.GONE);
                alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.unlock_class)
                        .setView(view)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.unlock, null)
                        .create();
                break;
            case REMOVE_LOCK:
                mPasswordErrorTextView.setVisibility(View.GONE);
                alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.unlock_class)
                        .setView(view)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.unlock, null)
                        .create();
                break;
            case APPLY_LOCK:
                mPasswordErrorTextView.setVisibility(View.GONE);
                alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.lock_class)
                        .setView(view)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.lock, null)
                        .create();
                break;
            default:
                setCancelable(false);
                mPasswordErrorTextView.setVisibility(View.VISIBLE);
                alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.unlock_password)
                        .setView(view)
                        .setPositiveButton(R.string.unlock, null)
                        .create();
                break;
        }
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivityFragment mrf;
                        ClassActivityFragment crf;
                        SharedPreferences sharedPref;
                        String savedPassword;
                        String password = mPasswordEditText.getText().toString();
                        switch(mOption) {
                            case UNLOCK_APP:
                                sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                                savedPassword = sharedPref.getString(getString(R.string.unlock_password_key), getString(R.string.default_unlock_password));
                                if(!StringUtils.equals(savedPassword, password)) {
                                    mPasswordErrorTextView.setText(R.string.incorrect_password);
                                    mPasswordEditText.requestFocus();
                                    return;
                                }
                                mListener.onUnlockPassword(mClass);
                                dismiss();
                                break;
                            case UNLOCK_CLASS:
                                mrf = (MainActivityFragment) getFragmentManager().findFragmentByTag(MainActivityFragment.TAG);
                                if(mrf != null)
                                    mrf.unlockClass(mClass, password);
                                dismiss();
                                break;
                            case REMOVE_LOCK:
                                crf = (ClassActivityFragment) getFragmentManager().findFragmentByTag(ClassActivityFragment.TAG);
                                if(crf != null)
                                    crf.unlockClass(mClass, password);
                                dismiss();
                                break;
                            case APPLY_LOCK:
                                crf = (ClassActivityFragment) getFragmentManager().findFragmentByTag(ClassActivityFragment.TAG);
                                if(crf != null)
                                    crf.lockClass(mClass, password);
                                dismiss();
                                break;
                            default:
                                sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                                savedPassword = sharedPref.getString(getString(R.string.unlock_password_key), getString(R.string.default_unlock_password));
                                if(!StringUtils.equals(savedPassword, password)) {
                                    mPasswordErrorTextView.setText(R.string.incorrect_password);
                                    mPasswordEditText.requestFocus();
                                    return;
                                }
                                mListener.onUnlockPassword(mClass);
                                dismiss();
                                break;
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

    public enum Option {
        UNLOCK_APP(0),
        UNLOCK_CLASS(1),
        REMOVE_LOCK(2),
        APPLY_LOCK(3);

        private int mValue;

        private Option(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }
}
