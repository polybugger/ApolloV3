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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassStudentContract;

public class ClassStudentsFragment extends Fragment {

    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";

    public static boolean REQUERY = false;
    public static boolean DELETE_CLASS_STUDENT = false;
    public static ClassStudentContract.ClassStudentEntry CLASS_STUDENT = null;

    private ClassContract.ClassEntry mClass;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;

    public static ClassStudentsFragment newInstance(ClassContract.ClassEntry _class) {
        ClassStudentsFragment f = new ClassStudentsFragment();
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
            rf.getClassStudentsSummary(mClass, getTag());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(DELETE_CLASS_STUDENT) {
            mAdapter.removeByClassStudent(CLASS_STUDENT);
            DELETE_CLASS_STUDENT = false;
            CLASS_STUDENT = null;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), getString(R.string.class_student_deleted), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    public void requeryClass(ClassContract.ClassEntry _class) {
        mClass = _class;
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_class_students, menu);
    }

    public void onGetClassStudentsSummary(ArrayList<ClassStudentSummary> arrayList, String fragmentTag) {
        mAdapter.setArrayList(arrayList);
    }

    public void insertClassStudent(ClassStudentContract.ClassStudentEntry classStudent, long id, String fragmentTag) {
        if(id != -1) {
            classStudent.setId(id);
            ClassStudentSummary classStudentSummary = new ClassStudentSummary(classStudent);
            mAdapter.add(classStudentSummary);
            mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), getString(R.string.class_student_added), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    public static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private Fragment mFragment;
        private ArrayList<ClassStudentSummary> mArrayList;

        private int mSortId;

        private Comparator<ClassStudentSummary> mComparator;

        public Adapter(Fragment fragment) {
            mFragment = fragment;
            mArrayList = new ArrayList<>();
            mComparator = new Comparator<ClassStudentSummary>() {
                @Override
                public int compare(ClassStudentSummary lhs, ClassStudentSummary rhs) {
                    if(mSortId == R.id.action_sort_last_name) {
                        return lhs.mClassStudent.getStudent().getLastName().compareToIgnoreCase(rhs.mClassStudent.getStudent().getLastName());
                    }
                    else if(-mSortId == R.id.action_sort_last_name) {
                        return -lhs.mClassStudent.getStudent().getLastName().compareToIgnoreCase(rhs.mClassStudent.getStudent().getLastName());
                    }
                    if(mSortId == R.id.action_sort_first_name) {
                        return lhs.mClassStudent.getStudent().getFirstName().compareToIgnoreCase(rhs.mClassStudent.getStudent().getFirstName());
                    }
                    else if(-mSortId == R.id.action_sort_first_name) {
                        return -lhs.mClassStudent.getStudent().getFirstName().compareToIgnoreCase(rhs.mClassStudent.getStudent().getFirstName());
                    }
                    return 0;
                }
            };
        }

        public void setArrayList(ArrayList<ClassStudentSummary> arrayList) {
            mArrayList = arrayList;
            notifyDataSetChanged();
        }

        public void add(ClassStudentSummary entry) {
            mArrayList.add(entry);
            notifyDataSetChanged();
        }

        public ClassStudentSummary removeByClassStudent(ClassStudentContract.ClassStudentEntry classStudent) {
            ClassStudentSummary classStudentSummary = null;
            int size = mArrayList.size();
            for(int i = 0; i < size; ++i) {
                classStudentSummary = mArrayList.get(i);
                if(classStudentSummary.mClassStudent.equals(classStudent)) {
                    mArrayList.remove(i);
                    break;
                }
            }
            notifyDataSetChanged();
            return classStudentSummary;
        }

        public void update(ClassStudentSummary classStudentSummary) {
            ClassStudentSummary tmpClassStudentSummary;
            int size = mArrayList.size(), pos = size;
            for(int i = 0; i < size; ++i) {
                tmpClassStudentSummary = mArrayList.get(i);
                if(tmpClassStudentSummary.mClassStudent.equals(classStudentSummary.mClassStudent)) {
                    pos = i;
                    break;
                }
            }
            if(pos < size)
                mArrayList.remove(pos);
            mArrayList.add(pos, classStudentSummary);
            notifyDataSetChanged();
        }

        public void sortBy(int sortId) {
            mSortId = (mSortId == sortId) ? -sortId : sortId;
            Collections.sort(mArrayList, mComparator);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_class_student_summary, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ClassStudentSummary entry = mArrayList.get(position);

            Resources res = mFragment.getResources();
            int topMargin = res.getDimensionPixelSize(R.dimen.recycler_view_item_margin_top);
            int rightMargin = res.getDimensionPixelSize(R.dimen.recycler_view_item_margin_right);
            int bottomMargin = res.getDimensionPixelSize(R.dimen.recycler_view_item_margin_bottom);
            int leftMargin = res.getDimensionPixelSize(R.dimen.recycler_view_item_margin_left);
            if(position == 0) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.mLinearLayout.getLayoutParams();
                layoutParams.setMargins(leftMargin, topMargin * 2, rightMargin, bottomMargin);
                holder.mLinearLayout.setLayoutParams(layoutParams);
                holder.mDivider.setVisibility(View.VISIBLE);
            }
            else if(position == (mArrayList.size() - 1)) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.mLinearLayout.getLayoutParams();
                layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin * 2);
                holder.mLinearLayout.setLayoutParams(layoutParams);
                holder.mDivider.setVisibility(View.GONE);
            }
            else {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.mLinearLayout.getLayoutParams();
                layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                holder.mLinearLayout.setLayoutParams(layoutParams);
                holder.mDivider.setVisibility(View.VISIBLE);
            }
            holder.mClickableLayout.setTag(entry);
            holder.mClickableLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClassStudentSummary classStudentSummary = (ClassStudentSummary) v.getTag();
                    Intent intent = new Intent(mFragment.getContext(), ClassStudentActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable(ClassStudentActivity.CLASS_ARG, ((ClassActivity) mFragment.getActivity()).getClassEntry());
                    args.putSerializable(ClassStudentActivity.CLASS_STUDENT_ARG, classStudentSummary.mClassStudent);
                    intent.putExtras(args);
                    mFragment.startActivity(intent);
                }
            });

            holder.mNameTextView.setText(entry.mClassStudent.getStudent().getName(mFragment.getContext()));
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            protected LinearLayout mClickableLayout;
            protected LinearLayout mLinearLayout;
            protected TextView mNameTextView;
            protected View mDivider;

            public ViewHolder(View itemView) {
                super(itemView);
                mClickableLayout = (LinearLayout) itemView.findViewById(R.id.clickable_layout);
                mLinearLayout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
                mNameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
                mDivider = itemView.findViewById(R.id.divider);
            }
        }
    }

    public static class ClassStudentSummary {
        public ClassStudentContract.ClassStudentEntry mClassStudent;

        public ClassStudentSummary(ClassStudentContract.ClassStudentEntry classStudent) {
            mClassStudent = classStudent;
        }
    }
}
