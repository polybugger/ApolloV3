package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassItemContract;

public class ClassItemActivityFragment extends Fragment {

    public interface Listener {
    }

    public static final String TAG = "net.polybugger.apollot.class_item_activity_fragment";

    private Listener mListener;

    public static ClassItemActivityFragment newInstance() {
        return new ClassItemActivityFragment();
    }

    public ClassItemActivityFragment() {
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
        public ClassItemContract.ClassItemEntry mClassItem;
        public int mRowsUpdated;
        public int mRowsDeleted;
        public long mId;
        public String mFragmentTag;
        public AsyncTaskResult() { }
    }

    private class AsyncTaskParams {
        public ClassContract.ClassEntry mClass;
        public ClassItemContract.ClassItemEntry mClassItem;
        public String mFragmentTag;

        public AsyncTaskParams() { }
    }

}
