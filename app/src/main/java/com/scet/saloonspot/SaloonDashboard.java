package com.scet.saloonspot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scet.saloonspot.models.Saloon;
import com.scet.saloonspot.utils.Constant;

public class SaloonDashboard extends AppCompatActivity {

    String uid = "";
    DatabaseReference mDatabase;
    TextView saloonname;
    TextView txtActiveService;
    ProgressDialog progressDialog;
    FloatingActionButton addservicebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saloon_dashboard);
        getInit();
        getIntentData(getIntent());
    }

    private void getInit() {
        saloonname = findViewById(R.id.saloonname);
        txtActiveService = findViewById(R.id.txtActiveService);
        addservicebtn = findViewById(R.id.addservicebtn);
        addservicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaloonDashboard.this,AddServiceActivity.class);
                intent.putExtra(Constant.ARGS_SALOON,uid);
                startActivity(intent);
            }
        });

        txtActiveService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaloonDashboard.this,ShowServiceActivity.class);
                intent.putExtra(Constant.ARGS_SALOON,uid);
                startActivity(intent);
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting Data");
        progressDialog.setMessage("Please wait");
        progressDialog.show();
    }

    private void getIntentData(Intent intent) {
        uid = intent.getStringExtra(Constant.ARGS_SALOON);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Saloons").child(uid);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // Saloon saloon= dataSnapshot.getValue(Saloon.class);
                String name = dataSnapshot.child("SaloonName").getValue().toString();
                saloonname.setText(name);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(SaloonDashboard.this, "Failed to load", Toast.LENGTH_SHORT).show();

            }
        };
        mDatabase.addValueEventListener(postListener);
    }
}