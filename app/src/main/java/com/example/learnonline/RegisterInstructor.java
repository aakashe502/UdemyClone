package com.example.learnonline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class RegisterInstructor extends AppCompatActivity {
    private ImageButton backBtn;
    private EditText nameEt,emailEt,passwordEt,phoneEt;
    private Button signinBtn;
    private TextView intructorTv;
    private ImageView profile;
    private Uri image_uri;
    private  String [] cameraPermissions;
    private  String [] storagePermissions;

    private static final int IMAGE_PICK_GALLERY_CODE=400;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_instructor);

        backBtn=findViewById(R.id.backBtn);
        nameEt=findViewById(R.id.nameEt);
        emailEt=findViewById(R.id.emailEt);
        passwordEt=findViewById(R.id.passwordEt);
        phoneEt=findViewById(R.id.phoneEt);
        signinBtn=findViewById(R.id.RegisterBtn);
        profile=findViewById(R.id.profile_image);

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait.");
        progressDialog.setCanceledOnTouchOutside(false);

        cameraPermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
            }
        });

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInstructor();
            }
        });

    }
    private  String name,email,password,phone,type;

    private void checkInstructor() {
        name=nameEt.getText().toString();
        email=emailEt.getText().toString();
        password = passwordEt.getText().toString();
        phone=phoneEt.getText().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"name is necessary",Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"email is necessary",Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"password is necessary",Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"phone is necessary",Toast.LENGTH_SHORT).show();
        }
        else{
            LOadInfo();
        }

    }
    private void LOadInfo() {
        progressDialog.setMessage("Creating Account..");
        progressDialog.show();
        //create account
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Account createdd
                        progressDialog.dismiss();
                        savetoFirebase();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterInstructor.this,"ther ei s error"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void savetoFirebase() {
        progressDialog.setMessage("saving info..");
       progressDialog.show();


                if(image_uri==null){
            HashMap<String ,Object> hashMap = new HashMap<>();
            hashMap.put("uid",firebaseAuth.getUid());
            hashMap.put("name",name);
            hashMap.put("email",email);
            hashMap.put("password",password);
            hashMap.put("phone",phone);
            hashMap.put("image","");
            hashMap.put("type","teacher");

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid()).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            startActivity(new Intent(RegisterInstructor.this,MainInstructorActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterInstructor.this,"yarrar",Toast.LENGTH_SHORT).show();

                        }
                    });
        }
        else{
            String filePathAndName="profile_images/"+""+firebaseAuth.getUid();
            StorageReference storageReference= FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());
                            Uri downloadImageUri=uriTask.getResult();
                            if(uriTask.isSuccessful()){
                                HashMap<String ,Object> hashMap = new HashMap<>();
                                hashMap.put("uid",firebaseAuth.getUid().toString());

                                hashMap.put("name",name);
                                hashMap.put("email",email);
                                hashMap.put("password",password);
                                hashMap.put("phone",phone);
                                hashMap.put("image",""+downloadImageUri);
                                hashMap.put("type","teacher");

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                ref.child(firebaseAuth.getUid()).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressDialog.dismiss();
                                                startActivity(new Intent(RegisterInstructor.this,MainInstructorActivity.class));
                                                finish();


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                                Toast.makeText(RegisterInstructor.this,"yarar"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterInstructor.this,"error"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&& resultCode==RESULT_OK&&data!=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            image_uri=result.getUri();
            profile.setImageURI(image_uri);
        }
        if((requestCode==IMAGE_PICK_GALLERY_CODE)&&resultCode==RESULT_OK&&data!=null){
            image_uri=data.getData();
            Picasso.get().load(image_uri).fit().centerCrop().into(profile);
        }

    }
}
