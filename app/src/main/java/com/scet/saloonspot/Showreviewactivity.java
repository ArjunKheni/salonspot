package com.scet.saloonspot;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scet.saloonspot.adapters.ReviewAdapter;
import com.scet.saloonspot.adapters.ServiceAdapter;
import com.scet.saloonspot.models.Review;
import com.scet.saloonspot.models.Services;
import com.scet.saloonspot.utils.AppUtils;
import com.scet.saloonspot.utils.Constant;

import java.util.ArrayList;

public class Showreviewactivity extends AppCompatActivity {
    EditText edtreview;
    Button btnaddreview;
    RecyclerView reviewrecyclerview;
    RatingBar rtbreview;
    String id = "";
    LinearLayout login_layout;
    private DatabaseReference mDatabase;
    boolean isReviewed = false;
    ProgressDialog progressDialog;
    ArrayList<Review> reviewsList = new ArrayList<>();
    ReviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showreviewactivity);
        id = getIntent().getStringExtra(Constant.ARGS_SALOON);
        progressDialog = new ProgressDialog(this);
        getInit();
        getData();
        isReviewed = AppUtils.getValue(this,Constant.ISREVIEWED);
        if (isReviewed){
            login_layout.setVisibility(View.GONE);
        } else {
            login_layout.setVisibility(View.VISIBLE);
        }

    }

    private void getInit() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        edtreview = findViewById(R.id.edtreview);
        btnaddreview = findViewById(R.id.btnaddreview);
        reviewrecyclerview = findViewById(R.id.reviewrecyclerview);
        login_layout = findViewById(R.id.login_layout);
        rtbreview = findViewById(R.id.rtbreview);
        reviewrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        btnaddreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Review review = new Review();
                review.setReview(edtreview.getText().toString());
                review.setRatting(String.valueOf(rtbreview.getRating()));
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                review.setUserName(currentFirebaseUser.getDisplayName());
                mDatabase.child("Saloons").child(id).child("Reviews").child(mDatabase.push().getKey()).setValue(review);
                edtreview.setText("");
                rtbreview.setNumStars(0);
                finish();
                AppUtils.setValue(Showreviewactivity.this,Constant.ISREVIEWED,true);
                Toast.makeText(getApplicationContext(),"Review Added Sucessfully",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting Data");
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saloons").child(id).child("Reviews");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    String rating = datas.child("ratting").getValue().toString();
                    String strReview = datas.child("review").getValue().toString();
                    String struser = datas.child("userName").getValue().toString();
                   //String type = datas.child("type").getValue().toString();

                    Review review = new Review();
                    review.setRatting(rating);
                    review.setReview(strReview);
                    reviewsList.add(review);
                }
                adapter = new ReviewAdapter(Showreviewactivity.this,reviewsList);
                reviewrecyclerview.setAdapter(adapter);
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
}
