package com.StartupBBSR.competo.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.StartupBBSR.competo.databinding.FragmentInboxBinding;

import androidx.fragment.app.Fragment;

public class InboxFragment extends Fragment {

    private FragmentInboxBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInboxBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }
}