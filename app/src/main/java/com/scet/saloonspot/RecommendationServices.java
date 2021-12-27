package com.scet.saloonspot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scet.saloonspot.adapters.ServiceAdapter;
import com.scet.saloonspot.models.Services;
import com.scet.saloonspot.utils.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendationServices extends AppCompatActivity {
    ServiceAdapter adapter;
    RecyclerView activelistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_services);
        getIntentData(getIntent());
        showrecommend();

    }

    String value;

    private void getIntentData(Intent intent) {
        value = intent.getStringExtra("ServiceType");
        //value = "new";
//        activelistview = findViewById(R.id.activelistview);
//        activelistview.setLayoutManager(new LinearLayoutManager(this));
    }

    ArrayList<Services> servicesArraylist = new ArrayList<>();
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayList<String> listItems2 = new ArrayList<String>();

    private void showrecommend() {

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Saloons");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datas) {

                for (DataSnapshot data : datas.getChildren()) {


                    for (DataSnapshot data2 : data.getChildren()) {
                        // do something with objec

                        if (data2.getKey().equalsIgnoreCase("Services")) {
//                             Services service = data2.getValue(Services.class);

                            for (DataSnapshot data3 : data2.getChildren()) {
                                Services service = data3.getValue(Services.class);

                                if (value.equalsIgnoreCase("new")) {
                                    listItems.add(service.getName().toLowerCase());

                                } else if (service.getType().equalsIgnoreCase(value)) {
//                                    Log.e("$$$", service.getName().toString());

                                    listItems.add(service.getName().toLowerCase());
//                                    boolean flg = false;
//                                    for (String object: listItems) {
//                                        if(service.getName().equalsIgnoreCase(object))
//                                        {
//                                            flg = true;
//                                        }
//                                    }
//
//                                    if(flg == false){
//                                        listItems.add(service.getName());
//                                    }


                                }
                            }


                        }
                    }


//                    if (service.getType().equalsIgnoreCase(value) && servicesArraylist.size() < 3) {
//                        servicesArraylist.add(service);
//                    }
//                    Log.e("REC-Data", servicesArraylist.toString());


//                    adapter = new ServiceAdapter(RecommendationServices.this, servicesArraylist, "user", null);
//                    activelistview.setAdapter(adapter);
                }


                Log.e("###", mostCommon(listItems)); //1
                listItems2.add(mostCommon(listItems));
                listItems.removeAll(Collections.singleton(mostCommon(listItems)));
                Log.e("###", mostCommon(listItems)); //2
                listItems2.add(mostCommon(listItems));

                listItems.removeAll(Collections.singleton(mostCommon(listItems)));
                Log.e("###", mostCommon(listItems)); //3
                listItems2.add(mostCommon(listItems));


//                for(String object : listItems2){
//                    Log.e("$$$$$$$$$$$$",object);
//                }

adapterbind();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void adapterbind() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_recommendation_listview, R.id.label,listItems2);
        ListView listView = (ListView) findViewById(R.id.recservicename);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RecommendationServices.this, Dashboard.class);
                intent.setAction("reco");
                intent.putExtra(Constant.ARGS_SALOON, listItems2.get(i));
                startActivity(intent);
            }
        });
    }

    public static <T> T mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Map.Entry<T, Integer> max = null;

        for (Map.Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max.getKey();
    }


}





