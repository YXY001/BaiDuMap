package com.yuanxueyuan.baidumap;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;

public class MapApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
	}

}
