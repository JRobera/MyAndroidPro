package com.example.myandroidpro;

import static android.text.TextUtils.isEmpty;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JobPostFragment extends Fragment {
    BottomNavigationView post_type, bottomNavigationView;

    private JsonData jsonData;
    private EditText job_title, job_description, job_requirements, job_salary, job_location;
    private Button btn_post_job;

    public JobPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        job_title = view.findViewById(R.id.job_title);
        job_description = view.findViewById(R.id.job_description);
        job_requirements = view.findViewById(R.id.job_requirements);
        job_salary = view.findViewById(R.id.job_salary);
        job_location = view.findViewById(R.id.job_location);
        btn_post_job = view.findViewById(R.id.btn_post_job);

        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);

        post_type = view.findViewById(R.id.postbottomNavigationView);
        post_type.setSelectedItemId(R.id.job_post);
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

        btn_post_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmpty(job_title.getText()) && !isEmpty(job_description.getText()) && !isEmpty(job_requirements.getText())){

                Call<JobModel> call = jsonData.createJobPost(LoginActivity.getUser_id(),job_title.getText().toString(),job_description.getText().toString(),job_requirements.getText().toString(),job_salary.getText().toString(),job_location.getText().toString());
                call.enqueue(new Callback<JobModel>() {
                    @Override
                    public void onResponse(Call<JobModel> call, Response<JobModel> response) {
                        if(!response.isSuccessful()){
                            return;
                        }
                    }
                    @Override
                    public void onFailure(Call<JobModel> call, Throwable t) {

                    }
                });
            }
                job_title.setText("");
                job_description.setText("");
                job_requirements.setText("");
                job_salary.setText("");
                job_location.setText("");
                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.container, new JobFragment());
                fragmentTransaction1.commit();
               MainActivity.getBottomNavigationView().setSelectedItemId(R.id.job);
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btn_post_job.getWindowToken(), 0);



            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_post, container, false);
    }
}