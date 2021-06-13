package com.StartupBBSR.competo.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.StartupBBSR.competo.Adapters.BannerEventPagerAdapter;
import com.StartupBBSR.competo.Adapters.VerticalRecyclerViewAdapter;
import com.StartupBBSR.competo.Models.BannerEvent;
import com.StartupBBSR.competo.Models.HorizontalModel;
import com.StartupBBSR.competo.Models.VerticalModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.databinding.FragmentFeedMainBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


public class FeedMainFragment extends Fragment {
    FragmentFeedMainBinding binding;
    BannerEventPagerAdapter bannerEventPagerAdapter;
    TabLayout indicatorTab;
    ViewPager bannerViewPager;
    List<BannerEvent> FeedBannerList;
    RecyclerView verticalRecycleView;
    VerticalRecyclerViewAdapter adapter;
    ArrayList<VerticalModel> arrayListVertical;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentFeedMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //VerticalRecyclerView
        arrayListVertical = new ArrayList<>();
        verticalRecycleView = view.findViewById(R.id.recyclerView);
        verticalRecycleView.setHasFixedSize(true);
        verticalRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        adapter = new VerticalRecyclerViewAdapter(getContext(), arrayListVertical);
        verticalRecycleView.setAdapter(adapter);
        setData();

        bannerViewPager = view.findViewById(R.id.banner_viewPager1);
        indicatorTab = view.findViewById(R.id.tab_indicator1);

        FeedBannerList = new ArrayList<>();
        FeedBannerList.add(new BannerEvent(1, "SGWOMENS MONTH", "https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg", ""));
        FeedBannerList.add(new BannerEvent(2, "SGWOMEN MONTH", "https://image.shutterstock.com/image-vector/womens-history-month-annual-that-600w-1328422742.jpg", ""));
        FeedBannerList.add(new BannerEvent(3, "SGWOME MONTH", "https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg", ""));
        FeedBannerList.add(new BannerEvent(4, "SGWOM MONTH", "https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg", ""));
        FeedBannerList.add(new BannerEvent(5, "SGWO MONTH", "https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg", ""));
        FeedBannerList.add(new BannerEvent(6, "SGW MONTH", "https://media.glassdoor.com/l/90/f1/7b/b8/company-event.jpg", ""));


        setBannerEventPagerAdapter(FeedBannerList);


        return view;
    }

    private void setData() {
        for (int i = 1; i <= 5; i++) {
            VerticalModel mVerticalModel = new VerticalModel();

            mVerticalModel.setTitle("Title" + i);

            ArrayList<HorizontalModel> arrayList = new ArrayList<>();

            for (int j = 0; j <= 5; j++) {

                HorizontalModel mHorizontalModel = new HorizontalModel();
                mHorizontalModel.setDescription("Description" + j);
                mHorizontalModel.setName("Name" + j);
                arrayList.add(mHorizontalModel);

            }
            mVerticalModel.setArrayList(arrayList);
            arrayListVertical.add(mVerticalModel);
        }

        adapter.notifyDataSetChanged();
    }

    private void setBannerEventPagerAdapter(List<BannerEvent> FeedBannerList) {
        bannerEventPagerAdapter = new BannerEventPagerAdapter(getContext(), FeedBannerList);
        bannerViewPager.setAdapter(bannerEventPagerAdapter);

        indicatorTab.setupWithViewPager(bannerViewPager);

        Timer sliderTimer = new Timer();
        sliderTimer.scheduleAtFixedRate(new AutoSlide(), 4000, 6000);
        indicatorTab.setupWithViewPager(bannerViewPager, true);

    }

    class AutoSlide extends TimerTask {

        @Override
        public void run() {
            if (getActivity() == null) return;
            getActivity().runOnUiThread(new Runnable() {
                @Override

                public void run() {

                    if (bannerViewPager.getCurrentItem() < FeedBannerList.size() - 1) {
                        bannerViewPager.setCurrentItem(bannerViewPager.getCurrentItem() + 1);
                    } else {
                        bannerViewPager.setCurrentItem(0);
                    }

                }

            });

        }
    }

}
