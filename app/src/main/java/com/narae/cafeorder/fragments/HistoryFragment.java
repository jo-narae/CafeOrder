package com.narae.cafeorder.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.narae.cafeorder.R;
import com.narae.cafeorder.adapter.HistoryListAdapter;
import com.narae.cafeorder.database.DBManager;
import com.narae.cafeorder.history.History;
import com.narae.cafeorder.history.HistoryItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment{

    HistoryListAdapter listAdapter;
    ExpandableListView expListView;
    List<History> historyList;

    View currentView;

    DBManager manager;

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

        manager = new DBManager(getContext());

        historyList = new ArrayList<>();

        // get the listview
        expListView = (ExpandableListView) currentView.findViewById(R.id.hisExp);

        orderRequest();

        listAdapter = new HistoryListAdapter(getContext(), historyList);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        return currentView;
    }

    /**
     * 서버 통신 후 리스트 조회
     */
    private void orderRequest() {
        String userId = manager.selectUserId();
        String url = getString(R.string.server_url) + "/orders/search_history?userId=" + userId;
        Log.d("url", url);
        JsonArrayRequest socRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // 리스트가 있을 경우 이 로직 실행함
                for(int i=0; response.length() > i; i++) {
                    try {
                        String orderStatus = "";
                        if(response.getJSONObject(i).getString("orderStatus").equals("ORDER")) {
                            orderStatus = "주문 신청";
                        }
                        if(response.getJSONObject(i).getString("orderStatus").equals("ACCEPT")) {
                            orderStatus = "주문 승인";
                        }
                        if(response.getJSONObject(i).getString("orderStatus").equals("MAKING")) {
                            orderStatus = "음료 제조";
                        }
                        if(response.getJSONObject(i).getString("orderStatus").equals("COMPLETE")) {
                            orderStatus = "주문 완료";
                        }
                        JSONArray jsonArray = null;
                        jsonArray = response.getJSONObject(i).getJSONArray("coffees");

                        String uri = "@drawable/" + jsonArray.getJSONObject(0).getString("keyName");
                        String packName = getContext().getPackageName(); // 패키지명
                        int imageResource = getResources().getIdentifier(uri, null, packName);

                        String detailString = jsonArray.getJSONObject(0).getString("korName"); //"아메리카노 외 1건"
                        if(jsonArray.length() > 1) {
                            int count = jsonArray.length();
                            count = count - 1;
                            detailString += " 외 " + count + "건";
                        }

                        historyList.add(new History(String.valueOf(i), imageResource, orderStatus, detailString));

                        for(int j=0; jsonArray.length() > j; j++) {
                            String korName = jsonArray.getJSONObject(j).getString("korName");
                            String itemDetailString = jsonArray.getJSONObject(j).getString("size") + "/" + jsonArray.getJSONObject(j).getString("temperature") + "/" + jsonArray.getJSONObject(j).getString("count") + "건/" + jsonArray.getJSONObject(j).getString("totalPrice") + "원";
                            historyList.get(i).addHistoryItem(new HistoryItem(korName, itemDetailString));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(response.length() == 0) { //리스트가 없을 경우 이 로직을 실행함
                    currentView.findViewById(R.id.hisExp).setVisibility(View.GONE);
                    currentView.findViewById(R.id.noCurrentResult).setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 리스트가 없을 경우 이 로직 실행함
                VolleyLog.d("error", error);
                currentView.findViewById(R.id.hisExp).setVisibility(View.GONE);
                currentView.findViewById(R.id.noCurrentResult).setVisibility(View.VISIBLE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(socRequest);
    }

}
