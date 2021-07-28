package com.example.tradeappv20.CollectionActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tradeappv20.ChatActivity.User;
import com.example.tradeappv20.LoginActivity.RegisterActivity;
import com.example.tradeappv20.MainActivity;
import com.example.tradeappv20.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddCollectionItemActivity extends AppCompatActivity {

    String mUri = "";

    ImageView item_image;
    EditText item_name;
    EditText item_description;
    EditText edit_tag1;
    EditText edit_tag2;
    EditText edit_tag3;
    EditText edit_tag4;
    EditText edit_tag5;
    SwitchCompat switch_compat;
    Button add_btn;
    Uri item_image_uri;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    StorageReference storageReference;
    private static final int PICK_IMAGE = 1;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collection_item);

        item_image = findViewById(R.id.item_image);
        item_name = findViewById(R.id.item_name);
        item_description = findViewById(R.id.item_description);
        switch_compat = findViewById(R.id.switch_compat);
        add_btn = findViewById(R.id.add_btn);
        edit_tag1 = findViewById(R.id.edit_tag1);
        edit_tag2 = findViewById(R.id.edit_tag2);
        edit_tag3 = findViewById(R.id.edit_tag3);
        edit_tag4 = findViewById(R.id.edit_tag4);
        edit_tag5 = findViewById(R.id.edit_tag5);


        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        // Нажатие на картинку
        item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });


        // Сохраняем все
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateItemname();
                // Если изображение выбрано и имя соответствует требованиям
                if(!mUri.equals("") && validateItemname()) {
                    String txt_item_name = item_name.getText().toString();
                    String txt_item_description = item_description.getText().toString();

                    final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                    uploadTask = fileReference.putFile(imageUri);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return fileReference.getDownloadUrl();
                        }
                    });

                    addItem(firebaseUser.getUid(), txt_item_name, txt_item_description, item_image_uri);
                } else {
                    if(mUri.equals("")) {
                        Toast.makeText(AddCollectionItemActivity.this, "Выберите изображение", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void onBackClick(View view) {
        Intent intent = new Intent(AddCollectionItemActivity.this, CollectionActivity.class);
        startActivity(intent);
        finish();
    }

    private void addItem(String owner, String item_name, String item_description, Uri item_uri) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", UUID.randomUUID().toString());
        hashMap.put("owner", owner);
        hashMap.put("item_name", item_name);
        hashMap.put("item_description", item_description);
        hashMap.put("imageURL", "" + mUri);
        hashMap.put("tag1", edit_tag1.getText().toString());
        hashMap.put("tag2", edit_tag2.getText().toString());
        hashMap.put("tag3", edit_tag3.getText().toString());
        hashMap.put("tag4", edit_tag4.getText().toString());
        hashMap.put("tag5", edit_tag5.getText().toString());
        if (switch_compat.isChecked()) {
            hashMap.put("for_free", "yes");
        } else {
            hashMap.put("for_free", "no");
        }

        // когда предмет добавлен возращает обратно в коллекцию
        reference.child("Items").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    finish();
                }
            }
        });
    }

    // нормоконтроль названия предмета
    private Boolean validateItemname() {
        String val = item_name.getText().toString();
        //String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (val.isEmpty()) {
            item_name.setError("Поле должно быть заполнено");
            return false;
        }
        else if (val.length() >= 25) {
            item_name.setError("Допустимая длина до 25 символов");
            return false;
//        } else if (!val.matches(noWhiteSpace)) {
//            item_name.setError("Не допустимые символы");
//            return false;
        } else {
            item_name.setError(null);
            return true;
        }
    }

    // все ниже это сложные манипуляции открытия галереи телефона и сохранения картинки в базу изображений
    private void openImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = AddCollectionItemActivity.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(AddCollectionItemActivity.this);
        pd.setMessage("Загрузка...");
        pd.show();

        if (imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        mUri = downloadUri.toString();
                        Glide.with(AddCollectionItemActivity.this).load(mUri).into(item_image);
                        pd.dismiss();
                    } else {
                        Toast.makeText(AddCollectionItemActivity.this, "Ошибка!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddCollectionItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(AddCollectionItemActivity.this, "Изображение не выбрано!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(AddCollectionItemActivity.this, "Загрузка...", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }

}