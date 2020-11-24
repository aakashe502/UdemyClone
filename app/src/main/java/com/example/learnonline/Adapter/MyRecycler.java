package com.example.learnonline.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnonline.Models.ItemData;
import com.example.learnonline.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyRecycler extends RecyclerView.Adapter<MyRecycler.ViewHolder> {
    private Context context;
    private ArrayList<ItemData> arrayList;

    public MyRecycler(Context context,ArrayList<ItemData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.myrecycler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        holder.price.setText(arrayList.get(position).getPrice());
        holder.original.setText(arrayList.get(position).getDiscounted());

        try{
            Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.drawable.ic_person_gray).into(holder.imageView);
        }
        catch (Exception e){
            Picasso.get().load(R.drawable.ic_person_white).placeholder(R.drawable.ic_person_gray).into(holder.imageView);

        }
        holder.original.setPaintFlags(holder.price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView price,original;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageview1);
            price=itemView.findViewById(R.id.price);
            original=itemView.findViewById(R.id.original);
        }
    }
}

