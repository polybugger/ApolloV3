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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import net.polybugger.apollot.db.DateTimeFormat;

import org.apache.commons.lang3.StringUtils;

public class ClassItemRecordsFragment extends Fragment {

    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";
    public static final String CLASS_ITEM_ARG = "net.polybugger.apollot.class_item_arg";

    public static boolean REQUERY = false;

    private ClassContract.ClassEntry mClass;
    private ClassItemContract.ClassItemEntry mClassItem;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private MenuItem mAttendanceMenuItem;
    private MenuItem mScoreMenuItem;
    private MenuItem mSubmissionDateMenuItem;

    public static ClassItemRecordsFragment newInstance(ClassContract.ClassEntry _class, ClassItemContract.ClassItemEntry classItem) {
        ClassItemRecordsFragment f = new ClassItemRecordsFragment();
        Bundle args = new Bundle();
        args.putSerializable(CLASS_ARG, _class);
        args.putSerializable(CLASS_ITEM_ARG, classItem);
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
        mClassItem = (ClassItemContract.ClassItemEntry) args.getSerializable(CLASS_ITEM_ARG);

        View view = inflater.inflate(R.layout.fragment_class_item_records, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new Adapter(this, mClassItem);
        mRecyclerView.setAdapter(mAdapter);

        ClassItemActivityFragment rf = (ClassItemActivityFragment) getFragmentManager().findFragmentByTag(ClassItemActivityFragment.TAG);
        if(rf != null)
            rf.getClassItemRecords(mClassItem, getTag());

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
            case R.id.action_sort_last_name:
                mAdapter.sortBy(R.id.action_sort_last_name);
                return true;
            case R.id.action_sort_first_name:
                mAdapter.sortBy(R.id.action_sort_first_name);
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
        inflater.inflate(R.menu.fragment_class_item_records, menu);
        mAttendanceMenuItem = menu.findItem(R.id.action_sort_attendance);
        mScoreMenuItem = menu.findItem(R.id.action_sort_score);
        mSubmissionDateMenuItem = menu.findItem(R.id.action_sort_submission_date);

        mAttendanceMenuItem.setVisible(mClassItem.isCheckAttendance());
        mScoreMenuItem.setVisible(mClassItem.isRecordScores());
        mSubmissionDateMenuItem.setVisible(mClassItem.isRecordSubmissions());
    }

    public void onGetClassItemRecords(ArrayList<ClassItemRecordsFragment.ClassItemRecordSummary> arrayList, String fragmentTag) {
        mAdapter.setArrayList(arrayList);
    }

    public void updateClassItem(ClassItemContract.ClassItemEntry classItem) {
        mClassItem = classItem;
        mAdapter.setClassItem(classItem);
        mAttendanceMenuItem.setVisible(mClassItem.isCheckAttendance());
        mScoreMenuItem.setVisible(mClassItem.isRecordScores());
        mSubmissionDateMenuItem.setVisible(mClassItem.isRecordSubmissions());
    }

    public void onInsertClassItemRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, long id, String fragmentTag) {
        if(id != -1) {
            classItemRecord.setId(id);
            ClassItemRecordSummary classItemRecordSummary = new ClassItemRecordSummary(classItemRecord);
            mAdapter.update(classItemRecordSummary);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), getString(R.string.class_item_record_updated), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }

    }

    public void onUpdateClassItemRecord(ClassItemRecordContract.ClassItemRecordEntry classItemRecord, ClassItemContract.ClassItemEntry classItem, int rowsUpdated, String fragmentTag) {
        ClassItemRecordSummary classItemRecordSummary = new ClassItemRecordSummary(classItemRecord);
        mAdapter.update(classItemRecordSummary);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), getString(R.string.class_item_record_updated), Snackbar.LENGTH_SHORT).show();
            }
        }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
    }

    public static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private Fragment mFragment;
        private ClassItemContract.ClassItemEntry mClassItem;
        private ArrayList<ClassItemRecordSummary> mArrayList;

        private int mSortId;

        private Comparator<ClassItemRecordSummary> mComparator;

        public Adapter(Fragment fragment, ClassItemContract.ClassItemEntry classItem) {
            mFragment = fragment;
            mClassItem = classItem;
            mArrayList = new ArrayList<>();
            mComparator = new Comparator<ClassItemRecordSummary>() {
                @Override
                public int compare(ClassItemRecordSummary lhs, ClassItemRecordSummary rhs) {
                    if(mSortId == R.id.action_sort_last_name) {
                        return lhs.mClassItemRecord.getClassStudent().getStudent().getLastName().compareToIgnoreCase(rhs.mClassItemRecord.getClassStudent().getStudent().getLastName());
                    }
                    else if(-mSortId == R.id.action_sort_last_name) {
                        return -lhs.mClassItemRecord.getClassStudent().getStudent().getLastName().compareToIgnoreCase(rhs.mClassItemRecord.getClassStudent().getStudent().getLastName());
                    }
                    else if(mSortId == R.id.action_sort_first_name) {
                        return lhs.mClassItemRecord.getClassStudent().getStudent().getFirstName().compareToIgnoreCase(rhs.mClassItemRecord.getClassStudent().getStudent().getFirstName());
                    }
                    else if(-mSortId == R.id.action_sort_first_name) {
                        return -lhs.mClassItemRecord.getClassStudent().getStudent().getFirstName().compareToIgnoreCase(rhs.mClassItemRecord.getClassStudent().getStudent().getFirstName());
                    }
                    else if(mSortId == R.id.action_sort_attendance) {
                        boolean lb = lhs.mClassItemRecord.getAttendance();
                        boolean rb = rhs.mClassItemRecord.getAttendance();
                        return (lb ? -1 : rb ? 1 : 0);
                    }
                    else if(-mSortId == R.id.action_sort_attendance) {
                        boolean lb = lhs.mClassItemRecord.getAttendance();
                        boolean rb = rhs.mClassItemRecord.getAttendance();
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

        public void setArrayList(ArrayList<ClassItemRecordSummary> arrayList) {
            mArrayList = arrayList;
            notifyDataSetChanged();
        }

        public void setClassItem(ClassItemContract.ClassItemEntry classItem) {
            mClassItem = classItem;
            notifyDataSetChanged();
        }

        public void updateSubmissionDate(Date submissionDate, int position) {
            mArrayList.get(position).mClassItemRecord.setSubmissionDate(submissionDate);
            notifyDataSetChanged();
        }

        public void update(ClassItemRecordSummary classItemRecordSummary) {

            ClassItemRecordSummary tmpClassItemRecordSummary;
            int size = mArrayList.size(), pos = size;
            for(int i = 0; i < size; ++i) {
                tmpClassItemRecordSummary = mArrayList.get(i);
                if(tmpClassItemRecordSummary.mClassItemRecord.getClassStudent().equals(classItemRecordSummary.mClassItemRecord.getClassStudent())) {
                    pos = i;
                    break;
                }
            }
            if(pos < size)
                mArrayList.remove(pos);
            mArrayList.add(pos, classItemRecordSummary);
            notifyDataSetChanged();
        }

        public void sortBy(int sortId) {
            mSortId = (mSortId == sortId) ? -sortId : sortId;
            Collections.sort(mArrayList, mComparator);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_class_item_record, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            ClassItemRecordSummary entry = mArrayList.get(position);

            ClassItemTypeContract.ClassItemTypeEntry itemType = mClassItem.getItemType();
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
                    ClassItemRecordSummary classItemSummary = (ClassItemRecordSummary) v.getTag();
                    FragmentManager fm = mFragment.getFragmentManager();
                    ClassItemRecordInsertUpdateDialogFragment df = (ClassItemRecordInsertUpdateDialogFragment) fm.findFragmentByTag(ClassItemRecordInsertUpdateDialogFragment.TAG);
                    if(df == null) {
                        df = ClassItemRecordInsertUpdateDialogFragment.newInstance(classItemSummary.mClassItemRecord, mClassItem, mFragment.getString(R.string.update_class_item_record), mFragment.getString(R.string.save_changes), mFragment.getTag());
                        df.show(fm, ClassItemRecordInsertUpdateDialogFragment.TAG);
                    }
                }
            });

            holder.mNameTextView.setText(entry.mClassItemRecord.getClassStudent().getStudent().getName(mFragment.getContext()));

            if(mClassItem.isCheckAttendance()) {
                holder.mAttendanceLinearLayout.setVisibility(View.VISIBLE);
                Boolean attendance = entry.mClassItemRecord.getAttendance();
                if(attendance != null)
                    holder.mAttendanceTextView.setText(attendance ? mFragment.getString(R.string.attendance_present) : mFragment.getString(R.string.attendance_absent));
                else
                    holder.mAttendanceTextView.setText(mFragment.getString(R.string.dash_string));
            }
            else
                holder.mAttendanceLinearLayout.setVisibility(View.GONE);

            if(mClassItem.isRecordScores()) {
                holder.mScoreLinearLayout.setVisibility(View.VISIBLE);
                Float score = entry.mClassItemRecord.getScore();
                if(score != null) {
                    holder.mScoreTextView.setText(String.format("%.2f", score));
                    holder.mSlashSeparatorTextView.setVisibility(View.VISIBLE);
                    holder.mPerfectScoreTextView.setVisibility(View.VISIBLE);
                    holder.mPerfectScoreTextView.setText(String.format("%.2f", mClassItem.getPerfectScore()));
                }
                else {
                    holder.mScoreTextView.setText(mFragment.getString(R.string.dash_string));
                    holder.mSlashSeparatorTextView.setVisibility(View.GONE);
                    holder.mPerfectScoreTextView.setVisibility(View.GONE);
                }
            }
            else
                holder.mScoreLinearLayout.setVisibility(View.GONE);

            SimpleDateFormat sdf;
            if(StringUtils.equalsIgnoreCase(mFragment.getResources().getConfiguration().locale.getLanguage(), ApolloDbAdapter.JA_LANGUAGE))
                sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE_JA, mFragment.getResources().getConfiguration().locale);
            else
                sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE, mFragment.getResources().getConfiguration().locale);

            if(mClassItem.isRecordSubmissions()) {
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
            int paddingTop = holder.mNameTextView.getPaddingTop();
            int paddingRight = holder.mNameTextView.getPaddingRight();
            int paddingLeft = holder.mNameTextView.getPaddingLeft();
            if(holder.mRemarksTextView.getVisibility() == View.GONE) {
                if(holder.mSubmissionDateLinearLayout.getVisibility() == View.VISIBLE) {
                    holder.mSubmissionDateLinearLayout.setPadding(paddingLeft, 0, paddingRight, paddingTop);
                    holder.mScoreLinearLayout.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mAttendanceLinearLayout.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mNameTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
                }
                else if(holder.mScoreLinearLayout.getVisibility() == View.VISIBLE) {
                    holder.mScoreLinearLayout.setPadding(paddingLeft, 0, paddingRight, paddingTop);
                    holder.mAttendanceLinearLayout.setPadding(paddingLeft, 0, paddingRight, 0);
                    holder.mNameTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
                }
                else if(holder.mAttendanceLinearLayout.getVisibility() == View.VISIBLE) {
                    holder.mAttendanceLinearLayout.setPadding(paddingLeft, 0, paddingRight, paddingTop);
                    holder.mNameTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
                }
                else
                    holder.mNameTextView.setPadding(paddingLeft, paddingTop, paddingRight, paddingTop);
            }
            else {
                holder.mSubmissionDateLinearLayout.setPadding(paddingLeft, 0, paddingRight, 0);
                holder.mScoreLinearLayout.setPadding(paddingLeft, 0, paddingRight, 0);
                holder.mAttendanceLinearLayout.setPadding(paddingLeft, 0, paddingRight, 0);
                holder.mNameTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
            }
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            protected LinearLayout mBackgroundLayout;
            protected LinearLayout mClickableLayout;
            protected TextView mNameTextView;
            protected LinearLayout mAttendanceLinearLayout;
            protected TextView mAttendanceTextView;
            protected LinearLayout mScoreLinearLayout;
            protected TextView mScoreTextView;
            protected TextView mSlashSeparatorTextView;
            protected TextView mPerfectScoreTextView;
            protected LinearLayout mSubmissionDateLinearLayout;
            protected TextView mSubmissionDateTextView;
            protected TextView mRemarksTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mBackgroundLayout = (LinearLayout) itemView.findViewById(R.id.background_layout);
                mClickableLayout = (LinearLayout) itemView.findViewById(R.id.clickable_layout);
                mNameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
                mAttendanceLinearLayout = (LinearLayout) itemView.findViewById(R.id.attendance_linear_layout);
                mAttendanceTextView = (TextView) itemView.findViewById(R.id.attendance_text_view);
                mScoreLinearLayout = (LinearLayout) itemView.findViewById(R.id.score_linear_layout);
                mScoreTextView = (TextView) itemView.findViewById(R.id.score_text_view);
                mSlashSeparatorTextView = (TextView) itemView.findViewById(R.id.slash_separator_text_view);
                mPerfectScoreTextView = (TextView) itemView.findViewById(R.id.perfect_score_text_view);
                mSubmissionDateLinearLayout = (LinearLayout) itemView.findViewById(R.id.submission_date_linear_layout);
                mSubmissionDateTextView = (TextView) itemView.findViewById(R.id.submission_date_text_view);
                mRemarksTextView = (TextView) itemView.findViewById(R.id.remarks_text_view);
            }
        }
    }

    public static class ClassItemRecordSummary {
        public ClassItemRecordContract.ClassItemRecordEntry mClassItemRecord;

        public ClassItemRecordSummary(ClassItemRecordContract.ClassItemRecordEntry classItemRecord) {
            mClassItemRecord = classItemRecord;
        }
    }
}
