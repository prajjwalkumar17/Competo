package com.StartupBBSR.competo.Activity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import jp.wasabeef.glide.transformations.BlurTransformation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.StartupBBSR.competo.Fragments.FindFragment;
import com.StartupBBSR.competo.Fragments.HomeFragment;
import com.StartupBBSR.competo.Fragments.ProfileFragment;
import com.StartupBBSR.competo.Fragments.WishlistFragment;
import com.StartupBBSR.competo.Models.UserModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.ActivityMainBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ActivityMainBinding activityMainBinding;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View header;
    private NavHostFragment navHostFragment;
    private BottomNavigationView bottomNavigationView;

    private DocumentReference documentReference;
    private FirebaseFirestore firestoreDB;
    private FirebaseAuth firebaseAuth;
    private String userid;
    private DocumentSnapshot documentSnapshot;

    private Constant constant;
    private UserModel userModel;


    private static final String TAG = "test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());


        drawerLayout = activityMainBinding.drawer;
        navigationView = activityMainBinding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);

//        Bottom Navigation bar
        bottomNavigationView = activityMainBinding.bottomNavBar;


        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            switch (item.getItemId()){
                case R.id.homeFragment:
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    break;

                case R.id.findFragment:
                    fragment = new FindFragment();
                    loadFragment(fragment);
                    break;

                case R.id.wishlistFragment:
                    fragment = new WishlistFragment();
                    loadFragment(fragment);
                    break;

                case R.id.profileFragment:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    break;
            }
            return true;

        });

        firestoreDB = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userid = firebaseAuth.getUid();
        constant = new Constant();
        userModel = new UserModel();

        documentReference =firestoreDB.collection(constant.getUsers()).document(userid);


        activityMainBinding.actionBar.drawerToggleIcon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                if (!drawerLayout.isDrawerOpen(Gravity.START))
                    drawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

//        Get Data when this activity starts
        getUserData();
    }

    private void getUserData() {
//      get realtime data and store it in a class
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Log.d(TAG, "onEvent: " + error.toString());
                    return;
                }

                if (value != null && value.exists()){
                    documentSnapshot = value;
                    saveDataToClass();
                    Log.d(TAG, "onEvent: " + value.getData());
                }
            }
        });
    }

    private void saveDataToClass() {

        getIntent().putExtra(constant.getUserModelObject(), userModel);

        userModel.setUserName(documentSnapshot.getString(constant.getUserNameField()));
        userModel.setUserEmail(documentSnapshot.getString(constant.getUserEmailField()));
        userModel.setUserPhoto(documentSnapshot.getString(constant.getUserPhotoField()));
        userModel.setUserBio(documentSnapshot.getString(constant.getUserBioField()));
        userModel.setUserLinkedin(documentSnapshot.getString(constant.getUserLinkedinField()));
        userModel.setUserPhone(documentSnapshot.getString(constant.getUserPhoneField()));
        userModel.setUserRole(documentSnapshot.getString(constant.getUserisUserField()));
        userModel.setOrganizerRole(documentSnapshot.getString(constant.getUserisOrganizerField()));
        userModel.setUserChips((List<String>) documentSnapshot.get(constant.getUserInterestedChipsField()));

        Log.d(TAG, "saveDataToClass: " + userModel.getUserChips());


        updateHeader();

    }

    private void loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logOut)
            logout();
        else if (id == R.id.menu_settings)
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();

        return true;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    private void updateHeader() {
        TextView tvname = header.findViewById(R.id.tvHeaderName);
        ImageView ivprofile = header.findViewById(R.id.header_image);
        ImageView ivprofilebackground = header.findViewById(R.id.headerBackgroundImage);
        tvname.setText(userModel.getUserName());
        String imguri = userModel.getUserPhoto();
        if (imguri != null){
            loadUsingGlide(imguri, ivprofile, 1, 1);
            loadUsingGlide(imguri, ivprofilebackground, 25, 5);
        }
    }


    private void loadUsingGlide(String imgurl, ImageView imageView, int radius, int sampling) {
        Glide.with(this).
                load(imgurl).
                apply(RequestOptions.bitmapTransform(new BlurTransformation(radius, sampling)))
                .into(imageView);
    }
}