package com.example.tradeappv20.LoginActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.tradeappv20.MainActivity;
import com.example.tradeappv20.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username, email, password;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Регистрация");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);

        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                reference = FirebaseDatabase.getInstance().getReference("Users");

                validateEmail();
                validateUsername();
                validatePassword();

                if (validatePassword() && validateEmail() && validateUsername()) {
                    register(txt_username, txt_email, txt_password);
                }

//                if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
//                    Toast.makeText(RegisterActivity.this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show();
//                } else if(txt_password.length() < 6) {
//                    Toast.makeText(RegisterActivity.this, "Пароль должен быть длиннее 6 символов", Toast.LENGTH_SHORT).show();
//                } else if(txt_password.length() < 6) {
//                    Toast.makeText(RegisterActivity.this, "Пароль должен быть длиннее 6 символов", Toast.LENGTH_SHORT).show();
//                }  else {
//                    register(txt_username, txt_email, txt_password);
//                }
            }
        });
    }
    private Boolean validateUsername() {
        String val = username.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (val.isEmpty()) {
            username.setError("Поле должно быть заполнено");
            return false;
        }
        else if (val.length() >= 15) {
            username.setError("Допустимая длина до 15 символов");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            username.setError("Не допустимые символы");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (val.isEmpty()) {
            password.setError("Поле должно быть заполнено");
            return false;
        }
        else if (val.length() < 6) {
            password.setError("Допустимая длина от 6 символов");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            password.setError("Не допустимые символы");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = email.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            email.setError("Поле должно быть заполнено");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Некорректный email адрес");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private void register(String username, String email, String password){

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");
                            hashMap.put("user_info", "");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Ошибка рeгистрации!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}