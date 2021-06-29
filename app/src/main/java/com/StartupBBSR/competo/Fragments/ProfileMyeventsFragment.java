package com.StartupBBSR.competo.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.StartupBBSR.competo.Adapters.MyEventAdapter;
import com.StartupBBSR.competo.Models.EventModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.FragmentProfileMyeventsBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileMyeventsFragment extends Fragment {

    private FragmentProfileMyeventsBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestoreDB;
    private String userID;

    private EventModel eventModel;
    private Constant constant;

    private CollectionReference collectionReference;
    private DocumentReference myEventRef;

    private MyEventAdapter adapter;
    private FirestoreRecyclerOptions<EventModel> options;

    private NavController navController;

    List<String> myEvents = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileMyeventsBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getUid();

        constant = new Constant();
        eventModel = new EventModel();

        collectionReference = firestoreDB.collection(constant.getEvents());
        myEventRef = firestoreDB.collection(constant.getUsers())
                .document(userID);

        myEventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    myEvents = (List<String>) documentSnapshot.get(constant.getUserMyEventField());
                    if (myEvents != null && myEvents.size() != 0) {
                        initData();
                    }
                }
            }
        });

        return view;
    }

    private void initData() {
        Query query = collectionReference.whereIn(constant.getEventIDField(), myEvents);
        options = new FirestoreRecyclerOptions.Builder<EventModel>()
                .setQuery(query, EventModel.class)
                .build();
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.myEventRecyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);

        adapter = new MyEventAdapter(options, getContext());

        adapter.setOnClickListener(new MyEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot) {
                EventModel eventModel = snapshot.toObject(EventModel.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("eventDetails", eventModel);
                bundle.putString("from", "myevent");
                navController.navigate(R.id.action_profileMainFragment_to_eventDetailsFragment2, bundle);
            }
        });

        adapter.startListening();
        recyclerView.setAdapter(adapter);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(getParentFragment().getView());
    }
}