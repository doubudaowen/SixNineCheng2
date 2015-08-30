package com.mobile.liujiucheng.sixninecheng.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mobile.liujiucheng.main.adapter.MyAdapter;
import com.mobile.liujiucheng.main.bean.SecondHouse;
import com.mobile.liujiucheng.main.bean.SecondHouse.MData;
import com.mobile.liujiucheng.main.utils.GsonUtil;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.StreamTools;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.main.widget.PullToRefreshListView;
/**
 * 地图的二手房
 * @author pc
 *
 */
public class MapSecondActivity extends BaseActivity implements OnClickListener {

	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	
	/**
	 * 二手房的 javabean
	 */
	private List<MData> mListHouse;
	
	private List<MData> mmListHouse;
	
	private SecondHouse mSendHouse;
	
	private MyAdapter mAdapter;
	private String data;
	
	private Context context;
	private BitmapUtils bitmapUtils;
	
	private MyOnScrollListener mScrListener;
	private SharedPreferences mSP;
	
	/**
	 * 添加的头部
	 */
	private View mFootView;
	
	private TextView mCancal;
	private TextView mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_map_second);
		Intent intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent  
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据  
        data = bundle.getString("data");
        initView();
	}
	
	private void initView() {
		if (mSP == null) {
			mSP = this.getSharedPreferences("cache", MODE_PRIVATE);
		}
		context = this;
		mTitle = (TextView) findViewById(R.id.tv_title);
		mCancal = (TextView) findViewById(R.id.tv_cancal);
		mCancal.setOnClickListener(this);
		mFootView = View.inflate(context, R.layout.footer, null);
		mListHouse = new ArrayList<SecondHouse.MData>();
		bitmapUtils = new BitmapUtils(context);
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_main_collection);
		mListView = mPullToRefreshListView.getRefreshableView();
		mScrListener = new MyOnScrollListener();
		mListView.setOnScrollListener(mScrListener);
		parseJson();
		setMyAdapter();
	}

	private void setMyAdapter() {
		if(mListHouse == null || mListHouse.size() == 0){
			return;
		}
		mTitle.setText(mListHouse.get(0).bussinessarea);
		if(mAdapter == null){
			mAdapter = new MyAdapter(mListHouse,context) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					View view;
					ViewHolder holder;
					if (convertView == null) {
						holder = new ViewHolder();
						view = View.inflate(context, R.layout.collection_second_item, null);
						holder.mTitle = (TextView) view.findViewById(R.id.tv_title);
						holder.mHouse = (TextView) view.findViewById(R.id.tv_house);
						holder.mPrice = (TextView) view.findViewById(R.id.tv_price);
						holder.mImage = (ImageView) view
								.findViewById(R.id.iv_collection_icon);
						view.setTag(holder);
					} else {
						view = convertView;
						holder = (ViewHolder) view.getTag();
					}
					holder.mTitle.setText(mListHouse.get(position).title);
					holder.mHouse.setText("面积:" + mListHouse.get(position).spec
							+ "/m² - 户型 - " + mListHouse.get(position).houseType + "");
					if(mListHouse.get(position).rent.equals("0")){
						holder.mPrice.setText("面议");
					}else{
						holder.mPrice.setText("" + mListHouse.get(position).rent + "万");
					}
					bitmapUtils.display(holder.mImage, mListHouse.get(position).imgUrl);
					return view;
				}
			};
			mListView.setAdapter(mAdapter);
		}else{
			if(mListHouse.size() >0){
				mAdapter.notifyDataSetChanged();
			}
		}
		/**
		 * 准备传值
		 */
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context, DetailsActivity.class);
				// 传递数据
				intent.putExtra("sid", mListHouse.get(position - 1).sid);
				intent.putExtra("tag", "2");
				context.startActivity(intent);
				finish();
			}
		});
	}
	private String state;
	private void parseJson() {
		mSendHouse = GsonUtil.json2Bean(data, SecondHouse.class);
		mmListHouse = mSendHouse.data;	
		//Log.e("TAG", "mmListHouse == "+ mmListHouse);
		state = mSendHouse.status;
		Log.e("TAG", "state=="+state);
		if(state.equals("N")){
			Toast.makeText(context, "没有更多的数据", Toast.LENGTH_SHORT).show();
			return;
		}
		if(mListHouse != null){
			mListHouse.clear();
		}
		if(mmListHouse != null){
			mListHouse.addAll(mmListHouse);
		}
	}
	
	private int page = 2;
	private List<MData> mListLoadHouse;
	private class MyOnScrollListener implements OnScrollListener{

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				int mLastPosition = mListView.getLastVisiblePosition();
				if (mListHouse.size() == mLastPosition) {
					NetHead head = new NetHead(context);
					RequestParams params = head.setHeader();
					HttpUtils http = new HttpUtils();
					params.addBodyParameter("houseT", "2");
					params.addBodyParameter("city", "北京");
					params.addBodyParameter("page", (page++) + "");
					
					String listName = mSP.getString("listName", null);
					final String parentId = mSP.getString("parentId", null);
					if(!TextUtils.isEmpty(listName)){
						params.addBodyParameter("listName",listName);
					}
					Log.e("TAG", "==2==="+listName);
					if(!TextUtils.isEmpty(parentId)){
						params.addBodyParameter("parentId",parentId);
						params.addBodyParameter("type", "mapQueryHouse");
					}
					mListView.addFooterView(mFootView);
					http.send(HttpMethod.POST, UrlsUtils.urls, params,
							new RequestCallBack<String>(){
						@Override
						public void onFailure(HttpException arg0,
								String arg1) {
							Toast.makeText(getApplicationContext(),StreamTools.getString(), Toast.LENGTH_SHORT).show();
							mListView.removeFooterView(mFootView);
						}
						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							mListView.removeFooterView(mFootView);
							data = arg0.result;
							if (!TextUtils.isEmpty(data)) {
								mSendHouse = GsonUtil.json2Bean(data, SecondHouse.class);
								mListLoadHouse = mSendHouse.data; 
								if(mListLoadHouse != null){
									mListHouse.addAll(mListLoadHouse);
									if(mListLoadHouse.size()>0){
										mAdapter.notifyDataSetChanged();
									}
								}else{
									Toast.makeText(context, "没有更多的数据", Toast.LENGTH_SHORT).show();
								}
							}
						}
					});
				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			
		}
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}
	static class ViewHolder {
		TextView mTitle;
		TextView mHouse;
		TextView mPrice;
		ImageView mImage;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.tv_cancal:
			finish();
			break;
		}
	}
}
