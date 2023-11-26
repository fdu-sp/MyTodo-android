package com.zmark.mytodo.model;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmark.mytodo.R;

import java.util.List;

public class QuadrantAdapter extends RecyclerView.Adapter<QuadrantAdapter.QuadrantViewHolder> {

    private final List<String> tasks;

    public QuadrantAdapter(List<String> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public QuadrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quadrant_item, parent, false);
        return new QuadrantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuadrantViewHolder holder, int position) {
        String task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class QuadrantViewHolder extends RecyclerView.ViewHolder {

        private final TextView taskTitleView;
        private final CheckBox taskCheckBox;

        public QuadrantViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitleView = itemView.findViewById(R.id.taskTitle);
            taskCheckBox = itemView.findViewById(R.id.taskCheckBox);
        }

        public void bind(String task) {
            Log.d("quadrant-item", "bind: " + task);
            taskTitleView.setText(task);
            // 可以添加勾选框的逻辑
            if (taskCheckBox != null) {
                taskCheckBox.setChecked(false);
            }
        }
    }
}
