package com.example.nfc_parking1_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.nfc_parking1_project.activity.Detail_Info_Plate;
import com.example.nfc_parking1_project.activity.MainActivity;
import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.activity.ScanActivity;


public class ProfileFragment extends Fragment {
    MainActivity mainActivity;
    ScanActivity scanActivity;
    Detail_Info_Plate detail_info_plate;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        /*Button btnScan = view.findViewById(R.id.btn_redirect_scan_activity);
        Button btnDetail = view.findViewById(R.id.btn_redirect_detail_info_activity);

        mainActivity = (MainActivity) getActivity();
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScanActivity.class);
                startActivity(intent);
            }
        });

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Detail_Info_Plate.class);
                startActivity(intent);
            }
        });
        return view;*/

        return view;
    }

}