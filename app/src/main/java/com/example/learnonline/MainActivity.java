package com.example.learnonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText emailEt,passwordEt;
    private Button signin;
    private TextView noAccountTv;
    private  FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEt=findViewById(R.id.emailEt);
        passwordEt=findViewById(R.id.passwordEt);
        signin=findViewById(R.id.signin);
        //startActivity(new Intent(MainActivity.this,HomeActivity.class));


        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait.");
        progressDialog.setCanceledOnTouchOutside(false);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
            finish();
        }

        noAccountTv=findViewById(R.id.noAccountTv);
        noAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,
                        RegisterUser.class));
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //goHome();
                loginUser();
            }
        });

    }

    private void loginUser() {

        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"enter email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"enter password",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"logginf@",Toast.LENGTH_SHORT).show();
            progressDialog.setMessage("Logging Please wait");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            progressDialog.dismiss();
                            goHome();

                            //loginTO();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,"soem errror",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        }
    }

    private void goHome() {

        startActivity(new Intent(MainActivity.this,HomeActivity.class));
        finish();
    }

    private void loginTO() {
        progressDialog.setMessage("Logging Please wait");
        progressDialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String uid = ""+ds.child("type").getValue();
                    if(uid.equals("user")){
                        progressDialog.dismiss();
                        startActivity(new Intent(MainActivity.this,HomeActivity.class));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            progressDialog.dismiss();
            }
        });
    }

    private void checkUser() {
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String type = ""+ds.child("type").getValue();
                    if(type.equals("user")){
                        progressDialog.dismiss();
                        startActivity(new Intent(MainActivity.this,MainUserActivity.class));
                        finish();
                    }
                    else if(type.equals("teacher")){
                        progressDialog.dismiss();
                        startActivity(new Intent(MainActivity.this,MainInstructorActivity.class));
                        finish();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            progressDialog.dismiss();
            }
        });
    }
}
