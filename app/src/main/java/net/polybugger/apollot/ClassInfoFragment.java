package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.polybugger.apollot.db.AcademicTermContract;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.PastCurrentEnum;

import org.apache.commons.lang3.StringUtils;

public class ClassInfoFragment extends Fragment {

    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";

    private ClassContract.ClassEntry mClass;

    private LinearLayout mBackgroundLayout;
    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private TextView mAcademicTermTextView;
    private TextView mCurrentTextView;

    public static ClassInfoFragment newInstance(ClassContract.ClassEntry _class) {
        ClassInfoFragment f = new ClassInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(CLASS_ARG, _class);
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

        View view = inflater.inflate(R.layout.fragment_class_info, container, false);
        mBackgroundLayout = (LinearLayout) view.findViewById(R.id.background_layout);
        mTitleTextView = (TextView) view.findViewById(R.id.title_text_view);
        mAcademicTermTextView = (TextView) view.findViewById(R.id.academic_term_text_view);
        mCurrentTextView = (TextView) view.findViewById(R.id.current_text_view);

        populateClassInfo();
        return view;
    }

    private void populateClassInfo() {
        AcademicTermContract.AcademicTermEntry academicTerm = mClass.getAcademicTerm();
        if(academicTerm != null)
            mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(academicTerm.getColor(), getContext()));
        mTitleTextView.setText(mClass.getTitle());
        String academicTermYear = mClass.getAcademicTermYear();
        if(StringUtils.isBlank(academicTermYear)) {
            mAcademicTermTextView.setVisibility(View.GONE);
        }
        else {
            mAcademicTermTextView.setText(academicTermYear);
            mAcademicTermTextView.setVisibility(View.VISIBLE);
        }
        mCurrentTextView.setText(PastCurrentEnum.toString(mClass.getPastCurrent(), getContext()));


    }

}
