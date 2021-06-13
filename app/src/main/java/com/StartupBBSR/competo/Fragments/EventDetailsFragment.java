package com.StartupBBSR.competo.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.StartupBBSR.competo.Adapters.EventFragmentAdapter;
import com.StartupBBSR.competo.Adapters.InterestChipAdapter;
import com.StartupBBSR.competo.Models.EventModel;
import com.StartupBBSR.competo.Models.FragmentEventModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.databinding.FragmentEventDetailsBinding;
import com.StartupBBSR.competo.databinding.InterestChipItemBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class EventDetailsFragment extends Fragment {

    private FragmentEventDetailsBinding binding;
    private EventModel eventModel;

    private List<String> mTagSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventModel = (EventModel) getArguments().getSerializable("eventDetails");

        binding.tvEventTitle.setText(eventModel.getEventTitle());
        binding.tvEventDescription.setText(eventModel.getEventDescription());
        binding.tvEventDate.setText(eventModel.getEventDate());
        binding.tvEventVenue.setText(eventModel.getEventVenue());
        binding.tvEventTime.setText(eventModel.getEventTime());
        Glide.with(getContext()).load(eventModel.getEventPoster()).into(binding.ivImage);


        initTagSet();
        initTagRecycler();

        binding.btnEventRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri eventLink = Uri.parse(eventModel.getEventLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, eventLink);
                startActivity(intent);

            }
        });

        binding.btnEventFindPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "To be implemented", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initTagSet() {
        if (eventModel.getEventTags() != null){
            mTagSet = eventModel.getEventTags();
        } else
            binding.eventTagRecyclerView.setVisibility(View.GONE);
    }

    private void initTagRecycler() {
        RecyclerView tagRecyclerView = binding.eventTagRecyclerView;
        tagRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        InterestChipAdapter adapter = new InterestChipAdapter(mTagSet);
        tagRecyclerView.setAdapter(adapter);
    }
}