package com.StartupBBSR.competo.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.StartupBBSR.competo.Adapters.EventFragmentAdapter;
import com.StartupBBSR.competo.Adapters.EventPalUserAdapter;
import com.StartupBBSR.competo.Models.EventModel;
import com.StartupBBSR.competo.Models.EventPalModel;
import com.StartupBBSR.competo.Models.UserModel;
import com.StartupBBSR.competo.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FindActivity extends AppCompatActivity {
    private static final String TAG = "FindActivity";
    private static final String EVENT = "events";
    private static final String USER = "Users";

    private final FirebaseFirestore mDB = FirebaseFirestore.getInstance();

    private EventFragmentAdapter mAdapter;
    private EventPalUserAdapter mADAPTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_find);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager (this, 2));

        Query query = mDB.collection(EVENT)
                .orderBy("eventStatus");
        FirestoreRecyclerOptions<EventModel> options = new FirestoreRecyclerOptions.Builder<EventModel>()
                .setQuery(query, EventModel.class)
                .build();

        mAdapter = new EventFragmentAdapter(options);
        recyclerView.setAdapter(mAdapter);

        query = mDB.collection(USER)
                .orderBy("Name");
        FirestoreRecyclerOptions<UserModel> Options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class)
                .build();

        mADAPTER = new EventPalUserAdapter((FirestoreRecyclerOptions<UserModel>) getOptions(Options));
        recyclerView.setAdapter(mADAPTER);



        EditText searchBox = findViewById(R.id.search_field);
        searchBox.addTextChangedListener(new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d (TAG, "Searchbox has changed to: " + s.toString ());
                Query query;
                if (s.toString ().isEmpty ()) {
                    query = mDB.collection (EVENT)
                            .orderBy ("eventStatus");
                } else {
                    query = mDB.collection (USER)
                            .whereEqualTo ("Name", s.toString ())
                            .orderBy ("Name");
                    query = mDB.collection (EVENT)
                            .whereEqualTo ("eventTitle", s.toString ())
                            .orderBy ("eventStatus");
                }
                FirestoreRecyclerOptions<EventModel> options = new FirestoreRecyclerOptions.Builder<EventModel> ()
                        .setQuery (query, EventModel.class)
                        .build ();
                mAdapter.updateOptions (options);
                FirestoreRecyclerOptions<UserModel> Options = new FirestoreRecyclerOptions.Builder<UserModel> ()
                        .setQuery (query, UserModel.class)
                        .build ();
                mADAPTER.updateOptions ((FirestoreRecyclerOptions<EventPalModel>) getOptions (Options));
            }

        });

    }

    private static Object getOptions(FirestoreRecyclerOptions<UserModel> Options) {
        return Options;
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
