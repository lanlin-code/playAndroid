package com.example.playandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.playandroid.R;
import com.example.playandroid.entity.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TextAdapter extends RecyclerView.Adapter {

    private List<Text> mList;

    public TextAdapter(List<Text> list) {
        mList = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mChapterName;
        TextView mNiceDate;
        TextView mTitle;
        TextView mAuthor;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mAuthor = itemView.findViewById(R.id.text_author);
            mChapterName = itemView.findViewById(R.id.chapter_name);
            mNiceDate = itemView.findViewById(R.id.nice_date);
            mTitle = itemView.findViewById(R.id.text_title);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.relativelayout_text_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Text item = mList.get(position);
        viewHolder.mTitle.setText(item.getTitle());
        viewHolder.mNiceDate.setText(item.getNiceDate());
        viewHolder.mAuthor.setText(item.getAuthor());
        viewHolder.mChapterName.setText(item.getSuperChapterName() + "/" + item.getChapterName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
