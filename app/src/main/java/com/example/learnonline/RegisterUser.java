package com.example.learnonline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.HashMap;

public class RegisterUser extends AppCompatActivity {
    private ImageButton backBtn;
    private EditText nameEt,emailEt,passwordEt,phoneEt;
    private Button signinBtn;
    //private TextView intructorTv;
    private ImageView profileimasge;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private static  int IMAGE_PICK_GALLERY_CODE=1;
    private Uri image_uri;
    private EditText upiid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        profileimasge=findViewById(R.id.profile_image);
        backBtn=findViewById(R.id.backBtn);
        nameEt=findViewById(R.id.nameEt);
        emailEt=findViewById(R.id.emailEt);
        passwordEt=findViewById(R.id.passwordEt);
        phoneEt=findViewById(R.id.phoneEt);
        signinBtn=findViewById(R.id.RegisterBtn);
        //intructorTv=findViewById(R.id.InstrucorTv);
        upiid=findViewById(R.id.upiid);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait.");
        progressDialog.setCanceledOnTouchOutside(false);


        profileimasge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opengallery();
            }
        });
//




        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDetails();

            }
        });
//        intructorTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(RegisterUser.this,RegisterInstructor.class));
//            }
//        });

    }

    private void opengallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(((requestCode == IMAGE_PICK_GALLERY_CODE) && resultCode== RESULT_OK && (data != null))){
            image_uri=data.getData();
            Picasso.get().load(image_uri).fit().centerCrop().into(profileimasge);
        }

    }

    private String name,email,password,phone;
    private void checkDetails() {
         name=nameEt.getText().toString();
         email = emailEt.getText().toString();
         password = passwordEt.getText().toString();
         phone = phoneEt.getText().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Name is Necessary",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Email is Necessary",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"password is Necessary",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Phone number is Necessary",Toast.LENGTH_SHORT).show();
        }
        else if(image_uri==null){
            Toast.makeText(this,"image cannot be null ",Toast.LENGTH_SHORT).show();
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
                        savetoFireBaseData();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterUser.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void savetoFireBaseData() {
        progressDialog.setMessage("saving user Information");
        progressDialog.show();
        if(image_uri==null){
            HashMap<String,Object> hashMap = new HashMap<>();
            Toast.makeText(RegisterUser.this,"ud"+firebaseAuth.getUid().toString(),Toast.LENGTH_SHORT).show();
            hashMap.put("uid",firebaseAuth.getUid().toString());
            hashMap.put("name",name);
            hashMap.put("email",email);
            hashMap.put("password",password);
            hashMap.put("phone",phone);
            hashMap.put("type","user");
            hashMap.put("upiid",upiid.getText().toString());
            hashMap.put("profile",image_uri.toString());

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid()).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            startActivity(new Intent(RegisterUser.this,MainActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterUser.this,"Oops something went wrong",Toast.LENGTH_SHORT).show();

                        }
                    });
        }
        else{
            String filePathAndName="profile_images"+""+firebaseAuth.getUid();
            StorageReference storageReference= FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());
                            Uri downloadImageUri=uriTask.getResult();
                            if(uriTask.isSuccessful()){
                                HashMap<String,Object> hashMap = new HashMap<>();
                                Toast.makeText(RegisterUser.this,"ud"+firebaseAuth.getUid().toString(),Toast.LENGTH_SHORT).show();
                                hashMap.put("uid",firebaseAuth.getUid());
                                hashMap.put("name",name);
                                hashMap.put("email",email);
                                hashMap.put("password",password);
                                hashMap.put("phone",phone);
                                hashMap.put("type","user");
                                hashMap.put("upiid",upiid.getText().toString());
                                hashMap.put("profile",downloadImageUri.toString());

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                ref.child(firebaseAuth.getUid()).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressDialog.dismiss();
                                                startActivity(new Intent(RegisterUser.this,HomeActivity.class));
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(RegisterUser.this,"Oops something went wrong",Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(RegisterUser.this,"ther is unrecog error :",Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterUser.this,"some error = "+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }




    }
}
