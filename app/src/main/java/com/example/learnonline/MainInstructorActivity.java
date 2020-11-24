package com.example.learnonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class MainInstructorActivity extends AppCompatActivity {
    private Button signou;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_instructor);
        signou=findViewById(R.id.signou);
        firebaseAuth = FirebaseAuth.getInstance();
        signou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(MainInstructorActivity.this,MainActivity.class));
                finish();

            }
        });
    }
}
