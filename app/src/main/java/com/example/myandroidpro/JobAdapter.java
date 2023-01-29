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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    static JsonData jsonData;
    private final Context context;
    private final ArrayList<JobModel> jobModelArrayList;

    public JobAdapter(Context context, ArrayList<JobModel> jobModelArrayList) {
        this.context = context;
        this.jobModelArrayList = jobModelArrayList;

        //TODO retrofit instance to connect to the API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://android-api.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonData = retrofit.create(JsonData.class);
    }

    @NonNull
    @Override
    public JobAdapter.JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_card_layout,parent,false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobAdapter.JobViewHolder holder, int position) {
        JobModel model = jobModelArrayList.get(position);
        holder.job_post_id.setText(model.get_id());
        Call<List<UserModel>> call = jsonData.getUserById(model.getAuthor());
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (!response.isSuccessful()){return;}

                List<UserModel> users = response.body();
                for(UserModel user : users){
                    holder.users_name.setText(user.getUser_name());
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), "failed to get username", Toast.LENGTH_SHORT).show();
            }

        });
        holder.job_title.setText("Job Title: "+model.getTitle());
        holder.job_description.setText("Job Description: "+model.getDescription());
        holder.job_requirements.setText("Job Requirements: "+model.getRequirements());
        holder.job_salary.setText(String.valueOf("Job Salary: "+model.getSalary()));
        holder.job_location.setText("Job Location: "+model.getLocation());
        holder.job_posted_date.setText(String.valueOf("Posted At: "+model.getPosted_date()));
        if(model.getAuthor().equals(LoginActivity.getUser_id())){
            holder.delete_job.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return jobModelArrayList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        private final TextView job_post_id, users_name, job_title, job_description, job_requirements, job_salary, job_location, job_posted_date;
        private final ImageView delete_job;
        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            job_post_id = itemView.findViewById(R.id.job_post_id);
            users_name = itemView.findViewById(R.id.users_name);
            job_title = itemView.findViewById(R.id.job_title);
            job_description = itemView.findViewById(R.id.job_description);
            job_requirements = itemView.findViewById(R.id.job_requirements);
            job_salary = itemView.findViewById(R.id.job_salary);
            job_location = itemView.findViewById(R.id.job_location);
            job_posted_date = itemView.findViewById(R.id.job_posted_date);
            delete_job = itemView.findViewById(R.id.delete_job);
            delete_job.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(delete_job.getContext());
                    builder.setMessage("Are you sure you want to delete this post?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Call<Void> call = jsonData.deleteJobPost(job_post_id.getText().toString());
                                    call.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            Toast.makeText(delete_job.getContext(), "Job Post deleted", Toast.LENGTH_SHORT).show();
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
