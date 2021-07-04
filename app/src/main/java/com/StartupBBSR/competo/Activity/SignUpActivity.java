 package com.StartupBBSR.competo.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.StartupBBSR.competo.Listeners.addOnTextChangeListener;
import com.StartupBBSR.competo.Models.ChatConnectionModel;
import com.StartupBBSR.competo.R;
import com.StartupBBSR.competo.Utils.Constant;
import com.StartupBBSR.competo.databinding.ActivitySignUpBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


 public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding activitySignUpBinding;
    private int flag = 0;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseDB;


    private int temp_flag = 0;
    private String btn_clicked = "";

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 123;

    //    Facebook
    private CallbackManager mCallbackManager;
    private static String TAG = "fbdebug";

    private Constant constant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        Disable nightmode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        activitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpBinding.getRoot());

//       Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseFirestore.getInstance();

        constant = new Constant();


//        For fb
        mCallbackManager = CallbackManager.Factory.create();

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


        //        Google Sign in
        activitySignUpBinding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_clicked = "google";
                checkRole();
            }
        });

//        Facebook Sign in
        activitySignUpBinding.btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_clicked = "fb";
                checkRole();
            }
        });

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: ");
                Log.d("fblog", "onSuccess: " + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: ");
                Log.d("fblog", "onError: " + error);
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

    private void checkRole() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("Select Role");
        builder.setMessage("Are you a part of any organization?");

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        temp_flag = 1;
                        Log.d("alertdialogrole", "Role: Organization");
                        if (btn_clicked.equals("google")) {
                            googleSignIn();
                            googleSignInIntent();
                        } else if (btn_clicked.equals("fb")) {
                            LoginManager.getInstance().logInWithReadPermissions(SignUpActivity.this, Arrays.asList("email", "public_profile"));
                        }

                        dialogInterface.dismiss();
                    }
                }
        );

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        temp_flag = 0;
                        Log.d("alertdialogrole", "Role: User ");

                        if (btn_clicked.equals("google")) {
                            googleSignIn();
                            googleSignInIntent();
                        } else if (btn_clicked.equals("fb")) {
                            LoginManager.getInstance().logInWithReadPermissions(SignUpActivity.this, Arrays.asList("email", "public_profile"));
                        }
                        dialogInterface.dismiss();
                    }
                }
        );

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void googleSignIn() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void googleSignInIntent() {
        Intent googleSignInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(googleSignInIntent, RC_SIGN_IN);
    }

    private void handleFacebookToken(AccessToken accessToken) {
        Log.d(TAG, "handleFacebookToken: ");
        Log.d("fblog", "handleFacebookToken: " + accessToken);

        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("fblog", "SignInWithCredential: successful");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    loginWithCredential(user);
                } else {
                    Toast.makeText(getApplicationContext(), "SignInWithCredential: failed\n" + task.getResult(), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Result from intent from googleSignIn
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
//                Google Sign In successful
                GoogleSignInAccount account = task.getResult(ApiException.class);

                Log.d("googlesignin: ", "firebaseAuthWithGoogle: " + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.d("googlesignin: ", "Google Sign In failed\n" + e.getMessage());
                ;
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Sign in success
                            Log.d("googlesignin", "signInWithCredential: success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            loginWithCredential(user);
                        } else {
                            Toast.makeText(getApplicationContext(), "SignInWithCredential: failed", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }

    private void loginWithCredential(FirebaseUser user) {
        String TAG = "cred";

        Log.d(TAG, "loginWithCredential: Name: " + user.getDisplayName());
        Log.d(TAG, "loginWithCredential: Email: " + user.getEmail());
        Log.d(TAG, "loginWithCredential: Phone: " + user.getPhoneNumber());
        if (temp_flag == 1) {
            Log.d(TAG, "loginWithCredential: Role: Organizer");
        } else {
            Log.d(TAG, "loginWithCredential: Role: User");
        }

//        To add user to database if not done
        DocumentReference documentReference = firebaseDB.collection("Users")
                .document(firebaseAuth.getCurrentUser().getUid());

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put(constant.getUserNameField(), user.getDisplayName());
        userInfo.put(constant.getUserPhoneField(), user.getPhoneNumber());
        userInfo.put(constant.getUserEmailField(), user.getEmail());
        userInfo.put(constant.getUserPhotoField(), user.getPhotoUrl().toString());
        userInfo.put(constant.getUserPhotoField(), null);
        userInfo.put(constant.getUserBioField(), null);
        userInfo.put(constant.getUserLinkedinField(), null);
        userInfo.put(constant.getUserInterestedChipsField(), null);
        userInfo.put(constant.getUserIdField(), firebaseAuth.getUid());
        userInfo.put(constant.getUserMyEventField(), null);


//        Now we check the role selected
        if (temp_flag == 0) {
            userInfo.put(constant.getUserisUserField(), "1");
            userInfo.put(constant.getUserisOrganizerField(), "0");
        } else {
            userInfo.put(constant.getUserisOrganizerField(), "1");
            userInfo.put(constant.getUserisUserField(), "0");
        }

        documentReference.set(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
//                Update connection to null
                CollectionReference connectionRef = firebaseDB.collection(constant.getChatConnections());
                connectionRef.document(firebaseAuth.getUid()).set(new ChatConnectionModel(null, null));
            }
        });

        if (temp_flag == 0)
            Toast.makeText(this, "User Mode", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Organizer Mode", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();

    }

    private void onSignUp() {
//        Check whether any field is left blank
        flag = 0;
        checkEmptyField(activitySignUpBinding.nameET, activitySignUpBinding.nameTIL);
        checkEmptyField(activitySignUpBinding.numberET, activitySignUpBinding.numberTIL);
        checkEmptyField(activitySignUpBinding.emailET, activitySignUpBinding.emailTIL);
        checkEmptyField(activitySignUpBinding.passwordET, activitySignUpBinding.passwordTIL);

        //check for email format
        checkemailformat(activitySignUpBinding.emailET, activitySignUpBinding.emailTIL);

        //check phone number
        checkphonenumber(activitySignUpBinding.numberET, activitySignUpBinding.numberTIL);


        if (flag == 6) {
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
                    userInfo.put(constant.getUserNameField(), activitySignUpBinding.nameET.getText().toString());
                    userInfo.put(constant.getUserPhoneField(), activitySignUpBinding.numberET.getText().toString());
                    userInfo.put(constant.getUserEmailField(), activitySignUpBinding.emailET.getText().toString());
                    userInfo.put(constant.getUserPhotoField(), null);
                    userInfo.put(constant.getUserBioField(), null);
                    userInfo.put(constant.getUserLinkedinField(), null);
                    userInfo.put(constant.getUserInterestedChipsField(), null);
                    userInfo.put(constant.getUserIdField(), firebaseAuth.getUid());
                    userInfo.put(constant.getUserMyEventField(), null);

//                      Now we check the role selected from the switch
                    if (activitySignUpBinding.roleSwitch.isChecked()) {
                        userInfo.put(constant.getUserisUserField(), "1");
                        userInfo.put(constant.getUserisOrganizerField(), "0");
                    } else {
                        userInfo.put(constant.getUserisOrganizerField(), "1");
                        userInfo.put(constant.getUserisUserField(), "0");
                    }


                    documentReference.set(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
//                Update connection to null
                            CollectionReference connectionRef = firebaseDB.collection(constant.getChatConnections());
                            connectionRef.document(firebaseAuth.getUid()).set(new ChatConnectionModel(null, null));
                        }
                    });


                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

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

     private void checkemailformat(EditText et, TextInputLayout til)
     {
         if(Pattern.compile("gmail\\.com$").matcher(et.getText().toString()).find())
         {
             flag++;
         }
         else if(Pattern.compile("hotmail\\.com$").matcher(et.getText().toString()).find())
         {
             flag++;
         }
         else if(Pattern.compile("yahoo\\.com$").matcher(et.getText().toString()).find())
         {
             flag++;
         }
         else if(Pattern.compile("kiit\\.ac\\.in$").matcher(et.getText().toString()).find())
         {
             flag++;
         }
         else
         {
             til.setError("Please enter correct format");
             til.setErrorEnabled(true);
         }
     }

     private void checkphonenumber(EditText et, TextInputLayout til)
     {
         if(Pattern.compile("^[1-9][0-9]{9}$").matcher(et.getText().toString()).find())
         {
             flag++;
         }
         else
         {
             til.setError("Please correct the phone number");
             til.setErrorEnabled(true);
         }
     }


 }