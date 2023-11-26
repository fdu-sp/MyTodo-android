package com.zmark.mytodo.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmark.mytodo.R;
import com.zmark.mytodo.model.TodoItem;
import com.zmark.mytodo.model.TodoListAdapter;

import java.util.List;

public class ToDoListActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list_main);
        // 获取RecyclerView引用
        RecyclerView todoRecyclerView = findViewById(R.id.todoRecyclerView);
        // 创建待办事项列表数据
        List<TodoItem> todoList = TodoItem.getToDoList(100);
        // 创建RecyclerView的Adapter
        TodoListAdapter todoListAdapter = new TodoListAdapter(todoList);
        // 设置RecyclerView的LayoutManager
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 将Adapter设置给RecyclerView
        todoRecyclerView.setAdapter(todoListAdapter);
//        TextView label = findViewById(R.id.label);
//        registerForContextMenu(label);
    }
}
