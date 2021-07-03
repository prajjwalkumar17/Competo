package com.StartupBBSR.competo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.StartupBBSR.competo.Models.EventPalModel;
import com.StartupBBSR.competo.Models.UserModel;
import com.StartupBBSR.competo.databinding.EventPalUserItemBinding;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class EventPalUserAdapter extends FirestoreRecyclerAdapter<EventPalModel, EventPalUserAdapter.ViewHolder> {


    /*In certain cases, you'd want to setup click handlers for views within the RecyclerView but
    define the click logic within the containing Activity or Fragment
    (i.e bubble up events from the adapter). To achieve this, create a custom listener within
    the adapter and then fire the events upwards to an interface implementation defined within
    the parent:*/

    //    Listener Member Variable
    private OnItemClickListener listener;

    public EventPalUserAdapter (FirestoreRecyclerOptions<UserModel> Options) {
        super((FirestoreRecyclerOptions<EventPalModel>) getOptions(Options));
        this.listener= listener;

    }

    private static Object getOptions(FirestoreRecyclerOptions<UserModel> Options) {
        return Options;
    }

    public void updateOptions (FirestoreRecyclerOptions<EventPalModel> Options) {
    }


    //    Listener Interface
    public interface OnItemClickListener {

        void onButtonClick(DocumentSnapshot snapshot);

        void onBottomSheetToggleClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private EventPalUserItemBinding binding;

    private Context context;


    public EventPalUserAdapter(Context context, @NonNull FirestoreRecyclerOptions<EventPalModel> options) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public EventPalUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = EventPalUserItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull EventPalModel model) {
//        holder.setmData(localItems.get(position));
        holder.Name.setText(model.getName());
        holder.About.setText(model.getBio());
        Glide.with(context).load(model.getPhoto()).into(holder.Image);

        holder.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        InterestChipAdapter adapter = new InterestChipAdapter(model.getChips());
        holder.recyclerView.setAdapter(adapter);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView Name, About;
        private ImageView Image;
        private RecyclerView recyclerView;
        private Button btnSendMessageRequestEventPal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = binding.tvEventPalUserName;
            About = binding.tvEventPalUserAbout;
            Image = binding.ivEventPalUserImage;
            recyclerView = binding.eventPalUserSkillRecyclerView;
            btnSendMessageRequestEventPal = binding.btnSendMessage;


            binding.btnBottomSheet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onBottomSheetToggleClick(itemView, position);
                        }
                    }
                }
            });

            btnSendMessageRequestEventPal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
                            listener.onButtonClick(documentSnapshot);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
        }
    }
}
