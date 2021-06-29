package com.StartupBBSR.competo.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.StartupBBSR.competo.R;

public class FindFragment extends Fragment {

    public FindFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find, container, false);
    }

    public void findTeamMate() {
        Bundle bundle = new Bundle();
        bundle.putString("ft", "teamMate");
        Fragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, homeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}