package com.example.hugo.syms;

/**
 * Created by Hugo on 30/12/2014.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * Created by Hugo on 29/12/2014.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.hugo.syms.clientData.Notification;

import java.sql.SQLException;

public class NewNotifDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context ctx = getActivity();
        final EditText titleEdit = new EditText(ctx);
        final EditText textEdit = new EditText(ctx);
        titleEdit.setInputType(InputType.TYPE_CLASS_TEXT);
        titleEdit.setHint("Title");
        textEdit.setInputType(InputType.TYPE_CLASS_TEXT);
        textEdit.setHint("Text");

        final AlertDialog alert = new AlertDialog.Builder(ctx)
                .setCustomTitle(titleEdit)
                .setView(textEdit)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                Button okBtn = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                okBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String text = textEdit.getText().toString();
                        String title = titleEdit.getText().toString();
                        Notification notification = new Notification(title,text);
                        Utils.showNotif(getActivity(), notification);
                        alert.dismiss();
                    }
                });
            }
        });
        return alert;
    }
}