package com.example.myandroidpro;

import static android.text.TextUtils.isEmpty;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.Reader;
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
//                Toast.makeText(context.getApplicationContext(), "failed to get username", Toast.LENGTH_SHORT).show();
            }

        });
        holder.post_title.setText(model.getTitle());
        holder.post.setText(model.getBody());

        holder.likes_count.setText(String.valueOf(model.getLikes()));


        Call<Integer> call2 = jsonData.getComment_count(holder._id.getText().toString());
        call2.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(!response.isSuccessful()){return;}
                holder.comment_count.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
//                Toast.makeText(context.getApplicationContext(), "comment failure", Toast.LENGTH_SHORT).show();
            }
        });
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
        private final EditText ed_comment;
        private final Button btn_comment;

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
                    Intent intent = new Intent(comment_image.getContext(), CommentActivity.class);
                    intent.putExtra("post_id", _id.getText().toString());
                    comment_image.getContext().startActivity(intent);
                }
            });


            like_image = itemView.findViewById(R.id.like_image);
            like_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call<List<UserModel>> call  = jsonData.getUsers(LoginActivity.getEmail());
                    call.enqueue(new Callback<List<UserModel>>() {
                        @Override
                        public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                            if(!response.isSuccessful()){
                                return;
                            }
                            List<UserModel> users = response.body();

                            for(UserModel user : users){

                                if(user.getLikes().size() == 0) {

                                    Call<UserModel> callu = jsonData.userLikes(_id.getText().toString(),LoginActivity.getEmail());
                                    callu.enqueue(new Callback<UserModel>() {
                                        @Override
                                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {}

                                        @Override
                                        public void onFailure(Call<UserModel> call, Throwable t) {
                                            Toast.makeText(_id.getContext(), "network failure", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    Call<PostModel> call1 = jsonData.updateLike(_id.getText().toString(),Integer.valueOf(likes_count.getText().toString())+1);
                                    call1.enqueue(new Callback<PostModel>() {
                                        @Override
                                        public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                                            if(!response.isSuccessful()){return;}
                                        }
                                        @Override
                                        public void onFailure(Call<PostModel> call, Throwable t) { }
                                    });
                                    int color = Color.parseColor("#CC2E76BE");
                                    like_image.setColorFilter(color);
                                    likes_count.setText(String.valueOf(Integer.valueOf(likes_count.getText().toString())+1));

                                }else {
                                    int count=1;
                                    for (UserModel.LikedPostId likedPostId : user.getLikes()) {

                                        if(likedPostId.getPostid().equals(_id.getText().toString())){

                                            Call<PostModel> call1 = jsonData.updateLike(_id.getText().toString(),Integer.valueOf(likes_count.getText().toString())-1);
                                            call1.enqueue(new Callback<PostModel>() {
                                                @Override
                                                public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                                                    if(!response.isSuccessful()){return;}
                                                }
                                                @Override
                                                public void onFailure(Call<PostModel> call, Throwable t) { }
                                            });
                                            int color = Color.parseColor("#FFFFFF");
                                            like_image.setColorFilter(color);
                                            likes_count.setText(String.valueOf(Integer.valueOf(likes_count.getText().toString())-1));

                                           Call<UserModel> callmodifay = jsonData.updateLiked(LoginActivity.getUser_id(),_id.getText().toString());
                                           callmodifay.enqueue(new Callback<UserModel>() {
                                               @Override
                                               public void onResponse(Call<UserModel> call, Response<UserModel> response) { }

                                               @Override
                                               public void onFailure(Call<UserModel> call, Throwable t) { }
                                           });
                                            break;

                                        }else{

                                            if(user.getLikes().size() == count) {
                                                Call<UserModel> callu = jsonData.userLikes(_id.getText().toString(),LoginActivity.getEmail());
                                                callu.enqueue(new Callback<UserModel>() {
                                                    @Override
                                                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {}

                                                    @Override
                                                    public void onFailure(Call<UserModel> call, Throwable t) {}

                                                });

                                                Call<PostModel> call1 = jsonData.updateLike(_id.getText().toString(),Integer.valueOf(likes_count.getText().toString())+1);
                                                call1.enqueue(new Callback<PostModel>() {
                                                    @Override
                                                    public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                                                        if(!response.isSuccessful()){return;}
                                                    }
                                                    @Override
                                                    public void onFailure(Call<PostModel> call, Throwable t) { }
                                                });
                                                int color = Color.parseColor("#CC2E76BE");
                                                like_image.setColorFilter(color);
                                                likes_count.setText(String.valueOf(Integer.valueOf(likes_count.getText().toString())+1));

                                            }

                                        }

                                        count++;
                                    }
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<List<UserModel>> call, Throwable t) {
                            Toast.makeText(_id.getContext(), "network failure", Toast.LENGTH_SHORT).show();
                        }
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

            ed_comment = itemView.findViewById(R.id.ed_comment);
            btn_comment = itemView.findViewById(R.id.btn_comment);
            btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isEmpty(ed_comment.getText())){
                        Call<List<CommentModel>> call = jsonData.addComment(LoginActivity.getUser_id(), _id.getText().toString(), ed_comment.getText().toString());
                        call.enqueue(new Callback<List<CommentModel>>() {
                        @Override
                        public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                            if (!response.isSuccessful()) {
                                return;
                            }
                        }
                        @Override
                        public void onFailure(Call<List<CommentModel>> call, Throwable t) {

                        }
                    });
                        comment_count.setText(String.valueOf(Integer.valueOf(comment_count.getText().toString())+1));
                }else{
                        Toast.makeText(_id.getContext(), "comment empty", Toast.LENGTH_SHORT).show();
                    }
                    ed_comment.setText("");

                }
            });


        }
        }


}
