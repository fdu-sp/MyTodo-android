package com.zmark.mytodo.service.bo.tag.resp;

/**
 * @author ZMark
 * @date 2023/12/4 10:57
 */
public class TagSimpleResp {
    private Long id;
    private String tagName;
    private String tagPath;

    public TagSimpleResp(Long id, String tagName, String tagPath) {
        this.id = id;
        this.tagName = tagName;
        this.tagPath = tagPath;
    }

    public TagSimpleResp() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagPath() {
        return tagPath;
    }

    public void setTagPath(String tagPath) {
        this.tagPath = tagPath;
    }
}
