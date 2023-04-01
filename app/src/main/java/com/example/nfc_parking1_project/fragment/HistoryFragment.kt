package com.example.nfc_parking1_project.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.nfc_parking1_project.R
import com.example.nfc_parking1_project.ViewModel.CardViewModel
import com.example.nfc_parking1_project.ViewModel.HistoryViewModel
import com.example.nfc_parking1_project.activity.MainActivity
import com.example.nfc_parking1_project.activity.MembershipActivity
import com.example.nfc_parking1_project.adapter.HistoryAdapter
import com.example.nfc_parking1_project.api.HistoryAPI
import com.example.nfc_parking1_project.helper.Constant
import com.example.nfc_parking1_project.model.History
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment() {
    var mainActivity: MainActivity? = null
    var radioFilter: RadioGroup? = null
    var vehicleIn: RadioButton? = null
    var historyVehicle: RadioButton? = null
    var reportLost: RadioButton? = null
    var swipeHistory: SwipeRefreshLayout? = null
    var dialogFilter: Dialog? = null
    private var rcvVehicle: RecyclerView? = null
    private var vehicleAdapter: HistoryAdapter? = null
    private var countHistory: TextView? = null
    private var histories: List<History?>? = null
    private var searchBar: SearchView? = null
    private val viewModel by navGraphViewModels<HistoryViewModel>(R.id.mobile_navigation)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        callApiGetHistories()
         return inflater.inflate(R.layout.fragment_history, null) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnMembership = view.findViewById<Button>(R.id.btn_membership)
        countHistory = view.findViewById(R.id.tv_first)
        mainActivity = activity as MainActivity?
        //Set up dialog filter
        setUpDialogFilter()
        //Set up Onclick event button in fragment
        btnMembership.setOnClickListener {
            val intent = Intent(
                activity,
                MembershipActivity::class.java
            )
            startActivity(intent)
        }
        val btnFilter = view.findViewById<Button>(R.id.btn_filter)
        btnFilter.setOnClickListener { dialogFilter!!.show() }

        //handle filter

        //implement search
        setUpSearch(view)
        rcvVehicle = view.findViewById<View>(R.id.rcv_vehicle) as RecyclerView
        vehicleAdapter = HistoryAdapter(this.context)
        val linearLayoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        rcvVehicle!!.layoutManager = linearLayoutManager
        rcvVehicle!!.adapter = vehicleAdapter

        //Set up swipe history
        setUpSwipeHistory(view)
        if (viewModel.listStateHistory != null){
            rcvVehicle!!.layoutManager?.onRestoreInstanceState(viewModel.listStateHistory)
            viewModel.listStateHistory = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.listStateHistory = rcvVehicle!!.layoutManager?.onSaveInstanceState()
    }

    private fun setUpSwipeHistory(v: View) {
        swipeHistory = v.findViewById(R.id.swipe_history)
        swipeHistory?.setOnRefreshListener(OnRefreshListener {
            callApiGetHistories()
            swipeHistory?.setRefreshing(false)
        })
    }

    private fun callApiGetHistories() {
        HistoryAPI.historyApi.getHistories(Constant.TOKEN)
            .enqueue(object : Callback<List<History?>?> {
                override fun onResponse(
                    call: Call<List<History?>?>,
                    response: Response<List<History?>?>
                ) {
                    Log.d("History Fragment", "On Response")
                    if (response.code() == 200) {
                        histories = response.body()
                        Log.d("History Fragment", "recived data")
                        vehicleAdapter!!.setData(histories)
                        countHistory!!.text =
                            String.format("Number of vehicle: %s", vehicleAdapter!!.itemCount)
                        rcvVehicle!!.adapter = vehicleAdapter
                    } else {
                        Toast.makeText(context, "Can not get history", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<History?>?>, t: Throwable) {
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
                    Log.d("History Fragment", t.message!!)
                }
            })
    }

    private val currentHistories: Unit
        private get() {
            HistoryAPI.historyApi.getCurrentHistory(Constant.TOKEN)
                .enqueue(object : Callback<List<History?>?> {
                    override fun onResponse(
                        call: Call<List<History?>?>,
                        response: Response<List<History?>?>
                    ) {
                        if (response.code() == 200) {
                            histories = response.body()
                            Log.d("History Fragment", "recived data")
                            vehicleAdapter!!.setData(histories)
                            countHistory!!.text =
                                String.format("Number of vehicle: %s", vehicleAdapter!!.itemCount)
                            rcvVehicle!!.adapter = vehicleAdapter
                        } else {
                            Toast.makeText(context, "Can not get history", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<List<History?>?>, t: Throwable) {
                        Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
                        Log.d("History Fragment", t.message!!)
                    }
                })
        }

    private fun setUpDialogFilter() {
        dialogFilter = Dialog(requireContext())
        dialogFilter!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogFilter!!.setContentView(R.layout.dialog_bottom_filter)
        dialogFilter!!.window!!
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogFilter!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogFilter!!.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialogFilter!!.window!!.setGravity(Gravity.BOTTOM)
        vehicleIn = dialogFilter!!.findViewById(R.id.radio_one)
        historyVehicle = dialogFilter!!.findViewById(R.id.radio_two)
        reportLost = dialogFilter!!.findViewById(R.id.radio_three)
        radioFilter = dialogFilter!!.findViewById(R.id.radioGroup)
        radioFilter?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_one -> {
                    currentHistories
                    dialogFilter!!.cancel()
                }
                R.id.radio_two -> {
                    callApiGetHistories()
                    dialogFilter!!.cancel()
                }
                R.id.radio_three -> {
                    lostCardHistories
                    dialogFilter!!.cancel()
                }
            }
        })

//        vehicleIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getCurrentHistories();
//                dialogFilter.cancel();
//            }
//        });
//        historyVehicle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callApiGetHistories();
//                dialogFilter.cancel();
//            }
//        });
//
//        reportLost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getLostCardHistories();
//                dialogFilter.cancel();
//            }
//        });
    }

    private fun setUpSearch(v: View) {
        searchBar = v.findViewById(R.id.sv_history)
        searchBar?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                vehicleAdapter!!.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                vehicleAdapter!!.filter.filter(newText)
                return false
            }
        })
    }

    private val lostCardHistories: Unit
        private get() {
            HistoryAPI.historyApi.getLostCardHistory(Constant.TOKEN)
                .enqueue(object : Callback<List<History?>?> {
                    override fun onResponse(
                        call: Call<List<History?>?>,
                        response: Response<List<History?>?>
                    ) {
                        if (response.code() == 200) {
                            histories = response.body()
                            Log.d("History Fragment", "recived data")
                            vehicleAdapter!!.setData(histories)
                            countHistory!!.text =
                                String.format("Number of vehicle: %s", vehicleAdapter!!.itemCount)
                            rcvVehicle!!.adapter = vehicleAdapter
                        } else {
                            Toast.makeText(context, "Can not get history", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<List<History?>?>, t: Throwable) {
                        Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
                        Log.d("History Fragment", t.message!!)
                    }
                })
        }

    companion object {
        const val TAG = "HistoryFragment"
    }
}