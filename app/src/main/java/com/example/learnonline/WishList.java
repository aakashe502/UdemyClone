package com.example.learnonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.learnonline.Adapter.AdapterWishListed;
import com.example.learnonline.Models.ItemData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WishList extends AppCompatActivity {
    private String timestamp,type;
    private RecyclerView recyclerView;
    AdapterWishListed adapterWishListed;
    private ArrayList<ItemData> itemData1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        itemData1=new ArrayList<>();
        bottomNavigationView.setSelectedItemId(R.id.wishlist);

        recyclerView=findViewById(R.id.recyclerview);
        adapterWishListed=new AdapterWishListed(this,itemData1);
        recyclerView.setAdapter(adapterWishListed);
        getDatas();

        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapterWishListed=new AdapterWishListed(this,itemData1);
        recyclerView.setAdapter(adapterWishListed);


        Log.i("itemdata ye wala ",String.valueOf(itemData1));


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case  R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case  R.id.nav_search:
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case  R.id.nav_account:
                        startActivity(new Intent(getApplicationContext(),Account.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.wishlist:
                        startActivity(new Intent(getApplicationContext(),WishList.class));
                        overridePendingTransition(0,0);
                        return true;
                    case  R.id.accessibility:
                        startActivity(new Intent(getApplicationContext(),MyCourses.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });



    }



    private void putData(String a,String b) {
        Log.i("" ,a+" "+b);


    }


    private void getDatas() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(FirebaseAuth.getInstance().getUid().toString()).child("WishListed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    timestamp=""+ds.child("timestamp").getValue().toString();
                    type=""+ ds.child("type").getValue().toString();
                    Log.i("timestamp = ",timestamp+" "+type);

                      DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
                      databaseReference.child("TCourses").child(type.trim()).child("panel").child(timestamp.trim()).addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                              Toast.makeText(WishList.this,"hello there",Toast.LENGTH_SHORT).show();
                              ItemData itemData=snapshot.child("INFO").getValue(ItemData.class);
                              itemData1.add(itemData);
                              Log.i("this time is ",""+itemData.toString());
                              adapterWishListed.notifyDataSetChanged();
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError error) {

                          }
                      });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void layoutAnimation(RecyclerView recyclerView){
        Context context=recyclerView.getContext();
        LayoutAnimationController layoutAnimationController =
                AnimationUtils.loadLayoutAnimation(context,R.anim.layout_slideleft);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,HomeActivity.class));
    }
}
