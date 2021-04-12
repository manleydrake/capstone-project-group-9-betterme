package com.example.betterme;

import android.content.DialogInterface;

import java.text.ParseException;

public interface DialogCloseListener {

    public void handleDialogClose(DialogInterface dialog) throws ParseException;
}
