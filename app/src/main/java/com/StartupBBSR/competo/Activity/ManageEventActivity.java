package com.StartupBBSR.competo.Activity;

import android.os.Bundle;
import android.widget.Toast;

import com.StartupBBSR.competo.Adapters.TagRecyclerAdapter;
import com.StartupBBSR.competo.databinding.ActivityManageEventBinding;
import com.google.android.material.chip.ChipGroup;

import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ManageEventActivity extends AppCompatActivity {

    private ActivityManageEventBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}