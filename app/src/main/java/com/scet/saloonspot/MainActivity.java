package com.scet.saloonspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scet.saloonspot.utils.Constant;

public class MainActivity extends AppCompatActivity {

    EditText edEmail, edPassword;
    TextView txtSignUp;
    CheckBox show_hide;
    Button btnLogin;
    CoordinatorLayout coordinatorLayoutlogin;
    //    CheckBox show_hide;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static final String adminUser = "admin@admin.com";
    public static final String adminUPassword = "admin@123";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Toast.makeText(MainActivity.this, "Successfully signed in with: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out

                    Toast.makeText(MainActivity.this, "User Signout in with: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                }
                // ...
            }
        };

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();


        btnLogin = findViewById(R.id.btnlogin);
        edEmail = findViewById(R.id.etemail);
        txtSignUp = findViewById(R.id.txtsignup);
        edPassword = findViewById(R.id.etpassword);
        show_hide = findViewById(R.id.show_hide_password);
        coordinatorLayoutlogin=findViewById(R.id.coordinatorLayoutlogin);
        show_hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // show password
                    edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){


                //  Toast.makeText(MainActivity.this, "Login Clicked", Toast.LENGTH_SHORT).show();

                String email = edEmail.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                if (email.equals("") || password.equals("")) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayoutlogin, "Please Fill All The Fields", Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else if (email.equalsIgnoreCase(adminUser) && password.equalsIgnoreCase(adminUPassword)){
                        Intent intent = new Intent(MainActivity.this,AdminDashBoardActivity.class);
                        startActivity(intent);
                        finish();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Snackbar snackbar = Snackbar
                                                .make(coordinatorLayoutlogin, "Login Success", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                        String value = "";
                                        if (task.getResult().getUser().getDisplayName().equals(Constant.SALOON)){
                                            value = Constant.SALOON;
                                            Intent intent = new Intent(MainActivity.this, SaloonDashboard.class);
                                            intent.putExtra(Constant.ARGS_SALOON,task.getResult().getUser().getUid());
                                            startActivity(intent);
                                            finish();

                                        } else if (task.getResult().getUser().getDisplayName().equals(Constant.USER)){
                                            value = Constant.USER;
                                            Intent intent = new Intent(MainActivity.this, Dashboard.class);
                                            intent.putExtra(Constant.ARGS_SALOON,value);
                                            startActivity(intent);
                                            finish();
                                        }


                                    } else {
                                        Snackbar snackbar = Snackbar
                                                .make(coordinatorLayoutlogin, "Login Failes", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }
                                }
                            });


                }
            }});}




    @Override
    public void onBackPressed(){

        finishAffinity();


    }}

