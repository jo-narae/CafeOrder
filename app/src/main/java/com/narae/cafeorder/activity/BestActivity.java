package com.narae.cafeorder.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.narae.cafeorder.R;
import com.narae.cafeorder.best.BestMenuListViewAdapter;
import com.narae.cafeorder.database.DBManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BestActivity extends AppCompatActivity {

    BestMenuListViewAdapter adapter;
    private Toolbar toolbar;

    private LayerDrawable mCartMenuIcon;
    private int mCartCount;

    DBManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final ListView listview ;

        // Adapter 생성
        adapter = new BestMenuListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.lv_best);
        listview.setAdapter(adapter);

        top5ListData();

        manager = new DBManager(this);
        mCartCount = manager.cartTotalCount();

    }

    /**
     * 뒤로 가기시 재실행
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        mCartCount = manager.cartTotalCount();
        setBadgeCount(this, mCartMenuIcon, String.valueOf(mCartCount));
    }

    private void top5ListData() {
        String url = getString(R.string.server_url) + "/coffeeinfo/top5";
        JsonArrayRequest socRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject coffee = response.getJSONObject(i);
                        String korName = coffee.getString("korName");
                        String sellCount = coffee.getString("sellCount") + "건";
                        String img_name = coffee.getString("keyName");

                        String packName = getApplication().getPackageName(); // 패키지명
                        String coffeeImg = "@drawable/" + img_name;
                        int coffeeImageResource = getResources().getIdentifier(coffeeImg, null, packName);

                        int rankNumber = i;
                        rankNumber++;
                        String rankImage = "@drawable/rank" + rankNumber;
                        int rankImageResource = getResources().getIdentifier(rankImage, null, packName);

                        adapter.addItem(ContextCompat.getDrawable(getApplicationContext(), rankImageResource), ContextCompat.getDrawable(getApplicationContext(), coffeeImageResource),
                                korName, sellCount) ;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                adapter.notifyDataSetInvalidated(); //비동기 통신시 리스트 출력이 느리거나 안되는 경우가 있어 adapter를 notify시킴
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR" + error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        requestQueue.add(socRequest);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mCartMenuIcon = (LayerDrawable) menu.findItem(R.id.action_cart).getIcon();
        setBadgeCount(this, mCartMenuIcon, String.valueOf(mCartCount));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cart) {
            startActivity(new Intent(BestActivity.this, CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
}
