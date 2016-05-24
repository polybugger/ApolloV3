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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.polybugger.apollot.db.AcademicTermContract;
import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassItemTypeContract;
import net.polybugger.apollot.db.ClassScheduleContract;
import net.polybugger.apollot.db.PastCurrentEnum;

import org.apache.commons.lang3.StringUtils;

public class ClassesFragment extends Fragment {

    public static final String TAG_PAST = "net.polybugger.apollot.past_classes_fragment";
    public static final String TAG_CURRENT = "net.polybugger.apollot.current_classes_fragment";
    public static final String PAST_CURRENT_ARG = "net.polybugger.apollot.past_current_arg";

    public static boolean REQUERY = false;
    public static boolean REQUERY_CLASS = false;
    public static ClassContract.ClassEntry CLASS = null;

    private PastCurrentEnum mPastCurrent;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;

    public static ClassesFragment newInstance(PastCurrentEnum pastCurrentEnum) {
        ClassesFragment f = new ClassesFragment();
        Bundle args = new Bundle();
        args.putSerializable(PAST_CURRENT_ARG, pastCurrentEnum);
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
        mPastCurrent = (PastCurrentEnum) args.getSerializable(PAST_CURRENT_ARG);
        switch(mPastCurrent) {
            case PAST:
                getActivity().setTitle(R.string.past_classes);
                break;
            case CURRENT:
                getActivity().setTitle(R.string.current_classes);
                break;
        }
        View view = inflater.inflate(R.layout.fragment_classes, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new Adapter(this);
        mRecyclerView.setAdapter(mAdapter);

        MainActivityFragment f = (MainActivityFragment) getFragmentManager().findFragmentByTag(MainActivityFragment.TAG);
        if(f != null)
            f.getClassesSummary(mPastCurrent);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(REQUERY) {
            MainActivityFragment f = (MainActivityFragment) getFragmentManager().findFragmentByTag(MainActivityFragment.TAG);
            if(f != null)
                f.getClassesSummary(mPastCurrent);
            REQUERY = false;
        }
        // TODO other types of REQUERY
        else if(REQUERY_CLASS) {
            if(mPastCurrent != CLASS.getPastCurrent())
                mAdapter.removeByClass(CLASS);
            else {
                MainActivityFragment f = (MainActivityFragment) getFragmentManager().findFragmentByTag(MainActivityFragment.TAG);
                if(f != null)
                    f.requeryClassSummary(CLASS, getTag());
            }
            REQUERY_CLASS = false;
            CLASS = null;
        }
        else {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_sort_class_code:
                mAdapter.sortBy(R.id.action_sort_class_code);
                return true;
            case R.id.action_sort_class_description:
                mAdapter.sortBy(R.id.action_sort_class_description);
                return true;
            case R.id.action_sort_academic_term:
                mAdapter.sortBy(R.id.action_sort_academic_term);
                return true;
            case R.id.action_sort_year:
                mAdapter.sortBy(R.id.action_sort_year);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_classes, menu);
    }

    public void onGetClassesSummary(ArrayList<ClassSummary> arrayList, PastCurrentEnum pastCurrent) {
        mAdapter.setArrayList(arrayList);
    }

    public void insertClass(ClassContract.ClassEntry entry) {
        ClassesFragment.ClassSummary classSummary = new ClassesFragment.ClassSummary(entry);
        mAdapter.add(classSummary);
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), getString(R.string.class_added), Snackbar.LENGTH_SHORT).show();
            }
        }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
    }

    public void onRequeryClassSummary(ClassSummary classSummary, String fragmentTag) {
        if(mPastCurrent == classSummary.mClass.getPastCurrent()) {
            mAdapter.update(classSummary);
        }
    }

    public static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private Fragment mFragment;
        private ArrayList<ClassSummary> mArrayList;

        private int mSortId;

        private Comparator<ClassSummary> mComparator;

        public Adapter(Fragment fragment) {
            mFragment = fragment;
            mArrayList = new ArrayList<>();
            mComparator = new Comparator<ClassSummary>() {
                @Override
                public int compare(ClassSummary lhs, ClassSummary rhs) {
                    if(mSortId == R.id.action_sort_class_code) {
                        return lhs.mClass.getCode().compareToIgnoreCase(rhs.mClass.getCode());
                    }
                    else if(-mSortId == R.id.action_sort_class_code) {
                        return -lhs.mClass.getCode().compareToIgnoreCase(rhs.mClass.getCode());
                    }
                    else if(mSortId == R.id.action_sort_class_description) {
                        return lhs.mClass.getDescription().compareToIgnoreCase(rhs.mClass.getDescription());
                    }
                    else if(-mSortId == R.id.action_sort_class_description) {
                        return -lhs.mClass.getDescription().compareToIgnoreCase(rhs.mClass.getDescription());
                    }
                    else if(mSortId == R.id.action_sort_academic_term) {
                        AcademicTermContract.AcademicTermEntry lat = lhs.mClass.getAcademicTerm();
                        AcademicTermContract.AcademicTermEntry rat = rhs.mClass.getAcademicTerm();
                        if(lat == null)
                            return 1;
                        if(rat == null)
                            return -1;
                        return lat.getDescription().compareToIgnoreCase(rat.getDescription());
                    }
                    else if(-mSortId == R.id.action_sort_academic_term) {
                        AcademicTermContract.AcademicTermEntry lat = lhs.mClass.getAcademicTerm();
                        AcademicTermContract.AcademicTermEntry rat = rhs.mClass.getAcademicTerm();
                        if(lat == null)
                            return -1;
                        if(rat == null)
                            return 1;
                        return -lat.getDescription().compareToIgnoreCase(rat.getDescription());
                    }
                    else if(mSortId == R.id.action_sort_year) {
                        Long lYear = lhs.mClass.getYear();
                        Long rYear = rhs.mClass.getYear();
                        if(lYear == null)
                            return 1;
                        if(rYear == null)
                            return -1;
                        return (lYear < rYear ? -1 : 1);
                    }
                    else if(-mSortId == R.id.action_sort_year) {
                        Long lYear = lhs.mClass.getYear();
                        Long rYear = rhs.mClass.getYear();
                        if(lYear == null)
                            return -1;
                        if(rYear == null)
                            return 1;
                        return (lYear < rYear ? 1 : -1);
                    }
                    return 0;
                }
            };
        }

        public void setArrayList(ArrayList<ClassSummary> arrayList) {
            mArrayList = arrayList;
            notifyDataSetChanged();
        }

        public void add(ClassSummary entry) {
            mArrayList.add(entry);
            notifyDataSetChanged();
        }

        public boolean containsByClass(ClassContract.ClassEntry _class) {
            for(ClassSummary classSummary : mArrayList) {
                if(classSummary.mClass.equals(_class))
                    return true;
            }
            return false;
        }

        public ClassSummary removeByClass(ClassContract.ClassEntry _class) {
            ClassSummary classSummary = null;
            int size = mArrayList.size();
            for(int i = 0; i < size; ++i) {
                classSummary = mArrayList.get(i);
                if(classSummary.mClass.equals(_class)) {
                    mArrayList.remove(i);
                    break;
                }
            }
            notifyDataSetChanged();
            return classSummary;
        }

        public void update(ClassSummary classSummary) {
            ClassSummary tmpClassSummary = null;
            int size = mArrayList.size(), pos = size;
            for(int i = 0; i < size; ++i) {
                tmpClassSummary = mArrayList.get(i);
                if(tmpClassSummary.mClass.equals(classSummary.mClass)) {
                    pos = i;
                    break;
                }
            }
            if(pos < size)
                mArrayList.remove(pos);
            mArrayList.add(pos, classSummary);
            notifyDataSetChanged();
        }

        public void sortBy(final int sortId) {
            mSortId = (mSortId == sortId) ? -sortId : sortId;
            Collections.sort(mArrayList, mComparator);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_class_summary, parent, false));
        }

        // TODO adjust formatting for empty fields
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ClassSummary entry = mArrayList.get(position);
            AcademicTermContract.AcademicTermEntry academicTerm = entry.mClass.getAcademicTerm();
            if(academicTerm != null)
                holder.mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(academicTerm.getColor(), mFragment.getContext()));
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
                    ClassSummary classSummary = (ClassSummary) v.getTag();
                    if(classSummary.mClass.isLocked()) {
                        FragmentManager fm = mFragment.getFragmentManager();
                        UnlockPasswordDialogFragment df = (UnlockPasswordDialogFragment) fm.findFragmentByTag(UnlockPasswordDialogFragment.TAG);
                        if(df == null) {
                            df = UnlockPasswordDialogFragment.newInstance(classSummary.mClass, UnlockPasswordDialogFragment.Option.UNLOCK_CLASS);
                            df.show(fm, UnlockPasswordDialogFragment.TAG);
                        }
                    }
                    else {
                        Intent intent = new Intent(mFragment.getContext(), ClassActivity.class);
                        Bundle args = new Bundle();
                        args.putSerializable(ClassActivity.CLASS_ARG, classSummary.mClass);
                        intent.putExtras(args);
                        mFragment.startActivity(intent);
                    }
                }
            });

            if(entry.mClass.isLocked()) {
                holder.mTitleTextView.setText(String.format("%s %s", entry.mClass.getCode(), mFragment.getString(R.string.ellipsis)));
                holder.mLockedImageView.setVisibility(View.VISIBLE);
                holder.mAcademicTermTextView.setVisibility(View.GONE);
            }
            else {
                holder.mTitleTextView.setText(entry.mClass.getTitle());
                holder.mLockedImageView.setVisibility(View.GONE);
                String academicTermYear = entry.mClass.getAcademicTermYear();
                if(StringUtils.isBlank(academicTermYear)) {
                    holder.mAcademicTermTextView.setVisibility(View.GONE);
                }
                else {
                    holder.mAcademicTermTextView.setText(academicTermYear);
                    holder.mAcademicTermTextView.setVisibility(View.VISIBLE);
                }
            }
            if(holder.mAcademicTermTextView.getVisibility() == View.GONE) {
                int paddingTop = holder.mTitleTextView.getPaddingTop();
                int paddingRight = holder.mTitleTextView.getPaddingRight();
                int paddingLeft = holder.mTitleTextView.getPaddingLeft();
                holder.mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, paddingTop);
            }
            else {
                int paddingTop = holder.mTitleTextView.getPaddingTop();
                int paddingRight = holder.mTitleTextView.getPaddingRight();
                int paddingLeft = holder.mTitleTextView.getPaddingLeft();
                holder.mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
            }

            if(entry.mClassSchedules.size() == 0) {
                holder.mClassScheduleTimeLocationTextView.setVisibility(View.GONE);
            }
            else {
                ClassScheduleContract.ClassScheduleEntry classSchedule = entry.mClassSchedules.get(0);
                String time = classSchedule.getTime(ApolloDbAdapter.getAppContext());
                if(entry.mClassSchedules.size() > 1)
                    time = time + " ...";
                String location = classSchedule.getLocation();
                if(!StringUtils.isBlank(location)) {
                    time = time + "\n" + location;
                }
                holder.mClassScheduleTimeLocationTextView.setText(time);
                holder.mClassScheduleTimeLocationTextView.setVisibility(View.VISIBLE);
            }
            if(entry.mStudentCount == 0) {
                holder.mStudentCountTextView.setVisibility(View.GONE);
            }
            else {
                holder.mStudentCountTextView.setText(String.format("%s %d", mFragment.getString(R.string.students_label), entry.mStudentCount));
                holder.mStudentCountTextView.setVisibility(View.VISIBLE);
            }

            if(entry.mItemSummaryCount.size() == 0) {
                holder.mItemCountTextView.setVisibility(View.GONE);
                holder.mItemSummaryCountLinearLayout.setVisibility(View.GONE);
                if(holder.mStudentCountTextView.getVisibility() == View.VISIBLE) {
                    int paddingTop = holder.mStudentCountTextView.getPaddingTop();
                    int paddingRight = holder.mStudentCountTextView.getPaddingRight();
                    int paddingLeft = holder.mStudentCountTextView.getPaddingLeft();
                    holder.mStudentCountTextView.setPadding(paddingLeft, paddingTop, paddingRight, paddingTop);
                }
                else {
                    int paddingTop = holder.mStudentCountTextView.getPaddingTop();
                    int paddingRight = holder.mStudentCountTextView.getPaddingRight();
                    int paddingLeft = holder.mStudentCountTextView.getPaddingLeft();
                    holder.mStudentCountTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);
                }
            }
            else {
                LayoutInflater inflater = mFragment.getLayoutInflater(null);
                int itemSummaryTotal = 0, count;
                View view; TextView textView;
                holder.mItemSummaryCountLinearLayout.removeAllViews();
                for(Map.Entry<ClassItemTypeContract.ClassItemTypeEntry, Integer> itemCount : entry.mItemSummaryCount.entrySet()) {
                    ClassItemTypeContract.ClassItemTypeEntry itemType = itemCount.getKey();
                    count = itemCount.getValue();
                    view = inflater.inflate(R.layout.row_class_summary_item_count, null);
                    textView = (TextView) view.findViewById(R.id.text_view);
                    textView.setBackgroundResource(BackgroundRect.getBackgroundResource(itemType.getColor(), mFragment.getContext()));
                    textView.setText(String.format("%s%s %d", itemType.getDescription(), mFragment.getString(R.string.colon), count));
                    holder.mItemSummaryCountLinearLayout.addView(view);
                    itemSummaryTotal = itemSummaryTotal + count;
                }
                holder.mItemCountTextView.setText(String.format("%s %d", mFragment.getString(R.string.class_activities_label), itemSummaryTotal));
                holder.mItemCountTextView.setVisibility(View.VISIBLE);
                holder.mItemSummaryCountLinearLayout.setVisibility(View.VISIBLE);
                if(holder.mStudentCountTextView.getVisibility() == View.GONE) {
                    int paddingTop = holder.mTitleTextView.getPaddingTop();
                    int paddingRight = holder.mItemCountTextView.getPaddingRight();
                    int paddingBottom = holder.mItemCountTextView.getPaddingBottom();
                    int paddingLeft = holder.mItemCountTextView.getPaddingLeft();
                    holder.mItemCountTextView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                }
                else {
                    int paddingRight = holder.mItemCountTextView.getPaddingRight();
                    int paddingBottom = holder.mItemCountTextView.getPaddingBottom();
                    int paddingLeft = holder.mItemCountTextView.getPaddingLeft();
                    holder.mItemCountTextView.setPadding(paddingLeft, 0, paddingRight, paddingBottom);
                }
            }

            if(holder.mClassScheduleTimeLocationTextView.getVisibility() == View.GONE)
                holder.mClassScheduleDivider.setVisibility(View.GONE);
            else
                holder.mClassScheduleDivider.setVisibility(View.VISIBLE);
            if(holder.mStudentCountTextView.getVisibility() == View.GONE && holder.mItemCountTextView.getVisibility() == View.GONE)
                holder.mStudentCountDivider.setVisibility(View.GONE);
            else
                holder.mStudentCountDivider.setVisibility(View.VISIBLE);
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            protected LinearLayout mBackgroundLayout;
            protected LinearLayout mClickableLayout;
            protected TextView mTitleTextView;
            protected ImageView mLockedImageView;
            protected TextView mAcademicTermTextView;
            protected View mClassScheduleDivider;
            protected TextView mClassScheduleTimeLocationTextView;
            protected View mStudentCountDivider;
            protected TextView mStudentCountTextView;
            protected TextView mItemCountTextView;
            protected LinearLayout mItemSummaryCountLinearLayout;


            public ViewHolder(View itemView) {
                super(itemView);
                mBackgroundLayout = (LinearLayout) itemView.findViewById(R.id.background_layout);
                mClickableLayout = (LinearLayout) itemView.findViewById(R.id.clickable_layout);
                mTitleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
                mLockedImageView = (ImageView) itemView.findViewById(R.id.locked_image_view);
                mAcademicTermTextView = (TextView) itemView.findViewById(R.id.academic_term_text_view);
                mClassScheduleDivider = itemView.findViewById(R.id.class_schedule_divider);
                mClassScheduleTimeLocationTextView = (TextView) itemView.findViewById(R.id.class_schedule_time_location_text_view);
                mStudentCountDivider = itemView.findViewById(R.id.student_count_divider);
                mStudentCountTextView = (TextView) itemView.findViewById(R.id.student_count_text_view);
                mItemCountTextView = (TextView) itemView.findViewById(R.id.item_count_text_view);
                mItemSummaryCountLinearLayout = (LinearLayout) itemView.findViewById(R.id.item_summary_count_linear_layout);
            }
        }
    }

    public static class ClassSummary {
        public ClassContract.ClassEntry mClass;
        public ArrayList<ClassScheduleContract.ClassScheduleEntry> mClassSchedules;
        public long mStudentCount;
        public LinkedHashMap<ClassItemTypeContract.ClassItemTypeEntry, Integer> mItemSummaryCount;

        public ClassSummary(ClassContract.ClassEntry _class) {
            mClass = _class;
            mClassSchedules = new ArrayList<>();
            mStudentCount = 0;
            mItemSummaryCount = new LinkedHashMap<>();
        }
    }
}
