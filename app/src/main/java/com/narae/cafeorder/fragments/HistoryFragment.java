package com.narae.cafeorder.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.narae.cafeorder.R;
import com.narae.cafeorder.adapter.HistoryListAdapter;
import com.narae.cafeorder.history.History;
import com.narae.cafeorder.history.HistoryItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HistoryFragment extends Fragment{

    HistoryListAdapter listAdapter;
    ExpandableListView expListView;
    List<History> historyList;

    View currentView;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_history, container, false);

        historyList = new ArrayList<>();

        // get the listview
        expListView = (ExpandableListView) currentView.findViewById(R.id.hisExp);

        //prepareListDateNew();
        orderRequest();
        listAdapter = new HistoryListAdapter(getContext(), historyList);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        return currentView;
    }

    /*
     * Preparing the list data
     */
    private  void prepareListDateNew(){
        historyList.add(new History("0", ContextCompat.getDrawable(getContext(), R.drawable.americano), "주문완료", "아메리카노 외 1건"));
        historyList.add(new History("1", ContextCompat.getDrawable(getContext(), R.drawable.americano), "주문완료", "아메리카노"));
        historyList.add(new History("2", ContextCompat.getDrawable(getContext(), R.drawable.americano), "주문완료", "카페라떼 외 1건"));

        historyList.get(0).addHistoryItem(new HistoryItem("아메리카노", "3", "3000"));
        historyList.get(0).addHistoryItem(new HistoryItem("카페라떼", "2", "3000"));

        historyList.get(1).addHistoryItem(new HistoryItem("아메리카노", "2", "3000"));
        historyList.get(1).addHistoryItem(new HistoryItem("카페라떼", "2", "3000"));

        historyList.get(2).addHistoryItem(new HistoryItem("아메리카노", "1", "3000"));
        historyList.get(2).addHistoryItem(new HistoryItem("카페라떼", "1", "3000"));

    }

    /**
     * 서버 통신 후 리스트 조회
     */
    private void orderRequest() {
        String url = getString(R.string.server_url) + "/orders/search_history?userId=test";
        JsonObjectRequest socRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // 아이디가 있을 경우 이 로직 실행함
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 아이디가 없을 경우 이 로직 실행함
                VolleyLog.d("error", error);
                currentView.findViewById(R.id.hisExp).setVisibility(View.GONE);
                currentView.findViewById(R.id.noCurrentResult).setVisibility(View.VISIBLE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(socRequest);
    }

}
