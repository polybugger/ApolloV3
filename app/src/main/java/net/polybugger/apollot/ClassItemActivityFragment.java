package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.Date;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassItemContract;
import net.polybugger.apollot.db.ClassItemNoteContract;
import net.polybugger.apollot.db.ClassItemRecordContract;
import net.polybugger.apollot.db.ClassNoteContract;

public class ClassItemActivityFragment extends Fragment {

    public interface Listener {
        void onUpdateClassItem(ClassItemContract.ClassItemEntry classItem, int rowsUpdated, String fragmentTag);
        void onGetClassItemRecords(ArrayList<ClassItemRecordsFragment.ClassItemRecordSummary> arrayList, String fragmentTag);
        void onInsertClassItemRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, long id, String fragmentTag);
        void onUpdateClassItemRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, int rowsUpdated, String fragmentTag);
        void onGetClassItemSummaryInfo(ClassItemInfoFragment.ClassItemSummaryInfo classItemSummaryInfo, String fragmentTag);
        void onGetClassItemNotes(ArrayList<ClassItemNoteContract.ClassItemNoteEntry> classItemNotes, String fragmentTag);
        void onInsertClassItemNote(ClassItemNoteContract.ClassItemNoteEntry itemNote, long id, String fragmentTag);
        void onUpdateClassItemNote(ClassItemNoteContract.ClassItemNoteEntry itemNote, int rowsUpdated, String fragmentTag);
        void onDeleteClassItemNote(ClassItemNoteContract.ClassItemNoteEntry itemNote, int rowsDeleted, String fragmentTag);
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

    public void getClassItemSummaryInfo(ClassItemContract.ClassItemEntry classItem, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassItem = classItem;
        params.mFragmentTag = fragmentTag;
        new GetClassItemSummaryInfoAsyncTask().execute(params);
    }

    public void getClassItemNotes(ClassItemContract.ClassItemEntry classItem, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassItem = classItem;
        params.mFragmentTag = fragmentTag;
        new GetClassItemNotesAsyncTask().execute(params);
    }

    public void insertClassItemNote(ClassItemNoteContract.ClassItemNoteEntry itemNote, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mItemNote = itemNote;
        params.mFragmentTag = fragmentTag;
        new InsertClassItemNoteAsyncTask().execute(params);
    }

    public void updateClassItemNote(ClassItemNoteContract.ClassItemNoteEntry itemNote, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mItemNote = itemNote;
        params.mFragmentTag = fragmentTag;
        new UpdateClassItemNoteAsyncTask().execute(params);
    }

    public void deleteClassItemNote(ClassItemNoteContract.ClassItemNoteEntry itemNote, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mItemNote = itemNote;
        params.mFragmentTag = fragmentTag;
        new DeleteClassItemNoteAsyncTask().execute(params);
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
            ApolloDbAdapter.close();
            for(ClassItemRecordContract.ClassItemRecordEntry classItemRecord : classItemRecords) {
                ClassItemRecordsFragment.ClassItemRecordSummary classItemRecordSummary = new ClassItemRecordsFragment.ClassItemRecordSummary(classItemRecord);
                result.mClassItemRecords.add(classItemRecordSummary);
            }
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

    private class GetClassItemSummaryInfoAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassItem = params[0].mClassItem;
            result.mClassItemRecords = new ArrayList<>();
            result.mClassItemSummaryInfo = new ClassItemInfoFragment.ClassItemSummaryInfo();
            SQLiteDatabase db = ApolloDbAdapter.open();
            ArrayList<ClassItemRecordContract.ClassItemRecordEntry> classItemRecords = ClassItemRecordContract._getEntriesByClassItem(db, result.mClassItem.getClassId(), result.mClassItem.getId());
            ApolloDbAdapter.close();
            int total = 0;
            for(ClassItemRecordContract.ClassItemRecordEntry classItemRecord : classItemRecords) {
                total = total + 1;
                Boolean attendance = classItemRecord.getAttendance();
                if(attendance != null) {
                    if(attendance)
                        result.mClassItemSummaryInfo.mPresentAttendance = result.mClassItemSummaryInfo.mPresentAttendance + 1;
                    else
                        result.mClassItemSummaryInfo.mAbsentAttendance = result.mClassItemSummaryInfo.mAbsentAttendance + 1;
                }
                Float score = classItemRecord.getScore();
                if(score != null)
                    result.mClassItemSummaryInfo.mRecordedScores = result.mClassItemSummaryInfo.mRecordedScores + 1;
                Date submissionDate = classItemRecord.getSubmissionDate();
                if(submissionDate != null)
                    result.mClassItemSummaryInfo.mRecordedSubmissions = result.mClassItemSummaryInfo.mRecordedSubmissions + 1;
            }
            result.mClassItemSummaryInfo.mTotalAttendance = total;
            result.mClassItemSummaryInfo.mTotalScore = total;
            result.mClassItemSummaryInfo.mTotalSubmission = total;
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onGetClassItemSummaryInfo(result.mClassItemSummaryInfo, result.mFragmentTag);
            }
        }
    }

    private class GetClassItemNotesAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mClassItemNotes = ClassItemNoteContract._getEntriesByClassId(db, params[0].mClassItem.getClassId());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onGetClassItemNotes(result.mClassItemNotes, result.mFragmentTag);
            }
        }
    }

    private class InsertClassItemNoteAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mItemNote = params[0].mItemNote;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mId = ClassItemNoteContract._insert(db, result.mItemNote.getClassId(), result.mItemNote.getClassItemId(), result.mItemNote.getNote(), result.mItemNote.getDateCreated());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onInsertClassItemNote(result.mItemNote, result.mId, result.mFragmentTag);
            }
        }
    }

    private class UpdateClassItemNoteAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mItemNote = params[0].mItemNote;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsUpdated = ClassItemNoteContract._update(db, result.mItemNote.getId(), result.mItemNote.getClassId(), result.mItemNote.getClassItemId(), result.mItemNote.getNote(), result.mItemNote.getDateCreated());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onUpdateClassItemNote(result.mItemNote, result.mRowsUpdated, result.mFragmentTag);
            }
        }
    }

    private class DeleteClassItemNoteAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mItemNote = params[0].mItemNote;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsDeleted = ClassItemNoteContract._delete(db, result.mItemNote.getId(), result.mItemNote.getClassId());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onDeleteClassItemNote(result.mItemNote, result.mRowsDeleted, result.mFragmentTag);
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
        public ClassItemInfoFragment.ClassItemSummaryInfo mClassItemSummaryInfo;
        public ClassItemNoteContract.ClassItemNoteEntry mItemNote;
        public int mRowsUpdated;
        public int mRowsDeleted;
        public long mId;
        public ArrayList<ClassItemRecordsFragment.ClassItemRecordSummary> mClassItemRecords;
        public ArrayList<ClassItemNoteContract.ClassItemNoteEntry> mClassItemNotes;
        public String mFragmentTag;
        public AsyncTaskResult() { }
    }

    private class AsyncTaskParams {
        public ClassContract.ClassEntry mClass;
        public ClassItemContract.ClassItemEntry mClassItem;
        public ClassItemRecordContract.ClassItemRecordEntry mClassItemRecord;
        public ClassItemNoteContract.ClassItemNoteEntry mItemNote;
        public String mFragmentTag;

        public AsyncTaskParams() { }
    }

}
