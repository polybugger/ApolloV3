package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassStudentContract;
import net.polybugger.apollot.db.GenderEnum;
import net.polybugger.apollot.db.StudentContract;

import org.apache.commons.lang3.StringUtils;

public class ClassStudentInfoFragment extends Fragment {

    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";
    public static final String CLASS_STUDENT_ARG = "net.polybugger.apollot.class_student_arg";

    private ClassContract.ClassEntry mClass;
    private ClassStudentContract.ClassStudentEntry mClassStudent;

    private TextView mNameTextView;
    private TextView mGenderTextView;
    private TextView mEmailAddressTextView;
    private TextView mContactNumberTextView;

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
            }
        });

        populateClassStudentInfo();

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
}
