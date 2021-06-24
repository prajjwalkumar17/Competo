package com.StartupBBSR.competo.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.StartupBBSR.competo.Adapters.EventFragmentAdapter;
import com.StartupBBSR.competo.Models.EventModel;
import com.StartupBBSR.competo.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EventActivity extends AppCompatActivity {
    private static final String TAG = "EventActivity";
    private static final String EVENT = "Events";

    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    private EventFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager (this, 2));

        Query query = mDb.collection(EVENT)
                .orderBy("eventStatus");
        FirestoreRecyclerOptions<EventModel> options = new FirestoreRecyclerOptions.Builder<EventModel>()
                .setQuery(query, EventModel.class)
                .build();

        mAdapter = new EventFragmentAdapter(options);
        recyclerView.setAdapter(mAdapter);

        EditText searchBox = findViewById(R.id.EventsearchBox);
        searchBox.addTextChangedListener(new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "Searchbox has changed to: " + s.toString());
                Query query;
                if (s.toString().isEmpty()) {
                    query = mDb.collection(EVENT)
                            .orderBy("eventStatus");
                } else {
                    query = mDb.collection(EVENT)
                            .whereEqualTo("eventTitle", s.toString())
                            .orderBy("eventStatus");
                }
                FirestoreRecyclerOptions<EventModel> options = new FirestoreRecyclerOptions.Builder<EventModel>()
                        .setQuery(query, EventModel.class)
                        .build();
                mAdapter.updateOptions(options);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

}
