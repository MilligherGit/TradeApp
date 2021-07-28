package com.example.tradeappv20.CollectionActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.example.tradeappv20.ChatActivity.Adapters.MessageAdapter;
import com.example.tradeappv20.ChatActivity.Adapters.UserAdapter;
import com.example.tradeappv20.ChatActivity.Chat;
import com.example.tradeappv20.ChatActivity.MessageActivity;
import com.example.tradeappv20.ChatActivity.User;
import com.example.tradeappv20.MainActivity;
import com.example.tradeappv20.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Intent intent;

    EditText search_users;

    List<Item> mItem;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent = getIntent();

        //String userid = intent.getStringExtra("userid");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readItems(firebaseUser.getUid()); //, item.getImageURL()
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        search_users = findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchTags(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void searchTags(String s) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Items").orderByChild("item_description").startAt(s).endAt(s + "\uf8ff"); //endAt(s); //equalTo("*"+s+"*"); //startAt(s).endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mItem.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    assert item != null;
                    assert firebaseUser != null;
                        mItem.add(item);
                }

                itemAdapter = new ItemAdapter(CollectionActivity.this, mItem);
                recyclerView.setAdapter(itemAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readItems(String myid){
        mItem = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Items");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mItem.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Item item = snapshot.getValue(Item.class);
                    if(item.getOwner().equals(myid)){
                        mItem.add(item);
                    }

                    itemAdapter = new ItemAdapter(CollectionActivity.this, mItem);
                    recyclerView.setAdapter(itemAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onAddCollectionButtonClick(View view) {
        startActivity(new Intent(CollectionActivity.this, AddCollectionItemActivity.class));
    }

    public void onBackClick(View view) {
        finish();
    }
}