package com.example.tradeappv20.CollectionActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tradeappv20.ChatActivity.MessageActivity;
import com.example.tradeappv20.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {


    private Context mContext;
    private List<Item> mItem;
    private String imageurl;

    FirebaseUser fuser;

    public ItemAdapter(Context mContext, List<Item> mItem, String imageurl) { //
        this.mContext = mContext;
        this.mItem = mItem;
        this.imageurl = imageurl;
    }

    public ItemAdapter(Context mContext, List<Item> mItem) { //
        this.mContext = mContext;
        this.mItem = mItem;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_item, parent, false);
        return new ItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {

        Item item = mItem.get(position);

        holder.item_name.setText(item.getItem_name());
        try {
            if(item.getFor_free().equals("yes")) {
                holder.for_free.setVisibility(View.VISIBLE);
            }
        } catch (Exception e){

        }

        if(item.getImageURL().equals("default")){
            holder.item_image.setImageResource(R.drawable.collections);
        } else {
            Glide.with(mContext).load(item.getImageURL()).into(holder.item_image);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(mContext, ItemActivity.class);
                    intent.putExtra("item_uri", item.getImageURL());
                    intent.putExtra("item_id", item.getId());
                    intent.putExtra("item_name", item.getItem_name());
                    intent.putExtra("owner", item.getOwner());
                    intent.putExtra("isTrade", item.getIsTrade());
                    mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_msg;
        public TextView item_name;
        public ImageView for_free;
        public TextView trade_txt;
        public ImageView item_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_msg = itemView.findViewById(R.id.show_msg);
            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_name);
            for_free = itemView.findViewById(R.id.for_free);
            trade_txt = itemView.findViewById(R.id.trade_txt);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}