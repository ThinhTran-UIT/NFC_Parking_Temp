package com.example.nfc_parking1_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CardFragment extends Fragment {

    private RecyclerView rcvCard;
    private CardAdapter cardAdapter;
    public CardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_card, null);
        rcvCard = (RecyclerView) root.findViewById(R.id.rcv_card);
        cardAdapter = new CardAdapter(this.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(),RecyclerView.VERTICAL,false);
        rcvCard.setLayoutManager(linearLayoutManager);
        cardAdapter.setData(getListData());
        rcvCard.setAdapter(cardAdapter);
        return root;
    }

    private List<Card> getListData()
    {
        List<Card> cardList = new ArrayList<>();
        cardList.add(new Card("123456789","Report Lost"));
        cardList.add(new Card("123456789","Report Lost"));
        cardList.add(new Card("123456789","Report Lost"));
        cardList.add(new Card("123456789","Active"));
        cardList.add(new Card("123456789","Active"));
        cardList.add(new Card("123456789","Active"));
        cardList.add(new Card("123456789","Active"));
        cardList.add(new Card("123456789","Active"));
        cardList.add(new Card("123456789","Active"));
        cardList.add(new Card("123456789","Available"));
        cardList.add(new Card("123456789","Available"));
        cardList.add(new Card("123456789","Available"));
        cardList.add(new Card("123456789","Available"));
        return cardList;
    }
}