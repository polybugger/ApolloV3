package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.polybugger.apollot.db.DateTimeFormat;

public class TimePickerDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onSetButtonTime(Date time, String dialogFragmentTag, int buttonId);
    }

    public static final String TAG = "net.polybugger.apollot.time_picker_dialog_fragment";
    public static final String TIME_ARG = "net.polybugger.apollot.time_arg";
    public static final String TITLE_ARG = "net.polybugger.apollot.title_arg";
    public static final String DIALOG_FRAGMENT_TAG_ARG = "net.polybugger.apollot.dialog_fragment_tag_arg";
    public static final String BUTTON_ID_ARG = "net.polybugger.apollot.button_id_arg";

    private Listener mListener;
    private TimePicker mTimePicker;

    public static TimePickerDialogFragment newInstance(Date time, String title, String dialogFragmentTag, int buttonId) {
        TimePickerDialogFragment df = new TimePickerDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(TIME_ARG, time);
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
        Date time = (Date) args.getSerializable(TIME_ARG);
        final String dialogFragmentTag = args.getString(DIALOG_FRAGMENT_TAG_ARG);
        final int buttonId = args.getInt(BUTTON_ID_ARG);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_time_picker, null);
        mTimePicker = (TimePicker) view.findViewById(R.id.time_picker);
        mTimePicker.setIs24HourView(false);
        SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.TIME_DB_TEMPLATE, getResources().getConfiguration().locale);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Date defaultTimeStart;
        try {
            defaultTimeStart = sdf.parse(sharedPref.getString(getString(R.string.default_time_start_key), getString(R.string.default_time_start_time)));
        }
        catch(Exception e) {
            defaultTimeStart = new Date();
        }
        if(time != null)
            defaultTimeStart = time;
        Calendar cal = Calendar.getInstance();
        cal.setTime(defaultTimeStart);
        if(Build.VERSION.SDK_INT >= 23 ) {
            mTimePicker.setHour(cal.get(Calendar.HOUR_OF_DAY));
            mTimePicker.setMinute(cal.get(Calendar.MINUTE));
        }
        else {
            mTimePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            mTimePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
        }
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
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cal = Calendar.getInstance();
                        if(Build.VERSION.SDK_INT >= 23 ) {
                            cal.set(Calendar.HOUR_OF_DAY, mTimePicker.getHour());
                            cal.set(Calendar.MINUTE, mTimePicker.getMinute());
                        }
                        else {
                            cal.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
                            cal.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
                        }
                        mListener.onSetButtonTime(cal.getTime(), dialogFragmentTag, buttonId);
                        dismiss();
                    }
                });
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onSetButtonTime(null, dialogFragmentTag, buttonId);
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
