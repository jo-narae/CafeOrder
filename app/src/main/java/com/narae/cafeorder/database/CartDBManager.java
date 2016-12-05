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

    /**
     * 생성자
     */
    public CartDBManager(Context context) {
        this.context = context;
        this.opener = new OpenHelper(context, db_name, null, dbVersion);
        db = opener.getWritableDatabase();
    }

    /**
     * Opener of DB and Table
     */
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

    /**
     * 장바구니 데이터 입력
     */
    public boolean insertCartList(String key_name, String eng_name, String kor_name, String count, String total_price, String size, String temperature) {
        String sql = "INSERT INTO " + table_name + "(key_name, eng_name, kor_name, count, total_price, size, temperature) " +
                "VALUES ('" + key_name + "', '"+ eng_name + "', '" + kor_name + "', '" + count + "', '" + total_price + "', '" + size + "', '" + temperature + "');";
        db.execSQL(sql);
        return true;
    }

    /**
     * 장바구니 총 개수 조회
     */
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

    /**
     * 장바구니 총 주문금액 조회
     */
    public String cartTotalPrice() {
        String sql = "select sum(total_price) as total_price from " + table_name;
        Cursor c = db.rawQuery(sql, null);
        String price = "";

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(c.moveToFirst()) {
            price = c.getString(c.getColumnIndex("total_price"));
            c.close();
            return price;
        }
        c.close();
        return price;
    }

    /**
     * 장바구니 리스트
     */
    public List<CartMenuListViewItem> selectCartList(Context context) {
        String sql = "select * from " + table_name;
        Cursor result =  db.rawQuery(sql, null);

        ArrayList<CartMenuListViewItem> arrayList = new ArrayList<>();
        result.moveToFirst();
        while(!result.isAfterLast()){
            String uri = "@drawable/" + result.getString(result.getColumnIndex("key_name"));
            String packName = context.getPackageName(); // 패키지명
            int imageResource = context.getResources().getIdentifier(uri, null, packName);

            CartMenuListViewItem item = new CartMenuListViewItem();
            item.setIcon(ContextCompat.getDrawable(context, imageResource));
            item.setSeq(result.getString(result.getColumnIndex("seq")));
            item.setEngName(result.getString(result.getColumnIndex("eng_name")));
            item.setKorName(result.getString(result.getColumnIndex("kor_name")));
            item.setCount(result.getString(result.getColumnIndex("count")));
            item.setTotalPrice(result.getString(result.getColumnIndex("total_price")));
            item.setTemperature(result.getString(result.getColumnIndex("temperature")));
            item.setSize(result.getString(result.getColumnIndex("size")));
            arrayList.add(item);
            result.moveToNext();
        }

        return arrayList;
    }

    /**
     * 장바구니 데이터 삭제
     */
    public void deleteCartList(String seq) {
        String sql = "DELETE FROM " + table_name + " WHERE seq=" + seq;
        db.execSQL(sql);
    }

    /**
     * 장바구니 개수 및 총 가격 수정
     */
    public void updateCartList(String seq, String count, String total_price) {
        String sql = "UPDATE " + table_name + " SET count = " + count + ", total_price = " + total_price + " WHERE seq=" + seq;
        db.execSQL(sql);
    }

}
