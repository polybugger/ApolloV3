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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import net.polybugger.apollot.db.AcademicTermContract;
import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.PastCurrentEnum;

import org.apache.commons.lang3.StringUtils;

public class ClassInsertUpdateDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onConfirmInsertUpdateClass(ClassContract.ClassEntry _class);
    }

    public static final String TAG = "net.polybugger.apollot.insert_update_class_dialog_fragment";
    public static final String CLASS_ARG = "net.polybugger.apollot.class_arg";
    public static final String TITLE_ARG = "net.polybugger.apollot.title_arg";
    public static final String BUTTON_TEXT_ARG = "net.polybugger.apollot.button_text_arg";

    private Listener mListener;
    private ClassContract.ClassEntry mClass;
    private LinearLayout mBackgroundLayout;
    private EditText mCodeEditText;
    private TextView mErrorTextView;
    private EditText mDescriptionEditText;
    private Spinner mAcademicTermSpinner;
    private EditText mYearEditText;
    private CheckBox mCurrentCheckBox;

    public static ClassInsertUpdateDialogFragment newInstance(ClassContract.ClassEntry _class, String title, String buttonText) {
        ClassInsertUpdateDialogFragment df = new ClassInsertUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(CLASS_ARG, _class);
        args.putString(TITLE_ARG, title);
        args.putString(BUTTON_TEXT_ARG, buttonText);
        df.setArguments(args);
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        mClass = (ClassContract.ClassEntry) args.getSerializable(CLASS_ARG);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_class_insert_update, null);
        mBackgroundLayout = (LinearLayout) view.findViewById(R.id.background_layout);
        mCodeEditText = (EditText) view.findViewById(R.id.code_edit_text);
        mErrorTextView = (TextView) view.findViewById(R.id.error_text_view);
        mDescriptionEditText = (EditText) view.findViewById(R.id.description_edit_text);
        mAcademicTermSpinner = (Spinner) view.findViewById(R.id.academic_term_spinner);
        mYearEditText = (EditText) view.findViewById(R.id.year_edit_text);
        mCurrentCheckBox = (CheckBox) view.findViewById(R.id.current_check_box);

        mCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                mErrorTextView.setText(" ");
            }
        });
        mAcademicTermSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // TODO make sure the colors are preserved on theme change from system
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AcademicTermContract.AcademicTermEntry academicTerm = (AcademicTermContract.AcademicTermEntry) parent.getItemAtPosition(position);
                if(academicTerm != null) {
                    TextView tv = (TextView) view;
                    if(academicTerm.getId() == -1)
                        tv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.tertiary_text_dark));
                    else
                        tv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.primary_text_light));
                    mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(academicTerm.getColor(), getActivity()));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        if(mClass.getId() != -1) {
            mCodeEditText.setText(mClass.getCode());
            mDescriptionEditText.setText(mClass.getDescription());
            Long year = mClass.getYear();
            if(year != null)
                mYearEditText.setText(String.valueOf(year));
        }
        switch(mClass.getPastCurrent()) {
            case PAST:
                mCurrentCheckBox.setChecked(false);
                break;
            case CURRENT:
                mCurrentCheckBox.setChecked(true);
                break;
        }

        // TODO just get the academic terms without using async task
        /* MainActivityFragment rf = (MainActivityFragment) getFragmentManager().findFragmentByTag(MainActivityFragment.TAG);
        if(rf != null)
            rf.getAcademicTerms(TAG);
        */
        SQLiteDatabase db = ApolloDbAdapter.open();
        ArrayList<AcademicTermContract.AcademicTermEntry> academicTerms = AcademicTermContract._getEntries(db);
        ApolloDbAdapter.close();
        academicTerms.add(0, new AcademicTermContract.AcademicTermEntry(-1, getString(R.string.academic_term_hint), null));
        ArrayAdapter<AcademicTermContract.AcademicTermEntry> spinnerAdapter = new ArrayAdapter<AcademicTermContract.AcademicTermEntry>(getActivity(), android.R.layout.simple_spinner_item, academicTerms) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
                AcademicTermContract.AcademicTermEntry academicTerm = getItem(position);
                if(academicTerm.getId() == -1)
                    tv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.tertiary_text_dark));
                else
                    tv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.primary_text_light));
                try {
                    tv.setBackgroundColor(Color.parseColor(academicTerm.getColor()));
                }
                catch(Exception e) { }
                return tv;
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAcademicTermSpinner.setAdapter(spinnerAdapter);
        if(mClass.getId() != -1) {
            AcademicTermContract.AcademicTermEntry academicTerm = mClass.getAcademicTerm();
            if(academicTerm != null)
                mAcademicTermSpinner.setSelection(spinnerAdapter.getPosition(academicTerm));
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
                        String code = mCodeEditText.getText().toString();
                        if(StringUtils.isEmpty(code)) {
                            mErrorTextView.setText(R.string.please_enter_a_class_code);
                            mCodeEditText.requestFocus();
                            return;
                        }
                        mClass.setCode(code);
                        mClass.setDescription(mDescriptionEditText.getText().toString());
                        AcademicTermContract.AcademicTermEntry academicTerm = (AcademicTermContract.AcademicTermEntry) mAcademicTermSpinner.getSelectedItem();
                        if(academicTerm != null && academicTerm.getId() != -1)
                            mClass.setAcademicTerm(academicTerm);
                        else
                            mClass.setAcademicTerm(null);
                        Long year;
                        try {
                            year = Long.parseLong(mYearEditText.getText().toString());
                        }
                        catch(Exception e) {
                            year = null;
                        }
                        mClass.setYear(year);
                        if(mCurrentCheckBox.isChecked())
                            mClass.setPastCurrent(PastCurrentEnum.CURRENT);
                        else
                            mClass.setPastCurrent(PastCurrentEnum.PAST);
                        mListener.onConfirmInsertUpdateClass(mClass);
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

    // TODO make sure the colors are preserved on theme change from system
    // public void onGetAcademicTerms(ArrayList<AcademicTermContract.AcademicTermEntry> arrayList, String fragmentTag) { }
}
