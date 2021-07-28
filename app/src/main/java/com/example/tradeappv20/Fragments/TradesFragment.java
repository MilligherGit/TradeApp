package com.example.tradeappv20.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.tradeappv20.CollectionActivity.Item;
import com.example.tradeappv20.CollectionActivity.ItemAdapter;
import com.example.tradeappv20.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TradesFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    Intent intent;

    List<Record> mRecord;
    List<Record> mRecord2;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    RecordAdapter recordAdapter;
    RecordAdapter recordAdapter2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trades, container, false);

        recyclerView2 = view.findViewById(R.id.recycler_view2);
        recyclerView2.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setStackFromEnd(true);
        linearLayoutManager2.setReverseLayout(true);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        readItems();

        return view;
    }

    private void readItems(){
        mRecord = new ArrayList<>();
        mRecord2 = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Records");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mRecord.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Record record = snapshot.getValue(Record.class);
                    if(record.getNew_owner().equals(firebaseUser.getUid())){
                        mRecord2.add(record);
                    }
                    if (record.getLast_owner().equals(firebaseUser.getUid())){
                        mRecord.add(record);
                    }

                    recordAdapter = new RecordAdapter(getContext(), mRecord); //, item.getImageURL()
                    recyclerView.setAdapter(recordAdapter);
                    recordAdapter2 = new RecordAdapter(getContext(), mRecord2); //, item.getImageURL()
                    recyclerView2.setAdapter(recordAdapter2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}