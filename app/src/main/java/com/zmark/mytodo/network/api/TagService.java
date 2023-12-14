package com.zmark.mytodo.network.api;

import com.zmark.mytodo.network.bo.tag.resp.TagSimpleResp;
import com.zmark.mytodo.network.result.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TagService {
    @GET("tag/simple/get-all-tags")
    Call<Result<List<TagSimpleResp>>> getAllTagsSimple();
}
