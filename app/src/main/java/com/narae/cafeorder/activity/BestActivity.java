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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.narae.cafeorder.R;
import com.narae.cafeorder.best.BestMenuListViewAdapter;
import com.narae.cafeorder.best.BestMenuListViewItem;

public class BestActivity extends AppCompatActivity {

    private View seletedItem;
    private Toolbar toolbar;

    private LayerDrawable mCartMenuIcon;
    private int mCartCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final ListView listview ;
        BestMenuListViewAdapter adapter;

        // Adapter 생성
        adapter = new BestMenuListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.lv_best);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.rank1), ContextCompat.getDrawable(this, R.drawable.americano),
                "아메리카노", "300건") ;
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.rank2), ContextCompat.getDrawable(this, R.drawable.americano),
                "카푸치노", "200건") ;
        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.rank3), ContextCompat.getDrawable(this, R.drawable.americano),
                "얼 그레이 티", "100건") ;

        // 네 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.rank4), ContextCompat.getDrawable(this, R.drawable.americano),
                "카페 라떼", "70건") ;
        // 다섯 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.rank5), ContextCompat.getDrawable(this, R.drawable.americano),
                "카페 모카", "20건") ;

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

    public void onClickIncrementCartCount(View view) {
        setBadgeCount(this, mCartMenuIcon, String.valueOf(mCartCount++));
    }
}
