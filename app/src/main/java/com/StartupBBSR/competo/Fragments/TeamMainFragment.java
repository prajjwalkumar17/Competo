package com.StartupBBSR.competo.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.StartupBBSR.competo.Adapters.TeamListAdapter;
import com.StartupBBSR.competo.Models.TeamModel;
import com.StartupBBSR.competo.Models.UserModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.FragmentTeamMainBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class TeamMainFragment extends Fragment {

    private FragmentTeamMainBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestoreDB;
    private String userID;

    private UserModel userModel;
    private Constant constant;

    private NavController navController;

    private CollectionReference collectionReference;
    private DocumentReference teamRef;
    private List<String> teamList = new ArrayList<>();
    private FirestoreRecyclerOptions<TeamModel> options;
    private TeamListAdapter adapter;
    private Query query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTeamMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getUid();

        constant = new Constant();
        userModel = new UserModel();

        collectionReference = firestoreDB.collection(constant.getTeams());
        teamRef = firestoreDB.collection(constant.getChatConnections()).document(userID);

        binding.btnCreateTeamFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_teamMainFragment_to_createTeamFragment);
            }
        });

        teamRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();

                    teamList = (List<String>) snapshot.get(constant.getTeamConnections());
//                    Log.d("team", "onComplete: " + teamList);
                    if (teamList != null && teamList.size() != 0) {
                        initData();
                    }
                }
            }
        });

        binding.teamRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                teamRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();

                            teamList = (List<String>) snapshot.get(constant.getTeamConnections());
//                    Log.d("team", "onComplete: " + teamList);
                            binding.teamRefreshLayout.setRefreshing(false);
                            if (teamList != null && teamList.size() != 0) {
                                initData();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getContext(), "Could not refresh", Toast.LENGTH_SHORT).show();
                        binding.teamRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        return view;
    }

    private void initData() {
        query = collectionReference.whereIn(constant.getTeamIDField(), teamList);
        options = new FirestoreRecyclerOptions.Builder<TeamModel>()
                .setQuery(query, TeamModel.class)
                .build();

        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.teamRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        adapter = new TeamListAdapter(options, getContext());
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        binding.teamRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && binding.btnCreateTeamFab.getVisibility() == View.VISIBLE) {
                    binding.btnCreateTeamFab.hide();
                } else if (dy < 0 && binding.btnCreateTeamFab.getVisibility() != View.VISIBLE) {
                    binding.btnCreateTeamFab.show();
                }
            }
        });
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