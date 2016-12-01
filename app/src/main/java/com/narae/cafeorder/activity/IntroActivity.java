package com.narae.cafeorder.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.narae.cafeorder.R;
import com.narae.cafeorder.database.UserDBManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonIntro;
    private Button buttonidcheck;
    private TextView user_id;
    private TextView user_name;
    private TextView user_password;
    private TextView password_check;

    UserDBManager manager;

    private String input_user_id;
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

        manager = new UserDBManager(this);
        userCheck(); //디바이스 내 회원 정보가 있는지 체크

        buttonIntro.setOnClickListener(this);
        buttonidcheck.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonintro:
                if(validationCheck()) {
                    userJoinRequest();
                    break;
                }
                break;
            case R.id.buttonidcheck:
                idOverlapCheck();
                break;
        }
    }

    /**
     * 디바이스 내 회원 정보가 있는지 체크
     */
    private void userCheck() {
        if(manager.selectUserInfo()) {
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
        }
    }

    /**
     * 아이디 값 변화 체크
     */
    private boolean idValueChangeCheck() {
        if(!input_user_id.equals(user_id.getText().toString())) {
            idValidationFlag = false;
            Toast toast = Toast.makeText(getApplicationContext(),
                    "아이디 중복체크를 다시 해주세요.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
            return false;
        }
        return true;
    }

    /**
     * 아이디 중복체크
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
        idOverlapCheckRequest(url);
    }

    /**
     * 아이디 중복체크 서버통신 구현
     * @param url
     */
    private void idOverlapCheckRequest(String url) {
        JsonObjectRequest socRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // 아이디가 있을 경우 이 로직 실행함
                Toast toast = Toast.makeText(getApplicationContext(),
                        "아이디가 존재합니다.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 200);
                toast.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 아이디가 없을 경우 이 로직 실행함
                input_user_id = user_id.getText().toString();
                idValidationFlag = true;
                Toast toast = Toast.makeText(getApplicationContext(),
                        "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 200);
                toast.show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        requestQueue.add(socRequest);
    }

    private void userJoinRequest() {
        Map<String, String> jsonParam = new HashMap<String, String>();
        jsonParam.put("id", user_id.getText().toString());
        jsonParam.put("name", user_name.getText().toString());
        jsonParam.put("password", user_password.getText().toString());

        String url = getString(R.string.server_url) + "/users";

        JsonObjectRequest socRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                userCheck(); //디바이스 내 회원 정보가 있는지 체크
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error!!", error);
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
        if(!idValueChangeCheck()){
            return false;
        }
        manager.insertUserInfo(user_id.getText().toString(), user_name.getText().toString(), user_password.getText().toString()); //회원정보 입력
        return true;
    }

}
