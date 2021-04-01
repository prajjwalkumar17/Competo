package com.StartupBBSR.competo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.StartupBBSR.competo.Listeners.addOnTextChangeListener;
import com.StartupBBSR.competo.MainActivity;
import com.StartupBBSR.competo.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding activitySignUpBinding;
    private int flag = 0;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpBinding.getRoot());

//       Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseFirestore.getInstance();

        activitySignUpBinding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUp();
            }
        });

        activitySignUpBinding.signInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignIn();
            }
        });

//        Text Change Listeners
        textChangedListener(activitySignUpBinding.nameET, activitySignUpBinding.nameTIL);
        textChangedListener(activitySignUpBinding.numberET, activitySignUpBinding.numberTIL);
        textChangedListener(activitySignUpBinding.emailET, activitySignUpBinding.emailTIL);
        textChangedListener(activitySignUpBinding.passwordET, activitySignUpBinding.passwordTIL);

//        Check role switch
        checkRoleSwitch();


    }

    private void onSignUp() {
//        Check whether any field is left blank
        flag = 0;
        checkEmptyField(activitySignUpBinding.nameET, activitySignUpBinding.nameTIL);
        checkEmptyField(activitySignUpBinding.numberET, activitySignUpBinding.numberTIL);
        checkEmptyField(activitySignUpBinding.emailET, activitySignUpBinding.emailTIL);
        checkEmptyField(activitySignUpBinding.passwordET, activitySignUpBinding.passwordTIL);


        if (flag == 4) {
//            We can now register the user
            activitySignUpBinding.signUpProgressLayout.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(
                    activitySignUpBinding.emailET.getText().toString(),
                    activitySignUpBinding.passwordET.getText().toString()
            ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(SignUpActivity.this, "Account Created", Toast.LENGTH_SHORT).show();

//                    Now to store data into the database
                    DocumentReference documentReference = firebaseDB.collection("Users")
                            .document(firebaseAuth.getCurrentUser().getUid());

                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("Name", activitySignUpBinding.nameET.getText().toString());
                    userInfo.put("Phone", activitySignUpBinding.numberET.getText().toString());
                    userInfo.put("Email", activitySignUpBinding.emailET.getText().toString());

//                      Now we check the role selected from the switch
                    if (activitySignUpBinding.roleSwitch.isChecked())
                        userInfo.put("isUser", "1");
                    else
                        userInfo.put("isOrganizer", "1");


                    documentReference.set(userInfo); // add onsuccess or onfailure here for debugging

                    if (activitySignUpBinding.roleSwitch.isChecked()) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), OrganizerActivity.class));
                        finish();
                    }


                    activitySignUpBinding.signUpProgressLayout.setVisibility(View.GONE);
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(SignUpActivity.this, "Account creation failed:\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                activitySignUpBinding.signUpProgressLayout.setVisibility(View.GONE);
            });


        }
    }

    private void onSignIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
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

    private void checkRoleSwitch() {
        activitySignUpBinding.roleSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b)
                activitySignUpBinding.roleTV.setText("User");
            else
                activitySignUpBinding.roleTV.setText("Organizer");
        });
    }

}