package com.example.cheezycode;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// SecondActivity is acting as MainActivity

public class SecondActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolBar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    RecyclerView recyclerView;

//    Adding code for Infinite RecyclerView
    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    List<Item> items = new ArrayList<>();

    Boolean isScrolling = false;

    int currentItems, totalItems, scrolledOutItems;

    String token = "";

    SpinKitView spinKitView;

//    Adding code for Infinite RecyclerView ends here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        //    Adding code for Infinite RecyclerView
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewAdapter = new RecyclerViewAdapter(this,items);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        spinKitView = findViewById(R.id.spin_kit);


        //    Adding code for Infinite RecyclerView ends here


        setUpToolbar();

        navigationView = findViewById(R.id.navigation_menu1);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.nav_home :

                        Toast.makeText(SecondActivity.this,"Home......",Toast.LENGTH_SHORT).show();

//                        Toast.makeText(getApplicationContext(),"bb",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_gallery :

                        Toast.makeText(SecondActivity.this,"Gallery..",Toast.LENGTH_SHORT).show();
                        break;

                    default: Toast.makeText(SecondActivity.this,"Default case..",Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });



//        Code for RecyclerView OnScrollListener added here

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){

                    isScrolling = true;

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrolledOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrolledOutItems == totalItems)){

                    isScrolling = false;

                    getData();
                }
            }
        });

//        Code for Recyclerview OnscrollListener ends here


        getData();

//        Code added for notification for higher version devices,above Oreo Version
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel("MyNotification","MyNotification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

//        Code for Firebase Messaging, taken from firebase website Android section, messaging

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Success";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }

                        Toast.makeText(SecondActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }

                });
//
    }

//        Code for Firebase Messaging ends here

    private void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout1);
        toolBar = findViewById(R.id.toolBar1);
//        getData();

//        setSupportActionBar(toolBar);
//        toolBar.setTitle(R.string.app_name);
//        toolBar.setTitleTextColor(this.getResources().getColor(R.color.colorBackground1));
        setSupportActionBar(toolBar);

        actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


    }

    private void getData(){

        String url = BloggerAPI.url + "?key=" + BloggerAPI.key;

        if (token != "") {

            url = url + "&pageToken=" + token;
        }
        if(token == null){

            return;
        }

        spinKitView.setVisibility(View.VISIBLE);

            final Call<PostList> postList = BloggerAPI.getService().getPostList(url);


        postList.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {

                PostList list = response.body();
                token = list.getNextPageToken();

                items.addAll(list.getItems());
                recyclerViewAdapter.notifyDataSetChanged();

//                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(SecondActivity.this,list.getItems() );

                Toast.makeText(SecondActivity.this, "success", Toast.LENGTH_LONG).show();
                spinKitView.setVisibility(View.GONE);
//                recyclerView.setAdapter(recyclerViewAdapter);

//                recyclerView.setAdapter(new RecyclerViewAdapter(SecondActivity.this,list.getItems()));
//                Toast.makeText(SecondActivity.this,"Success",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {

                Toast.makeText(SecondActivity.this,"Error occurred",Toast.LENGTH_SHORT).show();

            }
        });


    }






}
