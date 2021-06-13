package com.StartupBBSR.competo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.StartupBBSR.competo.Models.MessageModel;
import com.StartupBBSR.competo.databinding.ReceiverTextItemBinding;
import com.StartupBBSR.competo.databinding.SenderTextItemBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends FirestoreRecyclerAdapter<MessageModel, RecyclerView.ViewHolder> {

    private ReceiverTextItemBinding receiverTextItemBinding;
    private SenderTextItemBinding senderTextItemBinding;

    private Context context;

    private int SENDER_VIEW_TYPE = 1, RECEIVER_VIEW_TYPE = 2;


    public ChatAdapter(@NonNull FirestoreRecyclerOptions<MessageModel> options, Context context) {
        super(options);
        this.context = context;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE) {
            senderTextItemBinding = SenderTextItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new SenderViewHolder(senderTextItemBinding.getRoot());
        } else {
            receiverTextItemBinding = ReceiverTextItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ReceiverViewHolder(receiverTextItemBinding.getRoot());
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull MessageModel model) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("KK:mm a");

        if (holder.getClass() == SenderViewHolder.class) {
            senderTextItemBinding.tvSenderText.setText(model.getMessage());
            senderTextItemBinding.tvSenderTextTime.setText(simpleDateFormat
                    .format(new Date(Long.parseLong(model.getTimestamp().toString()))));
        } else {
            receiverTextItemBinding.tvReceiverText.setText(model.getMessage());
            receiverTextItemBinding.tvReceiverTextTime.setText(simpleDateFormat
                    .format(new Date(Long.parseLong(model.getTimestamp().toString()))));
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getSenderID().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        private TextView receivedMessage, receivedTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receivedMessage = receiverTextItemBinding.tvReceiverText;
            receivedTime = receiverTextItemBinding.tvReceiverTextTime;
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        private TextView sentMessage, sentTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            sentMessage = senderTextItemBinding.tvSenderText;
            sentTime = senderTextItemBinding.tvSenderTextTime;
        }
    }
}