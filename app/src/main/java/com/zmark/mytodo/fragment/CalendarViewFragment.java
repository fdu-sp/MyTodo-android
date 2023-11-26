package com.zmark.mytodo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zmark.mytodo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarViewFragment extends Fragment {

    private GridLayout calendarGrid;
    private TextView monthYearTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_view, container, false);
        calendarGrid = view.findViewById(R.id.calendarGrid);
        monthYearTextView = view.findViewById(R.id.monthYearTextView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化日历视图
        Calendar today = Calendar.getInstance();
        updateCalendar(today);
    }

    private void updateCalendar(Calendar calendar) {
        // 清空之前的日期视图
        calendarGrid.removeAllViews();

        // 设置月份和年份
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthYearTextView.setText(monthYearFormat.format(calendar.getTime()));

        // 设置星期标签
        String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String weekday : weekdays) {
            TextView dayLabel = new TextView(requireContext());
            dayLabel.setText(weekday);
            dayLabel.setTextSize(16);
            dayLabel.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            calendarGrid.addView(dayLabel);
        }

        // 设置日期
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        for (int i = 0; i < dayOfWeek; i++) {
            // 空白格子填充
            TextView emptyDay = new TextView(requireContext());
            calendarGrid.addView(emptyDay);
        }

        for (int i = 1; i <= daysInMonth; i++) {
            final int day = i;
            TextView dayView = new TextView(requireContext());
            dayView.setText(String.valueOf(i));
            dayView.setTextSize(18);
            dayView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            dayView.setOnClickListener(v -> onDayClicked(day));
            calendarGrid.addView(dayView);
        }
    }

    private void onDayClicked(int day) {
        // 处理日期点击事件，你可以根据需要进行处理
        // 在这里，你可能会打开一个新的界面或执行其他操作
    }
}
