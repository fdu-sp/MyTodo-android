package com.zmark.mytodo.network.bo.list.req;

/**
 * @author ZMark
 * @date 2023/12/7 20:46
 */
public class TaskListCreateReq {
    /**
     * 清单名称必填
     * 清单名称不能为空
     */
    String name;

    /**
     * 必填，但是可以为空
     */
    String description;

    Long taskGroupId;

    public TaskListCreateReq(String listName, String description, Long taskGroupId) {
        this.name = listName;
        this.description = description;
        this.taskGroupId = taskGroupId;
    }
}
