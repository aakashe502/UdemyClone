package com.example.learnonline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.learnonline.Activities.AddCourseInformation;
import com.example.learnonline.COnstCalues.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AddnewCourse extends AppCompatActivity {
    private Button cont;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_addnew_course);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom2);

        bottomNavigationView.setSelectedItemId(R.id.addnewcourse);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mycourseadd:
                        startActivity(new Intent(AddnewCourse.this,InstructMode.class));
                        break;
                }
                return false;
            }
        });

        cont=findViewById(R.id.next);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddnewCourse.this,AddCourseInformation.class));
            }
        });

}}

//  courseimage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
//            }
//        });
//
//        addc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                checkInfo();
//            }
//        });
//    }
//
//    private void choosefromgallery() {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.setType("video/*");
//        startActivityForResult(intent,VIDEO_GALLEY_CODE);
//    }
//
//    private String tit;
//    private String desc;
//
//
//    private void checkInfo() {
//        tit=title.getText().toString();
//        desc=description.getText().toString();
//        if(TextUtils.isEmpty(tit)){
//            Toast.makeText(this,"title is necessary",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(TextUtils.isEmpty(desc)){
//            Toast.makeText(this,"description is necessary",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(image_uri==null){
//            Toast.makeText(this,"image is necessary",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(type.equals("Select type")){
//            Toast.makeText(this,"Please select course type",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        else{
//            LoadToFirebase();
//        }
//    }
//
//    private void LoadToFirebase() {
//        progressDialog.setMessage("ADding Product");
//        progressDialog.show();
//        final String timestamp = "" + System.currentTimeMillis();
//        String filepath = "profile_images" + "" + timestamp;
//
//
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filepath);
//        storageReference.putFile(image_uri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                        while (!uriTask.isSuccessful()) ;
//                        Uri downloadImageUri = uriTask.getResult();
//                        if (uriTask.isSuccessful()) {
//                            HashMap<String, Object> hashMap = new HashMap<>();
//                            hashMap.put("type",""+type.getText().toString());
//                            hashMap.put("title",""+tit);
//                            hashMap.put("description",""+desc);
//                            hashMap.put("picc",""+downloadImageUri);
//                            hashMap.put("timestamp",""+timestamp);
//                            hashMap.put("userid",firebaseAuth.getUid().toString());
//                            DatabaseReference reference=FirebaseDatabase.getInstance().getReference("TCourses");
//                            reference.child(type.getText().toString()).child("panel").child(timestamp).setValue(hashMap);
//                            DatabaseReference reference2=FirebaseDatabase.getInstance().getReference("TCourses");
//                            reference2.child(type.getText().toString()).child("title").child("header_title").setValue("top courses in "+type.getText().toString());
//                                  hashMap.remove("userid");
//                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//                            databaseReference.child(firebaseAuth.getUid()).child("Courses").child(timestamp).setValue(hashMap)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            progressDialog.dismiss();
//                                            startActivity(new Intent(AddnewCourse.this,InstructMode.class));
//                                            Toast.makeText(AddnewCourse.this,"product added succesfully",Toast.LENGTH_SHORT).show();
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            progressDialog.dismiss();
//
//
//
//                                        }
//                                    });
//
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressDialog.dismiss();
//                        Toast.makeText(AddnewCourse.this,"error",Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//    }
//    @Override
//    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data) {
//        super.onActivityResult(requestCode,resultCode,data);
//        if(((requestCode == IMAGE_PICK_GALLERY_CODE) && resultCode== RESULT_OK && (data != null))){
//            image_uri=data.getData();
//            Picasso.get().load(image_uri).fit().centerCrop().into(courseimage);
//
//        }
//        if(requestCode ==VIDEO_GALLEY_CODE&&resultCode==RESULT_OK&&data!=null){
//            videouri = data.getData();
//            videoView.setVideoURI(videouri);
//        }
//    }

   /* addc=findViewById(R.id.addc);
    videoView=findViewById(R.id.video);
        videoView.setMediaController(new MediaController(this));
        videoView.start();
 select=findViewById(R.id.selectvideo);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosefromgallery();
            }
        });


        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait.");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth=FirebaseAuth.getInstance();

        courseimage=findViewById(R.id.courseimage);
        title=findViewById(R.id.titlecourse);
        description=findViewById(R.id.description);
        type=findViewById(R.id.type);
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddnewCourse.this);
                builder.setTitle("Product Category")
                        .setItems(Constants.productCategories,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface,int i) {
                                //get pick up
                                String category=Constants.productCategories[i];
                                //set pick up
                                type.setText(category);

                            }
                        })
                        .show();

            }
        })










        private static final int VIDEO_GALLEY_CODE = 2;
    private ImageView courseimage;
    private EditText title,description;
    private TextView type,select;
    private static  int IMAGE_PICK_GALLERY_CODE=1;
    private Uri image_uri;
    private Button addc;
    private Uri videouri;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private VideoView videoView;


        */