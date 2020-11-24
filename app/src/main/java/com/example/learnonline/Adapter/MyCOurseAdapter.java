package com.example.learnonline.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnonline.CourseInfoActivity;
import com.example.learnonline.Models.ItemData;
import com.example.learnonline.Models.ModelList;
import com.example.learnonline.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyCOurseAdapter extends RecyclerView.Adapter<MyCOurseAdapter.MyViewHoll> {
    private Context context;
    private ArrayList<ItemData> itemData;

    public MyCOurseAdapter(Context context,ArrayList<ItemData> itemData) {
        this.context = context;
        this.itemData = itemData;
    }

    @NonNull
    @Override
    public MyViewHoll onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_rowhorizontal,parent,false);
        return new MyViewHoll(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHoll holder,final int position) {
        holder.sorname.setText(itemData.get(position).getTitle());
//        Log.i("son","title"+itemData.get(position).getTitle());
        try{
            Picasso.get().load(itemData.get(position).getImage()).placeholder(R.drawable.ic_person_gray).into(holder.item_image);
        }
        catch (Exception e){
            Picasso.get().load(R.drawable.ic_person_gray).placeholder(R.drawable.ic_person_gray).into(holder.item_image);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(context,CourseInfoActivity.class);
               intent.putExtra("cname",itemData.get(position).getImage());
               intent.putExtra("userid",""+itemData.get(position).getUserid());
               intent.putExtra("timestamp",""+itemData.get(position).getTimestamp());
               intent.putExtra("teacher",itemData.get(position).getDescription());
               intent.putExtra("heading",itemData.get(position).getTitle());
               context.startActivity(intent);
            }
        });
        holder.Price.setText(itemData.get(position).getPrice());
        holder.Price.setPaintFlags(holder.Price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.Discounted.setText(itemData.get(position).getDiscounted());


    }

    @Override
    public int getItemCount() {
        return (itemData!=null?itemData.size():0);
    }

    public  class  MyViewHoll extends RecyclerView.ViewHolder{
        private ImageView item_image;
        private TextView sorname;
        private TextView Price,Discounted;

        public MyViewHoll(@NonNull View itemView) {
            super(itemView);
            item_image=itemView.findViewById(R.id.item_image);
            sorname=itemView.findViewById(R.id.sorname);
            Price=itemView.findViewById(R.id.price);
            Discounted=itemView.findViewById(R.id.original);
        }
    }
}
