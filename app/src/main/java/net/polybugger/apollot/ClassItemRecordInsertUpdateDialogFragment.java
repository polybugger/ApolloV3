package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassItemContract;
import net.polybugger.apollot.db.ClassItemRecordContract;
import net.polybugger.apollot.db.ClassItemTypeContract;
import net.polybugger.apollot.db.DateTimeFormat;

import org.apache.commons.lang3.StringUtils;

public class ClassItemRecordInsertUpdateDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onConfirmInsertUpdateClassItemRecord(ClassItemRecordContract.ClassItemRecordEntry entry, ClassItemContract.ClassItemEntry classItem, String fragmentTag);
    }

    public static final String TAG = "net.polybugger.apollot.insert_update_class_item_record_dialog_fragment";
    public static final String ENTRY_ARG = "net.polybugger.apollot.entry_arg";
    public static final String CLASS_ITEM_ARG = "net.polybugger.apollot.class_item_arg";
    public static final String TITLE_ARG = "net.polybugger.apollot.title_arg";
    public static final String BUTTON_TEXT_ARG = "net.polybugger.apollot.button_text_arg";
    public static final String FRAGMENT_TAG_ARG = "net.polybugger.apollot.fragment_tag_arg";

    private Listener mListener;
    private ClassItemRecordContract.ClassItemRecordEntry mEntry;
    private ClassItemContract.ClassItemEntry mClassItem;
    private AlertDialog mAlertDialog;
    private LinearLayout mBackgroundLayout;
    private TextView mTitleTextView;
    private TextView mItemDateTextView;
    private TextView mItemTypeTextView;
    private TextView mCheckAttendanceTextView;
    private TextView mPerfectScoreTextView;
    private TextView mSubmissionDueDateTextView;
    private TextView mNameTextView;
    private RadioGroup mAttendanceRadioGroup;
    private RadioButton mPresentRadioButton;
    private RadioButton mAbsentRadioButton;
    private EditText mScoreEditText;
    private Button mSubmissionDateButton;
    private EditText mRemarksEditText;

    public static ClassItemRecordInsertUpdateDialogFragment newInstance(ClassItemRecordContract.ClassItemRecordEntry entry, ClassItemContract.ClassItemEntry classItem, String title, String buttonText, String fragmentTag) {
        ClassItemRecordInsertUpdateDialogFragment df = new ClassItemRecordInsertUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ENTRY_ARG, entry);
        args.putSerializable(CLASS_ITEM_ARG, classItem);
        args.putString(TITLE_ARG, title);
        args.putString(BUTTON_TEXT_ARG, buttonText);
        args.putString(FRAGMENT_TAG_ARG, fragmentTag);
        df.setArguments(args);
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        mEntry = (ClassItemRecordContract.ClassItemRecordEntry) args.getSerializable(ENTRY_ARG);
        mClassItem = (ClassItemContract.ClassItemEntry) args.getSerializable(CLASS_ITEM_ARG);
        final String fragmentTag = args.getString(FRAGMENT_TAG_ARG);

        final SimpleDateFormat sdf;
        if(StringUtils.equalsIgnoreCase(getContext().getResources().getConfiguration().locale.getLanguage(), ApolloDbAdapter.JA_LANGUAGE))
            sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE_JA, getContext().getResources().getConfiguration().locale);
        else
            sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE, getContext().getResources().getConfiguration().locale);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_class_item_record_insert_update, null);
        mBackgroundLayout = (LinearLayout) view.findViewById(R.id.background_layout);
        ClassItemTypeContract.ClassItemTypeEntry itemType = mClassItem.getItemType();
        if(itemType != null)
            mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(itemType.getColor(), getContext()));
        else
            mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(null, getContext()));
        mTitleTextView = (TextView) view.findViewById(R.id.title_text_view);
        mTitleTextView.setText(mClassItem.getDescription());
        mNameTextView = (TextView) view.findViewById(R.id.name_text_view);
        mNameTextView.setText(mEntry.getClassStudent().getStudent().getName(getContext()));
        mItemDateTextView = (TextView) view.findViewById(R.id.item_date_text_view);
        Date itemDate = mClassItem.getItemDate();
        if(itemDate == null)
            mItemDateTextView.setVisibility(View.GONE);
        else {
            mItemDateTextView.setText(sdf.format(itemDate));
            mItemDateTextView.setVisibility(View.VISIBLE);
        }
        mItemTypeTextView = (TextView) view.findViewById(R.id.item_type_text_view);
        if(itemType != null) {
            mItemTypeTextView.setVisibility(View.VISIBLE);
            mItemTypeTextView.setText(itemType.getDescription());
        }
        else
            mItemTypeTextView.setVisibility(View.GONE);
        mCheckAttendanceTextView = (TextView) view.findViewById(R.id.check_attendance_text_view);
        mAttendanceRadioGroup = (RadioGroup) view.findViewById(R.id.attendance_radio_group);
        mPresentRadioButton = (RadioButton) view.findViewById(R.id.present_radio_button);
        mAbsentRadioButton = (RadioButton) view.findViewById(R.id.absent_radio_button);
        mPerfectScoreTextView = (TextView) view.findViewById(R.id.perfect_score_text_view);
        mScoreEditText = (EditText) view.findViewById(R.id.score_edit_text);
        if(mClassItem.isCheckAttendance()) {
            mCheckAttendanceTextView.setVisibility(View.VISIBLE);
            mAttendanceRadioGroup.setVisibility(View.VISIBLE);
            mAttendanceRadioGroup.clearCheck();
            mAttendanceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId == R.id.absent_radio_button) {
                        mScoreEditText.setEnabled(false);
                        mScoreEditText.setText(null);
                    }
                    else {
                        mScoreEditText.setEnabled(true);
                    }
                }
            });
            Boolean attendance = mEntry.getAttendance();
            if(attendance != null) {
                if(attendance)
                    mPresentRadioButton.setChecked(true);
                else
                    mAbsentRadioButton.setChecked(true);
            }
        }
        else {
            mCheckAttendanceTextView.setVisibility(View.GONE);
            mAttendanceRadioGroup.setVisibility(View.GONE);
        }
        if(mClassItem.isRecordScores()) {
            mPerfectScoreTextView.setVisibility(View.VISIBLE);
            mScoreEditText.setVisibility(View.VISIBLE);
            Float perfectScore = mClassItem.getPerfectScore();
            if(perfectScore != null)
                mPerfectScoreTextView.setText(String.format("%s %.2f", getString(R.string.perfect_score_label), perfectScore));
            else
                mPerfectScoreTextView.setText(String.format("%s %s", getString(R.string.perfect_score_label), getString(R.string.dash_string)));
            Float score = mEntry.getScore();
            if(score != null)
                mScoreEditText.setText(String.format("%.2f", score));
            else
                mScoreEditText.setText(null);
        }
        else {
            mPerfectScoreTextView.setVisibility(View.GONE);
            mScoreEditText.setVisibility(View.GONE);
        }
        mSubmissionDueDateTextView = (TextView) view.findViewById(R.id.submission_due_date_text_view);
        mSubmissionDateButton = (Button) view.findViewById(R.id.submission_date_button);
        if(mClassItem.isRecordSubmissions()) {
            mSubmissionDueDateTextView.setVisibility(View.VISIBLE);
            mSubmissionDateButton.setVisibility(View.VISIBLE);
            Date submissionDueDate = mClassItem.getSubmissionDueDate();
            if(submissionDueDate != null)
                mSubmissionDueDateTextView.setText(String.format("%s %s", getString(R.string.due_date_label), sdf.format(submissionDueDate)));
            else
                mSubmissionDueDateTextView.setText(String.format("%s %s", getString(R.string.due_date_label), getString(R.string.dash_string)));
            mSubmissionDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getFragmentManager();
                    DatePickerDialogFragment df = (DatePickerDialogFragment) fm.findFragmentByTag(DatePickerDialogFragment.TAG);
                    if(df == null) {
                        df = DatePickerDialogFragment.newInstance((Date) v.getTag(), getString(R.string.submission_date_hint), getTag(), R.id.submission_date_button);
                        df.show(fm, DatePickerDialogFragment.TAG);
                    }
                }
            });
            Date submissionDate = mEntry.getSubmissionDate();
            if(submissionDate != null)
                mSubmissionDateButton.setText(sdf.format(submissionDate));
            else
                mSubmissionDateButton.setText(null);
            mSubmissionDateButton.setTag(submissionDate);
        }
        else {
            mSubmissionDueDateTextView.setVisibility(View.GONE);
            mSubmissionDateButton.setVisibility(View.GONE);
        }
        mRemarksEditText = (EditText) view.findViewById(R.id.remarks_edit_text);
        mRemarksEditText.setText(mEntry.getRemarks());

        mAlertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(args.getString(TITLE_ARG))
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(args.getString(BUTTON_TEXT_ARG), null)
                .create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mClassItem.isCheckAttendance()) {
                            if(mPresentRadioButton.isChecked()) mEntry.setAttendance(true);
                            if(mAbsentRadioButton.isChecked()) mEntry.setAttendance(false);
                        }
                        if(mClassItem.isRecordScores()) {
                            Float score;
                            try {
                                score = Float.parseFloat(mScoreEditText.getText().toString());
                            }
                            catch(Exception e) {
                                score = null;
                            }
                            mEntry.setScore(score);
                        }
                        if(mClassItem.isRecordSubmissions())
                            mEntry.setSubmissionDate((Date) mSubmissionDateButton.getTag());
                        mEntry.setRemarks(mRemarksEditText.getText().toString());
                        mListener.onConfirmInsertUpdateClassItemRecord(mEntry, mClassItem, fragmentTag);
                        dismiss();
                    }
                });
            }
        });
        return mAlertDialog;
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

    // TODO make sure the colors are preserved on theme change from system
    // public void onGetAcademicTerms(ArrayList<AcademicTermContract.AcademicTermEntry> arrayList, String fragmentTag) { }

    public void setButtonDate(Date date, int buttonId) {
        Context context = getContext();
        final SimpleDateFormat sdf;
        if(StringUtils.equalsIgnoreCase(context.getResources().getConfiguration().locale.getLanguage(), ApolloDbAdapter.JA_LANGUAGE))
            sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE_JA, context.getResources().getConfiguration().locale);
        else
            sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE, context.getResources().getConfiguration().locale);

        Button b = (Button) mAlertDialog.findViewById(buttonId);
        if(date != null) {
            b.setText(sdf.format(date));
        }
        else {
            b.setText(null);
        }
        b.setTag(date);
    }
}
