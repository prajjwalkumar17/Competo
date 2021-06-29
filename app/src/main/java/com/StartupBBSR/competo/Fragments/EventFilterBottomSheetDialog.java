package com.StartupBBSR.competo.Fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.databinding.EventFilterBottomsheetLayoutBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EventFilterBottomSheetDialog extends BottomSheetDialogFragment {

    private EventFilterBottomsheetLayoutBinding binding;
    private ChipGroup chipGroup;
    private String[] selectedFilters;

    private Context mcontext;

    private BottomSheetListener listener;

    public interface BottomSheetListener {
        void onApplyButtonClicked(List<String> selectedFilters);
    }

    public EventFilterBottomSheetDialog(Context context) {
        this.mcontext = context;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (BottomSheetListener) getTargetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = EventFilterBottomsheetLayoutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        chipGroup = binding.filterEventChipGroup;

        binding.btnApplyEventFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSelectedFilters();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] filters = getResources().getStringArray(R.array.FilterChips);

        for (String filter : filters) {
            Chip chip = new Chip(getContext());
            chip.setText(filter);
            chip.setTextColor(getResources().getColor(R.color.white));
            chip.setCheckable(true);
            chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.chip_background_color)));
            chipGroup.addView(chip);
        }
    }

    private void getSelectedFilters() {
        int count = 0;

        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                count++;
            }
        }

        selectedFilters = new String[count];
        int index = 0;
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                selectedFilters[index++] = chip.getText().toString();
            }
        }

        List<String> selectedFilterList = Arrays.asList(selectedFilters);
        listener.onApplyButtonClicked(selectedFilterList);
        dismiss();

    }

}
