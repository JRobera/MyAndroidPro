package com.example.myandroidpro;

import static android.text.TextUtils.isEmpty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationActivity extends AppCompatActivity {
    private JsonData jsonData;
    private EditText signup_username, signup_email, signup_password;
    private Button signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        signup_username = findViewById(R.id.signup_username);
        signup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signup_password);
        signup_btn = findViewById(R.id.signup_btn);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://android-api.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonData = retrofit.create(JsonData.class);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmpty(signup_username.getText().toString())){
                    if(!isEmpty(signup_email.getText().toString())){
                        if(!isEmpty(signup_password.getText().toString())){

                            if(!Patterns.EMAIL_ADDRESS.matcher(signup_email.getText().toString()).matches()){
                                TextInputLayout useremail = findViewById(R.id.useremailLayout);
                                useremail.setError("Invalid Email");
                                Toast.makeText(RegistrationActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                            }else {
                                Call<UserModel> call = jsonData.createUser(signup_username.getText().toString(),signup_email.getText().toString(),signup_password.getText().toString());
                                call.enqueue(new Callback<UserModel>() {
                                    @Override
                                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                        if(!response.isSuccessful()){
                                            return;
                                        }

                                        Toast.makeText(RegistrationActivity.this, String.valueOf(response), Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onFailure(Call<UserModel> call, Throwable t) {

                                    }
                                });
//                            Toast.makeText(RegistrationActivity.this, "user login", Toast.LENGTH_SHORT).show();
                                Call<List<UserModel>> calls = jsonData.getUsers(signup_email.getText().toString());
                                calls.enqueue(new Callback<List<UserModel>>() {
                                    @Override
                                    public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                                        if(!response.isSuccessful()){
                                            return;
                                        }
                                        List<UserModel> users = response.body();
                                        for(UserModel user : users){
                                            LoginActivity.setUser_id(user.get_id());
                                            LoginActivity.setUser_name(user.getUser_name());
                                            LoginActivity.setEmail(user.getUser_email());
                                            if(signup_email.getText().toString().equals(user.getUser_email())){
                                                if(signup_password.getText().toString().equals(user.getPassword())){
                                                    // save user preferences
//                                    saveData();
                                                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }else{
                                                    Toast.makeText(RegistrationActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(RegistrationActivity.this, "User does not exist "+signup_email.getText().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<UserModel>> call, Throwable t) {
                                        Toast.makeText(RegistrationActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                                    }
                                });
//                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
                            }
                        }else{
                            TextInputLayout password = findViewById(R.id.userpasswordLayout);
                            password.setError("password required");
                        }

                    }else{
                        TextInputLayout useremail = findViewById(R.id.useremailLayout);
                        useremail.setError("Email required");
                    }
                }else{
                    TextInputLayout username = findViewById(R.id.usernameLayout);
                    username.setError("User name required");
                }

            }
        });

    }
}