package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassScheduleContract;
import net.polybugger.apollot.db.ClassStudentContract;
import net.polybugger.apollot.db.StudentContract;

public class ClassStudentActivityFragment extends Fragment {

    public interface Listener {
        void onUpdateClassStudent(ClassStudentContract.ClassStudentEntry classStudent, int rowsUpdated, String fragmentTag);
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
        public int mRowsUpdated;
        public int mRowsDeleted;
        public long mId;
        public String mFragmentTag;
        public AsyncTaskResult() { }
    }

    private class AsyncTaskParams {
        public ClassContract.ClassEntry mClass;
        public ClassStudentContract.ClassStudentEntry mClassStudent;
        public String mFragmentTag;

        public AsyncTaskParams() { }
    }

}
