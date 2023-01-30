package com.example.myandroidpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentActivity extends AppCompatActivity {
    private JsonData jsonData;
    private static RecyclerView commentRV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        commentRV = findViewById(R.id.commentRV);

        Intent intent = getIntent();
        String post_id = intent.getStringExtra("post_id");

        ArrayList<CommentModel> commentModelsArrayList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://android-api.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonData = retrofit.create(JsonData.class);


        CommentAdapter commentAdapter = new CommentAdapter(this, commentModelsArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        Call<List<CommentModel>> call = jsonData.getComments(post_id);
        call.enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                if(!response.isSuccessful()){
                    return;
                }

                List<CommentModel> comments = response.body();
                for(CommentModel comment: comments){
                    commentModelsArrayList.add(new CommentModel(comment.getUserId(),comment.getPostId(),comment.getContent(),comment.getLikes()));
                }

                commentRV.setLayoutManager(linearLayoutManager);
                commentRV.setAdapter(commentAdapter);


            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {

            }
        });

    }
}