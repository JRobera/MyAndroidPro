package com.example.myandroidpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
//import android.widget.SearchView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//BottomNavigationView.OnNavigationItemSelectedListener
public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private static BottomNavigationView bottomNavigationView;

    public static BottomNavigationView getBottomNavigationView() { return bottomNavigationView;}

    TextView txterror;
    private JsonData jsonData;
    RecyclerView postRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txterror = findViewById(R.id.error_message);
//        postRV = findViewById(R.id.idRVPost);
//        ArrayList<PostModel> postModelArrayList = new ArrayList<PostModel>();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:4000/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        jsonData = retrofit.create(JsonData.class);
//
//        Call<List<PostModel>> call = jsonData.getPosts();
//        call.enqueue(new Callback<List<PostModel>>() {
//            @Override
//            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
//                if(!response.isSuccessful()){
//                    txterror.setText("code "+ response.code());
//                    return;
//                }
//                List<PostModel> posts = response.body();
//                for(PostModel post : posts){
//                    postModelArrayList.add(new PostModel(post.getAuthor(), post.getTitle(), post.getBody(),post.getLikes()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<PostModel>> call, Throwable t) {
//                txterror.setText(t.getMessage());
//                txterror.setVisibility(View.VISIBLE);
//            }
//        });
//
//        // Here, we have created new array list and added data to it
//        postModelArrayList.add(new PostModel("Robera", R.drawable.ic_person, "title", R.drawable.ic_launcher_background, "This is the first post",5,0,3,0));
//        postModelArrayList.add(new PostModel("Abera", R.drawable.ic_person,"Second title", R.drawable.ic_launcher_background,"This is the second post",72,0,4,0));
//        postModelArrayList.add(new PostModel("Haji", R.drawable.ic_person,"Thrid title", R.drawable.ic_launcher_background,"This is the theied post",2,0,4,0));
//        postModelArrayList.add(new PostModel("Helen", R.drawable.ic_person,"Forth title", R.drawable.ic_launcher_background,"This is the forth post",12,0,6,0));
//        postModelArrayList.add(new PostModel("Abera", R.drawable.ic_person,"Fiveth title", R.drawable.ic_launcher_background,"This is the fiveth post",22,0,11,0));
//        postModelArrayList.add(new PostModel("Firans", R.drawable.ic_person,"Sixth title", R.drawable.ic_launcher_background,"This is the sixth post",30,0,15,0));
//        postModelArrayList.add(new PostModel("Abera", R.drawable.ic_person,"Seventh title", R.drawable.ic_launcher_background,"This is the seventh post",15,0,5,0));
////
        // we are initializing our adapter class and passing our arraylist to it.
//        PostAdapter postAdapter = new PostAdapter(this, postModelArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
//        postRV.setLayoutManager(linearLayoutManager);
//        postRV.setAdapter(postAdapter);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

    }
    final HomeFragment homeFragment = new HomeFragment();
    final PostFragment postFragment = new PostFragment();
    final ProfileFragment profileFragment = new ProfileFragment();
    final JobFragment jobFragment = new JobFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                return true;
            case R.id.post:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, postFragment).commit();
                return true;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                return true;
            case R.id.job:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, jobFragment).commit();
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                return true;
            }
        });
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new SearchFragment(s)).commit();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });

        MenuItem menuItem1 = menu.findItem(R.id.log_out);
        menuItem1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId()== R.id.log_out){
                    LoginActivity.logOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }
}