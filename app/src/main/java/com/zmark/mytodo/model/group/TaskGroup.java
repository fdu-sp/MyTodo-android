package com.zmark.mytodo.model.group;

import com.zmark.mytodo.network.bo.group.resp.TaskGroupSimpleResp;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 分组：包含多个清单
 *
 * @see TaskListSimple
 */
public class TaskGroup {
    Long id;
    String name;
    String description;
    Long count;
    List<TaskListSimple> taskListSimpleList;
    String createTime;
    String updateTime;

    boolean isExpanded = false;

    public TaskGroup() {
    }

    public static TaskGroup from(TaskGroupSimpleResp simpleResp) {
        TaskGroup taskGroup = new TaskGroup();
        taskGroup.id = simpleResp.getId();
        taskGroup.name = simpleResp.getName();
        taskGroup.description = simpleResp.getDescription();
        taskGroup.count = simpleResp.getCount();
        taskGroup.taskListSimpleList =
                Optional.ofNullable(simpleResp.getTaskLists())
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .map(TaskListSimple::from)
                        .collect(Collectors.toList());
        taskGroup.createTime = simpleResp.getCreateTime();
        taskGroup.updateTime = simpleResp.getUpdateTime();
        return taskGroup;
    }

    public static List<TaskGroup> from(List<TaskGroupSimpleResp> simpleRespList) {
        return simpleRespList.stream().map(TaskGroup::from).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<TaskListSimple> getTaskListSimpleList() {
        return taskListSimpleList;
    }

    public void setTaskListSimpleList(List<TaskListSimple> taskListSimpleList) {
        this.taskListSimpleList = taskListSimpleList;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}