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
}
