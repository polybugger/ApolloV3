package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class DatePickerDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onSetButtonDate(Date date, String dialogFragmentTag, int buttonId);
    }

    public static final String TAG = "net.polybugger.apollot.date_picker_dialog_fragment";
    public static final String DATE_ARG = "net.polybugger.apollot.date_arg";
    public static final String TITLE_ARG = "net.polybugger.apollot.title_arg";
    public static final String DIALOG_FRAGMENT_TAG_ARG = "net.polybugger.apollot.dialog_fragment_tag_arg";
    public static final String BUTTON_ID_ARG = "net.polybugger.apollot.button_id_arg";

    private Listener mListener;
    private DatePicker mDatePicker;

    public static DatePickerDialogFragment newInstance(Date date, String title, String dialogFragmentTag, int buttonId) {
        DatePickerDialogFragment df = new DatePickerDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(DATE_ARG, date);
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
        Date date = (Date) args.getSerializable(DATE_ARG);
        final String dialogFragmentTag = args.getString(DIALOG_FRAGMENT_TAG_ARG);
        final int buttonId = args.getInt(BUTTON_ID_ARG);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_date_picker, null);
        mDatePicker = (DatePicker) view.findViewById(R.id.date_picker);
        mDatePicker.setSpinnersShown(false);
        mDatePicker.setCalendarViewShown(true);
        if(date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            mDatePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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
                        cal.set(Calendar.YEAR, mDatePicker.getYear());
                        cal.set(Calendar.MONTH, mDatePicker.getMonth());
                        cal.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        mListener.onSetButtonDate(cal.getTime(), dialogFragmentTag, buttonId);
                        dismiss();
                    }
                });
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onSetButtonDate(null, dialogFragmentTag, buttonId);
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
