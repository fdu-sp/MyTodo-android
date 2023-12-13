package com.zmark.mytodo.fragment;

import static com.zmark.mytodo.service.invariant.Msg.CLIENT_REQUEST_ERROR;
import static com.zmark.mytodo.service.invariant.Msg.SERVER_INTERNAL_ERROR;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zmark.mytodo.MainApplication;
import com.zmark.mytodo.R;
import com.zmark.mytodo.model.group.TaskListSimple;
import com.zmark.mytodo.model.task.PriorityTypeE;
import com.zmark.mytodo.model.task.TaskDetail;
import com.zmark.mytodo.service.impl.MyDayTaskServiceImpl;
import com.zmark.mytodo.service.impl.TaskServiceImpl;
import com.zmark.mytodo.utils.TimeUtils;

/**
 * 任务详情页
 */
public class TaskDetailBottomSheetFragment extends AddTaskBottomSheetFragment {
    private static final String TAG = "TaskDetailBottomSheetFragment";

    public interface OnTaskCompleteStateListener {
        void onTaskCompleteStateChanged(TaskDetail taskDetail);
    }

    private OnTaskCompleteStateListener onTaskCompleteStateListener;

    private final TaskListSimple taskListSimple;

    public TaskDetailBottomSheetFragment(TaskListSimple taskListSimple, TaskDetail taskDetail) {
        super(taskDetail);
        this.taskListSimple = taskListSimple;
    }

