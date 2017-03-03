package com.yuanxueyuan.baidumap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class demo extends AppCompatActivity implements BaiduMap.OnMarkerClickListener,
        View.OnClickListener,BaiduMap.OnMapLoadedCallback,
        /*BaiduMap.OnMapRenderCallback,*/BaiduMap.OnMapClickListener,
        BaiduMap.OnMapStatusChangeListener{
    /**
     * MapView 是地图主控件
     */

    // 定位相关
    LocationClient mLocClient;
    boolean isFirstLoc = true; // 是否首次定位
    private double latitude, longitude;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Button nextBtn,trajectoryBtn,locationBtn;
    private ImageView resetImg;
    //UiSettings:百度地图 UI 控制器
    private UiSettings mUiSettings;
    private View mPop;
    private ImageView liveviewImg,detailsImg;
    private TextView txt;
    private Marker mMarkerA,mMarkerB,mMarkerC,mMarkerD,mMarkerE,mMarkerF,mMarkerG,mMarkerH,mMarketLocation,mMoveMarker = null;
    private BitmapDescriptor bitmapA,bitmapB,bitmapC,bitmapD,bitmapE,bitmapF,bitmapG,bitmapH,bitmapLocation= null;
    private List<LatLng> points;
    private Polyline mPolyline;
    private Handler mHandler;
    // 通过设置间隔时间和距离可以控制速度和图标移动的距离
    private static final int TIME_INTERVAL = 80;
    private static final double DISTANCE = 0.00002;
    private float zoomStart;
    private float zoomFinish = 0f;

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
        resetImg.setOnTouchListener();
        trajectoryBtn = (Button) findViewById(R.id.trajectoryBtn);
        locationBtn = (Button) findViewById(R.id.locationBtn);
        mBaiduMap = mMapView.getMap();
        LayoutInflater mInflater = getLayoutInflater();
        mPop = (View) mInflater.inflate(R.layout.alert_device_info, null, false);
        liveviewImg = (ImageView) mPop.findViewById(R.id.liveviewImg);
        detailsImg = (ImageView) mPop.findViewById(R.id.detailsImg);
        txt = (TextView) mPop.findViewById(R.id.txt);

//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); // 设置为一般地图

        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE); //设置为卫星地图
