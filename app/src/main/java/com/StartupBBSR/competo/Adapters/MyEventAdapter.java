package com.StartupBBSR.competo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.StartupBBSR.competo.Models.EventModel;
import com.StartupBBSR.competo.databinding.MyEventItemBinding;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MyEventAdapter extends FirestoreRecyclerAdapter<EventModel, MyEventAdapter.ViewHolder> {

    private MyEventItemBinding binding;
    private Context context;

    public MyEventAdapter(@NonNull FirestoreRecyclerOptions<EventModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyEventAdapter.ViewHolder holder, int position, @NonNull EventModel model) {
        String date = model.getEventDate();
        holder.day.setText(date.substring(0,2));
        holder.month.setText(date.substring(3,6));
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
        }
    }
}
