package com.StartupBBSR.competo.Activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.StartupBBSR.competo.Fragments.FindFragment;
import com.StartupBBSR.competo.Fragments.HomeFragment;
import com.StartupBBSR.competo.Fragments.ProfileFragment;
import com.StartupBBSR.competo.Fragments.WishlistFragment;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ActivityMainBinding activityMainBinding;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavHostFragment navHostFragment;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());


        drawerLayout = activityMainBinding.drawer;
        navigationView = activityMainBinding.navView;
        navigationView.setNavigationItemSelectedListener(this);

//        Bottom Navigation bar
        bottomNavigationView = activityMainBinding.bottomNavBar;


// TODO: 5/12/2021 implement bottom nav bar using nav host 
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


        activityMainBinding.actionBar.drawerToggleIcon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                if (!drawerLayout.isDrawerOpen(Gravity.START))
                    drawerLayout.openDrawer(Gravity.START);
            }
        });
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
}