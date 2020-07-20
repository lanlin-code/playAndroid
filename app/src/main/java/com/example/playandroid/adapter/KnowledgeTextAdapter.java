package com.example.playandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.playandroid.R;
import com.example.playandroid.entity.KnowledgeText;
import com.example.playandroid.manager.DataTransferManager;
import com.example.playandroid.view.WebActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class KnowledgeTextAdapter extends RecyclerView.Adapter {

    private List<KnowledgeText> mKnowledgeTexts;

    public KnowledgeTextAdapter(List<KnowledgeText> texts) {
        mKnowledgeTexts = texts;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView niceDate;
        TextView kind;
        TextView title;
        TextView author;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            niceDate = itemView.findViewById(R.id.knowledge_time);
            kind = itemView.findViewById(R.id.knowledge_kind);
            title = itemView.findViewById(R.id.knowledge_title);
            author = itemView.findViewById(R.id.knowledge_author);
            view = itemView;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.linearlayout_knowledge_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final KnowledgeText item = mKnowledgeTexts.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.author.setText(item.getAuthor());
        viewHolder.title.setText(item.getTitle());
        viewHolder.kind.setText(item.getSuperChapterName() + "/" + item.getChapterName());
        viewHolder.niceDate.setText(item.getNiceDate());
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra(DataTransferManager.KEY, item.getLink());
                context.startActivity(intent);
            }
        });
        Log.d("TAG", "onBindViewHolder: ");
    }

    @Override
    public int getItemCount() {
        return mKnowledgeTexts.size();
    }
}
