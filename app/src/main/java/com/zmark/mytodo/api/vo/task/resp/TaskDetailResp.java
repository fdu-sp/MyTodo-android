package com.zmark.mytodo.api.vo.task.resp;


import com.zmark.mytodo.api.vo.tag.resp.TagSimpleResp;
import com.zmark.mytodo.api.vo.task.resp.inner.TaskContentInfoResp;
import com.zmark.mytodo.api.vo.task.resp.inner.TaskPriorityInfoResp;
import com.zmark.mytodo.api.vo.task.resp.inner.TaskTimeInfoResp;

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
    private TaskContentInfoResp taskContentInfo;
    private TaskPriorityInfoResp taskPriorityInfo;
    private TaskTimeInfoResp taskTimeInfo;
    private String createTime;
    private String updateTime;
}
