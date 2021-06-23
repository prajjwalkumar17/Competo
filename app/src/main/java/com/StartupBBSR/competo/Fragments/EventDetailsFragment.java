package com.StartupBBSR.competo.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.StartupBBSR.competo.Adapters.InterestChipAdapter;
import com.StartupBBSR.competo.Models.EventModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.FragmentEventDetailsBinding;
import com.StartupBBSR.competo.databinding.FragmentHomeBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

public class EventDetailsFragment extends Fragment {

    private FragmentEventDetailsBinding binding;
    private EventModel eventModel;

    private List<String> mTagSet;
    private Constant constant;

    private int eventPresentFlag = 0;

    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.navigate(R.id.action_eventDetailsFragment_to_eventMainFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        constant = new Constant();

        EventModel model = (EventModel) getArguments().getSerializable("eventDetails");
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection(constant.getUsers())
                .document(FirebaseAuth.getInstance().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        List<String> myEvents = (List<String>) snapshot.get(constant.getUserMyEventField());
                        if (myEvents != null) {
                            for (String event : myEvents) {
                                if (event.equals(model.getEventID())) {
                                    binding.btnAddToMyEvents.setText("Remove from my events");
                                    eventPresentFlag = 1;
                                }
                            }
                        }
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

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
            }
        });

        binding.btnAddToMyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventPresentFlag == 0) {
//                    Add event to my event
                    CollectionReference myEventRef = FirebaseFirestore.getInstance().collection(constant.getUsers());
                    myEventRef.document(FirebaseAuth.getInstance().getUid())
                            .update(constant.getUserMyEventField(), FieldValue.arrayUnion(eventModel.getEventID()))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Event Added", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
//                    Remove event from my event
                    CollectionReference myEventRef = FirebaseFirestore.getInstance().collection(constant.getUsers());
                    myEventRef.document(FirebaseAuth.getInstance().getUid())
                            .update(constant.getUserMyEventField(), FieldValue.arrayRemove(eventModel.getEventID()))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Event Removed", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }

    private void initTagSet() {
        if (eventModel.getEventTags() != null) {
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