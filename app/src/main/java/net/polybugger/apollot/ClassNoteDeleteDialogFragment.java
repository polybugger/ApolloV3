package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import net.polybugger.apollot.db.ClassItemNoteContract;
import net.polybugger.apollot.db.ClassNoteContract;

public class ClassNoteDeleteDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onConfirmDeleteClassNote(ClassNoteContract.ClassNoteEntry entry, String fragmentTag, ClassItemNoteContract.ClassItemNoteEntry itemNote, boolean isClassNote);
    }

    public static final String TAG = "net.polybugger.apollot.delete_class_note_dialog_fragment";
    public static final String ENTRY_ARG = "net.polybugger.apollot.entry_arg";
    public static final String FRAGMENT_TAG_ARG = "net.polybugger.apollot.fragment_tag_arg";
    public static final String ITEM_NOTE_ARG = "net.polybugger.apollot.item_note_arg";
    public static final String IS_CLASS_NOTE_ARG = "net.polybugger.apollot.is_class_note_arg";

    private Listener mListener;
    private boolean mIsClassNote;

    public static ClassNoteDeleteDialogFragment newInstance(ClassNoteContract.ClassNoteEntry entry, String fragmentTag, ClassItemNoteContract.ClassItemNoteEntry itemNote, boolean isClassNote) {
        ClassNoteDeleteDialogFragment df = new ClassNoteDeleteDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ENTRY_ARG, entry);
        args.putString(FRAGMENT_TAG_ARG, fragmentTag);
        args.putSerializable(ITEM_NOTE_ARG, itemNote);
        args.putBoolean(IS_CLASS_NOTE_ARG, isClassNote);
        df.setArguments(args);
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final ClassNoteContract.ClassNoteEntry entry = (ClassNoteContract.ClassNoteEntry) args.getSerializable(ENTRY_ARG);
        final ClassItemNoteContract.ClassItemNoteEntry itemNote = (ClassItemNoteContract.ClassItemNoteEntry) args.getSerializable(ITEM_NOTE_ARG);
        mIsClassNote = args.getBoolean(IS_CLASS_NOTE_ARG);
        final String fragmentTag = args.getString(FRAGMENT_TAG_ARG);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_class_note_delete, null);
        ((TextView) view.findViewById(R.id.date_created_note_text_view)).setText(Html.fromHtml(mIsClassNote ? entry.getDateCreatedNote(getContext()) : itemNote.getDateCreatedNote(getContext())));
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.remove_class_note)
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
                        mListener.onConfirmDeleteClassNote(entry, fragmentTag, itemNote, mIsClassNote);
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
