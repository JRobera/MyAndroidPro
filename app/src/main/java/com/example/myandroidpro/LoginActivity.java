package com.example.myandroidpro;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private JsonData jsonData;
    private static EditText login_email, login_password;
    private Button login;
    private TextView create_account;
    private ProgressBar login_progress;
    private static String user_id;
    private static String user_name;
    private static Integer user_image;
    private static String email;

//TODO: used to pass user_id globaliy
    public static String getUser_id() { return user_id; }

    public static void setUser_id(String user_id) { LoginActivity.user_id = user_id;}

    public static String getUser_name() { return user_name; }

    public static void setUser_name(String user_name) { LoginActivity.user_name = user_name; }

    public static Integer getUser_image() { return user_image; }

    public static String getEmail() { return email; }

    public static void setEmail(String email) { LoginActivity.email = email; }

    private static final String SHARED_PREFS =  "sharedPrefs";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String ID ="id";
    private static final String USER_NAME = "user_name";
    private static final String USER_IMAGE = "we";
    private static final String UEMAIL = "uemail";

    private static String saved_email, saved_password, saved_id, saved_name, saved_uemail;
    private Integer saved_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login = findViewById(R.id.login_btn);
        create_account = findViewById(R.id.new_account);
        login_progress = findViewById(R.id.login_progress);

//TODO retrofit instance to connect to the API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://android-api.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonData = retrofit.create(JsonData.class);

    login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!isEmpty(login_email.getText().toString())){
                if(!isEmpty(login_password.getText().toString())){
                    if(!Patterns.EMAIL_ADDRESS.matcher(login_email.getText().toString()).matches()){
                        TextInputLayout useremail = findViewById(R.id.userEmailLayout);
                        useremail.setError("Invalid Email");
                        Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    }else {
                        checkUser();
                    }
                }else{
                    TextInputLayout password = findViewById(R.id.userPasswordLayout);
                    password.setError("Password required!");}
            }else{
                TextInputLayout email = findViewById(R.id.userEmailLayout);
                email.setError("Email required!");
            }
        }
    });

    create_account.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }
    });


    loadData();
    updateViews();
    if(!isEmpty(login_email.getText().toString())  && !isEmpty(login_password.getText().toString())){
            checkUser();
        }


    }
    public void checkUser(){

        login_progress.setVisibility(View.VISIBLE);

        Call<List<UserModel>> call = jsonData.getUsers(login_email.getText().toString());
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if(!response.isSuccessful()){
                    return;
                }
                List<UserModel> users = response.body();

                for(UserModel user : users){
                    user_id = user.get_id();
                    user_name = user.getUser_name();
                    user_image = user.getUser_image();
                    email = user.getUser_email();

                        if(login_email.getText().toString().equals(user.getUser_email())){
                            if(login_password.getText().toString().equals(user.getPassword())){
                                // save user preferences
                                saveData();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                login_progress.setVisibility(View.INVISIBLE);
                            }else{
                                TextInputLayout password = findViewById(R.id.userPasswordLayout);
                                password.setError("Incorrect password!");
                                Toast.makeText(LoginActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                                login_progress.setVisibility(View.INVISIBLE);

                            }
                        }else {
                            TextInputLayout email = findViewById(R.id.userEmailLayout);
                            email.setError("User does not exist");
                            Toast.makeText(LoginActivity.this, "User does not exist "+login_email.getText().toString(), Toast.LENGTH_SHORT).show();
                            login_progress.setVisibility(View.INVISIBLE);
                        }
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                login_progress.setVisibility(View.INVISIBLE);
//                login_email.setText(t.toString());
                Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, login_email.getText().toString());
        editor.putString(PASSWORD, login_password.getText().toString());
        editor.putString(ID, user_id );
        editor.putString(USER_NAME, user_name);
//        editor.putInt("we",user_image);
        editor.putString(UEMAIL, email);
        editor.apply();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        saved_email= sharedPreferences.getString(EMAIL,"");
        saved_password = sharedPreferences.getString(PASSWORD, "");
        saved_id = sharedPreferences.getString(ID,"");
        saved_name = sharedPreferences.getString(USER_NAME, "");
        saved_image = sharedPreferences.getInt(USER_IMAGE,0);
        saved_uemail = sharedPreferences.getString(UEMAIL, "");
    }
    public void updateViews(){
        login_email.setText(saved_email);
        login_password.setText(saved_password);
        user_id = saved_id;
        user_name = saved_name;
        user_image = saved_image;
        email = saved_uemail;
    }

// TODO::Log out
    public static void logOut(){
         LoginActivity.clear();
    }
    public static void clear(){
        SharedPreferences sharedPreferences = login_email.getContext().getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


}