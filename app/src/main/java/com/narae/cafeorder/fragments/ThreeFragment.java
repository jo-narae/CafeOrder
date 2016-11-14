package com.narae.cafeorder.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.narae.cafeorder.R;
import com.narae.cafeorder.adapter.CurrentListAdapter;


public class ThreeFragment extends Fragment{

    CurrentListAdapter adapter ;

    public ThreeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        // Adapter 생성 및 Adapter 지정.
        adapter = new CurrentListAdapter() ;

        adapter.addItem("아메리카노", "Tall/Hot/2건/6000원") ;
        adapter.addItem("카페라떼", "Tall/Iced/1건/4000원") ;

        ListView listview = (ListView)view.findViewById(R.id.statuslist);
        listview.setAdapter(adapter);

        return view;

    }

}
