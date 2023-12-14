package com.zmark.mytodo.fragment.common;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zmark.mytodo.MainApplication;
import com.zmark.mytodo.R;
import com.zmark.mytodo.model.tag.TagSimple;
import com.zmark.mytodo.model.tag.TagSimpleListAdapter;
import com.zmark.mytodo.network.ApiUtils;
import com.zmark.mytodo.network.bo.tag.resp.TagSimpleResp;
import com.zmark.mytodo.network.invariant.Msg;
import com.zmark.mytodo.network.result.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class TagSelectBottomSheetFragment extends BottomSheetDialogFragment {

    private final static String TAG = "TagSelectBottomSheetFragment";


    /**
     * data-Tag列表
     */
    private List<TagSimple> tagList;

    private TagSimpleListAdapter.OnTagSelectListener onTagSelectListener;

    private RecyclerView tagSimpleListRecyclerView;

    public void setOnTagSelectListener(TagSimpleListAdapter.OnTagSelectListener listener) {
        this.onTagSelectListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.tagList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tag_simple_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tagSimpleListRecyclerView = view.findViewById(R.id.tagSimpleListRecyclerView);
        // 获取数据并更新UI
        this.fetchDataAndUpdateUI();
    }

    private void fetchDataAndUpdateUI() {
        Call<Result<List<TagSimpleResp>>> call = MainApplication.getTagService().getAllTagsSimple();

        ApiUtils.doRequest(call, new ApiUtils.Callbacks<List<TagSimpleResp>>() {

            @Override
            public void onSuccess(List<TagSimpleResp> data) {
                for (TagSimpleResp tagSimpleResp : data) {
                    tagList.add(new TagSimple(tagSimpleResp));
                }
                updateUI();
            }

            @Override
            public void onFailure(Integer code, String msg) {
                Log.w(TAG, "onFailure: " + code + " " + msg);
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClientRequestError(Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(requireContext(), Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerInternalError() {
                Toast.makeText(requireContext(), Msg.SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        requireActivity().runOnUiThread(() -> {
            TagSimpleListAdapter adapter = new TagSimpleListAdapter(tagList);
            tagSimpleListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            adapter.setOnTagSelectListener(tagSimple -> {
                if (onTagSelectListener != null) {
                    onTagSelectListener.onTagSelect(tagSimple);
                }
            });
            tagSimpleListRecyclerView.setAdapter(adapter);
        });
    }
}
