package com.zmark.mytodo.model.tag;

import com.zmark.mytodo.network.bo.tag.resp.TagSimpleResp;

public class TagSimple {
    private Long id;
    private String tagName;
    private String tagPath;

    public TagSimple(TagSimpleResp tagSimpleResp) {
        this.id = tagSimpleResp.getId();
        this.tagName = tagSimpleResp.getTagName();
        this.tagPath = tagSimpleResp.getTagPath();
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

    public TagSimpleResp toTagSimpleResp() {
        TagSimpleResp tagSimpleResp = new TagSimpleResp();
        tagSimpleResp.setId(this.id);
        tagSimpleResp.setTagName(this.tagName);
        tagSimpleResp.setTagPath(this.tagPath);
        return tagSimpleResp;
    }
}
