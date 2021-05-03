package com.StartupBBSR.competo.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.StartupBBSR.competo.databinding.ActivityEditProfileBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding activityEditProfileBinding;


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseDB;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String userId;

    private int flag = 0;
    private int REQUEST_PHOTO_CODE = 123;
    private Uri profileImageUri, mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEditProfileBinding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(activityEditProfileBinding.getRoot());

//        Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getUid();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        DocumentReference documentReference = firebaseDB.collection("Users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                activityEditProfileBinding.etName.setText(value.getString("Name"));
                activityEditProfileBinding.etEmail.setText(value.getString("Email"));
                activityEditProfileBinding.etPhone.setText(value.getString("Phone"));
                activityEditProfileBinding.BioTV.setText(value.getString("Bio"));
                activityEditProfileBinding.etLinkedIn.setText(value.getString("LinkedIn"));
                Glide.with(getApplicationContext()).load(value.getString("Photo"))
                        .into(activityEditProfileBinding.profileImage);
//                activityEditProfileBinding.profileImage.setImageURI(Uri.parse(value.getString("Photo")));

            }
        });

        activityEditProfileBinding.btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyInput();
            }
        });


        activityEditProfileBinding.btnEditProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_PHOTO_CODE);
            }
        });

        activityEditProfileBinding.tvEditProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileActivity.super.onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHOTO_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            profileImageUri = data.getData();
            activityEditProfileBinding.profileImage.setImageURI(profileImageUri);
            // TODO: 4/25/2021 shift uploadPicture
            uploadPicture();
        }
    }

    private void uploadPicture() {
        StorageReference ref = storageReference.child("ProfileImages/" + userId);
        ref.putFile(profileImageUri);

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                mUri = uri;
                Log.d("test", "onSuccess: " + mUri);
            }
        });
    }

    private void verifyInput() {
        flag = 0;
        checkEmptyField(activityEditProfileBinding.etName);
        checkEmptyField(activityEditProfileBinding.BioTV);
        checkEmptyField(activityEditProfileBinding.etEmail);
        checkEmptyField(activityEditProfileBinding.etPhone);
        checkEmptyField(activityEditProfileBinding.etLinkedIn);

        if (flag == 5) {
            updateUser();
        } else {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkEmptyField(EditText et) {
        if (et.getText().toString().isEmpty())
            flag--;
        else
            flag++;
    }

    private void updateUser() {


        DocumentReference documentReference = firebaseDB.collection("Users")
                .document(userId);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("Name", activityEditProfileBinding.etName.getText().toString());
        userInfo.put("Phone", activityEditProfileBinding.etPhone.getText().toString());
        userInfo.put("Email", activityEditProfileBinding.etEmail.getText().toString());
        userInfo.put("LinkedIn", activityEditProfileBinding.etLinkedIn.getText().toString());
        userInfo.put("Bio", activityEditProfileBinding.BioTV.getText().toString());
//        Log.d("test", "onSuccess: " + mUri);
        if (mUri != null){
            userInfo.put("Photo", mUri.toString());
        }
        documentReference.update(userInfo);
    }
}