package com.example.nfc_parking1_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.activity.AddCardActivity;
import com.example.nfc_parking1_project.activity.MainActivity;
import com.example.nfc_parking1_project.adapter.CardAdapter;
import com.example.nfc_parking1_project.api.CardAPI;
import com.example.nfc_parking1_project.model.Card;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardFragment extends Fragment {

    final static String TAG = "CardFragment";
    MainActivity mainActivity;
    private RecyclerView rcvCard;
    private CardAdapter cardAdapter;
    private List<Card> cardList;
    private TextView cardNumber;
    private String token;
    private SwipeRefreshLayout swipeCard;
    public CardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_card, null);
        cardList = new ArrayList<>();
       /* Button btnAddCard = root.findViewById(R.id.btn_add_card);
        mainActivity = (MainActivity) getActivity();
        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCardActivity.class);
                startActivity(intent);
            }
        });*/
        rcvCard = (RecyclerView) root.findViewById(R.id.rcv_card);
        cardAdapter = new CardAdapter(this.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        rcvCard.setLayoutManager(linearLayoutManager);
        cardNumber = root.findViewById(R.id.tv_number_parking_card);
//        cardAdapter.setData(getListData());
        cardNumber.setText(String.format("Number of parking card: %s", cardAdapter.getItemCount()));
        rcvCard.setAdapter(cardAdapter);
        callApiGetCard();
        setUpSwipeCard(root);
        return root;
    }
    private void setUpSwipeCard(View view){
        swipeCard = view.findViewById(R.id.swipe_card);
        swipeCard.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callApiGetCard();
                swipeCard.setRefreshing(false);
            }
        });
    }
    private void callApiGetCard() {
        CardAPI.cardApi.getCards(token).enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {
                try {
                    if (response.code() == 200) {
                        cardList = response.body();
                        cardAdapter.setData(cardList);
                        cardNumber.setText(String.format("Number of parking card: %s", cardAdapter.getItemCount()));
                        rcvCard.setAdapter(cardAdapter);
                    } else {
                        Toast.makeText(getContext(), "Can not get card", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<List<Card>> call, Throwable t) {
                Toast.makeText(getContext(), "Can not get card", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reportLost(String id) {

    }

//    private List<Card> getListData() {
//        List<Card> cardList = new ArrayList<>();
//
//        cardList.add(new Card("124A23412", "Active"));
//        cardList.add(new Card("12432511", "Available"));
//
//        return cardList;
//    }
}