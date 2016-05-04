package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.PastCurrentEnum;

public class ClassNewDialogFragment extends DialogFragment implements MainActivityFragment.Listener {

    public interface Listener {
        void onConfirmInsertClass(ClassContract.ClassEntry entry);
    }

    public static final String TAG = "net.polybugger.apollot.insert_class_dialog_fragment";
    public static final String PAST_CURRENT_ARG = "net.polybugger.apollot.past_current_arg";

    private Listener mListener;
    private ClassContract.ClassEntry mEntry;
    private LinearLayout mBackgroundLayout;
    private EditText mCodeEditText;
    private EditText mDescriptionEditText;
    private Spinner mAcademicTermSpinner;
    private EditText mYearEditText;
    private CheckBox mCurrentCheckBox;

    public static ClassNewDialogFragment newInstance(PastCurrentEnum pastCurrent) {
        ClassNewDialogFragment df = new ClassNewDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(PAST_CURRENT_ARG, pastCurrent);
        df.setArguments(args);
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        PastCurrentEnum pastCurrent = (PastCurrentEnum) args.getSerializable(PAST_CURRENT_ARG);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_class_insert, null);
        mBackgroundLayout = (LinearLayout) view.findViewById(R.id.background_layout);
        mCodeEditText = (EditText) view.findViewById(R.id.code_edit_text);
        mDescriptionEditText = (EditText) view.findViewById(R.id.description_edit_text);
        mAcademicTermSpinner = (Spinner) view.findViewById(R.id.academic_term_spinner);
        mYearEditText = (EditText) view.findViewById(R.id.year_edit_text);
        mCurrentCheckBox = (CheckBox) view.findViewById(R.id.current_check_box);

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

        switch(pastCurrent) {
            case PAST:
                mCurrentCheckBox.setChecked(false);
                break;
            case CURRENT:
                mCurrentCheckBox.setChecked(true);
                break;
        }

        MainActivityFragment rf = (MainActivityFragment) getFragmentManager().findFragmentByTag(MainActivityFragment.TAG);
        if(rf != null)
            rf.getAcademicTerms(TAG);

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.new_class))
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(getString(R.string.add), null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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

    @Override
    public void onGetClassesSummary(ArrayList<ClassesFragment.ClassSummary> arrayList, PastCurrentEnum pastCurrent) {
        // unused
    }

    // TODO make sure the colors are preserved on theme change from system
    @Override
    public void onGetAcademicTerms(ArrayList<AcademicTermContract.AcademicTermEntry> arrayList, String fragmentTag) {
        arrayList.add(0, new AcademicTermContract.AcademicTermEntry(-1, getString(R.string.academic_term_hint), null));
        ArrayAdapter<AcademicTermContract.AcademicTermEntry> spinnerAdapter = new ArrayAdapter<AcademicTermContract.AcademicTermEntry>(getActivity(), android.R.layout.simple_spinner_item, arrayList) {
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
    }
}
