package com.zmark.mytodo.network.bo.group.req;

/**
 * @author ZMark
 * @date 2023/12/8 14:24
 */

public class TaskGroupCreateReq {
    /**
     * 分组名称必填
     * 分组名称不能为空
     */
    String name;

    /**
     * 分组描述 必填，但是可以为空
     */
    String description;

    public TaskGroupCreateReq(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
