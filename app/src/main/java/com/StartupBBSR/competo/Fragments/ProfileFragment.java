package com.StartupBBSR.competo.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.StartupBBSR.competo.Activity.EditProfileActivity;
import com.StartupBBSR.competo.databinding.FragmentProfileBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    // tab titles
    private String[] profileTabTitles = new String[]{"About", "My Events", "Interests", "Updates"};

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseDB;
    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DocumentReference documentReference = firebaseDB.collection("Users").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                binding.profileName.setText(value.getString("Name"));
                Glide.with(getContext())
                        .load(value.getString("Photo"))
                        .into(binding.profileImage);
            }
        });
    }

    private void init() {
        binding.profileViewPager.setAdapter(new ProfileViewPagerFragmentAdapter(this));

        new TabLayoutMediator(binding.profileTablayout,
                binding.profileViewPager, ((tab, position) ->
                tab.setText(profileTabTitles[position])
        )).attach();
    }

    private class ProfileViewPagerFragmentAdapter extends FragmentStateAdapter {
        public ProfileViewPagerFragmentAdapter(@NonNull ProfileFragment fragmentActivity) {
            super(fragmentActivity);

        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new ProfileAboutFragment();
                case 1:
                    return new ProfileMyeventsFragment();
                case 2:
                    return new ProfileInterestsFragment();
                case 3:
                    return new ProfileUpdatesFragment();
            }
            return new ProfileAboutFragment();
        }

        @Override
        public int getItemCount() {
            return profileTabTitles.length;
        }
    }
}