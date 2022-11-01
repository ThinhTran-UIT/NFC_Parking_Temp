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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(),RecyclerView.VERTICAL,false);
        rcvVehicle.setLayoutManager(linearLayoutManager);
        vehicleAdapter.setData(getListData());
        rcvVehicle.setAdapter(vehicleAdapter);
        // Inflate the layout for this fragment
        return root;
    }

    private List<Vehicle> getListData()
    {
        List<Vehicle> vehicleList = new ArrayList<>();

        vehicleList.add(new Vehicle("134245234","71B2-12312"));
        vehicleList.add(new Vehicle("212423433","71B2-12312"));
        vehicleList.add(new Vehicle("235345633","71B2-12312"));
        vehicleList.add(new Vehicle("142354654","71B2-12312"));
        vehicleList.add(new Vehicle("223454363","71B2-12312"));
        vehicleList.add(new Vehicle("323453215","71B2-12312"));
        vehicleList.add(new Vehicle("114235362","71B2-12312"));
        vehicleList.add(new Vehicle("223534621","71B2-12312"));
        vehicleList.add(new Vehicle("324235672","71B2-12312"));
        vehicleList.add(new Vehicle("123534625","71B2-12312"));
        vehicleList.add(new Vehicle("242354563","71B2-12312"));
        vehicleList.add(new Vehicle("323453643","71B2-12312"));

        return vehicleList;
    }
}