package com.zmark.mytodo.api.vo.task.req;

import java.sql.Date;
import java.sql.Time;
import java.util.List;


/**
 * @author ZMark
 * @date 2023/12/4 10:04
 */
public class TaskCreatReq {
    private String title;

    private List<String> tagNames;

    /**
     * content description
     */
    private String description;

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
}
