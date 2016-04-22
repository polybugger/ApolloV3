package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import net.polybugger.apollot.db.AcademicTermContract;
import net.polybugger.apollot.db.ApolloDbAdapter;

public class SettingsAcademicTermsActivityFragment extends Fragment {

    public interface Listener {
        void onGetAcademicTerms(ArrayList<AcademicTermContract.AcademicTermEntry> arrayList);
        void onInsertAcademicTerm(AcademicTermContract.AcademicTermEntry entry, long id);
        void onUpdateAcademicTerm(AcademicTermContract.AcademicTermEntry entry, int rowsUpdated);
        void onDeleteAcademicTerm(AcademicTermContract.AcademicTermEntry entry, int rowsDeleted);
    }

    public static final String TAG = "net.polybugger.apollot.settings_academic_terms_activity_fragment";

    private Listener mListener;
    private GetAsyncTask mGetAsyncTask;
    private ArrayList<AcademicTermContract.AcademicTermEntry> mArrayList;

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

    public void insertAcademicTerm(AcademicTermContract.AcademicTermEntry entry) {
        new InsertAsyncTask().execute(entry);
    }

    public void updateAcademicTerm(AcademicTermContract.AcademicTermEntry entry) {
        new UpdateAsyncTask().execute(entry);
    }

    public void deleteAcademicTerm(AcademicTermContract.AcademicTermEntry entry) {
        new DeleteAsyncTask().execute(entry);
    }

    private class GetAsyncTask extends AsyncTask<Void, Integer, ArrayList<AcademicTermContract.AcademicTermEntry>> {

        @Override
        protected ArrayList<AcademicTermContract.AcademicTermEntry> doInBackground(Void... ignore) {
            SQLiteDatabase db = ApolloDbAdapter.open();
            ArrayList<AcademicTermContract.AcademicTermEntry> arrayList = AcademicTermContract._getEntries(db);
            ApolloDbAdapter.close();
            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<AcademicTermContract.AcademicTermEntry> arrayList) {
            mArrayList = arrayList;
            if(mListener != null)
                mListener.onGetAcademicTerms(mArrayList);
        }
    }

    private class InsertAsyncTask extends AsyncTask<AcademicTermContract.AcademicTermEntry, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AcademicTermContract.AcademicTermEntry... entry) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mEntry = entry[0];
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mId = AcademicTermContract._insert(db, result.mEntry.getDescription(), result.mEntry.getColor());
            ApolloDbAdapter.close();
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onInsertAcademicTerm(result.mEntry, result.mId);
            }
        }
    }

    private class UpdateAsyncTask extends AsyncTask<AcademicTermContract.AcademicTermEntry, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AcademicTermContract.AcademicTermEntry... entry) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mEntry = entry[0];
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsUpdated = AcademicTermContract._update(db, result.mEntry.getId(), result.mEntry.getDescription(), result.mEntry.getColor());
            ApolloDbAdapter.close();
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onUpdateAcademicTerm(result.mEntry, result.mRowsUpdated);
        }
    }

    private class DeleteAsyncTask extends AsyncTask<AcademicTermContract.AcademicTermEntry, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AcademicTermContract.AcademicTermEntry... entry) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mEntry = entry[0];
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsDeleted = AcademicTermContract._delete(db, result.mEntry.getId());
            ApolloDbAdapter.close();
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onDeleteAcademicTerm(result.mEntry, result.mRowsDeleted);
        }
    }

    private static class AsyncTaskResult {
        public AcademicTermContract.AcademicTermEntry mEntry;
        public long mId;
        public int mRowsUpdated;
        public int mRowsDeleted;

        public AsyncTaskResult() { }
    }
}
