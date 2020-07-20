package com.example.playandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.playandroid.R;
import com.example.playandroid.entity.Knowledge;
import com.example.playandroid.entity.KnowledgeSystem;
import com.example.playandroid.manager.DataTransferManager;
import com.example.playandroid.view.FlowLayout;
import com.example.playandroid.view.KnowledgeActivity;

import java.util.List;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class KnowledgeSystemAdapter extends RecyclerView.Adapter {

    private List<KnowledgeSystem> mKnowledgeSystemList;

    public KnowledgeSystemAdapter(List<KnowledgeSystem> knowledgeSystemList) {
        mKnowledgeSystemList = knowledgeSystemList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView tag;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.knowledge_name);
            tag = itemView.findViewById(R.id.knowledge_tag);
            view = itemView;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.relativelayout_knowledge_system_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final KnowledgeSystem item = mKnowledgeSystemList.get(position);
        viewHolder.title.setText(item.getName());
        List<Knowledge> knowledgeList = item.getKnowledgeList();
        StringBuilder stringBuilder = new StringBuilder();
        for (Knowledge knowledge : knowledgeList) {
            stringBuilder.append(knowledge.getName()).append("  ");
        }
        viewHolder.tag.setText(stringBuilder.toString());
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, KnowledgeActivity.class);
                intent.putExtra(DataTransferManager.KEY, item);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mKnowledgeSystemList.size();
    }
}
