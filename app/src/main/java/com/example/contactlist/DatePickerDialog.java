package com.example.contactlist;

import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerDialog extends DialogFragment {

private void saveItem(Calendar selectedTime) {
    SaveDateListener activity = (SaveDateListener) getActivity();
    activity.didFinishDatePickerDialog(selectedTime);
    getDialog().dismiss();
}
    Calendar selectedDate;

    public interface SaveDateListener {
        void onRequestionPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults);

        void didFinishDatePickerDialog(Calendar selectedTime);

    }
    public DatePickerDialog(){
    }
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.select_date,container);
        getDialog().setTitle("Select Date");
        selectedDate = Calendar.getInstance();
        final CalendarView cv = view.findViewById(R.id.calendarView);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                selectedDate.set(year,month,day);
            }
        });

        Button saveButton = view.findViewById(R.id.buttonSelect);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem(selectedDate);

            }
        });
        Button cancelButton = view.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        return view;
    }

}
