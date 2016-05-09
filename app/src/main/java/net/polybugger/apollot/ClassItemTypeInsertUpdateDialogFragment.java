package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import net.polybugger.apollot.db.ClassItemTypeContract;

import org.apache.commons.lang3.StringUtils;

public class ClassItemTypeInsertUpdateDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onConfirmInsertUpdateClassItemType(ClassItemTypeContract.ClassItemTypeEntry entry);
    }

    public static final String TAG = "net.polybugger.apollot.insert_update_class_item_type_dialog_fragment";
    public static final String ENTRY_ARG = "net.polybugger.apollot.entry_arg";
    public static final String TITLE_ARG = "net.polybugger.apollot.title_arg";
    public static final String BUTTON_TEXT_ARG = "net.polybugger.apollot.button_text_arg";

    private Listener mListener;
    private ClassItemTypeContract.ClassItemTypeEntry mEntry;
    private LinearLayout mBackgroundLayout;
    private EditText mEditText;
    private TextView mErrorTextView;
    // private Drawable mForegroundImage;
    private ArrayList<ImageButton> mImageButtons;

    public static ClassItemTypeInsertUpdateDialogFragment newInstance(ClassItemTypeContract.ClassItemTypeEntry entry, String title, String buttonText) {
        ClassItemTypeInsertUpdateDialogFragment df = new ClassItemTypeInsertUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ENTRY_ARG, entry);
        args.putString(TITLE_ARG, title);
        args.putString(BUTTON_TEXT_ARG, buttonText);
        df.setArguments(args);
        return df;
    }

    // TODO add checked foreground
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        mEntry = (ClassItemTypeContract.ClassItemTypeEntry) args.getSerializable(ENTRY_ARG);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_class_item_type_insert_update, null);
        mBackgroundLayout = (LinearLayout) view.findViewById(R.id.background_layout);
        mEditText = (EditText) view.findViewById(R.id.edit_text);
        mErrorTextView = (TextView) view.findViewById(R.id.error_text_view);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                mErrorTextView.setText(" ");
            }
        });

        View.OnClickListener bgClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String color = (String) v.getTag();
                mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(color, getActivity()));
                mBackgroundLayout.setTag(color);
                /*
                for(ImageButton imageButton : mImageButtons) {
                    if(StringUtils.equalsIgnoreCase(color, (CharSequence) imageButton.getTag())) {
                        imageButton.setForeground(mForegroundImage);
                    }
                    else {
                        imageButton.setForeground(null);
                    }
                }
                */
            }
        };
        // mForegroundImage = ContextCompat.getDrawable(getActivity(), R.drawable.ic_bg_on_black_24dp);
        mImageButtons = new ArrayList<>();
        ImageButton imageButton0 = (ImageButton) view.findViewById(R.id.image_button_0);
        imageButton0.setTag(BackgroundRect.getHexString(0));
        imageButton0.setOnClickListener(bgClickListener);
        mImageButtons.add(imageButton0);
        ImageButton imageButton1 = (ImageButton) view.findViewById(R.id.image_button_1);
        imageButton1.setTag(BackgroundRect.getHexString(1));
        imageButton1.setOnClickListener(bgClickListener);
        mImageButtons.add(imageButton1);
        ImageButton imageButton2 = (ImageButton) view.findViewById(R.id.image_button_2);
        imageButton2.setTag(BackgroundRect.getHexString(2));
        imageButton2.setOnClickListener(bgClickListener);
        mImageButtons.add(imageButton2);
        ImageButton imageButton3 = (ImageButton) view.findViewById(R.id.image_button_3);
        imageButton3.setTag(BackgroundRect.getHexString(3));
        imageButton3.setOnClickListener(bgClickListener);
        mImageButtons.add(imageButton3);
        ImageButton imageButton4 = (ImageButton) view.findViewById(R.id.image_button_4);
        imageButton4.setTag(BackgroundRect.getHexString(4));
        imageButton4.setOnClickListener(bgClickListener);
        mImageButtons.add(imageButton4);
        ImageButton imageButton5 = (ImageButton) view.findViewById(R.id.image_button_5);
        imageButton5.setTag(BackgroundRect.getHexString(5));
        imageButton5.setOnClickListener(bgClickListener);
        mImageButtons.add(imageButton5);
        ImageButton imageButton6 = (ImageButton) view.findViewById(R.id.image_button_6);
        imageButton6.setTag(BackgroundRect.getHexString(6));
        imageButton6.setOnClickListener(bgClickListener);
        mImageButtons.add(imageButton6);
        ImageButton imageButton7 = (ImageButton) view.findViewById(R.id.image_button_7);
        imageButton7.setTag(BackgroundRect.getHexString(7));
        imageButton7.setOnClickListener(bgClickListener);
        mImageButtons.add(imageButton7);

        if(mEntry == null)
            mEntry = new ClassItemTypeContract.ClassItemTypeEntry(-1, "", null);
        else {
            mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(mEntry.getColor(), getActivity()));
            mBackgroundLayout.setTag(mEntry.getColor());
            mEditText.setText(mEntry.getDescription());
            mEditText.setSelection(mEditText.getText().length());
            /*
            for(ImageButton imageButton : mImageButtons) {
                if(StringUtils.equalsIgnoreCase(mEntry.getColor(), (CharSequence) imageButton.getTag())) {
                    imageButton.setForeground(mForegroundImage);
                }
                else {
                    imageButton.setForeground(null);
                }
            }
            */
        }
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(args.getString(TITLE_ARG))
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(args.getString(BUTTON_TEXT_ARG), null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String description = mEditText.getText().toString();
                        if(StringUtils.isEmpty(description)) {
                            mErrorTextView.setText(R.string.please_enter_a_class_activity);
                            mEditText.requestFocus();
                            return;
                        }
                        mEntry.setDescription(description);
                        mEntry.setColor((String) mBackgroundLayout.getTag());
                        mListener.onConfirmInsertUpdateClassItemType(mEntry);
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
