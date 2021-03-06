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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassScheduleContract;
import net.polybugger.apollot.db.DateTimeFormat;
import net.polybugger.apollot.db.DaysBits;

import org.apache.commons.lang3.StringUtils;

public class ClassScheduleInsertUpdateDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onConfirmInsertUpdateClassSchedule(ClassScheduleContract.ClassScheduleEntry entry, String fragmentTag);
    }

    public static final String TAG = "net.polybugger.apollot.insert_update_class_schedule_dialog_fragment";
    public static final String ENTRY_ARG = "net.polybugger.apollot.entry_arg";
    public static final String TITLE_ARG = "net.polybugger.apollot.title_arg";
    public static final String BUTTON_TEXT_ARG = "net.polybugger.apollot.button_text_arg";
    public static final String FRAGMENT_TAG_ARG = "net.polybugger.apollot.fragment_tag_arg";

    private Listener mListener;
    private ClassScheduleContract.ClassScheduleEntry mEntry;
    private AlertDialog mAlertDialog;
    private Button mTimeStartButton;
    private TextView mErrorTextView;
    private Button mTimeEndButton;
    private Button mDaysButton;
    private EditText mRoomEditText;
    private EditText mBuildingEditText;
    private EditText mCampusEditText;

    public static ClassScheduleInsertUpdateDialogFragment newInstance(ClassScheduleContract.ClassScheduleEntry entry, String title, String buttonText, String fragmentTag) {
        ClassScheduleInsertUpdateDialogFragment df = new ClassScheduleInsertUpdateDialogFragment();
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
        mEntry = (ClassScheduleContract.ClassScheduleEntry) args.getSerializable(ENTRY_ARG);
        final String fragmentTag = args.getString(FRAGMENT_TAG_ARG);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_class_schedule_insert_update, null);
        mTimeStartButton = (Button) view.findViewById(R.id.time_start_button);
        mErrorTextView = (TextView) view.findViewById(R.id.error_text_view);
        mTimeEndButton = (Button) view.findViewById(R.id.time_end_button);
        mDaysButton = (Button) view.findViewById(R.id.days_button);
        mRoomEditText = (EditText) view.findViewById(R.id.room_edit_text);
        mBuildingEditText = (EditText) view.findViewById(R.id.building_edit_text);
        mCampusEditText = (EditText) view.findViewById(R.id.campus_edit_text);
        mTimeStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                TimePickerDialogFragment df = (TimePickerDialogFragment) fm.findFragmentByTag(TimePickerDialogFragment.TAG);
                if(df == null) {
                    df = TimePickerDialogFragment.newInstance((Date) v.getTag(), getString(R.string.time_start), getTag(), R.id.time_start_button);
                    df.show(fm, TimePickerDialogFragment.TAG);
                }
            }
        });
        mTimeEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                TimePickerDialogFragment df = (TimePickerDialogFragment) fm.findFragmentByTag(TimePickerDialogFragment.TAG);
                if(df == null) {
                    df = TimePickerDialogFragment.newInstance((Date) v.getTag(), getString(R.string.time_end), getTag(), R.id.time_end_button);
                    df.show(fm, TimePickerDialogFragment.TAG);
                }
            }
        });
        mDaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DaysPickerDialogFragment df = (DaysPickerDialogFragment) fm.findFragmentByTag(DaysPickerDialogFragment.TAG);
                if(df == null) {
                    int days;
                    try {
                        days = (int) v.getTag();
                    }
                    catch(Exception e) {
                        days = 0;
                    }
                    df = DaysPickerDialogFragment.newInstance(days, getString(R.string.days), getTag(), R.id.days_button);
                    df.show(fm, DaysPickerDialogFragment.TAG);
                }
            }
        });
        Context context = getContext();
        final SimpleDateFormat sdf;
        if(StringUtils.equalsIgnoreCase(context.getResources().getConfiguration().locale.getLanguage(), ApolloDbAdapter.JA_LANGUAGE))
            sdf = new SimpleDateFormat(DateTimeFormat.TIME_DISPLAY_TEMPLATE_JA, context.getResources().getConfiguration().locale);
        else
            sdf = new SimpleDateFormat(DateTimeFormat.TIME_DISPLAY_TEMPLATE, context.getResources().getConfiguration().locale);

        if(mEntry == null) {
            mEntry = new ClassScheduleContract.ClassScheduleEntry(-1, -1, null, null, 0, null, null, null);
        }
        else {
            Date timeStart = mEntry.getTimeStart();
            if(timeStart != null) {
                mTimeStartButton.setText(sdf.format(timeStart));
                mTimeStartButton.setTag(timeStart);
            }
            Date timeEnd = mEntry.getTimeEnd();
            if(timeEnd != null) {
                mTimeEndButton.setText(sdf.format(timeEnd));
                mTimeEndButton.setTag(timeEnd);
            }
            int days = mEntry.getDays();
            mDaysButton.setText(DaysBits.intToString(context, days));
            mDaysButton.setTag(days);
            mRoomEditText.setText(mEntry.getRoom());
            mBuildingEditText.setText(mEntry.getBuilding());
            mCampusEditText.setText(mEntry.getCampus());
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
                        Date timeStart = (Date) mTimeStartButton.getTag();
                        if(timeStart == null) {
                            mErrorTextView.setText(R.string.please_input_a_time_start);
                            mTimeStartButton.requestFocus();
                            return;
                        }
                        int days;
                        try {
                            days = (int) mDaysButton.getTag();
                        }
                        catch(Exception e) {
                            days = 0;
                        }
                        mEntry.setTimeStart(timeStart);
                        mEntry.setTimeEnd((Date) mTimeEndButton.getTag());
                        mEntry.setDays(days);
                        mEntry.setRoom(mRoomEditText.getText().toString());
                        mEntry.setBuilding(mBuildingEditText.getText().toString());
                        mEntry.setCampus(mCampusEditText.getText().toString());
                        mListener.onConfirmInsertUpdateClassSchedule(mEntry, fragmentTag);
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

    public void setButtonTime(Date time, int buttonId) {
        Context context = getContext();
        final SimpleDateFormat sdf;
        if(StringUtils.equalsIgnoreCase(context.getResources().getConfiguration().locale.getLanguage(), ApolloDbAdapter.JA_LANGUAGE))
            sdf = new SimpleDateFormat(DateTimeFormat.TIME_DISPLAY_TEMPLATE_JA, context.getResources().getConfiguration().locale);
        else
            sdf = new SimpleDateFormat(DateTimeFormat.TIME_DISPLAY_TEMPLATE, context.getResources().getConfiguration().locale);

        Button b = (Button) mAlertDialog.findViewById(buttonId);
        if(time != null) {
            b.setText(sdf.format(time));
            if(buttonId == R.id.time_start_button)
                mErrorTextView.setText(" ");
        }
        else {
            b.setText(null);
        }
        b.setTag(time);
    }

    public void setButtonDays(int days, int buttonId) {
        Context context = getContext();
        mDaysButton.setText(DaysBits.intToString(context, days));
        mDaysButton.setTag(days);
    }
}
