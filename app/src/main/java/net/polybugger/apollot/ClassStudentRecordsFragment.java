package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassItemContract;
import net.polybugger.apollot.db.ClassItemRecordContract;
import net.polybugger.apollot.db.ClassItemTypeContract;
import net.polybugger.apollot.db.ClassStudentContract;
import net.polybugger.apollot.db.DateTimeFormat;

import org.apache.commons.lang3.StringUtils;

public class ClassStudentRecordsFragment extends Fragment {

    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";
    public static final String CLASS_STUDENT_ARG = "net.polybugger.apollot.class_student_arg";

    public static boolean REQUERY = false;

    private ClassContract.ClassEntry mClass;
    private ClassStudentContract.ClassStudentEntry mClassStudent;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;

    public static ClassStudentRecordsFragment newInstance(ClassContract.ClassEntry _class, ClassStudentContract.ClassStudentEntry classStudent) {
        ClassStudentRecordsFragment f = new ClassStudentRecordsFragment();
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
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        mClass = (ClassContract.ClassEntry) args.getSerializable(CLASS_ARG);
        mClassStudent = (ClassStudentContract.ClassStudentEntry) args.getSerializable(CLASS_STUDENT_ARG);

        View view = inflater.inflate(R.layout.fragment_class_student_records, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new Adapter(this, mClassStudent);
        mRecyclerView.setAdapter(mAdapter);

        ClassStudentActivityFragment rf = (ClassStudentActivityFragment) getFragmentManager().findFragmentByTag(ClassStudentActivityFragment.TAG);
        if(rf != null)
            rf.getClassStudentRecords(mClassStudent, getTag());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO onResume
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_sort_item_description:
                mAdapter.sortBy(R.id.action_sort_item_description);
                return true;
            case R.id.action_sort_item_date:
                mAdapter.sortBy(R.id.action_sort_item_date);
                return true;
            case R.id.action_sort_item_type:
                mAdapter.sortBy(R.id.action_sort_item_type);
                return true;
            case R.id.action_sort_check_attendance:
                mAdapter.sortBy(R.id.action_sort_check_attendance);
                return true;
            case R.id.action_sort_perfect_score:
                mAdapter.sortBy(R.id.action_sort_perfect_score);
                return true;
            case R.id.action_sort_submission_due_date:
                mAdapter.sortBy(R.id.action_sort_submission_due_date);
                return true;
            case R.id.action_sort_attendance:
                mAdapter.sortBy(R.id.action_sort_attendance);
                return true;
            case R.id.action_sort_score:
                mAdapter.sortBy(R.id.action_sort_score);
                return true;
            case R.id.action_sort_submission_date:
                mAdapter.sortBy(R.id.action_sort_submission_date);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_class_student_records, menu);
    }

    public void onGetClassStudentRecords(ArrayList<ClassStudentRecordsFragment.ClassStudentRecordSummary> classStudentRecords, String fragmentTag) {
        mAdapter.setArrayList(classStudentRecords);
    }

