package com.scet.saloonspot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scet.saloonspot.models.Saloon;
import com.scet.saloonspot.utils.Constant;

import java.util.Calendar;

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
        mDatabase = FirebaseDatabase.getInstance().getReference().child("R_Request");
        addservicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaloonDashboard.this, AddServiceActivity.class);
                intent.putExtra(Constant.ARGS_SALOON, uid);
                startActivity(intent);
            }
        });

        txtActiveService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaloonDashboard.this, ShowServiceActivity.class);
                intent.putExtra(Constant.ARGS_SALOON, uid);
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
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Saloons").child(uid);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_saloondash, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.saloontime:
                showCustomDialog();
                return true;
            case R.id.saloonApp:
                Intent intent = new Intent(SaloonDashboard.this,AppoitmentActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    AlertDialog alertDialog = null;
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showCustomDialog() {
        ViewGroup viewGroup = findViewById(R.id.content);
        View view = LayoutInflater.from(this).inflate(R.layout.timepicker_dialog, viewGroup, false);

        Button save = view.findViewById(R.id.save);
        Button cancel = view.findViewById(R.id.cancel);

        final Button btn_openTime = view.findViewById(R.id.opentimepick);
        final Button btn_closeTime = view.findViewById(R.id.closetilepick);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

        btn_openTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, 9);
                c.set(Calendar.MINUTE, 00);
                TimePickerDialog timePickerDialog = new TimePickerDialog(SaloonDashboard.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                btn_openTime.setText(hourOfDay + ":" + minute);
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });

        btn_closeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, 22);
                c.set(Calendar.MINUTE, 00);
                TimePickerDialog timePickerDialog = new TimePickerDialog(SaloonDashboard.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                btn_closeTime.setText(hourOfDay + ":" + minute);
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                addOrUpdateWorkingHours(btn_openTime.getText().toString(), btn_closeTime.getText().toString());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void addOrUpdateWorkingHours(String openTime, String closeTime){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Saloons").child(uid).child("Workinghours").child("OpenTime").setValue(openTime.trim());
        mDatabase.child("Saloons").child(uid).child("Workinghours").child("CloseTime").setValue(closeTime.trim());
    }

}