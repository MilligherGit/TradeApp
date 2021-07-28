package com.example.tradeappv20.ChatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tradeappv20.ChatActivity.Adapters.ViewPagerAdapter;
import com.example.tradeappv20.ChatActivity.Fragments.ChatsFragment;
import com.example.tradeappv20.ChatActivity.Fragments.UsersFragment;
import com.example.tradeappv20.MainActivity;
import com.example.tradeappv20.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    DatabaseReference reference;
    FirebaseUser firebaseUser;

    String back = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(ChatsFragment.getInstance(), "Чаты");
        viewPagerAdapter.addFragment(UsersFragment.getInstance(), "Пользователи");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        back = intent.getStringExtra("back");
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    public void onBackClick(View view) {
        try {
            if (back.equals("chat")) {
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                finish();
            }
        }catch (Exception e){
            finish();
        }
    }
}