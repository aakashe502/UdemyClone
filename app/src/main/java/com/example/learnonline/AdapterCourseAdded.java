package com.example.learnonline;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnonline.Activities.EditCourseActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterCourseAdded extends RecyclerView.Adapter<AdapterCourseAdded.MyViewHolder> {

    private Context context;
    private ArrayList<ModelCourse> courseref;

    public AdapterCourseAdded(Context context,ArrayList<ModelCourse> courseref) {
        this.context = context;
        this.courseref = courseref;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.courseofinstructor,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder,int position) {
        ModelCourse modelCourse=courseref.get(position);
        String tit=modelCourse.getTitle();
        String desc=modelCourse.getDescription();
        String imageuri=modelCourse.getPicc();
        holder.title.setText(tit);
        holder.description.setText(desc);

        try {
            Picasso.get().load(imageuri).placeholder(R.drawable.ic_person_gray).into(holder.pic);
        }
        catch (Exception e){
            Picasso.get().load(imageuri).placeholder(R.drawable.ic_person_gray).into(holder.pic);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context,EditCourseActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return courseref.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView pic;
        private TextView title,description;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pic=itemView.findViewById(R.id.iconc);
            title=itemView.findViewById(R.id.titlec);
            description=itemView.findViewById(R.id.descriptionc);
        }
    }
}
