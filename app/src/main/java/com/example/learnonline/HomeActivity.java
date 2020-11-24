package com.example.learnonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.learnonline.Adapter.TcourseAdapter;
import com.example.learnonline.Models.ItemData;
import com.example.learnonline.Models.ModelList;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

   private  String s="";
   TcourseAdapter adapter;
     ArrayList<ModelList> ListMain;
    private static final String TAG = "HomeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

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


        ImageSlider imageSlider = findViewById(R.id.slider);
        List<SlideModel> slideModels = new ArrayList<>();



        slideModels.add(new SlideModel(R.drawable.brochure,"dictionary",ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.namecard,"take your learning to next level",ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.poster,"there you go",ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.sticker,"lets go",ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.chic5,"eat",ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.chic6,"eat 1",ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(slideModels,ScaleTypes.CENTER_CROP);
        imageSlider.startSliding(3000);
        recyclerView=findViewById(R.id.recMAin);

        ListMain =new ArrayList<>();
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));





    adapter=new TcourseAdapter(HomeActivity.this,ListMain);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();



        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("TCourses");
        reference.addValueEventListener(new ValueEventListener() {
       @Override
       public void onDataChange(@NonNull DataSnapshot snapshot) {

           for(DataSnapshot ds:snapshot.getChildren()){
               if(ds.hasChild("title")){
                   if(ds.child("title").hasChild("header_title")){
                       s=ds.child("title").child("header_title").getValue().toString();
                   }
                   else{
                       s="Recent";
                   }
               }
               else{
                   s="top Courses";
               }


               String header_title=s;
               Log.i(TAG,"first snap = "+ds.child("title").child("header_title").getValue());
               ModelList modelLists=new ModelList();
                ArrayList<ModelList> a=new ArrayList<>();
                ArrayList<ItemData> h=new ArrayList<>();
               for(DataSnapshot p:ds.child("panel").getChildren()){
                  //     Log.i(TAG,"values in it "+p.child("INFO").child("title").getValue());
                       ItemData modelList = p.child("INFO").getValue(ItemData.class);
                      // Log.i("hi = ",""+p.child("INFO").getValue());
                       modelList.setTitle(p.child("INFO").child("title").getValue().toString());
                       h.add(modelList);
                  // Log.i(TAG,"second snap "+p);
                  // ItemData modelList=p.getValue(ItemData.class);
                  // Log.i(TAG,"modellist is"+modelList);
               }
               a.add(modelLists);
               modelLists.setHeader_title(s);
               modelLists.setListItem(h);
               ListMain.add(modelLists);
               adapter=new TcourseAdapter(HomeActivity.this,ListMain);
               recyclerView.setAdapter(adapter);
               Log.i(TAG,"value of s"+s);
               Log.i(TAG,"pass list is = "+ListMain);
           }
           adapter.notifyDataSetChanged();
            progressDialog.dismiss();
       }

       @Override
       public void onCancelled(@NonNull DatabaseError error) {

       }
   });

    }


}
//modelList.getDescription(),modelList.getPicc(),modelList.getTimestamp(),modelList.getTitle(),modelList.getType() ,modelList.getUserid()
/**
 DatabaseReference reference= FirebaseDatabase.getInstance().getReference("TCourses");
 reference.addValueEventListener(new ValueEventListener() {
@Override
public void onDataChange(@NonNull DataSnapshot snapshot) {
for(DataSnapshot ds:snapshot.getChildren()){
Toast.makeText(HomeActivity.this,"error "+ds.getValue(),Toast.LENGTH_SHORT).show();
Log.i(TAG,"onDataChange: "+ds);

int i=1;
String a="";
ItemData arrayList=null;
HashMap<String, String> b=new HashMap<>();
for(DataSnapshot p:ds.getChildren()){
Log.i(TAG,"child wala  = "+p);

if(p.getKey().equals("header_title")){
a=p.getValue().toString();
Log.i(TAG,p.getValue().getClass().getName());
}
else {
b = (HashMap<String, String>) p.getValue();

arrayList = new ItemData(b.get("description"), b.get("picc"), b.get("timestamp"), b.get("title"),  b.get("type"), b.get("userid"));
}

Log.i(TAG,p.getValue().getClass().getName());
}
Log.i(TAG,"a="+ a);
Log.i(TAG,"b="+b);
i++;

Toast.makeText(HomeActivity.this,"i = "+a,Toast.LENGTH_SHORT).show();

//Toast.makeText(HomeActivity.this,""+a,Toast.LENGTH_SHORT).show();

ModelList modelList = new ModelList(a.toString(),arrayList);
modelLists.add(modelList);
TcourseAdapter tcourseAdapter=new TcourseAdapter(HomeActivity.this,modelLists);
recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
recyclerView.setAdapter(tcourseAdapter);
}

}

@Override
public void onCancelled(@NonNull DatabaseError error) {

}
});*/