package com.example.myandroidpro;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myandroidpro.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    RecyclerView postRV;
//    private FragmentHomeBinding binding;
    private JsonData jsonData;
    TextView txt_error;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         postRV = view.findViewById(R.id.idRVPost);
         txt_error = view.findViewById(R.id.txt_error);

        ArrayList<PostModel> postModelArrayList = new ArrayList<PostModel>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://android-api.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonData = retrofit.create(JsonData.class);
        // we are initializing our adapter class and passing our arraylist to it.
        PostAdapter postAdapter = new PostAdapter(this.getContext(), postModelArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);

        Call<List<PostModel>> call = jsonData.getPosts();
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if(!response.isSuccessful()){
                    txt_error.setVisibility(View.VISIBLE);
                    txt_error.setText("code "+response.code());
                    return;
                }

                List<PostModel> posts = response.body();

                for(PostModel post : posts){
                    postModelArrayList.add(new PostModel(post.get_id(), post.getAuthor(), post.getTitle(), post.getBody(),post.getLikes()));
                }

                // in below two lines we are setting layoutmanager and adapter to our recycler view.
                postRV.setLayoutManager(linearLayoutManager);
                postRV.setAdapter(postAdapter);
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                txt_error.setVisibility(View.VISIBLE);
                txt_error.setText(t.getMessage());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}