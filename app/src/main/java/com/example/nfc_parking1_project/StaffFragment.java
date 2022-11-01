package com.example.nfc_parking1_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class StaffFragment extends Fragment {
    RegisterStaff registerStaff;
    MainActivity mainActivity;
    private RecyclerView rcvUser;
    private UserAdapter userAdapter;

    public StaffFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_staff, container, false);

        //Button Register user(staff)
        Button btnRegister = view.findViewById(R.id.btn_add_user);
        mainActivity = (MainActivity)getActivity();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisterStaff.class);
                startActivity(intent);
            }
        });

        rcvUser = (RecyclerView) view.findViewById(R.id.rcv_staff);
        userAdapter = new UserAdapter(this.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(),RecyclerView.VERTICAL,false);
        rcvUser.setLayoutManager(linearLayoutManager);
        userAdapter.setData(getListData());
        rcvUser.setAdapter(userAdapter);

        return view;
    }

    private List<User> getListData()
    {
        List<User> userList = new ArrayList<>();

        userList.add(new User("Tran Duc Thinh","0973583485", "Testing Parking Lot"));
        userList.add(new User("Tran Duc Thinh","0973583485", "Testing Parking Lot"));
        userList.add(new User("Tran Duc Thinh","0973583485", "Testing Parking Lot"));
        userList.add(new User("Tran Duc Thinh","0973583485", "Testing Parking Lot"));
        userList.add(new User("Tran Duc Thinh","0973583485", "Testing Parking Lot"));
        userList.add(new User("Boc Tan Lui","0344256789", "Testing Parking Lot"));
        userList.add(new User("Boc Tan Lui","0344256789", "Testing Parking Lot"));
        userList.add(new User("Boc Tan Lui","0344256789", "Testing Parking Lot"));
        userList.add(new User("Boc Tan Lui","0344256789", "Testing Parking Lot"));
        userList.add(new User("Boc Tan Lui","0344256789", "Testing Parking Lot"));
        userList.add(new User("Boc Tan Lui","0344256789", "Testing Parking Lot"));
        userList.add(new User("Boc Tan Lui","0344256789", "Testing Parking Lot"));

        return userList;
    }

}