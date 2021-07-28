package com.example.tradeappv20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tradeappv20.ChatActivity.ChatActivity;
import com.example.tradeappv20.ChatActivity.MessageActivity;
import com.example.tradeappv20.ChatActivity.User;
import com.example.tradeappv20.CollectionActivity.CollectionActivity;
import com.example.tradeappv20.LoginActivity.StartActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    View navHeader;
    Button exit;
    TextView profile_name;
    TextView profile_email;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile_image = findViewById(R.id.profile_image);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        NavigationView navigationView = findViewById(R.id.navigationView);
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        navHeader = navigationView.getHeaderView(0);
        exit = (Button)navHeader.findViewById(R.id.exit);
        username = (TextView)navHeader.findViewById(R.id.nav_username);
        username.setText(firebaseUser.getEmail());
        profile_image = (CircleImageView)navHeader.findViewById(R.id.profile_image);
        NavigationUI.setupWithNavController(navigationView, navController);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.drawable.profile);
                } else {
                    try {
                        Glide.with(MainActivity.this).load(user.getImageURL()).into(profile_image);
                    }catch (Exception e){
                        //Toast.makeText(MainActivity.this, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        ProfileFragment profileFragmentName = (ProfileFragment) fragmentManager.findFragmentById(R.id.profile_name);
//        profileFragmentName.setProfileName(username.getText().toString());
//        ProfileFragment profileFragmentEmail = (ProfileFragment) fragmentManager.findFragmentById(R.id.profile_emale);
//        profileFragmentEmail.setProfileEmail(firebaseUser.getEmail());
    }

    public void onChatClick(MenuItem item){
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    public void onExitClick(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, StartActivity.class));
        finish();
    }

//    public void onCollectionClick(MenuItem item) {
//        Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
//        startActivity(intent);
//    }
}