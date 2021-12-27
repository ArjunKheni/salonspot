package com.scet.saloonspot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scet.saloonspot.adapters.AppoitmentAdapater;
import com.scet.saloonspot.models.Request;
import com.scet.saloonspot.models.Services;
import com.scet.saloonspot.utils.AppUtils;
import com.scet.saloonspot.utils.Constant;

import java.security.Key;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppoitmentActivity extends AppCompatActivity {
    String User_id, highesttype;
    AppoitmentAdapater appoitmentAdapater;
    RecyclerView recyclerView;
    TextView noservice;
    String action = "";
    ImageButton recommend;
    String loginKey;
    static String key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoitment);
        action = getIntent().getAction();
        User_id = AppUtils.getStringValue(this, Constant.USERID);
        recyclerView = findViewById(R.id.req_recyclerview);
        recommend =findViewById(R.id.recommend);
        noservice = findViewById(R.id.noservice);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        if (action.equalsIgnoreCase("user")) {
        ShowAppMent();
        appoitmentAdapater = new AppoitmentAdapater(AppoitmentActivity.this, requests, loginKey);
        recyclerView.setAdapter(appoitmentAdapater);
//        if (key.equalsIgnoreCase(""))
//        {
//
//        }
//        else
//        {
//
//
//        }

        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),RecommendationServices.class);
           if(key.equalsIgnoreCase(""))
           {
               key = "new";
               intent.putExtra("ServiceType", key);
               startActivity(intent);
           }
           else
           {
               intent.putExtra("ServiceType", key);
               startActivity(intent);

           }


            }
        });
        }


    private DatabaseReference mDatabase;
    ArrayList<Request> requests = new ArrayList<>();

    private void ShowAppMent() {
        boolean isUser = AppUtils.getValue(AppoitmentActivity.this, Constant.ISUSER);
        loginKey = AppUtils.getStringValue(AppoitmentActivity.this, Constant.LoginKey);
        if (loginKey.equalsIgnoreCase(Constant.USER)) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("R_Request").child(User_id);
        } else {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("R_Request");
        }

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot datas) {

                String loginKey = AppUtils.getStringValue(AppoitmentActivity.this, Constant.LoginKey);
                if (loginKey.equalsIgnoreCase(Constant.USER)) {

                    ArrayList<String> servicesArraylist = new ArrayList<String>();

                    for (DataSnapshot data : datas.getChildren()) {

                        Request request = data.getValue(Request.class);
                        if (AppUtils.getValue(AppoitmentActivity.this, Constant.ISUSER) &&
                                request.getUserID().equals(User_id)) {
                            requests.add(request);
                            Log.e("TAGTAG", "  " + requests.size() + " ");
                        } else if (AppUtils.getValue(AppoitmentActivity.this, Constant.ISSALON) &&
                                request.getSaloon_Id().equals(User_id)) {
                            requests.add(request);
                        }
                        appoitmentAdapater = new AppoitmentAdapater(AppoitmentActivity.this, requests, loginKey);
                        recyclerView.setAdapter(appoitmentAdapater);
                        appoitmentAdapater.notifyDataSetChanged();

                    }

                    ArrayList<Request> servicelist = requests;
                    if (servicelist.size() > 0) {
                        for (int i = 0; i < requests.size(); i++) {
                                ArrayList<Services> temp = requests.get(i).getService();
                            if (temp.size() > 0) {
                                for (int j = 0; j < temp.size(); j++) {
                                    servicesArraylist.add(temp.get(j).getType());
                                }
                            }
                        }
                    }
                    Log.e("tag", "size" + servicesArraylist.size());
                    if(servicesArraylist.size()==0)
                    {
                        noservice.setVisibility(TextView.VISIBLE);
                        recyclerView.setVisibility(TextView.INVISIBLE);
                     Toast.makeText(AppoitmentActivity.this,"No Past Orders available",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        noservice.setVisibility(TextView.INVISIBLE);
                        recyclerView.setVisibility(TextView.VISIBLE);
                        getWordFrequencies(servicesArraylist);
                    }

                } else {
                    for (DataSnapshot data : datas.getChildren()) {
                        for (DataSnapshot dataSnapshot : data.getChildren()) {
                            Request request = dataSnapshot.getValue(Request.class);
                            if (AppUtils.getValue(AppoitmentActivity.this, Constant.ISUSER) &&
                                    request.getUserID().equals(User_id)) {
                                requests.add(request);
                                Log.e("TAGTAG", "  " + requests.size() + " ");
                            } else if (AppUtils.getValue(AppoitmentActivity.this, Constant.ISSALON) &&
                                    request.getSaloon_Id().equals(User_id)) {
                                requests.add(request);
                            }
                            appoitmentAdapater = new AppoitmentAdapater(AppoitmentActivity.this, requests, loginKey);
                            recyclerView.setAdapter(appoitmentAdapater);
                            appoitmentAdapater.notifyDataSetChanged();

                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AppoitmentActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addValueEventListener(postListener);

    }


    public static Map<String, Integer> getWordFrequencies(List<String> words) {
        Map<String, Integer> wordFrequencies = new LinkedHashMap<String, Integer>();
        if (words != null) {
            for (String word : words) {
                if (word != null) {
                    word = word.trim();
                    if (!wordFrequencies.containsKey(word)) {
                        wordFrequencies.put(word, 0);
                    }
                    int count = wordFrequencies.get(word);
                    wordFrequencies.put(word, ++count);
                }
            }
        }



    Map.Entry<String, Integer> entry = wordFrequencies.entrySet().iterator().next();


    key = entry.getKey();
    Integer value = entry.getValue();



        return wordFrequencies;
    }



}