//        mBaiduMap.setTrafficEnabled(true); //开启交通图
        mBaiduMap.setMaxAndMinZoomLevel(0.1f,0.1f);
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
        mHandler = new Handler(Looper.getMainLooper());

    }

    /**
     * 定位到當前坐標
     */
    private void initLocation(){
        //设置中心点坐标
        /*LatLng cenpt = new LatLng(39.103386,117.09226);
        MapStatus mapStatus = new MapStatus.Builder().target(cenpt).zoom(17).build();
        bitmapLocation = BitmapDescriptorFactory.fromResource(R.drawable.location_current);
        MarkerOptions option = new MarkerOptions().position(cenpt).icon(bitmapLocation);
//        option.animateType(MarkerOptions.MarkerAnimateType.grow);
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus((mapStatus));
//        mBaiduMap.setMapStatus(mapStatusUpdate);
        mBaiduMap.animateMapStatus(mapStatusUpdate);
        mMarketLocation = (Marker) mBaiduMap.addOverlay(option);*/

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(locListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll");// 设置坐标类型
        option.setAddrType("all");
        option.setScanSpan(1000);//
        mLocClient.setLocOption(option);
        mLocClient.start();

        MapStatus mapStatus = new MapStatus.Builder().zoom(17).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus((mapStatus));
        mBaiduMap.animateMapStatus(mapStatusUpdate);
    }
    /**
     * 定位监听器
     */
    BDLocationListener locListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mBaiduMap == null) {
                return;
            }
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())//
                    .direction(100)// 方向
                    .latitude(location.getLatitude())//
                    .longitude(location.getLongitude())//
                    .build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            // 第一次定位的时候，那地图中心店显示为定位到的位置
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                // MapStatusUpdate描述地图将要发生的变化
                // MapStatusUpdateFactory生成地图将要反生的变化
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(msu);
//				 bdMap.setMyLocationEnabled(false);
                Toast.makeText(getApplicationContext(), location.getAddrStr(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
        zoomStart = mapStatus.zoom;
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
        if (zoomStart != mapStatus.zoom) {
            mUiSettings.setScrollGesturesEnabled(false);
        }
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        mUiSettings.setScrollGesturesEnabled(true);
        zoomFinish = mapStatus.zoom;
        if (mapStatus.zoom == 21.0f){
            Toast.makeText(this,"已放大至最高级别",Toast.LENGTH_SHORT).show();
        } else if (mapStatus.zoom == 3.0f){
            Toast.makeText(this,"已缩小至最低级别",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
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

        Context context;
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
        // 构造折线点坐标
        points = new ArrayList<LatLng>();
        for (int index = 0; index < latlngs.length; index++) {
            points.add(latlngs[index]);
        }
        //构建分段颜色索引数组,下方用colorsValues
//        List<Integer> colors = new ArrayList<>();
//        colors.add(Integer.valueOf(Color.GREEN));
//        colors.add(Integer.valueOf(Color.BLACK));
//        colors.add(Integer.valueOf(Color.WHITE));
//        colors.add(Integer.valueOf(Color.YELLOW));

        OverlayOptions ooPolyline = new PolylineOptions().width(10).color(Color.GREEN).points(points);
        //添加在地图中
        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
    }


    //设置移动的marker
    private void setMove(){
        OverlayOptions markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)).position(points.get(0))
                .rotate((float) getAngle(0));
        mMoveMarker = (Marker) mBaiduMap.addOverlay(markerOptions);
    }

    /**
     * 根据点获取图标转的角度
     */
    private double getAngle(int startIndex) {
        if ((startIndex + 1) >= mPolyline.getPoints().size()) {
            throw new RuntimeException("index out of bonds");
        }
        LatLng startPoint = mPolyline.getPoints().get(startIndex);
        LatLng endPoint = mPolyline.getPoints().get(startIndex + 1);
        return getAngle(startPoint, endPoint);
    }

    /**
     * 根据两点算取图标转的角度
     */
    private double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {//正南正北走向
            if (toPoint.latitude > fromPoint.latitude) {//向北
                return 0;
            } else {//向南
                return 180;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        return angle;
    }


    /**
     * 算斜率
     */
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        //如果经度相同，取最大值
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        //纬度差/经度差
        return ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
    }
    /**
     * 根据点和斜率算取截距
     */
    private double getInterception(double slope, LatLng point) {
        //y = kx + b    b = y-kx
        double interception = point.latitude - slope * point.longitude;
        return interception;
    }

    /**
     * 计算x方向每次移动的距离
     */
    private double getXMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
    }

    /**
     * 循环进行移动逻辑
     */

    public void moveLooper() {
        new Thread() {
            public void run() {
                while (true) {
                    for (int i = 0; i < latlngs.length - 1; i++) {
                        final LatLng startPoint = latlngs[i];
                        final LatLng endPoint = latlngs[i + 1];
                        mMoveMarker.setPosition(startPoint);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // refresh marker's rotate
                                if (mMapView == null) {
                                    return;
                                 }
                                mMoveMarker.setRotate((float) getAngle(startPoint,endPoint));
                            }
                        });
                        double slope = getSlope(startPoint, endPoint);
                        // 是不是正向的标示
                        boolean isReverse = (startPoint.latitude > endPoint.latitude);

                        double intercept = getInterception(slope, startPoint);

                        double xMoveDistance = isReverse ? getXMoveDistance(slope) :-1 * getXMoveDistance(slope);

                        for (double j = startPoint.latitude; !((j > endPoint.latitude) ^ isReverse); j = j - xMoveDistance) {
                            LatLng latLng = null;
                            if (slope == Double.MAX_VALUE) {
                                latLng = new LatLng(j, startPoint.longitude);
                            } else {
                                latLng = new LatLng(j, (j - intercept) / slope);
                            }

                            final LatLng finalLatLng = latLng;
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mMapView == null) {
                                        return;
                                    }
                                    mMoveMarker.setPosition(finalLatLng);
                                }
                            });
                            try {
                                Thread.sleep(TIME_INTERVAL);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }

        }.start();
    }





    private static final LatLng[] latlngs = new LatLng[] {
            new LatLng(39.106749,117.083344),
            new LatLng(39.106665,117.08338),
            new LatLng(39.106574,117.083407),
            new LatLng(39.106500,117.08341),
            new LatLng(39.106474,117.08342),
            new LatLng(39.106374,117.08343),
            new LatLng(39.106274,117.08345),
            new LatLng(39.105127,117.08347),
            new LatLng(39.10481,117.084152),
            new LatLng(39.104509,117.085019),
            new LatLng(39.104226,117.085558),
            new LatLng(39.103512,117.086762),
            new LatLng(39.10312,117.087436),
            new LatLng(39.102602,117.088343),
            new LatLng(39.102385,117.088711),
            new LatLng(39.10116,117.091029),
            new LatLng(39.100747,117.094101),
            new LatLng(39.101468,117.094074),
            new LatLng(39.102084,117.094074),
            new LatLng(39.102532,117.094101),
            new LatLng(39.102756,117.094038),
            new LatLng(39.102777,117.093625),
            new LatLng(39.102882,117.092745),
            new LatLng(39.103183,117.091694),
            new LatLng(39.103309,117.09155),
            new LatLng(39.103505,117.091137),
            new LatLng(39.103645,117.090948),
            new LatLng(39.103857,117.091116),
    };


    @Override
    public boolean onMarkerClick(Marker marker) {
        mBaiduMap.hideInfoWindow();
        if (marker == null || marker == mMarketLocation) {
            return false;
        }
        txt.setText(marker.getTitle());
        InfoWindow mInfoWindow = new InfoWindow(mPop, marker.getPosition(), -100);
        mBaiduMap.showInfoWindow(mInfoWindow);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(marker.getPosition());
        mBaiduMap.animateMapStatus(update);
        return false;
    }

    /**
     * 添加點擊事件
     */
    private void addClick(){
        mBaiduMap.setOnMarkerClickListener(this);
        mBaiduMap.setOnMapStatusChangeListener(this);
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
                mBaiduMap.clear();
                setImg();
                setMove();
                moveLooper();
                break;
            case R.id.locationBtn:
                mBaiduMap.clear();
//                mMarketLocation.remove();
                //设置标志
                setMarker();
                break;
        }
    }


    @Override
    public void onMapLoaded() {
        Toast.makeText(demo.this,"地图加载完成了",Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onMapRenderFinished() {
//        Toast.makeText(demo.this,"地图渲染完成了",Toast.LENGTH_SHORT).show();
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
