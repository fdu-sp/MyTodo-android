package com.zmark.mytodo.api.bo.group.resp;

import com.zmark.mytodo.api.bo.list.resp.TaskListSimpleResp;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/7 20:28
 */
public class TaskGroupSimpleResp {
    Long id;
    String name;
    String description;
    Long count;
    List<TaskListSimpleResp> taskLists;
    String createTime;
    String updateTime;
}
