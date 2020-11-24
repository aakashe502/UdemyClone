package com.example.learnonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnonline.Activities.PlayVideo;
import com.example.learnonline.Adapter.MyCOurseAdapter;
import com.example.learnonline.Adapter.TcourseAdapter;
import com.example.learnonline.Models.ItemData;
import com.example.learnonline.Models.ModelList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseInfoActivity extends AppCompatActivity {
    private ImageView textView;

    private TextView title,description,date,instructor,target,requirements,learn,price,original;
    private RecyclerView recyclerView;
    private  String s="",timestp="",typ="";
    TcourseAdapter adapter;
    ArrayList<ModelList> ListMain;
    private ImageView backbtn;
    private static final String TAG = "HomeActivity";
    private Button playlist,wishlist,buynow;
    String name="",userid="",heading="";
    String timestamp;
    final int UPI_PAYMENT = 0;
    String upiid="",money="";
    String instructid="";
    private static boolean enrolled=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        learn=findViewById(R.id.learn);
        wishlist=findViewById(R.id.wishlist);
        price=findViewById(R.id.price);
        backbtn=findViewById(R.id.backbtn);
        original=findViewById(R.id.original);
        buynow=findViewById(R.id.buynow);
        playlist=findViewById(R.id.playlist);
        target=findViewById(R.id.target);
        requirements=findViewById(R.id.requirements);
        textView=findViewById(R.id.cname);
        description=findViewById(R.id.desc);
        date=findViewById(R.id.date);
        instructor=findViewById(R.id.instructor);

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentActivity();
            }
        });

        heading=getIntent().getStringExtra("heading");
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wishlist.getText().equals("WishList")){
                    wishlist.setText("WishListed");
                    AddToDataBase();
                }
                else{
                    wishlist.setText("WishList");
                }
            }
        });
        final String str=getIntent().getStringExtra("cname");
        userid=getIntent().getStringExtra("userid");
        timestamp=getIntent().getStringExtra("timestamp");
        String a=getIntent().getStringExtra("teacher");
        Picasso.get().load(str).into(textView);
        instructor.setText("created By : "+userid);




        //getName();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(FirebaseAuth.getInstance().getUid().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("WishListed").hasChild(timestamp)){
                    wishlist.setText("wishlisted");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(CourseInfoActivity.this);
                //inflate view
                View view1=LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottomsheet_price,null);


                ImageButton backBtn1=view1.findViewById(R.id.backBtn1);
                ImageButton deleteBtn=view1.findViewById(R.id.deleteBtn);
                ImageButton editBtn=view1.findViewById(R.id.editBtn);
                ImageView productIconIv=view1.findViewById(R.id.productIconIv);
                TextView discountedNoteTv=view1.findViewById(R.id.discountedNoteTv);
                TextView titleTv=view1.findViewById(R.id.titleTv);
                TextView descriptionTv=view1.findViewById(R.id.descriptionTv);
                TextView categoryTv=view1.findViewById(R.id.categoryTv);
               // TextView quantityTv=view1.findViewById(R.id.quantityTv);
                TextView discountedPriceTv=view1.findViewById(R.id.discountedPriceTv);
                TextView originalPriceTv=view1.findViewById(R.id.originalPriceTv);
                money=discountedPriceTv.getText().toString();


                backBtn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });
                
                Button pay=view1.findViewById(R.id.pay);
                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadInfos();
                    }
                });


               originalPriceTv.setText(original.getText().toString());
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();
                Picasso.get().load(str).into(productIconIv);
                titleTv.setText(title.getText().toString());
                categoryTv.setText(typ.toString());
                discountedPriceTv.setText(price.getText().toString());
                descriptionTv.setText(learn.getText().toString());
                originalPriceTv.setText(original.getText().toString());
                originalPriceTv.setPaintFlags(price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);


            }
        });

        //recyclerView=findViewById(R.id.recyclerview);

        title=findViewById(R.id.title);


        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userid).child("Courses").child(timestamp).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ItemData itemData=snapshot.child("INFO").getValue(ItemData.class);
