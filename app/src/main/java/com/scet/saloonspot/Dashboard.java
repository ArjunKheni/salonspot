package com.scet.saloonspot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scet.saloonspot.adapters.SaloonAdapter;
import com.scet.saloonspot.models.Saloon;
import com.scet.saloonspot.utils.Constant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Dashboard extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Saloon> saloonList = new ArrayList<>();
    ArrayList<Saloon> allsaloonlist = new ArrayList<>();
    public static ArrayList<Saloon> copySaloonList = new ArrayList<>();
    SaloonAdapter adapter;
    Spinner sortspinner;
    ImageButton aibotid;
    private FirebaseAuth firebaseAuth;
    String value = "";
    int i = 0;
    float rat = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        recyclerView = findViewById(R.id.recyclerView);
        sortspinner = findViewById(R.id.sortspinner);
        aibotid = findViewById(R.id.aibotid);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseAuth = FirebaseAuth.getInstance();
        getIntentData(getIntent());
        getData();

aibotid.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i1= new Intent(Dashboard.this, chatbot.class);
        startActivity(i1);
    }
});
        sortspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    Collections.sort(saloonList, new Comparator<Saloon>() {
                        @Override
                        public int compare(Saloon o1, Saloon o2) {
                            // return Float.valueOf(o1.getAvgRating()) > Float.valueOf(o2.getAvgRating());
                            Float f1 = Float.valueOf(o1.getAvgRating());
                            Float f2 = Float.valueOf(o2.getAvgRating());
                            return f2.compareTo(f1);
                        }
                    });

                    adapter = new SaloonAdapter(Dashboard.this,saloonList);
                    recyclerView.setAdapter(adapter);
                } else if (position == 0) {
                    adapter = new SaloonAdapter(Dashboard.this,allsaloonlist);
                    recyclerView.setAdapter(adapter);
            }
                else
                {

                    adapter = new SaloonAdapter(Dashboard.this,allsaloonlist);
                    recyclerView.setAdapter(adapter);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getIntentData(Intent intent) {
        value = intent.getStringExtra(Constant.ARGS_SALOON);
        if (value.equals(Constant.USER)){
            Toast.makeText(this, "User Login", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Saloon Login", Toast.LENGTH_SHORT).show();
        }
    }

    private void getData() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saloons");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    String name = datas.child("SaloonName").getValue().toString();
                    String address = datas.child("SaloonAddress").getValue().toString();
                    String mobile = datas.child("Mobile").getValue().toString();
                    String area = datas.child("Area").getValue().toString();
                    String email = datas.child("Email").getValue().toString();
                    String password = datas.child("Password").getValue().toString();
                    String ext = datas.child("ext").getValue().toString();
                    ext = ext.replace("image/","");
                    String id = datas.getKey();

                    DataSnapshot ratting = datas.child("Reviews");
                    for (DataSnapshot snapshot : ratting.getChildren()){
                        i = i + 1;
                        rat = rat + Float.parseFloat(snapshot.child("ratting").getValue().toString());
                    }

                    if (i > 0) {
                        rat = rat / i;
                    } else {
                        rat = 0;
                    }

                    Saloon saloon = new Saloon(name,address,mobile,area,email,password,"",ext);
                    saloon.setAvgRating(String.valueOf(rat));
                    saloon.setId(id);
                    saloonList.add(saloon);
                    allsaloonlist.add(saloon);
                }

                adapter = new SaloonAdapter(Dashboard.this,saloonList);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
