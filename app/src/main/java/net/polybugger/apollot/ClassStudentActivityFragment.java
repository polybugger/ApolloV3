package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import net.polybugger.apollot.db.ClassContract;

public class ClassStudentActivityFragment extends Fragment {

    public interface Listener {
    }

    public static final String TAG = "net.polybugger.apollot.class_student_activity_fragment";

    private Listener mListener;

    public static ClassStudentActivityFragment newInstance() {
        return new ClassStudentActivityFragment();
    }

    public ClassStudentActivityFragment() {
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

    private class AsyncTaskResult {
        public ClassContract.ClassEntry mClass;
        public int mRowsUpdated;
        public int mRowsDeleted;
        public long mId;
        public String mFragmentTag;
        public AsyncTaskResult() { }
    }

    private class AsyncTaskParams {
        public ClassContract.ClassEntry mClass;
        public String mFragmentTag;

        public AsyncTaskParams() { }
    }

}
