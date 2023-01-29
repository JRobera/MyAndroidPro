package com.example.myandroidpro;

import static android.text.TextUtils.isEmpty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostFragment extends Fragment {
BottomNavigationView post_type, bottomNavigationView;
private JsonData jsonData;
private EditText ed_title, ed_post;
private Button btn_post;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ed_title = view.findViewById(R.id.ed_title);
        ed_post = view.findViewById(R.id.ed_post);
        btn_post = view.findViewById(R.id.btn_post);

        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);


        post_type = view.findViewById(R.id.postbottomNavigationView);
        post_type.setSelectedItemId(R.id.rpost);
        post_type.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.rpost:
                        FragmentManager fragmentManager1 = getFragmentManager();
                        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                        fragmentTransaction1.replace(R.id.container, new PostFragment());
                        fragmentTransaction1.commit();
                        return true;
                    case R.id.job_post:
                        FragmentManager fragmentManager2 = getFragmentManager();
                        FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                        fragmentTransaction2.replace(R.id.container, new JobPostFragment());
                        fragmentTransaction2.commit();
                        return true;
                }
                return false;
            }
        });

        //TODO retrofit instance to connect to the API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://android-api.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonData = retrofit.create(JsonData.class);


        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEmpty(ed_title.getText().toString()) && !isEmpty(ed_post.getText().toString())) {

                Call<PostModel> call = jsonData.createPost(ed_title.getText().toString(), ed_post.getText().toString(), LoginActivity.getUser_id());
                call.enqueue(new Callback<PostModel>() {
                    @Override
                    public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<PostModel> call, Throwable t) {
                    }
                });
                ed_title.setText("");
                ed_post.setText("");
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new HomeFragment());
                fragmentTransaction.commit();
                    MainActivity.getBottomNavigationView().setSelectedItemId(R.id.home);
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(btn_post.getWindowToken(), 0);

                }
        }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }
}