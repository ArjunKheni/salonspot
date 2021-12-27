package com.scet.saloonspot;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scet.saloonspot.adapters.ServiceAdapter;
import com.scet.saloonspot.models.Request;
import com.scet.saloonspot.models.Saloon;
import com.scet.saloonspot.models.Services;
import com.scet.saloonspot.models.User;
import com.scet.saloonspot.utils.AppUtils;
import com.scet.saloonspot.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SaloonViewActivity extends AppCompatActivity implements ServiceAdapter.onServiceAdd {

    String User_id;
    TextView sname;
    TextView semail;
    TextView saddress;
    TextView saloonarea;
    TextView smobile;
    TextView workinghr;
    TextView totalamt;
    ImageView saloonphoto;
    LinearLayout llTotal;
    Button btn_date,btn_time;
    Button bookNow;
    FloatingActionButton addreview;
    int image;
    String name, email, mobile, area, add, id, workhr;
    RecyclerView ServicerecyclerView;
    ArrayList<Services> servicesList = new ArrayList<>();
    ServiceAdapter adapter;
    ProgressDialog progressDialog;
    // Reqest mgmt
    ArrayList<Services> selectServicesList = new ArrayList<>();
    int total;
    AlertDialog alertDialog = null;
     AlertDialog ad;
    private DatabaseReference mDatabase;
    final int UPI_PAYMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saloon_view);
        getIntentData(getIntent());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getinit();
        }
        getData();

    }

    private void getIntentData(Intent intent) {
        name = intent.getStringExtra("sname");
        email = intent.getStringExtra("semail");
        add = intent.getStringExtra("sadd");
        mobile = intent.getStringExtra("smobile");
        workhr = intent.getStringExtra("workingHr");
        area = intent.getStringExtra("sarea");
        id = intent.getStringExtra("id");
        image = intent.getIntExtra("logo", 0);
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
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    String name = datas.child("name").getValue().toString();
                    String price = datas.child("price").getValue().toString();
                    String type = datas.child("type").getValue().toString();

                    Services services = new Services();
                    services.setType(type);
                    services.setPrice(price);
                    services.setName(name);

                    servicesList.add(services);

                }
                adapter = new ServiceAdapter(SaloonViewActivity.this, servicesList, "user", SaloonViewActivity.this);
                ServicerecyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getinit() {
        sname = findViewById(R.id.sname);
        semail = findViewById(R.id.semail);
        saddress = findViewById(R.id.saddress);
        saloonarea = findViewById(R.id.saloonarea);
        smobile = findViewById(R.id.smobile);
        workinghr = findViewById(R.id.workinghr);
        saloonphoto = findViewById(R.id.saloonphoto);
        llTotal = findViewById(R.id.llTotal);
        totalamt = findViewById(R.id.totalamt);
        addreview = findViewById(R.id.addreview);
        bookNow = findViewById(R.id.booknow);
        ServicerecyclerView = findViewById(R.id.ServicerecyclerView);
        semail.setText(email);
        sname.setText(name);
        saddress.setText(add);
        saloonarea.setText(area);
        smobile.setText(mobile);
        workinghr.setText(workhr);
        saloonphoto.setImageDrawable(getDrawable(image));
        ServicerecyclerView.setLayoutManager(new LinearLayoutManager(this));

        User_id = AppUtils.getStringValue(SaloonViewActivity.this, Constant.USERID);

        addreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaloonViewActivity.this, Showreviewactivity.class);
                intent.putExtra(Constant.ARGS_SALOON, id);
                startActivity(intent);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        bookNow.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showCustomDialog() {
        ViewGroup viewGroup = findViewById(R.id.content);
        View view = LayoutInflater.from(this).inflate(R.layout.bookappment_dialog, viewGroup, false);

        Button bookNow = view.findViewById(R.id.save);
        Button cancel = view.findViewById(R.id.cancel);

        btn_date = view.findViewById(R.id.btn_date);
        btn_time = view.findViewById(R.id.btn_time);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

        final Calendar c = Calendar.getInstance();

        btn_date.setText(new SimpleDateFormat("dd MMM yyyy").format(c.getTime()));
        btn_time.setText(new SimpleDateFormat("HH:mm").format(c.getTime()));

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SaloonViewActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                btn_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

            }
        });

        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(SaloonViewActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                btn_time.setText(hourOfDay + ":" + minute);
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(SaloonViewActivity.this);
                alertDialog1.setTitle("Payment Method");
                final String[] items = {"Cash", "UPI"};
                final int checkedItem = -1;

                alertDialog1.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                final String key = mDatabase.push().getKey();
                                User user = AppUtils.getUser(SaloonViewActivity.this, Constant.ARGS_USER);
                                Saloon saloon = new Saloon(name, add, mobile, workhr, area, email, "", "", "");
                                Request request = new Request();
                                request.setService(selectServicesList);
                                request.setSaloon_Id(id);
                                request.setStatus("Pending");
                                request.setPay_Method("Cash");
                                request.setAppointment_Time(btn_date.getText() + "  " + btn_time.getText());
                                request.setAmount(total + "");
                                request.setPay_Status("P_Cash");
                                request.setAccepted(false);
                                request.setUserID(User_id);
                                request.setKey(key);
                                request.setSaloon(saloon);
                                request.setUser(user);
                                mDatabase.child("R_Request").child(User_id).child(key).setValue(request);
                                break;
                            case 1:
				//replace with your up id 
                                String upiId="test@ybl";
                                String upiname="Salon Spot";
                                String note="Payment for Booking Service";
                                payUsingUpi(total, upiId, upiname, note);
                                break;
                        }
                    }
                });
                alertDialog1.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ad.dismiss();

                    }
                });

                ad = alertDialog1.show();


            }

            private void payUsingUpi(int total, String upiId, String upiname, String note) {
                ad.dismiss();
                Uri uri = Uri.parse("upi://pay").buildUpon()
                        .appendQueryParameter("pa", upiId)
                        .appendQueryParameter("pn", upiname)
                        .appendQueryParameter("tn", note)
                        .appendQueryParameter("am", String.valueOf(total))
                        .appendQueryParameter("cu", "INR")
                        .build();


                Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
                upiPayIntent.setData(uri);

                // will always show a dialog to user to choose an app
                Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

                // check if intent resolves
                if(null != chooser.resolveActivity(getPackageManager())) {
                    startActivityForResult(chooser, UPI_PAYMENT);
                } else {
                    Toast.makeText(SaloonViewActivity.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(SaloonViewActivity.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                final String key = mDatabase.push().getKey();
                User user = AppUtils.getUser(SaloonViewActivity.this, Constant.ARGS_USER);
                Saloon saloon = new Saloon(name, add, mobile, workhr, area, email, "", "", "");
                Request request = new Request();
                request.setService(selectServicesList);
                request.setSaloon_Id(id);
                request.setStatus("Pending");
                request.setPay_Method("UPI");
                request.setAppointment_Time(btn_date.getText() + "  " + btn_time.getText());
                request.setAmount(total + "");
                request.setPay_Status("Confirmed");
                request.setAccepted(false);
                request.setUserID(User_id);
                request.setKey(key);
                request.setSaloon(saloon);
                request.setUser(user);
                mDatabase.child("R_Request").child(User_id).child(key).setValue(request);
                alertDialog.cancel();
                Toast.makeText(SaloonViewActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();

                Log.d("UPI", "responseStr: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {

                final String key = mDatabase.push().getKey();
                User user = AppUtils.getUser(SaloonViewActivity.this, Constant.ARGS_USER);
                Saloon saloon = new Saloon(name, add, mobile, workhr, area, email, "", "", "");
                Request request = new Request();
                request.setService(selectServicesList);
                request.setSaloon_Id(id);
                request.setStatus("Pending");
                request.setPay_Method("UPI");
                request.setAppointment_Time(btn_date.getText() + "  " + btn_time.getText());
                request.setAmount(total + "");
                request.setPay_Status("user Cancel");
                request.setAccepted(false);
                request.setUserID(User_id);
                request.setKey(key);
                request.setSaloon(saloon);
                request.setUser(user);
                mDatabase.child("R_Request").child(User_id).child(key).setValue(request);
                Toast.makeText(SaloonViewActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();

            }
            else {

                final String key = mDatabase.push().getKey();
                User user = AppUtils.getUser(SaloonViewActivity.this, Constant.ARGS_USER);
                Saloon saloon = new Saloon(name, add, mobile, workhr, area, email, "", "", "");
                Request request = new Request();
                request.setService(selectServicesList);
                request.setSaloon_Id(id);
                request.setStatus("Pending");
                request.setPay_Method("UPI");
                request.setAppointment_Time(btn_date.getText() + "  " + btn_time.getText());
                request.setAmount(total + "");
                request.setPay_Status("Failed");
                request.setAccepted(false);
                request.setUserID(User_id);
                request.setKey(key);
                request.setSaloon(saloon);
                request.setUser(user);
                mDatabase.child("R_Request").child(User_id).child(key).setValue(request);
                Toast.makeText(SaloonViewActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(SaloonViewActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onServeiceAdd(Services services, boolean isRemove) {
        if (isRemove) {
            selectServicesList.remove(services);
            total = total - Integer.parseInt(services.getPrice());
        } else {
            selectServicesList.add(services);
            total = total + Integer.parseInt(services.getPrice());
        }
        llTotal.setVisibility(View.VISIBLE);
        totalamt.setText(String.valueOf(total));
    }

}
