package com.narae.cafeorder.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.narae.cafeorder.R;
import com.narae.cafeorder.adapter.CurrentListAdapter;
import com.narae.cafeorder.database.DBManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CurrentFragment extends Fragment{

    CurrentListAdapter adapter ;
    View currentView;
    DBManager manager;

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

        manager = new DBManager(getContext());

        // Adapter 생성 및 Adapter 지정.
        adapter = new CurrentListAdapter() ;

        ListView listview = (ListView)currentView.findViewById(R.id.statuslist);
        listview.setAdapter(adapter);

        orderRequest();

        return currentView;
    }

    /**
     * 서버 통신 후 리스트 조회
     */
    private void orderRequest() {
        String userId = manager.selectUserId();
        String url = getString(R.string.server_url) + "/orders/current?userId=" + userId;
        JsonObjectRequest socRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;
                int totalCount = 0;
                int totalPrice = 0;
                // 리스트가 있을 경우 이 로직 실행함
                try {
                    jsonArray = response.getJSONArray("coffees");
                    statusSetting(response.getString("orderStatus"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                totalCount = jsonArray.length();
                for (int i = 0; i < totalCount; i++) {
                    JSONObject coffee = null;
                    try {
                        coffee = jsonArray.getJSONObject(i);
                        String keyName = coffee.getString("keyName");
                        String detailString = coffee.getString("size") + "/" + coffee.getString("temperature") + "/" + coffee.getString("count") + "건/" + coffee.getString("totalPrice") + "원";
                        totalPrice = totalPrice + coffee.getInt("totalPrice");
                        adapter.addItem(keyName, detailString) ;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                TextView totalCountText = (TextView) currentView.findViewById(R.id.totalCount);
                TextView totalPriceText = (TextView) currentView.findViewById(R.id.totalPrice);

                totalCountText.setText(totalCount + "건");
                totalPriceText.setText(totalPrice + "원");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 리스트가 없을 경우 이 로직 실행함
                VolleyLog.d("error", error);
                currentView.findViewById(R.id.currentResult).setVisibility(View.GONE);
                currentView.findViewById(R.id.noCurrentResult).setVisibility(View.VISIBLE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(socRequest);
    }

    private void statusSetting(String orderStatus) {
        String currentStatusText = "상태를 불러올 수 없습니다.";
        if (orderStatus.equals("ORDER")) {
            ImageView statusImg = (ImageView) currentView.findViewById(R.id.startbullet);
            statusImg.setImageResource(R.drawable.startvisible);
            currentStatusText = "주문이 신청되었습니다.";
        }
        if (orderStatus.equals("ACCEPT")) {
            ImageView statusImg = (ImageView) currentView.findViewById(R.id.middlebullet1);
            statusImg.setImageResource(R.drawable.middlevisible);
            currentStatusText = "주문이 승인되었습니다.";
        }
        if (orderStatus.equals("MAKING")) {
            ImageView statusImg = (ImageView) currentView.findViewById(R.id.middlebullet2);
            statusImg.setImageResource(R.drawable.middlevisible);
            currentStatusText = "음료를 제조하고 있습니다.";
        }
        if (orderStatus.equals("COMPLETE")) {
            ImageView statusImg = (ImageView) currentView.findViewById(R.id.endbullet);
            statusImg.setImageResource(R.drawable.endvisible);
            currentStatusText = "주문이 완료되었습니다.";
        }
        TextView currentText = (TextView) currentView.findViewById(R.id.currentText);
        currentText.setText(currentStatusText);
    }

}
