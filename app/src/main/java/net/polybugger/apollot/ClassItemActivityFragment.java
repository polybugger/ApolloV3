package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassItemContract;
import net.polybugger.apollot.db.ClassItemRecordContract;

public class ClassItemActivityFragment extends Fragment {

    public interface Listener {
        void onUpdateClassItem(ClassItemContract.ClassItemEntry classItem, int rowsUpdated, String fragmentTag);
        void onGetClassItemRecords(ArrayList<ClassItemRecordsFragment.ClassItemRecordSummary> arrayList, String fragmentTag);
        void onInsertClassItemRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, long id, String fragmentTag);
        void onUpdateClassItemRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, int rowsUpdated, String fragmentTag);
    }

    public static final String TAG = "net.polybugger.apollot.class_item_activity_fragment";

    private Listener mListener;

    public static ClassItemActivityFragment newInstance() {
        return new ClassItemActivityFragment();
    }

    public ClassItemActivityFragment() {
        setRetainInstance(true);
    }

    public void updateClassItem(ClassItemContract.ClassItemEntry classItem, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassItem = classItem;
        params.mFragmentTag = fragmentTag;
        new UpdateClassItemAsyncTask().execute(params);
    }

    public void getClassItemRecords(ClassItemContract.ClassItemEntry classItem, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassItem = classItem;
        params.mFragmentTag = fragmentTag;
        new GetClassItemRecordsAsyncTask().execute(params);
    }

    public void insertClassItemRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassItemRecord = classItemRecord;
        params.mClassItem = classItem;
        params.mFragmentTag = fragmentTag;
        new InsertClassItemRecordAsyncTask().execute(params);
    }

    public void updateClassItemRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassItemRecord = classItemRecord;
        params.mClassItem = classItem;
        params.mFragmentTag = fragmentTag;
        new UpdateClassItemRecordAsyncTask().execute(params);
    }

    private class UpdateClassItemAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassItem = params[0].mClassItem;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsUpdated = ClassItemContract._update(db, result.mClassItem.getId(), result.mClassItem.getClassId(), result.mClassItem.getDescription(), result.mClassItem.getItemType(), result.mClassItem.getItemDate(), result.mClassItem.isCheckAttendance(), result.mClassItem.isRecordScores(), result.mClassItem.getPerfectScore(), result.mClassItem.isRecordSubmissions(), result.mClassItem.getSubmissionDueDate());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onUpdateClassItem(result.mClassItem, result.mRowsUpdated, result.mFragmentTag);
            }
        }
    }

    private class InsertClassItemRecordAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassItem = params[0].mClassItem;
            result.mClassItemRecord = params[0].mClassItemRecord;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mId = ClassItemRecordContract._insert(db, result.mClassItem.getClassId(), result.mClassItem.getId(), result.mClassItemRecord.getClassStudent(), result.mClassItemRecord.getAttendance(), result.mClassItemRecord.getScore(), result.mClassItemRecord.getSubmissionDate(), result.mClassItemRecord.getRemarks());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onInsertClassItemRecord(result.mClassItemRecord, result.mClassItem, result.mId, result.mFragmentTag);
            }
        }
    }

    private class UpdateClassItemRecordAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassItem = params[0].mClassItem;
            result.mClassItemRecord = params[0].mClassItemRecord;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsUpdated = ClassItemRecordContract._update(db, result.mClassItemRecord.getId(), result.mClassItem.getClassId(), result.mClassItem.getId(), result.mClassItemRecord.getClassStudent(), result.mClassItemRecord.getAttendance(), result.mClassItemRecord.getScore(), result.mClassItemRecord.getSubmissionDate(), result.mClassItemRecord.getRemarks());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onUpdateClassItemRecord(result.mClassItemRecord, result.mClassItem, result.mRowsUpdated, result.mFragmentTag);
            }
        }
    }

    private class GetClassItemRecordsAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassItem = params[0].mClassItem;
            result.mClassItemRecords = new ArrayList<>();
            SQLiteDatabase db = ApolloDbAdapter.open();
            ArrayList<ClassItemRecordContract.ClassItemRecordEntry> classItemRecords = ClassItemRecordContract._getEntriesByClassItem(db, result.mClassItem.getClassId(), result.mClassItem.getId());
            for(ClassItemRecordContract.ClassItemRecordEntry classItemRecord : classItemRecords) {
                ClassItemRecordsFragment.ClassItemRecordSummary classItemRecordSummary = new ClassItemRecordsFragment.ClassItemRecordSummary(classItemRecord);
                // TODO fetch class item records for summary
                result.mClassItemRecords.add(classItemRecordSummary);
            }
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onGetClassItemRecords(result.mClassItemRecords, result.mFragmentTag);
            }
        }
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

    private class AsyncTaskResult {
        public ClassContract.ClassEntry mClass;
        public ClassItemContract.ClassItemEntry mClassItem;
        public ClassItemRecordContract.ClassItemRecordEntry mClassItemRecord;
        public int mRowsUpdated;
        public int mRowsDeleted;
        public long mId;
        public ArrayList<ClassItemRecordsFragment.ClassItemRecordSummary> mClassItemRecords;
        public String mFragmentTag;
        public AsyncTaskResult() { }
    }

    private class AsyncTaskParams {
        public ClassContract.ClassEntry mClass;
        public ClassItemContract.ClassItemEntry mClassItem;
        public ClassItemRecordContract.ClassItemRecordEntry mClassItemRecord;
        public String mFragmentTag;

        public AsyncTaskParams() { }
    }

}
