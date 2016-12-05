package com.narae.cafeorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.narae.cafeorder.R;
import com.narae.cafeorder.cart.CartMenuListViewAdapter;
import com.narae.cafeorder.cart.CartMenuListViewItem;
import com.narae.cafeorder.database.CartDBManager;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private Toolbar toolbar;

    CartDBManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new CartDBManager(this);

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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cart) {
            startActivity(new Intent(CartActivity.this, BestActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
