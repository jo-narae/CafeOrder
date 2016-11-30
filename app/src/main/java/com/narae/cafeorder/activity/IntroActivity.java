package com.narae.cafeorder.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.narae.cafeorder.R;
import com.narae.cafeorder.database.DBManager;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonIntro;
    private TextView user_id;
    private TextView user_name;
    private TextView user_password;
    private TextView password_check;
    DBManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        buttonIntro = (Button) findViewById(R.id.buttonintro);
        user_id = (TextView) findViewById(R.id.user_id);
        user_name = (TextView) findViewById(R.id.user_name);
        user_password = (TextView) findViewById(R.id.user_password);
        password_check = (TextView) findViewById(R.id.password_check);

        manager = new DBManager(this);
        userCheck(); //디바이스 내 회원 정보가 있는지 체크

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
        manager.insertUserInfo(user_id.getText().toString(), user_name.getText().toString(), user_password.getText().toString()); //회원정보 입력
        userCheck(); //디바이스 내 회원 정보가 있는지 체크
        return true;
    }

    /**
     * 디바이스 내 회원 정보가 있는지 체크
     */
    private void userCheck() {
        if(manager.selectUserInfo()) {
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
        }
    }
}
