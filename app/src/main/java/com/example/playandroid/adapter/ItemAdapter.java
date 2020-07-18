package com.example.playandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.playandroid.R;
import com.example.playandroid.entity.ImageLoader;
import com.example.playandroid.entity.Item;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter {

    private List<Item> mItemList;
    private ImageLoader imageLoader;
    public ItemAdapter(List<Item> items, int maxSize) {
        mItemList = items;
        imageLoader = new ImageLoader(maxSize);
    }

    static class ViewHold extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title;
        TextView description;
        TextView author;
        TextView time;

        ViewHold(@NonNull View itemView) {
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
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHold viewHold = (ViewHold) holder;
        Item item = mItemList.get(position);
        viewHold.author.setText(item.getAuthor());
        viewHold.description.setText(item.getDescription());
        viewHold.title.setText(item.getTitle());
        viewHold.time.setText(item.getNiceDate());
        imageLoader.display(item.getPictureLink(), viewHold.imageView);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
