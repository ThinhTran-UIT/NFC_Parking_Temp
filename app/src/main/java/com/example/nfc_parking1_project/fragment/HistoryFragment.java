package com.example.nfc_parking1_project.fragment;




import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.activity.MainActivity;
import com.example.nfc_parking1_project.activity.MembershipActivity;
import com.example.nfc_parking1_project.adapter.HistoryAdapter;
import com.example.nfc_parking1_project.api.HistoryAPI;
import com.example.nfc_parking1_project.model.History;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    final static String TAG = "HistoryFragment";
    MainActivity mainActivity;
    RadioGroup radioFilter;
    RadioButton vehicleIn;
    RadioButton historyVehicle;
    RadioButton reportLost;
    SwipeRefreshLayout swipeHistory;
    Dialog dialogFilter;
    private RecyclerView rcvVehicle;
    private HistoryAdapter vehicleAdapter;
    private TextView countHistory;
    private List<History> histories;
    private String token;
    private SearchView searchBar;
    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_history, null);
        //Get authorize token
        Bundle bundle = this.getArguments();
        try {
            if (bundle != null) {
                token = bundle.getString("token");
            }
            Log.d(TAG, token);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        Button btnMembership = root.findViewById(R.id.btn_membership);
        countHistory = root.findViewById(R.id.tv_first);
        mainActivity = (MainActivity) getActivity();
        //Set up dialog filter
        setUpDialogFilter();
        //Set up Onclick event button in fragment
        btnMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MembershipActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });
        Button btnFilter = root.findViewById(R.id.btn_filter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFilter.show();
            }
        });

        //handle filter

        //implement search
        setUpSearch(root);


        rcvVehicle = (RecyclerView) root.findViewById(R.id.rcv_vehicle);
        vehicleAdapter = new HistoryAdapter(this.getContext(),token);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        rcvVehicle.setLayoutManager(linearLayoutManager);
        rcvVehicle.setAdapter(vehicleAdapter);
        callApiGetHistories();
        //Set up swipe history
        setUpSwipeHistory(root);


        // Inflate the layout for this fragment
        return root;
    }

    private void setUpSwipeHistory(View v){
        swipeHistory = v.findViewById(R.id.swipe_history);
        swipeHistory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callApiGetHistories();
                swipeHistory.setRefreshing(false);
            }
        });
    }

    private void callApiGetHistories() {
        HistoryAPI.historyApi.getHistories(token).enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call<List<History>> call, Response<List<History>> response) {
                Log.d("History Fragment", "On Response");
                if (response.code() == 200) {
                    histories = response.body();
                    Log.d("History Fragment", "recived data");
                    vehicleAdapter.setData(histories);
                    countHistory.setText(String.format("Number of vehicle: %s", vehicleAdapter.getItemCount()));
                    rcvVehicle.setAdapter(vehicleAdapter);
                } else {
                    Toast.makeText(getContext(), "Can not get history", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<History>> call, Throwable t) {
                Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
                Log.d("History Fragment", t.getMessage());
            }
        });
    }

    private void getCurrentHistories() {
        HistoryAPI.historyApi.getCurrentHistory(token).enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call<List<History>> call, Response<List<History>> response) {
                if (response.code() == 200) {
                    histories = response.body();
                    Log.d("History Fragment", "recived data");
                    vehicleAdapter.setData(histories);
                    countHistory.setText(String.format("Number of vehicle: %s", vehicleAdapter.getItemCount()));
                    rcvVehicle.setAdapter(vehicleAdapter);
                } else {
                    Toast.makeText(getContext(), "Can not get history", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<History>> call, Throwable t) {
                Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
                Log.d("History Fragment", t.getMessage());
            }
        });
    }


    private void setUpDialogFilter() {
        dialogFilter = new Dialog(getContext());
        dialogFilter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFilter.setContentView(R.layout.dialog_bottom_filter);
        dialogFilter.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogFilter.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogFilter.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogFilter.getWindow().setGravity(Gravity.BOTTOM);
        vehicleIn = dialogFilter.findViewById(R.id.radio_one);
        historyVehicle = dialogFilter.findViewById(R.id.radio_two);
        reportLost = dialogFilter.findViewById(R.id.radio_three);
        radioFilter = dialogFilter.findViewById(R.id.radioGroup);

        radioFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_one:
                        getCurrentHistories();
                        dialogFilter.cancel();
                        break;
                    case R.id.radio_two:
                        callApiGetHistories();
                        dialogFilter.cancel();
                        break;
                    case R.id.radio_three:
                        getLostCardHistories();
                        dialogFilter.cancel();
                        break;
                }
            }
        });

//        vehicleIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getCurrentHistories();
//                dialogFilter.cancel();
//            }
//        });
//        historyVehicle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callApiGetHistories();
//                dialogFilter.cancel();
//            }
//        });
//
//        reportLost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getLostCardHistories();
//                dialogFilter.cancel();
//            }
//        });


    }

    private void setUpSearch(View v){
        searchBar = v.findViewById(R.id.sv_history);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                vehicleAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                vehicleAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }


    private void getLostCardHistories() {
        HistoryAPI.historyApi.getLostCardHistory(token).enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call<List<History>> call, Response<List<History>> response) {
                if (response.code() == 200) {
                    histories = response.body();
                    Log.d("History Fragment", "recived data");
                    vehicleAdapter.setData(histories);
                    countHistory.setText(String.format("Number of vehicle: %s", vehicleAdapter.getItemCount()));
                    rcvVehicle.setAdapter(vehicleAdapter);
                } else {
                    Toast.makeText(getContext(), "Can not get history", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<History>> call, Throwable t) {
                Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
                Log.d("History Fragment", t.getMessage());
            }
        });
    }

}