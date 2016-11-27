package com.narae.cafeorder.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.narae.cafeorder.R;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonIntro;
    private TextView user_id;
    private TextView user_name;
    private TextView user_password;
    private TextView password_check;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        buttonIntro = (Button) findViewById(R.id.buttonintro);
        user_id = (TextView) findViewById(R.id.user_id);
        user_name = (TextView) findViewById(R.id.user_name);
        user_password = (TextView) findViewById(R.id.user_password);
        password_check = (TextView) findViewById(R.id.password_check);

        createDatabase("testdb");

        buttonIntro.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonintro:
                if(validationCheck()) {
                    //startActivity(new Intent(IntroActivity.this, MainActivity.class));
                    //break;
                }
                break;
        }
    }

    boolean isTableExists(SQLiteDatabase db, String tableName)
    {
        if (tableName == null || db == null || !db.isOpen())
        {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    private void createDatabase(String name) {
        try {
            //database is connected
            db = openOrCreateDatabase(
                    name,
                    Activity.MODE_PRIVATE,
                    null
            );
            if(!isTableExists(db, "user_info")) {
                //table create
                createTable("user_info");
            } else {
                //table exist
                selectRecord("user_info");
            }
        } catch (Exception e) {
            //database is not connected
            e.printStackTrace();
        }
    }

    private void createTable(String name) {
        System.out.print("create table [" + name + "]");

        try {
            //table is created
            db.execSQL("create table if not exists " + name + "("
                    + "user_id text PRIMARY KEY, "
                    + "user_name text,"
                    + "user_password text);" );
        } catch (Exception e) {
            //table is not created
            e.printStackTrace();
        }
    }

    private void insertRecord(String name) {
        db.execSQL("INSERT INTO " + name + " VALUES ('" + user_id.getText().toString() + "', '"+ user_name.getText().toString() + "', '" + user_password.getText().toString() + "');");
    }

    private void selectRecord(String name) {
        Cursor c = db.rawQuery("SELECT * FROM " + name, null);
        if(c.moveToFirst()) {
            // 이미 가입한 경우
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            c.close();
        }
        c.close();
    }

    /**
     * 유효성 검사(미입력, 비밀번호 체크)
     */
    private boolean validationCheck() {
        if(user_id.getText().toString().equals("")){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "아이디를 입력해주세요.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
            return false;
        }
        if(user_name.getText().toString().equals("")){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "이름 또는 별명을 입력해주세요.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
            return false;
        }
        if(user_password.getText().toString().equals("")){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "비밀번호를 입력해주세요.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
            return false;
        }
        if(password_check.getText().toString().equals("")){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "비밀번호를 재입력해주세요.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
            return false;
        }
        if(!user_password.getText().toString().equals(password_check.getText().toString())){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "비밀번호와 비밀번호 재입력이\n일치하지 않습니다.\n다시 확인해주세요.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
            return false;
        }
        insertRecord("user_info");
        selectRecord("user_info"); //아이디 조회 후 다음 페이지로 넘어가도록 함
        return true;
    }
}
