package com.zmark.mytodo.service.bo.task.resp.inner;


import java.sql.Date;
import java.sql.Time;

/**
 * @author ZMark
 * @date 2023/12/4 10:18
 */
public class TaskTimeInfoResp {
    private Date endDate;
    private Time endTime;
    private Boolean activateCountdown;
    private Date expectedExecutionDate;
    private Time expectedExecutionStartPeriod;
    private Time expectedExecutionEndPeriod;
}
