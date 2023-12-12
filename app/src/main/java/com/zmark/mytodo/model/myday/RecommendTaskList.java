package com.zmark.mytodo.model.myday;

import com.zmark.mytodo.model.TaskSimple;
import com.zmark.mytodo.service.bo.list.resp.RecommendTaskListResp;
import com.zmark.mytodo.service.bo.task.resp.TaskSimpleResp;

import java.util.ArrayList;
import java.util.List;

public class RecommendTaskList {
    String title;
    List<TaskSimple> taskSimpleRespList;

    public RecommendTaskList(String title, List<TaskSimple> taskSimpleRespList) {
        this.title = title;
        this.taskSimpleRespList = taskSimpleRespList;
    }

    public RecommendTaskList(RecommendTaskListResp resp) {
        this.title = resp.getTitle();
        this.taskSimpleRespList = new ArrayList<>();
        for (TaskSimpleResp taskSimpleResp : resp.getTaskSimpleRespList()) {
            this.taskSimpleRespList.add(new TaskSimple(taskSimpleResp));
        }
    }

    public RecommendTaskList() {
        this.title = "";
        this.taskSimpleRespList = new ArrayList<>();
    }

    public int getSize() {
        return taskSimpleRespList.size();
    }

    public boolean isEmpty() {
        return taskSimpleRespList == null || taskSimpleRespList.isEmpty();
    }

    public String getTitle() {
        return title;
    }

    public List<TaskSimple> getTaskSimpleRespList() {
        return taskSimpleRespList;
    }
}
