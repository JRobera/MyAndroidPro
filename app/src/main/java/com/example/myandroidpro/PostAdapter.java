package com.example.myandroidpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{
    static JsonData jsonData;
    private final Context context;
    private final ArrayList<PostModel> postModelArrayList;


    public PostAdapter(Context context, ArrayList<PostModel> postModelArrayList) {
        this.context = context;
        this.postModelArrayList = postModelArrayList;

        //TODO retrofit instance to connect to the API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://android-api.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonData = retrofit.create(JsonData.class);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostModel model = postModelArrayList.get(position);
//        holder.user_image.setImageResource(model.getAuthor());
        holder._id.setText(model.get_id());
        Call<List<UserModel>> call = jsonData.getUserById(model.getAuthor());
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (!response.isSuccessful()){return;}

                List<UserModel> users = response.body();
                for(UserModel user : users){
                    holder.user_name.setText(user.getUser_name());
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), "failed to get username", Toast.LENGTH_SHORT).show();
            }

        });
        holder.post_title.setText(model.getTitle());
//        holder.post_image.setImageResource(model.getTitle());
        holder.post.setText(model.getBody());
        holder.likes_count.setText(String.valueOf(model.getLikes()));
//        holder.comment_count.setText(String.valueOf(model.getBody()));
        if(model.getAuthor().equals(LoginActivity.getUser_id())){
            holder.delete_image.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return postModelArrayList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private final ImageView user_image;
        private final TextView _id;
        private final TextView user_name;
        private final TextView post_title;
        private final ImageView post_image;
        private final TextView post;
        private final TextView likes_count;
        private final ImageView like_image;
        private final ImageView delete_image;
        private final ImageView comment_image;
        private final TextView comment_count;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            _id = itemView.findViewById(R.id._id);
            user_image = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
            post_title = itemView.findViewById(R.id.post_title);
            post_image = itemView.findViewById(R.id.post_image);
            post = itemView.findViewById(R.id.post);
            likes_count = itemView.findViewById(R.id.txt_likes);
            comment_count = itemView.findViewById(R.id.txt_comment);

            comment_image = itemView.findViewById(R.id.comment_image);
            comment_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager2 = new CommentFragment().getFragmentManager();
                    FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                    fragmentTransaction2.replace(R.id.container, new JobPostFragment());
                    fragmentTransaction2.commit();
//                    MainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container, new CommentFragment()).commit();

                    Toast.makeText(comment_image.getContext(), "comment clicked", Toast.LENGTH_SHORT).show();
                }
            });


            like_image = itemView.findViewById(R.id.like_image);
            like_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Call<PostModel> call = jsonData.updateLike(_id.getText().toString(),Integer.valueOf(likes_count.getText().toString())+1);
                            call.enqueue(new Callback<PostModel>() {
                                @Override
                                public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                                    if(!response.isSuccessful()){
                                        return;
                                    }
                                }
                                @Override
                                public void onFailure(Call<PostModel> call, Throwable t) { }
                            });
                }
            });

            delete_image = itemView.findViewById(R.id.delete_img);
            delete_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(delete_image.getContext());
                    builder.setMessage("Are you sure you want to delete this post?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Call<Void> call = jsonData.deletePost(_id.getText().toString());
                                    call.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            Toast.makeText(delete_image.getContext(), "Post deleted", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {

                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });


        }
        }


}
