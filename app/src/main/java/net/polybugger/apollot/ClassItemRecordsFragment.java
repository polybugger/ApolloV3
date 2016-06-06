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
        /*
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
        }
        */
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

    public  void onGetClassItemRecords(ArrayList<ClassItemRecordsFragment.ClassItemRecordSummary> arrayList, String fragmentTag) {
        mAdapter.setArrayList(arrayList);
    }

    public void setButtonDate(Date date, int position) {
        mAdapter.updateSubmissionDate(date, position);
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
                    /*
                    if(mSortId == R.id.action_sort_item_description) {
                        return lhs.mClassItem.getDescription().compareToIgnoreCase(rhs.mClassItem.getDescription());
                    }
                    else if(-mSortId == R.id.action_sort_item_description) {
                        return -lhs.mClassItem.getDescription().compareToIgnoreCase(rhs.mClassItem.getDescription());
                    }
                    */
                    return 0;
                }
            };
        }

        public void setArrayList(ArrayList<ClassItemRecordSummary> arrayList) {
            mArrayList = arrayList;
            notifyDataSetChanged();
        }

        public void updateSubmissionDate(Date submissionDate, int position) {
            mArrayList.get(position).mClassItemRecord.setSubmissionDate(submissionDate);
            notifyDataSetChanged();
        }

        public void update(ClassItemRecordSummary classItemSummary) {
            /*
            ClassItemSummary tmpClassItemSummary;
            int size = mArrayList.size(), pos = size;
            for(int i = 0; i < size; ++i) {
                tmpClassItemSummary = mArrayList.get(i);
                if(tmpClassItemSummary.mClassItem.equals(classItemSummary.mClassItem)) {
                    pos = i;
                    break;
                }
            }
            if(pos < size)
                mArrayList.remove(pos);
            mArrayList.add(pos, classItemSummary);
            */
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

            holder.mNameTextView.setText(entry.mClassItemRecord.getClassStudent().getStudent().getName(mFragment.getContext()));

            if(mClassItem.isCheckAttendance()) {
                holder.mAttendanceRadioGroup.setVisibility(View.VISIBLE);
                holder.mAttendanceRadioGroup.setTag(null);
                holder.mAttendanceRadioGroup.clearCheck();
                Boolean attendance = entry.mClassItemRecord.getAttendance();
                if(attendance != null) {
                    if(attendance)
                        holder.mPresentRadioButton.setChecked(true);
                    else
                        holder.mAbsentRadioButton.setChecked(true);
                }
                holder.mAttendanceRadioGroup.setTag(new InfoTag(mArrayList, position, 0, this));
            }
            else
                holder.mAttendanceRadioGroup.setVisibility(View.GONE);

            if(mClassItem.isRecordScores()) {
                holder.mScoreEditText.setVisibility(View.VISIBLE);
                holder.mScoreEditText.setTag(null);
                Float score = entry.mClassItemRecord.getScore();
                if(score != null)
                    holder.mScoreEditText.setText(String.format("%.2f", score));
                else {
                    holder.mScoreEditText.setText(null);
                }
                holder.mScoreEditText.setTag(new InfoTag(mArrayList, position, 0, this));
            }
            else
                holder.mScoreEditText.setVisibility(View.GONE);

            if(mClassItem.isRecordSubmissions()) {
                holder.mSubmissionDateButton.setVisibility(View.VISIBLE);
                final SimpleDateFormat sdf;
                if(StringUtils.equalsIgnoreCase(mFragment.getResources().getConfiguration().locale.getLanguage(), ApolloDbAdapter.JA_LANGUAGE))
                    sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE_JA, mFragment.getResources().getConfiguration().locale);
                else
                    sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE, mFragment.getResources().getConfiguration().locale);
                Date submissionDate = entry.mClassItemRecord.getSubmissionDate();
                if(submissionDate != null)
                    holder.mSubmissionDateButton.setText(sdf.format(submissionDate));
                else
                    holder.mSubmissionDateButton.setText(null);
                holder.mSubmissionDateButton.setTag(submissionDate);
                holder.mSubmissionDateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = mFragment.getFragmentManager();
                        DatePickerDialogFragment df = (DatePickerDialogFragment) fm.findFragmentByTag(DatePickerDialogFragment.TAG);
                        if(df == null) {
                            df = DatePickerDialogFragment.newInstance((Date) v.getTag(), mFragment.getString(R.string.submission_date_hint), mFragment.getTag(), position);
                            df.show(fm, DatePickerDialogFragment.TAG);
                        }
                    }
                });
            }
            else
                holder.mSubmissionDateButton.setVisibility(View.GONE);

            holder.mRemarksEditText.setTag(null);
            holder.mRemarksEditText.setText(entry.mClassItemRecord.getRemarks());
            holder.mRemarksEditText.setTag(new InfoTag(mArrayList, position, 1, this));

        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            protected LinearLayout mBackgroundLayout;
            protected TextView mNameTextView;
            protected RadioGroup mAttendanceRadioGroup;
            protected RadioButton mPresentRadioButton;
            protected RadioButton mAbsentRadioButton;
            protected EditText mScoreEditText;
            protected Button mSubmissionDateButton;
            protected EditText mRemarksEditText;

            public ViewHolder(View itemView) {
                super(itemView);
                mBackgroundLayout = (LinearLayout) itemView.findViewById(R.id.background_layout);
                mNameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
                mAttendanceRadioGroup = (RadioGroup) itemView.findViewById(R.id.attendance_radio_group);
                mPresentRadioButton = (RadioButton) itemView.findViewById(R.id.present_radio_button);
                mAbsentRadioButton = (RadioButton) itemView.findViewById(R.id.absent_radio_button);
                mScoreEditText = (EditText) itemView.findViewById(R.id.score_edit_text);
                mSubmissionDateButton = (Button) itemView.findViewById(R.id.submission_date_button);
                mRemarksEditText = (EditText) itemView.findViewById(R.id.remarks_edit_text);

                mAttendanceRadioGroup.setOnCheckedChangeListener(new MyCheckChangedListener(mScoreEditText));
                mScoreEditText.setOnEditorActionListener(new MyEditorActionListener(mScoreEditText));
                mScoreEditText.setOnFocusChangeListener(new MyFocusChangeListener(mScoreEditText));
                //mScoreEditText.addTextChangedListener(new MyTextWatcher(mScoreEditText));
                mRemarksEditText.setOnEditorActionListener(new MyEditorActionListener(mRemarksEditText));
                mRemarksEditText.setOnFocusChangeListener(new MyFocusChangeListener(mRemarksEditText));
                //mRemarksEditText.addTextChangedListener(new MyTextWatcher(mRemarksEditText));
            }
        }
    }

    public static class ClassItemRecordSummary {
        public ClassItemRecordContract.ClassItemRecordEntry mClassItemRecord;

        public ClassItemRecordSummary(ClassItemRecordContract.ClassItemRecordEntry classItemRecord) {
            mClassItemRecord = classItemRecord;
        }
    }

    public static class InfoTag {
        public ArrayList<ClassItemRecordSummary> mArrayList;
        public int mPosition;
        public int mField;
        public Adapter mAdapter;

        public InfoTag(ArrayList<ClassItemRecordSummary> arrayList, int position, int field, Adapter adapter) {
            mArrayList = arrayList;
            mPosition = position;
            mField = field;
            mAdapter = adapter;
        }
    }

    public static class MyEditorActionListener implements EditText.OnEditorActionListener {

        private EditText mEditText;

        public MyEditorActionListener(EditText editText) {
            mEditText = editText;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            actionId = actionId & EditorInfo.IME_MASK_ACTION;
            if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT ||
                    event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if(true) { //!event.isShiftPressed()) {
                    InfoTag infoTag = (InfoTag) mEditText.getTag();
                    if(infoTag == null)
                        return false;
                    ClassItemRecordSummary itemRecordSummary = infoTag.mArrayList.get(infoTag.mPosition);
                    switch(infoTag.mField) {
                        case 0:
                            Float score;
                            try {
                                score = Float.parseFloat(mEditText.getText().toString());
                            }
                            catch(Exception e) {
                                score = null;
                            }
                            itemRecordSummary.mClassItemRecord.setScore(score);
                            break;
                        case 1:
                            itemRecordSummary.mClassItemRecord.setRemarks(mEditText.getText().toString());
                            break;
                    }
                    infoTag.mAdapter.notifyDataSetChanged();
                    return true; // consume.
                }
            }
            return false; // pass on to other listeners.
        }
    }

    public static class MyFocusChangeListener implements View.OnFocusChangeListener {

        private EditText mEditText;

        public MyFocusChangeListener(EditText editText) {
            mEditText = editText;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus) {
                InfoTag infoTag = (InfoTag) mEditText.getTag();
                if(infoTag == null)
                    return;
                ClassItemRecordSummary itemRecordSummary = infoTag.mArrayList.get(infoTag.mPosition);
                switch(infoTag.mField) {
                    case 0:
                        Float score;
                        try {
                            score = Float.parseFloat(mEditText.getText().toString());
                        }
                        catch(Exception e) {
                            score = null;
                        }
                        itemRecordSummary.mClassItemRecord.setScore(score);
                        break;
                    case 1:
                        itemRecordSummary.mClassItemRecord.setRemarks(mEditText.getText().toString());
                        break;
                }
                infoTag.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public static class MyTextWatcher implements TextWatcher {

        private EditText mEditText;

        public MyTextWatcher(EditText editText) {
            mEditText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }
        @Override
        public void afterTextChanged(Editable s) {
            InfoTag infoTag = (InfoTag) mEditText.getTag();
            if(infoTag == null)
                return;
            ClassItemRecordSummary itemRecordSummary = infoTag.mArrayList.get(infoTag.mPosition);
            switch(infoTag.mField) {
                case 0:
                    Float score;
                    try {
                        score = Float.parseFloat(mEditText.getText().toString());
                    }
                    catch(Exception e) {
                        score = null;
                    }
                    itemRecordSummary.mClassItemRecord.setScore(score);
                    break;
                case 1:
                    itemRecordSummary.mClassItemRecord.setRemarks(mEditText.getText().toString());
                    break;
            }
            infoTag.mAdapter.notifyDataSetChanged();
        }
    }

    public static class MyCheckChangedListener implements RadioGroup.OnCheckedChangeListener {

        private EditText mScoreEditText;

        public MyCheckChangedListener(EditText scoreEditText) {
            mScoreEditText = scoreEditText;
        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            InfoTag infoTag = (InfoTag) group.getTag();
            if(infoTag == null)
                return;
            ClassItemRecordSummary itemRecordSummary = infoTag.mArrayList.get(infoTag.mPosition);
            if(checkedId == R.id.absent_radio_button) {
                mScoreEditText.setEnabled(false);
                itemRecordSummary.mClassItemRecord.setAttendance(false);
            }
            else {
                mScoreEditText.setEnabled(true);
                itemRecordSummary.mClassItemRecord.setAttendance(true);
            }
            infoTag.mAdapter.notifyDataSetChanged();
        }
    }
}
