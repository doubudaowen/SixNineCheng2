package com.mobile.liujiucheng.main.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
/**
 * @author Mr_Gang
 */
public class CityPosition {
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private boolean isFirstIn = true;
	private Context context;
	private BaiduMap baiduMap;
	private String mCity;
	private MyAsyncTask mAsync;
	private BDLocation mLocation;
	private TextView mTextCity;
	//private PopupWindow popupWindow;
	private LinearLayout mLinerLayout;
	public CityPosition(BaiduMap baiduMap, Context context, TextView mTextCity,LinearLayout mLinerLayout) {
		this.context = context;
		this.mTextCity = mTextCity;
		this.baiduMap = baiduMap;
		this.mLinerLayout = mLinerLayout;
		initLocation();
	}
	private void initLocation() {
		mAsync = new MyAsyncTask();
		mLocationClient = new LocationClient(context);
		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(2000);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		//setPopWindow();
	}
	public String getCity() {
		return mCity;
	}
	private class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			mLocation = location;
			if (context != null && !NetworkUtils.isNetworkConnected(context)) {
				ToastUtils.show(context, "");
				return;
			}
			if (isFirstIn) {
				mAsync.execute();
			}
		}
	}
	private void location(BDLocation location) {
		if (isFirstIn) {
			LatLng latLng = new LatLng(location.getLatitude(),
					location.getLongitude());
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
			baiduMap.animateMapStatus(msu);
			isFirstIn = false;
			//Toast.makeText(context, location.getAddrStr(), Toast.LENGTH_LONG).show();
			mCity = location.getCity();
			mLinerLayout.setVisibility(View.GONE);
			mTextCity.setVisibility(View.VISIBLE);
			if (!TextUtils.isEmpty(mCity)) {
				mTextCity.setText(mCity);
			} else {
				Toast.makeText(context, "", Toast.LENGTH_LONG).show();
			}
		} else {
			baiduMap.setMyLocationEnabled(false);
			if (mLocationClient != null) {
				mLocationClient.stop();
			}
		}
	}
	private class MyAsyncTask extends AsyncTask<Void, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Void... params) {
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			location(mLocation);
		}
	}
}