    public void onInsertClassStudentRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, long id, String fragmentTag) {
        if(id != -1) {
            classItemRecord.setId(id);
            ClassStudentRecordSummary classItemRecordSummary = new ClassStudentRecordSummary(classItem, classItemRecord);
            mAdapter.update(classItemRecordSummary);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), getString(R.string.class_student_record_updated), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
            // TODO update student info tab
            /*
            ClassItemActivityFragment rf = (ClassItemActivityFragment) getFragmentManager().findFragmentByTag(ClassItemActivityFragment.TAG);
            if(rf != null) {
                rf.getClassItemSummaryInfo(mClassItem, getTag());
                //rf.getGradeBreakdowns(mClass, getTag());
                //rf.getClassNotes(mClass, getTag());
            }
            */
        }

    }

    public void onUpdateClassStudentRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, int rowsUpdated, String fragmentTag) {
        ClassStudentRecordSummary classItemRecordSummary = new ClassStudentRecordSummary(classItem, classItemRecord);
        mAdapter.update(classItemRecordSummary);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), getString(R.string.class_student_record_updated), Snackbar.LENGTH_SHORT).show();
            }
        }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        // TODO update student info tab
        /*
        ClassItemActivityFragment rf = (ClassItemActivityFragment) getFragmentManager().findFragmentByTag(ClassItemActivityFragment.TAG);
        if(rf != null) {
            rf.getClassItemSummaryInfo(mClassItem, getTag());
            //rf.getGradeBreakdowns(mClass, getTag());
            //rf.getClassNotes(mClass, getTag());
        }
        */
    }

    public static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private Fragment mFragment;
        ClassStudentContract.ClassStudentEntry mClassStudent;
        private ArrayList<ClassStudentRecordSummary> mArrayList;

        private int mSortId;

        private Comparator<ClassStudentRecordSummary> mComparator;

        public Adapter(Fragment fragment, ClassStudentContract.ClassStudentEntry classStudent) {
            mFragment = fragment;
            mClassStudent = classStudent;
            mArrayList = new ArrayList<>();
            mComparator = new Comparator<ClassStudentRecordSummary>() {
                @Override
                public int compare(ClassStudentRecordSummary lhs, ClassStudentRecordSummary rhs) {
                    if(mSortId == R.id.action_sort_item_description) {
                        return lhs.mClassItem.getDescription().compareToIgnoreCase(rhs.mClassItem.getDescription());
                    }
                    else if(-mSortId == R.id.action_sort_item_description) {
                        return -lhs.mClassItem.getDescription().compareToIgnoreCase(rhs.mClassItem.getDescription());
                    }
                    else if(mSortId == R.id.action_sort_item_date) {
                        Date lDate = lhs.mClassItem.getItemDate();
                        Date rDate = rhs.mClassItem.getItemDate();
                        if(lDate == null)
                            return 1;
                        if(rDate == null)
                            return -1;
                        Calendar lCal = Calendar.getInstance();
                        lCal.setTime(lDate);
                        Calendar rCal = Calendar.getInstance();
                        rCal.setTime(rDate);
                        return lCal.compareTo(rCal);
                    }
                    else if(-mSortId == R.id.action_sort_item_date) {
                        Date lDate = lhs.mClassItem.getItemDate();
                        Date rDate = rhs.mClassItem.getItemDate();
                        if(lDate == null)
                            return -1;
                        if(rDate == null)
                            return 1;
                        Calendar lCal = Calendar.getInstance();
                        lCal.setTime(lDate);
                        Calendar rCal = Calendar.getInstance();
                        rCal.setTime(rDate);
                        return -lCal.compareTo(rCal);
                    }
                    else if(mSortId == R.id.action_sort_item_type) {
                        ClassItemTypeContract.ClassItemTypeEntry lit = lhs.mClassItem.getItemType();
                        ClassItemTypeContract.ClassItemTypeEntry rit = rhs.mClassItem.getItemType();
                        if(lit == null)
                            return 1;
                        if(rit == null)
                            return -1;
                        return lit.getDescription().compareToIgnoreCase(rit.getDescription());
                    }
                    else if(-mSortId == R.id.action_sort_item_type) {
                        ClassItemTypeContract.ClassItemTypeEntry lit = lhs.mClassItem.getItemType();
                        ClassItemTypeContract.ClassItemTypeEntry rit = rhs.mClassItem.getItemType();
                        if(lit == null)
                            return -1;
                        if(rit == null)
                            return 1;
                        return -lit.getDescription().compareToIgnoreCase(rit.getDescription());
                    }
                    else if(mSortId == R.id.action_sort_check_attendance) {
                        boolean lb = lhs.mClassItem.isCheckAttendance();
                        boolean rb = rhs.mClassItem.isCheckAttendance();
                        return (lb ? -1 : rb ? 1 : 0);
                    }
                    else if(-mSortId == R.id.action_sort_check_attendance) {
                        boolean lb = lhs.mClassItem.isCheckAttendance();
                        boolean rb = rhs.mClassItem.isCheckAttendance();
                        return -(lb ? -1 : rb ? 1 : 0);
                    }
                    else if(mSortId == R.id.action_sort_perfect_score) {
                        Float lps = lhs.mClassItem.getPerfectScore();
                        Float rps = rhs.mClassItem.getPerfectScore();
                        if(lps == null)
                            return 1;
                        if(rps == null)
                            return -1;
                        return (lps < rps ? -1 : 1);
                    }
                    else if(-mSortId == R.id.action_sort_perfect_score) {
                        Float lps = lhs.mClassItem.getPerfectScore();
                        Float rps = rhs.mClassItem.getPerfectScore();
                        if(lps == null)
                            return -1;
                        if(rps == null)
                            return 1;
                        return -(lps < rps ? -1 : 1);
                    }
                    else if(mSortId == R.id.action_sort_submission_due_date) {
                        Date lDate = lhs.mClassItem.getSubmissionDueDate();
                        Date rDate = rhs.mClassItem.getSubmissionDueDate();
                        if(lDate == null)
                            return 1;
                        if(rDate == null)
                            return -1;
                        Calendar lCal = Calendar.getInstance();
                        lCal.setTime(lDate);
                        Calendar rCal = Calendar.getInstance();
                        rCal.setTime(rDate);
                        return lCal.compareTo(rCal);
                    }
                    else if(-mSortId == R.id.action_sort_submission_due_date) {
                        Date lDate = lhs.mClassItem.getSubmissionDueDate();
                        Date rDate = rhs.mClassItem.getSubmissionDueDate();
                        if(lDate == null)
                            return -1;
                        if(rDate == null)
                            return 1;
                        Calendar lCal = Calendar.getInstance();
                        lCal.setTime(lDate);
                        Calendar rCal = Calendar.getInstance();
                        rCal.setTime(rDate);
                        return -lCal.compareTo(rCal);
                    }
                    else if(mSortId == R.id.action_sort_attendance) {
                        Boolean lb = lhs.mClassItemRecord.getAttendance();
                        Boolean rb = rhs.mClassItemRecord.getAttendance();
                        if(lb == null)
                            return 1;
                        if(rb == null)
                            return -1;
                        return (lb ? -1 : rb ? 1 : 0);
                    }
                    else if(-mSortId == R.id.action_sort_attendance) {
                        Boolean lb = lhs.mClassItemRecord.getAttendance();
                        Boolean rb = rhs.mClassItemRecord.getAttendance();
                        if(lb == null)
                            return -1;
                        if(rb == null)
                            return 1;
                        return -(lb ? -1 : rb ? 1 : 0);
                    }
                    else if(mSortId == R.id.action_sort_score) {
                        Float lps = lhs.mClassItemRecord.getScore();
                        Float rps = rhs.mClassItemRecord.getScore();
                        if(lps == null)
                            return 1;
                        if(rps == null)
                            return -1;
                        return (lps < rps ? -1 : 1);
                    }
                    else if(-mSortId == R.id.action_sort_score) {
                        Float lps = lhs.mClassItemRecord.getScore();
                        Float rps = rhs.mClassItemRecord.getScore();
                        if(lps == null)
                            return -1;
                        if(rps == null)
                            return 1;
                        return -(lps < rps ? -1 : 1);
                    }
                    else if(mSortId == R.id.action_sort_submission_date) {
                        Date lDate = lhs.mClassItemRecord.getSubmissionDate();
                        Date rDate = rhs.mClassItemRecord.getSubmissionDate();
                        if(lDate == null)
                            return 1;
                        if(rDate == null)
                            return -1;
                        Calendar lCal = Calendar.getInstance();
                        lCal.setTime(lDate);
                        Calendar rCal = Calendar.getInstance();
                        rCal.setTime(rDate);
                        return lCal.compareTo(rCal);
                    }
                    else if(-mSortId == R.id.action_sort_submission_date) {
                        Date lDate = lhs.mClassItemRecord.getSubmissionDate();
                        Date rDate = rhs.mClassItemRecord.getSubmissionDate();
                        if(lDate == null)
                            return -1;
                        if(rDate == null)
                            return 1;
                        Calendar lCal = Calendar.getInstance();
                        lCal.setTime(lDate);
                        Calendar rCal = Calendar.getInstance();
                        rCal.setTime(rDate);
                        return -lCal.compareTo(rCal);
                    }
                    return 0;
                }
            };
        }

        public void setArrayList(ArrayList<ClassStudentRecordSummary> arrayList) {
            mArrayList = arrayList;
            notifyDataSetChanged();
        }

        public void sortBy(int sortId) {
            mSortId = (mSortId == sortId) ? -sortId : sortId;
            Collections.sort(mArrayList, mComparator);
            notifyDataSetChanged();
        }

        public void update(ClassStudentRecordSummary classStudentRecordSummary) {
            ClassStudentRecordSummary tmpClassItemRecordSummary;
            int size = mArrayList.size(), pos = size;
            for(int i = 0; i < size; ++i) {
                tmpClassItemRecordSummary = mArrayList.get(i);
                if(tmpClassItemRecordSummary.mClassItem.equals(classStudentRecordSummary.mClassItem)) {
                    pos = i;
                    break;
                }
            }
            if(pos < size)
                mArrayList.remove(pos);
            mArrayList.add(pos, classStudentRecordSummary);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_class_student_record, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            ClassStudentRecordSummary entry = mArrayList.get(position);

            ClassItemTypeContract.ClassItemTypeEntry itemType = entry.mClassItem.getItemType();
            if(itemType != null)
                holder.mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(itemType.getColor(), mFragment.getContext()));
            else
                holder.mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(null, mFragment.getContext()));

            Resources res = mFragment.getResources();
            int topMargin = res.getDimensionPixelSize(R.dimen.recycler_view_item_margin_top);
            int rightMargin = res.getDimensionPixelSize(R.dimen.recycler_view_item_margin_right);
            int bottomMargin = res.getDimensionPixelSize(R.dimen.recycler_view_item_margin_bottom);
            int leftMargin = res.getDimensionPixelSize(R.dimen.recycler_view_item_margin_left);
            if(position == 0) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.mBackgroundLayout.getLayoutParams();
                layoutParams.setMargins(leftMargin, topMargin * 2, rightMargin, bottomMargin);
                holder.mBackgroundLayout.setLayoutParams(layoutParams);
            }
            else if(position == (mArrayList.size() - 1)) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.mBackgroundLayout.getLayoutParams();
                layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin * 2);
                holder.mBackgroundLayout.setLayoutParams(layoutParams);
            }
            else {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.mBackgroundLayout.getLayoutParams();
                layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                holder.mBackgroundLayout.setLayoutParams(layoutParams);
            }
            holder.mClickableLayout.setTag(entry);
            holder.mClickableLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClassStudentRecordSummary classStudentRecordSummary = (ClassStudentRecordSummary) v.getTag();
                    FragmentManager fm = mFragment.getFragmentManager();
                    ClassItemRecordInsertUpdateDialogFragment df = (ClassItemRecordInsertUpdateDialogFragment) fm.findFragmentByTag(ClassItemRecordInsertUpdateDialogFragment.TAG);
                    if(df == null) {
                        classStudentRecordSummary.mClassItemRecord.setClassStudent(mClassStudent);
                        df = ClassItemRecordInsertUpdateDialogFragment.newInstance(classStudentRecordSummary.mClassItemRecord, classStudentRecordSummary.mClassItem, mFragment.getString(R.string.update_class_student_record), mFragment.getString(R.string.save_changes), mFragment.getTag());
                        df.show(fm, ClassItemRecordInsertUpdateDialogFragment.TAG);
                    }
                }
            });

            holder.mTitleTextView.setText(entry.mClassItem.getDescription());
            final SimpleDateFormat sdf;
            if(StringUtils.equalsIgnoreCase(res.getConfiguration().locale.getLanguage(), ApolloDbAdapter.JA_LANGUAGE))
                sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE_JA, res.getConfiguration().locale);
            else
                sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE, res.getConfiguration().locale);

            Date itemDate = entry.mClassItem.getItemDate();
            if(itemDate == null)
                holder.mItemDateTextView.setVisibility(View.GONE);
            else {
                holder.mItemDateTextView.setText(sdf.format(itemDate));
                holder.mItemDateTextView.setVisibility(View.VISIBLE);
            }
            if(itemType == null)
                holder.mItemTypeTextView.setVisibility(View.GONE);
            else {
                holder.mItemTypeTextView.setText(itemType.getDescription());
                holder.mItemTypeTextView.setVisibility(View.VISIBLE);
            }
            if(entry.mClassItem.isCheckAttendance())
                holder.mCheckAttendanceTextView.setVisibility(View.VISIBLE);
            else
                holder.mCheckAttendanceTextView.setVisibility(View.GONE);

            if(entry.mClassItem.isRecordScores()) {
                Float perfectScore = entry.mClassItem.getPerfectScore();
                if(perfectScore == null)
                    holder.mPerfectScoreLinearLayout.setVisibility(View.GONE);
                else {
                    holder.mPerfectScoreTextView.setText(String.format("%.2f", perfectScore));
                    holder.mPerfectScoreLinearLayout.setVisibility(View.VISIBLE);
                }
            }
            else
                holder.mPerfectScoreLinearLayout.setVisibility(View.GONE);

            if(entry.mClassItem.isRecordSubmissions()) {
                Date submissionDueDate = entry.mClassItem.getSubmissionDueDate();
                if(submissionDueDate == null)
                    holder.mSubmissionDueDateLinearLayout.setVisibility(View.GONE);
                else {
                    holder.mSubmissionDueDateTextView.setText(sdf.format(submissionDueDate));
                    holder.mSubmissionDueDateLinearLayout.setVisibility(View.VISIBLE);
                }
            }
            else
                holder.mSubmissionDueDateLinearLayout.setVisibility(View.GONE);

            if(entry.mClassItem.isCheckAttendance()) {
                holder.mAttendanceLinearLayout.setVisibility(View.VISIBLE);
                Boolean attendance = entry.mClassItemRecord.getAttendance();
                if(attendance != null)
                    holder.mAttendanceTextView.setText(attendance ? mFragment.getString(R.string.attendance_present) : mFragment.getString(R.string.attendance_absent));
                else
                    holder.mAttendanceTextView.setText(mFragment.getString(R.string.dash_string));
            }
            else
                holder.mAttendanceLinearLayout.setVisibility(View.GONE);

            if(entry.mClassItem.isRecordScores()) {
                holder.mScoreLinearLayout.setVisibility(View.VISIBLE);
                Float score = entry.mClassItemRecord.getScore();
                if(score != null) {
                    holder.mScoreTextView.setText(String.format("%.2f", score));
                    holder.mSlashSeparatorTextView.setVisibility(View.VISIBLE);
                    holder.mPerfectScoreTotalTextView.setVisibility(View.VISIBLE);
                    holder.mPerfectScoreTotalTextView.setText(String.format("%.2f", entry.mClassItem.getPerfectScore()));
                }
                else {
                    holder.mScoreTextView.setText(mFragment.getString(R.string.dash_string));
                    holder.mSlashSeparatorTextView.setVisibility(View.GONE);
                    holder.mPerfectScoreTotalTextView.setVisibility(View.GONE);
                }
            }
            else
                holder.mScoreLinearLayout.setVisibility(View.GONE);

            if(entry.mClassItem.isRecordSubmissions()) {
                holder.mSubmissionDateLinearLayout.setVisibility(View.VISIBLE);
                Date submissionDate = entry.mClassItemRecord.getSubmissionDate();
                if(submissionDate != null)
                    holder.mSubmissionDateTextView.setText(sdf.format(submissionDate));
                else
                    holder.mSubmissionDateTextView.setText(mFragment.getString(R.string.dash_string));
            }
            else
                holder.mSubmissionDateLinearLayout.setVisibility(View.GONE);

            String remarks = entry.mClassItemRecord.getRemarks();
            if(!StringUtils.isBlank(remarks)) {
                holder.mRemarksTextView.setVisibility(View.VISIBLE);
                holder.mRemarksTextView.setText(String.format("%s %s", mFragment.getString(R.string.remarks_label), remarks));
            }
            else
                holder.mRemarksTextView.setVisibility(View.GONE);

            // only set padding if remarks is gone
            int paddingTop = holder.mTitleTextView.getPaddingTop();
            int paddingRight = holder.mTitleTextView.getPaddingRight();
            int paddingLeft = holder.mTitleTextView.getPaddingLeft();
            if(holder.mRemarksTextView.getVisibility() == View.GONE) {
                if(holder.mSubmissionDateLinearLayout.getVisibility() == View.VISIBLE) {
                    holder.mSubmissionDateLinearLayout.setPadding(paddingLeft, 0, paddingRight, paddingTop);
                    holder.mScoreLinearLayout.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mAttendanceLinearLayout.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mSubmissionDueDateLinearLayout.setPadding(0, 0, 0, 0);
                    holder.mPerfectScoreLinearLayout.setPadding(0, 0, 0, 0);
                    holder.mCheckAttendanceTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mItemTypeTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mItemDateTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
                }
                else if(holder.mScoreLinearLayout.getVisibility() == View.VISIBLE) {
                    holder.mScoreLinearLayout.setPadding(paddingLeft, 0, paddingRight, paddingTop);
                    holder.mAttendanceLinearLayout.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mSubmissionDueDateLinearLayout.setPadding(0, 0, 0, 0);
                    holder.mPerfectScoreLinearLayout.setPadding(0, 0, 0, 0);
                    holder.mCheckAttendanceTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mItemTypeTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mItemDateTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
                }
                else if(holder.mAttendanceLinearLayout.getVisibility() == View.VISIBLE) {
                    holder.mAttendanceLinearLayout.setPadding(paddingLeft, 0, paddingRight, paddingTop);
                    holder.mSubmissionDueDateLinearLayout.setPadding(0, 0, 0, 0);
                    holder.mPerfectScoreLinearLayout.setPadding(0, 0, 0, 0);
                    holder.mCheckAttendanceTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mItemTypeTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mItemDateTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
                }
                else if(holder.mSubmissionDueDateLinearLayout.getVisibility() == View.VISIBLE) {
                    holder.mSubmissionDueDateLinearLayout.setPadding(0, 0, 0, paddingTop);
                    holder.mPerfectScoreLinearLayout.setPadding(0, 0, 0, 0);
                    holder.mCheckAttendanceTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mItemTypeTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mItemDateTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
                }
                else if(holder.mPerfectScoreLinearLayout.getVisibility() == View.VISIBLE) {
                    holder.mPerfectScoreLinearLayout.setPadding(0, 0, 0, paddingTop);
                    holder.mCheckAttendanceTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mItemTypeTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mItemDateTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
                }
                else if(holder.mCheckAttendanceTextView.getVisibility() == View.VISIBLE) {
                    holder.mCheckAttendanceTextView.setPadding(paddingLeft, 0, paddingRight, paddingTop);
                    holder.mItemTypeTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mItemDateTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
                }
                else if(holder.mItemTypeTextView.getVisibility() == View.VISIBLE) {
                    holder.mItemTypeTextView.setPadding(paddingLeft, 0, paddingRight, paddingTop);
                    holder.mItemDateTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
                }
                else if(holder.mItemDateTextView.getVisibility() == View.VISIBLE) {
                    holder.mItemDateTextView.setPadding(paddingLeft, 0, paddingRight, paddingTop);
                    holder.mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
                }
                else
                    holder.mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, paddingTop);
            }
            else {
                holder.mSubmissionDateLinearLayout.setPadding(paddingLeft, 0, paddingRight, 0);
                holder.mScoreLinearLayout.setPadding(paddingLeft, 0, paddingRight, 0);
                holder.mAttendanceLinearLayout.setPadding(paddingLeft, 0, paddingRight, 0);
                holder.mSubmissionDueDateTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                holder.mPerfectScoreTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                holder.mCheckAttendanceTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                holder.mItemTypeTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                holder.mItemDateTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                holder.mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
            }
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            protected LinearLayout mBackgroundLayout;
            protected LinearLayout mClickableLayout;
            protected TextView mTitleTextView;
            protected TextView mItemDateTextView;
            protected TextView mItemTypeTextView;
            protected TextView mCheckAttendanceTextView;
            protected LinearLayout mPerfectScoreLinearLayout;
            protected TextView mPerfectScoreTextView;
            protected LinearLayout mSubmissionDueDateLinearLayout;
            protected TextView mSubmissionDueDateTextView;
            protected LinearLayout mAttendanceLinearLayout;
            protected TextView mAttendanceTextView;
            protected LinearLayout mScoreLinearLayout;
            protected TextView mScoreTextView;
            protected TextView mSlashSeparatorTextView;
            protected TextView mPerfectScoreTotalTextView;
            protected LinearLayout mSubmissionDateLinearLayout;
            protected TextView mSubmissionDateTextView;
            protected TextView mRemarksTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mBackgroundLayout = (LinearLayout) itemView.findViewById(R.id.background_layout);
                mClickableLayout = (LinearLayout) itemView.findViewById(R.id.clickable_layout);
                mTitleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
                mItemDateTextView = (TextView) itemView.findViewById(R.id.item_date_text_view);
                mItemTypeTextView = (TextView) itemView.findViewById(R.id.item_type_text_view);
                mCheckAttendanceTextView = (TextView) itemView.findViewById(R.id.check_attendance_text_view);
                mPerfectScoreLinearLayout = (LinearLayout) itemView.findViewById(R.id.perfect_score_linear_layout);
                mPerfectScoreTextView = (TextView) itemView.findViewById(R.id.perfect_score_text_view);
                mSubmissionDueDateLinearLayout = (LinearLayout) itemView.findViewById(R.id.submission_due_date_linear_layout);
                mSubmissionDueDateTextView = (TextView) itemView.findViewById(R.id.submission_due_date_text_view);
                mAttendanceLinearLayout = (LinearLayout) itemView.findViewById(R.id.attendance_linear_layout);
                mAttendanceTextView = (TextView) itemView.findViewById(R.id.attendance_text_view);
                mScoreLinearLayout = (LinearLayout) itemView.findViewById(R.id.score_linear_layout);
                mScoreTextView = (TextView) itemView.findViewById(R.id.score_text_view);
                mSlashSeparatorTextView = (TextView) itemView.findViewById(R.id.slash_separator_text_view);
                mPerfectScoreTotalTextView = (TextView) itemView.findViewById(R.id.perfect_score_total_text_view);
                mSubmissionDateLinearLayout = (LinearLayout) itemView.findViewById(R.id.submission_date_linear_layout);
                mSubmissionDateTextView = (TextView) itemView.findViewById(R.id.submission_date_text_view);
                mRemarksTextView = (TextView) itemView.findViewById(R.id.remarks_text_view);
            }
        }
    }

    public static class ClassStudentRecordSummary {
        public ClassItemContract.ClassItemEntry mClassItem;
        public ClassItemRecordContract.ClassItemRecordEntry mClassItemRecord;

        public ClassStudentRecordSummary(ClassItemContract.ClassItemEntry classItem, ClassItemRecordContract.ClassItemRecordEntry classItemRecord) {
            mClassItem = classItem;
            mClassItemRecord = classItemRecord;
        }
    }
}
