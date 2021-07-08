package com.StartupBBSR.competo.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.StartupBBSR.competo.Adapters.CreateTeamUserListAdapter;
import com.StartupBBSR.competo.Models.EventPalModel;
import com.StartupBBSR.competo.Models.TeamModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.FragmentCreateTeamBinding;
import com.StartupBBSR.competo.databinding.ViewmembersAlertLayoutBinding;
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
import androidx.appcompat.widget.SearchView;
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

    private ArrayList<String> selectedUserIds, selectedNames;
    private ListView memberNameListView;
    private ArrayAdapter<String> memberNameListAdapter;

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
                    if (imageUri != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "running: ");
                                ArrayList<EventPalModel> selectedUsers = adapter.getSelected();

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (selectedUsers.size() >= 2 && selectedUsers.size() <= 6) {
                                            Log.d(TAG, "run: " + selectedUsers.size());

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    selectedUserIds = new ArrayList<>();
                                                    selectedNames = new ArrayList<>();

                                                    for (EventPalModel model : selectedUsers) {
                                                        selectedUserIds.add(model.getUserID());
                                                        selectedNames.add(model.getName());
                                                    }

                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (!selectedUserIds.contains(userID))
                                                                selectedUserIds.add(userID);

                                                            Log.d(TAG, "run2: " + selectedUserIds);
                                                            showSelectedNames();
                                                        }
                                                    });
                                                }
                                            }).start();
                                        } else if (selectedUsers.size() < 2)
                                            Toast.makeText(getContext(), "Select more than 1 member", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(getContext(), "Cannot add more than 6 members", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).start();
                    } else {
                        Toast.makeText(getContext(), "Select a team image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Give your team a name", Toast.LENGTH_SHORT).show();
                }

            }
        });


        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });

        return view;
    }

    private void showSelectedNames() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Create new team?");
        builder.setMessage("Team Name: " + binding.etTeamName.getText().toString().trim() + "\nSelected Names:");
        ViewmembersAlertLayoutBinding viewmembersAlertLayoutBinding = ViewmembersAlertLayoutBinding.inflate(getLayoutInflater());
        View view = viewmembersAlertLayoutBinding.getRoot();
        memberNameListView = viewmembersAlertLayoutBinding.membersListView;
        memberNameListAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, selectedNames);
        memberNameListView.setAdapter(memberNameListAdapter);
        memberNameListAdapter.notifyDataSetChanged();
        builder.setView(view);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                uploadPhoto();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
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
        adapter.startListening();
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
                        for (String id : teamMembers) {
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


    private void search(String newText) {
        Query teamUserSearchQuery = collectionReference.orderBy(constant.getUserNameField())
                .whereGreaterThanOrEqualTo(constant.getUserNameField(), newText);

        options = new FirestoreRecyclerOptions.Builder<EventPalModel>()
                .setQuery(teamUserSearchQuery, EventPalModel.class)
                .build();

        initRecyclerView();
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


        binding.createTeamRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && binding.btnCreateTeam.getVisibility() == View.VISIBLE) {
                    binding.btnCreateTeam.hide();
                } else if (dy < 0 && binding.btnCreateTeam.getVisibility() != View.VISIBLE) {
                    binding.btnCreateTeam.show();
                }
            }
        });
    }
}