package com.StartupBBSR.competo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.StartupBBSR.competo.Models.TeamMessageModel;
import com.StartupBBSR.competo.databinding.ReceiverTeamTextItemBinding;
import com.StartupBBSR.competo.databinding.SenderTeamTextItemBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TeamChatAdapter extends FirestoreRecyclerAdapter<TeamMessageModel, RecyclerView.ViewHolder> {

    private ReceiverTeamTextItemBinding receiverTeamTextItemBinding;
    private SenderTeamTextItemBinding senderTeamTextItemBinding;
    private Context context;

    private int SENDER_VIEW_TYPE = 1, RECEIVER_VIEW_TYPE = 2;

    public TeamChatAdapter(@NonNull FirestoreRecyclerOptions<TeamMessageModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull TeamMessageModel model) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("KK:mm a");
        if (holder.getClass() == SenderViewHolder.class) {
            senderTeamTextItemBinding.tvSenderText.setText(model.getMessage());
            senderTeamTextItemBinding.tvSenderTextTime.setText(simpleDateFormat.format(new Date(Long.parseLong(model.getTimestamp().toString()))));
        } else {
            receiverTeamTextItemBinding.tvSenderName.setText(model.getSenderName());
            receiverTeamTextItemBinding.tvReceiverText.setText(model.getMessage());
            receiverTeamTextItemBinding.tvReceiverTextTime.setText(simpleDateFormat.format(new Date(Long.parseLong(model.getTimestamp().toString()))));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE) {
            senderTeamTextItemBinding = SenderTeamTextItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new SenderViewHolder(senderTeamTextItemBinding.getRoot());
        } else {
            receiverTeamTextItemBinding = ReceiverTeamTextItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ReceiverViewHolder(receiverTeamTextItemBinding.getRoot());
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
        private TextView receivedMessage, senderName, receivedTime;


        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receivedMessage = receiverTeamTextItemBinding.tvReceiverText;
            receivedTime = receiverTeamTextItemBinding.tvReceiverTextTime;
            senderName = receiverTeamTextItemBinding.tvSenderName;
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        private TextView sentMessage, sentTime;


        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            sentMessage = senderTeamTextItemBinding.tvSenderText;
            sentTime = senderTeamTextItemBinding.tvSenderTextTime;
        }
    }
}
