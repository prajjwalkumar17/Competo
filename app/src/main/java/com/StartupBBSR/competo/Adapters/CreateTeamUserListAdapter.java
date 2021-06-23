package com.StartupBBSR.competo.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.StartupBBSR.competo.Models.EventPalModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.databinding.CreateTeamUserItemBinding;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CreateTeamUserListAdapter extends FirestoreRecyclerAdapter<EventPalModel, CreateTeamUserListAdapter.ViewHolder> {

    private CreateTeamUserItemBinding binding;
    private Context context;

    public CreateTeamUserListAdapter(@NonNull FirestoreRecyclerOptions<EventPalModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull EventPalModel model) {
        holder.name.setText(model.getName());

        if (model.getPhoto() != null)
            Glide.with(context).load(Uri.parse(model.getPhoto())).into(holder.image);
        else
            Glide.with(context).load(R.drawable.ic_baseline_person_24).into(holder.image);

        model.setSelected(false);
        holder.checkBox.setChecked(model.isSelected());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setSelected(!model.isSelected());
                holder.checkBox.setChecked(model.isSelected());
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = CreateTeamUserItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        CheckBox checkBox;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = binding.userImage;
            name = binding.userName;
            checkBox = binding.userCheckBox;
            cardView = binding.cardView;
        }
    }

    public ArrayList<EventPalModel> getSelected() {
        ArrayList<EventPalModel> selected = new ArrayList<>();
        for (int i = 0; i < getSnapshots().size(); i++) {
            if (getItem(i).isSelected()) {
                selected.add(getSnapshots().get(i));
            }
        }
        return selected;
    }
}
