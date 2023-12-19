package com.zmark.mytodo.model.task.touch;

public interface TaskSimpleTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    
    void onItemLeftSwipe(int adapterPosition);

    void onItemRightSwipe(int adapterPosition);
}
