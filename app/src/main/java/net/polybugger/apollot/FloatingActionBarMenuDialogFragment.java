package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class FloatingActionBarMenuDialogFragment extends AppCompatDialogFragment {

    public static final String TAG = "net.polybugger.apollot.floating_action_bar_menu_dialog_fragment";
    public static final String FRAGMENT_TAG_ARG = "net.polybugger.apollot.fragment_tag_arg";
    public static final String FAB_MODE_ARG = "net.polybugger.apollot.fab_mode_arg";

    public static FloatingActionBarMenuDialogFragment newInstance(String fragmentTag, FABMode fabMode) {
        FloatingActionBarMenuDialogFragment df = new FloatingActionBarMenuDialogFragment();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TAG_ARG, fragmentTag);
        args.putSerializable(FAB_MODE_ARG, fabMode);
        df.setArguments(args);
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final String fragmentTag = args.getString(FRAGMENT_TAG_ARG);
        FABMode fabMode = (FABMode) args.getSerializable(FAB_MODE_ARG);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_floating_action_bar_menu, null);

        switch(fabMode) {
            case CLASS_INFO_FRAGMENT:
                view.findViewById(R.id.fragment_class_info_layout).setVisibility(View.VISIBLE);
                view.findViewById(R.id.new_schedule_clickable_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        FragmentManager fm = getFragmentManager();
                        ClassScheduleInsertUpdateDialogFragment df = (ClassScheduleInsertUpdateDialogFragment) fm.findFragmentByTag(ClassScheduleInsertUpdateDialogFragment.TAG);
                        if(df == null) {
                            df = ClassScheduleInsertUpdateDialogFragment.newInstance(null, getString(R.string.new_class_schedule), getString(R.string.add), fragmentTag);
                            df.show(fm, ClassScheduleInsertUpdateDialogFragment.TAG);
                        }
                    }
                });
                view.findViewById(R.id.new_grade_breakdown_clickable_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        FragmentManager fm = getFragmentManager();
                        ClassGradeBreakdownInsertUpdateDialogFragment df = (ClassGradeBreakdownInsertUpdateDialogFragment) fm.findFragmentByTag(ClassGradeBreakdownInsertUpdateDialogFragment.TAG);
                        if(df == null) {
                            df = ClassGradeBreakdownInsertUpdateDialogFragment.newInstance(null, getString(R.string.new_class_grade_breakdown), getString(R.string.add), fragmentTag);
                            df.show(fm, ClassGradeBreakdownInsertUpdateDialogFragment.TAG);
                        }
                    }
                });
                view.findViewById(R.id.new_note_clickable_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        FragmentManager fm = getFragmentManager();
                        ClassNoteInsertUpdateDialogFragment df = (ClassNoteInsertUpdateDialogFragment) fm.findFragmentByTag(ClassNoteInsertUpdateDialogFragment.TAG);
                        if(df == null) {
                            df = ClassNoteInsertUpdateDialogFragment.newInstance(null, getString(R.string.new_class_note), getString(R.string.add), fragmentTag, null, true);
                            df.show(fm, ClassNoteInsertUpdateDialogFragment.TAG);
                        }
                    }
                });
                break;
            case CLASS_STUDENTS_FRAGMENT:
                view.findViewById(R.id.fragment_class_students_layout).setVisibility(View.VISIBLE);
                view.findViewById(R.id.new_student_clickable_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        FragmentManager fm = getFragmentManager();
                        ClassStudentInsertUpdateDialogFragment df = (ClassStudentInsertUpdateDialogFragment) fm.findFragmentByTag(ClassStudentInsertUpdateDialogFragment.TAG);
                        if(df == null) {
                            df = ClassStudentInsertUpdateDialogFragment.newInstance(null, getString(R.string.new_class_student), getString(R.string.add), fragmentTag);
                            df.show(fm, ClassStudentInsertUpdateDialogFragment.TAG);
                        }
                    }
                });
                view.findViewById(R.id.existing_student_clickable_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        FragmentManager fm = getFragmentManager();
                        ClassStudentInsertExistingDialogFragment df = (ClassStudentInsertExistingDialogFragment) fm.findFragmentByTag(ClassStudentInsertExistingDialogFragment.TAG);
                        if(df == null) {
                            ClassActivity pa = (ClassActivity) getActivity();
                            df = ClassStudentInsertExistingDialogFragment.newInstance(pa.getClassEntry(), getString(R.string.existing_students), getString(R.string.add), fragmentTag);
                            df.show(fm, ClassStudentInsertExistingDialogFragment.TAG);
                        }
                    }
                });
                break;
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(null)
                .setView(view)
                .setNegativeButton(null, null)
                .setPositiveButton(null, null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Window window = alertDialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(0));
                WindowManager.LayoutParams windowParams = window.getAttributes();
                windowParams.dimAmount = 0f;
                windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                int sourceX = 0;
                int sourceY = 0;
                window.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
                Resources res = getResources();
                //windowParams.x = sourceX + res.getDimensionPixelSize(R.dimen.fab_margin); // dpToPx(rightMargin); // about half of confirm button size left of source view
                int bottomMargin = res.getDimensionPixelSize(R.dimen.tab_height_fab_margin) + res.getDimensionPixelSize(R.dimen.fab_size);
                windowParams.y = sourceY + bottomMargin; // above source view
                window.setAttributes(windowParams);
            }
        });
        return alertDialog;
    }

    public int dpToPx(float valueInDp) {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if(context instanceof Activity) {
            try {
                mListener = (Listener) context;
            }
            catch(ClassCastException e) {
                throw new ClassCastException(context.toString() + " must implement " + Listener.class.toString());
            }
        }
        */
    }

    @Override
    public void onDetach() {
        // mListener = null;
        super.onDetach();
    }

    public enum FABMode {

        CLASS_INFO_FRAGMENT(0),
        CLASS_STUDENTS_FRAGMENT(1);

        private int mValue;

        private FABMode(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }
}
