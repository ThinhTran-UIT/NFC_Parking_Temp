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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rcvVehicle;
    private VehicleAdapter vehicleAdapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        vehicleList.add(new Vehicle("1","71B2 12312"));
        vehicleList.add(new Vehicle("2","71B2 12312"));
        vehicleList.add(new Vehicle("3","71B2 12312"));
        vehicleList.add(new Vehicle("1","71B2 12312"));
        vehicleList.add(new Vehicle("2","71B2 12312"));
        vehicleList.add(new Vehicle("3","71B2 12312"));
        vehicleList.add(new Vehicle("1","71B2 12312"));
        vehicleList.add(new Vehicle("2","71B2 12312"));
        vehicleList.add(new Vehicle("3","71B2 12312"));
        vehicleList.add(new Vehicle("1","71B2 12312"));
        vehicleList.add(new Vehicle("2","71B2 12312"));
        vehicleList.add(new Vehicle("3","71B2 12312"));

        return vehicleList;
    }

}