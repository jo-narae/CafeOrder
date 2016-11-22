package com.narae.cafeorder.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.narae.cafeorder.R;
import com.narae.cafeorder.best.BestMenuListViewAdapter;
import com.narae.cafeorder.best.BestMenuListViewItem;
import com.narae.cafeorder.menu.MenuListViewAdapter;
import com.narae.cafeorder.menu.MenuListViewItem;

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
        adapter.addItem(1, ContextCompat.getDrawable(this, R.drawable.americano),
                "Box", "30") ;
        // 두 번째 아이템 추가.
        adapter.addItem(2, ContextCompat.getDrawable(this, R.drawable.americano),
                "Circle", "15") ;
        // 세 번째 아이템 추가.
        adapter.addItem(3, ContextCompat.getDrawable(this, R.drawable.americano),
                "Ind", "20") ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                BestMenuListViewItem item = (BestMenuListViewItem) parent.getItemAtPosition(position) ;
                String titleStr = item.getTitle() ;
                String descStr = item.getSellCount() ;
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
