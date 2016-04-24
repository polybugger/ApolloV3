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

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassItemTypeContract;

public class SettingsClassItemTypesActivityFragment extends Fragment {

    public interface Listener {
        void onGetClassItemTypes(ArrayList<ClassItemTypeContract.ClassItemTypeEntry> arrayList);
        void onInsertClassItemType(ClassItemTypeContract.ClassItemTypeEntry entry, long id);
        void onUpdateClassItemType(ClassItemTypeContract.ClassItemTypeEntry entry, int rowsUpdated);
        void onDeleteClassItemType(ClassItemTypeContract.ClassItemTypeEntry entry, int rowsDeleted);
    }

    public static final String TAG = "net.polybugger.apollot.settings_class_item_types_activity_fragment";

    private Listener mListener;
    private GetAsyncTask mGetAsyncTask;
    private ArrayList<ClassItemTypeContract.ClassItemTypeEntry> mArrayList;

    public static SettingsClassItemTypesActivityFragment newInstance() {
        return new SettingsClassItemTypesActivityFragment();
    }

    public SettingsClassItemTypesActivityFragment() {
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
            mListener.onGetClassItemTypes(mArrayList);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
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

    public void insertClassItemType(ClassItemTypeContract.ClassItemTypeEntry entry) {
        new InsertAsyncTask().execute(entry);
    }

    public void updateClassItemType(ClassItemTypeContract.ClassItemTypeEntry entry) {
        new UpdateAsyncTask().execute(entry);
    }

    public void deleteClassItemType(ClassItemTypeContract.ClassItemTypeEntry entry) {
        new DeleteAsyncTask().execute(entry);
    }

    private class GetAsyncTask extends AsyncTask<Void, Integer, ArrayList<ClassItemTypeContract.ClassItemTypeEntry>> {

        @Override
        protected ArrayList<ClassItemTypeContract.ClassItemTypeEntry> doInBackground(Void... ignore) {
            SQLiteDatabase db = ApolloDbAdapter.open();
            ArrayList<ClassItemTypeContract.ClassItemTypeEntry> arrayList = ClassItemTypeContract._getEntries(db);
            ApolloDbAdapter.close();
            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<ClassItemTypeContract.ClassItemTypeEntry> arrayList) {
            mArrayList = arrayList;
            if(mListener != null)
                mListener.onGetClassItemTypes(mArrayList);
        }
    }

    private class InsertAsyncTask extends AsyncTask<ClassItemTypeContract.ClassItemTypeEntry, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(ClassItemTypeContract.ClassItemTypeEntry... entry) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mEntry = entry[0];
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mId = ClassItemTypeContract._insert(db, result.mEntry.getDescription(), result.mEntry.getColor());
            ApolloDbAdapter.close();
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onInsertClassItemType(result.mEntry, result.mId);
            }
        }
    }

    private class UpdateAsyncTask extends AsyncTask<ClassItemTypeContract.ClassItemTypeEntry, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(ClassItemTypeContract.ClassItemTypeEntry... entry) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mEntry = entry[0];
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsUpdated = ClassItemTypeContract._update(db, result.mEntry.getId(), result.mEntry.getDescription(), result.mEntry.getColor());
            ApolloDbAdapter.close();
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onUpdateClassItemType(result.mEntry, result.mRowsUpdated);
        }
    }

    private class DeleteAsyncTask extends AsyncTask<ClassItemTypeContract.ClassItemTypeEntry, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(ClassItemTypeContract.ClassItemTypeEntry... entry) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mEntry = entry[0];
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsDeleted = ClassItemTypeContract._delete(db, result.mEntry.getId());
            ApolloDbAdapter.close();
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onDeleteClassItemType(result.mEntry, result.mRowsDeleted);
        }
    }

    private static class AsyncTaskResult {
        public ClassItemTypeContract.ClassItemTypeEntry mEntry;
        public long mId;
        public int mRowsUpdated;
        public int mRowsDeleted;

        public AsyncTaskResult() { }
    }
}
