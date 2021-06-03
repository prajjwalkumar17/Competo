package com.StartupBBSR.competo.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.databinding.FragmentInboxBinding;
import com.StartupBBSR.competo.databinding.FragmentInboxMainBinding;


public class inbox_main extends Fragment {

    private FragmentInboxMainBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        database= FirebaseDatabase.getInstance();
        binding = FragmentInboxMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();



        // Inflate the layout for this fragment
        return view;
    }
}