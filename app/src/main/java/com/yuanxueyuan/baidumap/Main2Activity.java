package com.yuanxueyuan.baidumap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = null;
    private Double latitude,longtitude;//經緯度
    private TextView latitudeTv,longtitudeTv;
    private Button markertBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        latitudeTv = (TextView) findViewById(R.id.latitude);
        longtitudeTv = (TextView) findViewById(R.id.longtitude);
        markertBtn  = (Button) findViewById(R.id.markert);
        markertBtn.setOnClickListener(this);
        //定位功能
        myListener = new MyLocationListener();//初始化監聽器;
        mLocationClient = new LocationClient(getApplicationContext());//声明LocationClient类
        mLocationClient.registerLocationListener(myListener);//注册监听函数
        mLocationClient.registerNotifyLocationListener(myListener);
        setLocation();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Main2Activity.this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * 位置的回调方法
     * 苑雪元
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        //接收位置信息的回调方法
        public void onReceiveLocation(BDLocation location) {
            if (location.hasSpeed()){
                Toast.makeText(Main2Activity.this,"正在移动",Toast.LENGTH_SHORT).show();
            }
            latitude = location.getLatitude();
            longtitude = location.getLongitude();
            latitudeTv.setText(latitude+"");
            longtitudeTv.setText(longtitude+"");
        }
    }




    /**
     * 定位功能
     */
    private void setLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        //int span=1000;
        //option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        mLocationClient.requestLocation();//发送请求
    }


}
