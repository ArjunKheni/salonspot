package com.scet.saloonspot.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.scet.saloonspot.R;
import com.scet.saloonspot.SaloonViewActivity;
import com.scet.saloonspot.models.Saloon;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import kotlin.jvm.internal.Intrinsics;

public class SaloonAdapter extends RecyclerView.Adapter<SaloonAdapter.ViewHolder> {

    int[] images = new int[]{R.drawable.s1,R.drawable.s2,R.drawable.s3,R.drawable.s4,R.drawable.s5};

    Context context;
    ArrayList<Saloon> list = new ArrayList<>();

    public SaloonAdapter(Context context,ArrayList<Saloon> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SaloonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SaloonAdapter.ViewHolder holder, final int position) {
        final Saloon saloon = list.get(position);

        holder.sarea.setText(saloon.getArea());
        holder.sname.setText(saloon.getName());
        holder.srating.setText(saloon.getAvgRating());
        Log.e("EXT", "  images/" + saloon.getName() + "." + saloon.getExt());

        int min = 0;
        int max = 4;

        Random r = new Random();
        final int i1 = r.nextInt(max - min + 1) + min;
        holder.sphoto.setImageDrawable(context.getDrawable(images[i1]));

        holder.btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SaloonViewActivity.class);
                intent.putExtra("sname",saloon.getName());
                intent.putExtra("semail",saloon.getEmail());
                intent.putExtra("sadd",saloon.getAddress());
                intent.putExtra("smobile",saloon.getMobileNo());
                intent.putExtra("workingHr",saloon.getWorkingHr());
                intent.putExtra("sarea",saloon.getArea());
                intent.putExtra("id",saloon.getId());
                intent.putExtra("logo",images[i1]);
                context.startActivity(intent);

            }
        });

//        StorageReference storageRef =
//                FirebaseStorage.getInstance().getReference();
//        storageRef.child("images/").child(saloon.getName() +"."+ saloon.getExt()).getDownloadUrl()
//                .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Glide.with(context).load(uri).into(holder.sphoto);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e("ERROR","  "+position + "  "+e.getLocalizedMessage());
//            }
//        });


//        FirebaseStorage storageDisplayImg;
//        StorageReference storageRef;
//        FirebaseAuth auth;
//
//        storageDisplayImg=FirebaseStorage.getInstance();
//        auth = FirebaseAuth.getInstance();
//        FirebaseUser userConnect = auth.getCurrentUser();
//        String id_user=userConnect.getUid();
//        storageRef = storageDisplayImg.getReference().child("  images/" + saloon.getName() + "." + saloon.getExt()); // return gs://mydreambook-32321.appspot.com/images/test23-03-2017_16:46:55
//
//            Glide.with(context)
//                    .load(storageRef)
//                    .into(holder.sphoto);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update(ArrayList<Saloon> saloonList) {
        this.list.clear();
        this.list = saloonList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView sname,sarea,srating;
        Button btnbook;
        ImageView sphoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnbook = itemView.findViewById(R.id.btnbook);
            sname = itemView.findViewById(R.id.sname);
            sarea = itemView.findViewById(R.id.sarea);
            sphoto = itemView.findViewById(R.id.sphoto);
            srating = itemView.findViewById(R.id.srating);

        }
    }
}

