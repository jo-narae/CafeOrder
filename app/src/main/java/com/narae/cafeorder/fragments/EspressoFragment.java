package com.narae.cafeorder.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

    /**
     * 아이템 선택
     */
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
                    //해당 아이템 초기화
                    TextView countText = (TextView)seletedItem.findViewById(R.id.countText);
                    countText.setText("1");
                    seletedItem.findViewById(R.id.coffeeICED).setSelected(false);
                }

                if(seletedItem!=v) {
                    seletedItem = v;
                    v.findViewById(R.id.hiddenCount).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.hiddenMenuLayout).setVisibility(View.VISIBLE);
                    seletedItem.findViewById(R.id.coffeeHOT).setSelected(true);
                    v.findViewById(R.id.coffeeHOT).setOnClickListener(myListener);
                    v.findViewById(R.id.coffeeICED).setOnClickListener(myListener);
                    v.findViewById(R.id.countAdd).setOnClickListener(myListener);
                    v.findViewById(R.id.countDelete).setOnClickListener(myListener);
                } else {
                    seletedItem = null;
                }
                // TODO : use item data.
            }
        });
    }

    /**
     * 선택된 아이템에 해당하는 버튼 로직
     */
    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView countText = (TextView)seletedItem.findViewById(R.id.countText);
            int count = Integer.parseInt(countText.getText().toString());
            switch (view.getId()) {
                case R.id.countAdd:
                    count++; // 개수 증가
                    countText.setText(String.valueOf(count));
                    break;
                case R.id.countDelete:
                    if(count>1) { //최소 1 이하로는 떨어지지 않도록 한다
                        count--; // 개수 감소
                    }
                    countText.setText(String.valueOf(count));
                    break;
                case R.id.coffeeHOT:
                    seletedItem.findViewById(R.id.coffeeHOT).setSelected(true);
                    seletedItem.findViewById(R.id.coffeeICED).setSelected(false);
                    break;
                case R.id.coffeeICED :
                    seletedItem.findViewById(R.id.coffeeHOT).setSelected(false);
                    seletedItem.findViewById(R.id.coffeeICED).setSelected(true);
                    break;
            }
        }
    };

}
