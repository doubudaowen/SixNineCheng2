package com.mobile.liujiucheng.sixninecheng.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * 地图详情页
 * @author pc
 */
public class DetailMapActivity extends BaseActivity implements OnClickListener {

	private MapView mMapView;
	private BaiduMap baiduMap;
	private String latitude;
	private String longitude;
	private ImageView mImage;
	private static final float mZoom = 15.f;
	private static final int mSize = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_detail_page);
		initData();
		initView();
	}

	private void initData() {
		Intent intent = this.getIntent();
		latitude = intent.getStringExtra("latitude");
		longitude = intent.getStringExtra("longitude");
	}

	private void initView() {
		mImage = (ImageView) findViewById(R.id.iv_exit);
		mImage.setOnClickListener(this);
		mMapView = (MapView) findViewById(R.id.bmapView);
		baiduMap = mMapView.getMap();
		baiduMap.setTrafficEnabled(true);
		mMapView.showZoomControls(false);
		if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
			double mLatitude = Double.parseDouble(latitude);
			double mLongitude = Double.parseDouble(longitude);
			if (mLatitude > mSize && mLongitude > mSize) {
				LatLng latLng2 = new LatLng(mLatitude, mLongitude);
				MapStatus mapStatus = new MapStatus.Builder().target(latLng2)
						.zoom(mZoom).build();
				MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
						.newMapStatus(mapStatus);
				baiduMap.setMapStatus(mapStatusUpdate);
				fillMakers(latLng2);
			} else {
				MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(mZoom);
				baiduMap.setMapStatus(msu);
			}
		} else {
			MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(mZoom);
			baiduMap.setMapStatus(msu);
		}
	}
	private void fillMakers(LatLng latLng) {
		// 定义Maker坐标点
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.badidu_icon);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(latLng).icon(
				bitmap);
		// 在地图上添加Marker，并显示
		baiduMap.addOverlay(option);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.iv_exit:
			finish();
			break;
		}
	}
}
