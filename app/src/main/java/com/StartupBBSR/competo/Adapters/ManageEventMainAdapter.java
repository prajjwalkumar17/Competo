package com.StartupBBSR.competo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.StartupBBSR.competo.Models.EventModel;
import com.StartupBBSR.competo.databinding.ManageEventMainItemBinding;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ManageEventMainAdapter extends FirestoreRecyclerAdapter<EventModel, ManageEventMainAdapter.ViewHolder> {

    private ManageEventMainItemBinding binding;

    private List<EventModel> localItems;
    private Context context;

    //    For snackbar undo
    private DocumentReference documentReference;
    private EventModel eventModel;

    public OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);

        void onEditEventButtonClick(DocumentSnapshot snapshot);

        void handleDeleteItem(DocumentSnapshot documentSnapshot);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ManageEventMainAdapter(Context context, @NonNull FirestoreRecyclerOptions<EventModel> options) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public ManageEventMainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ManageEventMainItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding.getRoot());
    }

    @Override
    protected void onBindViewHolder(@NonNull ManageEventMainAdapter.ViewHolder holder, int position, @NonNull EventModel model) {

        SimpleDateFormat simpleEventDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.US);
        SimpleDateFormat simpleEventTimeFormat = new SimpleDateFormat("KK:mm a", Locale.US);
        String eventDate = "", eventTime = "";

        if (model.getEventDateStamp() != null)
            eventDate = simpleEventDateFormat.format(new Date(Long.parseLong(model.getEventDateStamp().toString())));

        if (model.getEventTimeStamp() != null)
            eventTime = simpleEventTimeFormat.format(new Date(Long.parseLong(model.getEventTimeStamp().toString())));

        holder.title.setText(model.getEventTitle());
        holder.description.setText(model.getEventDescription());
        holder.venue.setText(model.getEventVenue());
        /*holder.date.setText(model.getEventDate());
        holder.time.setText(model.getEventTime());*/
        holder.date.setText(eventDate);
        holder.time.setText(eventTime);
        Glide.with(context).load(model.getEventPoster()).into(holder.image);
//
        boolean isExpanded = model.isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image, btnEditEvent;
        private TextView title, description, venue, date, time;

        private ConstraintLayout expandableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = binding.ivEventImage;
            title = binding.tvEventTitle;
            description = binding.tvEventDescription;
            venue = binding.tvEventVenue;
            date = binding.tvEventDate;
            time = binding.tvEventTime;
            btnEditEvent = binding.btnEditEvent;
            expandableLayout = binding.expandableLayout;


            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: 5/23/2021 get events
//                    EventModel eventModel = localItems.get(getAdapterPosition());
//                    eventModel.setExpanded(!eventModel.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                    if (listener != null) {
                        int position = getAdapterPosition();
                        EventModel eventModel = getItem(position);
                        eventModel.setExpanded(!eventModel.isExpanded());
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });


            btnEditEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
                            listener.onEditEventButtonClick(snapshot);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }

    public void onSwipeDeleteItem(int position) {

        getSnapshots().getSnapshot(position).getReference().delete();
        Toast.makeText(context, "Event Deleted", Toast.LENGTH_SHORT).show();

//        was made for snackbar but app crashes
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                listener.handleDeleteItem(getSnapshots().getSnapshot(position));
//            }
//        });
    }

    public void notifySetChanged() {
        notifyDataSetChanged();
    }
}
