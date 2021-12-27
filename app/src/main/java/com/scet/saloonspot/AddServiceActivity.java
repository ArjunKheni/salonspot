package com.scet.saloonspot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scet.saloonspot.models.Services;
import com.scet.saloonspot.utils.Constant;

public class AddServiceActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinner;
    EditText edtservicename;
    EditText edtservicprice;
    Button btnaddservice;
    String type = "";
    String name = "";
    String price = "";
    String saloonId = "";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saloon_addservice);
        getIntentData(getIntent());
        getInit();
    }

    private void getIntentData(Intent intent) {
        saloonId = intent.getStringExtra(Constant.ARGS_SALOON);
    }

    private void getInit() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        spinner = findViewById(R.id.spinner);
        edtservicename = findViewById(R.id.edtservicename);
        edtservicprice = findViewById(R.id.edtservicprice);
        btnaddservice = findViewById(R.id.btnaddservice);

        btnaddservice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnaddservice:
                addService();
                break;
        }
    }

    private void addService() {
        type = spinner.getSelectedItem().toString();
        name = edtservicename.getText().toString();
        price = edtservicprice.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Enter Service Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(price)){
            Toast.makeText(this, "Please Enter price", Toast.LENGTH_SHORT).show();
        } else {

            Services services = new Services();
            services.setName(name);
            services.setPrice(price);
            services.setType(type);

            mDatabase.child("Saloons").child(saloonId).child("Services").child(mDatabase.push().getKey()).setValue(services);

            edtservicename.setText("");
            edtservicprice.setText("");
        }
    }
}