package com.StartupBBSR.competo.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.StartupBBSR.competo.Adapters.MessageRequestAdapter;
import com.StartupBBSR.competo.Models.MessageModel;
import com.StartupBBSR.competo.Models.RequestModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.FragmentMessageRequestBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MessageRequestFragment extends Fragment {

    private FragmentMessageRequestBinding binding;

    private Constant constant;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestoreDB;

    private CollectionReference collectionReference;

    private MessageRequestAdapter adapter;
    private FirestoreRecyclerOptions<RequestModel> options;

    private NavController navController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.navigate(R.id.action_messageRequestFragment_to_inboxMainFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessageRequestBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();

        constant = new Constant();

        collectionReference = firestoreDB.collection(constant.getRequests())
                .document(firebaseAuth.getUid())
                .collection(constant.getRequests());

        binding.requestLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_messageRequestFragment_to_inboxMainFragment);
            }
        });

        initData();
        initRecyclerView();

        return view;
    }

    private void initData() {
        Query query = collectionReference.orderBy("timestamp");

        options = new FirestoreRecyclerOptions.Builder<RequestModel>()
                .setQuery(query, RequestModel.class)
                .build();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.requestRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        adapter = new MessageRequestAdapter(options, getContext());
        adapter.setOnButtonClickListener(new MessageRequestAdapter.onButtonClickListener() {
            @Override
            public void onAcceptButtonClick(DocumentSnapshot snapshot) {

                RequestModel requestModel = snapshot.toObject(RequestModel.class);
                String message = requestModel.getRequestMessage();
                String senderID = requestModel.getSenderID();
                Long timestamp = requestModel.getTimestamp();

                MessageModel messageModel = new MessageModel(senderID, message, timestamp);

                String senderRoom = senderID + firebaseAuth.getUid();
                String receiverRoom = firebaseAuth.getUid() + senderID;

                firestoreDB.collection(constant.getChats())
                        .document(senderRoom)
                        .collection(constant.getMessages())
                        .add(messageModel)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                firestoreDB.collection(constant.getChats())
                                        .document(receiverRoom)
                                        .collection(constant.getMessages())
                                        .add(messageModel)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {

//                                                Update Connection

                                                CollectionReference connectionRef = firestoreDB.collection(constant.getChatConnections());
                                                connectionRef.document(firebaseAuth.getUid())
                                                        .update("Connections", FieldValue.arrayUnion(senderID))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                connectionRef.document(senderID)
                                                                        .update("Connections", FieldValue.arrayUnion(firebaseAuth.getUid()))
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {

                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }
                                        });
                            }
                        }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        snapshot.getReference().delete();
                        Toast.makeText(getContext(), "Request moved to inbox", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onRejectButtonClick(DocumentSnapshot snapshot) {
                snapshot.getReference().delete();
                Toast.makeText(getContext(), "Request Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
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
            adapter.startListening();
        }
    }
}