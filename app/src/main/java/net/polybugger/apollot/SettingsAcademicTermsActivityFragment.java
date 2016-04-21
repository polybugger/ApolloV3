package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import net.polybugger.apollot.db.AcademicTermContract;
import net.polybugger.apollot.db.ApolloDbAdapter;

public class SettingsAcademicTermsActivityFragment extends Fragment {

    public interface Listener {
        void onGetAcademicTerms(ArrayList<AcademicTermContract.AcademicTermEntry> arrayList);
        void onDeleteAcademicTerm(AcademicTermContract.AcademicTermEntry entry, int rowsDeleted);
    }

    public static final String TAG = "net.polybugger.apollot.settings_academic_terms_activity_fragment";

    private ArrayList<AcademicTermContract.AcademicTermEntry> mArrayList;
    private Listener mListener;
    private GetAsyncTask mGetAsyncTask;

    public static SettingsAcademicTermsActivityFragment newInstance() {
        return new SettingsAcademicTermsActivityFragment();
    }

    public SettingsAcademicTermsActivityFragment() {
        setRetainInstance(true);
        mGetAsyncTask = new GetAsyncTask();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AsyncTask.Status status = mGetAsyncTask.getStatus();
        if(status == AsyncTask.Status.PENDING) {
            mGetAsyncTask.execute();
        }
        else if(status == AsyncTask.Status.FINISHED) {
            mListener.onGetAcademicTerms(mArrayList);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void deleteAcademicTerm(AcademicTermContract.AcademicTermEntry entry) {
        new DeleteAsyncTask().execute(entry);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
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

    private class GetAsyncTask extends AsyncTask<Void, Integer, ArrayList<AcademicTermContract.AcademicTermEntry>> {

        @Override
        protected ArrayList<AcademicTermContract.AcademicTermEntry> doInBackground(Void... ignore) {
            SQLiteDatabase db = ApolloDbAdapter.open();
            mArrayList = AcademicTermContract._getEntries(db);
            ApolloDbAdapter.close();
            return mArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<AcademicTermContract.AcademicTermEntry> arrayList) {
            if(mListener != null)
                mListener.onGetAcademicTerms(arrayList);
        }
    }

    private class DeleteAsyncTask extends AsyncTask<AcademicTermContract.AcademicTermEntry, Integer, Integer> {

        private AcademicTermContract.AcademicTermEntry mEntry;

        @Override
        protected Integer doInBackground(AcademicTermContract.AcademicTermEntry... entry) {
            mEntry = entry[0];
            SQLiteDatabase db = ApolloDbAdapter.open();
            int rowsDeleted = AcademicTermContract._delete(db, mEntry.getId());
            ApolloDbAdapter.close();
            return rowsDeleted;
        }

        @Override
        protected void onPostExecute(Integer rowsDeleted) {
            if(mListener != null)
                mListener.onDeleteAcademicTerm(mEntry, rowsDeleted);
        }
    }

    public static class InsertUpdateDialogFragment extends DialogFragment {

        public interface Listener {
            void onConfirmInsertAcademicTerm(AcademicTermContract.AcademicTermEntry entry);
            void onConfirmUpdateAcademicTerm(AcademicTermContract.AcademicTermEntry entry);
        }

        public static final String TAG = "net.polybugger.apollot.academic_term_insert_update_dialog_fragment";
        public static final String ENTRY_ARG = "net.polybugger.apollot.entry_arg";
        public static final String TITLE_ARG = "net.polybugger.apollot.title_arg";
        public static final String BUTTON_TEXT_ARG = "net.polybugger.apollot.button_text_arg";

        private Listener mListener;
        private AcademicTermContract.AcademicTermEntry mEntry;
        private EditText mEditText;
        private TextView mErrorTextView;

        public static InsertUpdateDialogFragment newInstance(AcademicTermContract.AcademicTermEntry entry, String title, String buttonText) {
            InsertUpdateDialogFragment df = new InsertUpdateDialogFragment();
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
            String title = args.getString(TITLE_ARG);
            String buttonText = args.getString(BUTTON_TEXT_ARG);
            View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_academic_term_insert_update, null);
            mEditText = (EditText) view.findViewById(R.id.edit_text);
            mErrorTextView = (TextView) view.findViewById(R.id.error_text_view);
            if(mEntry == null)
                mEntry = new AcademicTermContract.AcademicTermEntry(-1, null, null);
            mEditText.setText(mEntry.getDescription());
            LinearLayout backgroundLayout = (LinearLayout) view.findViewById(R.id.background_layout);
            int color;
            try {
                color = Color.parseColor(mEntry.getColor());
            }
            catch(Exception e) {
                color = Color.TRANSPARENT;
            }
            GradientDrawable bg = (GradientDrawable) backgroundLayout.getBackground();
            bg.setColor(color);
            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setView(view)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(buttonText, null)
                    .create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mEntry.setColor(mEditText.getText().toString());
                            if(mEntry.getId() == -1) {
                                mListener.onConfirmInsertAcademicTerm(mEntry);
                            }
                            else {
                                mListener.onConfirmUpdateAcademicTerm(mEntry);
                            }
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

    public static class DeleteDialogFragment extends DialogFragment {

        public interface Listener {
            void onConfirmDeleteAcademicTerm(AcademicTermContract.AcademicTermEntry entry);
        }

        public static final String TAG = "net.polybugger.apollot.academic_term_delete_dialog_fragment";
        public static final String ENTRY_ARG = "net.polybugger.apollot.entry_arg";

        private Listener mListener;

        public static DeleteDialogFragment newInstance(AcademicTermContract.AcademicTermEntry entry) {
            DeleteDialogFragment df = new DeleteDialogFragment();
            Bundle args = new Bundle();
            args.putSerializable(ENTRY_ARG, entry);
            df.setArguments(args);
            return df;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle args = getArguments();
            final AcademicTermContract.AcademicTermEntry entry = (AcademicTermContract.AcademicTermEntry) args.getSerializable(ENTRY_ARG);
            View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_academic_term_delete, null);
            LinearLayout backgroundLayout = (LinearLayout) view.findViewById(R.id.background_layout);
            int color;
            try {
                color = Color.parseColor(entry.getColor());
            }
            catch(Exception e) {
                color = Color.TRANSPARENT;
            }
            GradientDrawable bg = (GradientDrawable) backgroundLayout.getBackground();
            bg.setColor(color);
            TextView textView = (TextView) view.findViewById(R.id.text_view);
            textView.setText(entry.getDescription());
            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.remove_academic_term)
                    .setView(view)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.remove, null)
                    .create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mListener.onConfirmDeleteAcademicTerm(entry);
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
}
