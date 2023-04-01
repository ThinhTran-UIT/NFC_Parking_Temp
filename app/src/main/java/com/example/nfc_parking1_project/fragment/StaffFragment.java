package com.example.nfc_parking1_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.activity.MainActivity;
import com.example.nfc_parking1_project.activity.RegisterStaff;
import com.example.nfc_parking1_project.adapter.UserAdapter;
import com.example.nfc_parking1_project.api.UserAPI;
import com.example.nfc_parking1_project.helper.Constant;
import com.example.nfc_parking1_project.kotlin.ScanActivityKotlin;
import com.example.nfc_parking1_project.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StaffFragment extends Fragment {
    static final String TAG = "StaffFragment";
    RegisterStaff registerStaff;
    MainActivity mainActivity;
    private RecyclerView rcvUser;
    private UserAdapter userAdapter;
    private TextView numberStaff;
    private SearchView searchBar;
    private SwipeRefreshLayout swipeUser;
    public StaffFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_staff, container, false);
        //Button Register user(staff)
        Button btnRegister = view.findViewById(R.id.btn_add_user);
        mainActivity = (MainActivity) getActivity();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisterStaff.class);
                startActivity(intent);
            }
        });
        numberStaff = view.findViewById(R.id.tv_number_staff);
        //Button Register user(staff)
        /*Button btnCamera = view.findViewById(R.id.btn_camera);
        mainActivity = (MainActivity) getActivity();
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScanActivityKotlin.class);
                startActivity(intent);
            }
        });*/
        setUpSearch(view);
        rcvUser = (RecyclerView) view.findViewById(R.id.rcv_staff);
        userAdapter = new UserAdapter(this.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        rcvUser.setLayoutManager(linearLayoutManager);
//        userAdapter.setData(getListData());
        rcvUser.setAdapter(userAdapter);
        callApiGetAllStaff();
        setUpSwipeUser(view);
        return view;
    }

    private void setUpSwipeUser(View view){
        swipeUser =  view.findViewById(R.id.swipe_user);
        swipeUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callApiGetAllStaff();
                swipeUser.setRefreshing(false);
            }
        });
    }
    private void setUpSearch(View v){
        searchBar = v.findViewById(R.id.sv_user);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                userAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void callApiGetAllStaff() {
        UserAPI.userApi.getAllUser(Constant.TOKEN).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.code() == 200) {
                    List<User> users = response.body();
                    userAdapter.setData(users);
                    numberStaff.setText(String.format("Number of vehicle: %s", userAdapter.getItemCount()));
                    rcvUser.setAdapter(userAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }


}