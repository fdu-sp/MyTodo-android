package com.zmark.mytodo.model.tag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmark.mytodo.MainApplication;
import com.zmark.mytodo.R;

import java.util.List;

public class TagSimpleListAdapter extends RecyclerView.Adapter<TagSimpleListAdapter.ViewHolder> {
    public interface OnTagSelectListener {
        void onTagSelect(TagSimple tagSimple);
    }

    private List<TagSimple> tagList;
    private OnTagSelectListener onTagSelectListener;

    public TagSimpleListAdapter(List<TagSimple> tagList) {
        this.tagList = tagList;
    }

    public void setOnTagSelectListener(OnTagSelectListener listener) {
        this.onTagSelectListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_simple, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TagSimple tag = tagList.get(position);
        String tagPath = tag.getTagPath();
        holder.imageViewTagIcon.setImageTintList(MainApplication.getUnCheckedColorStateList());
        if (tagPath.split("/").length == 1) {
            holder.imageViewTagIcon.setImageResource(R.drawable.ic_tag_1_simple);
        } else {
            holder.imageViewTagIcon.setImageResource(R.drawable.ic_tag_2_no_simple);
        }
        holder.tagLinearLayout.setOnClickListener(v -> {
            if (onTagSelectListener != null) {
                onTagSelectListener.onTagSelect(tag);
            }
        });
        holder.textViewTagPath.setTextColor(MainApplication.getUnCheckedColorStateList());
        holder.textViewTagPath.setText(tag.getTagPath());
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout tagLinearLayout;
        ImageView imageViewTagIcon;
        TextView textViewTagPath;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tagLinearLayout = itemView.findViewById(R.id.tagLinearLayout);
            imageViewTagIcon = itemView.findViewById(R.id.imageViewTagIcon);
            textViewTagPath = itemView.findViewById(R.id.textViewTagPath);
        }
    }
}
