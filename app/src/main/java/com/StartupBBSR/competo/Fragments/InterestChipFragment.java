package com.StartupBBSR.competo.Fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.StartupBBSR.competo.Models.UserModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.FragmentInterestChipBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class InterestChipFragment extends Fragment {

    private FragmentInterestChipBinding binding;

    public static final String TAG = "chiptest";

    ChipGroup chipGroup;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseDB;
    private String userId;

    private Constant constant;
    private UserModel userModel;

    private NavController navController;


    String[] selectedChips;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInterestChipBinding.inflate(getLayoutInflater(), container, false);
        chipGroup = binding.idChipGroup;
        View view = binding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getUid();

        constant = new Constant();

//        Get data from Main Activity via get Intent
        userModel = (UserModel) getActivity().getIntent().getSerializableExtra(constant.getUserModelObject());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        String[] filters = {"Web Development", "Social Sciences", "Marketing",
                "App Development", "UI/UX", "Coder", "Hackathon", "HR",
                "Workshop", "Content Writing", "Video Editor", "Photo Editor"};

        for (String filter : filters) {
            Chip chip = new Chip(getContext());
            chip.setText(filter);
            chip.setTextColor(getResources().getColor(R.color.white));
            chip.setCheckable(true);
            chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.chip_background_color)));
            chipGroup.addView(chip);
        }

        binding.btnFilterApplyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCheckedChips();
            }
        });
    }

    private void getCheckedChips() {
        int count = 0;

        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()){
                count++;
            }
        }

        if (count <= 10 && count >= 3){
            selectedChips = new String[count];
            int index = 0;
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                if (chip.isChecked()){
                    selectedChips[index++] = chip.getText().toString();
                }
            }
            saveData();
        } else {
            if (count > 10)
                Toast.makeText(getActivity(), "Cannot select more than 10 items", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), "Select at least 3 items", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveData() {
        binding.btnFilterApplyChanges.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        DocumentReference documentReference = firebaseDB.collection(constant.getUsers())
                .document(userId);

        Map<String, Object> userInfo = new HashMap<>();

        Log.d(TAG, "saveData: " + Arrays.asList(selectedChips));

        userInfo.put(constant.getUserInterestedChipsField(), Arrays.asList(selectedChips));

        documentReference.update(userInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        binding.btnFilterApplyChanges.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.GONE);
                        navController.navigate(R.id.action_interestChipFragment_to_profileMainFragment);
                    }
                });
    }
}