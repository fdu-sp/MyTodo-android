package com.zmark.mytodo.fragment.list.inner;

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
import com.zmark.mytodo.handler.OnTaskSimpleAddedListener;
import com.zmark.mytodo.model.myday.RecommendTaskList;
import com.zmark.mytodo.model.myday.RecommendTaskListAdapter;
import com.zmark.mytodo.network.ApiUtils;
import com.zmark.mytodo.network.bo.list.resp.RecommendMyDayResp;
import com.zmark.mytodo.network.invariant.Msg;
import com.zmark.mytodo.network.result.Result;
import com.zmark.mytodo.network.result.ResultCode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class RecommendMyDayBottomSheetFragment extends BottomSheetDialogFragment {
    private static final String TAG = "RecommendMyDayBottomSheetFragment";
    private RecyclerView recommendListView;

    private OnTaskSimpleAddedListener onTaskSimpleAddedListener;

    public void setOnTaskAddedListener(OnTaskSimpleAddedListener listener) {
        this.onTaskSimpleAddedListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_my_day, container, false);
        recommendListView = view.findViewById(R.id.recommendListView);
        this.fetchAndUpdateUI();
        return view;
    }

    private void fetchAndUpdateUI() {
        Call<Result<RecommendMyDayResp>> call = MainApplication.getMyDayTaskService().getRecommendTaskList();
        call.enqueue(new retrofit2.Callback<Result<RecommendMyDayResp>>() {
            @Override
            public void onResponse(@NonNull Call<Result<RecommendMyDayResp>> call,
                                   @NonNull retrofit2.Response<Result<RecommendMyDayResp>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
                    ApiUtils.handleResponseError(TAG, response);
                    return;
                }
                Result<RecommendMyDayResp> result = response.body();
                if (result == null) {
                    Log.w(TAG, "result is null");
                    Toast.makeText(getContext(), Msg.SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.getCode() != ResultCode.SUCCESS.getCode()) {
                    Log.w(TAG, "code:" + result.getCode() + " onResponse: " + result.getMsg());
                    Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                RecommendMyDayResp recommendMyDayResp = result.getObject();
                if (recommendMyDayResp == null) {
                    Toast.makeText(getContext(), Msg.SERVER_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }
                updateUI(recommendMyDayResp);
            }

            @Override
            public void onFailure(@NonNull Call<Result<RecommendMyDayResp>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getContext(), Msg.CLIENT_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(RecommendMyDayResp recommendMyDayResp) {
        requireActivity().runOnUiThread(() -> {
            try {
                List<RecommendTaskList> recommendTaskLists = new ArrayList<>();
                recommendTaskLists.add(new RecommendTaskList(recommendMyDayResp.getTasksEndToday()));
                recommendTaskLists.add(new RecommendTaskList(recommendMyDayResp.getTasksEndInThreeDays()));
                recommendTaskLists.add(new RecommendTaskList(recommendMyDayResp.getTasksEndInFourToSevenDays()));
                recommendTaskLists.add(new RecommendTaskList(recommendMyDayResp.getUncompletedTasksEndBeforeToday()));
                recommendTaskLists.add(new RecommendTaskList(recommendMyDayResp.getLatestCreatedTasks()));
                RecommendTaskListAdapter recommendTaskListAdapter
                        = new RecommendTaskListAdapter(recommendTaskLists);
                recommendTaskListAdapter.setOnTaskAddedListener(onTaskSimpleAddedListener);
                recommendListView.setLayoutManager(new LinearLayoutManager(requireContext()));
                recommendListView.setAdapter(recommendTaskListAdapter);
            } catch (Exception e) {
                Log.e(TAG, "updateUI: " + e.getMessage(), e);
                Toast.makeText(getContext(), Msg.CLIENT_INTERNAL_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
