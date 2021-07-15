package com.StartupBBSR.competo.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.StartupBBSR.competo.Activity.ChatDetailActivity;
import com.StartupBBSR.competo.Adapters.EventPalUserAdapter;
import com.StartupBBSR.competo.Models.EventPalModel;
import com.StartupBBSR.competo.Models.RequestModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.AlertlayoutrequestBinding;
import com.StartupBBSR.competo.databinding.FragmentEventPalBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class EventPalFragment extends Fragment {

    public static final String TAG = "sheet";

    private FragmentEventPalBinding binding;

    private FirebaseFirestore firestoreDB;
    private FirebaseAuth firebaseAuth;
    private String userID;

    private Constant constant;

    private EventPalUserAdapter adapter;

    private CollectionReference collectionReference;
    private FirestoreRecyclerOptions<EventPalModel> options;

    private EventPalModel eventPalModel;
    private Query query;

    private NavController navController;
    private int flag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            eventPalModel = (EventPalModel) getArguments().getSerializable("userDetails");
            flag = 1;
        } else {
            flag = 0;
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (flag == 1) {
//                    Came from find fragment, go back there
                    navController.navigate(R.id.action_eventPalFragment2_to_findMainFragment);
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventPalBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        firestoreDB = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getUid();

        constant = new Constant();

        collectionReference = firestoreDB.collection(constant.getUsers());

        if (flag == 0) {
            query = collectionReference.orderBy(constant.getUserIdField())
                    .whereNotEqualTo(constant.getUserIdField(), userID);
        } else {
            query = collectionReference.whereEqualTo(constant.getUserIdField(), eventPalModel.getUserID());
        }


        options = new FirestoreRecyclerOptions.Builder<EventPalModel>()
                .setQuery(query, EventPalModel.class)
                .build();

        SnapHelper snapHelper = new LinearSnapHelper();

        RecyclerView recyclerView = binding.eventPalRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        snapHelper.attachToRecyclerView(recyclerView);


        adapter = new EventPalUserAdapter(getContext(), options);
        adapter.setOnItemClickListener(new EventPalUserAdapter.OnItemClickListener() {
            @Override
            public void onButtonClick(DocumentSnapshot snapshot) {
                EventPalModel model = snapshot.toObject(EventPalModel.class);

                String senderRoom = userID + model.getUserID();
                String receiverRoom = model.getUserID() + userID;

                firestoreDB.collection(constant.getChats())
                        .document(senderRoom)
                        .collection(constant.getMessages())
                        .limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
//                                        Create New Request
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        AlertlayoutrequestBinding alertlayoutrequestBinding = AlertlayoutrequestBinding.inflate(getLayoutInflater());
                                        View alertView = alertlayoutrequestBinding.getRoot();
                                        builder.setView(alertView);
                                        builder.setTitle("Connect with " + model.getName())
                                                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        if (!alertlayoutrequestBinding.input.getText().toString().isEmpty()) {
                                                            String requestMesssage = alertlayoutrequestBinding.input.getText().toString().trim();
                                                            RequestModel requestModel = new RequestModel(userID, requestMesssage, new Date().getTime());

                                                            firestoreDB.collection(constant.getRequests())
                                                                    .document(model.getUserID())
                                                                    .collection(constant.getRequests())
                                                                    .document(userID)
                                                                    .set(requestModel)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Toast.makeText(getContext(), "Request Sent", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });

                                                        }
                                                    }
                                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).show();
                                    } else {
//                                    Chat already present
                                        Intent intent = new Intent(getContext(), ChatDetailActivity.class);
                                        intent.putExtra("receiverID", model.getUserID());
                                        intent.putExtra("receiverName", model.getName());
                                        intent.putExtra("receiverPhoto", model.getPhoto());
                                        startActivity(intent);
                                    }
                                }
                            }
                        });
            }

            @Override
            public void onBottomSheetToggleClick(View itemView, int position) {

                View bottomSheet = itemView.findViewById(R.id.EventPalBottomSheet);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                ImageView btnBottomSheet = itemView.findViewById(R.id.btnBottomSheet);

                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {

                    Log.d(TAG, "onButtonClick: STATE_COLLAPSED");
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    itemView.findViewById(R.id.tvEventPalUserAbout).setVisibility(View.VISIBLE);
                    btnBottomSheet.setImageResource(R.drawable.down_arrow);

                } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

                    Log.d(TAG, "onButtonClick: STATE_EXPANDED");
                    itemView.findViewById(R.id.tvEventPalUserAbout).setVisibility(View.GONE);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    btnBottomSheet.setImageResource(R.drawable.ic_upperarrow);

                }
            }

        });
        recyclerView.setAdapter(adapter);
//        Query query = collectionReference.orderBy("Name").whereArrayContains("Chips", "Coder");
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (flag == 1)
            navController = Navigation.findNavController(getActivity(), R.id.find_fragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}