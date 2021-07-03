package com.StartupBBSR.competo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.StartupBBSR.competo.Models.OnboardingModel;
import com.StartupBBSR.competo.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder> {

    Context context;
    List<OnboardingModel> onboardingModelList;

    public OnboardingAdapter(Context context, List<OnboardingModel> onboardingModelList) {
        this.context = context;
        this.onboardingModelList = onboardingModelList;
    }

    @NonNull
    @Override
    public OnboardingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.onboarding_layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OnboardingModel onboardingModel = onboardingModelList.get(position);

        holder.title.setText(onboardingModel.getTitle());
        holder.description.setText(onboardingModel.getDescription());
        holder.image.setImageResource(onboardingModel.getImage());
    }

    @Override
    public int getItemCount() {
        return onboardingModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description;
        public ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvOnboardingTitle);
            description = itemView.findViewById(R.id.tvOnboardingDescription);
            image = itemView.findViewById(R.id.ivOnboardingImage);
        }
    }
}
