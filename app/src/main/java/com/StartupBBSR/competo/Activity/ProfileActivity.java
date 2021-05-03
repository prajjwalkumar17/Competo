package com.StartupBBSR.competo.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.StartupBBSR.competo.Fragments.ProfileAboutFragment;
import com.StartupBBSR.competo.Fragments.ProfileInterestsFragment;
import com.StartupBBSR.competo.Fragments.ProfileMyeventsFragment;
import com.StartupBBSR.competo.Fragments.ProfileUpdatesFragment;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.databinding.ActivityProfileBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding activityProfileBinding;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseDB;
    private String userId;


    Fragment profileFragment = null;
    FragmentManager profileFragmentManager;
    FragmentTransaction profileFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());

        //       Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getUid();


        activityProfileBinding.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
            }
        });

        profileFragment = new ProfileAboutFragment();
        profileFragmentManager = getSupportFragmentManager();
        profileFragmentTransaction = profileFragmentManager.beginTransaction();

        profileFragmentTransaction.replace(R.id.profileFrameLayout, profileFragment);
        profileFragmentTransaction.commit();


        activityProfileBinding.profileTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        profileFragment = new ProfileAboutFragment();
                        Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        profileFragment = new ProfileMyeventsFragment();
                        Toast.makeText(getApplicationContext(), "My Events", Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        profileFragment = new ProfileInterestsFragment();
                        Toast.makeText(getApplicationContext(), "Interests", Toast.LENGTH_SHORT).show();
                        break;

                    case 3:
                        profileFragment = new ProfileUpdatesFragment();
                        Toast.makeText(getApplicationContext(), "Updates", Toast.LENGTH_SHORT).show();
                        break;
                }

                profileFragmentManager = getSupportFragmentManager();
                profileFragmentTransaction = profileFragmentManager.beginTransaction();
                profileFragmentTransaction.replace(R.id.profileFrameLayout, profileFragment);
                profileFragmentTransaction.commit();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference = firebaseDB.collection("Users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                activityProfileBinding.profileName.setText(value.getString("Name"));
                Glide.with(getApplicationContext())
                        .load(value.getString("Photo"))
                        .into(activityProfileBinding.profileImage);
            }
        });
    }
}