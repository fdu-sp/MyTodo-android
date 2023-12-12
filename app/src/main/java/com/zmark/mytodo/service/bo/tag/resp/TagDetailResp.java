package com.zmark.mytodo.service.bo.tag.resp;

import java.util.List;

/**
 * @author ZMark
 * @date 2023/12/5 13:53
 */
public class TagDetailResp {
    private Long id;
    private String tagName;
    private String tagPath;
    private List<TagDetailResp> children;
}
