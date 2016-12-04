package com.narae.cafeorder.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.ContextCompat;

import com.narae.cafeorder.R;
import com.narae.cafeorder.cart.CartMenuListViewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 성열 on 2016-11-30.
 */

public class CartDBManager {

    // DB관련 상수 선언
    private static final String db_name = "testdb2";
    private static final String table_name = "cart_list";
    public static final int dbVersion = 1;

    // DB관련 객체 선언
    private OpenHelper opener; // DB opener
    private SQLiteDatabase db; // DB controller

    // 부가적인 객체들
    private Context context;

    // 생성자
    public CartDBManager(Context context) {
        this.context = context;
        this.opener = new OpenHelper(context, db_name, null, dbVersion);
        db = opener.getWritableDatabase();
    }

    // Opener of DB and Table
    private class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory,
                          int version) {
            super(context, name, null, version);
            // TODO Auto-generated constructor stub

        }

        // 생성된 DB가 없을 경우에 한번만 호출됨
        @Override
        public void onCreate(SQLiteDatabase arg0) {
            String createSql = "create table " + table_name + " ("
                    + "seq INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "key_name text, "
                    + "eng_name text,"
                    + "kor_name text,"
                    + "count text,"
                    + "total_price text,"
                    + "size text,"
                    + "temperature text)";
            arg0.execSQL(createSql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub
        }
    }

    // 담기 데이터 추가
    public boolean insertCartList(String key_name, String eng_name, String kor_name, String count, String total_price, String size, String temperature) {
        String sql = "INSERT INTO " + table_name + "(key_name, eng_name, kor_name, count, total_price, size, temperature) " +
                "VALUES ('" + key_name + "', '"+ eng_name + "', '" + kor_name + "', '" + count + "', '" + total_price + "', '" + size + "', '" + temperature + "');";
        db.execSQL(sql);
        return true;
    }

    // 장바구니 총 갯수 조회
    public int cartTotalCount() {
        String sql = "select * from " + table_name;
        Cursor c = db.rawQuery(sql, null);
        int count = 0;

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(c.moveToFirst()) {
            count = c.getCount();
            c.close();
            return count;
        }
        c.close();
        return count;
    }

    public List<CartMenuListViewItem> selectCartMenuList(Context context) {
        String sql = "select kor_name, eng_name, count, total_price from " + table_name;
        Cursor result =  db.rawQuery(sql, null);

        ArrayList<CartMenuListViewItem> arrayList = new ArrayList<>();
        result.moveToFirst();
        while(!result.isAfterLast()){
            CartMenuListViewItem item = new CartMenuListViewItem();
            item.setIcon(ContextCompat.getDrawable(context, R.drawable.americano));
            item.setTitle(result.getString(result.getColumnIndex("eng_name")));
            item.setSellCount(result.getString(result.getColumnIndex("count")));
            arrayList.add(item);
            result.moveToNext();
        }

        return arrayList;
    }

}
