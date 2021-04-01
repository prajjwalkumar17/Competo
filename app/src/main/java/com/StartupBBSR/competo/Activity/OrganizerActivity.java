package com.StartupBBSR.competo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.databinding.ActivityOrganizerBinding;
import com.google.firebase.auth.FirebaseAuth;

public class OrganizerActivity extends AppCompatActivity {

    private ActivityOrganizerBinding activityOrganizerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOrganizerBinding = ActivityOrganizerBinding.inflate(getLayoutInflater());
        setContentView(activityOrganizerBinding.getRoot());

        activityOrganizerBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }
}