package com.narae.cafeorder.database;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * Created by 성열 on 2016-11-30.
 */

public class DBManager {

    // DB관련 상수 선언
    private static final String db_name = "testdb";
    private static final String table_name = "user_info";
    public static final int dbVersion = 1;

    // DB관련 객체 선언
    private OpenHelper opener; // DB opener
    private SQLiteDatabase db; // DB controller

    // 부가적인 객체들
    private Context context;

    // 생성자
    public DBManager(Context context) {
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
            // String dropSql = "drop table if exists " + tableName;
            // db.execSQL(dropSql);

            String createSql = "create table " + table_name + " ("
                    + "user_id text PRIMARY KEY, "
                    + "user_name text,"
                    + "user_password text)";
            arg0.execSQL(createSql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub
        }
    }

    // 유저 데이터 추가
    public void insertUserInfo(String user_id, String user_name, String user_password) {
        String sql = "INSERT INTO " + table_name + " VALUES ('" + user_id + "', '"+ user_name + "', '" + user_password + "');";
        db.execSQL(sql);
    }

    // sqlite 내장 DB에 데이터가 있는지 체크
    public boolean selectUserInfo() {
        String sql = "select * from " + table_name;
        Cursor c = db.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(c.moveToFirst()) {
            c.close();
            return true;
        }
        c.close();
        return false;
    }

}
