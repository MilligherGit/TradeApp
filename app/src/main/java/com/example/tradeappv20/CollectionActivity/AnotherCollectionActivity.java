package com.example.tradeappv20.CollectionActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.tradeappv20.MainActivity;
import com.example.tradeappv20.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnotherCollectionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView trade_txt;
    TextView who;
    Intent intent;

    List<Item> mItem;

    String item_uri;
    String isTrade;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_collection);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent = getIntent();
        item_uri = intent.getStringExtra("item_uri");

        String userid = intent.getStringExtra("userid");
        isTrade = intent.getStringExtra("isTrade");

        trade_txt = findViewById(R.id.trade_txt);
        who = findViewById(R.id.who);
        if (isTrade.equals("no")) {
            who.setText("Коллекция пользователя " + intent.getStringExtra("username"));
        }

        if (!isTrade.equals("no")){
            who.setVisibility(View.GONE);
            trade_txt.setVisibility(View.VISIBLE);
        }

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readItems(userid); //, item.getImageURL()
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readItems(String userid){
        mItem = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Items");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mItem.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Item item = snapshot.getValue(Item.class);
                    if (isTrade.equals("no")) {
                        if (item.getOwner().equals(userid)) {
                            snapshot.getRef().child("isTrade").setValue("no");
                            mItem.add(item);
                        }
                    } else {
                        if (item.getOwner().equals(userid) && item.getFor_free().equals("no")) {
                            snapshot.getRef().child("isTrade").setValue(isTrade);
                            mItem.add(item);
                        }
                    }

                    itemAdapter = new ItemAdapter(AnotherCollectionActivity.this, mItem, item.getImageURL());
                    recyclerView.setAdapter(itemAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onBackClick(View view) {
        finish();
    }
}