package com.yuanxueyuan.baidumap;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Main4Activity extends AppCompatActivity implements BaiduMap.OnMapClickListener,View.OnClickListener{

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    List<LatLng> points = new ArrayList<LatLng>();
    private Button trajectoryBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        //初始化布局
        initView();

        //添加监听器
        addListener();

        //设置轨迹
//        setTrajectory();
    }

    /**
    * @author  yuanxueyuan
    * @Title:  initView
    * @Description: (初始化布局)
    * @date 2017/2/9 14:59
    */
    private void initView(){
        mMapView = (MapView) findViewById(R.id.bmapView);
        trajectoryBtn = (Button) findViewById(R.id.trajectoryBtn);
        mBaiduMap = mMapView.getMap();
    }

    /**
    * @author  yuanxueyuan
    * @Title:  addListener
    * @Description: (添加监听器)
    * @date 2017/2/9 15:06
    */
    private void addListener(){
        mBaiduMap.setOnMapClickListener(this);
        trajectoryBtn.setOnClickListener(this);
    }

    /**
    * @author  yuanxueyuan
    * @Title:  setTrajectory
    * @Description: (设置轨迹)
    * @date 2017/2/9 14:59
    */
    private void setTrajectory(){
        // 构造折线点坐标
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(new LatLng(39.965,116.404));
        points.add(new LatLng(39.925,116.454));
        points.add(new LatLng(39.955,116.494));
        points.add(new LatLng(39.905,116.554));
        points.add(new LatLng(39.965,116.604));

        //构建分段颜色索引数组
        List<Integer> colors = new ArrayList<>();
        colors.add(Integer.valueOf(Color.BLUE));
        colors.add(Integer.valueOf(Color.RED));
        colors.add(Integer.valueOf(Color.YELLOW));
        colors.add(Integer.valueOf(Color.GREEN));

//        OverlayOptions ooPolyline = new PolylineOptions().width(10)
//                .colorsValues(colors).points(points);
        //添加在地图中
//        Polyline mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        points.add(latLng);
        if (points.size() <= 1){
            return;
        }
        OverlayOptions ooPolyline = new PolylineOptions().width(10).color(R.color.colorAccent).points(points);
        //添加在地图中
        mBaiduMap.addOverlay(ooPolyline);
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.trajectoryBtn:
                Intent intent = new Intent(this,Main3Activity.class);
                startActivity(intent);
                break;
        }
    }
}
