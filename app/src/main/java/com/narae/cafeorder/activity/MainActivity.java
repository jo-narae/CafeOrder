package com.narae.cafeorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.narae.cafeorder.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btnOrder, btnHistory, btnRecipe, btnBest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnOrder = (Button) findViewById(R.id.btnOrder);
        btnHistory = (Button) findViewById(R.id.btnHistory);
        btnRecipe = (Button) findViewById(R.id.btnRecipe);
        btnBest = (Button) findViewById(R.id.btnBest);

        btnOrder.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
        btnRecipe.setOnClickListener(this);
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
            case R.id.btnRecipe:
                startActivity(new Intent(MainActivity.this, RecipeActivity.class));
                break;
            case R.id.btnBest:
                startActivity(new Intent(MainActivity.this, BestActivity.class));
                break;
        }
    }
}
