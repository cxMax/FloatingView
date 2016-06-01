package com.cxmax.floatingview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cxmax.library.FloatingView;

public class MainActivity extends AppCompatActivity implements FloatingView.OnFloatClickListener{
    private FloatingView mFloatingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mFloatingView = (FloatingView) findViewById(R.id.float_view);
        mFloatingView.setOnFloatClickListener(this);
    }

    @Override
    public void floatClick(View view) {
        Toast.makeText(this,"hhhhhhh",Toast.LENGTH_SHORT).show();
    }
}
