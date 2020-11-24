package com.example.learnonline.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnonline.Activities.MoreCourseActivity;
import com.example.learnonline.CourseInfoActivity;
import com.example.learnonline.Models.ItemData;
import com.example.learnonline.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ItemData> arrayList;

    public MoreAdapter(Context context,ArrayList<ItemData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.more_recycler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        holder.textView.setText(arrayList.get(position).getTitle());
        holder.original.setText(arrayList.get(position).getPrice());
        holder.price.setText(arrayList.get(position).getDiscounted());
        holder.price.setPaintFlags(holder.price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        try{
            Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.drawable.ic_person_gray).into(holder.imageView);
        }
        catch (Exception e){
            Picasso.get().load(R.drawable.ic_person_white).placeholder(R.drawable.ic_person_gray).into(holder.imageView);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,CourseInfoActivity.class);
                intent.putExtra("cname",arrayList.get(position).getImage());
                intent.putExtra("userid",arrayList.get(position).getUserid());
                intent.putExtra("timestamp",arrayList.get(position).getTimestamp());
                intent.putExtra("teacher",arrayList.get(position).getUserid());
                intent.putExtra("heading",arrayList.get(position).getTitle());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textView,start,price,original;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            textView=itemView.findViewById(R.id.topicname);
            start=itemView.findViewById(R.id.start);
            price=itemView.findViewById(R.id.price);
            original=itemView.findViewById(R.id.original);

        }
    }
}
