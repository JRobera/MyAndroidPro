package com.example.myandroidpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;

public class ProfileFragment extends Fragment {
private TextView user_name, user_email;
private ImageView img_avatar, prof_add_profile;

private static final String SHARED_PREFS = "shared_prefs";
private static final String IMAGE = "";
private int saved_profile;
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


        user_name.setText(LoginActivity.getUser_name());
        user_email.setText(LoginActivity.getEmail());
        img_avatar.setImageResource(LoginActivity.getUser_image());
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
        saved_profile = sharedPreferences.getInt(IMAGE,0);
    }
    public void updateViews(){
        img_avatar.setImageResource(saved_profile);
    }



}