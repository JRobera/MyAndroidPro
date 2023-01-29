package com.example.myandroidpro;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {
private RecyclerView searchRV;
private JsonData jsonData;
private String searchquery;

    public SearchFragment() {
        // Required empty public constructor
    }
    public SearchFragment(String query) {
        this.searchquery = query;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchRV = view.findViewById(R.id.idRVPost);

        ArrayList<PostModel> modelArrayList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://android-api.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonData = retrofit.create(JsonData.class);
        PostAdapter searchAdapter = new PostAdapter(this.getContext(), modelArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);

        Call<List<PostModel>> call =  jsonData.searchPost(searchquery);
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if(!response.isSuccessful()){
                    return;
                }

                List<PostModel> posts = response.body();
                for(PostModel post : posts){
                    modelArrayList.add(new PostModel(post.get_id(), post.getAuthor(), post.getTitle(), post.getBody(),post.getLikes()));
                }


                searchRV.setLayoutManager(linearLayoutManager);
                searchRV.setAdapter(searchAdapter);

            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}