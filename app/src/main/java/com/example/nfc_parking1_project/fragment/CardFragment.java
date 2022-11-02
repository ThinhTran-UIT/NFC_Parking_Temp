package com.example.nfc_parking1_project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nfc_parking1_project.api.CardAPI;
import com.example.nfc_parking1_project.model.Card;
import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.adapter.CardAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardFragment extends Fragment {

    private RecyclerView rcvCard;
    private CardAdapter cardAdapter;
    private List<Card> cardList;
    private TextView cardNumber;
    public CardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cardList = new ArrayList<>();
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_card, null);
        rcvCard = (RecyclerView) root.findViewById(R.id.rcv_card);
        cardAdapter = new CardAdapter(this.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        rcvCard.setLayoutManager(linearLayoutManager);
        cardNumber = root.findViewById(R.id.tv_number_parking_card);
        callApiGetCard();
        return root;
    }

    private void callApiGetCard() {
       CardAPI.cardApi.getCards().enqueue(new Callback<List<Card>>() {
           @Override
           public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {
                cardList=response.body();
                cardAdapter.setData(cardList);
                cardNumber.setText(String.format("Number of parking card: %s",cardAdapter.getItemCount()));
                rcvCard.setAdapter(cardAdapter);

           }
           @Override
           public void onFailure(Call<List<Card>> call, Throwable t) {
                Toast.makeText(getContext(),"Can not get card",Toast.LENGTH_SHORT).show();
           }
       });
    }
}