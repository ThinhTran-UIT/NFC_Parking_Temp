package com.example.nfc_parking1_project.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.nfc_parking1_project.R
import com.example.nfc_parking1_project.ViewModel.CardViewModel
import com.example.nfc_parking1_project.activity.MainActivity
import com.example.nfc_parking1_project.adapter.CardAdapter
import com.example.nfc_parking1_project.api.CardAPI
import com.example.nfc_parking1_project.helper.Constant
import com.example.nfc_parking1_project.model.Card
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardFragment : Fragment() {
    var mainActivity: MainActivity? = null
    private var rcvCard: RecyclerView? = null
    private var cardAdapter: CardAdapter? = null
    private var cardList: List<Card>? = null
    private var cardNumber: TextView? = null
    private var swipeCard: SwipeRefreshLayout? = null
    private val viewModel by navGraphViewModels<CardViewModel>(R.id.mobile_navigation)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardList = ArrayList()
        /* Button btnAddCard = root.findViewById(R.id.btn_add_card);
        mainActivity = (MainActivity) getActivity();
        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCardActivity.class);
                startActivity(intent);
            }
        });*/rcvCard = view.findViewById<View>(R.id.rcv_card) as RecyclerView
        cardAdapter = CardAdapter(this.context)
        val linearLayoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        rcvCard!!.layoutManager = linearLayoutManager
        cardNumber = view.findViewById(R.id.tv_number_parking_card)
        //        cardAdapter.setData(getListData());
        cardNumber?.setText(String.format("Number of parking card: %s", cardAdapter!!.itemCount))
        rcvCard!!.adapter = cardAdapter

        setUpSwipeCard(view)
        if (viewModel.listStateCard != null){
            rcvCard!!.layoutManager?.onRestoreInstanceState(viewModel.listStateCard)
            viewModel.listStateCard = null
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.listStateCard = rcvCard!!.layoutManager?.onSaveInstanceState()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        callApiGetCard()
        return  inflater.inflate(R.layout.fragment_card, null) as ViewGroup
    }

    private fun setUpSwipeCard(view: View) {
        swipeCard = view.findViewById(R.id.swipe_card)
        swipeCard?.setOnRefreshListener(OnRefreshListener {
            callApiGetCard()
            swipeCard?.setRefreshing(false)
        })
    }

    private fun callApiGetCard() {
        CardAPI.cardApi.getCards(Constant.TOKEN).enqueue(object : Callback<List<Card>?> {
            override fun onResponse(call: Call<List<Card>?>, response: Response<List<Card>?>) {
                try {
                    if (response.code() == 200) {
                        cardList = response.body()
                        cardAdapter!!.setData(cardList)
                        cardNumber!!.text =
                            String.format("Number of parking card: %s", cardAdapter!!.itemCount)
                        rcvCard!!.adapter = cardAdapter
                    } else {
                        Toast.makeText(context, "Can not get card", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Card>?>, t: Throwable) {
                Toast.makeText(context, "Can not get card", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun reportLost(id: String) {} //    private List<Card> getListData() {

    //        List<Card> cardList = new ArrayList<>();
    //
    //        cardList.add(new Card("124A23412", "Active"));
    //        cardList.add(new Card("12432511", "Available"));
    //
    //        return cardList;
    //    }
    companion object {
        const val TAG = "CardFragment"
    }
}