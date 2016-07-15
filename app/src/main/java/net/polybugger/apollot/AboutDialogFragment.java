package net.polybugger.apollot;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;

public class AboutDialogFragment extends AppCompatDialogFragment {

    public static final String TAG = "net.polybugger.apollot.about_dialog_fragment";

    public static AboutDialogFragment newInstance() {
        return new AboutDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_about, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.about_this_app)
                .setView(view)
                .setPositiveButton(R.string.close, null)
                .create();
        return alertDialog;
    }
}
