package com.zmark.mytodo.service.bo.task.req;

import com.zmark.mytodo.service.bo.tag.resp.TagSimpleResp;
import com.zmark.mytodo.service.bo.task.resp.inner.TaskContentInfoResp;
import com.zmark.mytodo.service.bo.task.resp.inner.TaskPriorityInfoResp;
import com.zmark.mytodo.service.bo.task.resp.inner.TaskTimeInfoResp;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/14 1:38
 */
public class TaskUpdateReq {
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(String completedTime) {
        this.completedTime = completedTime;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public List<TagSimpleResp> getTags() {
        return tags;
    }

    public void setTags(List<TagSimpleResp> tags) {
        this.tags = tags;
    }

    public Long getTaskListId() {
        return taskListId;
    }

    public void setTaskListId(Long taskListId) {
        this.taskListId = taskListId;
    }

    public String getTaskListName() {
        return taskListName;
    }

    public void setTaskListName(String taskListName) {
        this.taskListName = taskListName;
    }

    public Boolean getInMyDay() {
        return inMyDay;
    }

    public void setInMyDay(Boolean inMyDay) {
        this.inMyDay = inMyDay;
    }

    public TaskContentInfoResp getTaskContentInfo() {
        return taskContentInfo;
    }

    public void setTaskContentInfo(TaskContentInfoResp taskContentInfo) {
        this.taskContentInfo = taskContentInfo;
    }

    public TaskPriorityInfoResp getTaskPriorityInfo() {
        return taskPriorityInfo;
    }

    public void setTaskPriorityInfo(TaskPriorityInfoResp taskPriorityInfo) {
        this.taskPriorityInfo = taskPriorityInfo;
    }

    public TaskTimeInfoResp getTaskTimeInfo() {
        return taskTimeInfo;
    }

    public void setTaskTimeInfo(TaskTimeInfoResp taskTimeInfo) {
        this.taskTimeInfo = taskTimeInfo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
