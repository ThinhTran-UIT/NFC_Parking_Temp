package com.example.nfc_parking1_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint; //insert for button redirect
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    public Button buttonScan;
    public Button buttonDetailInfo;
    BottomNavigationView bottomNavigationView;
    private RecyclerView rcvVehicle;
    private VehicleAdapter vehicleAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start button redirect scan activity
        buttonScan = (Button) findViewById(R.id.btn_redirect_scan_activity);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Start button redirect detail in4 activity
        buttonDetailInfo  =(Button) findViewById(R.id.btn_redirect_detail_info_activity);
        buttonDetailInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Detail_Info_Plate.class);
                startActivity(intent);
                finish();
            }
        });

        //Bottom Navigation Bar
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HistoryFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.nav_history);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.nav_history:
                        fragment = new HistoryFragment();
                        break;
                    case R.id.nav_card:
                        fragment = new CardFragment();
                        break;
                    case R.id.nav_staff:
                        fragment = new StaffFragment();
                        break;
                    case R.id.nav_profile:
                        fragment = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
                return true;
            }
        });
    }
}