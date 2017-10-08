package com.github.pathmeasuredemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.pathmeasuredemo.heart.HeartView;

public class MainActivity extends AppCompatActivity {

    HeartView mHeartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHeartView = (HeartView) findViewById(R.id.heart);
        mHeartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHeartView.addHeart();
            }
        });
    }
}
