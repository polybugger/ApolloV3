package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.TextView;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassScheduleContract;

import org.apache.commons.lang3.StringUtils;

public class ClassScheduleDeleteDialogFragment extends AppCompatDialogFragment {

    public interface Listener {
        void onConfirmDeleteClassSchedule(ClassScheduleContract.ClassScheduleEntry entry, String fragmentTag);
    }

    public static final String TAG = "net.polybugger.apollot.delete_class_schedule_dialog_fragment";
    public static final String ENTRY_ARG = "net.polybugger.apollot.entry_arg";
    public static final String FRAGMENT_TAG_ARG = "net.polybugger.apollot.fragment_tag_arg";

    private Listener mListener;

    public static ClassScheduleDeleteDialogFragment newInstance(ClassScheduleContract.ClassScheduleEntry entry, String fragmentTag) {
        ClassScheduleDeleteDialogFragment df = new ClassScheduleDeleteDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ENTRY_ARG, entry);
        args.putString(FRAGMENT_TAG_ARG, fragmentTag);
        df.setArguments(args);
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final ClassScheduleContract.ClassScheduleEntry entry = (ClassScheduleContract.ClassScheduleEntry) args.getSerializable(ENTRY_ARG);
        final String fragmentTag = args.getString(FRAGMENT_TAG_ARG);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_class_schedule_delete, null);
        String time = entry.getTime(ApolloDbAdapter.getAppContext());
        String location = entry.getLocation();
        if(!StringUtils.isBlank(location)) {
            time = time + "\n" + location;
        }
        ((TextView) view.findViewById(R.id.time_location_text_view)).setText(time);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.remove_class_schedule)
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.remove, null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onConfirmDeleteClassSchedule(entry, fragmentTag);
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
