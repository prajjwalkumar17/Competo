package com.StartupBBSR.competo.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.StartupBBSR.competo.Adapters.CreateTeamUserListAdapter;
import com.StartupBBSR.competo.Adapters.TeamChatAdapter;
import com.StartupBBSR.competo.Models.EventPalModel;
import com.StartupBBSR.competo.Models.TeamModel;
import com.StartupBBSR.competo.Models.UserModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.FragmentCreateTeamBinding;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CreateTeamFragment extends Fragment {

    private FragmentCreateTeamBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestoreDB;
    private String userID;
    private Constant constant;
    private TeamModel teamModel;

    private Uri imageUri, imageDownloadUri;
    private UploadTask uploadTask;

    private NavController navController;

    private CollectionReference collectionReference;
    private CreateTeamUserListAdapter adapter;
    private FirestoreRecyclerOptions<EventPalModel> options;

    private ArrayList<String> selectedUserIds;

    public static final String TAG = "team";
    private static final int REQUEST_PHOTO_CODE = 123;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.navigate(R.id.action_createTeamFragment_to_teamMainFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateTeamBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getUid();

        constant = new Constant();

        collectionReference = firestoreDB.collection(constant.getUsers());

        initData();
        initRecyclerView();

        binding.ivTeamImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        binding.btnCreateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.etTeamName.getText().toString().equals("")) {
                    if (adapter.getSelected().size() >= 2) {
                        ArrayList<EventPalModel> selectedUsers = adapter.getSelected();
                        selectedUserIds = new ArrayList<>();
                        for (EventPalModel model: selectedUsers) {
                            selectedUserIds.add(model.getUserID());
                        }
//                        To add the creator to the team as well
                        selectedUserIds.add(userID);
                        uploadPhoto();
                    } else {
                        Toast.makeText(getContext(), "Select more than 1 member", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Name cannot be blank", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    private void initData() {
        Query query = collectionReference.orderBy(constant.getUserIdField()).whereNotEqualTo(constant.getUserIdField(), userID);
        options = new FirestoreRecyclerOptions.Builder<EventPalModel>()
                .setQuery(query, EventPalModel.class)
                .build();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.createTeamRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        adapter = new CreateTeamUserListAdapter(options, getContext());
        recyclerView.setAdapter(adapter);
    }

    private void uploadPhoto() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnCreateTeam.setVisibility(View.GONE);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(constant.getTeamPhotos() + "/" + firestoreDB.collection(constant.getTeams()).get());

        if (imageUri != null) {
            //            Compressing The Image
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

//                Higher the number, higher the quality
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                byte[] data = baos.toByteArray();

                uploadTask = storageReference.putBytes(data);

            } catch (IOException e) {
                e.printStackTrace();
            }

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Photo Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error Uploading Image:\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnCreateTeam.setVisibility(View.VISIBLE);
                }
            });

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        imageDownloadUri = task.getResult();
                        createTeam();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE);
            binding.btnCreateTeam.setVisibility(View.VISIBLE);
        }
    }

    private void createTeam() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnCreateTeam.setVisibility(View.GONE);

        CollectionReference collectionReference = firestoreDB.collection(constant.getTeams());

        String teamName = binding.etTeamName.getText().toString().trim();
        String image = imageDownloadUri.toString();
        String teamID = collectionReference.document().getId();
        String creatorID = userID;
        ArrayList<String> teamMembers = selectedUserIds;

        teamModel = new TeamModel(teamName, image, teamID, creatorID, teamMembers);

        collectionReference.document(teamID).set(teamModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Creating Team", Toast.LENGTH_SHORT).show();

//                        Updating Team Connection of every user with team ID
                        CollectionReference teamConnectionRef = firestoreDB.collection(constant.getChatConnections());
                        for (String id: teamMembers) {
                            teamConnectionRef.document(id).update(constant.getTeamConnections(), FieldValue.arrayUnion(teamID));
                        }
                        binding.progressBar.setVisibility(View.GONE);
                        binding.btnCreateTeam.setVisibility(View.VISIBLE);
                        navController.navigate(R.id.action_createTeamFragment_to_teamMainFragment);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error:\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
                binding.btnCreateTeam.setVisibility(View.VISIBLE);
            }
        });
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_PHOTO_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHOTO_CODE && resultCode == getActivity().RESULT_OK) {
            if (data == null) {
                Toast.makeText(getContext(), "Error Fetching Image", Toast.LENGTH_SHORT).show();
                return;
            }
            imageUri = data.getData();
            loadUsingGlide(imageUri.toString());
        }
    }

    private void loadUsingGlide(String imgurl) {
        Glide.with(this).
                load(imgurl).into(binding.ivTeamImage);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }
}