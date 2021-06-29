package com.StartupBBSR.competo.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.StartupBBSR.competo.Adapters.FindEventAdapter;
import com.StartupBBSR.competo.Adapters.FindUserAdapter;
import com.StartupBBSR.competo.Models.EventModel;
import com.StartupBBSR.competo.Models.EventPalModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.FragmentFindMainBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FindMainFragment extends Fragment {
    private FragmentFindMainBinding binding;

    private FindEventAdapter findEventAdapter;
    private FindUserAdapter findUserAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestoreDB;

    private NavController navController;

    private Constant constant;
    private CollectionReference eventCollectionReference;
    private CollectionReference userCollectionReference;

    private FirestoreRecyclerOptions<EventModel> eventOptions;
    private FirestoreRecyclerOptions<EventPalModel> userOptions;

    private int toggleFlag = -1;
    public static final String TAG = "find";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFindMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        constant = new Constant();

        eventCollectionReference = firestoreDB.collection(constant.getEvents());
        userCollectionReference = firestoreDB.collection(constant.getUsers());

        binding.buttonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    switch (checkedId) {
                        case R.id.btnfindEvents:
                            toggleFlag = 0;
                            Log.d(TAG, "onButtonChecked: " + toggleFlag);
                            break;

                        case R.id.btnfindUsers:
                            toggleFlag = 1;
                            Log.d(TAG, "onButtonChecked: " + toggleFlag);
                            break;
                    }
                }
            }
        });

        binding.findSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }


    private void search(String newText) {
        if (toggleFlag == 0) {
//            Search Events
            Query eventSearchQuery = eventCollectionReference
                    .orderBy("eventTitle")
                    .whereGreaterThanOrEqualTo("eventTitle", newText);

            eventOptions = new FirestoreRecyclerOptions.Builder<EventModel>()
                    .setQuery(eventSearchQuery, EventModel.class)
                    .build();

            initRecycler();
        } else {
//            Search Users
            Query userSearchQuery = userCollectionReference
                    .orderBy("Name")
                    .whereGreaterThanOrEqualTo("Name", newText);

            userOptions = new FirestoreRecyclerOptions.Builder<EventPalModel>()
                    .setQuery(userSearchQuery, EventPalModel.class)
                    .build();

            initRecycler();
        }
    }

    private void initRecycler() {

        RecyclerView recyclerView = binding.findRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        if (toggleFlag == 0) {
            findEventAdapter = new FindEventAdapter(eventOptions, getContext());
            findEventAdapter.setOnEventClickListener(new FindEventAdapter.onEventClickListener() {
                @Override
                public void onEventClick(DocumentSnapshot snapshot) {
                    EventModel model = snapshot.toObject(EventModel.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("eventDetails", model);
                    bundle.putString("from", "find");
                    navController.navigate(R.id.action_findMainFragment_to_eventDetailsFragment3, bundle);
                }
            });

            recyclerView.setAdapter(findEventAdapter);
            findEventAdapter.startListening();
        } else {
            findUserAdapter = new FindUserAdapter(userOptions, getContext());
            findUserAdapter.setOnUserClickListener(new FindUserAdapter.onUserClickListener() {
                @Override
                public void onUserClick(DocumentSnapshot snapshot) {
                    EventPalModel model = snapshot.toObject(EventPalModel.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userDetails", model);
                    bundle.putString("from", "find");
                    navController.navigate(R.id.action_findMainFragment_to_eventPalFragment2, bundle);
                }
            });

            recyclerView.setAdapter(findUserAdapter);
            findUserAdapter.startListening();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (findEventAdapter != null) {
            findEventAdapter.startListening();
        }

        if (findUserAdapter != null) {
            findUserAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (findEventAdapter != null) {
            findEventAdapter.stopListening();
        }

        if (findUserAdapter != null) {
            findUserAdapter.stopListening();
        }
    }
}