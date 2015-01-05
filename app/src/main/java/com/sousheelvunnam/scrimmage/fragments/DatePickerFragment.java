package com.sousheelvunnam.scrimmage.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import com.sousheelvunnam.scrimmage.ui.CreateGameActivity;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sousheel on 12/20/2014.
 */
public  class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public String mYear = "0";
    public String mMonth = "0";
    public String mDay = "0";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

        mYear = year + "";
        mMonth = month + "";
        mDay = day + "";
    }



    public String returnYear () {
        return mYear;
    }
    public String returnMonth () {
        return mMonth;
    }
    public String returnDay () {
        return  mDay;
    }
}
