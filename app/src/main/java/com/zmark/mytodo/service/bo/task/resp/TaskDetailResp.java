package com.zmark.mytodo.service.bo.task.resp;


import com.zmark.mytodo.service.bo.tag.resp.TagSimpleResp;
import com.zmark.mytodo.service.bo.task.resp.inner.TaskContentInfoResp;
import com.zmark.mytodo.service.bo.task.resp.inner.TaskPriorityInfoResp;
import com.zmark.mytodo.service.bo.task.resp.inner.TaskTimeInfoResp;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/4 10:05
 */
public class TaskDetailResp {
    private Long id;
    private String title;
    private Boolean completed;
    private String completedTime;
    private Boolean archived;
    private List<TagSimpleResp> tags;
    private Long taskListId;
    private String taskListName;
    private Boolean inMyDay;
    private TaskContentInfoResp taskContentInfo;
    private TaskPriorityInfoResp taskPriorityInfo;
    private TaskTimeInfoResp taskTimeInfo;
    private String createTime;
    private String updateTime;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public String getCompletedTime() {
        return completedTime;
    }

    public Boolean getArchived() {
        return archived;
    }

    public List<TagSimpleResp> getTags() {
        return tags;
    }

    public Long getTaskListId() {
        return taskListId;
    }

    public String getTaskListName() {
        return taskListName;
    }
    
    public Boolean isInMyDay() {
        return inMyDay;
    }

    public TaskContentInfoResp getTaskContentInfo() {
        return taskContentInfo;
    }

    public TaskPriorityInfoResp getTaskPriorityInfo() {
        return taskPriorityInfo;
    }

    public TaskTimeInfoResp getTaskTimeInfo() {
        return taskTimeInfo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }
}
