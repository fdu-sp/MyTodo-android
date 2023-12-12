package com.zmark.mytodo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zmark.mytodo.R;
import com.zmark.mytodo.model.TaskDetail;
import com.zmark.mytodo.model.group.TaskListSimple;

/**
 * 任务详情页
 */
public class TaskDetailFragment extends BottomSheetDialogFragment {
    private static final String TAG = "TaskDetailFragment";
    /**
     * model
     */
    private final TaskDetail taskDetail;

    private final TaskListSimple taskListSimple;

    private TextView taskListNameTextView;


    public TaskDetailFragment(TaskListSimple taskListSimple, TaskDetail taskDetail) {
        super();
        this.taskListSimple = taskListSimple;
        this.taskDetail = taskDetail;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);
        this.findViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.taskListNameTextView.setText(taskListSimple.getName());
    }

    private void findViews(View view) {
        this.taskListNameTextView = view.findViewById(R.id.toolbarTitle);

        // 设置图标的选中状态
//        ImageView iconImageView = findViewById(R.id.iconImageView);
//        ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.selector_icon_color);
//        iconImageView.setImageTintList(colorStateList);

    }
}
