package com.zmark.mytodo.handler;

import com.zmark.mytodo.model.group.TaskListSimple;

/**
 * @see com.zmark.mytodo.model.group.TaskListAdapter
 * @see com.zmark.mytodo.model.group.TaskGroupAdapter
 */
public interface OnListClickListener {
    void onItemClick(TaskListSimple task);
}