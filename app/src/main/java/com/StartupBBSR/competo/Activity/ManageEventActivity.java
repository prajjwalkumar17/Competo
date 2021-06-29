package com.StartupBBSR.competo.Activity;

import android.os.Bundle;
import com.StartupBBSR.competo.databinding.ActivityManageEventBinding;

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