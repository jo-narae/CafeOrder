package com.narae.cafeorder.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.narae.cafeorder.R;
import com.narae.cafeorder.database.DBManager;

import org.json.JSONArray;
import org.json.JSONObject;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonIntro;
    private Button buttonidcheck;
    private TextView user_id;
    private TextView user_name;
    private TextView user_password;
    private TextView password_check;
    DBManager manager;

    private boolean idCheckFlag = false;
    private boolean idValidationFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        buttonIntro = (Button) findViewById(R.id.buttonintro);
        buttonidcheck = (Button) findViewById(R.id.buttonidcheck);
        user_id = (TextView) findViewById(R.id.user_id);
        user_name = (TextView) findViewById(R.id.user_name);
        user_password = (TextView) findViewById(R.id.user_password);
        password_check = (TextView) findViewById(R.id.password_check);

        manager = new DBManager(this);
        userCheck(); //디바이스 내 회원 정보가 있는지 체크

        buttonIntro.setOnClickListener(this);
        buttonidcheck.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonintro:
                if(validationCheck()) {
                    userCheck(); //디바이스 내 회원 정보가 있는지 체크
                    break;
                }
                break;
            case R.id.buttonidcheck:
                idOverlapCheck();
                break;
        }
    }

    /**
     * 아이디 중복체크(서버통신)
     */
    private void idOverlapCheck() {
        // 아이디 중복체크를 위해서 아이디를 입력 받는다.
        if(user_id.getText().toString().equals("")){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "아이디를 입력해주세요.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
            return;
        }
        // 아이디를 입력 받은 후 서버 통신을 한다.
        String url = getString(R.string.server_url) + "/users/" + user_id.getText().toString();
        sendRequest(url);
        Log.d("idCheckFlag : ", String.valueOf(idCheckFlag));
        if(idCheckFlag) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "아이디가 존재합니다.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
        }
    }

    /**
     * 서버통신
     * @param url
     */
    private void sendRequest(String url) {
        JsonObjectRequest socRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // 아이디가 있을 경우 이 로직 실행함
                idCheckFlag = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 아이디가 없을 경우 이 로직 실행함
                idCheckFlag = true;
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        requestQueue.add(socRequest);

    }

    /**
     * 유효성 검사(미입력, 비밀번호 체크)
     */
    private boolean validationCheck() {
        if(user_id.getText().toString().equals("")){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "아이디를 입력해주세요.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
            return false;
        }
        if(!idValidationFlag) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "아이디 중복체크를 해주세요.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
            return false;
        }
        if(user_name.getText().toString().equals("")){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "이름 또는 별명을 입력해주세요.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
            return false;
        }
        if(user_password.getText().toString().equals("")){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
            return false;
        }
        if(password_check.getText().toString().equals("")){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "비밀번호를 재입력해주세요.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
            return false;
        }
        if(!user_password.getText().toString().equals(password_check.getText().toString())){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "비밀번호와 비밀번호 재입력이\n일치하지 않습니다.\n다시 확인해주세요.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
            return false;
        }
        manager.insertUserInfo(user_id.getText().toString(), user_name.getText().toString(), user_password.getText().toString()); //회원정보 입력
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
