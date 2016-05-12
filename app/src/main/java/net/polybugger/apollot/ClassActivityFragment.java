package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassPasswordContract;

import org.apache.commons.lang3.StringUtils;

public class ClassActivityFragment extends Fragment {

    public interface Listener {
        void onUnlockClass(ClassContract.ClassEntry _class, boolean passwordMatched);
        void onLockClass(ClassContract.ClassEntry _class);
        void onUpdateClass(ClassContract.ClassEntry _class, int rowsUpdated);
    }

    public static final String TAG = "net.polybugger.apollot.class_activity_fragment";

    private Listener mListener;

    public static ClassActivityFragment newInstance() {
        return new ClassActivityFragment();
    }

    public ClassActivityFragment() {
        setRetainInstance(true);
    }

    public void lockClass(ClassContract.ClassEntry _class, String password) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClass = _class;
        params.mPassword = password;
        new LockClassAsyncTask().execute(params);
    }

    public void unlockClass(ClassContract.ClassEntry _class, String password) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClass = _class;
        params.mPassword = password;
        new UnlockClassAsyncTask().execute(params);
    }

    public void updateClass(ClassContract.ClassEntry _class) {
        new UpdateClassAsyncTask().execute(_class);
    }

    private class UnlockClassAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClass = params[0].mClass;
            SQLiteDatabase db = ApolloDbAdapter.open();
            ClassPasswordContract.ClassPasswordEntry classPassword = ClassPasswordContract._getEntryByClassId(db, result.mClass.getId());
            result.mPasswordMatched = StringUtils.equals(classPassword.getPassword(), params[0].mPassword);
            if(result.mPasswordMatched)
                result.mClass.setLocked(!(ClassPasswordContract._delete(db, classPassword.getId()) > 0));
            ApolloDbAdapter.close();
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onUnlockClass(result.mClass, result.mPasswordMatched);
            }
        }
    }

    private class LockClassAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClass = params[0].mClass;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mClass.setLocked(ClassPasswordContract._insert(db, result.mClass.getId(), params[0].mPassword) > 0);
            ApolloDbAdapter.close();
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onLockClass(result.mClass);
            }
        }
    }

    private class UpdateClassAsyncTask extends AsyncTask<ClassContract.ClassEntry, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(ClassContract.ClassEntry... _class) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClass = _class[0];
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsUpdated = ClassContract._update(db, result.mClass.getId(), result.mClass.getCode(), result.mClass.getDescription(), result.mClass.getAcademicTerm(), result.mClass.getYear(), result.mClass.getPastCurrent(), result.mClass.getDateCreated());
            ApolloDbAdapter.close();
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onUpdateClass(result.mClass, result.mRowsUpdated);
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
        public boolean mPasswordMatched;
        public int mRowsUpdated;
        public AsyncTaskResult() { }
    }

    private class AsyncTaskParams {
        public ClassContract.ClassEntry mClass;
        public String mPassword;

        public AsyncTaskParams() { }
    }

}
