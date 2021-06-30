package com.StartupBBSR.competo.Fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.StartupBBSR.competo.Adapters.ManageEventMainAdapter;
import com.StartupBBSR.competo.Models.EventModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.FragmentManageLiveEventBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class ManageLiveEventFragment extends Fragment {

    private FragmentManageLiveEventBinding binding;

    private NavController navController;

    private ManageEventMainAdapter adapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestoreDB;

    private String organizerID;

    private CollectionReference collectionReference;
    private FirestoreRecyclerOptions<EventModel> options;
    private Constant constant;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageLiveEventBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        organizerID = firebaseAuth.getUid();

        constant = new Constant();
        collectionReference = firestoreDB.collection(constant.getEvents());


        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifySetChanged();
                binding.swipeContainer.setRefreshing(false);
            }
        });

        initData();
        initRecyclerView();

//        Swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.onSwipeDeleteItem(viewHolder.getAdapterPosition());
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20;

                Drawable delete_icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_delete_42);
                ColorDrawable background = new ColorDrawable(Color.RED);


                int iconMargin = (itemView.getHeight() - delete_icon.getIntrinsicHeight()) / 2;
                int iconTop = itemView.getTop() + (itemView.getHeight() - delete_icon.getIntrinsicHeight()) / 2;
                int iconBottom = iconTop + delete_icon.getIntrinsicHeight();

                if (dX > 0) { // Swiping to the right

                    int iconLeft = itemView.getLeft() + iconMargin + delete_icon.getIntrinsicWidth();
                    int iconRight = itemView.getLeft() + iconMargin;
                    delete_icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    background.setBounds(itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                            itemView.getBottom());

                } else if (dX < 0) { // Swiping to the left

                    int iconLeft = itemView.getRight() - iconMargin - delete_icon.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin;
                    delete_icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                            itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else { // view is unSwiped
                    background.setBounds(0, 0, 0, 0);
                }
                background.draw(c);
                delete_icon.draw(c);
            }
        }).attachToRecyclerView(binding.eventLiveRecyclerView);

        return view;
    }

    private void initData() {
        Query query = collectionReference
                .orderBy("eventID")
                .whereEqualTo(constant.getEventOrganizerID(), organizerID);

        options = new FirestoreRecyclerOptions.Builder<EventModel>()
                .setQuery(query, EventModel.class)
                .build();

    }

    private void initRecyclerView() {
        binding.eventLiveRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.eventLiveRecyclerView.setHasFixedSize(true);

        adapter = new ManageEventMainAdapter(getContext(), options);
        adapter.setOnItemClickListener(new ManageEventMainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
//                implemented in adapter
            }

            @Override
            public void onEditEventButtonClick(DocumentSnapshot snapshot) {
                EventModel eventModel = snapshot.toObject(EventModel.class);
//                Toast.makeText(getContext(), "eventModel: " + eventModel.getEventTitle(), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putSerializable("editEvent", eventModel);
                bundle.putSerializable("sourceFragment", "Live");
                navController.navigate(R.id.action_manageEventMainFragment_to_addEventFragment, bundle);
            }

            @Override
            public void handleDeleteItem(DocumentSnapshot documentSnapshot) {
                DocumentReference documentReference = documentSnapshot.getReference();
                EventModel eventModel = documentSnapshot.toObject(EventModel.class);

                Snackbar.make(binding.getRoot(), "Event Deleted", Snackbar.LENGTH_LONG)
                        // TODO: 5/24/2021 undo not working
//                        .setAction("Undo", view -> {
//                            documentReference.set(eventModel);
//                            Log.d("snack", "onUndoClick: " + documentReference.get());
//                        })
                        .show();
            }
        });

        binding.eventLiveRecyclerView.setAdapter(adapter);

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

//        Nav controller won't work if not initialised in OnViewCreated
//        navController = Navigation.findNavController(getActivity(), R.id.eventManageMainNavHostFragment);
    }
}