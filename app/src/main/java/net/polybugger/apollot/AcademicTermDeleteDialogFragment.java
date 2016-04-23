package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.polybugger.apollot.db.AcademicTermContract;

public class AcademicTermDeleteDialogFragment extends DialogFragment {

    public interface Listener {
        void onConfirmDeleteAcademicTerm(AcademicTermContract.AcademicTermEntry entry);
    }

    public static final String TAG = "net.polybugger.apollot.delete_academic_term_dialog_fragment";
    public static final String ENTRY_ARG = "net.polybugger.apollot.entry_arg";

    private Listener mListener;

    public static AcademicTermDeleteDialogFragment newInstance(AcademicTermContract.AcademicTermEntry entry) {
        AcademicTermDeleteDialogFragment df = new AcademicTermDeleteDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ENTRY_ARG, entry);
        df.setArguments(args);
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final AcademicTermContract.AcademicTermEntry entry = (AcademicTermContract.AcademicTermEntry) args.getSerializable(ENTRY_ARG);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_academic_term_delete, null);
        view.findViewById(R.id.background_layout).setBackgroundResource(BackgroundRect.getBackgroundResource(entry.getColor(), getActivity()));
        ((TextView) view.findViewById(R.id.text_view)).setText(entry.getDescription());
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.remove_academic_term)
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.remove, null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onConfirmDeleteAcademicTerm(entry);
                        dismiss();
                    }
                });
            }
        });
        return alertDialog;
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

}
