package com.StartupBBSR.competo.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.StartupBBSR.competo.Models.EventModel;
import com.StartupBBSR.competo.databinding.MyEventItemBinding;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MyEventAdapter extends FirestoreRecyclerAdapter<EventModel, MyEventAdapter.ViewHolder> {

    private MyEventItemBinding binding;
    private Context context;

    private SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.US);
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.US);

    public OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot snapshot);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MyEventAdapter(@NonNull FirestoreRecyclerOptions<EventModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyEventAdapter.ViewHolder holder, int position, @NonNull EventModel model) {
        String day = "", month = "";

        if (model.getEventDateStamp() != null) {
            day = dayFormat.format(new Date(Long.parseLong(model.getEventDateStamp().toString())));
            month = monthFormat.format(new Date(Long.parseLong(model.getEventDateStamp().toString())));
        }

        holder.day.setText(day);
        holder.month.setText(month);
        holder.title.setText(model.getEventTitle());
        Glide.with(context).load(model.getEventPoster()).into(holder.imageView);

    }

    @NonNull
    @Override
    public MyEventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = MyEventItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, day, month;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = binding.tvTitle;
            day = binding.tvDateDay;
            month = binding.tvDateMonth;
            imageView = binding.ivImage;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                            DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                            listener.onItemClick(snapshot);
                        }
                    }
                }
            });
        }
    }
}
