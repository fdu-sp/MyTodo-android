package com.zmark.mytodo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zmark.mytodo.R;
import com.zmark.mytodo.model.TaskDetail;
import com.zmark.mytodo.model.group.TaskListSimple;
import com.zmark.mytodo.service.impl.TaskServiceImpl;

/**
 * 任务详情页
 */
public class TaskDetailFragment extends BottomSheetDialogFragment {
    private static final String TAG = "TaskDetailFragment";

    public interface OnTaskCompleteStateListener {
        void onTaskCompleteStateChanged(TaskDetail taskDetail);
    }

    private OnTaskCompleteStateListener onTaskCompleteStateListener;

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
    private TextView addToMyDayTextView;

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
        this.taskListNameTextView = view.findViewById(R.id.toolbarTitle);
        this.backButton = view.findViewById(R.id.backButton);
        this.checkBox = view.findViewById(R.id.checkBox);
        this.taskTitle = view.findViewById(R.id.taskTitle);
        this.addToMyDayLayout = view.findViewById(R.id.addToMyDayLayout);
        this.addToMyDayTextView = view.findViewById(R.id.addToMyDayTextView);

        // 设置图标的选中状态
//        ImageView iconImageView = findViewById(R.id.iconImageView);
//        ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.selector_icon_color);
//        iconImageView.setImageTintList(colorStateList);

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
        // 设置添加到我的一天的点击事件
        
    }
}
