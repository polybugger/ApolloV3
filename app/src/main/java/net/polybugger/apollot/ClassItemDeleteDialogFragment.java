package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassItemContract;
import net.polybugger.apollot.db.ClassItemTypeContract;
import net.polybugger.apollot.db.DateTimeFormat;

import org.apache.commons.lang3.StringUtils;

public class ClassItemDeleteDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onConfirmDeleteClassItem(ClassItemContract.ClassItemEntry entry);
    }

    public static final String TAG = "net.polybugger.apollot.delete_class_item_dialog_fragment";
    public static final String ENTRY_ARG = "net.polybugger.apollot.entry_arg";

    private Listener mListener;
    private TextView mTitleTextView;
    private TextView mItemDateTextView;
    private TextView mItemTypeTextView;
    private TextView mCheckAttendanceTextView;
    private LinearLayout mPerfectScoreLinearLayout;
    private TextView mPerfectScoreTextView;
    private LinearLayout mSubmissionDueDateLinearLayout;
    private TextView mSubmissionDueDateTextView;

    public static ClassItemDeleteDialogFragment newInstance(ClassItemContract.ClassItemEntry entry) {
        ClassItemDeleteDialogFragment df = new ClassItemDeleteDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ENTRY_ARG, entry);
        df.setArguments(args);
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final ClassItemContract.ClassItemEntry entry = (ClassItemContract.ClassItemEntry) args.getSerializable(ENTRY_ARG);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_class_item_delete, null);
        mTitleTextView = (TextView) view.findViewById(R.id.text_view);
        mTitleTextView.setText(entry.getDescription());
        mItemDateTextView = (TextView) view.findViewById(R.id.item_date_text_view);
        Resources res = getResources();
        final SimpleDateFormat sdf;
        if(StringUtils.equalsIgnoreCase(res.getConfiguration().locale.getLanguage(), ApolloDbAdapter.JA_LANGUAGE))
            sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE_JA, res.getConfiguration().locale);
        else
            sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE, res.getConfiguration().locale);
        Date itemDate = entry.getItemDate();
        if(itemDate == null)
            mItemDateTextView.setVisibility(View.GONE);
        else {
            mItemDateTextView.setText(sdf.format(itemDate));
            mItemDateTextView.setVisibility(View.VISIBLE);
        }
        mItemTypeTextView = (TextView) view.findViewById(R.id.item_type_text_view);
        ClassItemTypeContract.ClassItemTypeEntry itemType = entry.getItemType();
        if(itemType != null) {
            view.findViewById(R.id.background_layout).setBackgroundResource(BackgroundRect.getBackgroundResource(itemType.getColor(), getActivity()));
            mItemTypeTextView.setText(itemType.getDescription());
            mItemTypeTextView.setVisibility(View.VISIBLE);
        }
        else
            mItemTypeTextView.setVisibility(View.GONE);

        mCheckAttendanceTextView = (TextView) view.findViewById(R.id.check_attendance_text_view);
        mPerfectScoreLinearLayout = (LinearLayout) view.findViewById(R.id.perfect_score_linear_layout);
        mPerfectScoreTextView = (TextView) view.findViewById(R.id.perfect_score_text_view);
        mSubmissionDueDateLinearLayout = (LinearLayout) view.findViewById(R.id.submission_due_date_linear_layout);
        mSubmissionDueDateTextView = (TextView) view.findViewById(R.id.submission_due_date_text_view);

        if(entry.isCheckAttendance())
            mCheckAttendanceTextView.setVisibility(View.VISIBLE);
        else
            mCheckAttendanceTextView.setVisibility(View.GONE);

        if(entry.isRecordScores()) {
            Float perfectScore = entry.getPerfectScore();
            if(perfectScore == null)
                mPerfectScoreLinearLayout.setVisibility(View.GONE);
            else {
                mPerfectScoreTextView.setText(String.format("%.2f", perfectScore));
                mPerfectScoreLinearLayout.setVisibility(View.VISIBLE);
            }
        }
        else
            mPerfectScoreLinearLayout.setVisibility(View.GONE);

        if(entry.isRecordSubmissions()) {
            Date submissionDueDate = entry.getSubmissionDueDate();
            if(submissionDueDate == null)
                mSubmissionDueDateLinearLayout.setVisibility(View.GONE);
            else {
                mSubmissionDueDateTextView.setText(sdf.format(submissionDueDate));
                mSubmissionDueDateLinearLayout.setVisibility(View.VISIBLE);
            }
        }
        else
            mSubmissionDueDateLinearLayout.setVisibility(View.GONE);

        int paddingTop = mTitleTextView.getPaddingTop();
        int paddingRight = mTitleTextView.getPaddingRight();
        int paddingLeft = mTitleTextView.getPaddingLeft();
        if(mSubmissionDueDateLinearLayout.getVisibility() == View.VISIBLE) {
            mSubmissionDueDateLinearLayout.setPadding(0, 0, 0, paddingTop);
            mPerfectScoreLinearLayout.setPadding(0, 0, 0, 0);
            mCheckAttendanceTextView.setPadding(paddingLeft, 0, paddingRight, 0);
            mItemTypeTextView.setPadding(paddingLeft, 0, paddingRight, 0);
            mItemDateTextView.setPadding(paddingLeft, 0, paddingRight, 0);
            mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
        }
        else if(mPerfectScoreLinearLayout.getVisibility() == View.VISIBLE) {
            mPerfectScoreLinearLayout.setPadding(0, 0, 0, paddingTop);
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

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_class_item_title)
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onConfirmDeleteClassItem(entry);
                        dismiss();
                    }
                });
            }
        });
        return alertDialog;
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
