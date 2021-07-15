package com.StartupBBSR.competo.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.StartupBBSR.competo.Adapters.ChatUserListAdapter;
import com.StartupBBSR.competo.Models.EventPalModel;
import com.StartupBBSR.competo.Models.UserModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.FragmentInboxBinding;
import com.StartupBBSR.competo.databinding.FragmentInboxMainBinding;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class InboxMainFragment extends Fragment {

    private FragmentInboxMainBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestoreDB;
    private String userID;

    private UserModel userModel;
    private Constant constant;

    private DocumentReference documentReference, chatRef;
    private CollectionReference collectionReference;

    private NavController navController;

    private ChatUserListAdapter adapter;
    private FirestoreRecyclerOptions<EventPalModel> options;

    private Query query;
    List<String> chatUsers = new ArrayList<>();
    List<String> demo, chatList;
    public static final String TAG = "chatUser";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInboxMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getUid();

        constant = new Constant();
        userModel = new UserModel();

        documentReference = firestoreDB.collection(constant.getUsers()).document(userID);

        collectionReference = firestoreDB.collection(constant.getUsers());

        chatRef = firestoreDB.collection(constant.getChatConnections())
                .document(userID);

        binding.tvMessageRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_inboxMainFragment_to_messageRequestFragment);
            }
        });

        chatRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    chatUsers = (List<String>) documentSnapshot.get("Connections");
                    Log.d(TAG, "onComplete: " + chatUsers);
                    if (chatUsers != null){
                        initData();
                    }
                }
            }
        });

        getRequestCounts();


        binding.inboxRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRequestCounts();
                chatRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            chatUsers = (List<String>) documentSnapshot.get("Connections");
                            Log.d(TAG, "onComplete: " + chatUsers);
                            binding.inboxRefreshLayout.setRefreshing(false);
                            if (chatUsers != null){
                                initData();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getContext(), "Could not refresh", Toast.LENGTH_SHORT).show();
                        binding.inboxRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        return view;
    }

    private void getRequestCounts() {
        firestoreDB.collection(constant.getRequests())
                .document(userID)
                .collection(constant.getRequests())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;

                            for (DocumentSnapshot document : task.getResult()) {
                                count++;
                            }
                            if (count!=0){
                                binding.tvMessageRequest.setText(count + " Message Request(s)");
                                binding.tvMessageRequest.setVisibility(View.VISIBLE);
                            } else {
                                binding.tvMessageRequest.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    private void initData() {
//        Query query = collectionReference.orderBy(constants.getUserIDField()).whereNotEqualTo(constants.getUserIDField(), userID);

        chatList = chatUsers;

        query = collectionReference.whereIn(constant.getUserIdField(), chatList);

        Log.d(TAG, "initData: " + chatList + "\n" + query);

        options = new FirestoreRecyclerOptions.Builder<EventPalModel>()
                .setQuery(query, EventPalModel.class)
                .build();

        initRecyclerView();

    }

    private void initRecyclerView() {

        RecyclerView recyclerView = binding.chatListRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        adapter = new ChatUserListAdapter(options, getContext());
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
        navController = Navigation.findNavController(view);
    }
}