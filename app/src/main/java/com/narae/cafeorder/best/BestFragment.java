package com.narae.cafeorder.best;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.narae.cafeorder.R;
import com.narae.cafeorder.menu.MenuListViewAdapter;
import com.narae.cafeorder.menu.MenuListViewItem;

/**
 * Created by existmaster on 2016. 11. 14..
 */

public class BestFragment extends Fragment{

    private View seletedItem;

    public BestFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_best, container, false);

        final ListView listview ;
        MenuListViewAdapter adapter;

        // Adapter 생성
        adapter = new MenuListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) view.findViewById(R.id.lv_best);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.ic_tab_contacts),
                "Box", "Account Box Black 36dp") ;
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.ic_tab_contacts),
                "Circle", "Account Circle Black 36dp") ;
        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.ic_tab_contacts),
                "Ind", "Assignment Ind Black 36dp") ;

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
        }) ;

        return view;
    }

}
