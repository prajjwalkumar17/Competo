package com.StartupBBSR.competo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.StartupBBSR.competo.Models.EventModel;
import com.StartupBBSR.competo.databinding.FindEventItemBinding;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FindEventAdapter extends FirestoreRecyclerAdapter<EventModel, FindEventAdapter.ViewHolder> {

    private FindEventItemBinding binding;
    private Context context;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.US);
    private SimpleDateFormat timeFormat = new SimpleDateFormat("KK:mm a", Locale.US);

    public onEventClickListener listener;

    public interface onEventClickListener {
        void onEventClick(DocumentSnapshot snapshot);
    }

    public void setOnEventClickListener(onEventClickListener listener) {
        this.listener = listener;
    }

    public FindEventAdapter(@NonNull FirestoreRecyclerOptions<EventModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FindEventAdapter.ViewHolder holder, int position, @NonNull EventModel model) {
        holder.title.setText(model.getEventTitle());
        holder.date.setText(dateFormat.format(new Date(Long.parseLong(model.getEventDateStamp().toString()))));
        holder.time.setText(timeFormat.format(new Date(Long.parseLong(model.getEventTimeStamp().toString()))));
        Glide.with(context).load(model.getEventPoster()).into(holder.image);
    }

    @NonNull
    @Override
    public FindEventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = FindEventItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, time;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = binding.tvTitle;
            date = binding.tvDate;
            time = binding.tvTime;
            image = binding.ivImage;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                            DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                            listener.onEventClick(snapshot);
                        }
                    }
                }
            });
        }
    }
}
