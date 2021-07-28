package com.example.tradeappv20.CollectionActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tradeappv20.ChatActivity.MessageActivity;
import com.example.tradeappv20.ChatActivity.User;
import com.example.tradeappv20.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Owner;
import java.util.List;

public class ItemActivity extends AppCompatActivity {

    ImageView item_image;
    TextView item_name;
    TextView item_description;
    TextView tags;
    TextView isFree;
    Intent intent;
    Button trade_btn;
    Button delete_btn;

    String item_uri;
    String item_id;
    String isTrade;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        item_image = findViewById(R.id.item_image);
        item_name = findViewById(R.id.item_name);
        tags = findViewById(R.id.tags);
        isFree = findViewById(R.id.isFree);
        item_description = findViewById(R.id.item_description);
        trade_btn = findViewById(R.id.trade_btn);
        delete_btn = findViewById(R.id.delete_btn);


        intent = getIntent();
        isTrade = intent.getStringExtra("isTrade");
        item_id = intent.getStringExtra("item_id");
        String owner = intent.getStringExtra("owner");

        reference = FirebaseDatabase.getInstance().getReference("Items");
        reference.orderByChild("id").equalTo(item_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    item_name.setText(item.getItem_name());
                    item_description.setText("Описание: " + item.getItem_description());
                    String tag1 = item.getTag1();
                    String tag2 = item.getTag2();
                    String tag3 = item.getTag3();
                    String tag4 = item.getTag4();
                    String tag5 = item.getTag5();
                    String all_tags = "Теги: "; // + tag1 + ", " + tag2 + ", " + tag3 + ", " + tag4 + ", " + tag5;
                    if (!tag1.equals("")) all_tags = all_tags + tag1;
                    if (!tag2.equals("")) all_tags = all_tags + ", " + tag2;
                    if (!tag3.equals("")) all_tags = all_tags + ", " + tag3;
                    if (!tag4.equals("")) all_tags = all_tags + ", " + tag4;
                    if (!tag5.equals("")) all_tags = all_tags + ", " + tag5;

                    tags.setText(all_tags);
                    if (item.getFor_free().equals("yes")) {
                        trade_btn.setText("Забрать");
                        isFree.setText("Даром");
                    } else {
                        trade_btn.setText("Меняться");
                        isFree.setText("На обмен");
                    }
                    try {
                        if (!item.getIsTrade().equals("no")){
                            trade_btn.setText("Предложить");
                        }
                    } catch (Exception e){

                    }
                    if(item.getImageURL().equals("default")){
                        item_image.setImageResource(R.drawable.collections);
                    } else{
                        Glide.with(ItemActivity.this).load(item.getImageURL()).into(item_image);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: ");
            }
        });


        if(owner.equals(firebaseUser.getUid())){
            trade_btn.setVisibility(View.GONE);
            delete_btn.setVisibility(View.VISIBLE);
        }
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.orderByChild("id").equalTo(item_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            child.getRef().setValue(null);
                        }
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("The read failed: ");
                    }
                });
            }
        });


        trade_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemActivity.this, MessageActivity.class);
                intent.putExtra("userid", owner);
                intent.putExtra("item_name", item_name.getText());
                intent.putExtra("item_id", item_id);
                if (trade_btn.getText().equals("Предложить")) {
                    intent.putExtra("isFree", "final_trade");
                    intent.putExtra("trade", "yes");
                    intent.putExtra("isTrade", isTrade);
                } else {
                    intent.putExtra("isFree", isFree.getText());
                    intent.putExtra("trade", "yes");
                    intent.putExtra("isTrade", "fghfgh");
                }
                startActivity(intent);
                finish();
            }
        });
    }

    public void onBackClick(View view) {
        finish();
    }
}