package com.example.nfc_parking1_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;


public class StaffFragment extends Fragment {
    RegisterStaff registerStaff;
    MainActivity mainActivity;

    public StaffFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff, container, false);

        Button btnRegister = view.findViewById(R.id.btn_add_user);

        mainActivity = (MainActivity) getActivity();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisterStaff.class);
                startActivity(intent);
            }
        });
        return view;
    }

}