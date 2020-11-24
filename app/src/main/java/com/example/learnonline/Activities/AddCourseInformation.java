package com.example.learnonline.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnonline.COnstCalues.Constants;
import com.example.learnonline.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddCourseInformation extends AppCompatActivity {
    private static final int IMAGE_PICK_GALLERY_CODE =1 ;
    private EditText title,learnwhat,req,target,upiid;
    private TextView type;
    private CircleImageView imageView;
    private Uri image_uri;
    private RelativeLayout relativeLayout;
    private Button movetonext;
    private EditText price,discounted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_information);
        title=findViewById(R.id.Course_TItle);
        type=findViewById(R.id.type);
        learnwhat=findViewById(R.id.whatlearn);
        req=findViewById(R.id.Requirements);
        target=findViewById(R.id.target);
        imageView=findViewById(R.id.pic);
        relativeLayout=findViewById(R.id.relative2);
        price=findViewById(R.id.price);
        discounted=findViewById(R.id.discounted);
        upiid=findViewById(R.id.upiid);

        movetonext=findViewById(R.id.movetonext);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFromGAllery();
            }
        });
        movetonext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                see();
            }
        });

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddCourseInformation.this);
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
        });

    }

    private void chooseFromGAllery() {

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
            Picasso.get().load(image_uri).fit().centerCrop().into(imageView);

        }

    }

    private String t,l,r,tar,typ,img;
    private void see() {
        t=title.getText().toString();
        l=learnwhat.getText().toString();
        r=req.getText().toString();
        tar=target.getText().toString();
        typ=type.getText().toString();

        if(TextUtils.isEmpty(t)){
            Snackbar.make(relativeLayout,"Title is Necessary",Snackbar.LENGTH_SHORT).show();

        }
        else  if(TextUtils.isEmpty(l)){
            Snackbar.make(relativeLayout,"What user want to learn field is empty is Necessary",Snackbar.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(r)){
            Snackbar.make(relativeLayout,"Requirement filed is Necessary",Snackbar.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(tar)){
            Snackbar.make(relativeLayout,"Target Filed is Necessary",Snackbar.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(typ)){
            Snackbar.make(relativeLayout,"Type  is Necessary",Snackbar.LENGTH_SHORT).show();

        }
       else  if(image_uri==null){
            Snackbar.make(relativeLayout,"image is necessary",Snackbar.LENGTH_SHORT).show();
            return;
        }
       else if(price.getText().toString()==null){
            Toast.makeText(this,"add price",Toast.LENGTH_SHORT).show();
            return;
        }
       else if(discounted.getText().toString()==null){
            Toast.makeText(this,"add discounted price",Toast.LENGTH_SHORT).show();
            return;
        }
       else if(upiid.getText().toString()==null){
            Toast.makeText(this,"To receive payments upi id is must",Toast.LENGTH_SHORT).show();
            return ;
        }
        else {
           Intent intent = new Intent(AddCourseInformation.this,UploadVideos.class);
           intent.putExtra("title",t);
           intent.putExtra("learnw",l);
           intent.putExtra("requirements",r);
           intent.putExtra("target",tar);
           intent.putExtra("type",typ);
           intent.putExtra("image",image_uri.toString());
           intent.putExtra("price",price.getText().toString());
           intent.putExtra("discounted",discounted.getText().toString());
           intent.putExtra("upiid",upiid.getText().toString());
           startActivity(intent);
        }
    }
}
