package com.StartupBBSR.competo.Fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.StartupBBSR.competo.Adapters.ManageEventMainAdapter;
import com.StartupBBSR.competo.Models.EventModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.FragmentManageEventMainBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class ManageEventMainFragment extends Fragment {

    private FragmentManageEventMainBinding binding;

    private NavController navController;

    private String[] manageEventTitles = new String[]{"Live Events" , "Draft Events"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageEventMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        binding.topAppBar.setOnMenuItemClickListener(item -> {
            int menu = item.getItemId();
            if (menu == R.id.btnAddEvent) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("editEvent", null);
                navController.navigate(R.id.action_manageEventMainFragment_to_addEventFragment, bundle);
            }
            return false;
        });

        init();

        return view;
    }

    private void init() {
        binding.manageEventViewPager.setAdapter(new ManageEventViewPagerAdapter(this));

        new TabLayoutMediator(binding.manageEventTabLayout,
                binding.manageEventViewPager, ((tab, position) ->
                tab.setText(manageEventTitles[position])
        )).attach();
    }

    private class ManageEventViewPagerAdapter extends FragmentStateAdapter {

        public ManageEventViewPagerAdapter(@NonNull ManageEventMainFragment fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new ManageLiveEventFragment();
                case 1:
                    return new ManageDraftEventFragment();
            }
            return new ManageLiveEventFragment();
        }

        @Override
        public int getItemCount() {
            return manageEventTitles.length;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }
}