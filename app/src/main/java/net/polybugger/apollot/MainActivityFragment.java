package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import net.polybugger.apollot.db.AcademicTermContract;
import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassItemContract;
import net.polybugger.apollot.db.ClassScheduleContract;
import net.polybugger.apollot.db.ClassStudentContract;
import net.polybugger.apollot.db.PastCurrentEnum;

public class MainActivityFragment extends Fragment {

    public interface Listener {
        void onGetClassesSummary(ArrayList<ClassesFragment.ClassSummary> arrayList, PastCurrentEnum pastCurrent);
        void onGetAcademicTerms(ArrayList<AcademicTermContract.AcademicTermEntry> arrayList, String fragmentTag);
        void onInsertClass(ClassContract.ClassEntry entry);
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

    public void getAcademicTerms(String fragmentTag) {
        new GetAcademicTermsAsyncTask().execute(fragmentTag);
    }

    public void insertClass(ClassContract.ClassEntry entry) {
        new InsertClassAsyncTask().execute(entry);
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
                classSummary.mItemSummaryCount = ClassItemContract._getItemSummaryCount(db, classId);
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

    private class GetAcademicTermsAsyncTask extends AsyncTask<String, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(String... fragmentTag) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mFragmentTag = fragmentTag[0];
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mAcademicTerms = AcademicTermContract._getEntries(db);
            ApolloDbAdapter.close();
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onGetAcademicTerms(result.mAcademicTerms, result.mFragmentTag);
            }
        }
    }

    private class InsertClassAsyncTask extends AsyncTask<ClassContract.ClassEntry, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(ClassContract.ClassEntry... entry) {
            ClassContract.ClassEntry classEntry = entry[0];
            SQLiteDatabase db = ApolloDbAdapter.open();
            long id = ClassContract._insert(db, classEntry.getCode(), classEntry.getDescription(), classEntry.getAcademicTerm(), classEntry.getYear(), classEntry.getPastCurrent(), classEntry.getDateCreated());
            ApolloDbAdapter.close();
            classEntry.setId(id);
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassEntry = classEntry;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onInsertClass(result.mClassEntry);
            }
        }
    }

    private class AsyncTaskResult {
        public PastCurrentEnum mPastCurrent;
        public ArrayList<ClassesFragment.ClassSummary> mClassSummaries;

        public String mFragmentTag;
        public ArrayList<AcademicTermContract.AcademicTermEntry> mAcademicTerms;

        public ClassContract.ClassEntry mClassEntry;

        public AsyncTaskResult() { }
    }
}
