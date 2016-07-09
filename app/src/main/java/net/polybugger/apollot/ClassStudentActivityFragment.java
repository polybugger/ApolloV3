package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassGradeBreakdownContract;
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
        void onGetClassItemSubTotalSummaries(ArrayList<ClassStudentInfoFragment.ClassItemSubTotalSummary> classItemSubTotalSummaries, String fragmentTag);
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

    public void getClassItemSubTotalSummary(ClassStudentContract.ClassStudentEntry classStudent, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassStudent = classStudent;
        params.mFragmentTag = fragmentTag;
        new GetClassItemSubTotalSummaryAsyncTask().execute(params);
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

    private class GetClassItemSubTotalSummaryAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassStudent = params[0].mClassStudent;
            result.mClassItemSubTotalSummaries = new ArrayList<>();
            SQLiteDatabase db = ApolloDbAdapter.open();
            ArrayList<ClassGradeBreakdownContract.ClassGradeBreakdownEntry> classGradeBreakdowns = ClassGradeBreakdownContract._getEntries(db, result.mClassStudent.getClassId());
            for(ClassGradeBreakdownContract.ClassGradeBreakdownEntry classGradeBreakdown : classGradeBreakdowns) {
                result.mClassItemSubTotalSummaries.add(new ClassStudentInfoFragment.ClassItemSubTotalSummary(classGradeBreakdown));
            }

            ClassStudentInfoFragment.ClassItemSubTotalSummary classItemSubTotalSummary;
            int index; float percentage;
            ArrayList<ClassStudentRecordsFragment.ClassStudentRecordSummary> classStudentRecords = ClassItemRecordContract._getEntriesByClassStudent(db, result.mClassStudent.getClassId(), result.mClassStudent.getStudent().getId());
            for(ClassStudentRecordsFragment.ClassStudentRecordSummary classStudentRecord : classStudentRecords) {
                classItemSubTotalSummary = new ClassStudentInfoFragment.ClassItemSubTotalSummary(new ClassGradeBreakdownContract.ClassGradeBreakdownEntry(-1, result.mClassStudent.getClassId(), classStudentRecord.mClassItem.getItemType(), 0));
                index = result.mClassItemSubTotalSummaries.indexOf(classItemSubTotalSummary);
                if(index != -1)
                    classItemSubTotalSummary = result.mClassItemSubTotalSummaries.get(index);
                else
                    result.mClassItemSubTotalSummaries.add(classItemSubTotalSummary);
                classItemSubTotalSummary.incrementItemTypeTotal();
                if(classStudentRecord.mClassItemRecord.getId() != -1) {
                    classItemSubTotalSummary.incrementItemTypeCount();
                    boolean recordScores = classStudentRecord.mClassItem.isRecordScores();
                    if(classStudentRecord.mClassItem.isRecordScores()) {
                        percentage = classStudentRecord.mClassItemRecord.getScore() / classStudentRecord.mClassItem.getPerfectScore();
                        classItemSubTotalSummary.addPercentage(percentage);
                    }
                    if(classStudentRecord.mClassItem.isCheckAttendance()) {
                        Boolean attendance = classStudentRecord.mClassItemRecord.getAttendance();
                        if(attendance != null) {
                            if(attendance) {
                                if(!recordScores)
                                    classItemSubTotalSummary.addPercentage((float) 1.0);
                            }
                            else
                                classItemSubTotalSummary.incrementAbsencesCount();
                        }
                        classItemSubTotalSummary.incrementAttendanceCount();
                    }
                }
            }
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onGetClassItemSubTotalSummaries(result.mClassItemSubTotalSummaries, result.mFragmentTag);
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
        public ArrayList<ClassStudentInfoFragment.ClassItemSubTotalSummary> mClassItemSubTotalSummaries;
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
