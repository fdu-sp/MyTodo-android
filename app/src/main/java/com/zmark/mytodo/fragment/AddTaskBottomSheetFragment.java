package com.zmark.mytodo.fragment;

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
import com.zmark.mytodo.fragment.common.DateTimePicker;
import com.zmark.mytodo.fragment.common.TagSelectBottomSheetFragment;
import com.zmark.mytodo.fragment.common.TaskListSelectBottomSheetFragment;
import com.zmark.mytodo.model.tag.TagSimple;
import com.zmark.mytodo.model.task.PriorityTypeE;
import com.zmark.mytodo.model.task.TaskDetail;
import com.zmark.mytodo.network.ApiUtils;
import com.zmark.mytodo.network.api.TaskService;
import com.zmark.mytodo.network.bo.task.req.TaskCreateReq;
import com.zmark.mytodo.network.invariant.Msg;
import com.zmark.mytodo.network.result.Result;
import com.zmark.mytodo.network.result.ResultCode;
import com.zmark.mytodo.utils.TimeUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTaskBottomSheetFragment extends BottomSheetDialogFragment {
    private static final String TAG = "AddTaskBottomSheetFragment";

    public interface OnTaskCreateListener {
        void onTaskCreate(TaskDetail taskDetail);
    }

    private OnTaskCreateListener onTaskCreateListener;

    protected TaskDetail taskDetail;
    protected PriorityTypeE priorityTypeE;

    protected ColorStateList checkedColorStateList;
    protected ColorStateList unCheckedColorStateList;

    protected ImageView backButton;
    protected TextView taskListNameTextView;
    protected CheckBox checkBox;
    protected EditText taskTitle;

    protected ImageView confirmButton;

    protected LinearLayout addToMyDayLayout;
    protected ImageView addToMyDayImageView;
    protected TextView addToMyDayTextView;

    protected LinearLayout priorityLayout;
    protected ImageView priorityImageView;
    protected TextView priorityTextView;

    protected LinearLayout reminderLayout;
    protected ImageView reminderImageView;
    protected TextView reminderTextView;

    protected LinearLayout dueDateLayout;
    protected ImageView dueDateImageView;
    protected TextView dueDateTextView;

    protected LinearLayout expectedExecutionDateLayout;
    protected ImageView expectedExecutionDateImageView;
    protected TextView expectedExecutionDateTextView;

    protected LinearLayout repeatedLayout;
    protected ImageView repeatedImageView;
    protected TextView repeatedTextView;

    protected LinearLayout listLayout;
    protected ImageView listImageView;
    protected TextView listTextView;

    protected LinearLayout tagLayout;
    protected ImageView tagImageView;
    protected TextView tagTextView;

    protected EditText editTextMultiLine;

    protected CardView bottomCardView;
    protected TextView timeCreateShowTextView;
    protected ImageView deleteImageView;

    public AddTaskBottomSheetFragment() {
        this.taskDetail = new TaskDetail();
        this.onTaskCreateListener = null;
    }

    public AddTaskBottomSheetFragment(TaskDetail taskDetail) {
        this.taskDetail = taskDetail;
        this.onTaskCreateListener = null;
    }

    public void setOnTaskCreateListener(OnTaskCreateListener onTaskCreateListener) {
        this.onTaskCreateListener = onTaskCreateListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (taskDetail.getTaskListId() == null) {
            taskDetail.setTaskListName(MainApplication.DEFAULT_LIST_NAME);
            taskDetail.setTaskListId(MainApplication.DEFAULT_LIST_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View bottomSheetView = inflater.inflate(R.layout.fragment_task_detail, container, false);// 获取底部抽屉中的 EditText
        this.findViews(bottomSheetView);

        // 设置清单标题
        this.updateTaskListView();

        // 设置返回按钮
        this.backButton.setOnClickListener(this::handleBackButtonClick);

        // 设置checkbox
        this.checkBox.setOnCheckedChangeListener(this::handleCheckBoxClick);
        this.updateCheckBoxUI();

        // 设置任务标题
        this.updateTaskTitleUI();

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

        // 设置重复
        repeatedLayout.setOnClickListener(this::handleRepeatedClick);
        this.updateRepeatedViewUI();

        // 设置清单
        listLayout.setOnClickListener(this::handleListSelectClick);
        this.updateListSelectUI();

        // 标签
        tagLayout.setOnClickListener(this::handleTagSet);
        this.updateTagSetUI();

        // 设置备注和描述
        this.updateDescriptionUI();

        // 设置删除
        deleteImageView.setOnClickListener(this::handleDeleteImageView);

        // 隐藏底部
        bottomCardView.setVisibility(View.INVISIBLE);

        // 在输入法弹出时调整窗口的大小
        Dialog dialog = getDialog();
        if (dialog != null) {
            // 设置Dialog的windowSoftInputMode属性
            Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        return bottomSheetView;
    }

    protected void updateTaskListView() {
        requireActivity().runOnUiThread(() -> taskListNameTextView.setText(taskDetail.getTaskListName()));
    }

    /**
     * 返回则关闭底部抽屉，舍弃用户的输入
     */
    protected void handleBackButtonClick(View view) {
        // 处理用户点击返回按钮的逻辑
        this.dismiss();
    }

    protected void handleCheckBoxClick(View view, boolean isChecked) {
        // 处理用户点击checkbox的逻辑
        taskDetail.setCompleted(!taskDetail.getCompleted());
        this.updateCheckBoxUI();
    }

    protected void updateCheckBoxUI() {
        requireActivity().runOnUiThread(() -> checkBox.setChecked(taskDetail.getCompleted()));
    }

    protected void updateTaskTitleUI() {
        requireActivity().runOnUiThread(() -> taskTitle.setText(taskDetail.getTitle()));
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
        if (taskDetail.getTaskListId() == null) {
            taskDetail.setTaskListName(MainApplication.DEFAULT_LIST_NAME);
            taskDetail.setTaskListId(MainApplication.DEFAULT_LIST_ID);
        }
        // 执行添加待办事项的操作
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
            taskDetail.getTaskPriorityInfo().setImportant(priorityTypeE.isImportant());
            taskDetail.getTaskPriorityInfo().setUrgent(priorityTypeE.isUrgent());
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
                String timeStr = TimeUtils.getFormattedTimeStrFromTimeStamp(reminderTimestamp);
                String reminderStr = "在 " + formattedDateStr + " " + timeStr + "时提醒我";
                reminderTextView.setText(reminderStr);
                reminderTextView.setTextColor(checkedColorStateList);
                reminderImageView.setImageTintList(checkedColorStateList);
            } else {
                reminderTextView.setText(R.string.setReminder);
                reminderTextView.setTextColor(unCheckedColorStateList);
                reminderImageView.setImageTintList(unCheckedColorStateList);
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
                                + " " + TimeUtils.getFormattedTimeStrFromTimeStr(endTime) + " 到期";
                Log.d(TAG, "updateDueDateTimeViewUI: " + dueDateTimeStr);
                dueDateTextView.setText(dueDateTimeStr);
                dueDateTextView.setTextColor(checkedColorStateList);
                dueDateImageView.setImageTintList(checkedColorStateList);
            } else {
                dueDateTextView.setText(R.string.no_deadline_is_set);
                dueDateTextView.setTextColor(unCheckedColorStateList);
                dueDateImageView.setImageTintList(unCheckedColorStateList);
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
                                + " " + TimeUtils.getFormattedTimeStrFromTimeStr(expectedExecutionStartPeriod) + " 开始";
                Log.d(TAG, "updateExpectedExecutionViewUI: " + expectedExecutionDateStr);
                expectedExecutionDateTextView.setText(expectedExecutionDateStr);
                expectedExecutionDateTextView.setTextColor(checkedColorStateList);
                expectedExecutionDateImageView.setImageTintList(checkedColorStateList);
            } else {
                expectedExecutionDateTextView.setText(R.string.plan_the_execution_time);
                expectedExecutionDateTextView.setTextColor(unCheckedColorStateList);
                expectedExecutionDateImageView.setImageTintList(unCheckedColorStateList);
            }
        });
    }

    protected void handleRepeatedClick(View view) {
        // todo 处理用户点击设置重复的逻辑
//        Toast.makeText(requireContext(), "设置重复", Toast.LENGTH_SHORT).show();
        this.updateRepeatedViewUI();
    }

    protected void updateRepeatedViewUI() {
        requireActivity().runOnUiThread(() -> {
            repeatedTextView.setText(R.string.no_repetition);
            repeatedTextView.setTextColor(unCheckedColorStateList);
            repeatedImageView.setImageTintList(unCheckedColorStateList);
        });
    }

    protected void handleListSelectClick(View view) {
        TaskListSelectBottomSheetFragment taskListSelectBottomSheetFragment = new TaskListSelectBottomSheetFragment();
        taskListSelectBottomSheetFragment.show(requireActivity().getSupportFragmentManager(), taskListSelectBottomSheetFragment.getTag());
        taskListSelectBottomSheetFragment.setOnListClickListener(taskListSimple -> {
            taskDetail.setTaskListId(taskListSimple.getId());
            taskDetail.setTaskListName(taskListSimple.getName());
            this.updateListSelectUI();
            taskListSelectBottomSheetFragment.dismiss();
        });
    }

    protected void updateListSelectUI() {
        Long taskListId = taskDetail.getTaskListId();
        String taskListName = taskDetail.getTaskListName();
        requireActivity().runOnUiThread(() -> {
            if (taskListId != null && taskListName != null) {
                updateTaskListView();
                listTextView.setText(taskListName);
                listTextView.setTextColor(checkedColorStateList);
                listImageView.setImageTintList(checkedColorStateList);
            } else {
                listTextView.setText(R.string.select_list);
                listTextView.setTextColor(unCheckedColorStateList);
                listImageView.setImageTintList(unCheckedColorStateList);
            }
        });
    }

    protected void handleTagSet(View view) {
        TagSelectBottomSheetFragment tagSelectBottomSheetFragment = new TagSelectBottomSheetFragment();
        tagSelectBottomSheetFragment.show(requireActivity().getSupportFragmentManager(), tagSelectBottomSheetFragment.getTag());
        tagSelectBottomSheetFragment.setOnTagSelectListener(tagSimple -> {
            taskDetail.addTag(tagSimple);
            this.updateTagSetUI();
            tagSelectBottomSheetFragment.dismiss();
        });
    }

    protected void updateTagSetUI() {
        requireActivity().runOnUiThread(() -> {
            List<TagSimple> tags = taskDetail.getTags();
            if (tags != null && !tags.isEmpty()) {
                String tagStr = taskDetail.getDetailTagString();
                tagTextView.setText(tagStr);
                tagTextView.setTextColor(checkedColorStateList);
                tagImageView.setImageTintList(checkedColorStateList);
            } else {
                tagTextView.setText(R.string.add_tag);
                tagTextView.setTextColor(unCheckedColorStateList);
                tagImageView.setImageTintList(unCheckedColorStateList);
            }
        });
    }

    protected void updateDescriptionUI() {
        String description = taskDetail.getTaskContentInfo().getDescription();
        requireActivity().runOnUiThread(() -> {
            if (description != null && !description.isEmpty()) {
                editTextMultiLine.setText(description);
            }
        });
    }

    protected void handleDeleteImageView(View view) {

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
                        if (onTaskCreateListener != null) {
                            onTaskCreateListener.onTaskCreate(taskDetail);
                        }
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
