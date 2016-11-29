package com.narae.cafeorder.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.narae.cafeorder.R;
import com.narae.cafeorder.menu.MenuListViewAdapter;
import com.narae.cafeorder.menu.MenuListViewItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EspressoFragment extends Fragment{

    MenuListViewAdapter adapter;
    ListView listview ;

    private View seletedItem;

    public EspressoFragment() {
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

        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        String url = getString(R.string.server_url) + "/coffeeinfo";

        // Adapter 생성
        adapter = new MenuListViewAdapter() ;
        listview = (ListView) view.findViewById(R.id.menulistview);

        sendRequest(url);
        settingItemClick();

        return view;
    }

    private void sendRequest(String url) {
        JsonArrayRequest socRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject coffee = response.getJSONObject(i);
                        String eng_name = coffee.getString("engName");
                        String kor_name = coffee.getString("korName");
                        String coffee_type = coffee.getString("coffeeType");
                        String price = coffee.getString("price") + "원";
                        String img_name = coffee.getString("keyName");

                        String uri = "@drawable/" + img_name;
                        String packName = getContext().getPackageName(); // 패키지명
                        int imageResource = getResources().getIdentifier(uri, null, packName);

                        if(coffee_type.equals("ESPRESSO")) {
                            adapter.addItem(ContextCompat.getDrawable(getContext(), imageResource), eng_name, kor_name, price);
                        }

                        // 리스트뷰 참조 및 Adapter달기
                        listview.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR" + error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(socRequest);

    }

    private void settingItemClick() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                MenuListViewItem item = (MenuListViewItem) parent.getItemAtPosition(position) ;
                String titleStr = item.getTitle() ;
                String descStr = item.getDesc() ;
                Drawable iconDrawable = item.getIcon();

                if(seletedItem!=null) {
                    seletedItem.findViewById(R.id.hiddenCount).setVisibility(View.GONE);
                    seletedItem.findViewById(R.id.hiddenMenuLayout).setVisibility(View.GONE);
                }

                if(seletedItem!=v) {
                    seletedItem = v;
                    v.findViewById(R.id.hiddenCount).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.hiddenMenuLayout).setVisibility(View.VISIBLE);
                } else {
                    seletedItem = null;
                }
                // TODO : use item data.
            }
        });
    }

}
