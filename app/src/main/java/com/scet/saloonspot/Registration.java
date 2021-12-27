package com.scet.saloonspot;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.Validator;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scet.saloonspot.models.User;
import com.scet.saloonspot.utils.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiresApi(api = Build.VERSION_CODES.O_MR1)
public class Registration extends AppCompatActivity implements Validator {

    Button btnSubmit;

    EditText etEmail, etPassword;
    TextView rtlogin;
    CheckBox show_hide;
    CoordinatorLayout coordinatorLayoutreg;
    EditText edMobile, edState, edCity, edArea, edUsername, edFirstname, edlastname;
    DatabaseReference mSearchedLocationReference;
    String uid, email;
    FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        show_hide = findViewById(R.id.shpass);
        edFirstname = findViewById(R.id.edFirstname);
        edlastname = findViewById(R.id.edLastname);
        edMobile = findViewById(R.id.edMobile);
        edState = findViewById(R.id.edState);
        edCity = findViewById(R.id.edCity);
        coordinatorLayoutreg = findViewById(R.id.coordinatorLayoutreg);
        edArea = findViewById(R.id.edArea);
        edUsername = findViewById(R.id.edUsername);

        etEmail = findViewById(R.id.edtemail);
        etPassword = findViewById(R.id.edtpass);
        rtlogin = findViewById(R.id.rtlogin);
        btnSubmit = findViewById(R.id.btnSubmit);

        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Toast.makeText(getApplicationContext(), "onAuthStateChanged:signed_in:" + user.getUid(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Successfully signed in with: " + user.getEmail(), Toast.LENGTH_SHORT).show();

                } else {
                    // User is signed out

                    Toast.makeText(getApplicationContext(), "Successfully signed Out", Toast.LENGTH_SHORT).show();
                }
                // ...
            }
        };
        rtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registration.this, LoginActivity.class);
                startActivity(i);
            }
        });
        show_hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // show password
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
                if (edFirstname.getText().toString().matches("") || edlastname.getText().toString().matches("") || edMobile.getText().toString().matches("")
                        || edState.getText().toString().matches("") || edCity.getText().toString().matches("") || edArea.getText().toString().matches("") || edUsername.getText().toString().matches("") || etEmail.getText().toString().matches("") || etPassword.getText().toString().matches("")) {

                    Snackbar snackbar = Snackbar.make(coordinatorLayoutreg, "Please Fill all the Details Carefully", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    final String email = etEmail.getText().toString().trim();
                    String pass = etPassword.getText().toString().trim();
                    if (email.matches(emailPattern)) {
                        if (pass.matches(PASSWORD_PATTERN) && pass.length() > 8) {

                            final String password = etPassword.getText().toString().trim();

                            final String firstname = edFirstname.getText().toString().trim();
                            user = FirebaseAuth.getInstance().getCurrentUser();

                            DatabaseReference myRef = database.getReference();
                            final String key = myRef.push().getKey();
                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {


                                            Toast.makeText(Registration.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                            if (email.equals("") || password.equals("")) {
                                                Toast.makeText(Registration.this, "Enter all Signup Credentials", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (!task.isSuccessful()) {
                                                    Toast.makeText(Registration.this, "Authentication Failed: Check Your Network Connections or No User Exit", Toast.LENGTH_SHORT).show();
                                                } else {


                                                    // ...
                                                }

                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(Constant.USER).build();

                                                user.updateProfile(profileUpdates)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                }
                                                            }
                                                        });

                                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                                DatabaseReference databaseReference;

                                                User user1 = new User();
                                                user1.setFirstname(edFirstname.getText().toString().trim());
                                                user1.setLastName(edlastname.getText().toString().trim());
                                                user1.setMobile(edMobile.getText().toString().trim());
                                                user1.setArea(edArea.getText().toString().trim());
                                                user1.setCity(edCity.getText().toString().trim());
                                                user1.setState(edState.getText().toString().trim());
                                                user1.setUserName(edUsername.getText().toString().trim());
                                                user1.setEmail(email);
                                                user1.setPassword(password);
                                                databaseReference = firebaseDatabase.getReference("Users");
                                                databaseReference.child(task.getResult().getUser().getUid()).setValue(user1);


//                                        databaseReference = firebaseDatabase.getReference("Users");
//                                        databaseReference.child(key).child("Firstname").setValue(edFirstname.getText().toString().trim());
//                                        databaseReference = firebaseDatabase.getReference("Users");
//                                        databaseReference.child(key).child("Lastname").setValue(edlastname.getText().toString().trim());
//                                        databaseReference = firebaseDatabase.getReference("Users");
//                                        databaseReference.child(key).child("Mobile").setValue(edMobile.getText().toString().trim());
//                                        databaseReference = firebaseDatabase.getReference("Users");
//                                        databaseReference.child(key).child("State").setValue(edState.getText().toString().trim());
//                                        databaseReference = firebaseDatabase.getReference("Users");
//                                        databaseReference.child(key).child("City").setValue(edCity.getText().toString().trim());
//                                        databaseReference = firebaseDatabase.getReference("Users");
//                                        databaseReference.child(key).child("Area").setValue(edArea.getText().toString().trim());
//                                        databaseReference = firebaseDatabase.getReference("Users");
//                                        databaseReference.child(key).child("Username").setValue(edUsername.getText().toString().trim());
//
//                                        databaseReference = firebaseDatabase.getReference("Users");
//                                        databaseReference.child(key).child("Email").setValue(email);
//
//                                        databaseReference = firebaseDatabase.getReference("Users");
//                                        databaseReference.child(key).child("Password").setValue(password);

//                                    progressDialog.dismiss();
                                                startActivity(new Intent(Registration.this, LoginActivity.class));
                                                finish();
//                                    finish();
                                            }


                                        }


                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter Strong Password", Toast.LENGTH_SHORT).show();

                        }
                    }

                    else{
                            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


//    @Override
//    public void onBackPressed(){
//
//        startActivity(new Intent(Registration.this, MainActivity.class));
//
//    }
            });
        }
    }
