package com.zmark.mytodo.fragment.common;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

public class DateTimePicker {
    public interface OnDateTimeSetDetailListener {
        void onDateTimeSet(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute);
    }

    public interface OnDateTimeSetListener {
        void onDateTimeSet(String date, String time);
    }

    public interface OnTimeStampSetListener {
        void onTimeStampSet(String timeStamp);
    }

    private OnDateTimeSetDetailListener onDateTimeSetDetailListener;
    private OnDateTimeSetListener onDateTimeSetListener;
    private OnTimeStampSetListener onTimeStampSetListener;

    public void setOnDateTimeSetDetailListener(OnDateTimeSetDetailListener onDateTimeSetDetailListener) {
        this.onDateTimeSetDetailListener = onDateTimeSetDetailListener;
    }

    public void setOnDateTimeSetListener(OnDateTimeSetListener onDateTimeSetListener) {
        this.onDateTimeSetListener = onDateTimeSetListener;
    }

    public void setOnTimeStampSetListener(OnTimeStampSetListener onTimeStampSetListener) {
        this.onTimeStampSetListener = onTimeStampSetListener;
    }

    public void show(Context context) {
        // 获取当前日期和时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        // 创建日期选择器对话框
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, selectedYear, monthOfYear, dayOfMonth1) -> {
                    // 在日期选择后，创建时间选择器对话框
                    TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                            (view1, selectedHourOfDay, selectedMinute) -> {
                                if (onDateTimeSetDetailListener != null) {
                                    onDateTimeSetDetailListener.onDateTimeSet(selectedYear, monthOfYear, dayOfMonth1, selectedHourOfDay, selectedMinute);
                                }
                                if (onDateTimeSetListener != null) {
                                    String date = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                                            selectedYear, monthOfYear + 1, dayOfMonth1);
                                    String time = String.format(Locale.getDefault(), "%02d:%02d",
                                            selectedHourOfDay, selectedMinute);
                                    onDateTimeSetListener.onDateTimeSet(date, time);
                                }
                                if (onTimeStampSetListener != null) {
                                    String timeStamp = String.format(Locale.getDefault(), "%04d-%02d-%02d %02d:%02d:00",
                                            selectedYear, monthOfYear + 1, dayOfMonth1, selectedHourOfDay, selectedMinute);
                                    onTimeStampSetListener.onTimeStampSet(timeStamp);
                                }
                            }, hourOfDay, minute, DateFormat.is24HourFormat(context));
                    // 显示时间选择器对话框
                    timePickerDialog.show();
                }, year, month, dayOfMonth);
        // 显示日期选择器对话框
        datePickerDialog.show();
    }
}
