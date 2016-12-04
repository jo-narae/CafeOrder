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

        List<CartMenuListViewItem> list = manager.selectCartMenuList(this);

        adapter.setItem(list);
/*        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.americano),
                "아메리카노", "Americano") ;
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.americano),
                "카페라떼", "Cafe Latte") ;
        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.americano),
                "카페모카", "Caffe Mocha") ;*/

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                CartMenuListViewItem item = (CartMenuListViewItem) parent.getItemAtPosition(position) ;
                String titleStr = item.getTitle() ;
                String descStr = item.getSellCount() ;
                Drawable iconDrawable = item.getIcon();
                Log.d("test1", "test3333");
                if(seletedItem!=null) {
                    Log.d("test1", "test");
                    seletedItem.findViewById(R.id.hiddenCount).setVisibility(View.GONE);
                }

                if(seletedItem!=v) {
                    seletedItem = v;
                    Log.d("test2", "test");
                    v.findViewById(R.id.hiddenCount).setVisibility(View.VISIBLE);
                } else {
                    Log.d("test3", "test");
                    seletedItem = null;
                }
                // TODO : use item data.
            }
        }) ;
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
