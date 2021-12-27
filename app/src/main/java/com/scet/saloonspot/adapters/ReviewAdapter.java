package com.scet.saloonspot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scet.saloonspot.R;
import com.scet.saloonspot.models.Review;
import com.scet.saloonspot.models.Services;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    Context context;
    ArrayList<Review> list = new ArrayList<>();

    public ReviewAdapter(Context context, ArrayList<Review> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_all_reviewscard,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        Review review = list.get(position);
        holder.txtRating.setText(review.getRatting());
        holder.txtReview.setText(review.getReview());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtReview,txtRating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReview = itemView.findViewById(R.id.txtReview);
            txtRating = itemView.findViewById(R.id.txtRating);
        }
    }
}
