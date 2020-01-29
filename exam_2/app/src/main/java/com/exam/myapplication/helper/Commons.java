package com.exam.myapplication.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class Commons {

    public static void showDatePicker(FragmentManager fragmentManager, DatePickerDialog.OnDateSetListener listener){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                listener,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        dpd.setCancelable(false);
        dpd.show(fragmentManager, "Datepickerdialog");
    }

    public static void showTimePicker(FragmentManager fragmentManager, TimePickerDialog.OnTimeSetListener listener){
        TimePickerDialog time = TimePickerDialog.newInstance(listener, false);
        time.setCancelable(false);
        time.show(fragmentManager, "TimePickerDialog");
    }


    public interface OnDialogClickListener{
        void onYes();
    }
    public static void showYesNoDialog(Context context, OnDialogClickListener listener){
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    listener.onYes();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
