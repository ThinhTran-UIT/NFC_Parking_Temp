package com.example.nfc_parking1_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView rcvVehicle;
    private VehicleAdapter vehicleAdapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_history, null);
        rcvVehicle = (RecyclerView) root.findViewById(R.id.rcv_vehicle);
        vehicleAdapter = new VehicleAdapter(this.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        rcvVehicle.setLayoutManager(linearLayoutManager);
        vehicleAdapter.setData(getListData());
        rcvVehicle.setAdapter(vehicleAdapter);
        // Inflate the layout for this fragment
        return root;
    }

    private List<Vehicle> getListData() {
        List<Vehicle> vehicleList = new ArrayList<>();

        vehicleList.add(new Vehicle("1", "71B2-12312"));
        vehicleList.add(new Vehicle("2", "71B2-12312"));
        vehicleList.add(new Vehicle("3", "71B2-12312"));
        vehicleList.add(new Vehicle("1", "71B2-12312"));
        vehicleList.add(new Vehicle("2", "71B2-12312"));
        vehicleList.add(new Vehicle("3", "71B2-12312"));
        vehicleList.add(new Vehicle("1", "71B2-12312"));
        vehicleList.add(new Vehicle("2", "71B2-12312"));
        vehicleList.add(new Vehicle("3", "71B2-12312"));
        vehicleList.add(new Vehicle("1", "71B2-12312"));
        vehicleList.add(new Vehicle("2", "71B2-12312"));
        vehicleList.add(new Vehicle("3", "71B2-12312"));

        return vehicleList;
    }
}