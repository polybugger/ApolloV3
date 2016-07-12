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
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassItemContract;
import net.polybugger.apollot.db.ClassItemTypeContract;
import net.polybugger.apollot.db.DateTimeFormat;

import org.apache.commons.lang3.StringUtils;

public class ClassItemInsertUpdateDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onConfirmInsertUpdateClassItem(ClassItemContract.ClassItemEntry entry, String fragmentTag);
    }

    public static final String TAG = "net.polybugger.apollot.insert_update_class_item_dialog_fragment";
    public static final String ENTRY_ARG = "net.polybugger.apollot.entry_arg";
    public static final String TITLE_ARG = "net.polybugger.apollot.title_arg";
    public static final String BUTTON_TEXT_ARG = "net.polybugger.apollot.button_text_arg";
    public static final String FRAGMENT_TAG_ARG = "net.polybugger.apollot.fragment_tag_arg";

    private Listener mListener;
    private ClassItemContract.ClassItemEntry mEntry;
    private AlertDialog mAlertDialog;
    private LinearLayout mBackgroundLayout;
    private Button mItemDateButton;
    private Spinner mItemTypeSpinner;
    private EditText mDescriptionEditText;
    private TextView mErrorTextView;
    private CheckBox mCheckAttendanceCheckBox;
    private CheckBox mRecordScoresCheckBox;
    private EditText mPerfectScoreEditText;
    private TextView mPerfectScoreErrorTextView;
    private CheckBox mRecordSubmissionsCheckBox;
    private Button mSubmissionDueDateButton;
    private TextView mSubmissionDueDateErrorTextView;


    public static ClassItemInsertUpdateDialogFragment newInstance(ClassItemContract.ClassItemEntry entry, String title, String buttonText, String fragmentTag) {
        ClassItemInsertUpdateDialogFragment df = new ClassItemInsertUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ENTRY_ARG, entry);
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
        mEntry = (ClassItemContract.ClassItemEntry) args.getSerializable(ENTRY_ARG);
        final String fragmentTag = args.getString(FRAGMENT_TAG_ARG);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_class_item_insert_update, null);
        mBackgroundLayout = (LinearLayout) view.findViewById(R.id.background_layout);
        mItemDateButton = (Button) view.findViewById(R.id.item_date_button);
        mItemTypeSpinner = (Spinner) view.findViewById(R.id.item_type_spinner);
        mDescriptionEditText = (EditText) view.findViewById(R.id.description_edit_text);
        mErrorTextView = (TextView) view.findViewById(R.id.error_text_view);
        mCheckAttendanceCheckBox = (CheckBox) view.findViewById(R.id.check_attendance_check_box);
        mRecordScoresCheckBox = (CheckBox) view.findViewById(R.id.record_scores_check_box);
        mPerfectScoreEditText = (EditText) view.findViewById(R.id.perfect_score_edit_text);
        mPerfectScoreErrorTextView = (TextView) view.findViewById(R.id.perfect_score_error_text_view);
        mRecordSubmissionsCheckBox = (CheckBox) view.findViewById(R.id.record_submissions_check_box);
        mSubmissionDueDateButton = (Button) view.findViewById(R.id.submission_due_date_button);
        mSubmissionDueDateErrorTextView = (TextView) view.findViewById(R.id.submission_due_date_error_text_view);
        mItemDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerDialogFragment df = (DatePickerDialogFragment) fm.findFragmentByTag(DatePickerDialogFragment.TAG);
                if(df == null) {
                    df = DatePickerDialogFragment.newInstance((Date) v.getTag(), getString(R.string.item_date_hint), getTag(), R.id.item_date_button);
                    df.show(fm, DatePickerDialogFragment.TAG);
                }
            }
        });
        mItemTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // TODO make sure the colors are preserved on theme change from system
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ClassItemTypeContract.ClassItemTypeEntry itemType = (ClassItemTypeContract.ClassItemTypeEntry) parent.getItemAtPosition(position);
                if(itemType != null) {
                    TextView tv = (TextView) view;
                    if(itemType.getId() == -1)
                        tv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.tertiary_text_dark));
                    else
                        tv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.primary_text_light));
                    mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(itemType.getColor(), getActivity()));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        // TODO just get the academic terms without using async task
        /* MainActivityFragment rf = (MainActivityFragment) getFragmentManager().findFragmentByTag(MainActivityFragment.TAG);
        if(rf != null)
            rf.getAcademicTerms(TAG);
        */
        SQLiteDatabase db = ApolloDbAdapter.open();
        ArrayList<ClassItemTypeContract.ClassItemTypeEntry> itemTypes = ClassItemTypeContract._getEntries(db);
        ApolloDbAdapter.close();
        itemTypes.add(0, new ClassItemTypeContract.ClassItemTypeEntry(-1, getString(R.string.item_type_hint), null));
        ArrayAdapter<ClassItemTypeContract.ClassItemTypeEntry> spinnerAdapter = new ArrayAdapter<ClassItemTypeContract.ClassItemTypeEntry>(getActivity(), android.R.layout.simple_spinner_item, itemTypes) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
                ClassItemTypeContract.ClassItemTypeEntry itemType = getItem(position);
                if(itemType.getId() == -1)
                    tv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.tertiary_text_dark));
                else
                    tv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.primary_text_light));
                try {
                    tv.setBackgroundColor(Color.parseColor(itemType.getColor()));
                }
                catch(Exception e) { }
                return tv;
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mItemTypeSpinner.setAdapter(spinnerAdapter);
        mDescriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                mErrorTextView.setText(" ");
            }
        });
        mRecordScoresCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mPerfectScoreEditText.setVisibility(View.VISIBLE);
                    mPerfectScoreEditText.requestFocus();
                    mPerfectScoreErrorTextView.setVisibility(View.VISIBLE);
                }
                else {
                    mPerfectScoreEditText.setVisibility(View.GONE);
                    mPerfectScoreErrorTextView.setVisibility(View.GONE);
                    mPerfectScoreErrorTextView.setText(" ");
                }
            }
        });
        mPerfectScoreEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                mPerfectScoreErrorTextView.setText(" ");
            }
        });
        mRecordSubmissionsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mSubmissionDueDateButton.setVisibility(View.VISIBLE);
                    mSubmissionDueDateButton.requestFocus();
                    mSubmissionDueDateErrorTextView.setVisibility(View.VISIBLE);
                }
                else {
                    mSubmissionDueDateButton.setVisibility(View.GONE);
                    mSubmissionDueDateErrorTextView.setVisibility(View.GONE);
                    mSubmissionDueDateErrorTextView.setText(" ");
                }
            }
        });
        mSubmissionDueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerDialogFragment df = (DatePickerDialogFragment) fm.findFragmentByTag(DatePickerDialogFragment.TAG);
                if(df == null) {
                    df = DatePickerDialogFragment.newInstance((Date) v.getTag(), getString(R.string.submission_due_date_hint), getTag(), R.id.submission_due_date_button);
                    df.show(fm, DatePickerDialogFragment.TAG);
                }
            }
        });

        Context context = getContext();
        final SimpleDateFormat sdf;
        if(StringUtils.equalsIgnoreCase(context.getResources().getConfiguration().locale.getLanguage(), ApolloDbAdapter.JA_LANGUAGE))
            sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE_JA, context.getResources().getConfiguration().locale);
        else
            sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE, context.getResources().getConfiguration().locale);

        if(mEntry == null) {
            mEntry = new ClassItemContract.ClassItemEntry(-1, -1, null, null, new Date(), false, false, null, false, null);
        }
        Date itemDate = mEntry.getItemDate();
        if(itemDate != null) {
            mItemDateButton.setText(sdf.format(itemDate));
            mItemDateButton.setTag(itemDate);
        }
        ClassItemTypeContract.ClassItemTypeEntry itemType = mEntry.getItemType();
        if(itemType != null)
            mItemTypeSpinner.setSelection(spinnerAdapter.getPosition(itemType));
        mDescriptionEditText.setText(mEntry.getDescription());
        mCheckAttendanceCheckBox.setChecked(mEntry.isCheckAttendance());
        mRecordScoresCheckBox.setChecked(mEntry.isRecordScores());
        Float perfectScore = mEntry.getPerfectScore();
        if(perfectScore != null)
            mPerfectScoreEditText.setText(String.format("%.2f", perfectScore));
        mRecordSubmissionsCheckBox.setChecked(mEntry.isRecordSubmissions());
        Date submissionDueDate = mEntry.getSubmissionDueDate();
        if(submissionDueDate != null) {
            mSubmissionDueDateButton.setText(sdf.format(submissionDueDate));
            mSubmissionDueDateButton.setTag(submissionDueDate);
        }

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
                        String description = mDescriptionEditText.getText().toString();
                        if(StringUtils.isEmpty(description)) {
                            mErrorTextView.setText(R.string.please_enter_an_activity_description);
                            mDescriptionEditText.requestFocus();
                            return;
                        }
                        mEntry.setDescription(description);
                        mEntry.setItemDate((Date) mItemDateButton.getTag());
                        ClassItemTypeContract.ClassItemTypeEntry itemType = (ClassItemTypeContract.ClassItemTypeEntry) mItemTypeSpinner.getSelectedItem();
                        if(itemType != null && itemType.getId() != -1)
                            mEntry.setItemType(itemType);
                        else
                            mEntry.setItemType(null);
                        mEntry.setCheckAttendance(mCheckAttendanceCheckBox.isChecked());
                        boolean recordScores = mRecordScoresCheckBox.isChecked();
                        Float perfectScore;
                        try {
                            perfectScore = Float.parseFloat(mPerfectScoreEditText.getText().toString());
                        }
                        catch(Exception e) {
                            perfectScore = null;
                        }
                        if(recordScores && perfectScore == null) {
                            mPerfectScoreErrorTextView.setText(R.string.please_enter_a_perfect_score);
                            mPerfectScoreEditText.requestFocus();
                            return;
                        }
                        mEntry.setRecordScores(recordScores);
                        mEntry.setPerfectScore(perfectScore);
                        boolean recordSubmissions = mRecordSubmissionsCheckBox.isChecked();
                        Date submissionDueDate = (Date) mSubmissionDueDateButton.getTag();
                        if(recordSubmissions && submissionDueDate == null) {
                            mSubmissionDueDateErrorTextView.setText(R.string.please_input_a_submission_due_date);
                            mSubmissionDueDateButton.requestFocus();
                            return;
                        }
                        mEntry.setRecordSubmissions(recordSubmissions);
                        mEntry.setSubmissionDueDate(submissionDueDate);
                        mListener.onConfirmInsertUpdateClassItem(mEntry, fragmentTag);
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
            if(buttonId == R.id.submission_due_date_button)
                mSubmissionDueDateErrorTextView.setText(" ");
        }
        else {
            b.setText(null);
        }
        b.setTag(date);
    }
}
