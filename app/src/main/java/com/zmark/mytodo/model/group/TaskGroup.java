package com.zmark.mytodo.model.group;

import java.util.List;

public class TaskGroup {
    private final String groupName;
    private final List<String> taskListNameList;

    public TaskGroup(String groupName, List<String> taskListNameList) {
        this.groupName = groupName;
        this.taskListNameList = taskListNameList;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<String> getTaskListNameList() {
        return taskListNameList;
    }
}