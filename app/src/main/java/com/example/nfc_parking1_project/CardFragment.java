package com.example.nfc_parking1_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CardFragment extends Fragment {
    AddCardActivity addCardActivity;
    MainActivity mainActivity;
    private RecyclerView rcvCard;
    private CardAdapter cardAdapter;

    public CardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_card, null);

        //Button Register user(staff)
        Button btnAddCard = view.findViewById(R.id.img_add_card);
        mainActivity = (MainActivity)getActivity();
        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCardActivity.class);
                startActivity(intent);
            }
        });

        rcvCard = (RecyclerView) view.findViewById(R.id.rcv_card);
        cardAdapter = new CardAdapter(this.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        rcvCard.setLayoutManager(linearLayoutManager);
        cardAdapter.setData(getListData());
        rcvCard.setAdapter(cardAdapter);
        return view;
    }

    private List<Card> getListData() {
        List<Card> cardList = new ArrayList<>();
        cardList.add(new Card("123456789", "Report Lost"));
        cardList.add(new Card("123456789", "Report Lost"));
        cardList.add(new Card("123456789", "Report Lost"));
        cardList.add(new Card("123456789", "Active"));
        cardList.add(new Card("123456789", "Active"));
        cardList.add(new Card("123456789", "Active"));
        cardList.add(new Card("123456789", "Active"));
        cardList.add(new Card("123456789", "Active"));
        cardList.add(new Card("123456789", "Active"));
        cardList.add(new Card("123456789", "Available"));
        cardList.add(new Card("123456789", "Available"));
        cardList.add(new Card("123456789", "Available"));
        cardList.add(new Card("123456789", "Available"));
        return cardList;
    }
}