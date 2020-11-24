package com.example.learnonline.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.learnonline.Adapter.MoreAdapter;
import com.example.learnonline.Models.ItemData;
import com.example.learnonline.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MoreCourseActivity extends AppCompatActivity {
    private ArrayList<ItemData> arrayList;
    private RecyclerView recyclerView;
    MoreAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_course);
        recyclerView=findViewById(R.id.recycler);
        arrayList=new ArrayList<>();

        String s=getIntent().getStringExtra("type");
        Toast.makeText(this,""+s,Toast.LENGTH_SHORT).show();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("TCourses");
        databaseReference.child(s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds:snapshot.child("panel").getChildren()){
                ItemData itemData=ds.child("INFO").getValue(ItemData.class);
                Log.i("newitem",itemData.getDescription());
                arrayList.add(itemData);
            }
            adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter=new MoreAdapter(this,arrayList);
        recyclerView.setAdapter(adapter);
    }
}
