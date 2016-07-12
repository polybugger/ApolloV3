package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassGradeBreakdownContract;
import net.polybugger.apollot.db.ClassItemTypeContract;

import org.apache.commons.lang3.StringUtils;

public class ClassGradeBreakdownInsertUpdateDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onConfirmInsertUpdateClassGradeBreakdown(ClassGradeBreakdownContract.ClassGradeBreakdownEntry entry, String fragmentTag);
    }

    public static final String TAG = "net.polybugger.apollot.insert_update_class_grade_breakdown_dialog_fragment";
    public static final String ENTRY_ARG = "net.polybugger.apollot.entry_arg";
    public static final String TITLE_ARG = "net.polybugger.apollot.title_arg";
    public static final String BUTTON_TEXT_ARG = "net.polybugger.apollot.button_text_arg";
    public static final String FRAGMENT_TAG_ARG = "net.polybugger.apollot.fragment_tag_arg";

    private Listener mListener;
    private ClassGradeBreakdownContract.ClassGradeBreakdownEntry mEntry;
    private AlertDialog mAlertDialog;
    private LinearLayout mBackgroundLayout;
    private TextView mItemTypeTextView;
    private Spinner mItemTypeSpinner;
    private TextView mItemTypeErrorTextView;
    private EditText mPercentageEditText;
    private TextView mPercentageErrorTextView;

    public static ClassGradeBreakdownInsertUpdateDialogFragment newInstance(ClassGradeBreakdownContract.ClassGradeBreakdownEntry entry, String title, String buttonText, String fragmentTag) {
        ClassGradeBreakdownInsertUpdateDialogFragment df = new ClassGradeBreakdownInsertUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ENTRY_ARG, entry);
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
        mEntry = (ClassGradeBreakdownContract.ClassGradeBreakdownEntry) args.getSerializable(ENTRY_ARG);
        final String fragmentTag = args.getString(FRAGMENT_TAG_ARG);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_class_grade_breakdown_insert_update, null);
        mBackgroundLayout = (LinearLayout) view.findViewById(R.id.grade_breakdown_background_layout);
        mItemTypeTextView = (TextView) view.findViewById(R.id.item_type_text_view);
        mItemTypeSpinner = (Spinner) view.findViewById(R.id.item_type_spinner);
        mItemTypeErrorTextView = (TextView) view.findViewById(R.id.item_type_error_text_view);
        mPercentageEditText = (EditText) view.findViewById(R.id.percentage_edit_text);
        mPercentageErrorTextView = (TextView) view.findViewById(R.id.percentage_error_text_view);

        mPercentageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                mPercentageErrorTextView.setText(" ");
            }
        });

        if(mEntry == null || mEntry.getId() == -1) {
            mEntry = new ClassGradeBreakdownContract.ClassGradeBreakdownEntry(-1, -1, null, 0f);
            mItemTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                // TODO make sure the colors are preserved on theme change from system
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ClassItemTypeContract.ClassItemTypeEntry itemType = (ClassItemTypeContract.ClassItemTypeEntry) parent.getItemAtPosition(position);
                    if(itemType != null) {
                        TextView tv = (TextView) view;
                        if(itemType.getId() == -1)
                            tv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.tertiary_text_dark));
                        else {
                            tv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.primary_text_light));
                            mItemTypeErrorTextView.setText(" ");
                        }
                        mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(itemType.getColor(), getActivity()));
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            });

            // TODO hack for getting class entry from parent activity
            ClassContract.ClassEntry _class = ((ClassActivity) getActivity()).getClassEntry();
            // TODO just get the academic terms without using async task
            SQLiteDatabase db = ApolloDbAdapter.open();
            ArrayList<ClassItemTypeContract.ClassItemTypeEntry> itemTypes = ClassGradeBreakdownContract._getUnusedItemTypes(db, _class.getId());
            ApolloDbAdapter.close();
            itemTypes.add(0, new ClassItemTypeContract.ClassItemTypeEntry(-1, getString(R.string.item_type_hint), null));
            ArrayAdapter<ClassItemTypeContract.ClassItemTypeEntry> spinnerAdapter = new ArrayAdapter<ClassItemTypeContract.ClassItemTypeEntry>(getActivity(), android.R.layout.simple_spinner_item, itemTypes) {
                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent){
                    TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
                    ClassItemTypeContract.ClassItemTypeEntry itemType = getItem(position);
                    if(itemType.getId() == -1)
                        tv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.tertiary_text_dark));
                    else
                        tv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.primary_text_light));
                    try {
                        tv.setBackgroundColor(Color.parseColor(itemType.getColor()));
                    }
                    catch(Exception e) { }
                    return tv;
                }
            };
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mItemTypeSpinner.setAdapter(spinnerAdapter);
            mItemTypeTextView.setVisibility(View.GONE);
            mItemTypeSpinner.setVisibility(View.VISIBLE);
            mItemTypeErrorTextView.setVisibility(View.VISIBLE);
        }
        else {
            ClassItemTypeContract.ClassItemTypeEntry itemType = mEntry.getItemType();
            mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(itemType.getColor(), getContext()));
            mItemTypeTextView.setText(itemType.getDescription());
            mPercentageEditText.setText(String.format("%.2f", mEntry.getPercentage()));
            mItemTypeTextView.setVisibility(View.VISIBLE);
            mItemTypeSpinner.setVisibility(View.GONE);
            mItemTypeErrorTextView.setVisibility(View.GONE);
        }

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
                        if(mEntry.getId() == -1) {
                            ClassItemTypeContract.ClassItemTypeEntry itemType = (ClassItemTypeContract.ClassItemTypeEntry) mItemTypeSpinner.getSelectedItem();
                            if(itemType == null || itemType.getId() == -1) {
                                mItemTypeErrorTextView.setText(R.string.please_select_a_class_activity);
                                mItemTypeSpinner.requestFocus();
                                return;
                            }
                            mEntry.setItemType(itemType);
                        }
                        String strPercentage = mPercentageEditText.getText().toString();
                        if(StringUtils.isBlank(strPercentage)) {
                            mPercentageErrorTextView.setText(R.string.please_enter_a_breakdown_percentage);
                            mPercentageEditText.requestFocus();
                            return;
                        }
                        float percentage;
                        try {
                            percentage = Float.parseFloat(strPercentage);
                        }
                        catch(Exception e) {
                            percentage = 0;
                        }
                        mEntry.setPercentage(percentage);
                        mListener.onConfirmInsertUpdateClassGradeBreakdown(mEntry, fragmentTag);
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
}
