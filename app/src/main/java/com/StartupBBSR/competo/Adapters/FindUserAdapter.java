package com.StartupBBSR.competo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.StartupBBSR.competo.Models.EventPalModel;
import com.StartupBBSR.competo.databinding.FindUserItemBinding;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FindUserAdapter extends FirestoreRecyclerAdapter<EventPalModel, FindUserAdapter.ViewHolder> {

    private FindUserItemBinding binding;
    private Context context;

    private onUserClickListener listener;

    public interface onUserClickListener {
        void onUserClick(DocumentSnapshot snapshot);
    }

    public void setOnUserClickListener(onUserClickListener listener) {
        this.listener = listener;
    }

    public FindUserAdapter(@NonNull FirestoreRecyclerOptions<EventPalModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull EventPalModel model) {
        holder.name.setText(model.getName());
        Glide.with(context).load(model.getPhoto()).into(holder.image);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = FindUserItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = binding.tvName;
            image = binding.ivImage;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                            DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                            listener.onUserClick(snapshot);
                        }
                    }
                }
            });
        }
    }
}
