package com.zmark.mytodo.fragment;

import static com.zmark.mytodo.service.invariant.Msg.CLIENT_REQUEST_ERROR;
import static com.zmark.mytodo.service.invariant.Msg.SERVER_INTERNAL_ERROR;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zmark.mytodo.R;
import com.zmark.mytodo.model.TaskDetail;
import com.zmark.mytodo.model.group.TaskListSimple;
import com.zmark.mytodo.service.impl.MyDayTaskServiceImpl;
import com.zmark.mytodo.service.impl.TaskServiceImpl;
import com.zmark.mytodo.utils.TimeUtils;

/**
 * 任务详情页
 */
public class TaskDetailFragment extends BottomSheetDialogFragment {
    private static final String TAG = "TaskDetailFragment";

    public interface OnTaskCompleteStateListener {
        void onTaskCompleteStateChanged(TaskDetail taskDetail);
    }

    private OnTaskCompleteStateListener onTaskCompleteStateListener;

    private ColorStateList checkedColorStateList;

    private ColorStateList unCheckedColorStateList;

    /**
     * model
     */
    private final TaskDetail taskDetail;

    private final TaskListSimple taskListSimple;

    private ImageView backButton;
    private TextView taskListNameTextView;
    private CheckBox checkBox;
    private TextView taskTitle;

    private LinearLayout addToMyDayLayout;
    private ImageView addToMyDayImageView;
    private TextView addToMyDayTextView;

    private LinearLayout dueDateLayout;
    private ImageView dueDateImageView;
    private TextView dueDateTextView;

    public TaskDetailFragment(TaskListSimple taskListSimple, TaskDetail taskDetail) {
        super();
        this.taskListSimple = taskListSimple;
        this.taskDetail = taskDetail;
    }

    public void setOnTaskCompleteStateListener(OnTaskCompleteStateListener listener) {
        this.onTaskCompleteStateListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);
        this.findViews(view);
        return view;
    }


    private void findViews(View view) {
        checkedColorStateList =
                ContextCompat.getColorStateList(requireContext(), R.color.cornflower_blue);
        unCheckedColorStateList =
                ContextCompat.getColorStateList(requireContext(), R.color.black);
        this.taskListNameTextView = view.findViewById(R.id.toolbarTitle);
        this.backButton = view.findViewById(R.id.backButton);
        this.checkBox = view.findViewById(R.id.checkBox);
        this.taskTitle = view.findViewById(R.id.taskTitle);
        this.addToMyDayLayout = view.findViewById(R.id.addToMyDayLayout);
        this.addToMyDayImageView = view.findViewById(R.id.addToMyDayImageView);
        this.addToMyDayTextView = view.findViewById(R.id.addToMyDayTextView);
        this.dueDateLayout = view.findViewById(R.id.dueDateLayout);
        this.dueDateImageView = view.findViewById(R.id.dueDateImageView);
        this.dueDateTextView = view.findViewById(R.id.dueDateTextView);
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
        // 设置 添加到我的一天的 点击事件
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
        // 设置 添加到我的一天 的UI
        this.updateAddToMyDayUI();
        // 设置 due date 的点击事件
        this.dueDateLayout.setOnClickListener(v -> {
            // todo
        });
        this.updateDueDateUI();
    }

    private void updateAddToMyDayUI() {
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
