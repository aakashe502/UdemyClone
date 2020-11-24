package com.example.learnonline;

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

import com.example.learnonline.Activities.SubjectActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterCats extends RecyclerView.Adapter<AdapterCats.MyViewHolder> {
    private ArrayList<String> categ = new ArrayList<>();
    Context context;
    private ArrayList<Integer> images = new ArrayList<>();



    public AdapterCats(ArrayList<String> categ,Context context,ArrayList<Integer> images) {
        this.categ = categ;
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rec_rowsearch,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,final int position) {
        holder.nt.setText(categ.get(position));
        holder.imageView.setImageResource(images.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,SubjectActivity.class);
                intent.putExtra("name",categ.get(position));
                context.startActivity(intent);
            }
        });

        //Picasso.get().load(images.get(position)).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return categ.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        private TextView nt;
        private ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nt=itemView.findViewById(R.id.cats);
            imageView=itemView.findViewById(R.id.imagecats);
        }
    }
}
