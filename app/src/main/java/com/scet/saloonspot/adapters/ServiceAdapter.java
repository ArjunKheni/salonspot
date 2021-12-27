package com.scet.saloonspot.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scet.saloonspot.R;
import com.scet.saloonspot.models.Services;
import com.scet.saloonspot.utils.Constant;

import java.util.ArrayList;

import static android.graphics.Color.WHITE;
import static android.graphics.Color.YELLOW;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    Context context;
    ArrayList<Services> list = new ArrayList<>();
    String value;
    int selectedPosition=-1;
    onServiceAdd listener;
    public ServiceAdapter(Context context,ArrayList<Services> list,String value,onServiceAdd listener){
        this.context = context;
        this.list = list;
        this.value = value;
        this.listener  = listener;
    }

    public interface onServiceAdd{
        void onServeiceAdd(Services services, boolean isRemove);
    }

    @NonNull
    @Override
    public ServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activelist_card,null,false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ServiceAdapter.ViewHolder holder, final int position) {
        final Services services = list.get(position);
        holder.txtPrice.setText(services.getPrice());
        holder.txtServicename.setText(services.getName());

        if (services.isSelected()){
            holder.btnadd.setText("Remove");
            holder.btnadd.setTextColor(WHITE);
            holder.btnadd.setBackgroundResource(R.drawable.activebutton);
        } else if(!services.isSelected()){
            holder.btnadd.setText("Add");
            holder.btnadd.setTextColor(R.color.background_color);
            holder.btnadd.setBackgroundResource(R.drawable.loginbutton_selector);
        }
        else
        {

        }

//        if (!services.isSelected()){
//            holder.btnadd.setText("Add");
//            holder.btnadd.setBackgroundResource(R.drawable.loginbutton_selector);
//        } else {
//            holder.btnadd.setText("Remove");
//            holder.btnadd.setBackgroundResource(R.drawable.activebutton);
//            holder.btnadd.setTextColor(WHITE);
//        }

//        if(selectedPosition==position) {
//            holder.btnadd.setText("Added");
//            holder.btnadd.setEnabled(false);
//        } else {
//            holder.btnadd.setText("Add");
//        }

        holder.btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  selectedPosition=position;
                boolean isRemove = false;
                if (holder.btnadd.getText().equals("Remove")){
                    isRemove = true;
                    services.setSelected(false);
                    holder.btnadd.setText("Add");
                } else {
                    isRemove = false;
                    services.setSelected(true);
                    holder.btnadd.setText("Remove");
                }

                notifyDataSetChanged();
                listener.onServeiceAdd(services,isRemove);
            }
        });
        switch (services.getType()){
            case Constant.Hair:
                holder.sphoto.setImageDrawable(context.getDrawable(R.drawable.hair));
                break;
            case Constant.Spa:
                holder.sphoto.setImageDrawable(context.getDrawable(R.drawable.spa));
                break;
            case Constant.Facial:
                holder.sphoto.setImageDrawable(context.getDrawable(R.drawable.facial));
                break;
            case Constant.Nails:
                holder.sphoto.setImageDrawable(context.getDrawable(R.drawable.nails));
                break;
            case Constant.Skin:
                holder.sphoto.setImageDrawable(context.getDrawable(R.drawable.skin));
                break;
        }

        if (value.equals("user")){
            holder.llAdd.setVisibility(View.VISIBLE);
            holder.llEditDelete.setVisibility(View.GONE);
        } else if (value.equals("saloon")){
            holder.llAdd.setVisibility(View.GONE);
            holder.llEditDelete.setVisibility(View.GONE);
        } else if (value.equals("app")){
            holder.llAdd.setVisibility(View.GONE);
            holder.llEditDelete.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtServicename;
        TextView txtPrice;
        ImageView sphoto;
        Button btnadd;
        LinearLayout llAdd,llEditDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtServicename = itemView.findViewById(R.id.txtServicename);
            sphoto = itemView.findViewById(R.id.sphoto);
            llAdd = itemView.findViewById(R.id.llAdd);
            llEditDelete = itemView.findViewById(R.id.llEditDelete);
            btnadd = itemView.findViewById(R.id.btnadd);

        }
    }
}
