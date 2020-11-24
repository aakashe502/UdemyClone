package com.example.learnonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class InstructMode extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<ModelCourse> coursees;
    private FirebaseAuth firebaseAuth;

    private static final String Tag = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruct_mode);
        firebaseAuth=FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom2);

        bottomNavigationView.setSelectedItemId(R.id.mycourseadd);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.addnewcourse:
                        startActivity(new Intent(InstructMode.this,AddnewCourse.class));
                        break;
                }
                return false;
            }
        });

        recyclerView=findViewById(R.id.recshop);
       // loadCourses();

    }

    private void loadCourses() {
        coursees=new ArrayList<>();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coursees.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    ModelCourse modelCourse = ds.getValue(ModelCourse.class);
                    coursees.add(modelCourse);
                    Log.i(Tag,"msg is"+modelCourse);
                }
                AdapterCourseAdded adapterCourseAdded=new AdapterCourseAdded(InstructMode.this,coursees);
                recyclerView.setAdapter(adapterCourseAdded);
                recyclerView.invalidate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
