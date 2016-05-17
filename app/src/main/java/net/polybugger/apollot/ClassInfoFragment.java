package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import net.polybugger.apollot.db.AcademicTermContract;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassGradeBreakdownContract;
import net.polybugger.apollot.db.ClassItemTypeContract;
import net.polybugger.apollot.db.ClassScheduleContract;
import net.polybugger.apollot.db.PastCurrentEnum;

import org.apache.commons.lang3.StringUtils;

public class ClassInfoFragment extends Fragment {

    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";

    private ClassContract.ClassEntry mClass;

    private LinearLayout mBackgroundLayout;
    private TextView mTitleTextView;
    private TextView mAcademicTermTextView;
    private TextView mCurrentTextView;

    private LinearLayout mScheduleLinearLayout;
    private ArrayList<ClassScheduleContract.ClassScheduleEntry> mScheduleList;
    private View.OnClickListener mEditScheduleClickListener;
    private View.OnClickListener mRemoveScheduleClickListener;

    private LinearLayout mGradeBreakdownLinearLayout;
    private ArrayList<ClassGradeBreakdownContract.ClassGradeBreakdownEntry> mGradeBreakdownList;
    private View.OnClickListener mEditGradeBreakdownClickListener;
    private View.OnClickListener mRemoveGradeBreakdownClickListener;
    private TextView mTotalPercentageTextView;

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
        view.findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ClassInsertUpdateDialogFragment df = (ClassInsertUpdateDialogFragment) fm.findFragmentByTag(ClassInsertUpdateDialogFragment.TAG);
                if(df == null) {
                    df = ClassInsertUpdateDialogFragment.newInstance(mClass, getString(R.string.update_class), getString(R.string.save_changes));
                    df.show(fm, ClassInsertUpdateDialogFragment.TAG);
                }
            }
        });

        mScheduleLinearLayout = (LinearLayout) view.findViewById(R.id.schedule_linear_layout);
        mRemoveScheduleClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ClassScheduleDeleteDialogFragment df = (ClassScheduleDeleteDialogFragment) fm.findFragmentByTag(ClassScheduleDeleteDialogFragment.TAG);
                if(df == null) {
                    df = ClassScheduleDeleteDialogFragment.newInstance((ClassScheduleContract.ClassScheduleEntry) v.getTag(), getTag());
                    df.show(fm, ClassScheduleDeleteDialogFragment.TAG);
                }
            }
        };
        mEditScheduleClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ClassScheduleInsertUpdateDialogFragment df = (ClassScheduleInsertUpdateDialogFragment) fm.findFragmentByTag(ClassScheduleInsertUpdateDialogFragment.TAG);
                if(df == null) {
                    df = ClassScheduleInsertUpdateDialogFragment.newInstance((ClassScheduleContract.ClassScheduleEntry) v.getTag(), getString(R.string.update_class_schedule), getString(R.string.save_changes), getTag());
                    df.show(fm, ClassScheduleInsertUpdateDialogFragment.TAG);
                }
            }
        };

        mGradeBreakdownLinearLayout = (LinearLayout) view.findViewById(R.id.grade_breakdown_linear_layout);
        mRemoveGradeBreakdownClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ClassGradeBreakdownDeleteDialogFragment df = (ClassGradeBreakdownDeleteDialogFragment) fm.findFragmentByTag(ClassGradeBreakdownDeleteDialogFragment.TAG);
                if(df == null) {
                    df = ClassGradeBreakdownDeleteDialogFragment.newInstance((ClassGradeBreakdownContract.ClassGradeBreakdownEntry) v.getTag(), getTag());
                    df.show(fm, ClassGradeBreakdownDeleteDialogFragment.TAG);
                }
            }
        };
        mEditGradeBreakdownClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        mTotalPercentageTextView = (TextView) view.findViewById(R.id.total_percentage_text_view);

        populateClassInfo();

        ClassActivityFragment rf = (ClassActivityFragment) getFragmentManager().findFragmentByTag(ClassActivityFragment.TAG);
        if(rf != null) {
            rf.getClassSchedules(mClass, getTag());
            rf.getGradeBreakdowns(mClass, getTag());
        }

        return view;
    }

    public void updateClass(ClassContract.ClassEntry _class, int rowsUpdated) {
        mClass = _class;
        populateClassInfo();
    }

    private void populateClassInfo() {
        AcademicTermContract.AcademicTermEntry academicTerm = mClass.getAcademicTerm();
        if(academicTerm != null)
            mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(academicTerm.getColor(), getContext()));
        else
            mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(null, getContext()));
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

    public void populateClassSchedules(ArrayList<ClassScheduleContract.ClassScheduleEntry> classSchedules, String fragmentTag) {
        mScheduleList = classSchedules;
        mScheduleLinearLayout.removeAllViews();
        for(ClassScheduleContract.ClassScheduleEntry schedule : mScheduleList) {
            mScheduleLinearLayout.addView(getScheduleView(getLayoutInflater(null), schedule, mEditScheduleClickListener, mRemoveScheduleClickListener));
        }
    }

    public void populateGradeBreakdowns(ArrayList<ClassGradeBreakdownContract.ClassGradeBreakdownEntry> gradeBreakdowns, String fragmentTag) {
        mGradeBreakdownList = gradeBreakdowns;
        mGradeBreakdownLinearLayout.removeAllViews();
        float totalPercentage = 0f;
        for(ClassGradeBreakdownContract.ClassGradeBreakdownEntry gradeBreakdown : mGradeBreakdownList) {
            mGradeBreakdownLinearLayout.addView(getGradeBreakdownView(getLayoutInflater(null), gradeBreakdown, mEditGradeBreakdownClickListener, mRemoveGradeBreakdownClickListener));
            totalPercentage = totalPercentage + gradeBreakdown.getPercentage();
        }
        mTotalPercentageTextView.setText(String.format("%.2f%%", totalPercentage));
        mTotalPercentageTextView.setTag(totalPercentage);
    }

    public void insertClassSchedule(ClassScheduleContract.ClassScheduleEntry classSchedule, long id, String fragmentTag) {
        if(id != -1) {
            classSchedule.setId(id);
            mScheduleList.add(classSchedule);
            mScheduleLinearLayout.addView(getScheduleView(getActivity().getLayoutInflater(), classSchedule, mEditScheduleClickListener, mRemoveScheduleClickListener));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), getString(R.string.class_schedule_added), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    public void updateClassSchedule(ClassScheduleContract.ClassScheduleEntry classSchedule, int rowsUpdated, String fragmentTag) {
        int position = mScheduleList.indexOf(classSchedule);
        if(position != -1) {
            mScheduleList.set(position, classSchedule);
            View view = mScheduleLinearLayout.getChildAt(position);
            if(view != null)
                _getScheduleView(view, classSchedule, mEditScheduleClickListener, mRemoveScheduleClickListener);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), getString(R.string.class_schedule_updated), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    public void deleteClassSchedule(ClassScheduleContract.ClassScheduleEntry classSchedule, int rowsDeleted, String fragmentTag) {
        int childPosition = mScheduleList.indexOf(classSchedule);
        if(childPosition != -1) {
            mScheduleList.remove(childPosition);
            mScheduleLinearLayout.removeViewAt(childPosition);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), getString(R.string.class_schedule_removed), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    public void deleteClassGradeBreakdown(ClassGradeBreakdownContract.ClassGradeBreakdownEntry classGradeBreakdown, int rowsDeleted, String fragmentTag) {
        int childPosition = mGradeBreakdownList.indexOf(classGradeBreakdown);
        if(childPosition != -1) {
            mGradeBreakdownList.remove(childPosition);
            mGradeBreakdownLinearLayout.removeViewAt(childPosition);

            float totalPercentage = (float) mTotalPercentageTextView.getTag();
            totalPercentage = totalPercentage - classGradeBreakdown.getPercentage();
            mTotalPercentageTextView.setText(String.format("%.2f%%", totalPercentage));
            mTotalPercentageTextView.setTag(totalPercentage);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), getString(R.string.class_grade_breakdown_removed), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    private View getScheduleView(LayoutInflater inflater, ClassScheduleContract.ClassScheduleEntry schedule, View.OnClickListener editClickListener, View.OnClickListener removeClickListener) {
        return _getScheduleView(inflater.inflate(R.layout.row_class_schedule, null), schedule, editClickListener, removeClickListener);
    }

    private View _getScheduleView(View view, ClassScheduleContract.ClassScheduleEntry schedule, View.OnClickListener editClickListener, View.OnClickListener removeClickListener) {
        String location = schedule.getLocation(), time;
        if(!StringUtils.isBlank(location))
            time = schedule.getTime(getContext()) + "\n" + location;
        else
            time = schedule.getTime(getContext());
        ((TextView) view.findViewById(R.id.time_location_text_view)).setText(time);
        LinearLayout editLinearLayout = (LinearLayout) view.findViewById(R.id.schedule_clickable_layout);
        editLinearLayout.setTag(schedule);
        editLinearLayout.setOnClickListener(editClickListener);
        ImageButton removeButton = (ImageButton) view.findViewById(R.id.remove_button);
        removeButton.setTag(schedule);
        removeButton.setOnClickListener(removeClickListener);
        return view;
    }

    private View getGradeBreakdownView(LayoutInflater inflater, ClassGradeBreakdownContract.ClassGradeBreakdownEntry gradeBreakdown, View.OnClickListener editClickListener, View.OnClickListener removeClickListener) {
        return _getGradeBreakdownView(inflater.inflate(R.layout.row_grade_breakdown, null), gradeBreakdown, editClickListener, removeClickListener);
    }

    private View _getGradeBreakdownView(View view, ClassGradeBreakdownContract.ClassGradeBreakdownEntry gradeBreakdown, View.OnClickListener editClickListener, View.OnClickListener removeClickListener) {
        ClassItemTypeContract.ClassItemTypeEntry itemType = gradeBreakdown.getItemType();
        ((TextView) view.findViewById(R.id.grade_breakdown_text_view)).setText(itemType.getDescription());
        ((TextView) view.findViewById(R.id.percentage_text_view)).setText(String.format("%.2f%%", gradeBreakdown.getPercentage()));
        LinearLayout backgroundLayout = (LinearLayout) view.findViewById(R.id.grade_breakdown_background_layout);
        backgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(itemType.getColor(), getContext()));
        LinearLayout editLinearLayout = (LinearLayout) view.findViewById(R.id.grade_breakdown_clickable_layout);
        editLinearLayout.setTag(gradeBreakdown);
        editLinearLayout.setOnClickListener(editClickListener);
        ImageButton removeButton = (ImageButton) view.findViewById(R.id.remove_button);
        removeButton.setTag(gradeBreakdown);
        removeButton.setOnClickListener(removeClickListener);
        return view;
    }
}
