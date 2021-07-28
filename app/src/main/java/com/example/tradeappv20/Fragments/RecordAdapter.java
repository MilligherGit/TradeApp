package com.example.tradeappv20.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tradeappv20.ChatActivity.User;
import com.example.tradeappv20.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    public static final int RECORD_TYPE_FREE = 0;
    public static final int RECORD_TYPE_TRADE = 1;

    private Context mContext;
    private List<Record> mRecord;
    private String imageurl;
    private boolean my;

    String last_owner_name;

    FirebaseUser firebaseUser;

    public RecordAdapter(Context mContext, List<Record> mRecord, String imageurl) {
        this.mContext = mContext;
        this.mRecord = mRecord;
        this.imageurl = imageurl;
    }

    public RecordAdapter(Context mContext, List<Record> mRecord) {
        this.mContext = mContext;
        this.mRecord = mRecord;
    }

    @NonNull
    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == RECORD_TYPE_FREE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.record_item_free, parent, false);
            return new RecordAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.record_item_trade, parent, false);
            return new RecordAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.ViewHolder holder, int position) {
        Record record = mRecord.get(position);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        holder.item_name.setText(record.getItem_name());

        try {
            holder.old_item_name.setText(record.getOld_item_name());
        } catch (Exception e){

        }

        if (!record.getWas_trade().equals("final_trade")) {
            DatabaseReference search_reference = FirebaseDatabase.getInstance().getReference("Users");
            search_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        if (user.getId().equals(mRecord.get(position).getLast_owner())) {
                            holder.owner_name.setText(user.getUsername());
                            if (user.getId().equals(firebaseUser.getUid())){
                                my = true;
                            } else {
                                my = false;
                            }
                            if (record.getNew_item_uri().equals("default")) {
                                holder.new_item_image.setImageResource(R.drawable.collections);
                            } else {
                                Glide.with(mContext).load(record.getNew_item_uri()).into(holder.new_item_image);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        try{
            if(my) {
                if (record.getNew_item_uri().equals("default")) {
                    holder.new_item_image.setImageResource(R.drawable.collections);
                } else {
                    Glide.with(mContext).load(record.getNew_item_uri()).into(holder.new_item_image);
                }
            } else {
                if (record.getNew_item_uri().equals("default")) {
                    holder.new_item_image.setImageResource(R.drawable.collections);
                } else {
                    Glide.with(mContext).load(record.getOld_item_uri()).into(holder.new_item_image);
                }
            }
        } catch (Exception e) {

        }
        try{
            if(!my) {
                if (record.getNew_item_uri().equals("default")) {
                    holder.old_item_image.setImageResource(R.drawable.collections);
                } else {
                    Glide.with(mContext).load(record.getNew_item_uri()).into(holder.old_item_image);
                }
            } else {
                if (record.getNew_item_uri().equals("default")) {
                    holder.old_item_image.setImageResource(R.drawable.collections);
                } else {
                    Glide.with(mContext).load(record.getOld_item_uri()).into(holder.old_item_image);
                }
            }
        } catch (Exception e) {

        }

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, ItemActivity.class);
//                intent.putExtra("item_uri", item.getImageURL());
//                intent.putExtra("item_id", item.getId());
//                intent.putExtra("item_name", item.getItem_name());
//                intent.putExtra("owner", item.getOwner());
//                mContext.startActivity(intent);
//            }
//        });
    }



    @Override
    public int getItemCount() {
        return mRecord.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView owner_name;
        public TextView item_name;
        public TextView old_item_name;
        public ImageView old_item_image;
        public ImageView new_item_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            owner_name = itemView.findViewById(R.id.owner_name);
            old_item_image = itemView.findViewById(R.id.old_item_image);
            new_item_image = itemView.findViewById(R.id.new_item_image);
            item_name = itemView.findViewById(R.id.item_name);
            old_item_name = itemView.findViewById(R.id.old_item_name);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mRecord.get(position).getWas_trade().equals("Даром")){
            return RECORD_TYPE_FREE;
        } else {
            return RECORD_TYPE_TRADE;
        }
    }
}
