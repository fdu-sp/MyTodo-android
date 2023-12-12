package com.zmark.mytodo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zmark.mytodo.R;
import com.zmark.mytodo.model.TaskDetail;

public class TaskDetailFragment extends BottomSheetDialogFragment {
    private static final String TAG = "TaskDetailFragment";
    /**
     * model
     */
    private final TaskDetail taskDetail;

    private Toolbar toolbar;


    public TaskDetailFragment(TaskDetail taskDetail) {
        super();
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
        this.toolbar.setTitle(taskDetail.getTitle());
    }

    private void findViews(View view) {
        this.toolbar = view.findViewById(R.id.taskDetailToolbar);

        // 设置图标的选中状态
//        ImageView iconImageView = findViewById(R.id.iconImageView);
//        ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.selector_icon_color);
//        iconImageView.setImageTintList(colorStateList);

    }
}
