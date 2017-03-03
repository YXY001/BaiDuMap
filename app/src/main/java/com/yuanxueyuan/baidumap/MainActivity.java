package com.yuanxueyuan.baidumap;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements BaiduMap.OnMarkerClickListener,View.OnClickListener,BaiduMap.OnMapLoadedCallback,/*BaiduMap.OnMapRenderCallback,*/BaiduMap.OnMapClickListener {
    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Button nextBtn,trajectoryBtn,locationBtn;
    private ImageView resetImg;
    //UiSettings:百度地图 UI 控制器
    private UiSettings mUiSettings;
    private View mPop;
    private ImageView liveviewImg,detailsImg;
    private TextView txt;
    private Marker mMarkerA,mMarkerB,mMarkerC,mMarkerD,mMarkerE,mMarkerF,mMarkerG,mMarkerH,mMarketLocation= null;
    private BitmapDescriptor bitmapA,bitmapB,bitmapC,bitmapD,bitmapE,bitmapF,bitmapG,bitmapH,bitmapLocation= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化界面
        initView();

        //定位到当前坐标
        initLocation();

        //设置标点的点击事件
        addClick();
    }

    /**
     * 初始化界面
     */
    private void initView(){
        mMapView = (MapView) findViewById(R.id.bmapView);
        resetImg  = (ImageView) findViewById(R.id.reset);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        trajectoryBtn = (Button) findViewById(R.id.trajectoryBtn);
        locationBtn = (Button) findViewById(R.id.locationBtn);
        mBaiduMap = mMapView.getMap();
        LayoutInflater mInflater = getLayoutInflater();
        mPop = (View) mInflater.inflate(R.layout.alert_device_info, null, false);
        liveviewImg = (ImageView) mPop.findViewById(R.id.liveviewImg);
        detailsImg = (ImageView) mPop.findViewById(R.id.detailsImg);
        txt = (TextView) mPop.findViewById(R.id.txt);
        //开启交通图
//        mBaiduMap.setTrafficEnabled(true);
        //设置是否显示比例尺控件
        mMapView.showScaleControl(false);
        //设置是否显示缩放控件
        mMapView.showZoomControls(true);
        // 删除百度地图LoGo
        mMapView.removeViewAt(1);

        mUiSettings = mBaiduMap.getUiSettings();
        //设置指南针的显隐
        mUiSettings.setCompassEnabled(false);
        //设置俯视
        mUiSettings.setOverlookingGesturesEnabled(false);
        //设置是否旋转
        mUiSettings.setRotateGesturesEnabled(false);

    }
    /**
     * 定位到當前坐標
     */
    private void initLocation(){
        //设置中心点坐标
        LatLng cenpt = new LatLng(39.103386,117.09226);
        MapStatus mapStatus = new MapStatus.Builder().target(cenpt).zoom(17).build();
        bitmapLocation = BitmapDescriptorFactory.fromResource(R.drawable.location_current);
        MarkerOptions option = new MarkerOptions().position(cenpt).icon(bitmapLocation);
//        option.animateType(MarkerOptions.MarkerAnimateType.grow);
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus((mapStatus));
        mBaiduMap.setMapStatus(mapStatusUpdate);
        mMarketLocation = (Marker) mBaiduMap.addOverlay(option);
    }

    /**
    * @author  yuanxueyuan
    * @Title:  initImg
    * @Description: (初始化图片)
    * @date 2017/2/9 19:23
    */
    private void initMarkerImg(){
        bitmapA = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        bitmapB = BitmapDescriptorFactory.fromResource(R.drawable.icon_markb);
        bitmapC = BitmapDescriptorFactory.fromResource(R.drawable.icon_markc);
        bitmapD = BitmapDescriptorFactory.fromResource(R.drawable.icon_markd);
        bitmapE = BitmapDescriptorFactory.fromResource(R.drawable.icon_marke);
        bitmapF = BitmapDescriptorFactory.fromResource(R.drawable.icon_markf);
        bitmapG = BitmapDescriptorFactory.fromResource(R.drawable.icon_markg);
        bitmapH = BitmapDescriptorFactory.fromResource(R.drawable.icon_markh);
    }

    /**
     * 模拟标记点
     */
    private void setMarker(){

        initMarkerImg();

        //定义Maker坐标点
        LatLng pointA = new LatLng(39.106885, 117.094029);
        LatLng pointB = new LatLng(39.104603, 117.089241);
        LatLng pointC = new LatLng(39.10144, 117.090247);
        LatLng pointD = new LatLng(39.100754, 117.094074);
        LatLng pointE = new LatLng(39.10074, 117.097685);
        LatLng pointF = new LatLng(39.102742, 117.094074);
        LatLng pointG = new LatLng(39.103568, 117.091011);
        LatLng pointH = new LatLng(39.104267, 117.094056);
        //构建MarkerOption，用于在地图上添加Marker
        MarkerOptions optionA = new MarkerOptions().position(pointA).icon(bitmapA);
        MarkerOptions optionB = new MarkerOptions().position(pointB).icon(bitmapB);
        MarkerOptions optionC = new MarkerOptions().position(pointC).icon(bitmapC);
        MarkerOptions optionD = new MarkerOptions().position(pointD).icon(bitmapD);
        MarkerOptions optionE = new MarkerOptions().position(pointE).icon(bitmapE);
        MarkerOptions optionF = new MarkerOptions().position(pointF).icon(bitmapF);
        MarkerOptions optionG = new MarkerOptions().position(pointG).icon(bitmapG);
        MarkerOptions optionH = new MarkerOptions().position(pointH).icon(bitmapH);

//        optionA.animateType(MarkerOptions.MarkerAnimateType.grow);
//        optionB.animateType(MarkerOptions.MarkerAnimateType.grow);
//        optionC.animateType(MarkerOptions.MarkerAnimateType.grow);
//        optionD.animateType(MarkerOptions.MarkerAnimateType.grow);
//        optionE.animateType(MarkerOptions.MarkerAnimateType.grow);
//        optionF.animateType(MarkerOptions.MarkerAnimateType.grow);
//        optionG.animateType(MarkerOptions.MarkerAnimateType.grow);
//        optionH.animateType(MarkerOptions.MarkerAnimateType.grow);
        //在地图上添加Marker，并显示
        mMarkerA = (Marker) mBaiduMap.addOverlay(optionA);
        mMarkerA.setTitle("通道1");
        mMarkerB = (Marker) mBaiduMap.addOverlay(optionB);
        mMarkerB.setTitle("通道2");
        mMarkerC = (Marker) mBaiduMap.addOverlay(optionC);
        mMarkerC.setTitle("通道3");
        mMarkerD = (Marker) mBaiduMap.addOverlay(optionD);
        mMarkerD.setTitle("通道4");
        mMarkerE = (Marker) mBaiduMap.addOverlay(optionE);
        mMarkerE.setTitle("通道5");
        mMarkerF = (Marker) mBaiduMap.addOverlay(optionF);
        mMarkerF.setTitle("通道6");
        mMarkerG = (Marker) mBaiduMap.addOverlay(optionG);
        mMarkerG.setTitle("通道7");
        mMarkerH = (Marker) mBaiduMap.addOverlay(optionH);
        mMarkerH.setTitle("通道8");
    }

    /**
    * @author  yuanxueyuan
    * @Title: setImg
    * @Description: (根据标点进行设置图片)
    * @date 2017/2/9 10:28
    */
    private void setImg(){
        //自定义颜色
        // 构造折线点坐标
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(new LatLng(39.106749,117.083344));
        points.add(new LatLng(39.106665,117.08338));
        points.add(new LatLng(39.106574,117.083407));
        points.add(new LatLng(39.106500,117.08341));
        points.add(new LatLng(39.106474,117.08342));
        points.add(new LatLng(39.106374,117.08343));
        points.add(new LatLng(39.106274,117.08345));
        points.add(new LatLng(39.105127,117.08347));
        points.add(new LatLng(39.10481,117.084152));
        points.add(new LatLng(39.104509,117.085019));
        points.add(new LatLng(39.104226,117.085558));
        points.add(new LatLng(39.103512,117.086762));
        points.add(new LatLng(39.10312,117.087436));
        points.add(new LatLng(39.102602,117.088343));
        points.add(new LatLng(39.102385,117.088711));
        points.add(new LatLng(39.10116,117.091029));
        points.add(new LatLng(39.100747,117.094101));
        points.add(new LatLng(39.101468,117.094074));
        points.add(new LatLng(39.102084,117.094074));
        points.add(new LatLng(39.102532,117.094101));
        points.add(new LatLng(39.102756,117.094038));
        points.add(new LatLng(39.102777,117.093625));
        points.add(new LatLng(39.102882,117.092745));
        points.add(new LatLng(39.103183,117.091694));
        points.add(new LatLng(39.103309,117.09155));
        points.add(new LatLng(39.103505,117.091137));
        points.add(new LatLng(39.103645,117.090948));
        points.add(new LatLng(39.103857,117.091116));

        //构建分段颜色索引数组
        List<Integer> colors = new ArrayList<>();/*
        colors.add(Integer.valueOf(Color.BLUE));
        colors.add(Integer.valueOf(Color.RED));
        colors.add(Integer.valueOf(Color.YELLOW));*/
        colors.add(Integer.valueOf(Color.GREEN));

//        OverlayOptions ooPolyline = new PolylineOptions().width(10).colorsValues(colors).points(points);
        //添加在地图中
//        mBaiduMap.addOverlay(ooPolyline);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mBaiduMap.hideInfoWindow();
        if (marker == null || marker == mMarketLocation) {
            return false;
        }
        txt.setText(marker.getTitle());
        InfoWindow mInfoWindow = new InfoWindow(mPop, marker.getPosition(), -130);
        mBaiduMap.showInfoWindow(mInfoWindow);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(marker.getPosition());
        mBaiduMap.setMapStatus(update);
        return true;
    }

    /**
     * 添加點擊事件
     */
    private void addClick(){
        mBaiduMap.setOnMarkerClickListener(this);
//        mBaiduMap.setOnMapRenderCallbadk(this);
//        mBaiduMap.setOnMapLoadedCallback(this);
        resetImg.setOnClickListener(this);
        mBaiduMap.setOnMapClickListener(this);
        nextBtn.setOnClickListener(this);
        trajectoryBtn.setOnClickListener(this);
        locationBtn.setOnClickListener(this);
        liveviewImg.setOnClickListener(this);
        detailsImg.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reset:
                initLocation();
                break;
            case R.id.nextBtn:
                Intent intent = new Intent(this,Main4Activity.class);
                startActivity(intent);
                break;
            case R.id.liveviewImg:
                Toast.makeText(this,"查看详情",Toast.LENGTH_SHORT).show();
                break;
            case R.id.detailsImg:
                Toast.makeText(this,"实时预览",Toast.LENGTH_SHORT).show();
                break;
            case R.id.trajectoryBtn:
                setImg();
                break;
            case R.id.locationBtn:
                //设置标志
                setMarker();
                break;
        }
    }


    @Override
    public void onMapLoaded() {
        Toast.makeText(MainActivity.this,"地图加载完成了",Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onMapRenderFinished() {
//        Toast.makeText(MainActivity.this,"地图渲染完成了",Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onMapClick(LatLng latLng) {
        mBaiduMap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }
}
