package com.scet.saloonspot;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.scet.saloonspot.utils.Constant;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddSaloonActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    private CircleImageView saloonLogo;
    private EditText edSaloonName;
    private EditText edMobile;
    private EditText edSaloonAddress;
    private EditText edArea;
    private EditText edEmail;
    private EditText edPassword;
    private Button btnSubmit;
    private Uri photoUri;
    private Spinner spinner;
    private static final int SELECT_PHOTO = 100;
    private static final int STORAGE_PERMISSION = 111;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    int selectedPosition = 0;
    private static final String[] areas = {"Varachcha", "Vesu", "Adajan", "Katargam", "Kamrej"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_saloon);
        firebaseAuth = FirebaseAuth.getInstance();
        getInit();
        setEvents();
        checkingPermissin();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    private void checkingPermissin() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        }
    }

    private void setEvents() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edSaloonName.equals("") || edArea.equals("") || edEmail.equals("") || edMobile.equals("") || edPassword.equals("") || edSaloonAddress.equals("") || photoUri.equals("")) {
                    Toast.makeText(AddSaloonActivity.this, "Enter all the Credentials", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(edEmail.getText().toString(), edPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    addDetails(task.getResult().getUser().getUid());
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(Constant.SALOON).build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                }
                            });

                }
            }
        });
        saloonLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
    }

    private void addDetails(String uid) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adding Details");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        StorageReference ref = storageReference.child("images/" + edSaloonName.getText());
        ref.putFile(photoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(AddSaloonActivity.this, "Added", Toast.LENGTH_SHORT).show();
                        onBackPressed();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddSaloonActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });


        DatabaseReference myRef = database.getReference();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final String key = uid;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference;
//        Saloon saloon = new Saloon(edSaloonName.getText().toString().trim(),
//                edSaloonAddress.getText().toString().trim(),
//                edArea.getText().toString().trim(),
//                edMobile.getText().toString().trim(),
//                edEmail.getText().toString(),
//                edPassword.getText().toString(), "");
//
//        myRef.child("Saloons").setValue(saloon);

        databaseReference = firebaseDatabase.getReference("Saloons");
        databaseReference.child(key).child("SaloonName").setValue(edSaloonName.getText().toString().trim());
//        databaseReference = firebaseDatabase.getReference("Saloons");
//        databaseReference.child(key).child("Lastname").setValue(edlastname.getText().toString().trim());
        databaseReference = firebaseDatabase.getReference("Saloons");
        databaseReference.child(key).child("Mobile").setValue(edMobile.getText().toString().trim());
        databaseReference = firebaseDatabase.getReference("Saloons");
        databaseReference.child(key).child("SaloonAddress").setValue(edSaloonAddress.getText().toString().trim());
        databaseReference = firebaseDatabase.getReference("Saloons");
//        databaseReference.child(key).child("City").setValue(edCity.getText().toString().trim());
//        databaseReference = firebaseDatabase.getReference("Saloons");
        databaseReference.child(key).child("Area").setValue(areas[selectedPosition]);
//        databaseReference = firebaseDatabase.getReference("Saloons");
//        databaseReference.child(key).child("Username").setValue(edUsername.getText().toString().trim());

        databaseReference = firebaseDatabase.getReference("Saloons");
        databaseReference.child(key).child("Email").setValue(edEmail.getText().toString());

        databaseReference = firebaseDatabase.getReference("Saloons");
        databaseReference.child(key).child("Password").setValue(edPassword.getText().toString());
        databaseReference = firebaseDatabase.getReference("Saloons");
        //String extension = photoUri.toString().substring(photoUri.toString().lastIndexOf("."));
        databaseReference.child(key).child("ext").setValue(getContentResolver().getType(photoUri));


    }

    private void getInit() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add saloon");
        saloonLogo = findViewById(R.id.saloonLogo);
        edSaloonName = findViewById(R.id.edSaloonName);
        edMobile = findViewById(R.id.edMobile);
        edSaloonAddress = findViewById(R.id.edSaloonAddress);
        edArea = findViewById(R.id.edArea);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddSaloonActivity.this,
                android.R.layout.simple_spinner_item, areas);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    photoUri = selectedImage;
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    saloonLogo.setImageBitmap(yourSelectedImage);
                }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedPosition = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
