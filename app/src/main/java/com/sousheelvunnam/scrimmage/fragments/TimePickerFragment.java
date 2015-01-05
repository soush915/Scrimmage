package com.sousheelvunnam.scrimmage.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Sousheel on 12/20/2014.
 */
public class TimePickerFragment extends DialogFragment
                                implements TimePickerDialog.OnTimeSetListener{

    public String mHour = "0";
    public String mMinute = "0";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        mHour = hourOfDay + "";
        mMinute = minute + "";

    }

    public String returnHour() {
        return mHour;
    }
    public String returnMinute() {
        return mMinute;
    }
}
