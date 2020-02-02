package com.example.cheezycode;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{


    private Context context;
    private List<Item> items;



    public RecyclerViewAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view =inflater.inflate(R.layout.post_item,viewGroup,false);
//
//        return new ViewHolder(view);

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Item item = items.get(i);
        viewHolder.postTitleID.setText(item.getTitle());
//        viewHolder.postDescriptionID.setText(item.getContent());

        Document document = Jsoup.parse(item.getContent());
        viewHolder.postDescriptionID.setText(document.text());

        Elements elements = document.select("img");
        Glide.with(context).
                load(elements.get(0).attr("src")).into(viewHolder.postImageID);

//        we are binding our click handler here
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("url",item.getUrl());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView postImageID;
        TextView postTitleID;
        TextView postDescriptionID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postImageID = itemView.findViewById(R.id.postImage);
            postTitleID = itemView.findViewById(R.id.postTitle);
            postDescriptionID = itemView.findViewById(R.id.postDescription);

        }
    }
}
