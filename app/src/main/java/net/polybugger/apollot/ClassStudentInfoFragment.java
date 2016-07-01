package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassStudentContract;

public class ClassStudentInfoFragment extends Fragment {

    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";
    public static final String CLASS_STUDENT_ARG = "net.polybugger.apollot.class_student_arg";

    private ClassContract.ClassEntry mClass;
    private ClassStudentContract.ClassStudentEntry mClassStudent;

    private NestedScrollView mNestedScrollView;
    private LinearLayout mBackgroundLayout;
    private TextView mTitleTextView;

    public static ClassStudentInfoFragment newInstance(ClassContract.ClassEntry _class, ClassStudentContract.ClassStudentEntry classStudent) {
        ClassStudentInfoFragment f = new ClassStudentInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(CLASS_ARG, _class);
        args.putSerializable(CLASS_STUDENT_ARG, classStudent);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity) {
            /*
            try {
                mListener = (Listener) context;
            }
            catch(ClassCastException e) {
                throw new ClassCastException(context.toString() + " must implement " + Listener.class.toString());
            }
            */
        }
    }

    @Override
    public void onDetach() {
        // mListener = null;
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        mClass = (ClassContract.ClassEntry) args.getSerializable(CLASS_ARG);
        mClassStudent = (ClassStudentContract.ClassStudentEntry) args.getSerializable(CLASS_STUDENT_ARG);

        View view = inflater.inflate(R.layout.fragment_class_student_info, container, false);
        mNestedScrollView = (NestedScrollView) view.findViewById(R.id.nested_scroll_view);
        mBackgroundLayout = (LinearLayout) view.findViewById(R.id.background_layout);
        mTitleTextView = (TextView) view.findViewById(R.id.title_text_view);
        view.findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // populateClassItemInfo();

        ClassStudentActivityFragment rf = (ClassStudentActivityFragment) getFragmentManager().findFragmentByTag(ClassStudentActivityFragment.TAG);
        if(rf != null) {
            //rf.getClassItemSummaryInfo(mClassItem, getTag());
            //rf.getClassItemNotes(mClassItem, getTag());
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
