package com.example.bryan_2.geekout_sqltest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by kschechter on 11/14/2017.
 */

// displays whenever need to hand phone to a new team
public class PlayerChangeDialogFragment extends DialogFragment {
    public static final String ALERT_MESSAGE = "alertMessage";

    public static PlayerChangeDialogFragment newInstance() {
        return new PlayerChangeDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(getArguments().getString(ALERT_MESSAGE))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                    }
                })
                .create();
    }
}
