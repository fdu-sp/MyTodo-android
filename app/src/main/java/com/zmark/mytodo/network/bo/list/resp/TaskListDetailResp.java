package com.zmark.mytodo.network.bo.list.resp;

import com.zmark.mytodo.network.bo.task.resp.TaskSimpleResp;

import java.util.List;


/**
 * @author ZMark
 * @date 2023/12/7 20:29
 */
public class TaskListDetailResp {
    Long id;

    String name;

    String description;

    Long groupId;

    Long count;

    List<TaskSimpleResp> tasks;

    String createTime;

    String updateTime;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getGroupId() {
        return groupId;
    }

    public Long getCount() {
        return count;
    }

    public List<TaskSimpleResp> getTasks() {
        return tasks;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }
}
