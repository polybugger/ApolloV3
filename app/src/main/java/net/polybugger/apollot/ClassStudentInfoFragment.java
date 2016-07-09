package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import net.polybugger.apollot.db.AToFCalculation;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassGradeBreakdownContract;
import net.polybugger.apollot.db.ClassItemTypeContract;
import net.polybugger.apollot.db.ClassStudentContract;
import net.polybugger.apollot.db.FourToOneCalculation;
import net.polybugger.apollot.db.GenderEnum;
import net.polybugger.apollot.db.OneToFiveCalculation;
import net.polybugger.apollot.db.StudentContract;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ClassStudentInfoFragment extends Fragment {

    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";
    public static final String CLASS_STUDENT_ARG = "net.polybugger.apollot.class_student_arg";

    public static boolean REQUERY = false;

    private ClassContract.ClassEntry mClass;
    private ClassStudentContract.ClassStudentEntry mClassStudent;

    private TextView mNameTextView;
    private TextView mGenderTextView;
    private TextView mEmailAddressTextView;
    private TextView mContactNumberTextView;

    private LinearLayout mSummaryLinearLayout;
    private Float mTotalPercentage;
    private TextView mTotalPercentageTextView;
    private TextView mOneToFiveFinalGradeTextView;
    private LinearLayout mOneToFiveLinearLayout;
    private TextView mAToFFinalGradeTextView;
    private LinearLayout mAToFLinearLayout;
    private TextView mFourToOneFinalGradeTextView;
    private LinearLayout mFourToOneLinearLayout;

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
        mNameTextView = (TextView) view.findViewById(R.id.name_text_view);
        mGenderTextView = (TextView) view.findViewById(R.id.gender_text_view);
        mEmailAddressTextView = (TextView) view.findViewById(R.id.email_address_text_view);
        mContactNumberTextView = (TextView) view.findViewById(R.id.contact_number_text_view);
        view.findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ClassStudentInsertUpdateDialogFragment df = (ClassStudentInsertUpdateDialogFragment) fm.findFragmentByTag(ClassStudentInsertUpdateDialogFragment.TAG);
                if(df == null) {
                    df = ClassStudentInsertUpdateDialogFragment.newInstance(mClassStudent, getString(R.string.update_class_student), getString(R.string.save_changes), getTag());
                    df.show(fm, ClassStudentInsertUpdateDialogFragment.TAG);
                }
            }
        });

        mSummaryLinearLayout = (LinearLayout) view.findViewById(R.id.summary_linear_layout);
        mTotalPercentageTextView = (TextView) view.findViewById(R.id.total_percentage_text_view);
        mOneToFiveFinalGradeTextView = (TextView) view.findViewById(R.id.one_to_five_final_grade_text_view);
        mOneToFiveLinearLayout = (LinearLayout) view.findViewById(R.id.one_to_five_linear_layout);
        mAToFFinalGradeTextView = (TextView) view.findViewById(R.id.a_to_f_final_grade_text_view);
        mAToFLinearLayout = (LinearLayout) view.findViewById(R.id.a_to_f_linear_layout);
        mFourToOneFinalGradeTextView= (TextView) view.findViewById(R.id.four_to_one_final_grade_text_view);
        mFourToOneLinearLayout = (LinearLayout) view.findViewById(R.id.four_to_one_linear_layout);

        populateClassStudentInfo();

        ClassStudentActivityFragment rf = (ClassStudentActivityFragment) getFragmentManager().findFragmentByTag(ClassStudentActivityFragment.TAG);
        if(rf != null)
            rf.getClassItemSubTotalSummary(mClassStudent, getTag());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(REQUERY) {
            ClassStudentActivityFragment rf = (ClassStudentActivityFragment) getFragmentManager().findFragmentByTag(ClassStudentActivityFragment.TAG);
            if(rf != null)
                rf.getClassItemSubTotalSummary(mClassStudent, getTag());
            REQUERY = false;
        }
    }

    private void populateClassStudentInfo() {
        Context context = getContext();
        StudentContract.StudentEntry student = mClassStudent.getStudent();
        mNameTextView.setText(student.getName(context));

        GenderEnum gender = student.getGender();
        if(gender == null)
            mGenderTextView.setVisibility(View.GONE);
        else {
            mGenderTextView.setText(GenderEnum.intToString(gender.getValue(), context));
            mGenderTextView.setVisibility(View.VISIBLE);
        }

        String emailAddress = student.getEmailAddress();
        if(StringUtils.isBlank(emailAddress))
            mEmailAddressTextView.setVisibility(View.GONE);
        else {
            mEmailAddressTextView.setText(emailAddress);
            mEmailAddressTextView.setVisibility(View.VISIBLE);
        }

        String contactNumber = student.getContactNumber();
        if(StringUtils.isBlank(contactNumber))
            mContactNumberTextView.setVisibility(View.GONE);
        else {
            mContactNumberTextView.setText(contactNumber);
            mContactNumberTextView.setVisibility(View.VISIBLE);
        }

        int paddingTop = mNameTextView.getPaddingTop();
        int paddingRight = mNameTextView.getPaddingRight();
        int paddingLeft = mNameTextView.getPaddingLeft();
        if(mContactNumberTextView.getVisibility() == View.VISIBLE) {
            mContactNumberTextView.setPadding(paddingLeft, 0, paddingRight, paddingTop);
            mEmailAddressTextView.setPadding(paddingLeft, 0, paddingRight, 0);
            mGenderTextView.setPadding(paddingLeft, 0, paddingRight, 0);
            mNameTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
        }
        else if(mEmailAddressTextView.getVisibility() == View.VISIBLE) {
            mEmailAddressTextView.setPadding(paddingLeft, 0, paddingRight, paddingTop);
            mGenderTextView.setPadding(paddingLeft, 0, paddingRight, 0);
            mNameTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
        }
        else if(mGenderTextView.getVisibility() == View.VISIBLE) {
            mGenderTextView.setPadding(paddingLeft, 0, paddingRight, paddingTop);
            mNameTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
        }
        else
            mNameTextView.setPadding(paddingLeft, paddingTop, paddingRight, paddingTop);
    }

    public void updateClassStudent(ClassStudentContract.ClassStudentEntry classStudent) {
        mClassStudent = classStudent;
        populateClassStudentInfo();
    }

    public void populateClassItemSubTotalSummaries(ArrayList<ClassItemSubTotalSummary> classItemSubTotalSummaries) {
        mSummaryLinearLayout.removeAllViews();
        mTotalPercentage = Float.valueOf(0);
        for(ClassItemSubTotalSummary classItemSubTotalSummary : classItemSubTotalSummaries) {
            mSummaryLinearLayout.addView(getClassItemSubTotalSummaryView(getLayoutInflater(null), classItemSubTotalSummary));
        }
        mTotalPercentageTextView.setText(String.format("%s %.2f%%", getString(R.string.total_label), mTotalPercentage));

        OneToFiveCalculation oneToFiveCalculation = new OneToFiveCalculation(getContext());
        if(oneToFiveCalculation.isSet()) {
            StringBuilder oneToFiveFinalGrade = new StringBuilder("");
            float oneToFiveGrade = oneToFiveCalculation.calculateFinalGrade(mTotalPercentage);
            if(oneToFiveGrade > oneToFiveCalculation.getPassingGradeMark()) {
                oneToFiveFinalGrade.append(String.format("%.2f", 5.0));
                oneToFiveFinalGrade.append(" (");
                oneToFiveFinalGrade.append(String.format("%.2f", oneToFiveGrade));
                oneToFiveFinalGrade.append(")");
            }
            else {
                oneToFiveFinalGrade.append(String.format("%.2f", oneToFiveGrade));
            }
            mOneToFiveFinalGradeTextView.setText(oneToFiveFinalGrade.toString());
            mOneToFiveLinearLayout.setVisibility(View.VISIBLE);
        }
        else {
            mOneToFiveLinearLayout.setVisibility(View.GONE);
        }

        AToFCalculation aToFCalculation = new AToFCalculation(getContext());
        if(aToFCalculation.isSet()) {
            mAToFLinearLayout.setVisibility(View.VISIBLE);
            mAToFFinalGradeTextView.setText(aToFCalculation.getFinalGrade(mTotalPercentage));
        }
        else {
            mAToFLinearLayout.setVisibility(View.GONE);
        }

        FourToOneCalculation fourToOneCalculation = new FourToOneCalculation(getContext());
        if(fourToOneCalculation.isSet()) {
            StringBuilder fourToOneFinalGrade = new StringBuilder("");
            float fourToOneGrade = fourToOneCalculation.calculateFinalGrade(mTotalPercentage);
            if(fourToOneGrade < fourToOneCalculation.getPassingGradeMark()) {
                fourToOneFinalGrade.append(String.format("%.2f", 0.0));
                fourToOneFinalGrade.append(" (");
                fourToOneFinalGrade.append(String.format("%.2f", fourToOneGrade));
                fourToOneFinalGrade.append(")");
            }
            else {
                fourToOneFinalGrade.append(String.format("%.2f", fourToOneGrade));
            }
            mFourToOneFinalGradeTextView.setText(fourToOneFinalGrade.toString());
            mFourToOneLinearLayout.setVisibility(View.VISIBLE);
        }
        else {
            mFourToOneLinearLayout.setVisibility(View.GONE);
        }
    }

    private View getClassItemSubTotalSummaryView(LayoutInflater inflater, ClassItemSubTotalSummary classItemSubTotalSummary) {
        return _getClassItemSubTotalSummaryView(inflater.inflate(R.layout.row_class_item_sub_total_summary, null), classItemSubTotalSummary);
    }

    private View _getClassItemSubTotalSummaryView(View view, ClassItemSubTotalSummary classItemSubTotalSummary) {
        ClassItemTypeContract.ClassItemTypeEntry itemType = classItemSubTotalSummary.mClassGradeBreakdown.getItemType();
        ((TextView) view.findViewById(R.id.grade_breakdown_text_view)).setText(itemType.getDescription());
        LinearLayout backgroundLayout = (LinearLayout) view.findViewById(R.id.background_layout);
        backgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(itemType.getColor(), getContext()));

        ((TextView) view.findViewById(R.id.count_text_view)).setText(String.format("%s %s", getString(R.string.count_label), classItemSubTotalSummary.mItemTypeCount + "/" + classItemSubTotalSummary.mItemTypeTotal));
        ((TextView) view.findViewById(R.id.attendance_count_text_view)).setText(String.format("%s %s", getString(R.string.attendance_count_label), (classItemSubTotalSummary.mAttendanceCount - classItemSubTotalSummary.mAbsencesCount) + "/" + classItemSubTotalSummary.mAttendanceCount));

        float breakdownPercentage = classItemSubTotalSummary.mClassGradeBreakdown.getPercentage();
        float summaryPercentage = classItemSubTotalSummary.getPercentage() * 100;

        ((TextView) view.findViewById(R.id.grade_breakdown_percentage_text_view)).setText(String.format("%s %.2f%%", getString(R.string.percentage_label), breakdownPercentage));
        ((TextView) view.findViewById(R.id.percentage_text_view)).setText(String.format("%.2f%%", summaryPercentage));

        float subTotal = breakdownPercentage / 100 * summaryPercentage;
        mTotalPercentage = mTotalPercentage + subTotal;
        ((TextView) view.findViewById(R.id.subtotal_percentage_text_view)).setText(String.format("%s %.2f%%", getString(R.string.sub_total_label), subTotal));

        return view;
    }

    public static class ClassItemSubTotalSummary {
        public ClassGradeBreakdownContract.ClassGradeBreakdownEntry mClassGradeBreakdown;
        public float mPercentage;
        public int mAttendanceCount;
        public int mAbsencesCount;
        public int mItemTypeCount;
        public int mItemTypeTotal;

        public ClassItemSubTotalSummary(ClassGradeBreakdownContract.ClassGradeBreakdownEntry classGradeBreakdown) {
            mClassGradeBreakdown = classGradeBreakdown;
            mPercentage = 0;
            mItemTypeCount = 0;
            mItemTypeTotal = 0;
            mAttendanceCount = 0;
            mAbsencesCount = 0;
        }

        public void incrementItemTypeCount() {
            mItemTypeCount = mItemTypeCount + 1;
        }
        public void incrementItemTypeTotal() {
            mItemTypeTotal = mItemTypeTotal + 1;
        }
        public void incrementAttendanceCount() {
            mAttendanceCount = mAttendanceCount + 1;
        }
        public void incrementAbsencesCount() {
            mAbsencesCount = mAbsencesCount + 1;
        }
        public void addPercentage(float percentage) {
            mPercentage = mPercentage + percentage;
        }
        public float getPercentage() {
            return mPercentage / mItemTypeTotal;
        }

        @Override
        public boolean equals(Object object) {
            if(!(object instanceof ClassItemSubTotalSummary))
                return false;
            if(object == this)
                return true;
            ClassItemSubTotalSummary entry = (ClassItemSubTotalSummary) object;
            return new EqualsBuilder()
                    .append(mClassGradeBreakdown, entry.mClassGradeBreakdown).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(97, 101)
                    .append(mClassGradeBreakdown).toHashCode();
        }

    }
}
