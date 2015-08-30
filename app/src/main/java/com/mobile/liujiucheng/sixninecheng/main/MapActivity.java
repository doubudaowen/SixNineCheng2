package com.mobile.liujiucheng.sixninecheng.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mobile.liujiucheng.main.bean.SecondhouseBean;
import com.mobile.liujiucheng.main.bean.SecondhouseBean.MLLData;
import com.mobile.liujiucheng.main.utils.GsonUtil;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.StreamTools;
import com.mobile.liujiucheng.main.utils.UrlsUtils;

/**
 * 地图找房
 */
public class MapActivity extends Activity implements OnClickListener {

	private MapView mMapView;

	private View mView;
	private Context context;
	private BaiduMap baiduMap;
	private Bitmap map;
	private String mResult;

	private SecondhouseBean bean;

	private List<MLLData> mData;
	private List<LatLng> mLata;

	private TextView mTvnumber;
	private TextView mTvPosition;
	private SharedPreferences mSP;
	private MapStatus mapStatus;
	
	private TextView tv_second_hand;//二手房
	private TextView tv_rent;//出租房
	
	private Map<String ,String> mId;//默认是首先访问二手房
	
	private ImageView mImageView;
	
	private EditText et_search;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_map_housing);
		initView();
		network(mId.get("id"));
	}

	private void network(String tag) {
		
		NetHead head = new NetHead(context);
		RequestParams params = head.setHeader();
		HttpUtils http = new HttpUtils();
		params.addBodyParameter("type", "mapQueryDistrict");
		params.addBodyParameter("houseT", tag);
		params.addBodyParameter("city", "北京");

		http.send(HttpMethod.POST, UrlsUtils.urls, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(context, StreamTools.getString(), Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						mResult = arg0.result;
						cacheData(1);
						parseJson();
						setMaker(1);
					}
				});
	}
	protected void setMaker(int tag) {
		//baiduMap.clear();
		// 定义Maker坐标点
		if (mData == null) {
			return;
		}
		for (int x = 0; x < mData.size(); x++) {
			int y = x;
			float mX = Float.parseFloat(mData.get(x).zuobiaoX);
			float mY = Float.parseFloat(mData.get(x).zuobiaoY);
			if (tag == 1) {
				mView = View.inflate(context, R.layout.activity_main_image,
						null);
				mTvnumber = (TextView) mView.findViewById(R.id.tv_mumber);
				mTvPosition = (TextView) mView.findViewById(R.id.tv_position);
				mTvPosition.setText(mData.get(y).district);
			} else {
				mView = View
						.inflate(context, R.layout.activity_main_text, null);
				mTvnumber = (TextView) mView.findViewById(R.id.tv_mumber);
				mTvPosition = (TextView) mView.findViewById(R.id.tv_position);
				mTvPosition.setText(mData.get(y).bussinessarea);
			}
			mTvnumber.setText(mData.get(y).count);
			map = getViewBitmap(mView);
			LatLng point = new LatLng(mY, mX);
			mLata.add(point);
			BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(map);
			// 构建MarkerOption，用于在地图上添加Marker
			OverlayOptions option;
			if(tag == 2){
				 Editor edit = mSP.edit();
				 edit.putString("parentId", mData.get(y).parentId);
				 edit.commit();
			}
			option = new MarkerOptions().position(point)
					.icon(bitmap).title(mData.get(y).listName);
			// 在地图上添加Marker，并显示
			baiduMap.addOverlay(option);
		}
	}
	//private Map<String, String> mMap;
	//private Set<String> data;
	private boolean b = true;
	protected void parseJson() {
		bean = GsonUtil.json2Bean(mResult, SecondhouseBean.class);
		mData = bean.data;
		Editor edit = mSP.edit();
		if(b){
			edit.putString("sid", "1");
			b = false;
		}else{
			edit.putString("sid", "2");
		}
		edit.commit();
	}

	protected void cacheData(int tag) {
		Editor edit = mSP.edit();
		if (tag == 1) {
			edit.putString("LevelOne", mResult);
		} else {
			//edit.putString("LevelTwo", mResult);
		}
		edit.commit();
	}

	private void initView() {
		mImageView = (ImageView) findViewById(R.id.iv_exit);
		mImageView.setOnClickListener(this);
		mId = new HashMap<String, String>();
		mId.put("id", "2");
		context = this;
		if (mSP == null) {
			mSP = this.getSharedPreferences("cache", MODE_PRIVATE);
		}
		et_search = (EditText) findViewById(R.id.et_search);
		mLata = new ArrayList<LatLng>();
		mMapView = (MapView) findViewById(R.id.bmapView);
		tv_second_hand = (TextView) findViewById(R.id.tv_second_hand);
		tv_rent = (TextView) findViewById(R.id.tv_rent);
		tv_rent.setOnClickListener(this);
		tv_second_hand.setOnClickListener(this);
		et_search.setOnClickListener(this);
		
		baiduMap = mMapView.getMap();
		// 设置覆盖物的监听事件
		baiduMap.setOnMarkerClickListener(listener);
		// 监听地图的变化情况
		baiduMap.setOnMapStatusChangeListener(listenerMapStatu);
		baiduMap.setTrafficEnabled(true);
		// 地图的状态
		mapStatus = baiduMap.getMapStatus();
		Log.e("TAG", "mapStatus" + mapStatus.zoom);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}
	/**
	 * 将布局装换成图片
	 * @param addViewContent
	 * @return
	 */
	private Bitmap getViewBitmap(View addViewContent) {
		addViewContent.setDrawingCacheEnabled(true);
		addViewContent.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addViewContent.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		addViewContent.layout(0, 0, addViewContent.getMeasuredWidth(),
				addViewContent.getMeasuredHeight());
		addViewContent.buildDrawingCache();
		Bitmap cacheBitmap = addViewContent.getDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
		return bitmap;
	}
	
	private int isFirst = 1;
	
	OnMarkerClickListener listener = new OnMarkerClickListener() {
		/**
		 * 地图 Marker 覆盖物点击事件监听函数
		 * @param marker
		 *   被点击的 marker
		 */
		public boolean onMarkerClick(Marker marker) {
			Editor edit = mSP.edit();
			//记录显示数据
			if(isFirst == 1){
				edit.putString("listName", marker.getTitle());
				isFirst++;
			}else{
				edit.putString("listName", marker.getTitle());
			}
			edit.commit();
			if(mSP.getString("sid", "").equals("1")){
				baiduMap.clear();
			}
			updateMap(marker,14);
			networkClass(marker,mId.get("id"));
			return true;
		}
	};
	
	/**
	 * 地图的状态变化情况
	 */
	OnMapStatusChangeListener listenerMapStatu = new OnMapStatusChangeListener() {
		@Override
		public void onMapStatusChange(MapStatus arg0) {
				if(arg0.zoom <= 11.0f && arg0.zoom >= 8.0f ){
					baiduMap.clear();
					String mOne = mSP.getString("LevelOne", null);
					if(!TextUtils.isEmpty(mOne)){
						mResult = mOne;
						b = true;
						isFirst = 1;
						resetData();
						parseJson();
						setMaker(1);
					}else{
						Log.e("TAG","数据为空");
					}
				}
		}
		@Override
		public void onMapStatusChangeFinish(MapStatus arg0) {

		}

		@Override
		public void onMapStatusChangeStart(MapStatus arg0) {

		}
	};
	/**
	 * 重置数据
	 */
	private void resetData() {
		Editor edit = mSP.edit();
		edit.putString("parentId", null);
		edit.commit();
	}
	protected void updateMap(Marker marker, int id) {
		MapStatus mapStatus = new MapStatus.Builder()
				.target(marker.getPosition()).zoom(id).build();
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mapStatus);
		baiduMap.setMapStatus(mapStatusUpdate);
	}

	protected void networkClass(Marker marker,final String tag) {
		
		NetHead head = new NetHead(context);
		RequestParams params = head.setHeader();
		HttpUtils http = new HttpUtils();
		
		params.addBodyParameter("houseT", tag);
		params.addBodyParameter("city", "北京");
		
		String listName = mSP.getString("listName", null);
		final String parentId = mSP.getString("parentId", null);
		Log.e("TAG", "==1====="+listName);
		Log.e("TAG", "==2====="+parentId);
		if(!TextUtils.isEmpty(listName)){
			params.addBodyParameter("listName",listName);
		}
		if(!TextUtils.isEmpty(parentId)){
			params.addBodyParameter("parentId",parentId);
			params.addBodyParameter("type", "mapQueryHouse");
		}else{
			params.addBodyParameter("type", "mapQueryBuss");
		}
		http.send(HttpMethod.POST, UrlsUtils.urls, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(context, StreamTools.getString(), Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						mResult = arg0.result;
						parseJson();
						if(!TextUtils.isEmpty(parentId)){
							//展示数据
							if(tag.equals("2")){
								Intent intent = new Intent(context, MapSecondActivity.class);
								intent.putExtra("data", mResult);
								startActivity(intent);
							}else{
								Intent intent = new Intent(context, MapRentActivity.class);
								intent.putExtra("data", mResult);
								startActivity(intent);
							}
						}else{
							setMaker(2);
						}
					}
				});
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tv_second_hand://二手房
			defaultColor();
			resetData();
			isFirst = 1;
			b = true;
			tv_second_hand.setTextColor(Color.parseColor("#66c6f2"));
			baiduMap.clear();
			updateMap();
			mId.put("id", "2");
			network(mId.get("id"));
			break;
		case R.id.tv_rent://租房
			defaultColor();
			resetData();
			isFirst = 1;
			b = true;
			tv_rent.setTextColor(Color.parseColor("#66c6f2"));
			baiduMap.clear();
			mId.put("id", "1");
			updateMap();
			network(mId.get("id"));
			break;
		case R.id.iv_exit:
			finish();
			break;
		case R.id.et_search:
			Intent intent = new Intent(context, SearchActivity.class);
			startActivity(intent);
			break;
		}
	}
	private void updateMap() {
		LatLng point = new LatLng(39.914884, 116.403883);
			MapStatus mapStatus = new MapStatus.Builder()
			.target(point).zoom(12).build();
	MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
			.newMapStatus(mapStatus);
	baiduMap.setMapStatus(mapStatusUpdate);
	}

	private void defaultColor() {
		tv_second_hand.setTextColor(Color.parseColor("#000000"));
		tv_rent.setTextColor(Color.parseColor("#000000"));
	}
	@Override
	public void finish() {
		super.finish();
		Editor edit = mSP.edit();
		edit.putString("listName", null);
		edit.putString("parentId", null);
		edit.commit();
	}
}
