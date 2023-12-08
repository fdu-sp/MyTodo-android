package com.zmark.mytodo.api.bo.list.resp;

/**
 * @author ZMark
 * @date 2023/12/8 14:08
 */
public class TaskListSimpleResp {
    Long id;
    String name;
    Long count;
    String description;
    Long groupId;
    String createTime;
    String updateTime;

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
}
