package com.example.myandroidpro;

import static android.text.TextUtils.isEmpty;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.net.URI;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {
private TextView user_name, user_email;
private ImageView img_avatar, prof_add_profile,prof_edit_profile;
private JsonData jsonData;

private static final String SHARED_PREFS = "shared_prefs";
private static final String IMAGE = "";
private String saved_profile;
Uri selectedprofile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectedprofile = data.getData();
        img_avatar.setImageURI(selectedprofile);
        saveDate();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO retrofit instance to connect to the API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://android-api.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonData = retrofit.create(JsonData.class);

        user_name = view.findViewById(R.id.prof_user_name);
        user_email = view.findViewById(R.id.prof_user_email);
        img_avatar = view.findViewById(R.id.img_avatar);

        prof_add_profile = view.findViewById(R.id.prof_add_profile);
        prof_add_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profile = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                profile.setType("image/*");
                profile.setAction(profile.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(profile,"Select profile image"),1);

            }
        });

        prof_edit_profile = view.findViewById(R.id.prof_edit_profile);
        prof_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getActivity();
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.edit_profile);

                EditText username = dialog.findViewById(R.id.user_name);
                username.setText(LoginActivity.getUser_name());
                EditText useremail = dialog.findViewById(R.id.user_email);
                useremail.setText(LoginActivity.getEmail());
                EditText oldpassword = dialog.findViewById(R.id.old_password);
                EditText newpassword = dialog.findViewById(R.id.new_password);
                Button updatebtn = dialog.findViewById(R.id.profile_update);
                updatebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isEmpty(useremail.getText().toString()) || !Patterns.EMAIL_ADDRESS.matcher(useremail.getText().toString()).matches()) {

                        Call<List<UserModel>> call = jsonData.getUsers(LoginActivity.getEmail());
                        call.enqueue(new Callback<List<UserModel>>() {
                            @Override
                            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {

                                List<UserModel> users = response.body();
                                for (UserModel user : users) {
                                    if (oldpassword.getText().toString().equals(user.getPassword())) {

                                        Call<UserModel> call2 = jsonData.updateUser(LoginActivity.getUser_id(), username.getText().toString(), useremail.getText().toString(), newpassword.getText().toString());
                                        call2.enqueue(new Callback<UserModel>() {
                                            @Override
                                            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<UserModel> call, Throwable t) {
                                                Toast.makeText(getActivity(), "Update Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        dialog.dismiss();
                                    } else {
                                        TextInputLayout Oldpass = dialog.findViewById(R.id.OldPassword);
                                        Oldpass.setError("Incorrect password");
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<UserModel>> call, Throwable t) {

                            }
                        });

                    }else{useremail.setError("Invalid Email");}

                    }

                });

                dialog.show();

            }
        });

        user_name.setText(LoginActivity.getUser_name());
        user_email.setText(LoginActivity.getEmail());
        loadDate();
        updateViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    public void saveDate(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IMAGE, selectedprofile.toString());

    }
    public void loadDate(){
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        saved_profile = sharedPreferences.getString(IMAGE,"");
    }
    public void updateViews(){
        img_avatar.setImageURI(Uri.parse(saved_profile));

    }



}