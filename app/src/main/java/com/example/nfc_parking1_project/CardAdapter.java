package com.example.nfc_parking1_project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private Context mContext;
    private List<Card> mListCard;

    public CardAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CardAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);
        return new CardAdapter.CardViewHolder(view);
    }

    @SuppressLint("Data set changed")
    public void setData(List<Card> list){
        this.mListCard = list;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = mListCard.get(position);
        if(card == null){
            Log.d("CARD", "onBindViewHolder: "+card.getStatus());
            return;
        }
        //holder.imgLocation.setImageResource(card.getResourceId());
   /*     holder.tvCardId.setText(card.getCardId());
        holder.tvStatus.setText(card.getStatus());*/
    }

    @Override
    public int getItemCount() {
        if (mListCard != null){
            return mListCard.size();
        }
        return 0;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
       // private ImageView imgLocation;
        private TextView tvCardId;
        private TextView tvStatus;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            //imgLocation = itemView.findViewById(R.id.img_location);
            tvCardId = itemView.findViewById(R.id.tv_card_id);
            tvStatus = itemView.findViewById(R.id.tv_card_status);
        }
    }
}
