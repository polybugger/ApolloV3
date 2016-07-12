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

import net.polybugger.apollot.db.AcademicTermContract;
import net.polybugger.apollot.db.ClassContract;

import org.apache.commons.lang3.StringUtils;

public class ClassDeleteDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onConfirmDeleteClass(ClassContract.ClassEntry entry);
    }

    public static final String TAG = "net.polybugger.apollot.delete_class_dialog_fragment";
    public static final String ENTRY_ARG = "net.polybugger.apollot.entry_arg";

    private Listener mListener;
    private TextView mTitleTextView;
    private TextView mAcademicTermTextView;

    public static ClassDeleteDialogFragment newInstance(ClassContract.ClassEntry entry) {
        ClassDeleteDialogFragment df = new ClassDeleteDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ENTRY_ARG, entry);
        df.setArguments(args);
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final ClassContract.ClassEntry entry = (ClassContract.ClassEntry) args.getSerializable(ENTRY_ARG);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_class_delete, null);
        AcademicTermContract.AcademicTermEntry academicTerm = entry.getAcademicTerm();
        if(academicTerm != null)
            view.findViewById(R.id.background_layout).setBackgroundResource(BackgroundRect.getBackgroundResource(academicTerm.getColor(), getActivity()));
        mTitleTextView = (TextView) view.findViewById(R.id.text_view);
        mTitleTextView.setText(entry.getTitle());
        mAcademicTermTextView = (TextView) view.findViewById(R.id.academic_term_text_view);
        String academicTermYear = entry.getAcademicTermYear();
        if(StringUtils.isBlank(academicTermYear)) {
            mAcademicTermTextView.setVisibility(View.GONE);
        }
        else {
            mAcademicTermTextView.setText(academicTermYear);
            mAcademicTermTextView.setVisibility(View.VISIBLE);
        }

        int paddingTop = mTitleTextView.getPaddingTop();
        int paddingRight = mTitleTextView.getPaddingRight();
        int paddingBottom = mTitleTextView.getPaddingBottom();
        int paddingLeft = mTitleTextView.getPaddingLeft();
        if(mAcademicTermTextView.getVisibility() == View.GONE)
            mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        else
            mTitleTextView.setPadding(paddingLeft, paddingTop, paddingRight, 0);

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_class_title)
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
                        mListener.onConfirmDeleteClass(entry);
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
