package com.example.hugo.syms;

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

public class AddMPhoneNumber extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context ctx = getActivity();
        final EditText et = new EditText(ctx);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setHint("0600000000");

        final AlertDialog alert = new AlertDialog.Builder(ctx)
                .setTitle("Enter your phone number")
                .setView(et)
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
                        String number = et.getText().toString();
                        if (!isPhoneValid(number)) {
                            et.setError("Invalid number!");
                            return;
                        }
                        Utils.addMPhoneNumber("+33"+number.subSequence(1,number.length()));
                        new GcmRegistrationAsyncTask(ctx).execute();
                        alert.dismiss();
                    }
                });
            }
        });
        return alert;
    }

    private boolean isPhoneValid(CharSequence phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }
}