package com.StartupBBSR.competo.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.StartupBBSR.competo.Adapters.EventFragmentAdapter;
import com.StartupBBSR.competo.Models.EventModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.FragmentEventMainBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

public class EventMainFragment extends Fragment {

    private FragmentEventMainBinding binding;

    private EventFragmentAdapter adapter;

    private FirebaseFirestore firestoreDB;

    private NavController navController;

    private Constant constant;
    private CollectionReference collectionReference;
    private FirestoreRecyclerOptions<EventModel> options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firestoreDB = FirebaseFirestore.getInstance();

        constant = new Constant();
        collectionReference = firestoreDB.collection(constant.getEvents());

        initData();
        initRecycler();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    private void initRecycler() {
        binding.eventRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.eventRecyclerView.setHasFixedSize(true);
        adapter = new EventFragmentAdapter(getContext(), options);

        adapter.setOnItemClickListener(new EventFragmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot) {
                EventModel model = snapshot.toObject(EventModel.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("eventDetails", model);
                navController.navigate(R.id.action_eventMainFragment_to_eventDetailsFragment, bundle);

            }
        });

        binding.eventRecyclerView.setAdapter(adapter);
    }

    private void initData() {
        Query query = collectionReference.whereNotEqualTo("eventStatus", "Draft").orderBy("eventStatus");


        //        Query query = collectionReference.orderBy("Name").whereArrayContains("Chips", "Coder");
//        Query query1 = collectionReference.whereArrayContainsAny("eventTags", )

        options = new FirestoreRecyclerOptions.Builder<EventModel>()
                .setQuery(query, EventModel.class)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }

    }
}