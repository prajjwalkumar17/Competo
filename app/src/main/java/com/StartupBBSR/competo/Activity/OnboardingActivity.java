package com.StartupBBSR.competo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.StartupBBSR.competo.Adapters.OnboardingAdapter;
import com.StartupBBSR.competo.Models.OnboardingModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.ViewPagerTransformer;
import com.StartupBBSR.competo.databinding.ActivityOnboardingBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;
    private OnboardingAdapter adapter;

    private int position;

    Animation getstartedbtnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //       Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

//        In case we need to make it full screen
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/


//        If this activity has already been opened
        if (receivedPrefData()) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        getstartedbtnAnim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.onboarding_getstartedbuttton_animation);

//        Fill list
        List<OnboardingModel> onboardingModelList = new ArrayList<>();
        onboardingModelList.add(new OnboardingModel("Fresh Food", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book", R.drawable.cover_image));
        onboardingModelList.add(new OnboardingModel("Fast Delivery", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book", R.drawable.iron_man));
        onboardingModelList.add(new OnboardingModel("Easy Payment", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book", R.drawable.latte));


//        Set adapter
        adapter = new OnboardingAdapter(this, onboardingModelList);
        binding.viewpager.setAdapter(adapter);
        binding.viewpager.setPageTransformer(new ViewPagerTransformer());

//        Set tabindicators
//        new TabLayoutMediator(binding.tabIndicator, binding.viewpager, ((tab, position1) -> tab.setText(String.valueOf(position1)))).attach();
        new TabLayoutMediator(binding.tabIndicator, binding.viewpager, ((tab, position1) -> tab.setIcon(R.drawable.tabindicator_selector))).attach();

        binding.btnOnboardingNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = binding.viewpager.getCurrentItem();
                if (position < onboardingModelList.size()) {
                    position++;
                    binding.viewpager.setCurrentItem(position);
                }

                if (position == onboardingModelList.size() - 1) {
                    binding.btnOnboardingNext.setVisibility(View.GONE);
                    binding.btnOnboardingGetstarted.setVisibility(View.VISIBLE);
                    binding.btnOnboardingGetstarted.setAnimation(getstartedbtnAnim);
                }
            }
        });

        binding.btnOnboardingGetstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                savePreferenceData();
                finish();
            }
        });


        binding.tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == onboardingModelList.size() - 1) {
                    binding.btnOnboardingNext.setVisibility(View.GONE);
                    binding.btnOnboardingGetstarted.setVisibility(View.VISIBLE);
                    binding.btnOnboardingGetstarted.setAnimation(getstartedbtnAnim);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private boolean receivedPrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("onboardingPref", MODE_PRIVATE);
        Boolean isOnboardingIntroduced = pref.getBoolean("isOnboardingIntroduced", false);
        return isOnboardingIntroduced;
    }

    private void savePreferenceData() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("onboardingPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isOnboardingIntroduced", true);
        editor.commit();
    }
}