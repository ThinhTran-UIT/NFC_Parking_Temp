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
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nfc_parking1_project.activity.LoginActivity;
import com.example.nfc_parking1_project.activity.MainActivity;
import com.example.nfc_parking1_project.R;


public class ProfileFragment extends Fragment {
    MainActivity mainActivity;
    private Button btnLogout;
    private Button btnEditProfileDialog;
    private Button btnChangePasswordDialog;
    private Dialog editProfileDialog;
    private Dialog changePasswordDialog;

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
        // Create edit profile dialog
        editProfileDialog = new Dialog(getContext());
        editProfileDialog.setContentView(R.layout.dialog_edit_profile);
        editProfileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //Create change password dialog
        changePasswordDialog = new Dialog(getContext());
        changePasswordDialog.setContentView(R.layout.dialog_change_password);
        changePasswordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnEditProfileDialog = view.findViewById(R.id.btn_edit_profile);
        btnChangePasswordDialog = view.findViewById(R.id.btn_change_password);

        btnEditProfileDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               editProfileDialog.show();
            }
        });
        btnChangePasswordDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordDialog.show();
            }
        });

        return view;
    }


}
