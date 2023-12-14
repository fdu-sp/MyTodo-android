package com.zmark.mytodo.network.bo.task.resp.inner;


/**
 * @author ZMark
 * @date 2023/12/4 10:09
 */
public class TaskContentInfoResp {
    private String description;
    private String createTime;
    private String updateTime;

    public TaskContentInfoResp() {
        this.description = "";
        this.createTime = null;
        this.updateTime = null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
