package com.yuanxueyuan.baidumap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ScaleGestureDetector;
import android.widget.Toast;

public class Main5Activity extends AppCompatActivity implements ScaleGestureDetector.OnScaleGestureListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        Toast.makeText(this,"111111111111",Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

    }
}
