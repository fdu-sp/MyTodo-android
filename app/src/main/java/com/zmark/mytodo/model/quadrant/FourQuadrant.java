package com.zmark.mytodo.model.quadrant;


import com.zmark.mytodo.model.group.TaskListSimple;
import com.zmark.mytodo.network.bo.quadrant.resp.FourQuadrantDetailResp;

/**
 * @author ZMark
 * @date 2023/12/13 3:21
 */

public class FourQuadrant {
    private TaskListSimple taskListInfo;
    private OneQuadrant urgentAndImportant;
    private OneQuadrant urgentAndNotImportant;
    private OneQuadrant notUrgentAndImportant;
    private OneQuadrant notUrgentAndNotImportant;

    public FourQuadrant(FourQuadrantDetailResp fourQuadrantDetailResp) {
        this.taskListInfo = new TaskListSimple(fourQuadrantDetailResp.getTaskListInfo());
        this.urgentAndImportant = new OneQuadrant(fourQuadrantDetailResp.getUrgentAndImportant());
        this.urgentAndNotImportant = new OneQuadrant(fourQuadrantDetailResp.getUrgentAndNotImportant());
        this.notUrgentAndImportant = new OneQuadrant(fourQuadrantDetailResp.getNotUrgentAndImportant());
        this.notUrgentAndNotImportant = new OneQuadrant(fourQuadrantDetailResp.getNotUrgentAndNotImportant());
    }

    public TaskListSimple getTaskListInfo() {
        return taskListInfo;
    }

    public OneQuadrant getUrgentAndImportant() {
        return urgentAndImportant;
    }

    public OneQuadrant getUrgentAndNotImportant() {
        return urgentAndNotImportant;
    }

    public OneQuadrant getNotUrgentAndImportant() {
        return notUrgentAndImportant;
    }

    public OneQuadrant getNotUrgentAndNotImportant() {
        return notUrgentAndNotImportant;
    }
}