    public void setOnTaskCompleteStateListener(OnTaskCompleteStateListener listener) {
        this.onTaskCompleteStateListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.taskListNameTextView.setText(taskListSimple.getName());
        this.backButton.setOnClickListener(v -> {
            this.dismiss();
        });
        // 设置checkbox的事件和选中状态
        this.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                taskDetail.complete();
                TaskServiceImpl.completeTask(taskDetail.getId());
            } else {
                taskDetail.unComplete();
                TaskServiceImpl.unCompleteTask(taskDetail.getId());
            }
            if (onTaskCompleteStateListener != null) {
                onTaskCompleteStateListener.onTaskCompleteStateChanged(taskDetail);
            }
        });
        this.checkBox.setChecked(taskDetail.getCompleted());
        // 设置任务标题
        this.taskTitle.setText(taskDetail.getTitle());

        // todo 确认

        // 设置 添加到我的一天
        // todo 如果是 我的一天 任务列表，需要通知上层更新
        this.addToMyDayLayout.setOnClickListener(v -> {
            if (taskDetail.isInMyDay()) {
                MyDayTaskServiceImpl.removeFromMyDayList(taskDetail.getId(), new MyDayTaskServiceImpl.Callbacks() {
                    @Override
                    public void onSuccess() {
                        taskDetail.setInMyDay(false);
                        updateAddToMyDayUI();
                    }

                    @Override
                    public void onFailure(Integer code, String msg) {
                        Log.w(TAG, "onFailure: " + code + " " + msg);
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onClientRequestError(Throwable t) {
                        Log.e(TAG, "onClientRequestError: ", t);
                        Toast.makeText(getContext(), CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onServerInternalError() {
                        Toast.makeText(getContext(), SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                MyDayTaskServiceImpl.addTaskToMyDay(taskDetail.getId(), new MyDayTaskServiceImpl.Callbacks() {
                    @Override
                    public void onSuccess() {
                        taskDetail.setInMyDay(true);
                        updateAddToMyDayUI();
                    }

                    @Override
                    public void onFailure(Integer code, String msg) {
                        Log.w(TAG, "onFailure: " + code + " " + msg);
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onClientRequestError(Throwable t) {
                        Log.e(TAG, "onClientRequestError: ", t);
                        Toast.makeText(getContext(), CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onServerInternalError() {
                        Toast.makeText(getContext(), SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        this.updateAddToMyDayUI();

        // 设置 priority
        this.priorityLayout.setOnClickListener(v -> {
            // todo
        });
        this.updatePriorityUI();

        // 设置 reminder
        this.reminderLayout.setOnClickListener(v -> {
            // todo
        });
        this.updateReminderUI();

        // 设置 due date
        this.dueDateLayout.setOnClickListener(v -> {
            // todo
        });
        this.updateDueDateUI();

        // 设置 expected execution date
        this.expectedExecutionDateLayout.setOnClickListener(v -> {
            // todo
        });
        this.updateExpectedExecutionDateUI();

        // 设置 repeated
        this.repeatedLayout.setOnClickListener(v -> {
            // todo
        });
        this.updateRepeatedUI();

        // 设置 list
        this.listLayout.setOnClickListener(v -> {
            // todo
        });
        this.updateListUI();

        // 设置 tag
        this.tagLayout.setOnClickListener(v -> {
            // todo
        });
        this.updateTagUI();

        // 设置创建时间
        this.updateTimeCreateUI();

        // 设置删除
        this.deleteImageView.setOnClickListener(v -> {
            // todo
        });
    }

    private void updateExpectedExecutionDateUI() {
        requireActivity().runOnUiThread(() -> {
            // todo
            expectedExecutionDateTextView.setTextColor(unCheckedColorStateList);
            expectedExecutionDateImageView.setImageTintList(unCheckedColorStateList);
        });
    }

    private void updatePriorityUI() {
        requireActivity().runOnUiThread(() -> {
            PriorityTypeE priorityTypeE = taskDetail.getTaskPriorityInfo().getPriorityType();
            ColorStateList colorStateList = MainApplication.getPriorityTextColor(priorityTypeE);
            priorityTextView.setTextColor(colorStateList);
            priorityTextView.setText(priorityTypeE.getDesc());
            priorityImageView.setImageTintList(colorStateList);
            priorityImageView.setImageTintList(colorStateList);
            // todo 点击事件
        });
    }

    private void updateTimeCreateUI() {
        requireActivity().runOnUiThread(() -> {
            String timeCreateStr = "创建于 " +
                    TimeUtils.getFormattedDateStrFromTimeStamp(taskDetail.getCreateTime());
            this.timeCreateShowTextView.setText(timeCreateStr);
        });
    }

    private void updateListUI() {
        requireActivity().runOnUiThread(() -> {
            // todo
            listTextView.setTextColor(unCheckedColorStateList);
            listImageView.setImageTintList(unCheckedColorStateList);
        });
    }

    private void updateRepeatedUI() {
        requireActivity().runOnUiThread(() -> {
            // todo
            repeatedTextView.setTextColor(unCheckedColorStateList);
            repeatedImageView.setImageTintList(unCheckedColorStateList);
        });
    }

    private void updateReminderUI() {
        requireActivity().runOnUiThread(() -> {
            // todo
            reminderTextView.setTextColor(unCheckedColorStateList);
            reminderImageView.setImageTintList(unCheckedColorStateList);
        });
    }

    private void updateTagUI() {
        requireActivity().runOnUiThread(() -> {
            if (taskDetail.getTags() == null || taskDetail.getTags().isEmpty()) {
                tagTextView.setText(R.string.setTags);
                tagTextView.setTextColor(unCheckedColorStateList);
                tagImageView.setImageTintList(unCheckedColorStateList);
            } else {
                String tagStr = taskDetail.getDetailTagString();
                tagTextView.setText(tagStr);
                tagTextView.setTextColor(checkedColorStateList);
                tagImageView.setImageTintList(checkedColorStateList);
            }
        });
    }

    @Override
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

    private void updateDueDateUI() {
        requireActivity().runOnUiThread(() -> {
            if (taskDetail.getTaskTimeInfo() == null ||
                    taskDetail.getTaskTimeInfo().getEndDate() == null) {
                dueDateTextView.setText(R.string.add_due_date);
                dueDateTextView.setTextColor(unCheckedColorStateList);
                dueDateImageView.setImageTintList(unCheckedColorStateList);
            } else {
                String dueDateStr = taskDetail.getTaskTimeInfo().getEndDate();
                String formattedDateStr = TimeUtils.getFormattedDateStr(TimeUtils.getDateFromStr(dueDateStr))
                        + " 到期";
                dueDateTextView.setText(formattedDateStr);
                dueDateTextView.setTextColor(checkedColorStateList);
                dueDateImageView.setImageTintList(checkedColorStateList);
            }
        });
    }
}
