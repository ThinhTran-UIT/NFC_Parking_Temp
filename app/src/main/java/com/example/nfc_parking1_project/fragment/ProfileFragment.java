package com.example.nfc_parking1_project.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;
import android.app.Fragment;

import com.example.nfc_parking1_project.activity.EditProfileActivity;
import com.example.nfc_parking1_project.activity.LoginActivity;
import com.example.nfc_parking1_project.activity.MainActivity;
import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.dialog.EditProfileDialog;


public class ProfileFragment extends Fragment {
    MainActivity mainActivity;
    public Button btnLogout;
    public Button btnEditProfileDialog;
    public Button btnChangePasswordDialog;
    public ProfileFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup containers, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, containers, false);

        //Button Logout user(staff)
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btnLogout = view.findViewById(R.id.btn_logout);
        mainActivity = (MainActivity)getActivity();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnEditProfileDialog = view.findViewById(R.id.btn_edit_profile);
        btnChangePasswordDialog = view.findViewById(R.id.btn_change_password);

        btnEditProfileDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileDialog dialog = new EditProfileDialog();
                dialog.show(getFragmentManager(), "abc");
            }
        });

        return view;
    }


}

/*//Button Edit Profile
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btnEditProfile = view.findViewById(R.id.btn_edit);
        mainActivity = (MainActivity)getActivity();
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        //Button Change Password
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btnChangePassword = view.findViewById(R.id.btn_change_password);
        mainActivity = (MainActivity)getActivity();
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });*/