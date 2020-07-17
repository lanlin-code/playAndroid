package com.example.playandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.playandroid.R;
import com.example.playandroid.entity.Item;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter {

    private List<Item> mItemList;

    public ItemAdapter(List<Item> items) {
        mItemList = items;
    }

    class ViewHold extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title;
        TextView description;
        TextView author;
        TextView time;

        public ViewHold(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.project_img);
            title = itemView.findViewById(R.id.project_title);
            description = itemView.findViewById(R.id.project_description);
            author = itemView.findViewById(R.id.project_author);
            time = itemView.findViewById(R.id.project_time);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.linearlayout_project_recycler_item_view, parent, false);
        ViewHold viewHold = new ViewHold(view);
        return viewHold;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHold viewHold = (ViewHold) holder;
        Item item = mItemList.get(position);

    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
