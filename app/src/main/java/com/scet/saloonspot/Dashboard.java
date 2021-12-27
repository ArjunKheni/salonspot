package com.scet.saloonspot;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scet.saloonspot.adapters.SaloonAdapter;
import com.scet.saloonspot.models.Request;
import com.scet.saloonspot.models.Saloon;
import com.scet.saloonspot.models.Services;
import com.scet.saloonspot.models.User;
import com.scet.saloonspot.utils.AppUtils;
import com.scet.saloonspot.utils.Constant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Dashboard extends AppCompatActivity {

    public static ArrayList<Saloon> copySaloonList = new ArrayList<>();
    String User_id;
    RecyclerView recyclerView;
    ArrayList<Saloon> saloonList = new ArrayList<>();
    ArrayList<Saloon> rdSaloonList = new ArrayList<>();
    ArrayList<Saloon> allsaloonlist = new ArrayList<>();
    ArrayList<Saloon> locationList = new ArrayList<>();
    SaloonAdapter adapter;
    Spinner sortspinner;
    ImageButton aibotid;
    String value = "";
    String action = "";
    int i = 0;
    float rat = 0;
    ArrayList<Request> requests = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

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
        getUser();
        getData();
        User_id = AppUtils.getStringValue(this, Constant.USERID);
        aibotid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(Dashboard.this, chatbot.class);
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

                    adapter = new SaloonAdapter(Dashboard.this, saloonList);
                    recyclerView.setAdapter(adapter);
                } else if (position == 0) {
                    adapter = new SaloonAdapter(Dashboard.this, allsaloonlist);
                    recyclerView.setAdapter(adapter);
                } else if (position == 2) {
                    showDialog(Dashboard.this);
                } else {
                    adapter = new SaloonAdapter(Dashboard.this, allsaloonlist);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void showDialog(Activity activity) {
        locationList = new ArrayList<>();
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.item_location_dialog);

        final TextView txtVarachcha = (TextView) dialog.findViewById(R.id.txtVarachcha);
        final TextView txtVesu = (TextView) dialog.findViewById(R.id.txtVesu);
        final TextView txtAdajan = (TextView) dialog.findViewById(R.id.txtAdajan);
        final TextView txtKatargam = (TextView) dialog.findViewById(R.id.txtKatargam);
        TextView txtKamrej = (TextView) dialog.findViewById(R.id.txtKamrej);

        txtVarachcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int j = 0; j < allsaloonlist.size(); j++) {
                    Saloon saloon = allsaloonlist.get(j);
                    if (saloon.getArea().equalsIgnoreCase(txtVarachcha.getText().toString())) {
                        locationList.add(saloon);
                    }
                }
                adapter = new SaloonAdapter(Dashboard.this, locationList);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }
        });

        txtAdajan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int j = 0; j < allsaloonlist.size(); j++) {
                    Saloon saloon = allsaloonlist.get(j);
                    if (saloon.getArea().equalsIgnoreCase(txtAdajan.getText().toString())) {
                        locationList.add(saloon);
                    }
                }
                adapter = new SaloonAdapter(Dashboard.this, locationList);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }
        });

        txtKatargam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int j = 0; j < allsaloonlist.size(); j++) {
                    Saloon saloon = allsaloonlist.get(j);
                    if (saloon.getArea().equalsIgnoreCase(txtKatargam.getText().toString())) {
                        locationList.add(saloon);
                    }
                }
                adapter = new SaloonAdapter(Dashboard.this, locationList);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }
        });

        txtVesu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int j = 0; j < allsaloonlist.size(); j++) {
                    Saloon saloon = allsaloonlist.get(j);
                    if (saloon.getArea().equalsIgnoreCase(txtVesu.getText().toString())) {
                        locationList.add(saloon);
                    }
                }
                adapter = new SaloonAdapter(Dashboard.this, locationList);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }
        });


        txtAdajan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int j = 0; j < allsaloonlist.size(); j++) {
                    Saloon saloon = allsaloonlist.get(j);
                    if (saloon.getArea().equalsIgnoreCase(txtAdajan.getText().toString())) {
                        locationList.add(saloon);
                    }
                }
                adapter = new SaloonAdapter(Dashboard.this, locationList);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void getUser() {
        boolean isUser = AppUtils.getValue(Dashboard.this, Constant.ISUSER);
        String userId = AppUtils.getStringValue(Dashboard.this, Constant.USERID);
        if (isUser) {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    AppUtils.storeUser(Dashboard.this, Constant.ARGS_USER, user);
//                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void getIntentData(Intent intent) {
        value = intent.getStringExtra(Constant.ARGS_SALOON);
        action = intent.getAction();
        if (value.equals(Constant.USER)) {
            Toast.makeText(this, "User Login", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "Saloon Login", Toast.LENGTH_SHORT).show();
            if (value.equalsIgnoreCase("login")) {

            } else {


            }
        }
    }

    private void getData() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saloons");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    final String saloonName = datas.child("SaloonName").getValue().toString();
                    //String id = datas.child("id").getValue().toString();
                    final String address = datas.child("SaloonAddress").getValue().toString();
                    final String mobile = datas.child("Mobile").getValue().toString();

                    String workingHr = "";
                    if (datas.hasChild("Workinghours")) {
                        workingHr = datas.child("Workinghours").child("OpenTime").getValue().toString() + " TO " +
                                datas.child("Workinghours").child("CloseTime").getValue().toString();
                    }
                    final String area = datas.child("Area").getValue().toString();
                    final String email = datas.child("Email").getValue().toString();
                    final String password = datas.child("Password").getValue().toString();
                    String ext = datas.child("ext").getValue().toString();
                    ext = ext.replace("image/", "");
                    final String id = datas.getKey();

                    DataSnapshot ratting = datas.child("Reviews");
                    for (DataSnapshot snapshot : ratting.getChildren()) {
                        i = i + 1;
                        rat = rat + Float.parseFloat(snapshot.child("ratting").getValue().toString());
                    }

//                    DataSnapshot services = datas.child("Services");
//                    for (DataSnapshot snapshot : services.getChildren()) {
//                        i = i + 1;
//                    }

                    if (i > 0) {
                        rat = rat / i;
                    } else {
                        rat = 0;
                    }

                    if (!action.equalsIgnoreCase("login")) {

                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saloons").child(id).child("Services");
                        final String finalWorkingHr = workingHr;
                        final String finalExt = ext;
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                    //saloonList = new ArrayList<>();
                                    String name = datas.child("name").getValue().toString();
                                    String price = datas.child("price").getValue().toString();
                                    String type = datas.child("type").getValue().toString();

                                    Services services = new Services();
                                    services.setType(type);
                                    services.setPrice(price);
                                    services.setName(name);

                                    if (name.equalsIgnoreCase(value)) {
                                        Saloon saloon = new Saloon(saloonName, address, mobile, finalWorkingHr, area, email, password, "", finalExt);
                                        saloon.setAvgRating(String.valueOf(rat));
                                        saloon.setId(id);
                                        rdSaloonList.add(saloon);
                                        //allsaloonlist.add(saloon);
                                    }

                                }

                                adapter = new SaloonAdapter(Dashboard.this, rdSaloonList);
                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    Saloon saloon = new Saloon(saloonName, address, mobile, workingHr, area, email, password, "", ext);
                    saloon.setAvgRating(String.valueOf(rat));
                    saloon.setId(id);
                    saloonList.add(saloon);
                    allsaloonlist.add(saloon);

                }

                adapter = new SaloonAdapter(Dashboard.this, saloonList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_request, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.appment:
                Intent intent = new Intent(Dashboard.this, AppoitmentActivity.class);
                startActivity(intent);
                //ShowAppMent();
                return true;
            case R.id.armod:
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.arcoresample");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                } else {
                    Toast.makeText(Dashboard.this, "There is no package available in android", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ShowAppMent() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("R_Request").child(User_id);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot datas) {
                ArrayList<Services> servicesList = null;
                String uid = datas.getKey();
//                Request request = new Request();
//                request.setAmount(datas.child("Amount").getValue().toString());
//                request.setAppointment_Time(datas.child("AppointmentTime").getValue().toString());
//                request.setPay_Method(datas.child("PayMethod").getValue().toString());
//                request.setPay_Status(datas.child("PayStatus").getValue().toString());
//                request.setStatus(datas.child("Status").getValue().toString());
//                request.setSaloon_Id(datas.child("salon_id").getValue().toString());

                for (DataSnapshot data : datas.getChildren()) {

                    Request request = data.getValue(Request.class);
                    requests.add(request);

//                    servicesList = new ArrayList<>();
//                    String name = data.child("name").getValue().toString();
//                    String price = data.child("price").getValue().toString();
//                    String type = data.child("type").getValue().toString();
//
//                    Services services = new Services();
//                    services.setType(type);
//                    services.setPrice(price);
//                    services.setName(name);
//
//                    servicesList.add(services);

                }

                Log.e("TAGTAG", "  " + requests.size() + " ");
                //request.setService(servicesList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Dashboard.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addValueEventListener(postListener);
    }


    private void rjjundsideShowAppMent() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("R_Request");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot datas) {
                ArrayList<Services> servicesList = null;
                String uid = datas.getKey();
//                Request request = new Request();
//                request.setAmount(datas.child("Amount").getValue().toString());
//                request.setAppointment_Time(datas.child("AppointmentTime").getValue().toString());
//                request.setPay_Method(datas.child("PayMethod").getValue().toString());
//                request.setPay_Status(datas.child("PayStatus").getValue().toString());
//                request.setStatus(datas.child("Status").getValue().toString());
//                request.setSaloon_Id(datas.child("salon_id").getValue().toString());

                for (DataSnapshot data : datas.getChildren()) {

                    Request request = data.getValue(Request.class);
                    //request.getSaloon_Id() = apputils.logged
                    requests.add(request);
                }

                Log.e("TAGTAG", "  " + requests.size() + " ");
                //request.setService(servicesList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Dashboard.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addValueEventListener(postListener);


        // rejected to close
        //Request request = getFrom Firebase as saloon side;
        //request.setAccpted(true or false);

        //mDatabase.child("R_Request").child(request.getUserId()).child(request.getKey()).setValue(request);
    }

}
