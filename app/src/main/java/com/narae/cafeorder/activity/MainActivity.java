package com.narae.cafeorder.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.narae.cafeorder.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btnOrder, btnHistory, btnBest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnOrder = (Button) findViewById(R.id.btnOrder);
        btnHistory = (Button) findViewById(R.id.btnHistory);
        btnBest = (Button) findViewById(R.id.btnBest);

        btnOrder.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
        btnBest.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOrder:
                startActivity(new Intent(MainActivity.this, OrderActivity.class));
                break;
            case R.id.btnHistory:
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                break;
            case R.id.btnBest:
                startActivity(new Intent(MainActivity.this, BestActivity.class));
                break;
        }
    }

    /**
     * 뒤로가기 처리 -> 어플 종료
     * 뒤로가기 버튼이 눌렸을 경우 인트로 화면이 아닌 어플리케이션 종료를 한다.
     * 이때 어플을 완전히 끄는 것에 대한 안내를 한다.
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
        alert_confirm.setMessage("프로그램을 종료 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'YES'
                        moveTaskToBack(true);
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'No'
                        return;
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }
}
