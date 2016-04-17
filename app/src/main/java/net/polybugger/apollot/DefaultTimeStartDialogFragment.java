package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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

public class DefaultTimeStartDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onChangeDefaultTimeStart(final String message);
    }

    public static final String TAG = "net.polybugger.apollot.default_time_start_dialog_fragment";

    private Listener mListener;
    private TimePicker mTimePicker;

    public static DefaultTimeStartDialogFragment newInstance() {
        return new DefaultTimeStartDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_default_time_start, null);
        mTimePicker = (TimePicker) view.findViewById(R.id.time_picker);
        mTimePicker.setIs24HourView(false);
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.TIME_DISPLAY_TEMPLATE, getResources().getConfiguration().locale);
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Date defaultTimeStart;
        try {
            defaultTimeStart = sdf.parse(sharedPref.getString(getString(R.string.default_time_start_key), getString(R.string.default_time_start_time)));
        }
        catch(Exception e) {
            defaultTimeStart = new Date();
        }
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
        final AlertDialog d = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.default_time_start)
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.change, null)
                .create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
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
                        sharedPref.edit().putString(getString(R.string.default_time_start_key), sdf.format(cal.getTime())).apply();
                        mListener.onChangeDefaultTimeStart(getString(R.string.default_time_start_changed));
                        dismiss();
                    }
                });
            }
        });
        return d;
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
}
