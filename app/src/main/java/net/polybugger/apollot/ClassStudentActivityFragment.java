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
import net.polybugger.apollot.db.ClassStudentContract;
import net.polybugger.apollot.db.StudentContract;

public class ClassStudentActivityFragment extends Fragment {

    public interface Listener {
        void onUpdateClassStudent(ClassStudentContract.ClassStudentEntry classStudent, int rowsUpdated, String fragmentTag);
        void onGetClassStudentRecords(ArrayList<ClassStudentRecordsFragment.ClassStudentRecordSummary> classStudentRecords, String fragmentTag);
        void onInsertClassStudentRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, long id, String fragmentTag);
        void onUpdateClassStudentRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, int rowsUpdated, String fragmentTag);
    }

    public static final String TAG = "net.polybugger.apollot.class_student_activity_fragment";

    private Listener mListener;

    public static ClassStudentActivityFragment newInstance() {
        return new ClassStudentActivityFragment();
    }

    public ClassStudentActivityFragment() {
        setRetainInstance(true);
    }

    public void updateClassStudent(ClassStudentContract.ClassStudentEntry classStudent, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassStudent = classStudent;
        params.mFragmentTag = fragmentTag;
        new UpdateClassStudentAsyncTask().execute(params);
    }

    public void getClassStudentRecords(ClassStudentContract.ClassStudentEntry classStudent, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassStudent = classStudent;
        params.mFragmentTag = fragmentTag;
        new GetClassStudentRecordsAsyncTask().execute(params);
    }

    public void insertClassStudentRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassItemRecord = classItemRecord;
        params.mClassItem = classItem;
        params.mFragmentTag = fragmentTag;
        new InsertClassStudentRecordAsyncTask().execute(params);
    }

    public void updateClassStudentRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassItemRecord = classItemRecord;
        params.mClassItem = classItem;
        params.mFragmentTag = fragmentTag;
        new UpdateClassStudentRecordAsyncTask().execute(params);
    }

    private class UpdateClassStudentAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassStudent = params[0].mClassStudent;
            StudentContract.StudentEntry student = result.mClassStudent.getStudent();
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsUpdated = StudentContract._update(db, student.getId(), student.getLastName(), student.getFirstName(), student.getMiddleName(), student.getGender(), student.getEmailAddress(), student.getContactNumber());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onUpdateClassStudent(result.mClassStudent, result.mRowsUpdated, result.mFragmentTag);
        }
    }

    private class GetClassStudentRecordsAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassStudent = params[0].mClassStudent;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mClassStudentRecords = ClassItemRecordContract._getEntriesByClassStudent(db, result.mClassStudent.getClassId(), result.mClassStudent.getStudent().getId());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onGetClassStudentRecords(result.mClassStudentRecords, result.mFragmentTag);
            }
        }
    }

    private class InsertClassStudentRecordAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

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
                mListener.onInsertClassStudentRecord(result.mClassItemRecord, result.mClassItem, result.mId, result.mFragmentTag);
            }
        }
    }

    private class UpdateClassStudentRecordAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

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
                mListener.onUpdateClassStudentRecord(result.mClassItemRecord, result.mClassItem, result.mRowsUpdated, result.mFragmentTag);
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
        public ClassStudentContract.ClassStudentEntry mClassStudent;
        public ClassItemContract.ClassItemEntry mClassItem;
        public ClassItemRecordContract.ClassItemRecordEntry mClassItemRecord;
        public int mRowsUpdated;
        public int mRowsDeleted;
        public long mId;
        public ArrayList<ClassStudentRecordsFragment.ClassStudentRecordSummary> mClassStudentRecords;
        public String mFragmentTag;
        public AsyncTaskResult() { }
    }

    private class AsyncTaskParams {
        public ClassContract.ClassEntry mClass;
        public ClassStudentContract.ClassStudentEntry mClassStudent;
        public ClassItemContract.ClassItemEntry mClassItem;
        public ClassItemRecordContract.ClassItemRecordEntry mClassItemRecord;
        public String mFragmentTag;
        public AsyncTaskParams() { }
    }

}
