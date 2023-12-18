package com.zmark.mytodo.network.bo.list.req;

/**
 * @author ZMark
 * @date 2023/12/18 22:56
 */
public class TaskListUpdateReq {
    Long id;

    String name;

    String description;

    Long taskGroupId;

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

    public Long getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(Long taskGroupId) {
        this.taskGroupId = taskGroupId;
    }
}
