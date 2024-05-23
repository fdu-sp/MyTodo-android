package com.zmark.mytodo.network.bo.list.resp;

/**
 * @author ZMark
 * @date 2023/12/11 19:38
 */
public class RecommendMyDayResp {
    /**
     * 截止日期为之后三天的任务
     */
    RecommendTaskListResp tasksEndInThreeDays;

    /**
     * 截止日期为之后四到七天的任务
     */
    RecommendTaskListResp tasksEndInFourToSevenDays;

    /**
     * 已经过期，但是没有完成的任务
     */
    RecommendTaskListResp uncompletedTasksEndBeforeToday;

    /**
     * 最新一天创建的任务
     */
    RecommendTaskListResp latestCreatedTasks;

    public RecommendTaskListResp getTasksEndInThreeDays() {
        return tasksEndInThreeDays;
    }

    public RecommendTaskListResp getTasksEndInFourToSevenDays() {
        return tasksEndInFourToSevenDays;
    }

    public RecommendTaskListResp getUncompletedTasksEndBeforeToday() {
        return uncompletedTasksEndBeforeToday;
    }

    public RecommendTaskListResp getLatestCreatedTasks() {
        return latestCreatedTasks;
    }
}
