package com.narae.cafeorder.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.narae.cafeorder.R;
import com.narae.cafeorder.adapter.CurrentListAdapter;

import org.json.JSONException;
import org.json.JSONObject;


public class CurrentFragment extends Fragment{

    CurrentListAdapter adapter ;
    View currentView;

    public CurrentFragment() {
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
        currentView = inflater.inflate(R.layout.fragment_current, container, false);

        // Adapter 생성 및 Adapter 지정.
        adapter = new CurrentListAdapter() ;

        adapter.addItem("아메리카노", "Tall/Hot/2건/6000원") ;
        adapter.addItem("카페라떼", "Tall/Iced/1건/4000원") ;

        ListView listview = (ListView)currentView.findViewById(R.id.statuslist);
        listview.setAdapter(adapter);

        orderRequest();

        return currentView;

    }

    /**
     * 서버 통신 후 리스트 조회
     */
    private void orderRequest() {
        String url = getString(R.string.server_url) + "/orders/current?userId=test";
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
                currentView.findViewById(R.id.currentResult).setVisibility(View.GONE);
                currentView.findViewById(R.id.noCurrentResult).setVisibility(View.VISIBLE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(socRequest);
    }

}