//                String upiid1=snapshot.child("INFO").child("upiid").getValue().toString();
//                upiid=upiid;
//                Toast.makeText(CourseInfoActivity.this,"upi is"+upiid,Toast.LENGTH_SHORT).show();
                Log.i("itemdata = ",""+itemData.getTitle());
                String ti=itemData.getTitle().toString();

                title.setText(ti);
                description.setText(""+itemData.getDescription());
                date.setText("Added on :"+itemData.getDate());
              //  instructor.setText("Created by "+itemData.getUserid());
                target.setText(""+itemData.getTarget());
                requirements.setText(""+itemData.getRequirements());
                name=itemData.getUserid().toString();
                learn.setText(""+itemData.getDescription());
                price.setText("RS "+itemData.getPrice());
                price.setPaintFlags(price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                original.setText("RS "+itemData.getDiscounted());
                original.setAllCaps(false);
                timestp = itemData.getTimestamp().toString();
                typ=itemData.getType();
                instructid=itemData.getUserid().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ListMain =new ArrayList<>();

        //adapter=new TcourseAdapter(CourseInfoActivity.this,ListMain);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        //progressDialog.show();

        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enrolled==true){
                Intent intent=new Intent(CourseInfoActivity.this,PlayVideo.class);
                intent.putExtra("timestamp",timestamp);
                intent.putExtra("userid",userid.toString());
                intent.putExtra("head",heading);
                startActivity(intent);}

                Toast.makeText(CourseInfoActivity.this,"enroll to get strated",Toast.LENGTH_SHORT).show();
            }
        });
       gotoNew();




//        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("TCourses");
//        reference1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for(DataSnapshot ds:snapshot.getChildren()){
//                    if(ds.hasChild("title")){
//                        if(ds.child("title").hasChild("header_title")){
//                            s=ds.child("title").child("header_title").getValue().toString();
//                        }
//                        else{
//                            s="Recent";
//                        }
//                    }
//                    else{
//                        s="top Courses";
//                    }
//
//
//                    String header_title=s;
//                    Log.i(TAG,"first snap = "+ds.child("title").child("header_title").getValue());
//                    ModelList modelLists=new ModelList();
//                    ArrayList<ModelList> a=new ArrayList<>();
//                    ArrayList<ItemData> h=new ArrayList<>();
//                    for(DataSnapshot p:ds.child("panel").getChildren()){
//
//
//                        Log.i(TAG,"values in it "+p.child("INFO").child("title").getValue());
//                        ItemData modelList = p.child("INFO").getValue(ItemData.class);
//                        Log.i("hi = ",""+p.child("INFO").getValue());
//                        modelList.setTitle(p.child("INFO").child("title").getValue().toString());
//                        h.add(modelList);
//
//                        // Log.i(TAG,"second snap "+p);
//                        // ItemData modelList=p.getValue(ItemData.class);
//
//
//
//                        // Log.i(TAG,"modellist is"+modelList);
//
//
//                    }
//                    a.add(modelLists);
//                    modelLists.setHeader_title(s);
//                    modelLists.setListItem(h);
//                    ListMain.add(modelLists);
//                    adapter=new TcourseAdapter(CourseInfoActivity.this,ListMain);
//                    recyclerView.setAdapter(adapter);
//
//                    Log.i(TAG,"value of s"+s);
//                    Log.i(TAG,"pass list is = "+ListMain);
//
//                }
//                adapter.notifyDataSetChanged();
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }

    private void gotoNew() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("Taken").hasChild(timestamp)){
                    Intent intent=new Intent(CourseInfoActivity.this,PlayVideo.class);
                    intent.putExtra("timestamp",timestamp);
                    intent.putExtra("userid",userid.toString());
                    intent.putExtra("head",heading);
                    enrolled=true;
                    startActivity(intent);


                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadInfos() {
    payUsingUpi(original.getText().toString(),"choudharyrashmi31@okhdfcbank","course money","hello");

    }
    void payUsingUpi(String amount, String upiId, String name, String note) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", money)
                .appendQueryParameter("Rs", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(CourseInfoActivity.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(CourseInfoActivity.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                //Code to handle successful transaction here.
                Toast.makeText(CourseInfoActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: "+approvalRefNo);
                HashMap<String,Object> hashMap=new HashMap<>();
               // hashMap
                hashMap.put("timestamp",timestp);
                hashMap.put("type",typ);
                hashMap.put("instructor",instructid);
                hashMap.put("money paid",money);
                hashMap.put("sent to","choudharyrashmi31@okhdfcbank");
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users");
                databaseReference.child(FirebaseAuth.getInstance().getUid()).child("Taken").child(timestp).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                               progressDialog.dismiss();
                                Toast.makeText(CourseInfoActivity.this,"everything fine",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CourseInfoActivity.this,MyCourses.class));
                                finish();

                            }
                        });
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(CourseInfoActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(CourseInfoActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CourseInfoActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }


    private void getName() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userid=snapshot.child(name).child("name").getValue().toString();
                Log.i("name is ",userid);
                //instructor.setText("INSTRUCTOR:"+userid.);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void AddToDataBase() {
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("timestamp",""+timestp);
        hashMap.put("type",""+typ);
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.child(FirebaseAuth.getInstance().getUid().toString()).child("WishListed").child(timestp).setValue(hashMap).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CourseInfoActivity.this,"Added to WishList",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void paymentActivity() {

    }

}
