package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

public class MainActivityFragment extends Fragment {

    // TODO create summary class for ClassEntry for this purpose only
    public interface Listener {
        void onGetCurrentClasses();
        void onGetPastClasses();
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

}
