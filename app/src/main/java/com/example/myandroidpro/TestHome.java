package com.example.myandroidpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class TestHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_home);
//        RecyclerView postRV = findViewById(R.id.idRVPost);

//        // Here, we have created new array list and added data to it
//        ArrayList<PostModel> postModelArrayList = new ArrayList<PostModel>();
//        postModelArrayList.add(new PostModel("Robera", R.drawable.ic_person, "title", R.drawable.ic_launcher_background, "This is the first post",5,0,"3",0));
//        postModelArrayList.add(new PostModel("Abera", R.drawable.ic_person,"Second title", R.drawable.ic_launcher_background,"This is the second post",72,0,"4",0));
//        postModelArrayList.add(new PostModel("Haji", R.drawable.ic_person,"Thrid title", R.drawable.ic_launcher_background,"This is the theied post",2,0,"4",0));
//        postModelArrayList.add(new PostModel("Helen", R.drawable.ic_person,"Forth title", R.drawable.ic_launcher_background,"This is the forth post",12,0,"6",0));
//        postModelArrayList.add(new PostModel("Abera", R.drawable.ic_person,"Fiveth title", R.drawable.ic_launcher_background,"This is the fiveth post",22,0,"11",0));
//        postModelArrayList.add(new PostModel("Firans", R.drawable.ic_person,"Sixth title", R.drawable.ic_launcher_background,"This is the sixth post",30,0,"15",0));
//        postModelArrayList.add(new PostModel("Abera", R.drawable.ic_person,"Seventh title", R.drawable.ic_launcher_background,"This is the seventh post",15,0,"5",0));
//
        // we are initializing our adapter class and passing our arraylist to it.
//        PostAdapter postAdapter = new PostAdapter(this, postModelArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
//        postRV.setLayoutManager(linearLayoutManager);
//        postRV.setAdapter(postAdapter);
    }
}