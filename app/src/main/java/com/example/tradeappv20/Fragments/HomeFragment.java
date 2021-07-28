package com.example.tradeappv20.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tradeappv20.CollectionActivity.CollectionActivity;
import com.example.tradeappv20.CollectionActivity.Item;
import com.example.tradeappv20.CollectionActivity.ItemAdapter;
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

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    Intent intent;

    EditText search_items;
    Button search_btn;

    List<Item> mItem;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    ItemAdapter itemAdapter;
    String search = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        search_items = view.findViewById(R.id.search_items);
        search_btn = view.findViewById(R.id.search_btn);
        search_items.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                search = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search_items.getText().toString().equals("")){
                    readItems();
                } else {
                    searchTags(search);
                }
            }
        });

        readItems();

        return view;
    }

    private void readItems(){
        mItem = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Items");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mItem.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Item item = snapshot.getValue(Item.class);
                    if(!item.getOwner().equals(firebaseUser.getUid())){
                        mItem.add(item);
                    }

                    itemAdapter = new ItemAdapter(getContext(), mItem); //, item.getImageURL()
                    recyclerView.setAdapter(itemAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchTags(String s) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mItem.clear();
        for(int i = 1; i<=5; i++) {
            Query query = FirebaseDatabase.getInstance().getReference("Items").orderByChild("tag" + i).equalTo(s); //endAt(s); //equalTo("*"+s+"*"); //startAt(s).endAt(s + "\uf8ff");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Item item = snapshot.getValue(Item.class);
                        assert item != null;
                        assert firebaseUser != null;
                        mItem.add(item);
                    }

                    itemAdapter = new ItemAdapter(getContext(), mItem);
                    recyclerView.setAdapter(itemAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}