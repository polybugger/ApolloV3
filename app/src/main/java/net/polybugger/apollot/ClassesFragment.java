package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import net.polybugger.apollot.db.AcademicTermContract;
import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassItemTypeContract;
import net.polybugger.apollot.db.ClassScheduleContract;
import net.polybugger.apollot.db.PastCurrentEnum;

import org.apache.commons.lang3.StringUtils;

public class ClassesFragment extends Fragment implements MainActivityFragment.Listener {

    public static final String TAG_PAST = "net.polybugger.apollot.past_classes_fragment";
    public static final String TAG_CURRENT = "net.polybugger.apollot.current_classes_fragment";
    public static final String PAST_CURRENT_ARG = "net.polybugger.apollot.past_current_arg";

    public static boolean REQUERY = false;

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
        mAdapter = new Adapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        MainActivityFragment f = (MainActivityFragment) getFragmentManager().findFragmentByTag(MainActivityFragment.TAG);
        f.getClassesSummary(mPastCurrent);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(REQUERY) {
            MainActivityFragment f = (MainActivityFragment) getFragmentManager().findFragmentByTag(MainActivityFragment.TAG);
            f.getClassesSummary(mPastCurrent);
            REQUERY = false;
        }
        else {

        }
    }

    @Override
    public void onGetClassesSummary(ArrayList<ClassSummary> arrayList, PastCurrentEnum pastCurrent) {
        mAdapter.setArrayList(arrayList);
    }


    public static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private FragmentActivity mActivity;
        private ArrayList<ClassSummary> mArrayList;

        public Adapter(FragmentActivity activity) {
            mActivity = activity;
            mArrayList = new ArrayList<>();
        }

        public void setArrayList(ArrayList<ClassSummary> arrayList) {
            mArrayList = arrayList;
            notifyDataSetChanged();
        }

        /*
        public void add(AcademicTermContract.AcademicTermEntry entry) {
            mArrayList.add(entry);
            notifyDataSetChanged();
        }

        public void update(AcademicTermContract.AcademicTermEntry entry) {
            int pos = mArrayList.indexOf(entry);
            mArrayList.remove(pos);
            mArrayList.add(pos, entry);
            notifyDataSetChanged();
        }

        public void remove(AcademicTermContract.AcademicTermEntry entry) {
            mArrayList.remove(entry);
            notifyDataSetChanged();
        }
        */

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
                holder.mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(academicTerm.getColor(), mActivity));
            Resources res = mActivity.getResources();
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
            holder.mClickableLayout.setTag(entry);
            holder.mClickableLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.mTitleTextView.setText(entry.mClass.getTitle());
            String academicTermYear = entry.mClass.getAcademicTermYear();
            if(StringUtils.isBlank(academicTermYear)) {
                holder.mAcademicTermTextView.setVisibility(View.GONE);
            }
            else {
                holder.mAcademicTermTextView.setText(academicTermYear);
                holder.mAcademicTermTextView.setVisibility(View.VISIBLE);
            }
            if(entry.mClassSchedules.size() == 0) {
                holder.mClassScheduleTimeTextView.setVisibility(View.GONE);
                holder.mClassScheduleLocationTextView.setVisibility(View.GONE);
            }
            else {
                ClassScheduleContract.ClassScheduleEntry classSchedule = entry.mClassSchedules.get(0);
                String time = classSchedule.getTime(ApolloDbAdapter.getAppContext());
                if(entry.mClassSchedules.size() > 1)
                    time = time + " ...";
                holder.mClassScheduleTimeTextView.setText(time);
                holder.mClassScheduleTimeTextView.setVisibility(View.VISIBLE);
                String location = classSchedule.getLocation();
                if(StringUtils.isBlank(location)) {
                    holder.mClassScheduleLocationTextView.setVisibility(View.GONE);
                }
                else {
                    holder.mClassScheduleLocationTextView.setText(location);
                    holder.mClassScheduleLocationTextView.setVisibility(View.VISIBLE);
                }
            }
            if(entry.mStudentCount == 0) {
                holder.mStudentCountTextView.setVisibility(View.GONE);
            }
            else {
                holder.mStudentCountTextView.setText(String.format("%s %d", mActivity.getString(R.string.students_label), entry.mStudentCount));
                holder.mStudentCountTextView.setVisibility(View.VISIBLE);
            }

            if(entry.mItemSummaryCount.size() == 0) {
                holder.mItemCountTextView.setVisibility(View.GONE);
                holder.mItemSummaryCountLinearLayout.setVisibility(View.GONE);
            }
            else {
                LayoutInflater inflater = mActivity.getLayoutInflater();
                int itemSummaryTotal = 0, count;
                View view; TextView textView;
                for(Map.Entry<ClassItemTypeContract.ClassItemTypeEntry, Integer> itemCount : entry.mItemSummaryCount.entrySet()) {
                    ClassItemTypeContract.ClassItemTypeEntry itemType = itemCount.getKey();
                    count = itemCount.getValue();
                    view = inflater.inflate(R.layout.row_class_summary_item_count, null);
                    textView = (TextView) view.findViewById(R.id.text_view);
                    textView.setBackgroundResource(BackgroundRect.getBackgroundResource(itemType.getColor(), mActivity));
                    textView.setText(String.format("%s%s %d", itemType.getDescription(), mActivity.getString(R.string.colon), count));
                    holder.mItemSummaryCountLinearLayout.addView(view);
                    itemSummaryTotal = itemSummaryTotal + count;
                }
                holder.mItemCountTextView.setText(String.format("%s %d", mActivity.getString(R.string.class_activities_label), itemSummaryTotal));
                holder.mItemCountTextView.setVisibility(View.VISIBLE);
                holder.mItemSummaryCountLinearLayout.setVisibility(View.VISIBLE);
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
            protected TextView mAcademicTermTextView;
            protected TextView mClassScheduleTimeTextView;
            protected TextView mClassScheduleLocationTextView;
            protected TextView mStudentCountTextView;
            protected TextView mItemCountTextView;
            protected LinearLayout mItemSummaryCountLinearLayout;


            public ViewHolder(View itemView) {
                super(itemView);
                mBackgroundLayout = (LinearLayout) itemView.findViewById(R.id.background_layout);
                mClickableLayout = (LinearLayout) itemView.findViewById(R.id.clickable_layout);
                mTitleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
                mAcademicTermTextView = (TextView) itemView.findViewById(R.id.academic_term_text_view);
                mClassScheduleTimeTextView = (TextView) itemView.findViewById(R.id.class_schedule_time_text_view);
                mClassScheduleLocationTextView = (TextView) itemView.findViewById(R.id.class_schedule_location_text_view);
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
        }
    }
}
