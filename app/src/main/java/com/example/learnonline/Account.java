package com.example.learnonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.learnonline.Adapter.MyRecycler;
import com.example.learnonline.COnstCalues.Constants;
import com.example.learnonline.COnstCalues.picklogout;
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
import java.util.List;

public class Account extends AppCompatActivity {
   // private TextView switchmode;
    private FirebaseAuth firebaseAuth;
    private TextView count,nameTv,nocourses;
    private ImageView logout;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private ArrayList<ItemData> arrayList;
    MyRecycler myRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        count=findViewById(R.id.count);
        logout=findViewById(R.id.logout);
        bottomNavigationView.setSelectedItemId(R.id.nav_account);
        firebaseAuth=FirebaseAuth.getInstance();
        arrayList=new ArrayList<>();
        recyclerView=findViewById(R.id.myrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
         myRecycler=new MyRecycler(this,arrayList);
        recyclerView.setAdapter(myRecycler);


    //image2=findViewById(R.id.imageview2);
    nocourses=findViewById(R.id.test);
                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Account.this);
                        builder.setTitle("Move to")
                                .setItems(picklogout.choose,new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface,int i) {
                                        //get pick up
                                        String category=Constants.productCategories[i];
                                        //set pick up
                                       if(category.equals("Logout")){
                                           firebaseAuth.signOut();
                                       }
                                       else{
                                           startActivity(new Intent(Account.this,InstructMode.class));
                                       }

                                    }
                                })
                                .show();

                    }
                });
        imageView=findViewById(R.id.profile_image2);
        nameTv=findViewById(R.id.nametv);
      //  Glide.with(this).load(R.drawable.no_courses).placeholder(R.drawable.ic_person_gray).into(image2);

//        Picasso.get().load(R.drawable.no_courses).placeholder(R.drawable.ic_person_gray).into(image2);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
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

//        ImageSlider imageSlider = findViewById(R.id.slider);
//        List<SlideModel> slideModels = new ArrayList<>();
//
//        slideModels.add(new SlideModel(R.drawable.brochure,"dictionary",ScaleTypes.CENTER_CROP));
//        slideModels.add(new SlideModel(R.drawable.namecard,"take your learning to next level",ScaleTypes.CENTER_CROP));
//        slideModels.add(new SlideModel(R.drawable.poster,"there you go",ScaleTypes.CENTER_CROP));
//        slideModels.add(new SlideModel(R.drawable.sticker,"lets go",ScaleTypes.CENTER_CROP));
//        slideModels.add(new SlideModel(R.drawable.chic5,"eat",ScaleTypes.CENTER_CROP));
//        slideModels.add(new SlideModel(R.drawable.chic6,"eat 1",ScaleTypes.CENTER_CROP));
//        imageSlider.setImageList(slideModels,ScaleTypes.CENTER_CROP);
//        imageSlider.stopSliding();

       // switchmode=findViewById(R.id.switchmode);
        loadNAme();
        getChildCount();
//        switchmode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Account.this,InstructMode.class));
//            }
//        });

//        if(count.getText().toString() =="0"){
//            nocourses.setVisibility(View.GONE);
//            image2.setVisibility(View.GONE);
//            imageSlider.setVisibility(View.VISIBLE);
//
//        }
//        else{
//            nocourses.setVisibility(View.VISIBLE);
//            image2.setVisibility(View.VISIBLE);
//            imageSlider.setVisibility(View.GONE);
//
//        }
    }

    private void getChildCount() {
       // Toast.makeText(this,"getting childcount",Toast.LENGTH_SHORT).show();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count.setText(String.valueOf(snapshot.child("Courses").getChildrenCount()));
                for(DataSnapshot ds:snapshot.child("Courses").getChildren()){
                    ItemData itemData=ds.child("INFO").getValue(ItemData.class);
                    arrayList.add(itemData);
                }
                recyclerView.setAdapter(myRecycler);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadNAme() {
       // Toast.makeText(this,"getting name",Toast.LENGTH_SHORT).show();
        DatabaseReference db=FirebaseDatabase.getInstance().getReference("Users");
        db.child(firebaseAuth.getUid())
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                String pic="",name="";
//              if(snapshot1.hasChild("profile")){
//                  pic=snapshot1.child("profile").getValue().toString();
//              }
//              else{
//                  pic="";
//              }
//              if(snapshot1.hasChild("name")){
//                  name=""+snapshot1.child("name").getValue().toString();
//              }
//              else{
//                  name="BOB MArley";
//              }
                name=snapshot1.child("name").getValue().toString();
                pic=snapshot1.child("profile").getValue().toString();
                try {
                    Picasso.get().load(pic).placeholder(R.drawable.ic_person_gray).into(imageView);
                }
                catch (Exception e){
                    Picasso.get().load(R.drawable.ic_person_white).placeholder(R.drawable.ic_person_gray).into(imageView);
                }

                nameTv.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
