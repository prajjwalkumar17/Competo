package com.StartupBBSR.competo.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.StartupBBSR.competo.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private ViewPager2 viewPager2;
    private int flag = 0;


    // tab titles
    private String[] homeTabTitles = new String[]{"Feed", "Team Finder", "Events", "Inbox"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getString("ft").equals("teamMate")) {
                flag = 1;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewPager2 = binding.homeViewpager;

        init();

        if (flag == 1) {
            findTeamMate();
        }
        return view;
    }


    public void init() {
        viewPager2.setAdapter(new HomeViewPagerFragmentAdapter(this));
      //  viewPager2.setUserInputEnabled(false);

//        Attaching tab mediator
        new TabLayoutMediator(binding.tabLayout2,
                binding.homeViewpager, ((tab, position) ->
                tab.setText(homeTabTitles[position])
        )).attach();
    }

    public void viewAllEvents() {
        viewPager2.setCurrentItem(2, true);
    }

    public void findTeamMate() {
        viewPager2.setCurrentItem(1, true);
    }


    private class HomeViewPagerFragmentAdapter extends FragmentStateAdapter {

        public HomeViewPagerFragmentAdapter(@NonNull HomeFragment fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new FeedFragment();
                case 1:
                    return new EventPalFragment();
                case 2:
                    return new EventFragment();
                case 3:
                    return new InboxFragment();
            }
            return new FeedFragment();
        }

        @Override
        public int getItemCount() {
//            tab size
            return homeTabTitles.length;
        }
    }
}
