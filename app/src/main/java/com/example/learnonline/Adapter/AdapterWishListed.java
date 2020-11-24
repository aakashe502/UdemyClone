package com.example.learnonline.Adapter;

import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnonline.CourseInfoActivity;
import com.example.learnonline.Models.ItemData;
import com.example.learnonline.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class AdapterWishListed  extends RecyclerView.Adapter<AdapterWishListed.ViewHolder> {
    private Context context;

    private ArrayList<ItemData> itemData;

    public AdapterWishListed(Context context,ArrayList<ItemData> itemData) {
        this.context = context;
        this.itemData = itemData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.new_wishlisted,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {

        Log.i("tag not shown",""+(itemData.get(position).getTitle()));
        holder.studentnum.setText(itemData.get(position).getStudentnumber());
        holder.title.setText(itemData.get(position).getTitle());
        holder.rating.setNumStars(Integer.parseInt(itemData.get(position).getRating().toString()));
        holder.instructor.setText(itemData.get(position).getUserid());
        holder.ratingnum.setText(itemData.get(position).getRating());
        holder.original.setText(itemData.get(position).getDiscounted());
        holder.price.setText(itemData.get(position).getPrice());

        try{
            Picasso.get().load(itemData.get(position).getImage()).placeholder(R.drawable.ic_person_gray).into(holder.image);
        }
        catch (Exception e){
            Picasso.get().load(R.drawable.ic_person_white).placeholder(R.drawable.ic_person_gray).into(holder.image);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,CourseInfoActivity.class);
                intent.putExtra("userid",""+itemData.get(position).getUserid());
                intent.putExtra("timestamp",""+itemData.get(position).getTimestamp());
                intent.putExtra("cname",""+itemData.get(position).getImage());
                intent.putExtra("teacher",itemData.get(position).getUserid());
                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView title,instructor,ratingnum,
                studentnum,price,original;
        private RatingBar rating;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            image=itemView.findViewById(R.id.image);
            instructor=itemView.findViewById(R.id.instructor);
            rating=itemView.findViewById(R.id.rating);
            ratingnum=itemView.findViewById(R.id.ratingnum);
            studentnum=itemView.findViewById(R.id.studentenroll);
            original=itemView.findViewById(R.id.original);
            price=itemView.findViewById(R.id.price);

        }
    }
}
