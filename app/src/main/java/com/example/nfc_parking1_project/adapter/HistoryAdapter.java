package com.example.nfc_parking1_project.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.api.CardAPI;
import com.example.nfc_parking1_project.api.MessageResponse;
import com.example.nfc_parking1_project.helper.Constant;
import com.example.nfc_parking1_project.model.History;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.VehicleViewHolder> implements Filterable {

    Dialog vehicleDialog;
    private Context mContext;
    private List<History> histories;
    private String token;
    private List<History> oldHistories;
    public HistoryAdapter(Context mContext,String token) {
        this.mContext = mContext;
        this.token = token;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);
        final VehicleViewHolder vehicleViewHolder = new VehicleViewHolder(view);

        vehicleDialog = new Dialog(mContext);
        vehicleDialog.setContentView(R.layout.dialog_history_detail);
        vehicleDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return vehicleViewHolder;
    }

    @SuppressLint("Data set changed")
    public void setData(List<History> list) {
        this.histories = list;
        this.oldHistories = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        History history = histories.get(position);
        if (history == null) {
            return;
        }
//        holder.tvCardId.setText(history.getCardId());
        String licenseNumber = history.getLicenseNumber();
        licenseNumber = licenseNumber.replace("-", "\n");
        String line1 = licenseNumber.substring(0, 2);
        String line2 = licenseNumber.substring(2);
        licenseNumber = line1 + "-" + line2;
        holder.tvPlateId.setText(licenseNumber);
        holder.timeIn.setText(history.getTimeIn());
        holder.timeOut.setText(history.getTimeOut());
        if(history.getCardStatus().equals(Constant.CARD_STATUS_LOST))
        {

            holder.imgVehicle.setImageResource(R.drawable.icon_motor_red);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tvCardId = vehicleDialog.findViewById(R.id.tv_card_id);
                TextView tvLicensePlate = vehicleDialog.findViewById(R.id.tv_license_plate);
                TextView tvTimeIn = vehicleDialog.findViewById(R.id.tv_time_in);
                TextView tvTimeOut = vehicleDialog.findViewById(R.id.tv_time_out);
                TextView tvConfirmGetIn = vehicleDialog.findViewById(R.id.tv_staff_confirm_getin);
                TextView tvConfirmGetOut = vehicleDialog.findViewById(R.id.tv_staff_confirm_getout);
                Button btnReportCardLost = vehicleDialog.findViewById(R.id.btn_report_lost);
                btnReportCardLost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reportLostCard(history.getCardId());
                    }
                });
                tvCardId.setText(history.getCardId());
                tvLicensePlate.setText(history.getLicenseNumber());
                tvTimeIn.setText(history.getTimeIn());
                tvTimeOut.setText(history.getTimeOut());
                try {
                    tvConfirmGetIn.setText(history.getUserCheckin());
                    tvConfirmGetOut.setText(history.getUserCheckout());
                } catch (Exception e) {

                }
                vehicleDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (histories != null) {
            return histories.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if(search.isEmpty())
                {
                    histories = oldHistories;
                }else
                {
                    List<History> list = new ArrayList<>();
                    for(History history:oldHistories)
                    {
                        if(history.getLicenseNumber().toLowerCase().contains(search.toLowerCase())){
                            list.add(history);
                        }
                    }
                    histories = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = histories;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                histories = (List<History>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgVehicle;
        private TextView tvCardId;
        private TextView tvPlateId;
        private TextView timeIn;
        private TextView timeOut;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);

            imgVehicle = itemView.findViewById(R.id.img_vehicle);
            tvCardId = itemView.findViewById(R.id.tv_card_id);
            tvPlateId = itemView.findViewById(R.id.tv_plate_id);
            timeIn = itemView.findViewById(R.id.tv_time_in);
            timeOut = itemView.findViewById(R.id.tv_time_out);
        }
    }

    private void reportLostCard(String id)
    {
        CardAPI.cardApi.reportLost(token,id).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                try {
                    if (response.code() == 200)
                    {
                        MessageResponse messageResponse = response.body();
                        if(messageResponse.getSuccess())
                        {
                            Toast.makeText(mContext, messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            vehicleDialog.cancel();
                        }else
                        {
                            Toast.makeText(mContext, messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(mContext, "Server Error!", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    Log.d("HistoryAdapter",e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d("HistoryAdapter",t.getMessage());
            }
        });
    }
}
