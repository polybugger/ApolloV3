package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassItemNoteContract;
import net.polybugger.apollot.db.ClassNoteContract;
import net.polybugger.apollot.db.DateTimeFormat;

import org.apache.commons.lang3.StringUtils;

public class ClassNoteInsertUpdateDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onConfirmInsertUpdateClassNote(ClassNoteContract.ClassNoteEntry entry, String fragmentTag, ClassItemNoteContract.ClassItemNoteEntry itemNote, boolean isClassNote);
    }

    public static final String TAG = "net.polybugger.apollot.insert_update_class_note_dialog_fragment";
    public static final String ENTRY_ARG = "net.polybugger.apollot.entry_arg";
    public static final String TITLE_ARG = "net.polybugger.apollot.title_arg";
    public static final String BUTTON_TEXT_ARG = "net.polybugger.apollot.button_text_arg";
    public static final String FRAGMENT_TAG_ARG = "net.polybugger.apollot.fragment_tag_arg";
    public static final String ITEM_NOTE_ARG = "net.polybugger.apollot.item_note_arg";
    public static final String IS_CLASS_NOTE_ARG = "net.polybugger.apollot.is_class_note_arg";

    private Listener mListener;
    private ClassNoteContract.ClassNoteEntry mEntry;
    private AlertDialog mAlertDialog;
    private Button mNoteDateButton;
    private TextView mNoteDateErrorTextView;
    private EditText mNoteEditText;
    private TextView mNoteErrorTextView;
    private ClassItemNoteContract.ClassItemNoteEntry mItemNote;
    private boolean mIsClassNote;

    public static ClassNoteInsertUpdateDialogFragment newInstance(ClassNoteContract.ClassNoteEntry entry, String title, String buttonText, String fragmentTag, ClassItemNoteContract.ClassItemNoteEntry itemNote, boolean isClassNote) {
        ClassNoteInsertUpdateDialogFragment df = new ClassNoteInsertUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ENTRY_ARG, entry);
        args.putString(TITLE_ARG, title);
        args.putString(BUTTON_TEXT_ARG, buttonText);
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
        mEntry = (ClassNoteContract.ClassNoteEntry) args.getSerializable(ENTRY_ARG);
        mItemNote = (ClassItemNoteContract.ClassItemNoteEntry) args.getSerializable(ITEM_NOTE_ARG);
        mIsClassNote = args.getBoolean(IS_CLASS_NOTE_ARG);

        final String fragmentTag = args.getString(FRAGMENT_TAG_ARG);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_class_note_insert_update, null);
        mNoteDateButton = (Button) view.findViewById(R.id.note_date_button);
        mNoteDateErrorTextView = (TextView) view.findViewById(R.id.note_date_error_text_view);
        mNoteEditText = (EditText) view.findViewById(R.id.note_edit_text);
        mNoteErrorTextView = (TextView) view.findViewById(R.id.note_error_text_view);
        mNoteDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerDialogFragment df = (DatePickerDialogFragment) fm.findFragmentByTag(DatePickerDialogFragment.TAG);
                if(df == null) {
                    df = DatePickerDialogFragment.newInstance((Date) v.getTag(), getString(R.string.note_date_hint), getTag(), R.id.note_date_button);
                    df.show(fm, DatePickerDialogFragment.TAG);
                }
            }
        });
        mNoteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                int charLimit = getResources().getInteger(R.integer.note_char_limit);
                int charsLeft = charLimit - s.length();
                if(charsLeft < charLimit)
                    mNoteErrorTextView.setText(String.format("%d %s", charsLeft, getString(R.string.characters_left)));
                else
                    mNoteErrorTextView.setText(" ");
            }
        });
        Context context = getContext();
        final SimpleDateFormat sdf;
        if(StringUtils.equalsIgnoreCase(context.getResources().getConfiguration().locale.getLanguage(), ApolloDbAdapter.JA_LANGUAGE))
            sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE_JA, context.getResources().getConfiguration().locale);
        else
            sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE, context.getResources().getConfiguration().locale);

        if(mEntry == null)
            mEntry = new ClassNoteContract.ClassNoteEntry(-1, -1, "", new Date());
        if(mItemNote == null)
            mItemNote = new ClassItemNoteContract.ClassItemNoteEntry(-1, -1, -1, "", new Date());
        Date noteDate = mIsClassNote ? mEntry.getDateCreated() : mItemNote.getDateCreated();
        if(noteDate != null) {
            mNoteDateButton.setText(sdf.format(noteDate));
            mNoteDateButton.setTag(noteDate);
        }
        mNoteEditText.setText(mIsClassNote ? mEntry.getNote() : mItemNote.getNote());
        mAlertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(args.getString(TITLE_ARG))
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(args.getString(BUTTON_TEXT_ARG), null)
                .create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Date noteDate = (Date) mNoteDateButton.getTag();
                        if(noteDate == null) {
                            mNoteDateErrorTextView.setText(R.string.please_input_a_note_date);
                            mNoteDateButton.requestFocus();
                            return;
                        }
                        String note = mNoteEditText.getText().toString();
                        if(StringUtils.isEmpty(note)) {
                            mNoteErrorTextView.setText(R.string.please_write_a_note);
                            mNoteEditText.requestFocus();
                            return;
                        }
                        mEntry.setDateCreated(noteDate);
                        mEntry.setNote(note);
                        mItemNote.setDateCreated(noteDate);
                        mItemNote.setNote(note);
                        mListener.onConfirmInsertUpdateClassNote(mEntry, fragmentTag, mItemNote, mIsClassNote);
                        dismiss();
                    }
                });
            }
        });
        return mAlertDialog;
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

    public void setButtonDate(Date date, int buttonId) {
        Context context = getContext();
        final SimpleDateFormat sdf;
        if(StringUtils.equalsIgnoreCase(context.getResources().getConfiguration().locale.getLanguage(), ApolloDbAdapter.JA_LANGUAGE))
            sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE_JA, context.getResources().getConfiguration().locale);
        else
            sdf = new SimpleDateFormat(DateTimeFormat.DATE_DISPLAY_TEMPLATE, context.getResources().getConfiguration().locale);
        if(date != null) {
            mNoteDateButton.setText(sdf.format(date));
            mNoteDateErrorTextView.setText(" ");
        }
        else {
            mNoteDateButton.setText(null);
        }
        mNoteDateButton.setTag(date);
    }
}
