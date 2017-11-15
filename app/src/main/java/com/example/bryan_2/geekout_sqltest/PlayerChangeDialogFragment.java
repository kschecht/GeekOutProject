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
    public static PlayerChangeDialogFragment newInstance() {
        return new PlayerChangeDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage("Hand the phone to the team to your right!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        // TODO - shuts down current activity, leads to new activity
                    }
                })
                .create();
    }
}
