package com.example.tradeappv20.ChatActivity.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tradeappv20.ChatActivity.Chat;
import com.example.tradeappv20.ChatActivity.MessageActivity;
import com.example.tradeappv20.CollectionActivity.AnotherCollectionActivity;
import com.example.tradeappv20.CollectionActivity.Item;
import com.example.tradeappv20.CollectionActivity.ItemActivity;
import com.example.tradeappv20.LoginActivity.RegisterActivity;
import com.example.tradeappv20.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    public static final int MSG_TYPE_LEFT_TRADE = 2;
    public static final int MSG_TYPE_RIGHT_TRADE = 3;
    public static final int MSG_TYPE_LEFT_FREE = 4;
    public static final int MSG_TYPE_RIGHT_FREE = 5;
    public static final int MSG_TYPE_LEFT_TRADED = 6;
    public static final int MSG_TYPE_LEFT_FINAL_TRADE = 7;
    public static final int MSG_TYPE_LEFT_FINAL_TRADED = 8;


    String item_name;
    String new_item_uri;
    String old_item_uri;

    boolean exist = true;


//    Button msg_btn;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;

    FirebaseUser fuser;
    DatabaseReference del_reference;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageurl = imageurl;

    }


    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Button msg_btn;
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else if (viewType == MSG_TYPE_RIGHT_TRADE){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_trade_item_right, parent, false);
            msg_btn = view.findViewById(R.id.msg_btn);
            msg_btn.setVisibility(View.GONE);
            return new MessageAdapter.ViewHolder(view);
        } else if (viewType == MSG_TYPE_RIGHT_FREE){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_trade_item_right, parent, false);
            msg_btn = view.findViewById(R.id.msg_btn);
            msg_btn.setVisibility(View.GONE);
            return new MessageAdapter.ViewHolder(view);
        } else if (viewType == MSG_TYPE_LEFT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else if (viewType == MSG_TYPE_LEFT_FREE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_trade_item_left, parent, false);
            msg_btn = view.findViewById(R.id.msg_btn);
            msg_btn.setVisibility(View.VISIBLE);
            msg_btn.setText("Отдать");
            return new MessageAdapter.ViewHolder(view);
        }else if (viewType == MSG_TYPE_LEFT_FINAL_TRADE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_traded_item_left, parent, false);
            msg_btn = view.findViewById(R.id.msg_btn);
            msg_btn.setVisibility(View.VISIBLE);
            msg_btn.setEnabled(true);
            msg_btn.setRotation(0);
            msg_btn.setText("Согласен");
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_trade_item_left, parent, false);
            msg_btn = view.findViewById(R.id.msg_btn);
            msg_btn.setVisibility(View.VISIBLE);
            msg_btn.setText("Менять");
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat = mChat.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chat.getExist().equals("no")){
                    Toast.makeText(mContext, "EXIST!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "NOT EXIST!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.show_msg.setText(chat.getMessage());

        if (chat.getTrade().equals("Даром")) {
            if(chat.getExist().equals("no")) {
                holder.msg_btn.setRotation(-20);
                holder.msg_btn.setText("Отдано");
                holder.msg_btn.setEnabled(false);
            } else
            holder.msg_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference search_reference = FirebaseDatabase.getInstance().getReference("Items");
                    search_reference.orderByChild("id").equalTo(mChat.get(position).getItem_id()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {

                                Item item = child.getValue(Item.class);
                                item_name = item.getItem_name();
                                new_item_uri = item.getImageURL();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("last_owner", chat.getSender());
                                hashMap.put("new_owner", fuser.getUid());
                                hashMap.put("was_trade", chat.getTrade());
                                hashMap.put("item_name", item_name);
                                hashMap.put("new_item_uri", new_item_uri);

                                reference.child("Records").push().setValue(hashMap);

                                child.getRef().setValue(null);

                                holder.msg_btn.setRotation(-20);
                                holder.msg_btn.setText("Отдано");
                                holder.msg_btn.setEnabled(false);

                                DatabaseReference chat_ref = FirebaseDatabase.getInstance().getReference("Chats");
                                chat_ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot chat_snapshot) {
                                        for (DataSnapshot chat_child : chat_snapshot.getChildren()) {
                                            Chat this_chat = chat_child.getValue(Chat.class);
                                            if (this_chat.getMessage().equals(chat.getMessage())) {
                                                chat_child.getRef().child("exist").setValue("no");
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            System.out.println("The read failed: ");
                        }
                    });
                }
            });
        } else if (chat.getTrade().equals("На обмен")){
            if(chat.getExist().equals("no")) {
                holder.msg_btn.setRotation(-20);
                holder.msg_btn.setText("Обменяно");
                holder.msg_btn.setEnabled(false);
            } else
            holder.msg_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference search_reference = FirebaseDatabase.getInstance().getReference("Items");
                    search_reference.orderByChild("id").equalTo(mChat.get(position).getItem_id()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                Item item = child.getValue(Item.class);
                                item_name = item.getItem_name();

                                Intent intent = new Intent(mContext, AnotherCollectionActivity.class);
                                intent.putExtra("userid", chat.getSender());
                                intent.putExtra("isTrade", mChat.get(position).getItem_id());
                                mContext.startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        } else if (chat.getTrade().equals("final_trade")){
            try {
                if(chat.getExist().equals("no")) {
                    holder.msg_btn.setRotation(-20);
                    holder.msg_btn.setText("Обменяно");
                    holder.msg_btn.setEnabled(false);
                } else
                holder.msg_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference first_search_reference = FirebaseDatabase.getInstance().getReference("Items");
                        first_search_reference.orderByChild("id").equalTo(mChat.get(position).getItem_id()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    Item item = child.getValue(Item.class);
                                    item_name = item.getItem_name();
                                    old_item_uri = item.getImageURL();

                                    child.getRef().setValue(null);

                                    DatabaseReference second_search_reference = FirebaseDatabase.getInstance().getReference("Items");
                                    second_search_reference.orderByChild("id").equalTo(mChat.get(position).getAnoser_item_id()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot second_dataSnapshot) {
                                            for (DataSnapshot second_child : second_dataSnapshot.getChildren()) {
                                                Item second_item = second_child.getValue(Item.class);
                                                new_item_uri = second_item.getImageURL();

                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("last_owner", chat.getSender());
                                                hashMap.put("new_owner", fuser.getUid());
                                                hashMap.put("was_trade", chat.getTrade());
                                                hashMap.put("item_name", chat.getAnoser_item_name());
                                                hashMap.put("old_item_name", chat.getItem_name());
                                                hashMap.put("old_item_uri", old_item_uri);
                                                hashMap.put("new_item_uri", new_item_uri);

                                                reference.child("Records").push().setValue(hashMap);

                                                second_child.getRef().setValue(null);

                                                holder.msg_btn.setRotation(-20);
                                                holder.msg_btn.setText("Обменяно");
                                                holder.msg_btn.setEnabled(false);

                                                DatabaseReference chat_ref = FirebaseDatabase.getInstance().getReference("Chats");
                                                chat_ref.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot chat_snapshot) {
                                                        for (DataSnapshot chat_child : chat_snapshot.getChildren()) {
                                                            Chat this_chat = chat_child.getValue(Chat.class);
                                                            if (this_chat.getMessage().equals(chat.getMessage())) {
                                                                chat_child.getRef().child("exist").setValue("no");
                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                System.out.println("The read failed: ");
                            }
                        });
                    }
                });
            } catch (Exception e){

            }
        }
        if(imageurl.equals("default")){
            holder.profile_image.setImageResource(R.drawable.profile);
        } else {
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_msg;
        public ImageView profile_image;
        public Button msg_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            msg_btn = itemView.findViewById(R.id.msg_btn);
            show_msg = itemView.findViewById(R.id.show_msg);
            profile_image = itemView.findViewById(R.id.profile_image);

        }
    }


    @Override
    public int getItemViewType(int position) {

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if(mChat.get(position).getSender().equals(fuser.getUid())){
            if(mChat.get(position).getTrade().equals("На обмен")){
                return MSG_TYPE_RIGHT_TRADE;
            } else if(mChat.get(position).getTrade().equals("Даром")){
                return MSG_TYPE_RIGHT_FREE;
            } else {
                return MSG_TYPE_RIGHT;
            }
        } else {
            if(mChat.get(position).getTrade().equals("На обмен")){
                return MSG_TYPE_LEFT_TRADE;
            } else if(mChat.get(position).getTrade().equals("Даром")){
                return MSG_TYPE_LEFT_FREE;
            } else if (mChat.get(position).getTrade().equals("final_trade")) {
                return MSG_TYPE_LEFT_FINAL_TRADE;
            } else {
                return MSG_TYPE_LEFT;
            }
        }
    }

}