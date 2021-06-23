package com.StartupBBSR.competo.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.StartupBBSR.competo.databinding.FragmentTeamBinding;

import androidx.fragment.app.Fragment;

public class TeamFragment extends Fragment {

    private FragmentTeamBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTeamBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }
}