package com.narae.cafeorder.cart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.narae.cafeorder.R;
import com.narae.cafeorder.database.CartDBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by existmaster on 2016. 11. 13..
 */

public class CartMenuListViewAdapter extends BaseAdapter {

    CartDBManager manager;

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private List<CartMenuListViewItem> MenuListViewItemList = new ArrayList<CartMenuListViewItem>() ;

    // ListViewAdapter의 생성자
    public CartMenuListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return MenuListViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cart_menu_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.cartImg) ;
        TextView korNameText = (TextView) convertView.findViewById(R.id.korNameText) ;
        TextView engNameText = (TextView) convertView.findViewById(R.id.engNameText) ;
        TextView priceText = (TextView) convertView.findViewById(R.id.priceText) ;
        TextView countText = (TextView) convertView.findViewById(R.id.countText) ;

        Button btnPlus = (Button) convertView.findViewById(R.id.btnPlus);
        Button btnMinus = (Button) convertView.findViewById(R.id.btnMinus);
        Button btnDelete = (Button) convertView.findViewById(R.id.btnDelete);

        // Data Set(MenuListViewItemList)에서 position에 위치한 데이터 참조 획득
        CartMenuListViewItem cartMenuListViewItem = MenuListViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageDrawable(cartMenuListViewItem.getIcon());
        korNameText.setText(cartMenuListViewItem.getKorName());
        engNameText.setText(cartMenuListViewItem.getEngName());
        priceText.setText(cartMenuListViewItem.getTotalPrice()+"원");
        countText.setText(cartMenuListViewItem.getCount());
        btnDelete.setSelected(true); //색깔 적용

        btnPlus.setOnClickListener(new ButtonClickListener(pos));
        btnMinus.setOnClickListener(new ButtonClickListener(pos));
        btnDelete.setOnClickListener(new ButtonClickListener(pos));

        btnDelete.setTag(cartMenuListViewItem.getSeq());

        manager = new CartDBManager(context);

        return convertView;
    }

    private class ButtonClickListener implements View.OnClickListener {

        private int position;

        ButtonClickListener() {
        }

        ButtonClickListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnPlus:
                    Log.d("Plus", "Plus");
                    break;
                case R.id.btnMinus:
                    Log.d("Minus", "Minus");
                    break;
                case R.id.btnDelete:
                    manager.deleteCartList(String.valueOf(view.getTag()));
                    MenuListViewItemList.remove(position);
                    notifyDataSetInvalidated();
                    break;
            }

        }
    }
    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return MenuListViewItemList.get(position) ;
    }

    //아이템 데이터 추가
    public void setItem(List<CartMenuListViewItem> list){
        MenuListViewItemList = list;
    }
}
