package com.scet.saloonspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scet.saloonspot.utils.AppUtils;
import com.scet.saloonspot.utils.Constant;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edEmail, edPassword;
    TextView txtSignUp,forgot_password;
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

                    Toast.makeText(LoginActivity.this, "Successfully signed in with: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out

                    Toast.makeText(LoginActivity.this, "User Signout in with: " + user.getEmail(), Toast.LENGTH_SHORT).show();
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
        forgot_password = findViewById(R.id.forgot_password);
        show_hide = findViewById(R.id.show_hide_password);
        coordinatorLayoutlogin = findViewById(R.id.coordinatorLayoutlogin);
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
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnlogin:
                new LongOperation(this).execute();
                break;
        }
    }

    public class LongOperation extends AsyncTask<Void, Void, Void> {

        public LongOperation(Activity activity) {

            dialog = new ProgressDialog(activity);
        }

        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Login....");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String email = edEmail.getText().toString().trim();
            String password = edPassword.getText().toString().trim();
            if (email.equals("") || password.equals("")) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayoutlogin, "Please Fill All The Fields", Snackbar.LENGTH_LONG);
                snackbar.show();

            } else if (email.equalsIgnoreCase(adminUser) && password.equalsIgnoreCase(adminUPassword)) {
                Intent intent = new Intent(LoginActivity.this, AdminDashBoardActivity.class);
                startActivity(intent);
                finish();
            } else {

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Snackbar snackbar = Snackbar
                                                .make(coordinatorLayoutlogin, "Login Successful", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                        String value = "";
                                        if (task.getResult().getUser().getDisplayName().equals(Constant.SALOON)) {
                                            AppUtils.setStringValue(LoginActivity.this, Constant.LoginKey, Constant.SALOON);
                                            AppUtils.setStringValue(LoginActivity.this, Constant.USERID, task.getResult().getUser().getUid());
                                            AppUtils.setValue(LoginActivity.this, Constant.ISSALON, true);
                                            AppUtils.setValue(LoginActivity.this, Constant.ISUSER, false);
                                            value = Constant.SALOON;
                                            Intent intent = new Intent(LoginActivity.this, SaloonDashboard.class);
                                            intent.putExtra(Constant.ARGS_SALOON, task.getResult().getUser().getUid());
                                            startActivity(intent);
                                            finish();

                                        } else if (task.getResult().getUser().getDisplayName().equals(Constant.USER)) {
                                            AppUtils.setStringValue(LoginActivity.this, Constant.LoginKey, Constant.USER);
                                            AppUtils.setStringValue(LoginActivity.this, Constant.USERID, task.getResult().getUser().getUid());
                                            AppUtils.setValue(LoginActivity.this, Constant.ISUSER, true);
                                            AppUtils.setValue(LoginActivity.this, Constant.ISSALON, false);
                                            value = Constant.USER;
                                            Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                                            intent.setAction("login");
                                            intent.putExtra(Constant.ARGS_SALOON, value);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } else {
                                        Snackbar snackbar = Snackbar
                                                .make(coordinatorLayoutlogin, "Login Failed", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }
                                }
                            });

            }
                return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}

