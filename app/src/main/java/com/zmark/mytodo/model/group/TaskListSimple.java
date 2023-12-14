package com.zmark.mytodo.model.group;

import com.zmark.mytodo.network.bo.list.resp.TaskListDetailResp;
import com.zmark.mytodo.network.bo.list.resp.TaskListSimpleResp;

/**
 * 清单，包含多个task
 */
public class TaskListSimple {
    Long id;
    String name;
    Long count;
    String description;
    Long groupId;
    String createTime;
    String updateTime;

    public TaskListSimple() {
    }

    public TaskListSimple(TaskListSimpleResp simpleResp) {
        this.id = simpleResp.getId();
        this.name = simpleResp.getName();
        this.count = simpleResp.getCount();
        this.description = simpleResp.getDescription();
        this.groupId = simpleResp.getGroupId();
        this.createTime = simpleResp.getCreateTime();
        this.updateTime = simpleResp.getUpdateTime();
    }

    public TaskListSimple(TaskListDetailResp taskListDetailResp) {
        this.id = taskListDetailResp.getId();
        this.name = taskListDetailResp.getName();
        this.count = taskListDetailResp.getCount();
        this.description = taskListDetailResp.getDescription();
        this.groupId = taskListDetailResp.getGroupId();
        this.createTime = taskListDetailResp.getCreateTime();
        this.updateTime = taskListDetailResp.getUpdateTime();
    }

    public static TaskListSimple from(TaskListSimpleResp simpleResp) {
        TaskListSimple taskListSimple = new TaskListSimple();
        taskListSimple.id = simpleResp.getId();
        taskListSimple.name = simpleResp.getName();
        taskListSimple.count = simpleResp.getCount();
        taskListSimple.description = simpleResp.getDescription();
        taskListSimple.groupId = simpleResp.getGroupId();
        taskListSimple.createTime = simpleResp.getCreateTime();
        taskListSimple.updateTime = simpleResp.getUpdateTime();
        return taskListSimple;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getCount() {
        return count;
    }

    public String getDescription() {
        return description;
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
