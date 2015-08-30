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
import com.mobile.liujiucheng.main.bean.Retalbean;
import com.mobile.liujiucheng.main.bean.SecondHouse;
import com.mobile.liujiucheng.main.bean.Retalbean.BData;
import com.mobile.liujiucheng.main.utils.GsonUtil;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.StreamTools;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.main.widget.PullToRefreshListView;
import com.mobile.liujiucheng.sixninecheng.main.RetalActivity.ViewHolder;
/**
 * 地图的出租房
 * @author pc
 */
public class MapRentActivity extends BaseActivity implements OnClickListener {

	private String data;
	/**
	 * 解析JSON字符串
	 */
	private List<BData> mData;
	
	private List<BData> mmData;
	
	private List<BData> mLoadData;//下拉加载的数据
	private Retalbean bean;
	/**
	 * 状态码
	 */
	private String mStatus;
	private Context context;
	
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	/**
	 * 添加的头部
	 */
	private View mFootView;
	private BitmapUtils bitmapUtils;
	private MyOnScrollListener mScrListener;
	private SharedPreferences mSP;
	
	
	private TextView mCancal;
	private TextView mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_map_rent);
		Intent intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent  
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据  
        data = bundle.getString("data");
        initView();
	}
	
	private void initView() {
		if (mSP == null) {
			mSP = this.getSharedPreferences("cache", MODE_PRIVATE);
		}
		mData = new ArrayList<Retalbean.BData>();
		// 传入回调接口
		context = this;
		mTitle = (TextView) findViewById(R.id.tv_title);
		mCancal = (TextView) findViewById(R.id.tv_cancal);
		mCancal.setOnClickListener(this);
		bitmapUtils = new BitmapUtils(context);
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_main_collection);
		//mPullToRefreshListView.setOnRefreshListener(mOnrefreshListener);
		mListView = mPullToRefreshListView.getRefreshableView();
		mScrListener = new MyOnScrollListener();
		mListView.setOnScrollListener(mScrListener);
		mFootView = View.inflate(context, R.layout.footer, null);
		parseJson();
		setMyadapter();
		setListItemClick();
	}
	private MyAdapter adapter;
	private void setMyadapter() {
		if(mData == null || mData.size() == 0){
			return;
		}
		mTitle.setText(mData.get(0).bussinessarea);
		if (adapter == null) {
			adapter = new MyAdapter(mData, context) {
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View view;
					ViewHolder holder;
					if (convertView == null) {
						holder = new ViewHolder();
						view = View.inflate(context, R.layout.collection_item,
								null);
						holder.mDate = (TextView) view
								.findViewById(R.id.tv_data);
						holder.mTitle = (TextView) view
								.findViewById(R.id.tv_title);
						holder.mHouse = (TextView) view
								.findViewById(R.id.tv_house);

						holder.mPrice = (TextView) view
								.findViewById(R.id.tv_price);
						holder.mPosition = (TextView) view
								.findViewById(R.id.tv_position);
						holder.mImage = (ImageView) view
								.findViewById(R.id.iv_collection_icon);
						view.setTag(holder);
					} else {
						view = convertView;
						holder = (ViewHolder) view.getTag();
					}
					holder.mDate.setText(mData.get(position).pubTume);
					holder.mTitle.setText(mData.get(position).title);
					holder.mHouse.setText("面积:" + mData.get(position).spec
							+ "/m² - 户型 - " + mData.get(position).houseType
							+ "");
					holder.mPrice
							.setText("" + mData.get(position).rent + "元/月");
					holder.mPosition.setText("" + mData.get(position).city
							+ " - " + mData.get(position).district + " - "
							+ mData.get(position).bussinessarea + "");
					bitmapUtils.display(holder.mImage, mData.get(position).imgUrl);
					return view;
				}
			};
			mListView.setAdapter(adapter);
		} else {
			if (mData.size() > 0) {
				adapter.notifyDataSetChanged();
			}
		}
	}
	private void parseJson() {
		if (!TextUtils.isEmpty(data)) {
			bean = GsonUtil.json2Bean(data, Retalbean.class);
			mStatus = bean.status;
			Log.e("TAG", "mStatus==" + mStatus);
			if (mStatus.equals("N")) {
				Toast.makeText(context, "没有更多的数据", Toast.LENGTH_SHORT).show();
			} else {
				if(mData != null){
					mData.clear();
				}
				//Log.e("TAG", "===mData===="+mData);
				mmData = bean.data;
				if(mmData != null){
					mData.addAll(mmData);
				}else{
					Toast.makeText(context, "数据为空", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	private int page = 2;
	private class MyOnScrollListener implements OnScrollListener{
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				int mLastPosition = mListView.getLastVisiblePosition();
				if (mData.size() == mLastPosition) {
					NetHead head = new NetHead(context);
					RequestParams params = head.setHeader();
					HttpUtils http = new HttpUtils();
					params.addBodyParameter("houseT", "1");
					params.addBodyParameter("city", "北京");
					params.addBodyParameter("page", (page++) + "");
					String listName = mSP.getString("listName", null);
					final String parentId = mSP.getString("parentId", null);
					if(!TextUtils.isEmpty(listName)){
						params.addBodyParameter("listName",listName);
					}
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
							Toast.makeText(getApplicationContext(),
									StreamTools.getString(), Toast.LENGTH_SHORT).show();
							mListView.removeFooterView(mFootView);
						}
						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							mListView.removeFooterView(mFootView);
							data = arg0.result;
							if (!TextUtils.isEmpty(data)) {
								data = arg0.result;
								bean = GsonUtil.json2Bean(data,
										Retalbean.class);
								mLoadData = bean.data;
								if (bean.status.equals("N")) {
									Toast.makeText(
											getApplicationContext(),
											"没有更多的数据", Toast.LENGTH_SHORT).show();
								} else {
									if (mLoadData.size() > 0 && mLoadData != null ) {
										mData.addAll(mLoadData);
										adapter.notifyDataSetChanged();
									}
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
			
		}
	}
	private void setListItemClick() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context, DetailsActivity.class);
				// 传递数据
				intent.putExtra("sid", mData.get(position - 1).sid);
				intent.putExtra("tag", "1");
				context.startActivity(intent);
				finish();
			}
		});
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
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
