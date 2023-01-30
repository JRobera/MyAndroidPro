package com.example.myandroidpro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private static JsonData jsonData;
    private final Context context;
    private final ArrayList<CommentModel> commentModels;

    public CommentAdapter(Context context,ArrayList<CommentModel> commentModels) {
        this.context = context;
        this.commentModels = commentModels;
        //TODO retrofit instance to connect to the API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://android-api.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonData = retrofit.create(JsonData.class);
    }


    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card_layout,parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        CommentModel model = commentModels.get(position);
        Call<List<UserModel>> call = jsonData.getUserById(model.getUserId());
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (!response.isSuccessful()){return;}

                List<UserModel> users = response.body();
                for(UserModel user : users){
                    holder.userId.setText(user.getUser_name());
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), "failed to get username", Toast.LENGTH_SHORT).show();
            }
        });
//        holder.userId.setText(model.getUserId());
        holder.comment_text.setText(model.getContent());

    }

    @Override
    public int getItemCount() {
        return commentModels.size();
    }
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView userId, comment_text;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userId = itemView.findViewById(R.id.userId);
            comment_text = itemView.findViewById(R.id.comment_text);

        }
    }
}
