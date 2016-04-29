package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassScheduleContract;
import net.polybugger.apollot.db.ClassStudentContract;
import net.polybugger.apollot.db.PastCurrentEnum;

public class MainActivityFragment extends Fragment {

    public interface Listener {
        void onGetClassesSummary(ArrayList<ClassesFragment.ClassSummary> arrayList, PastCurrentEnum pastCurrent);
    }

    public static final String TAG = "net.polybugger.apollot.main_activity_fragment";

    private Listener mListener;

    public static MainActivityFragment newInstance() {
        return new MainActivityFragment();
    }

    public MainActivityFragment() {
        setRetainInstance(true);
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

    public void getClassesSummary(PastCurrentEnum pastCurrent) {
        new GetClassesSummaryAsyncTask().execute(pastCurrent);
    }

    private class GetClassesSummaryAsyncTask extends AsyncTask<PastCurrentEnum, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(PastCurrentEnum... pastCurrent) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mPastCurrent = pastCurrent[0];
            result.mClassSummaries = new ArrayList<>();
            SQLiteDatabase db = ApolloDbAdapter.open();
            ArrayList<ClassContract.ClassEntry> classes = ClassContract._getEntriesByPastCurrent(db, result.mPastCurrent);
            for(ClassContract.ClassEntry _class : classes) {
                ClassesFragment.ClassSummary classSummary = new ClassesFragment.ClassSummary(_class);
                long classId = classSummary.mClass.getId();
                classSummary.mClassSchedules = ClassScheduleContract._getEntriesByClassId(db, classId);
                classSummary.mStudentCount = ClassStudentContract._getCount(db, classId);
                result.mClassSummaries.add(classSummary);
            }
            ApolloDbAdapter.close();
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onGetClassesSummary(result.mClassSummaries, result.mPastCurrent);
            }
        }
    }

    private class AsyncTaskResult {
        public PastCurrentEnum mPastCurrent;
        public ArrayList<ClassesFragment.ClassSummary> mClassSummaries;

        public AsyncTaskResult() { }
    }
}
