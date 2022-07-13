package com.example.alergenko.notifications;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ProblemNotification {
    private String tile;
    private String message;
    private Context context;

    public ProblemNotification(String tile, String message, Context context) {
        this.tile = tile;
        this.message = message;
        this.context = context;
    }

    public void show() {
        new AlertDialog.Builder(this.context)
                .setTitle(this.tile)
                .setMessage(this.message)
                .setPositiveButton("VREDU", (dialog, which) -> {
                    // Do nothing but close the dialog
                    dialog.dismiss();
                })
                .show();
    }
}
