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

import net.polybugger.apollot.db.AcademicTermContract;
import net.polybugger.apollot.db.ApolloDbAdapter;

import java.util.ArrayList;

public class SettingsAcademicTermsActivityFragment extends Fragment {

    public interface Listener {
        void onPreExecute();
        void onProgressUpdate(int percent);
        void onCancelled();
        void onPostExecute(ArrayList<AcademicTermContract.AcademicTermEntry> arrayList);
    }

    public static final String TAG = "net.polybugger.apollot.settings_academic_terms_activity_fragment";

    private ArrayList<AcademicTermContract.AcademicTermEntry> mArrayList;
    private Listener mListener;
    private GetAsyncTask mGetAsyncTask;

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
            mListener.onPostExecute(mArrayList);
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
        super.onDetach();
        mListener = null;
    }

    private class GetAsyncTask extends AsyncTask<Void, Integer, ArrayList<AcademicTermContract.AcademicTermEntry>> {

        @Override
        protected void onPreExecute() {
            if(mListener != null)
                mListener.onPreExecute();
        }

        @Override
        protected ArrayList<AcademicTermContract.AcademicTermEntry> doInBackground(Void... ignore) {
            SQLiteDatabase db = ApolloDbAdapter.open();
            mArrayList = AcademicTermContract._getEntries(db);
            ApolloDbAdapter.close();
            return mArrayList;
        }

        @Override
        protected void onProgressUpdate(Integer... percent) {
            if(mListener != null)
                mListener.onProgressUpdate(percent[0]);
        }

        @Override
        protected void onCancelled() {
            if(mListener != null)
                mListener.onCancelled();
        }

        @Override
        protected void onPostExecute(ArrayList<AcademicTermContract.AcademicTermEntry> arrayList) {
            if(mListener != null)
                mListener.onPostExecute(arrayList);
        }
    }
}
