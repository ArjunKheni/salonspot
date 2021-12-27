package com.scet.saloonspot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scet.saloonspot.adapters.ServiceAdapter;
import com.scet.saloonspot.models.Services;
import com.scet.saloonspot.utils.Constant;

import java.util.ArrayList;

public class ShowServiceActivity extends AppCompatActivity implements ServiceAdapter.onServiceAdd {

    RecyclerView activelistview;
    String saloonId = "";
    ArrayList<Services> servicesList = new ArrayList<>();
    ServiceAdapter adapter;
    Button btndelete;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_servicelist);
        btndelete=findViewById(R.id.btndelete);
        getInit();
        getData();
    }

    private void getData() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting Data");
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saloons").child(saloonId).child("Services");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                        String name = datas.child("name").getValue().toString();
                    String price = datas.child("price").getValue().toString();
                    String type = datas.child("type").getValue().toString();

                    Services services = new Services();
                    services.setType(type);
                    services.setPrice(price);
                    services.setName(name);

                    servicesList.add(services);

                }
                adapter = new ServiceAdapter(ShowServiceActivity.this,servicesList,"saloon",ShowServiceActivity.this);
                activelistview.setAdapter(adapter);
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
//btndelete.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot datas: dataSnapshot.getChildren()){
//
//                    datas.getRef().getKey();
//                }
//
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                progressDialog.dismiss();
//            }
//        });
//    }
//});

    }

    private void getInit() {
        saloonId = getIntent().getStringExtra(Constant.ARGS_SALOON);
        activelistview = findViewById(R.id.activelistview);
        activelistview.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onServeiceAdd(Services services, boolean isRemove) {

    }
}