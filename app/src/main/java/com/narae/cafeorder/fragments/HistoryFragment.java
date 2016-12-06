package com.narae.cafeorder.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.narae.cafeorder.R;
import com.narae.cafeorder.adapter.HistoryListAdapter;
import com.narae.cafeorder.history.History;
import com.narae.cafeorder.history.HistoryItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HistoryFragment extends Fragment{

    HistoryListAdapter listAdapter;
    ExpandableListView expListView;
    List<History> historyList;

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
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.hisExp);

        prepareListDateNew();
        listAdapter = new HistoryListAdapter(getContext(), historyList);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        return view;
    }

    /*
     * Preparing the list data
     */
    private  void prepareListDateNew(){
        historyList = new ArrayList<>();
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

}
