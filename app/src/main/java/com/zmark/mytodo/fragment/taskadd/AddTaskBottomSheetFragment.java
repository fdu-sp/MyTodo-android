package com.zmark.mytodo.fragment.taskadd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zmark.mytodo.MainApplication;
import com.zmark.mytodo.R;
import com.zmark.mytodo.model.PriorityTypeE;
import com.zmark.mytodo.model.TaskDetail;
import com.zmark.mytodo.service.ApiUtils;
import com.zmark.mytodo.service.api.TaskService;
import com.zmark.mytodo.service.bo.task.req.TaskCreateReq;
import com.zmark.mytodo.service.invariant.Msg;
import com.zmark.mytodo.service.result.Result;
import com.zmark.mytodo.service.result.ResultCode;
import com.zmark.mytodo.utils.TimeUtils;

import java.util.Objects;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTaskBottomSheetFragment extends BottomSheetDialogFragment {
    private static final String TAG = "AddTaskBottomSheetFragment";

    private TaskDetail taskDetail;
    private PriorityTypeE priorityTypeE;

    private ColorStateList checkedColorStateList;
    private ColorStateList unCheckedColorStateList;

    private ImageView backButton;
    private TextView taskListNameTextView;
    private CheckBox checkBox;
    private EditText taskTitle;

    private ImageView confirmButton;

    private LinearLayout addToMyDayLayout;
    private ImageView addToMyDayImageView;
    private TextView addToMyDayTextView;

    private LinearLayout priorityLayout;
    private ImageView priorityImageView;
    private TextView priorityTextView;

    private LinearLayout reminderLayout;
    private ImageView reminderImageView;
    private TextView reminderTextView;

    private LinearLayout dueDateLayout;
    private ImageView dueDateImageView;
    private TextView dueDateTextView;

    private LinearLayout expectedExecutionDateLayout;
    private ImageView expectedExecutionDateImageView;
    private TextView expectedExecutionDateTextView;

    private LinearLayout repeatedLayout;
    private ImageView repeatedImageView;
    private TextView repeatedTextView;

    private LinearLayout listLayout;
    private ImageView listImageView;
    private TextView listTextView;

    private LinearLayout tagLayout;
    private ImageView tagImageView;
    private TextView tagTextView;

    private EditText editTextMultiLine;

    private CardView bottomCardView;
    private TextView timeCreateShowTextView;
    private ImageView deleteImageView;

    public AddTaskBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.taskDetail = new TaskDetail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View bottomSheetView = inflater.inflate(R.layout.fragment_task_detail, container, false);// 获取底部抽屉中的 EditText
        this.findViews(bottomSheetView);

        this.taskListNameTextView.setText("默认清单");
        this.backButton.setOnClickListener(v -> {
            this.dismiss();
        });
        // 设置checkbox的事件和选中状态
        this.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> taskDetail.setCompleted(isChecked));
        this.checkBox.setChecked(taskDetail.getCompleted());
        // 设置任务标题
        this.taskTitle.setText(taskDetail.getTitle());

        // 显示确认按钮
        confirmButton.setVisibility(View.VISIBLE);
        confirmButton.setOnClickListener(this::handleConfirmButtonClick);

        // 添加到我的一天
        addToMyDayLayout.setOnClickListener(this::handleAddToMyDayClick);
        this.updateAddToMyDayUI();

        // 优先级
        priorityLayout.setOnClickListener(this::handlePriorityClick);
        this.updatePriorityViewUI();

        // 设置提醒
        reminderLayout.setOnClickListener(this::handleReminderClick);
        this.updateReminderClickUI();

        // 截止日期与时间
        dueDateLayout.setOnClickListener(this::handleDueDateTimeClick);
        this.updateDueDateTimeViewUI();

        // 预计执行时间
        expectedExecutionDateLayout.setOnClickListener(this::handleExpectedExecutionDateClick);
        this.updateExpectedExecutionViewUI();

        // 标签
        tagLayout.setOnClickListener(view -> this.handleTagSet());

        // 设置清单
        listLayout.setOnClickListener(view -> {
            // todo 处理用户点击设置清单的逻辑
            Toast.makeText(requireContext(), "设置清单", Toast.LENGTH_SHORT).show();
        });

        // 隐藏底部
        bottomCardView.setVisibility(View.GONE);

        // 在输入法弹出时调整窗口的大小
        Dialog dialog = getDialog();
        if (dialog != null) {
            // 设置Dialog的windowSoftInputMode属性
            Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        return bottomSheetView;
    }

    protected void handleConfirmButtonClick(View view) {
        // 处理用户点击发送按钮的逻辑
        if (taskTitle.getText().toString().isEmpty()) {
            // 如果待办事项为空，则不执行添加操作
            Toast.makeText(requireContext(), Msg.NO_TASK_TITLE, Toast.LENGTH_SHORT).show();
            return;
        }
        String title = taskTitle.getText().toString();
        String description = Optional.of(editTextMultiLine.getText().toString()).orElse("");
        taskDetail.setTitle(title);
        taskDetail.getTaskContentInfo().setDescription(description);
        // 执行添加待办事项的操作
        // todo 是否已经完成
        TaskCreateReq createReq = taskDetail.toTaskCreateReq();
        this.createNewTask(createReq);
    }

    protected void handleAddToMyDayClick(View view) {
        // 处理用户点击添加到我的一天的逻辑
        taskDetail.setInMyDay(!taskDetail.isInMyDay());
        this.updateAddToMyDayUI();
    }

    protected void updateAddToMyDayUI() {
        requireActivity().runOnUiThread(() -> {
            if (taskDetail.isInMyDay()) {
                addToMyDayTextView.setText(R.string.already_in_my_day);
                addToMyDayTextView.setTextColor(checkedColorStateList);
                addToMyDayImageView.setImageTintList(checkedColorStateList);
            } else {
                addToMyDayTextView.setText(R.string.add_to_my_day);
                addToMyDayTextView.setTextColor(unCheckedColorStateList);
                addToMyDayImageView.setImageTintList(unCheckedColorStateList);
            }
        });
    }

    protected void handlePriorityClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("设置优先级");
        String[] priorityArray = PriorityTypeE.getPriorityArray();

        // 设置当前选中的优先级
        builder.setSingleChoiceItems(priorityArray, priorityTypeE.getCode() - 1, (dialog, which) -> {
            priorityTypeE = PriorityTypeE.getByCode(which + 1);
            if (priorityTypeE == null) {
                Log.e(TAG, "handlePrioritySet: 优先级设置错误");
                priorityTypeE = PriorityTypeE.NOT_URGENCY_NOT_IMPORTANT;
            }
            updatePriorityViewUI();
            dialog.dismiss();
        });

        // 显示对话框
        builder.show();
    }

    protected void updatePriorityViewUI() {
        if (priorityTypeE == null) {
            priorityTypeE = PriorityTypeE.NOT_URGENCY_NOT_IMPORTANT;
        }
        ColorStateList colorStateList = MainApplication.getPriorityTextColor(priorityTypeE);
        priorityTextView.setText(priorityTypeE.getDesc());
        priorityTextView.setTextColor(colorStateList);
        priorityImageView.setImageTintList(colorStateList);
    }

    protected void handleReminderClick(View view) {
        DateTimePicker dateTimePicker = new DateTimePicker();
        dateTimePicker.setOnTimeStampSetListener((timestamp) -> {
            this.taskDetail.getTaskTimeInfo().setReminderTimestamp(timestamp);
            this.updateReminderClickUI();
        });
        dateTimePicker.show(requireContext());
    }

    protected void updateReminderClickUI() {
        String reminderTimestamp = taskDetail.getTaskTimeInfo().getReminderTimestamp();
        requireActivity().runOnUiThread(() -> {
            if (reminderTimestamp != null) {
                String formattedDateStr = TimeUtils.getFormattedDateStrFromTimeStamp(reminderTimestamp);
                String timeStr = TimeUtils.getTimeStrFromTimeStamp(reminderTimestamp);
                String reminderStr = "在 " + formattedDateStr + " " + timeStr + "时提醒我";
                reminderTextView.setText(reminderStr);
                reminderTextView.setTextColor(checkedColorStateList);
            } else {
                reminderTextView.setText(R.string.setReminder);
                reminderTextView.setTextColor(unCheckedColorStateList);
            }
        });
    }

    protected void handleDueDateTimeClick(View view) {
        DateTimePicker dateTimePicker = new DateTimePicker();
        dateTimePicker.setOnDateTimeSetListener((date, time) -> {
            this.taskDetail.getTaskTimeInfo().setEndDate(date);
            this.taskDetail.getTaskTimeInfo().setEndTime(time);
            this.updateDueDateTimeViewUI();
        });
        dateTimePicker.show(requireContext());
    }

    protected void updateDueDateTimeViewUI() {
        String endDate = taskDetail.getTaskTimeInfo().getEndDate();
        String endTime = taskDetail.getTaskTimeInfo().getEndTime();
        Log.d(TAG, "updateDueDateTimeViewUI: " + endDate + " " + endTime);
        requireActivity().runOnUiThread(() -> {
            if (endDate != null && endTime != null) {
                String dueDateTimeStr =
                        TimeUtils.getFormattedDateStr(TimeUtils.getDateFromStr(endDate))
                                + " " + TimeUtils.getDayOfWeek(endDate)
                                + " " + endTime + " 到期";
                Log.d(TAG, "updateDueDateTimeViewUI: " + dueDateTimeStr);
                dueDateTextView.setText(dueDateTimeStr);
                dueDateTextView.setTextColor(checkedColorStateList);
            } else {
                dueDateTextView.setText(R.string.no_deadline_is_set);
                dueDateTextView.setTextColor(unCheckedColorStateList);
            }
        });
    }

    protected void handleExpectedExecutionDateClick(View view) {
        DateTimePicker dateTimePicker = new DateTimePicker();
        dateTimePicker.setOnDateTimeSetListener((date, time) -> {
            this.taskDetail.getTaskTimeInfo().setExpectedExecutionDate(date);
            this.taskDetail.getTaskTimeInfo().setExpectedExecutionStartPeriod(time);
            this.updateExpectedExecutionViewUI();
        });
        dateTimePicker.show(requireContext());
    }

    protected void updateExpectedExecutionViewUI() {
        String expectedExecutionDate = taskDetail.getTaskTimeInfo().getExpectedExecutionDate();
        String expectedExecutionStartPeriod = taskDetail.getTaskTimeInfo().getExpectedExecutionStartPeriod();
        requireActivity().runOnUiThread(() -> {
            if (expectedExecutionDate != null && expectedExecutionStartPeriod != null) {
                String expectedExecutionDateStr =
                        TimeUtils.getFormattedDateStr(TimeUtils.getDateFromStr(expectedExecutionDate))
                                + " " + TimeUtils.getDayOfWeek(expectedExecutionDate)
                                + " " + expectedExecutionStartPeriod + " 开始";
                Log.d(TAG, "updateExpectedExecutionViewUI: " + expectedExecutionDateStr);
                expectedExecutionDateTextView.setText(expectedExecutionDateStr);
                expectedExecutionDateTextView.setTextColor(checkedColorStateList);
            } else {
                expectedExecutionDateTextView.setText(R.string.plan_the_execution_time);
                expectedExecutionDateTextView.setTextColor(unCheckedColorStateList);
            }
        });
    }

    private void handleTagSet() {
        // todo
    }

    private void createNewTask(TaskCreateReq taskCreateReq) {
        TaskService taskService = MainApplication.getTaskService();
        Call<Result<Object>> call = taskService.createNewTask(taskCreateReq);
        call.enqueue(new Callback<Result<Object>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                if (response.isSuccessful()) {
                    Result<Object> result = response.body();
                    assert result != null;
                    if (result.getCode() == ResultCode.SUCCESS.getCode()) {
                        Log.i(TAG, "onResponse: 任务创建成功");
                        Toast.makeText(requireContext(), "任务创建成功", Toast.LENGTH_SHORT).show();
                        // 关闭底部抽屉
                        dismiss();
                    } else {
                        Log.w(TAG, "code:" + result.getCode() + " onResponse: " + result.getMsg());
                        Toast.makeText(requireContext(), result.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    ApiUtils.handleResponseError(TAG, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Object>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(requireContext(), Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findViews(View view) {
        checkedColorStateList =
                MainApplication.getCheckedColorStateList();
        unCheckedColorStateList =
                MainApplication.getUnCheckedColorStateList();

        this.taskListNameTextView = view.findViewById(R.id.toolbarTitle);
        this.backButton = view.findViewById(R.id.backButton);
        this.checkBox = view.findViewById(R.id.checkBox);
        this.taskTitle = view.findViewById(R.id.taskTitle);
        this.confirmButton = view.findViewById(R.id.confirmButton);

        this.addToMyDayLayout = view.findViewById(R.id.addToMyDayLayout);
        this.addToMyDayImageView = view.findViewById(R.id.addToMyDayImageView);
        this.addToMyDayTextView = view.findViewById(R.id.addToMyDayTextView);

        this.priorityLayout = view.findViewById(R.id.priorityLayout);
        this.priorityImageView = view.findViewById(R.id.priorityImageView);
        this.priorityTextView = view.findViewById(R.id.priorityTextView);

        this.reminderLayout = view.findViewById(R.id.reminderLayout);
        this.reminderImageView = view.findViewById(R.id.reminderImageView);
        this.reminderTextView = view.findViewById(R.id.reminderTextView);

        this.dueDateLayout = view.findViewById(R.id.dueDateLayout);
        this.dueDateImageView = view.findViewById(R.id.dueDateImageView);
        this.dueDateTextView = view.findViewById(R.id.dueDateTextView);

        this.expectedExecutionDateLayout = view.findViewById(R.id.expectedExecutionDateLayout);
        this.expectedExecutionDateImageView = view.findViewById(R.id.expectedExecutionDateImageView);
        this.expectedExecutionDateTextView = view.findViewById(R.id.expectedExecutionDateTextView);

        this.repeatedLayout = view.findViewById(R.id.repeatedLayout);
        this.repeatedImageView = view.findViewById(R.id.repeatedImageView);
        this.repeatedTextView = view.findViewById(R.id.repeatedTextView);

        this.listLayout = view.findViewById(R.id.listLayout);
        this.listImageView = view.findViewById(R.id.listImageView);
        this.listTextView = view.findViewById(R.id.listTextView);

        this.tagLayout = view.findViewById(R.id.tagLayout);
        this.tagImageView = view.findViewById(R.id.tagImageView);
        this.tagTextView = view.findViewById(R.id.tagTextView);

        this.editTextMultiLine = view.findViewById(R.id.editTextMultiLine);

        this.bottomCardView = view.findViewById(R.id.bottomCardView);
        this.timeCreateShowTextView = view.findViewById(R.id.timeCreateShowTextView);
        this.deleteImageView = view.findViewById(R.id.deleteImageView);
    }
}
