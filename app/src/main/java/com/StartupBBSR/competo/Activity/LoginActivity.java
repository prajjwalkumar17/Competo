package com.StartupBBSR.competo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.StartupBBSR.competo.Listeners.addOnTextChangeListener;
import com.StartupBBSR.competo.MainActivity;
import com.StartupBBSR.competo.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding activityLoginBinding;
    private int flag = 0;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());

//       Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseFirestore.getInstance();

        activityLoginBinding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogin();
            }
        });

        activityLoginBinding.signupTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUp();
            }
        });

//        TextChangedListeners
        textChangedListener(activityLoginBinding.emailET, activityLoginBinding.emailTIL);
        textChangedListener(activityLoginBinding.passwordET, activityLoginBinding.passwordTIL);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    private void onLogin() {
//        Login
//        Check for blank fields
        flag = 0;
        checkEmptyField(activityLoginBinding.emailET, activityLoginBinding.emailTIL);
        checkEmptyField(activityLoginBinding.passwordET, activityLoginBinding.passwordTIL);

        if (flag == 2) {
//            Now we can attempt to login the user
            activityLoginBinding.loginProgressLayout.setVisibility(View.VISIBLE);

            firebaseAuth.signInWithEmailAndPassword(
                    activityLoginBinding.emailET.getText().toString(),
                    activityLoginBinding.passwordET.getText().toString()
            ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(LoginActivity.this, "LogIn Successful", Toast.LENGTH_SHORT).show();
                    checkUserRole(firebaseAuth.getCurrentUser().getUid());
                    activityLoginBinding.loginProgressLayout.setVisibility(View.GONE);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "LogIn Failed:\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    activityLoginBinding.loginProgressLayout.setVisibility(View.GONE);
                }
            });
        }
    }

    private void onSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkUserRole(String uid) {
//        To retrieve data from the database to check role of the user (organizer or user)
        DocumentReference documentReference = firebaseDB
                .collection("Users")
                .document(uid);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
//            Now this document snapshot will contain the data of the document referenced above
            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                Now to identify the user role
                if (documentSnapshot.getString("isOrganizer") != null) {
//                    organizer role
                    startActivity(new Intent(getApplicationContext(), OrganizerActivity.class));
                    finish();
                } else if (documentSnapshot.getString("isUser") != null) {
//                    User role
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void textChangedListener(TextInputEditText ET, TextInputLayout TIL) {
        ET.addTextChangedListener(new addOnTextChangeListener(this, ET, TIL));
    }

    private void checkEmptyField(EditText et, TextInputLayout til) {
        if (et.getText().toString().isEmpty()) {
            til.setError("Field cannot be blank");
            til.setErrorEnabled(true);
            flag--;
        } else {
            flag++;
        }

    }

}