package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;

import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.StudentContract;

public class ClassStudentInsertExistingDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onConfirmInsertExistingStudent(ArrayList<Long> studentIds, String fragmentTag);
    }

    public static final String TAG = "net.polybugger.apollot.insert_update_class_item_dialog_fragment";
    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";
    public static final String TITLE_ARG = "net.polybugger.apollot.title_arg";
    public static final String BUTTON_TEXT_ARG = "net.polybugger.apollot.button_text_arg";
    public static final String FRAGMENT_TAG_ARG = "net.polybugger.apollot.fragment_tag_arg";

    private Listener mListener;
    private ClassContract.ClassEntry mClass;
    private AlertDialog mAlertDialog;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;

    public static ClassStudentInsertExistingDialogFragment newInstance(ClassContract.ClassEntry _class, String title, String buttonText, String fragmentTag) {
        ClassStudentInsertExistingDialogFragment df = new ClassStudentInsertExistingDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(CLASS_ARG, _class);
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
        mClass = (ClassContract.ClassEntry) args.getSerializable(CLASS_ARG);
        final String fragmentTag = args.getString(FRAGMENT_TAG_ARG);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_class_student_insert_existing, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new Adapter(this);
        mRecyclerView.setAdapter(mAdapter);

        ClassActivityFragment rf = (ClassActivityFragment) getFragmentManager().findFragmentByTag(ClassActivityFragment.TAG);
        if(rf != null)
            rf.getExistingStudents(mClass, getTag());

        mAlertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(args.getString(TITLE_ARG))
                .setView(view)
                .setNeutralButton(R.string.clear, null)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(args.getString(BUTTON_TEXT_ARG), null)
                .create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<Long> studentIds = mAdapter.getCheckedStudentIds();
                        if(studentIds.size() == 0)
                            return;
                        mListener.onConfirmInsertExistingStudent(mAdapter.getCheckedStudentIds(), fragmentTag);
                        dismiss();
                    }
                });
                mAlertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdapter.clearCheckMarks();
                    }
                });
            }
        });
        return mAlertDialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Listener) activity;
        }
        catch(ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " + Listener.class.toString());
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public void onGetExistingStudents(ArrayList<StudentContract.StudentEntry> arrayList) {
        mAdapter.setArrayList(arrayList);
    }

    public static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private Fragment mFragment;
        private ArrayList<StudentContract.StudentEntry> mArrayList;
        private ArrayList<Boolean> mCheckMarks;

        public Adapter(Fragment fragment) {
            mFragment = fragment;
            mArrayList = new ArrayList<>();
            mCheckMarks = new ArrayList<>();
        }

        public void setArrayList(ArrayList<StudentContract.StudentEntry> arrayList) {
            mArrayList = arrayList;
            mCheckMarks = new ArrayList<>(Collections.nCopies(mArrayList.size(), false));
            notifyDataSetChanged();
        }

        public void clearCheckMarks() {
            int i = 0, size = mCheckMarks.size();
            while(i < size) {
                mCheckMarks.set(i, false);
                ++i;
            }
            notifyDataSetChanged();
        }

        public ArrayList<Long> getCheckedStudentIds() {
            ArrayList<Long> studentIds = new ArrayList<>();
            int i, size = mCheckMarks.size();
            for(i = 0; i < size; ++i)
                if(mCheckMarks.get(i))
                    studentIds.add(mArrayList.get(i).getId());
            return studentIds;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_existing_student, parent, false));
        }

        // TODO adjust formatting for empty fields
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            StudentContract.StudentEntry entry = mArrayList.get(position);
            Resources res = mFragment.getResources();
            int topMargin = res.getDimensionPixelSize(R.dimen.recycler_view_item_margin_top);
            int rightMargin = res.getDimensionPixelSize(R.dimen.recycler_view_item_margin_right);
            int bottomMargin = res.getDimensionPixelSize(R.dimen.recycler_view_item_margin_bottom);
            int leftMargin = res.getDimensionPixelSize(R.dimen.recycler_view_item_margin_left);
            if(position == 0) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.mLinearLayout.getLayoutParams();
                layoutParams.setMargins(leftMargin, topMargin * 2, rightMargin, bottomMargin);
                holder.mLinearLayout.setLayoutParams(layoutParams);
            }
            else if(position == (mArrayList.size() - 1)) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.mLinearLayout.getLayoutParams();
                layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin * 2);
                holder.mLinearLayout.setLayoutParams(layoutParams);
            }
            else {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.mLinearLayout.getLayoutParams();
                layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                holder.mLinearLayout.setLayoutParams(layoutParams);
            }
            holder.mStudentCheckBox.setTag(position);
            holder.mStudentCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = (int) buttonView.getTag();
                    mCheckMarks.set(position, isChecked);
                }
            });
            holder.mStudentCheckBox.setChecked(mCheckMarks.get(position));
            holder.mStudentCheckBox.setText(entry.getName(mFragment.getContext()));
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            protected LinearLayout mLinearLayout;
            protected CheckBox mStudentCheckBox;

            public ViewHolder(View itemView) {
                super(itemView);
                mLinearLayout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
                mStudentCheckBox = (CheckBox) itemView.findViewById(R.id.student_check_box);
            }
        }
    }
}
