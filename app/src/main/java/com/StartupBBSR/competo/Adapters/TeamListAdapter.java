package com.StartupBBSR.competo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.StartupBBSR.competo.Activity.TeamChatDetailActivity;
import com.StartupBBSR.competo.Models.TeamModel;
import com.StartupBBSR.competo.databinding.TeamItemBinding;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TeamListAdapter extends FirestoreRecyclerAdapter<TeamModel, TeamListAdapter.ViewHolder> {
    private TeamItemBinding binding;
    private Context context;

    public TeamListAdapter(@NonNull FirestoreRecyclerOptions<TeamModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull TeamListAdapter.ViewHolder holder, int position, @NonNull TeamModel model) {
        holder.teamName.setText(model.getTeamName());
        Glide.with(context).load(Uri.parse(model.getTeamImage())).into(holder.teamImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TeamChatDetailActivity.class);
                intent.putExtra("teamName", model.getTeamName());
                intent.putExtra("teamImage", model.getTeamImage());
                intent.putExtra("teamID", model.getTeamID());
                intent.putExtra("teamCreatorID", model.getCreatorID());
                ArrayList<String> teamMembers = (ArrayList<String>) model.getTeamMembers();
                intent.putStringArrayListExtra("teamMembers", teamMembers);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = TeamItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView teamImage;
        TextView teamName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            teamImage = binding.teamImage;
            teamName = binding.teamName;
        }
    }
}
