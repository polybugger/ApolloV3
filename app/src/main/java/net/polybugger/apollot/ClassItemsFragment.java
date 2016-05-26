package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import net.polybugger.apollot.db.ClassItemTypeContract;
import net.polybugger.apollot.db.DateTimeFormat;

import org.apache.commons.lang3.StringUtils;

public class ClassItemsFragment extends Fragment {

    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";

    public static boolean REQUERY = false;

    private ClassContract.ClassEntry mClass;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;

    public static ClassItemsFragment newInstance(ClassContract.ClassEntry _class) {
        ClassItemsFragment f = new ClassItemsFragment();
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
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        mClass = (ClassContract.ClassEntry) args.getSerializable(CLASS_ARG);

        View view = inflater.inflate(R.layout.fragment_class_items, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new Adapter(this);
        mRecyclerView.setAdapter(mAdapter);

        ClassActivityFragment rf = (ClassActivityFragment) getFragmentManager().findFragmentByTag(ClassActivityFragment.TAG);
        if(rf != null)
            rf.getClassItemsSummary(mClass, getTag());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO onResume
        if(REQUERY) {
            ClassActivityFragment rf = (ClassActivityFragment) getFragmentManager().findFragmentByTag(ClassActivityFragment.TAG);
            if(rf != null)
                rf.getClassItemsSummary(mClass, getTag());
            REQUERY = false;
        }
        else {

        }
    }

    public void requeryClass(ClassContract.ClassEntry _class) {
        mClass = _class;
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_class_items, menu);
    }

    public void onGetClassItemsSummary(ArrayList<ClassItemSummary> arrayList, String fragmentTag) {
        mAdapter.setArrayList(arrayList);
    }

    public static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private Fragment mFragment;
        private ArrayList<ClassItemSummary> mArrayList;

        private int mSortId;

        private Comparator<ClassItemSummary> mComparator;

        public Adapter(Fragment fragment) {
            mFragment = fragment;
            mArrayList = new ArrayList<>();
            mComparator = new Comparator<ClassItemSummary>() {
                @Override
                public int compare(ClassItemSummary lhs, ClassItemSummary rhs) {
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
                    return 0;
                }
            };
        }

        public void setArrayList(ArrayList<ClassItemSummary> arrayList) {
            mArrayList = arrayList;
            notifyDataSetChanged();
        }

        public void add(ClassItemSummary entry) {
            mArrayList.add(entry);
            notifyDataSetChanged();
        }

        public ClassItemSummary removeByClassItem(ClassItemContract.ClassItemEntry classItem) {
            ClassItemSummary classItemSummary = null;
            int size = mArrayList.size();
            for(int i = 0; i < size; ++i) {
                classItemSummary = mArrayList.get(i);
                if(classItemSummary.mClassItem.equals(classItem)) {
                    mArrayList.remove(i);
                    break;
                }
            }
            notifyDataSetChanged();
            return classItemSummary;
        }

        public void update(ClassItemSummary classItemSummary) {
            ClassItemSummary tmpClassItemSummary = null;
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
            notifyDataSetChanged();
        }

        public void sortBy(int sortId) {
            mSortId = (mSortId == sortId) ? -sortId : sortId;
            Collections.sort(mArrayList, mComparator);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_class_item_summary, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ClassItemSummary entry = mArrayList.get(position);
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
                    /*
                    Intent intent = new Intent(mFragment.getContext(), ClassActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable(ClassActivity.CLASS_ARG, classSummary.mClass);
                    intent.putExtras(args);
                    mFragment.startActivity(intent);
                    */
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
                    holder.mPerfectScoreTextView.setVisibility(View.GONE);
                else {
                    holder.mPerfectScoreTextView.setText(String.format("%s %.2f", mFragment.getString(R.string.perfect_score_label), perfectScore));
                    holder.mPerfectScoreTextView.setVisibility(View.VISIBLE);
                }
            }
            else
                holder.mPerfectScoreTextView.setVisibility(View.GONE);

            if(entry.mClassItem.isRecordSubmissions()) {
                Date submissionDueDate = entry.mClassItem.getSubmissionDueDate();
                if(submissionDueDate == null)
                    holder.mSubmissionDueDateTextView.setVisibility(View.GONE);
                else {
                    holder.mSubmissionDueDateTextView.setText(String.format("%s %s", mFragment.getString(R.string.submission_due_date_label), sdf.format(submissionDueDate)));
                    holder.mSubmissionDueDateTextView.setVisibility(View.VISIBLE);
                }
            }
            else
                holder.mSubmissionDueDateTextView.setVisibility(View.GONE);

            int paddingTop = holder.mTitleTextView.getPaddingTop();
            int paddingRight = holder.mTitleTextView.getPaddingRight();
            int paddingLeft = holder.mTitleTextView.getPaddingLeft();
            if(holder.mSubmissionDueDateTextView.getVisibility() == View.VISIBLE) {
                holder.mSubmissionDueDateTextView.setPadding(paddingLeft, 0, paddingRight, paddingTop);
                holder.mPerfectScoreTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                holder.mCheckAttendanceTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                holder.mItemTypeTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                holder.mItemDateTextView.setPadding(paddingLeft, 0, paddingRight, 0);
                holder.mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
            }
            else if(holder.mPerfectScoreTextView.getVisibility() == View.VISIBLE) {
                holder.mPerfectScoreTextView.setPadding(paddingLeft, 0, paddingRight, paddingTop);
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
            protected TextView mPerfectScoreTextView;
            protected TextView mSubmissionDueDateTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mBackgroundLayout = (LinearLayout) itemView.findViewById(R.id.background_layout);
                mClickableLayout = (LinearLayout) itemView.findViewById(R.id.clickable_layout);
                mTitleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
                mItemDateTextView = (TextView) itemView.findViewById(R.id.item_date_text_view);
                mItemTypeTextView = (TextView) itemView.findViewById(R.id.item_type_text_view);
                mCheckAttendanceTextView = (TextView) itemView.findViewById(R.id.check_attendance_text_view);
                mPerfectScoreTextView = (TextView) itemView.findViewById(R.id.perfect_score_text_view);
                mSubmissionDueDateTextView = (TextView) itemView.findViewById(R.id.submission_due_date_text_view);
            }
        }
    }

    public static class ClassItemSummary {
        public ClassItemContract.ClassItemEntry mClassItem;

        public ClassItemSummary(ClassItemContract.ClassItemEntry classItem) {
            mClassItem = classItem;
        }
    }
}
