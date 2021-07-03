package com.StartupBBSR.competo.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.StartupBBSR.competo.Adapters.ChatAdapter;
import com.StartupBBSR.competo.Models.MessageModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.ActivityChatDetailBinding;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatDetailActivity extends AppCompatActivity {

    private ActivityChatDetailBinding binding;

    private Constant constant;
    private MessageModel messageModel;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestoreDB;
    private FirestoreRecyclerOptions<MessageModel> options;
    private ChatAdapter chatAdapter;

    private String senderID, receiverID, receiverName, receiverPhoto;

    private String senderRoom, receiverRoom;
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();

        constant = new Constant();


        senderID = firebaseAuth.getUid();
        receiverID = getIntent().getStringExtra("receiverID");
        receiverName = getIntent().getStringExtra("receiverName");
        receiverPhoto = getIntent().getStringExtra("receiverPhoto");

        binding.chatUserName.setText(receiverName);

        if (receiverPhoto != null)
            Glide.with(ChatDetailActivity.this)
                    .load(Uri.parse(receiverPhoto))
                    .into(binding.chatUserImage);
        else
            Glide.with(ChatDetailActivity.this)
                    .load(R.drawable.ic_baseline_person_24)
                    .into(binding.chatUserImage);

        senderRoom = senderID + receiverID;
        receiverRoom = receiverID + senderID;

        collectionReference = firestoreDB.collection(constant.getChats())
                .document(senderRoom)
                .collection(constant.getMessages());

        binding.btnSendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.etMessage.getText().toString().equals("")) {
                    String message = binding.etMessage.getText().toString().trim();
                    messageModel = new MessageModel(senderID, message);
                    messageModel.setTimestamp(new Date().getTime());

                    binding.etMessage.setText("");

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

                                                }
                                            });
                                }
                            });
                }
            }
        });


        initData();
        initRecyclerview();

    }

    private void initData() {
        Query query = collectionReference.orderBy("timestamp");
        options = new FirestoreRecyclerOptions.Builder<MessageModel>()
                .setQuery(query, MessageModel.class)
                .build();
    }

    private void initRecyclerview() {
        RecyclerView chatRecyclerView = binding.chatRecyclerView;
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setHasFixedSize(true);
        chatAdapter = new ChatAdapter(options, this);
        chatRecyclerView.setAdapter(chatAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (chatAdapter != null) {
            chatAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (chatAdapter != null) {
            chatAdapter.stopListening();
        }
    }
}