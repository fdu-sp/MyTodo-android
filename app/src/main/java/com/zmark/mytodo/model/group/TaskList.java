package com.zmark.mytodo.model.group;

import java.util.List;

/**
 * 清单，包含多个task
 */
public class TaskList {
    List<String> taskLists;

    public TaskList(List<String> taskLists) {
        this.taskLists = taskLists;
    }
}
