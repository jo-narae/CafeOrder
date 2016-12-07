package com.narae.cafeorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.narae.cafeorder.R;
import com.narae.cafeorder.database.UserDBManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView user_id;
    private TextView user_password;

    UserDBManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_id = (TextView) findViewById(R.id.user_id);
        user_password = (TextView) findViewById(R.id.user_password);

        manager = new UserDBManager(this);

        userCheck(); //디바이스 내 회원 정보가 있는지 체크

        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnJoin).setOnClickListener(this);
    }

    /**
     * 디바이스 내 회원 정보가 있는지 체크
     */
    private void userCheck() {
        if(manager.selectUserInfo()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                if(validationCheck()) {
                    String url = getString(R.string.server_url) + "/users/" + user_id.getText().toString();
                    loginCheck(url);
                }
                break;
            case R.id.btnJoin:
                startActivity(new Intent(LoginActivity.this, JoinActivity.class));
                break;
        }
    }

    /**
     * 아이디 중복체크 서버통신 구현
     * @param url
     */
    private void loginCheck(String url) {
        JsonObjectRequest socRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            String id = "";
            String name = "";
            String password = "";
            @Override
            public void onResponse(JSONObject response) {
                // 아이디가 있을 경우 이 로직 실행함
                try {
                    id = response.getString("id");
                    name = response.getString("name");
                    password = response.getString("password");
                    if(!user_password.getText().toString().equals(password)) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "비밀번호를 잘못 입력하셨습니다.\n다시 입력해주세요.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 200);
                        toast.show();
                    } else {
                        //로그인 성공
                        manager.insertUserInfo(id, name, password); //회원정보 입력
                        //회원정보가 DB에 잘 들어갔는지 체크 후 화면 전환
                        userCheck();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 아이디가 없을 경우 이 로직 실행함
                Toast toast = Toast.makeText(getApplicationContext(),
                        "아이디가 존재하지 않습니다.\n회원가입 해주세요.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 200);
                toast.show();
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
        if(user_password.getText().toString().equals("")){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
            return false;
        }
        return true;
    }
}
