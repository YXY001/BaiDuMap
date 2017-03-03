package com.yuanxueyuan.baidumap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

import java.util.ArrayList;

public class Main3Activity extends AppCompatActivity implements BaiduMap.OnMarkerClickListener,View.OnClickListener{
    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;
    private InfoWindow mInfoWindow;
    private SeekBar alphaSeekBar ;
    private CheckBox animationBox ;
    private Button favoriteBtn;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdA = null;
    BitmapDescriptor bdB = null;
    BitmapDescriptor bdC = null;
    BitmapDescriptor bdD = null;
    BitmapDescriptor bd = null;
    BitmapDescriptor bdGround = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //初始化界面布局
        initView();

        //初始化数据
        initValues();

        //初始化标记
        initOverlay();

        //添加监听
        addListener();

    }


    /**
    * @author  yuanxueyuan
    * @Title:  initValues
    * @Description: (初始化数据)
    * @date 2017/2/9 11:14
    */
    private void initValues(){
        bdA = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        bdB = BitmapDescriptorFactory.fromResource(R.drawable.icon_markb);
        bdC = BitmapDescriptorFactory.fromResource(R.drawable.icon_markc);
        bdD = BitmapDescriptorFactory.fromResource(R.drawable.icon_markd);
        bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        bdGround = BitmapDescriptorFactory.fromResource(R.drawable.ground_overlay);
        //设置缩放级别
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);
    }

    /**
    * @author  yuanxueyuan
    * @Title: initView
    * @Description: (初始化界面布局)
    * @date 2017/2/9 11:13
    */
    private void initView(){
        alphaSeekBar = (SeekBar) findViewById(R.id.alphaBar);
        animationBox = (CheckBox) findViewById(R.id.animation);
        favoriteBtn = (Button) findViewById(R.id.favoriteBtn);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
    }


    /**
    * @author  yuanxueyuan
    * @Title:  addListener
    * @Description: (添加监听)
    * @date 2017/2/9 11:15
    */
    private void addListener(){
        alphaSeekBar.setOnSeekBarChangeListener(new SeekBarListener());
        mBaiduMap.setOnMarkerClickListener(this);
        favoriteBtn.setOnClickListener(this);
    }


    public void initOverlay() {
        // add marker overlay
        LatLng llA = new LatLng(39.963175, 116.400244);
        LatLng llB = new LatLng(39.942821, 116.369199);
        LatLng llC = new LatLng(39.939723, 116.425541);
        LatLng llD = new LatLng(39.906965, 116.401394);

        //A
        MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA).zIndex(18).draggable(true);//draggable是否支持拖拽
        if (animationBox.isChecked()) {
            // 掉下动画
//            ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
        }
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

        //B
        MarkerOptions ooB = new MarkerOptions().position(llB).icon(bdB).zIndex(5);
        if (animationBox.isChecked()) {
            // 掉下动画
//            ooB.animateType(MarkerOptions.MarkerAnimateType.drop);
        }
        mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));

        //C
        // perspective：设置是否开启 marker 覆盖物近大远小效果，默认开启  anchor：设置 marker 覆盖物的锚点比例，默认（0.5f, 1.0f）水平居中，垂直下对齐
        //rotate：设置 marker 覆盖物旋转角度，逆时针
        MarkerOptions ooC = new MarkerOptions().position(llC).icon(bdC).perspective(false).anchor(0.5f, 0.5f).rotate(30).zIndex(7);

        if (animationBox.isChecked()) {
            // 生长动画
//            ooC.animateType(MarkerOptions.MarkerAnimateType.grow);
        }
        mMarkerC = (Marker) (mBaiduMap.addOverlay(ooC));


        //D
        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
        giflist.add(bdA);
        giflist.add(bdB);
        giflist.add(bdC);
        MarkerOptions ooD = new MarkerOptions().position(llD).icons(giflist)
                .zIndex(0).period(10);
        if (animationBox.isChecked()) {
            // 生长动画
//            ooD.animateType(MarkerOptions.MarkerAnimateType.grow);
        }
        mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));

        // add ground overlay
        LatLng southwest = new LatLng(39.92235, 116.380338);
        LatLng northeast = new LatLng(39.947246, 116.414977);
        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
                .include(southwest).build();

        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds).image(bdGround).transparency(0.8f);//transparency：设置 ground 覆盖物透明度
        mBaiduMap.addOverlay(ooGround);

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(bounds.getCenter());
        mBaiduMap.setMapStatus(u);

        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(
                        Main3Activity.this,
                        "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
                                + marker.getPosition().longitude,
                        Toast.LENGTH_LONG).show();
            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
    }

    /**
     * 清除所有Overlay
     *
     * @param view
     */
    public void clearOverlay(View view) {
        mBaiduMap.clear();
        mMarkerA = null;
        mMarkerB = null;
        mMarkerC = null;
        mMarkerD = null;
    }

    /**
     * 重新添加Overlay
     *
     * @param view
     */
    public void resetOverlay(View view) {
        clearOverlay(null);
        initOverlay();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Button button = new Button(getApplicationContext());
        button.setBackgroundResource(R.drawable.popup);
        InfoWindow.OnInfoWindowClickListener listener = null;
        if (marker == mMarkerA || marker == mMarkerD) {
            button.setText("更改位置");
            button.setBackgroundColor( 0x0000f );
            button.setWidth( 300 );

            listener = new InfoWindow.OnInfoWindowClickListener() {
                public void onInfoWindowClick() {
                    LatLng ll = marker.getPosition();
                    LatLng llNew = new LatLng(ll.latitude + 0.005,ll.longitude + 0.005);
                    marker.setPosition(llNew);
                    mBaiduMap.hideInfoWindow();
                }
            };
            LatLng ll = marker.getPosition();
            mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
            mBaiduMap.showInfoWindow(mInfoWindow);
        } else if (marker == mMarkerB) {
            button.setText("更改图标");
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    marker.setIcon(bd);
                    mBaiduMap.hideInfoWindow();
                }
            });
            LatLng ll = marker.getPosition();
            mInfoWindow = new InfoWindow(button, ll, -47);
            mBaiduMap.showInfoWindow(mInfoWindow);
        } else if (marker == mMarkerC) {
            button.setText("删除");
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    marker.remove();
                    mBaiduMap.hideInfoWindow();
                }
            });
            LatLng ll = marker.getPosition();
            mInfoWindow = new InfoWindow(button, ll, -47);
            mBaiduMap.showInfoWindow(mInfoWindow);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.favoriteBtn:
                Intent intent = new Intent(this,FavoriteDemo.class);
                startActivity(intent);
                break;
        }
    }

    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // TODO Auto-generated method stub
            float alpha = ((float) seekBar.getProgress()) / 10;
            if (mMarkerA != null) {
//                mMarkerA.setAlpha(alpha);
            }
            if (mMarkerB != null) {
//                mMarkerB.setAlpha(alpha);
            }
            if (mMarkerC != null) {
//                mMarkerC.setAlpha(alpha);
            }
            if (mMarkerD != null) {
//                mMarkerD.setAlpha(alpha);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }

    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();
        super.onDestroy();
        // 回收 bitmap 资源
        bdA.recycle();
        bdB.recycle();
        bdC.recycle();
        bdD.recycle();
        bd.recycle();
        bdGround.recycle();
    }

}
