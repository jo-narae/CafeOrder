package com.narae.cafeorder.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.narae.cafeorder.R;
import com.narae.cafeorder.cart.CartMenuListViewAdapter;
import com.narae.cafeorder.cart.CartMenuListViewItem;
import com.narae.cafeorder.database.CartDBManager;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private View seletedItem;
    private Toolbar toolbar;

    private LayerDrawable mCartMenuIcon;
    private int mCartCount;

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
        adapter = new CartMenuListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.lv_best);
        listview.setAdapter(adapter);

        List<CartMenuListViewItem> list = manager.selectCartList(this);

        adapter.setItem(list);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mCartMenuIcon = (LayerDrawable) menu.findItem(R.id.action_cart).getIcon();
        setBadgeCount(this, mCartMenuIcon, String.valueOf(mCartCount++));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cart) {
            startActivity(new Intent(CartActivity.this, BestActivity.class));
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

    public void onClickIncrementCartCount(View view) {
        setBadgeCount(this, mCartMenuIcon, String.valueOf(mCartCount++));
    }
}
