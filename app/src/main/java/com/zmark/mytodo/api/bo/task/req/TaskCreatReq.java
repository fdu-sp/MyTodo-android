package com.zmark.mytodo.api.bo.task.req;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


/**
 * @author ZMark
 * @date 2023/12/4 10:04
 */
public class TaskCreatReq {
    private String title;
    /**
     * 可以为空，但是不可以为null，所以默认值为{}
     */
    private List<String> tagNames = new ArrayList<>();

    /**
     * content description
     * 不可以为null，所以默认值为""
     */
    private String description = "";

    private Long taskListId;

    /**
     * priority info
     */
    private Boolean isImportant;
    private Boolean isUrgent;

    /**
     * time info
     */
    private Date endDate;
    private Time endTime;
    private Boolean activateCountdown;
    private Date expectedExecutionDate;
    private Time expectedExecutionStartPeriod;
    private Time expectedExecutionEndPeriod;

    public TaskCreatReq(String todoTitle, String description) {
        this.title = todoTitle;
        this.description = description;
    }
}
