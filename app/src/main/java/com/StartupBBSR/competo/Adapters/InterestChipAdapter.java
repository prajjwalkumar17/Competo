package com.StartupBBSR.competo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.StartupBBSR.competo.databinding.InterestChipItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InterestChipAdapter extends RecyclerView.Adapter<InterestChipAdapter.ViewHolder> {

    private static InterestChipItemBinding binding;
    private List<String> localDataSet;

    public InterestChipAdapter(List<String> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = InterestChipItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextView().setText(localDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        if (localDataSet == null)
            return 0;
        else
            return localDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = binding.itemTextView;
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
