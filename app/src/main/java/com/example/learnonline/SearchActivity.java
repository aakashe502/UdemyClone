package com.example.learnonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_search);

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
//        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
//        toolbar.setTitle("toolbar new");
//
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.search:
//                        Toast.makeText(SearchActivity.this,"Search clicked",Toast.LENGTH_SHORT).show();
//                        break;
//                }
//                return false;
//            }
//        });

        ArrayList<String> img = new ArrayList<>();
        img.add("IT Industry");
        img.add("Web Dev");
        img.add("App Dev");
        img.add("Hacking");
        img.add("School");
        img.add("Personal Development");
        img.add("Music");
        img.add("Photography");
        img.add("Fitness & Health");
        img.add("Marketing");
        img.add("Teaching and Academics");

        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.ic_business_gray);
        images.add(R.drawable.ic_design_gray);
        images.add(R.drawable.ic_webdev_gray);
        images.add(R.drawable.ic_marketing_gray);
        images.add(R.drawable.ic_music_gray);
        images.add(R.drawable.ic_play_gray);
        images.add(R.drawable.ic_teachingacademics_gray);
        images.add(R.drawable.ic_marketing_gray);
        images.add(R.drawable.ic_it_software_gray);
        images.add(R.drawable.ic_business_gray);
        images.add(R.drawable.ic_teacher_agenda);

        RecyclerView recyclerView=findViewById(R.id.recyclerui);
        AdapterCats adapterCats = new AdapterCats(img,this,images);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterCats);


//        RecyclerView recyclerView1=findViewById(R.id.recyclerui2);
//        AdapterCats adapterCat = new AdapterCats(img,this,images);
//        recyclerView1.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
//        recyclerView1.setAdapter(adapterCat);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,HomeActivity.class));
    }
    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.topbar,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        int id = item.getItemId();
//
//        switch (id){
//            case R.id.search:
//                Toast.makeText(this,"search clicked",Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_search,menu);
        MenuItem menuitem=menu.findItem(R.id.action_search);
      SearchView searchView= (SearchView) menuitem.getActionView();
        searchView.setQueryHint("Search");
      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String query) {
              return false;
          }

          @Override
          public boolean onQueryTextChange(String newText) {
              return false;
          }
      });



        return super.onCreateOptionsMenu(menu);
    }
}
