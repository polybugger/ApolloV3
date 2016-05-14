package net.polybugger.apollot;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class FloatingActionBarMenuDialogFragment extends AppCompatDialogFragment {

    public static final String TAG = "net.polybugger.apollot.floating_action_bar_menu_dialog_fragment";

    public static FloatingActionBarMenuDialogFragment newInstance() {
        FloatingActionBarMenuDialogFragment df = new FloatingActionBarMenuDialogFragment();
        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_floating_action_bar_menu, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(null)
                .setView(view)
                .setNegativeButton(null, null)
                .setPositiveButton(null, null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Window window = alertDialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(0));
                WindowManager.LayoutParams windowParams = window.getAttributes();
                windowParams.dimAmount = 0f;
                windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                int sourceX = 0;
                int sourceY = 0;
                window.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
                Resources res = getResources();
                //windowParams.x = sourceX + res.getDimensionPixelSize(R.dimen.fab_margin); // dpToPx(rightMargin); // about half of confirm button size left of source view
                int bottomMargin = res.getDimensionPixelSize(R.dimen.tab_height) + res.getDimensionPixelSize(R.dimen.fab_margin) + res.getDimensionPixelSize(R.dimen.fab_size);
                windowParams.y = sourceY + bottomMargin; // above source view
                window.setAttributes(windowParams);
            }
        });
        return alertDialog;
    }

    public int dpToPx(float valueInDp) {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*
        try {
            mListener = (Listener) activity;
        }
        catch(ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " + Listener.class.toString());
        }
        */
    }

    @Override
    public void onDetach() {
        // mListener = null;
        super.onDetach();
    }

}
