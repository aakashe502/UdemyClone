package com.example.learnonline.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.learnonline.Activities.MoreCourseActivity;
import com.example.learnonline.Models.ItemData;
import com.example.learnonline.Models.ModelList;
import com.example.learnonline.R;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;
import java.util.List;

public class TcourseAdapter  extends RecyclerView.Adapter<TcourseAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ModelList> datalist;

    public TcourseAdapter(Context context,ArrayList<ModelList> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder,final int position) {

        holder.cattitle.setText(datalist.get(position).getHeader_title());



        MyCOurseAdapter myCOurseAdapter=new MyCOurseAdapter(context,datalist.get(position).getListItem());
        holder.recyclerView1.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        holder.recyclerView1.setLayoutManager(linearLayoutManager);
        holder.recyclerView1.setAdapter(myCOurseAdapter);
        holder.recyclerView1.setNestedScrollingEnabled(false);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,MoreCourseActivity.class);
                intent.putExtra("type",""+datalist.get(position).getListItem().get(0).getType());
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public  class  MyViewHolder extends RecyclerView.ViewHolder{
        private TextView cattitle;
        private RecyclerView recyclerView1;
        private ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cattitle=itemView.findViewById(R.id.cat_title);
            recyclerView1=itemView.findViewById(R.id.item_recycler);
            imageView=itemView.findViewById(R.id.more);
        }
    }
}
