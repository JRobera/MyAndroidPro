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
                bottomNavigationView.setSelectedItemId(R.id.home);
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