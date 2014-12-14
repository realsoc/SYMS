package com.example.hugo.syms;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hugo on 12/12/2014.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private Kid kid;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Date birthday = kid.getBirthday();
        int year;
        int month;
        int day;

        if(birthday == null){
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }else{
            year = birthday.getYear();
            month = birthday.getMonth();
            day = birthday.getDay();
        }


        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Toast.makeText(getActivity(),String.valueOf(dayOfMonth)+" "+String.valueOf(monthOfYear)+" "+String.valueOf(year),Toast.LENGTH_LONG).show();
        if(kid != null){
            kid.setBirthday(new Date(year,monthOfYear,dayOfMonth));
        }


    }

    public void setKid(Kid kid) {
        this.kid = kid;
    }
}
