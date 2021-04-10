package com.StartupBBSR.competo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.StartupBBSR.competo.Activity.LoginActivity;
import com.StartupBBSR.competo.Adapters.BannerEventPagerAdapter;
import com.StartupBBSR.competo.Models.BannerEvent;
import com.StartupBBSR.competo.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    BannerEventPagerAdapter bannerEventPagerAdapter;
    TabLayout indicatorTab,categoryTab;
    ViewPager bannerViewPager;
    List<BannerEvent> homeBannerList;
    List<BannerEvent> eventBannerList;
    List<BannerEvent> communicationFormBannerList;
    List<BannerEvent> eventPalBannerList;
//    List<BannerEvent> IndexBannerList;


    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        indicatorTab =findViewById(R.id.tab_indicator);
        categoryTab= findViewById(R.id.tabLayout2);


        homeBannerList =new ArrayList<>();
        homeBannerList.add(new BannerEvent(1,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        homeBannerList.add(new BannerEvent(2,"LITTLE WOMEN","http://androidappsforyoutube.s3.ap-south-1.amazonaws.com/primevideo/banners/homebanner2.png",""));
        homeBannerList.add(new BannerEvent(3,"BHOOT","https://png.pngtree.com/thumb_back/fw800/back_pic/05/06/22/87596c72e3222da.jpg",""));
        homeBannerList.add(new BannerEvent(4,"MIRZAPUR","http://androidappsforyoutube.s3.ap-south-1.amazonaws.com/primevideo/banners/homebanner4.png",""));
        homeBannerList.add(new BannerEvent(5,"PIKACHU","http://androidappsforyoutube.s3.ap-south-1.amazonaws.com/primevideo/banners/homebanner5.png",""));
        homeBannerList.add(new BannerEvent(6,"PONMAGAL VANDHAL","http://androidappsforyoutube.s3.ap-south-1.amazonaws.com/primevideo/banners/homebanner3.png",""));
        homeBannerList.add(new BannerEvent(7,"LITTLE WOMEN","http://androidappsforyoutube.s3.ap-south-1.amazonaws.com/primevideo/banners/homebanner2.png",""));
        homeBannerList.add(new BannerEvent(8,"BHOOT","","http://androidappsforyoutube.s3.ap-south-1.amazonaws.com/primevideo/banners/homebanner1.png"));
        homeBannerList.add(new BannerEvent(9,"MIRZAPUR","http://androidappsforyoutube.s3.ap-south-1.amazonaws.com/primevideo/banners/homebanner3.png",""));
        homeBannerList.add(new BannerEvent(10,"PIKACHU","http://androidappsforyoutube.s3.ap-south-1.amazonaws.com/primevideo/banners/homebanner4.png",""));

        eventBannerList =new ArrayList<>();
        eventBannerList.add(new BannerEvent(1,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        eventBannerList.add(new BannerEvent(2,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        eventBannerList.add(new BannerEvent(3,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        eventBannerList.add(new BannerEvent(4,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        eventBannerList.add(new BannerEvent(5,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        eventBannerList.add(new BannerEvent(6,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        eventBannerList.add(new BannerEvent(7,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        eventBannerList.add(new BannerEvent(8,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        eventBannerList.add(new BannerEvent(9,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));


         communicationFormBannerList =new ArrayList<>();
        communicationFormBannerList.add(new BannerEvent(1,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        communicationFormBannerList.add(new BannerEvent(2,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        communicationFormBannerList.add(new BannerEvent(3,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        communicationFormBannerList.add(new BannerEvent(4,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        communicationFormBannerList.add(new BannerEvent(5,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        communicationFormBannerList.add(new BannerEvent(6,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        communicationFormBannerList.add(new BannerEvent(7,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        communicationFormBannerList.add(new BannerEvent(8,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        communicationFormBannerList.add(new BannerEvent(9,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));

            eventPalBannerList=new ArrayList<>();
       eventPalBannerList.add(new BannerEvent(1,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        eventPalBannerList.add(new BannerEvent(2,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        eventPalBannerList.add(new BannerEvent(3,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        eventPalBannerList.add(new BannerEvent(4,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        eventPalBannerList.add(new BannerEvent(5,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        eventPalBannerList.add(new BannerEvent(6,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        eventPalBannerList.add(new BannerEvent(7,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        eventPalBannerList.add(new BannerEvent(8,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));
        eventPalBannerList.add(new BannerEvent(9,"PONMAGAL VANDHAL","https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg",""));


           //this is default tab selected
        setBannerEventPagerAdapter(homeBannerList);
        //on tab change selected data
        categoryTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 1:
                         setBannerEventPagerAdapter(eventBannerList);
                        return;
                    case 2:
                       setBannerEventPagerAdapter(communicationFormBannerList);
                     return;
                    case 3:
                       setBannerEventPagerAdapter(eventPalBannerList);
                     return;
                    default:
                        setBannerEventPagerAdapter(homeBannerList);

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
    private void setBannerEventPagerAdapter(List<BannerEvent>bannerEventList){

        bannerViewPager = findViewById(R.id.banner_viewPager);
        bannerEventPagerAdapter=new BannerEventPagerAdapter(this,bannerEventList);
        bannerViewPager.setAdapter(bannerEventPagerAdapter);

        indicatorTab.setupWithViewPager(bannerViewPager);


        Timer sliderTimer =new Timer();
        sliderTimer.scheduleAtFixedRate(new AutoSlide(),4000,6000);
        indicatorTab.setupWithViewPager(bannerViewPager, true);



    }
    class  AutoSlide extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override

                public void run() {

                    if (bannerViewPager.getCurrentItem() < homeBannerList.size() - 1) {
                        bannerViewPager.setCurrentItem(bannerViewPager.getCurrentItem() + 1);
                    } else {
                        bannerViewPager.setCurrentItem(0);
                    }

                }

            });


            activityMainBinding.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            });
        }
    }
}