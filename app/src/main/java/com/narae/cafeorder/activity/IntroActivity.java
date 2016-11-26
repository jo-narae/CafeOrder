package com.narae.cafeorder.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.narae.cafeorder.R;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        buttonIntro = (Button) findViewById(R.id.buttonintro);
        //startActivity(new Intent(IntroActivity.this, MainActivity.class));

        buttonIntro.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonintro:
                startActivity(new Intent(IntroActivity.this, MainActivity.class));
                break;
        }
    }
}
