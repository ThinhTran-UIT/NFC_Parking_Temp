package com.example.nfc_parking1_project.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.activity.AddCardActivity;
import com.example.nfc_parking1_project.activity.MainActivity;
import com.example.nfc_parking1_project.activity.MembershipActivity;
import com.example.nfc_parking1_project.model.Vehicle;
import com.example.nfc_parking1_project.adapter.VehicleAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView rcvVehicle;
    private VehicleAdapter vehicleAdapter;
    MainActivity mainActivity;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_history, null);

        Button btnMembership = root.findViewById(R.id.btn_membership);
        mainActivity = (MainActivity)getActivity();
        btnMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MembershipActivity.class);
                startActivity(intent);
            }
        });

        Button btnFilter = root.findViewById(R.id.btn_filter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        rcvVehicle = (RecyclerView) root.findViewById(R.id.rcv_vehicle);
        vehicleAdapter = new VehicleAdapter(this.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(),RecyclerView.VERTICAL,false);
        rcvVehicle.setLayoutManager(linearLayoutManager);
        vehicleAdapter.setData(getListData());
        rcvVehicle.setAdapter(vehicleAdapter);
        // Inflate the layout for this fragment
        return root;
    }

    private void showDialog() {
        final Dialog dialogFilter = new Dialog(getContext());
        dialogFilter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFilter.setContentView(R.layout.dialog_bottom_filter);

        RadioButton vehicleInLayout = dialogFilter.findViewById(R.id.radio_one);
        RadioButton historyVehicleLayout = dialogFilter.findViewById(R.id.radio_two);
        RadioButton reportLostLayout = dialogFilter.findViewById(R.id.radio_three);

        dialogFilter.show();
        dialogFilter.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogFilter.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogFilter.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogFilter.getWindow().setGravity(Gravity.BOTTOM);

    }

    private List<Vehicle> getListData()
    {
        List<Vehicle> vehicleList = new ArrayList<>();

        vehicleList.add(new Vehicle("134245234","71-B2\n12312"));
        vehicleList.add(new Vehicle("212423433","71-B2\n12312"));
        vehicleList.add(new Vehicle("235345633","71-B2\n12312"));
        vehicleList.add(new Vehicle("142354654","71-B2\n12312"));
        vehicleList.add(new Vehicle("223454363","71-B2\n12312"));
        vehicleList.add(new Vehicle("323453215","71-B2\n12312"));
        vehicleList.add(new Vehicle("114235362","71-B2\n12312"));
        vehicleList.add(new Vehicle("223534621","71-B2\n12312"));

        return vehicleList;
    }
}