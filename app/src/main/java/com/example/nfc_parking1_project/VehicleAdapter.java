package com.example.nfc_parking1_project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    private Context mContext;
    private List<Vehicle> mListVehicle;

    public VehicleAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);
        return new VehicleViewHolder(view);
    }

    @SuppressLint("Data set changed")
    public void setData(List<Vehicle> list) {
        this.mListVehicle = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle vehicle = mListVehicle.get(position);
        if (vehicle == null) {
            System.out.print("Car Null");
            return;
        }
        holder.tvCardId.setText(vehicle.getCardId());
        holder.tvPlateId.setText(vehicle.getPlateId());
    }

    @Override
    public int getItemCount() {
        if (mListVehicle != null) {
            return mListVehicle.size();
        }
        return 0;
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgVehicle;
        private TextView tvCardId;
        private TextView tvPlateId;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);

            imgVehicle = itemView.findViewById(R.id.img_vehicle);
            tvCardId = itemView.findViewById(R.id.tv_card_id);
            tvPlateId = itemView.findViewById(R.id.tv_plate_id);
        }
    }
}
