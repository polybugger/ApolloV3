package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.TextView;

import net.polybugger.apollot.db.ClassStudentContract;

public class ClassStudentDeleteDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onConfirmDeleteClassStudent(ClassStudentContract.ClassStudentEntry entry);
    }

    public static final String TAG = "net.polybugger.apollot.delete_class_student_dialog_fragment";
    public static final String ENTRY_ARG = "net.polybugger.apollot.entry_arg";

    private Listener mListener;
    private TextView mTitleTextView;

    public static ClassStudentDeleteDialogFragment newInstance(ClassStudentContract.ClassStudentEntry entry) {
        ClassStudentDeleteDialogFragment df = new ClassStudentDeleteDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ENTRY_ARG, entry);
        df.setArguments(args);
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final ClassStudentContract.ClassStudentEntry entry = (ClassStudentContract.ClassStudentEntry) args.getSerializable(ENTRY_ARG);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_class_student_delete, null);
        mTitleTextView = (TextView) view.findViewById(R.id.text_view);
        mTitleTextView.setText(entry.getStudent().getName(getContext()));

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_class_student_title)
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
                        mListener.onConfirmDeleteClassStudent(entry);
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
