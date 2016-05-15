package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import net.polybugger.apollot.db.DaysBits;

public class DaysPickerDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onSetButtonDays(int days, String dialogFragmentTag, int buttonId);
    }

    public static final String TAG = "net.polybugger.apollot.days_picker_dialog_fragment";
    public static final String DAYS_ARG = "net.polybugger.apollot.days_arg";
    public static final String TITLE_ARG = "net.polybugger.apollot.title_arg";
    public static final String DIALOG_FRAGMENT_TAG_ARG = "net.polybugger.apollot.dialog_fragment_tag_arg";
    public static final String BUTTON_ID_ARG = "net.polybugger.apollot.button_id_arg";

    private Listener mListener;
    private CheckBox mMondayCheckBox;
    private CheckBox mTuesdayCheckBox;
    private CheckBox mWednesdayCheckBox;
    private CheckBox mThursdayCheckBox;
    private CheckBox mFridayCheckBox;
    private CheckBox mSaturdayCheckBox;
    private CheckBox mSundayCheckBox;

    public static DaysPickerDialogFragment newInstance(int days, String title, String dialogFragmentTag, int buttonId) {
        DaysPickerDialogFragment df = new DaysPickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(DAYS_ARG, days);
        args.putString(TITLE_ARG, title);
        args.putString(DIALOG_FRAGMENT_TAG_ARG, dialogFragmentTag);
        args.putInt(BUTTON_ID_ARG, buttonId);
        df.setArguments(args);
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        int days = args.getInt(DAYS_ARG);
        final String dialogFragmentTag = args.getString(DIALOG_FRAGMENT_TAG_ARG);
        final int buttonId = args.getInt(BUTTON_ID_ARG);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_days_picker, null);
        mMondayCheckBox = (CheckBox) view.findViewById(R.id.monday_check_box);
        mTuesdayCheckBox = (CheckBox) view.findViewById(R.id.tuesday_check_box);
        mWednesdayCheckBox = (CheckBox) view.findViewById(R.id.wednesday_check_box);
        mThursdayCheckBox = (CheckBox) view.findViewById(R.id.thursday_check_box);
        mFridayCheckBox = (CheckBox) view.findViewById(R.id.friday_check_box);
        mSaturdayCheckBox = (CheckBox) view.findViewById(R.id.saturday_check_box);
        mSundayCheckBox = (CheckBox) view.findViewById(R.id.sunday_check_box);

        mMondayCheckBox.setChecked((days & DaysBits.M.getValue()) == DaysBits.M.getValue());
        mTuesdayCheckBox.setChecked((days & DaysBits.T.getValue()) == DaysBits.T.getValue());
        mWednesdayCheckBox.setChecked((days & DaysBits.W.getValue()) == DaysBits.W.getValue());
        mThursdayCheckBox.setChecked((days & DaysBits.Th.getValue()) == DaysBits.Th.getValue());
        mFridayCheckBox.setChecked((days & DaysBits.F.getValue()) == DaysBits.F.getValue());
        mSaturdayCheckBox.setChecked((days & DaysBits.S.getValue()) == DaysBits.S.getValue());
        mSundayCheckBox.setChecked((days & DaysBits.Su.getValue()) == DaysBits.Su.getValue());

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(args.getString(TITLE_ARG))
                .setView(view)
                .setNeutralButton(R.string.clear, null)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.set, null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((Button) alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int days = 0;
                        if(mMondayCheckBox.isChecked())
                            days = days + DaysBits.M.getValue();
                        if(mTuesdayCheckBox.isChecked())
                            days = days + DaysBits.T.getValue();
                        if(mWednesdayCheckBox.isChecked())
                            days = days + DaysBits.W.getValue();
                        if(mThursdayCheckBox.isChecked())
                            days = days + DaysBits.Th.getValue();
                        if(mFridayCheckBox.isChecked())
                            days = days + DaysBits.F.getValue();
                        if(mSaturdayCheckBox.isChecked())
                            days = days + DaysBits.S.getValue();
                        if(mSundayCheckBox.isChecked())
                            days = days + DaysBits.Su.getValue();
                        mListener.onSetButtonDays(days, dialogFragmentTag, buttonId);
                        dismiss();
                    }
                });
                ((Button) alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onSetButtonDays(0, dialogFragmentTag, buttonId);
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
