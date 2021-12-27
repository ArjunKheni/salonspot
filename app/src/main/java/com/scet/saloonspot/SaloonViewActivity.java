package com.scet.saloonspot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scet.saloonspot.adapters.ServiceAdapter;
import com.scet.saloonspot.models.Services;
import com.scet.saloonspot.utils.Constant;

import java.util.ArrayList;

public class SaloonViewActivity extends AppCompatActivity implements ServiceAdapter.onServiceAdd{

    TextView sname;
    TextView semail;
    TextView saddress;
    TextView saloonarea;
    TextView smobile;
    TextView totalamt;
    ImageView saloonphoto;
    LinearLayout llTotal;
    FloatingActionButton addreview;
    int image;
    String name,email,mobile,area,add,id;
    RecyclerView ServicerecyclerView;
    ArrayList<Services> servicesList = new ArrayList<>();
    ServiceAdapter adapter;
    ProgressDialog progressDialog;
    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saloon_view);
        getIntentData(getIntent());
        getinit();
        getData();

    }

    private void getIntentData(Intent intent) {
        name = intent.getStringExtra("sname");
        email = intent.getStringExtra("semail");
        add = intent.getStringExtra("sadd");
        mobile = intent.getStringExtra("smobile");
        area = intent.getStringExtra("sarea");
        id = intent.getStringExtra("id");
        image = intent.getIntExtra("logo",0);
    }


    private void getData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting Data");
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saloons").child(id).child("Services");

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
                adapter = new ServiceAdapter(SaloonViewActivity.this,servicesList,"user",SaloonViewActivity.this);
                ServicerecyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    private void getinit() {
        sname = findViewById(R.id.sname);
        semail = findViewById(R.id.semail);
        saddress = findViewById(R.id.saddress);
        saloonarea = findViewById(R.id.saloonarea);
        smobile = findViewById(R.id.smobile);
        saloonphoto = findViewById(R.id.saloonphoto);
        llTotal = findViewById(R.id.llTotal);
        totalamt = findViewById(R.id.totalamt);
        addreview = findViewById(R.id.addreview);
        ServicerecyclerView = findViewById(R.id.ServicerecyclerView);
        semail.setText(email);
        sname.setText(name);
        saddress.setText(add);
        saloonarea.setText(area);
        smobile.setText(mobile);
        saloonphoto.setImageDrawable(getDrawable(image));
        ServicerecyclerView.setLayoutManager(new LinearLayoutManager(this));

        addreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaloonViewActivity.this,Showreviewactivity.class);
                intent.putExtra(Constant.ARGS_SALOON,id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onServeiceAdd(Services services, boolean isRemove) {
        if (isRemove){
            total = total - Integer.parseInt(services.getPrice());
        } else {
            total = total + Integer.parseInt(services.getPrice());
        }
        llTotal.setVisibility(View.VISIBLE);
        totalamt.setText(String.valueOf(total));
    }
}
