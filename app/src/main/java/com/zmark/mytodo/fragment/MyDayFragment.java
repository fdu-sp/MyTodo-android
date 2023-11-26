package com.zmark.mytodo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmark.mytodo.R;
import com.zmark.mytodo.model.TodoItem;
import com.zmark.mytodo.model.TodoListAdapter;

import java.util.List;

public class MyDayFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.todo_list_main, container, false);
        // 获取RecyclerView引用
        RecyclerView todoRecyclerView = view.findViewById(R.id.todoRecyclerView);
        // 获取待办事项列表数据
        List<TodoItem> todoList = TodoItem.getToDoList(100);
        // 创建RecyclerView的Adapter
        TodoListAdapter todoListAdapter = new TodoListAdapter(todoList);
        // 设置RecyclerView的LayoutManager
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        // 将Adapter设置给RecyclerView
        todoRecyclerView.setAdapter(todoListAdapter);
        return view;
    }
}
