package com.vchohan.golftravel.teetime;

//public class DatePickerFragment extends DialogFragment {
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        // Use the current time as the default values for the picker
//        final Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//
//        // Activity needs to implement this interface
//        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getActivity();
//
//        // Create a new instance of TimePickerDialog and return it
//        return new DatePickerDialog(getActivity(), listener, year, month, day);
//    }
//}

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private int year, month, day;

    private Button btn;

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;

    }

    public void setObject(Button v) {
        this.btn = v;
    }

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
}
