package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.polybugger.apollot.db.AcademicTermContract;
import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassItemContract;
import net.polybugger.apollot.db.ClassItemNoteContract;
import net.polybugger.apollot.db.ClassItemTypeContract;
import net.polybugger.apollot.db.ClassNoteContract;
import net.polybugger.apollot.db.ClassScheduleContract;
import net.polybugger.apollot.db.DateTimeFormat;
import net.polybugger.apollot.db.PastCurrentEnum;

import org.apache.commons.lang3.StringUtils;

public class ClassItemInfoFragment extends Fragment {

    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";
    public static final String CLASS_ITEM_ARG = "net.polybugger.apollot.class_item_arg";

    private ClassContract.ClassEntry mClass;
    private ClassItemContract.ClassItemEntry mClassItem;

    private NestedScrollView mNestedScrollView;
    private LinearLayout mBackgroundLayout;
    private TextView mTitleTextView;
    private TextView mItemDateTextView;
    private TextView mItemTypeTextView;
    private TextView mCheckAttendanceTextView;
    private TextView mPerfectScoreTextView;
    private TextView mSubmissionDueDateTextView;

    private TextView mPresentSummaryTextView;
    private TextView mAbsentSummaryTextView;
    private TextView mUncheckedAttendanceSummaryTextView;
    private TextView mRecordedScoreSummaryTextView;
    private TextView mUnrecordedScoreSummaryTextView;
    private TextView mRecordedSubmissionSummaryTextView;
    private TextView mUnrecordedSubmissionSummaryTextView;

    private LinearLayout mClassItemNoteLinearLayout;
    private ArrayList<ClassItemNoteContract.ClassItemNoteEntry> mClassItemNoteList;
    private View.OnClickListener mEditClassItemNoteClickListener;
    private View.OnClickListener mRemoveClassItemNoteClickListener;

