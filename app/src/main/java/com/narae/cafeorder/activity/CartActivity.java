package com.narae.cafeorder.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.narae.cafeorder.cart.CartMenuListViewAdapter;
import com.narae.cafeorder.cart.CartMenuListViewItem;
import com.narae.cafeorder.database.DBManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private Button btnOrder;

    DBManager manager;

    String JSONString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnOrder = (Button) findViewById(R.id.btnOrder);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new DBManager(this);

        final ListView listview ;
        CartMenuListViewAdapter adapter;

        // Adapter 생성
        adapter = new CartMenuListViewAdapter(this) ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.lv_cart);
        listview.setAdapter(adapter);

        List<CartMenuListViewItem> list = manager.selectCartList(this);

        adapter.setItem(list);

        if(manager.cartTotalCount()>0) {
            TextView totalCountText = (TextView) findViewById(R.id.totalCount);
            TextView totalPriceText = (TextView) findViewById(R.id.totalPrice);
            totalCountText.setText(manager.cartTotalCount() + "건");
            totalPriceText.setText(manager.cartTotalPrice() + "원");
            findViewById(R.id.noItemResult).setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
            findViewById(R.id.totalResult).setVisibility(View.VISIBLE);
        } else {
            listview.setVisibility(View.GONE);
            findViewById(R.id.totalResult).setVisibility(View.GONE);
        }

        btnOrder.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cart) {
            startActivity(new Intent(CartActivity.this, BestActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOrder:
                //로직
                List<CartMenuListViewItem> list = manager.selectCartList(this);
                int listCount = manager.cartTotalCount();
                String userId = manager.selectUserId();

                JSONString += "{ \"userId\" : \"" + userId + "\", \"coffees\" : [ ";

                for(int i=0; i<listCount; i++) {
                    String keyName = String.valueOf(list.get(i).getKeyName());
                    String count = String.valueOf(list.get(i).getCount());
                    String size = String.valueOf(list.get(i).getSize());
                    String temperature = String.valueOf(list.get(i).getTemperature());
                    String totalPrice = String.valueOf(list.get(i).getTotalPrice());
                    int compareCount = i+1;
                    if(listCount == compareCount) {
                        JSONString += "{\"keyName\" : \"" + keyName + "\", \"count\" : " + count + ", \"size\" : \"" + size + "\", \"temperature\" : \"" + temperature + "\", \"totalPrice\" : " + totalPrice + "}";
                    } else {
                        JSONString += "{\"keyName\" : \"" + keyName + "\", \"count\" : " + count + ", \"size\" : \"" + size + "\", \"temperature\" : \"" + temperature + "\", \"totalPrice\" : " + totalPrice + "}, ";
                    }
                }

                JSONString += " ] }";

                orderRequest();
        }
    }

    /**
     * 서버 통신 후 주문
     */
    private void orderRequest() {
        String url = getString(R.string.server_url) + "/orders";
        JSONObject json = null;
        try {
            json = new JSONObject(JSONString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest socRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(CartActivity.this);
                alert_confirm.setMessage("주문이 신청되었습니다.").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                manager.allDeleteCartList(); //장바구니 리스트 삭제
                                finish(); //이전 화면으로 넘기기
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error!!", error);
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(CartActivity.this);
                alert_confirm.setMessage("주문이 실패했습니다. 다시 주문해주세요.").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return; // 확인 후 제자리에 멈추기
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        requestQueue.add(socRequest);
    }

}
