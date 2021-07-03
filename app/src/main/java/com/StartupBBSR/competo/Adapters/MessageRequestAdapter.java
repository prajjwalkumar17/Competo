package com.StartupBBSR.competo.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.StartupBBSR.competo.Models.RequestModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.RequestItemLayoutBinding;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageRequestAdapter extends FirestoreRecyclerAdapter<RequestModel, MessageRequestAdapter.ViewHolder> {

    private RequestItemLayoutBinding binding;
    private Context context;

    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    Constant constant = new Constant();


    private onButtonClickListener listener;

    public interface onButtonClickListener {
        void onAcceptButtonClick(DocumentSnapshot snapshot);

        void onRejectButtonClick(DocumentSnapshot snapshot);
    }

    public void setOnButtonClickListener(onButtonClickListener listener) {
        this.listener = listener;
    }

    public MessageRequestAdapter(@NonNull FirestoreRecyclerOptions<RequestModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageRequestAdapter.ViewHolder holder, int position, @NonNull RequestModel model) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy | KK:mm a");
        String senderID = model.getSenderID();
        DocumentReference documentReference = firestoreDB.collection(constant.getUsers())
                .document(senderID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        holder.senderName.setText(snapshot.getString(constant.getUserNameField()));
                        if (snapshot.getString(constant.getUserPhotoField()) != null)
                            Glide.with(context).load(Uri.parse(snapshot.getString(constant.getUserPhotoField()))).into(holder.senderImage);
                        else
                            Glide.with(context).load(R.drawable.ic_baseline_person_24).into(holder.senderImage);
                    }
                }
            }
        });
        holder.senderMessage.setText(model.getRequestMessage());
        holder.time.setText(simpleDateFormat.format(
                new Date(Long.parseLong(model.getTimestamp().toString()))
        ));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RequestItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding.getRoot());
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView senderImage;
        private TextView senderName, senderMessage, time;
        private Button acceptRequestButton, rejectRequestButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            senderImage = binding.ivSenderImage;
            senderMessage = binding.tvSenderMessage;
            senderName = binding.tvSenderName;
            time = binding.tvRequestTime;
            acceptRequestButton = binding.btnAcceptRequest;
            rejectRequestButton = binding.btnRejectRequest;


            acceptRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onAcceptButtonClick(getSnapshots().getSnapshot(getAdapterPosition()));
                }
            });

            rejectRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRejectButtonClick(getSnapshots().getSnapshot(getAdapterPosition()));
                }
            });

        }
    }
}