    public static ClassItemInfoFragment newInstance(ClassContract.ClassEntry _class, ClassItemContract.ClassItemEntry classItem) {
        ClassItemInfoFragment f = new ClassItemInfoFragment();
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
        Bundle args = getArguments();
        mClass = (ClassContract.ClassEntry) args.getSerializable(CLASS_ARG);
        mClassItem = (ClassItemContract.ClassItemEntry) args.getSerializable(CLASS_ITEM_ARG);

        View view = inflater.inflate(R.layout.fragment_class_item_info, container, false);
        mNestedScrollView = (NestedScrollView) view.findViewById(R.id.nested_scroll_view);
        mBackgroundLayout = (LinearLayout) view.findViewById(R.id.background_layout);
        mTitleTextView = (TextView) view.findViewById(R.id.title_text_view);
        mItemDateTextView = (TextView) view.findViewById(R.id.item_date_text_view);
        mItemTypeTextView = (TextView) view.findViewById(R.id.item_type_text_view);
        mCheckAttendanceTextView = (TextView) view.findViewById(R.id.check_attendance_text_view);
        mPerfectScoreTextView = (TextView) view.findViewById(R.id.perfect_score_text_view);
        mSubmissionDueDateTextView = (TextView) view.findViewById(R.id.submission_due_date_text_view);
        view.findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ClassItemInsertUpdateDialogFragment df = (ClassItemInsertUpdateDialogFragment) fm.findFragmentByTag(ClassItemInsertUpdateDialogFragment.TAG);
                if(df == null) {
                    df = ClassItemInsertUpdateDialogFragment.newInstance(mClassItem, getString(R.string.update_class_item), getString(R.string.save_changes), getTag());
                    df.show(fm, ClassItemInsertUpdateDialogFragment.TAG);
                }
            }
        });

        mPresentSummaryTextView = (TextView) view.findViewById(R.id.present_summary_text_view);
        mAbsentSummaryTextView = (TextView) view.findViewById(R.id.absent_summary_text_view);
        mUncheckedAttendanceSummaryTextView = (TextView) view.findViewById(R.id.unchecked_attendance_summary_text_view);
        mRecordedScoreSummaryTextView = (TextView) view.findViewById(R.id.recorded_score_summary_text_view);
        mUnrecordedScoreSummaryTextView = (TextView) view.findViewById(R.id.unrecorded_score_summary_text_view);
        mRecordedSubmissionSummaryTextView = (TextView) view.findViewById(R.id.recorded_submission_summary_text_view);
        mUnrecordedSubmissionSummaryTextView = (TextView) view.findViewById(R.id.unrecorded_submission_summary_text_view);

        mClassItemNoteLinearLayout = (LinearLayout) view.findViewById(R.id.notes_linear_layout);
        mRemoveClassItemNoteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ClassNoteDeleteDialogFragment df = (ClassNoteDeleteDialogFragment) fm.findFragmentByTag(ClassNoteDeleteDialogFragment.TAG);
                if(df == null) {
                    df = ClassNoteDeleteDialogFragment.newInstance(null, getTag(), (ClassItemNoteContract.ClassItemNoteEntry) v.getTag(), false);
                    df.show(fm, ClassNoteDeleteDialogFragment.TAG);
                }
            }
        };
        mEditClassItemNoteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ClassNoteInsertUpdateDialogFragment df = (ClassNoteInsertUpdateDialogFragment) fm.findFragmentByTag(ClassNoteInsertUpdateDialogFragment.TAG);
                if(df == null) {
                    df = ClassNoteInsertUpdateDialogFragment.newInstance(null, getString(R.string.update_item_note), getString(R.string.save_changes), getTag(), (ClassItemNoteContract.ClassItemNoteEntry) v.getTag(), false);
                    df.show(fm, ClassNoteInsertUpdateDialogFragment.TAG);
                }
            }
        };

        populateClassItemInfo();

        ClassItemActivityFragment rf = (ClassItemActivityFragment) getFragmentManager().findFragmentByTag(ClassItemActivityFragment.TAG);
        if(rf != null) {
            rf.getClassItemSummaryInfo(mClassItem, getTag());
            rf.getClassItemNotes(mClassItem, getTag());
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void updateClassItem(ClassItemContract.ClassItemEntry classItem, int rowsUpdated, String fragmentTag) {
        mClassItem = classItem;
        populateClassItemInfo();
        ClassItemActivityFragment rf = (ClassItemActivityFragment) getFragmentManager().findFragmentByTag(ClassItemActivityFragment.TAG);
        if(rf != null) {
            rf.getClassItemSummaryInfo(mClassItem, getTag());
        }
    }

    public void updateClassItemSummaryInfo(ClassItemSummaryInfo classItemSummaryInfo, String fragmentTag) {
        if(mClassItem.isCheckAttendance()) {
            mPresentSummaryTextView.setVisibility(View.VISIBLE);
            mAbsentSummaryTextView.setVisibility(View.VISIBLE);
            mUncheckedAttendanceSummaryTextView.setVisibility(View.VISIBLE);
            mPresentSummaryTextView.setText(String.format("%s %d", getString(R.string.present_label), classItemSummaryInfo.mPresentAttendance));
            mAbsentSummaryTextView.setText(String.format("%s %d", getString(R.string.absent_label), classItemSummaryInfo.mAbsentAttendance));
            mUncheckedAttendanceSummaryTextView.setText(String.format("%s %d", getString(R.string.unchecked_attendance_label), classItemSummaryInfo.mTotalAttendance - classItemSummaryInfo.mPresentAttendance - classItemSummaryInfo.mAbsentAttendance));
        }
        else {
            mPresentSummaryTextView.setVisibility(View.GONE);
            mAbsentSummaryTextView.setVisibility(View.GONE);
            mUncheckedAttendanceSummaryTextView.setVisibility(View.GONE);
        }
        if(mClassItem.isRecordScores()) {
            mRecordedScoreSummaryTextView.setVisibility(View.VISIBLE);
            mUnrecordedScoreSummaryTextView.setVisibility(View.VISIBLE);
            mRecordedScoreSummaryTextView.setText(String.format("%s %d", getString(R.string.recorded_scores_label), classItemSummaryInfo.mRecordedScores));
            mUnrecordedScoreSummaryTextView.setText(String.format("%s %d", getString(R.string.unrecorded_scores_label), classItemSummaryInfo.mTotalScore - classItemSummaryInfo.mRecordedScores));
        }
        else {
            mRecordedScoreSummaryTextView.setVisibility(View.GONE);
            mUnrecordedScoreSummaryTextView.setVisibility(View.GONE);
        }
        if(mClassItem.isRecordSubmissions()) {
            mRecordedSubmissionSummaryTextView.setVisibility(View.VISIBLE);
            mUnrecordedSubmissionSummaryTextView.setVisibility(View.VISIBLE);
            mRecordedSubmissionSummaryTextView.setText(String.format("%s %d", getString(R.string.recorded_submissions_label), classItemSummaryInfo.mRecordedSubmissions));
            mUnrecordedSubmissionSummaryTextView.setText(String.format("%s %d", getString(R.string.unrecorded_submissions_label), classItemSummaryInfo.mTotalSubmission - classItemSummaryInfo.mRecordedSubmissions));
        }
        else {
            mRecordedSubmissionSummaryTextView.setVisibility(View.GONE);
            mUnrecordedSubmissionSummaryTextView.setVisibility(View.GONE);
        }
    }

    private void populateClassItemInfo() {
        ClassItemTypeContract.ClassItemTypeEntry itemType = mClassItem.getItemType();
        if(itemType != null)
            mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(itemType.getColor(), getContext()));
        else
            mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(null, getContext()));

        mTitleTextView.setText(mClassItem.getDescription());

        Resources res = getResources();
        final SimpleDateFormat sdf;
        if(StringUtils.equalsIgnoreCase(res.getConfiguration().locale.getLanguage(), ApolloDbAdapter.JA_LANGUAGE))
            sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE_JA, res.getConfiguration().locale);
        else
            sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE, res.getConfiguration().locale);
        Date itemDate = mClassItem.getItemDate();
        if(itemDate == null)
            mItemDateTextView.setVisibility(View.GONE);
        else {
            mItemDateTextView.setText(sdf.format(itemDate));
            mItemDateTextView.setVisibility(View.VISIBLE);
        }

        if(itemType == null)
            mItemTypeTextView.setVisibility(View.GONE);
        else {
            mItemTypeTextView.setText(itemType.getDescription());
            mItemTypeTextView.setVisibility(View.VISIBLE);
        }

        if(mClassItem.isCheckAttendance())
            mCheckAttendanceTextView.setVisibility(View.VISIBLE);
        else
            mCheckAttendanceTextView.setVisibility(View.GONE);

        if(mClassItem.isRecordScores()) {
            Float perfectScore = mClassItem.getPerfectScore();
            if(perfectScore == null)
                mPerfectScoreTextView.setVisibility(View.GONE);
            else {
                mPerfectScoreTextView.setText(String.format("%s %.2f", getString(R.string.perfect_score_label), perfectScore));
                mPerfectScoreTextView.setVisibility(View.VISIBLE);
            }
        }
        else
            mPerfectScoreTextView.setVisibility(View.GONE);

        if(mClassItem.isRecordSubmissions()) {
            Date submissionDueDate = mClassItem.getSubmissionDueDate();
            if(submissionDueDate == null)
                mSubmissionDueDateTextView.setVisibility(View.GONE);
            else {
                mSubmissionDueDateTextView.setText(String.format("%s %s", getString(R.string.submission_due_date_label), sdf.format(submissionDueDate)));
                mSubmissionDueDateTextView.setVisibility(View.VISIBLE);
            }
        }
        else
            mSubmissionDueDateTextView.setVisibility(View.GONE);

        int paddingTop = mTitleTextView.getPaddingTop();
        int paddingRight = mTitleTextView.getPaddingRight();
        int paddingLeft = mTitleTextView.getPaddingLeft();
        if(mSubmissionDueDateTextView.getVisibility() == View.VISIBLE) {
            mSubmissionDueDateTextView.setPadding(paddingLeft, 0, paddingRight, paddingTop);
            mPerfectScoreTextView.setPadding(paddingLeft, 0, paddingRight, 0);
            mCheckAttendanceTextView.setPadding(paddingLeft, 0, paddingRight, 0);
            mItemTypeTextView.setPadding(paddingLeft, 0, paddingRight, 0);
            mItemDateTextView.setPadding(paddingLeft, 0, paddingRight, 0);
            mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
        }
        else if(mPerfectScoreTextView.getVisibility() == View.VISIBLE) {
            mPerfectScoreTextView.setPadding(paddingLeft, 0, paddingRight, paddingTop);
            mCheckAttendanceTextView.setPadding(paddingLeft, 0, paddingRight, 0);
            mItemTypeTextView.setPadding(paddingLeft, 0, paddingRight, 0);
            mItemDateTextView.setPadding(paddingLeft, 0, paddingRight, 0);
            mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
        }
        else if(mCheckAttendanceTextView.getVisibility() == View.VISIBLE) {
            mCheckAttendanceTextView.setPadding(paddingLeft, 0, paddingRight, paddingTop);
            mItemTypeTextView.setPadding(paddingLeft, 0, paddingRight, 0);
            mItemDateTextView.setPadding(paddingLeft, 0, paddingRight, 0);
            mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
        }
        else if(mItemTypeTextView.getVisibility() == View.VISIBLE) {
            mItemTypeTextView.setPadding(paddingLeft, 0, paddingRight, paddingTop);
            mItemDateTextView.setPadding(paddingLeft, 0, paddingRight, 0);
            mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
        }
        else if(mItemDateTextView.getVisibility() == View.VISIBLE) {
            mItemDateTextView.setPadding(paddingLeft, 0, paddingRight, paddingTop);
            mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
        }
        else
            mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, paddingTop);
    }

    public void populateClassItemNotes(ArrayList<ClassItemNoteContract.ClassItemNoteEntry> classItemNotes, String fragmentTag) {
        mClassItemNoteList = classItemNotes;
        mClassItemNoteLinearLayout.removeAllViews();
        for(ClassItemNoteContract.ClassItemNoteEntry classNote: mClassItemNoteList) {
            mClassItemNoteLinearLayout.addView(getClassItemNoteView(getLayoutInflater(null), classNote, mEditClassItemNoteClickListener, mRemoveClassItemNoteClickListener));
        }
    }

    public void insertClassItemNote(ClassItemNoteContract.ClassItemNoteEntry itemNote, long id, String fragmentTag) {
        if(id != -1) {
            itemNote.setId(id);
            mClassItemNoteList.add(itemNote);
            final View view = getClassItemNoteView(getActivity().getLayoutInflater(), itemNote, mEditClassItemNoteClickListener, mRemoveClassItemNoteClickListener);
            mClassItemNoteLinearLayout.addView(view);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mNestedScrollView.smoothScrollTo(0, view.getBottom());
                    Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), getString(R.string.item_note_added), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    public void updateClassItemNote(ClassItemNoteContract.ClassItemNoteEntry itemNote, int rowsUpdated, String fragmentTag) {
        int position = mClassItemNoteList.indexOf(itemNote);
        if(position != -1) {
            mClassItemNoteList.set(position, itemNote);
            final View view = mClassItemNoteLinearLayout.getChildAt(position);
            if(view != null)
                _getClassItemNoteView(view, itemNote, mEditClassItemNoteClickListener, mRemoveClassItemNoteClickListener);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mNestedScrollView.smoothScrollTo(0, view.getBottom());
                    Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), getString(R.string.item_note_updated), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    public void deleteClassItemNote(ClassItemNoteContract.ClassItemNoteEntry itemNote, int rowsDeleted, String fragmentTag) {
        int childPosition = mClassItemNoteList.indexOf(itemNote);
        if(childPosition != -1) {
            mClassItemNoteList.remove(childPosition);
            mClassItemNoteLinearLayout.removeViewAt(childPosition);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), getString(R.string.item_note_removed), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    private View getClassItemNoteView(LayoutInflater inflater, ClassItemNoteContract.ClassItemNoteEntry classItemNote, View.OnClickListener editClickListener, View.OnClickListener removeClickListener) {
        return _getClassItemNoteView(inflater.inflate(R.layout.row_class_note, null), classItemNote, editClickListener, removeClickListener);
    }

    private View _getClassItemNoteView(View view, ClassItemNoteContract.ClassItemNoteEntry classItemNote, View.OnClickListener editClickListener, View.OnClickListener removeClickListener) {
        ((TextView) view.findViewById(R.id.date_created_note_text_view)).setText(Html.fromHtml(classItemNote.getDateCreatedNote(getContext())));
        LinearLayout editLinearLayout = (LinearLayout) view.findViewById(R.id.note_clickable_layout);
        editLinearLayout.setTag(classItemNote);
        editLinearLayout.setOnClickListener(editClickListener);
        ImageButton removeButton = (ImageButton) view.findViewById(R.id.remove_button);
        removeButton.setTag(classItemNote);
        removeButton.setOnClickListener(removeClickListener);
        return view;
    }

    public static class ClassItemSummaryInfo {
        public int mTotalAttendance;
        public int mPresentAttendance;
        public int mAbsentAttendance;

        public int mTotalScore;
        public int mRecordedScores;

        public int mTotalSubmission;
        public int mRecordedSubmissions;

        public ClassItemSummaryInfo() {
            mTotalAttendance = 0;
            mPresentAttendance = 0;
            mAbsentAttendance = 0;
            mTotalScore = 0;
            mRecordedScores = 0;
            mTotalSubmission = 0;
            mRecordedSubmissions = 0;
        }
    }
}
