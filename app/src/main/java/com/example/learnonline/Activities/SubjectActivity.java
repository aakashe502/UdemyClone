package com.example.learnonline.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.learnonline.Adapter.AdapterWishListed;
import com.example.learnonline.Models.ItemData;
import com.example.learnonline.R;
import com.example.learnonline.WishList;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SubjectActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private RecyclerView recyclerView;
    String str;
    private ArrayList<ItemData> itemData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        toolbar=findViewById(R.id.appbar);
        itemData=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerview);
        str =getIntent().getStringExtra("name");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int r=item.getItemId();
                if(r==R.id.favorite){
                    Toast.makeText(SubjectActivity.this,"favourite",Toast.LENGTH_SHORT).show();
                }
                if(r==R.id.search){
                    Toast.makeText(SubjectActivity.this,"search",Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        loadData();
    }

    private void loadData() {
          DatabaseReference reference=FirebaseDatabase.getInstance().getReference("TCourses");
          reference.child(str).addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  for(DataSnapshot p:snapshot.child("panel").getChildren()){
                      //     Log.i(TAG,"values in it "+p.child("INFO").child("title").getValue());
                      ItemData modelList = p.child("INFO").getValue(ItemData.class);
                      // Log.i("hi = ",""+p.child("INFO").getValue());
                     itemData.add(modelList);
                     Log.i("itemdata",modelList.getDescription());
                      // Log.i(TAG,"second snap "+p);
                      // ItemData modelList=p.getValue(ItemData.class);
                      // Log.i(TAG,"modellist is"+modelList);
                  }
                  AdapterWishListed adapterWishListed=new AdapterWishListed(SubjectActivity.this,itemData);
                  recyclerView.setAdapter(adapterWishListed);
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });
        }
}
