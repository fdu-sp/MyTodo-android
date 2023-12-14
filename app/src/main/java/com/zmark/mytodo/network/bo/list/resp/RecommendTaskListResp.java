package com.zmark.mytodo.network.bo.list.resp;

import com.zmark.mytodo.network.bo.task.resp.TaskSimpleResp;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/11 19:05
 */

public class RecommendTaskListResp {
    String title;
    List<TaskSimpleResp> taskSimpleRespList;

    public String getTitle() {
        return title;
    }

    public List<TaskSimpleResp> getTaskSimpleRespList() {
        return taskSimpleRespList;
    }
}
