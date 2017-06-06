package finnhartshorn.monashlibrary.books;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import finnhartshorn.monashlibrary.R;

/**
 * Created by crusa on 6/06/2017.
 */

public class RefineSearchDialogue extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate dialogue
        builder.setView(inflater.inflate(R.layout.refine_dialogue, null))
                .setPositiveButton("Refine", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // refine
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // cancelled so do nothing
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
