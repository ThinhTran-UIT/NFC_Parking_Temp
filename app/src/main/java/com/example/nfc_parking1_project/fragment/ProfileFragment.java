package com.example.nfc_parking1_project.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.nfc_parking1_project.activity.Detail_Info_Plate;
import com.example.nfc_parking1_project.activity.EditProfileActivity;
import com.example.nfc_parking1_project.activity.LoginActivity;
import com.example.nfc_parking1_project.activity.MainActivity;
import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.activity.RegisterStaff;
import com.example.nfc_parking1_project.activity.ScanActivity;


public class ProfileFragment extends Fragment {
    MainActivity mainActivity;
    public Button btnLogout;
    public Button btnEditProfile;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Button Register user(staff)
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

        //Button Edit Profile
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

        return view;
    }

}