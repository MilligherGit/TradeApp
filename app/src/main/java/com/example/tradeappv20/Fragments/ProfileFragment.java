package com.example.tradeappv20.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.tradeappv20.ChatActivity.User;
import com.example.tradeappv20.CollectionActivity.AnotherCollectionActivity;
import com.example.tradeappv20.CollectionActivity.Item;
import com.example.tradeappv20.CollectionActivity.ItemAdapter;
import com.example.tradeappv20.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    CircleImageView image_profile;
    TextView username;
    TextView user_info;
    TextView edit;
    Boolean editing = false;
    EditText user_info_edit;
    EditText username_edit;
    Button end_edit;

    DatabaseReference reference;
    FirebaseUser firebaseUser;

    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        image_profile = view.findViewById(R.id.profile_icon);
        username = view.findViewById(R.id.username);
        user_info = view.findViewById(R.id.user_info);
        edit = view.findViewById(R.id.edit);
        user_info_edit = view.findViewById(R.id.user_info_edit);
        end_edit = view.findViewById(R.id.end_edit);
        username_edit = view.findViewById(R.id.username_edit);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());
                user_info.setText(user.getUser_info());
                user_info_edit.setText(user.getUser_info());
                username_edit.setText(user.getUsername());

                if (user.getImageURL().equals("default")){
                    image_profile.setImageResource(R.drawable.profile);
                } else {
                    Glide.with(getContext()).load(user.getImageURL()).into(image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_info.setVisibility(View.GONE);
                user_info_edit.setVisibility(View.VISIBLE);
                end_edit.setVisibility(View.VISIBLE);
                username_edit.setVisibility(View.VISIBLE);
                username.setVisibility(View.GONE);
                editing = true;
            }
        });

        end_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUsername();
                if(validateUsername()) {
                    user_info.setVisibility(View.VISIBLE);
                    username.setVisibility(View.VISIBLE);
                    user_info_edit.setVisibility(View.GONE);
                    end_edit.setVisibility(View.GONE);
                    username_edit.setVisibility(View.GONE);
                    editing = false;
                    username.setText(username_edit.getText().toString());
                    user_info.setText(user_info_edit.getText().toString());
                    DatabaseReference editReference = FirebaseDatabase.getInstance().getReference("Users");
                    editReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot edit_dataSnapshot) {
                            for (DataSnapshot edit_snapshot : edit_dataSnapshot.getChildren()) {
                                User user = edit_snapshot.getValue(User.class);
                                if (user.getId().equals(firebaseUser.getUid())) {
                                    edit_snapshot.getRef().child("user_info").setValue(user_info_edit.getText().toString());
                                    edit_snapshot.getRef().child("username").setValue(username_edit.getText().toString());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });


        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editing) {
                    openImage();
                }
            }
        });


        return view;
    }

    private Boolean validateUsername() {
        String val = username_edit.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (val.isEmpty()) {
            username_edit.setError("Поле должно быть заполнено");
            return false;
        }
        else if (val.length() >= 15) {
            username_edit.setError("Допустимая длина до 15 символов");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            username_edit.setError("Не допустимые символы");
            return false;
        } else {
            username_edit.setError(null);
            return true;
        }
    }

    private void openImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getContext());
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
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", ""+mUri);
                        reference.updateChildren(map);

                        pd.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Ошибка!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "Изображение не выбрано!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Загрузка...", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }
}