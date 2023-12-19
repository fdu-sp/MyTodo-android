package com.zmark.mytodo.model.task.touch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.zmark.mytodo.MainApplication;
import com.zmark.mytodo.R;

public class TaskSimpleTouchHelperCallback extends ItemTouchHelper.Callback {
    private final Context context;
    private final TaskSimpleTouchHelperAdapter mAdapter;

    public TaskSimpleTouchHelperCallback(Context context, TaskSimpleTouchHelperAdapter adapter) {
        this.context = context;
        this.mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; // 允许上下拖动
        int dragFlags = 0; // 不允许上下拖动
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END; // 允许左右滑动
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.START) {
            // 左滑处理
            mAdapter.onItemLeftSwipe(viewHolder.getAdapterPosition());
        } else if (direction == ItemTouchHelper.END) {
            // 右滑处理
            mAdapter.onItemRightSwipe(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // 在这里自定义绘制，可以根据滑动方向设置不同的图标

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // 左滑
            if (dX < 0) {
                drawLeftSwipeIcon(c, viewHolder, dX);
            }
            // 右滑
            else if (dX > 0) {
                drawRightSwipeIcon(c, viewHolder, dX);
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void drawLeftSwipeIcon(Canvas c, RecyclerView.ViewHolder viewHolder, float dX) {
        ColorStateList backgroundColor = MainApplication.getColor(context, R.color.crimson);

        // 计算背景的位置
        int backgroundLeft = viewHolder.itemView.getRight() + (int) dX;
        int backgroundRight = viewHolder.itemView.getRight();

        // 绘制背景
        Paint paint = new Paint();
        paint.setColor(backgroundColor.getDefaultColor());
        c.drawRect(backgroundLeft, viewHolder.itemView.getTop(), backgroundRight, viewHolder.itemView.getBottom(), paint);

        Drawable icon = ContextCompat.getDrawable(context, R.drawable.ic_delete_white);
        if (icon == null) {
            return;
        }

        // 调整图标的大小
        int iconSize = (int) (viewHolder.itemView.getHeight() * 0.4); // 设置图标大小为 itemView 高度的 40%
        icon.setBounds(0, 0, iconSize, iconSize);

        // 计算图标的位置
        int iconMargin = (viewHolder.itemView.getHeight() - iconSize) / 2;
        int iconTop = viewHolder.itemView.getTop() + iconMargin;
        int iconBottom = iconTop + iconSize;
        int iconLeft = viewHolder.itemView.getRight() - iconMargin - iconSize;
        int iconRight = viewHolder.itemView.getRight() - iconMargin;

        // 设置图标的绘制区域
        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

        // 绘制背景
        paint.setColor(backgroundColor.getDefaultColor());
        c.drawRect(backgroundLeft, viewHolder.itemView.getTop(), backgroundRight, viewHolder.itemView.getBottom(), paint);

        // 绘制图标
        icon.draw(c);
    }


    @SuppressLint("ResourceAsColor")
    private void drawRightSwipeIcon(Canvas c, RecyclerView.ViewHolder viewHolder, float dX) {
        // 计算背景的位置
        int backgroundLeft = viewHolder.itemView.getLeft();
        int backgroundRight = viewHolder.itemView.getLeft() + (int) dX;

        ColorStateList backgroundColor = MainApplication.getColor(context, R.color.teal_200);
        // 绘制背景
        Paint paint = new Paint();
        paint.setColor(backgroundColor.getDefaultColor());
        c.drawRect(backgroundLeft, viewHolder.itemView.getTop(), backgroundRight, viewHolder.itemView.getBottom(), paint);

        Drawable icon = ContextCompat.getDrawable(context, R.drawable.ic_check_white);
        if (icon == null) {
            return;
        }

        // 调整图标的大小
        int iconSize = (int) (viewHolder.itemView.getHeight() * 0.4); // 设置图标大小为 itemView 高度的 40%
        icon.setBounds(0, 0, iconSize, iconSize);

        // 计算图标的位置
        int iconMargin = (viewHolder.itemView.getHeight() - iconSize) / 2;
        int iconTop = viewHolder.itemView.getTop() + iconMargin;
        int iconBottom = iconTop + iconSize;
        int iconLeft = viewHolder.itemView.getLeft() + iconMargin;
        int iconRight = iconLeft + iconSize;

        // 设置图标的绘制区域
        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

        // 绘制背景
        paint.setColor(backgroundColor.getDefaultColor());
        c.drawRect(backgroundLeft, viewHolder.itemView.getTop(), backgroundRight, viewHolder.itemView.getBottom(), paint);

        // 绘制图标
        icon.draw(c);
    }

}
