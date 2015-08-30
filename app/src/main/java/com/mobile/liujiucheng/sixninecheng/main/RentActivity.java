package com.mobile.liujiucheng.sixninecheng.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.mobile.liujiucheng.main.utils.DialogUtils;
import com.mobile.liujiucheng.main.utils.GsonUtil;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.NetworkUtils;
import com.mobile.liujiucheng.main.utils.StreamTools;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.main.widget.PullToRefreshBase.OnRefreshListener;
import com.mobile.liujiucheng.main.widget.PullToRefreshListView;
import com.mobile.liujiucheng.sixninecheng.main.click.AreaPopClick;
import com.mobile.liujiucheng.sixninecheng.main.click.AreaPopClick.RequestSucessAndFail;
/**
 * 二手房
 * @author pc
 *
 */
public class RentActivity extends Activity implements OnClickListener {
	
	private LinearLayout mLinearLayoutArea;
	private LinearLayout mLinearLayoutClass;
	private LinearLayout mLinearLayoutPrice;

    private TextView tv_1;

	private Context context;

	private View mViewArea;
	private View mViewClass;
	private View mViewPrice;

	private View mView;
	
	private EditText et_search;//调到搜索界面

	private ListView mListView01;
//	private ListView mListView02;
//	private ListView mListView03;
	/**
	 * 类型ListView
	 */
	private ListView mListViewClass;
	/**
	 * 价格ListView
	 */
	private ListView mListViewPrice;

	private List<String> mList01;
//	private List<String> mList02;
//	private List<String> mList03;
	/**
	 * 类型
	 */
	private List<String> mListClass;
	/**
	 * 价格
	 */
	private List<String> mListPrice;
	private MyAdapter mAdapter01;
	
	private MyAdapter mAdapter02;
	private MyAdapter mAdapter03;

	private static final String showData = "没有更多的数据";
	/**
	 * 记录价格当前点中的位置
	 */
	private int currentPositionPrice = 0;
	/**
	 * 记录类型当前点中的位置
	 */
	private int currentPositionClass = 0;
	private PopupWindow mPopWindow;
	/**
	 * 记录用户选择的数据
	 */
	private Map<String, String> mMap;
	/**
	 * 记录用户选择的区域
	 */
	private Map<String, String> mMapArea;

	private AreaPopClick areaPopClick;

	private ImageView mImageView;
	private ImageView mImage;
	/**
	 * 刷新的ListView
	 */
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	/**
	 * 添加的头部
	 */
	private View mFootView;

	private BitmapUtils bitmapUtils;
	private MyOnScrollListener mScrListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_secondary_housing);
		getData();
		initView();
	}
	private void getData() {
		Intent intent = getIntent();
		mParams = intent.getStringExtra("mParams");
	}
	OnRefreshListener mOnrefreshListener = new OnRefreshListener() {
		public void onRefresh() {
			// new GetDataTask(mPullRefreshListView.getRefreshType()).execute();
			// 访问网络数据
			if(TextUtils.isEmpty(mParams)){
				network();
			}else{
				requestWork();
			}
		}
	};
	private void initView() {
		et_search = (EditText) findViewById(R.id.et_search);
		mImage = (ImageView) findViewById(R.id.iv_exit);
		mImage.setOnClickListener(this);
		et_search.setOnClickListener(this);
		context = this;
		mmListHouse = new ArrayList<SecondHouse.MData>();
		mListHouse = new ArrayList<SecondHouse.MData>();
		bitmapUtils = new BitmapUtils(context);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_main_collection);
		mListView = mPullRefreshListView.getRefreshableView();
		mScrListener = new MyOnScrollListener();
		mListView.setOnScrollListener(mScrListener);
		mPullRefreshListView.setOnRefreshListener(mOnrefreshListener);
		mView = View.inflate(context, R.layout.layout_dialog_item, null);
		mFootView = View.inflate(context, R.layout.footer, null);
		utils = new DialogUtils(context);
		utils.show();
		if(TextUtils.isEmpty(mParams)){
			network();
		}else{
			requestWork();
		}
		mMap = new HashMap<String, String>();
		if (mMapArea == null) {
			mMapArea = new HashMap<String, String>();
		}
		context = this;
		mLinearLayoutArea = (LinearLayout) findViewById(R.id.ll_area_position);
		mLinearLayoutClass = (LinearLayout) findViewById(R.id.ll_my_class);
		mLinearLayoutPrice = (LinearLayout) findViewById(R.id.ll_my_price);

		mLinearLayoutArea.setOnClickListener(this);
		mLinearLayoutClass.setOnClickListener(this);
		mLinearLayoutPrice.setOnClickListener(this);

		mViewArea = View.inflate(context, R.layout.popwindow_area, null);
		mViewClass = View.inflate(context, R.layout.popwindow_similar, null);
		mViewPrice = View.inflate(context, R.layout.popwindow_price, null);
	}

	/**
	 * 访问网络
	 */
	private HttpUtils http;
	private DialogUtils utils;
	private String mResult = "";
	/**
	 * 二手房的 javabean
	 */
	private List<MData> mListHouse;
	
	private List<MData> mmListHouse;
	
	private SecondHouse mSendHouse;
	
	private MyAdapter mAdapter;
	/**
	 * 判断网络的连接情况
	 */
	private boolean isConnection() {
		context = this;
		int type = NetworkUtils.getConnctedType(this);
		if(type == -1){
			Toast.makeText(this, this.getResources().getText(R.string.net_connection),Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	private void network() {
		RequestParams params = getHeader();
		if(mMapArea != null){
			if (mMapArea.size() > 0) {
				params.addBodyParameter("district", mMapArea.get("area"));
				params.addBodyParameter("bussinessarea", mMapArea.get("mPosition"));
			}
		}
		if(mMap != null){
			if (mMap.size() > 0) {
				params.addBodyParameter("houseType", mMap.get("mClass"));
				params.addBodyParameter("mRent", mMap.get("mPrice"));
			}
		}
		http = new HttpUtils();
		http.send(HttpMethod.POST, UrlsUtils.urls, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(context, StreamTools.getString(), Toast.LENGTH_SHORT).show();
						utils.closeDialog();
						colseHeard();
					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						colseHeard();
						mResult = arg0.result;
						utils.closeDialog();
						praseJson();
						setListAdapter();
					}
				});
	}
	private RequestParams getHeader() {
		NetHead head = new NetHead(context);
		RequestParams params = head.setHeader();
		params.addBodyParameter("type","findSecondHouse");
		params.addBodyParameter("city","北京");
		return params;
	}
	private int page = 2;
	private int pageNo = 2;
	private List<MData> mListLoadHouse;
	private class MyOnScrollListener implements OnScrollListener{
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			// 空闲的状态
			case OnScrollListener.SCROLL_STATE_IDLE:
				int mLastPosition = mListView.getLastVisiblePosition();
				String mUrls = UrlsUtils.urls;
				if (mListHouse.size() == mLastPosition) {
					if (http == null) {
						http = new HttpUtils();
					}
					RequestParams params = getHeader();
					if(TextUtils.isEmpty(mParams)){
						params.addBodyParameter("page",(page++)+"");
						if(mMapArea != null){
							if (mMapArea.size() > 0) {
								params.addBodyParameter("district", mMapArea.get("area"));
								params.addBodyParameter("bussinessarea", mMapArea.get("mPosition"));
							}
						}
						if(mMap != null){
							if (mMap.size() > 0) {
								params.addBodyParameter("houseType", mMap.get("mClass"));
								params.addBodyParameter("mRent", mMap.get("mPrice"));
							}
						}
					}else{
						params = new RequestParams();
						params.addBodyParameter("pageNo", (pageNo++) + "");
						params.addBodyParameter("type","getData");
						params.addBodyParameter("param",mParams);
						params.addBodyParameter("htype","secondHouse");
						params.addBodyParameter("cityCode","beijing");
						mUrls = UrlsUtils.urlLoading;
					}
					mListView.addFooterView(mFootView);
					http.send(HttpMethod.POST, mUrls, params,
							new RequestCallBack<String>() {
								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									Toast.makeText(context, StreamTools.getString(), Toast.LENGTH_SHORT).show();
									mListView.removeFooterView(mFootView);
									if(TextUtils.isEmpty(mParams)){
										page--;
									}else{
										pageNo--;
									}
								}
								@Override
								public void onSuccess(ResponseInfo<String> arg0) {
									mListView.removeFooterView(mFootView);
									mResult = arg0.result;
									if (!TextUtils.isEmpty(mResult)) {
										mSendHouse = GsonUtil.json2Bean(mResult, SecondHouse.class);
										mListLoadHouse = mSendHouse.data; 
										if(mListLoadHouse != null){
											mListHouse.addAll(mListLoadHouse);
											if(mListLoadHouse.size()>0){
												mAdapter.notifyDataSetChanged();
											}
										}else{
											Toast.makeText(context, showData, Toast.LENGTH_SHORT).show();
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
	private void colseHeard() {            //关闭下拉刷新的转圈圈
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}
	}
	protected void setListAdapter() {
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
                        holder.tv_1 = (TextView) view
                                .findViewById(R.id.tv_1);
						view.setTag(holder);
					} else {
						view = convertView;
						holder = (ViewHolder) view.getTag();
					}
					holder.mTitle.setText(mListHouse.get(position).title);
					holder.mHouse.setText("面积:" + mListHouse.get(position).spec
							+ "/m²");
					if(mListHouse.get(position).rent.equals("0")){
						holder.mPrice.setText("面议");
					}else{
						holder.mPrice.setText("" + mListHouse.get(position).rent + "万");
					}
                    holder.tv_1.setText("户型:"+ mListHouse.get(position).houseType + "");
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
			}
		});
	}
	static class ViewHolder {
		TextView mTitle;
		TextView mHouse,tv_1;
		TextView mPrice;
		ImageView mImage;
	}
	/**
	 * 状态
	 */
	private String state;
	private void praseJson() {
		if (!TextUtils.isEmpty(mResult)) {
			mSendHouse = GsonUtil.json2Bean(mResult, SecondHouse.class);
			mmListHouse = mSendHouse.data;
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
				if(mmListHouse.size() > 0){
					String sid = mmListHouse.get(0).sid;
					mListHouse.addAll(mmListHouse);
				}
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//获取数据,访问网络
				if(resultCode == RESULT_CODE ){
					mParams = data.getStringExtra("params");
					requestWork();
				}
	}
	private void requestWork() {
		boolean connection = isConnection();
		if(!connection){
			return ;
		}
		utils.show();
		NetHead head = new NetHead(context);
		RequestParams params = head.setHeader();
		params.addBodyParameter("type","getData");
		params.addBodyParameter("param",mParams);
		params.addBodyParameter("htype","secondHouse");
		params.addBodyParameter("cityCode","beijing");
		if(http == null){
			http = new HttpUtils();
		}
		http.send(HttpMethod.POST, UrlsUtils.urlLoadingSecond, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(getApplicationContext(), StreamTools.getString(), Toast.LENGTH_SHORT).show();
				utils.closeDialog();
				colseHeader();
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				colseHeader();
				utils.closeDialog();
				mResult = arg0.result;
				Log.e("TAG", "mResult ="+mResult);
				praseJson();
				setListAdapter();
			}
		});
	}
	protected void colseHeader() {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}
	}
	private static final int REQUEST_CODE = 100;
	private static final int RESULT_CODE = 200;
	private String mParams;
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.iv_exit:
			finish();
			break;
		case R.id.et_search:
			Intent intent = new Intent(context, TwoSearchActivity.class);
			intent.putExtra("tag", "2");//代表二手房
			startActivityForResult(intent, REQUEST_CODE);
			break;
		case R.id.ll_area_position:
			setPopWindow(v, mViewArea);
			setPopClick(mViewArea, id);
			break;
		case R.id.ll_my_class:
			setPopWindow(v, mViewClass);
			setPopClick(mViewClass, id);
			break;
		case R.id.ll_my_price:
			setPopWindow(v, mViewPrice);
			setPopClick(mViewPrice, id);
			break;
		}
	}
	
	private void setPopClick(View view, int id) {
		initPopWindowView(view, id);
	}
	private void initPopWindowView(View view, int id) {
		switch (id) {
		case R.id.ll_area_position:
			init(view, id);
			break;
		case R.id.ll_my_class:
			initMyData(view);
			break;
		case R.id.ll_my_price:
			initPriceData(view);
			break;
		}
	}
	private void initPriceData(View view) {
		mListViewPrice = (ListView) view.findViewById(R.id.lv_price);
		mListPrice = new ArrayList<String>();
		initPriceData();
		setPriceAdapter();
	}
	private void setPriceAdapter() {
		if(mAdapter03 == null){
			mAdapter03 = new MyAdapter(mListPrice,context) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					View view;
					if (convertView == null) {
						view = View.inflate(context, R.layout.layout_text_item, null);
					} else {
						view = convertView;
					}
					TextView tv = (TextView) view.findViewById(R.id.tv_context);
					tv.setText(mListPrice.get(position));
					if (currentPositionPrice == position) {
						tv.setTextColor(getResources().getColor(R.color.house_item_color));
						//view.setBackgroundColor(Color.parseColor("#FFFFFF"));
					} else {
						tv.setTextColor(getResources().getColor(R.color.hidden_search_word));
						//view.setBackgroundColor(Color.parseColor("#EEEEEE"));
					}
					return view; 
				}
			};
			mListViewPrice.setAdapter(mAdapter03);
		}else{
			mAdapter03.notifyDataSetChanged();
		}
		mListViewPrice.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentPositionPrice = position;
				mAdapter03.notifyDataSetChanged();
				if(position == 0){
					mMap.put("mPrice", "");
				}else if(position == 1){
					mMap.put("mPrice", "0-100");
				}else if(position == mListPrice.size() - 1){
					mMap.put("mPrice", "1000");
				}else{
					String money = mListPrice.get(position).substring(0, mListPrice.get(position).length()-1);
					mMap.put("mPrice" , money );
				}
				//访问网络0
				closePop();
				isConnection();
				networkClass();
			}
		});
	}
	private void initPriceData() {
		mListPrice.add("不限");
		mListPrice.add("100万以下");
		mListPrice.add("100-150万");
		mListPrice.add("150-200万");
		mListPrice.add("200-250万");
		mListPrice.add("250-300万");
		mListPrice.add("300-500万");
		mListPrice.add("500-1000万");
		mListPrice.add("1000万以上");
	}
	private void initMyData(View view) {
		mListViewClass = (ListView) view.findViewById(R.id.lv_class);
		mListClass = new ArrayList<String>();
		initClassData();
		setMyData();
	}
	private void setMyData() {
		if(mAdapter02 == null){
			mAdapter02 = new MyAdapter(mListClass,context) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					View view;
					if (convertView == null) {
						view = View.inflate(context, R.layout.layout_text_item, null);
					} else {
						view = convertView;
					}
					TextView tv = (TextView) view.findViewById(R.id.tv_context);
					tv.setText(mListClass.get(position));
					if (currentPositionClass == position) {
						tv.setTextColor(getResources().getColor(R.color.house_item_color));
					} else {
						tv.setTextColor(getResources().getColor(R.color.hidden_search_word));
						//view.setBackgroundColor(Color.parseColor("#EEEEEE"));
					}
					return view;
				}
			};
			mListViewClass.setAdapter(mAdapter02);
		}else{
			mAdapter02.notifyDataSetChanged();
		}
		/**
		 * 二手房的类型
		 */
		mListViewClass.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//mMap
				currentPositionClass = position;
				if(position == 0){
					mMap.put("mClass", "");
				}else{
					mMap.put("mClass", mListClass.get(position));
				}
				mAdapter02.notifyDataSetChanged();
				//访问网络
				closePop();
				isConnection();
				networkClass();
			}
		});
	}
	/**
	 * 关闭窗口
	 */
	public void closePop(){
		if (mPopWindow != null) {
			mPopWindow.dismiss();
		}
	}
	/**
	 * 访问网络
	 */
	protected void networkClass() {
		Log.e("TAG", "mMapArea=="+mMapArea);
		Log.e("TAG", "mMap=="+mMap);
		NetHead head = new NetHead(context);
		RequestParams setHeader = head.setHeader();
		setHeader.addBodyParameter("type", "findSecondHouse");
		setHeader.addBodyParameter("city", "北京");
		setHeader.addBodyParameter("district", mMapArea.get("area"));
		setHeader.addBodyParameter("bussinessarea", mMapArea.get("mPosition"));
		setHeader.addBodyParameter("houseType", mMap.get("mClass"));
		setHeader.addBodyParameter("mRent", mMap.get("mPrice"));
		utils.show();
		if (http == null) {
			http = new HttpUtils();
		}
		http.send(HttpMethod.POST, UrlsUtils.urls, setHeader,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(context, StreamTools.getString(), Toast.LENGTH_SHORT).show();
						utils.closeDialog();
					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (!TextUtils.isEmpty(arg0.result)) {
							mResult = arg0.result;
							utils.closeDialog();
							praseJson();
							setListAdapter();
						}
					}
				});
	}
	/**
	 * 填充数据
	 */
	private void initClassData() {
		mListClass.add("不限");
		mListClass.add("一室");
		mListClass.add("二室");
		mListClass.add("三室");
		mListClass.add("四室");
		mListClass.add("四室以上");
	}

	private void init(View view, int id) {
		mListView01 = (ListView) view.findViewById(R.id.lv_area);
//		mListView02 = (ListView) view.findViewById(R.id.lv_line);
//		mListView03 = (ListView) view.findViewById(R.id.lv_position);
		initData();
	}
	private void initData() {
		mList01 = new ArrayList<String>();
//		mList02 = new ArrayList<String>();
//		mList03 = new ArrayList<String>();
		mList01.add("区域");
		//mList01.add("地铁");
		setMyAdapter01();
	}
	private void setMyAdapter01() {
		mAdapter01 = new MyAdapter(mList01, context) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view;
				if (convertView == null) {
					view = View.inflate(context, R.layout.layout_text_item_area,
							null);
					TextView tv = (TextView) view.findViewById(R.id.tv_context);
					tv.setText(mList01.get(position));
					tv.setTextColor(getResources().getColor(R.color.house_item_color));
				} else {
					view = convertView;
				}
				return view;
			}
		};
		mListView01.setAdapter(mAdapter01);
		if (areaPopClick == null) {
			areaPopClick = new AreaPopClick(mViewArea, context, mListView01);
		}
		areaPopClick.setRequestSucessAndFail(new RequestSucessAndFail() {
			@Override
			public void isSuccessAndFail(int tag) {
				if(tag == 0){
				}
				if(tag == 1){
					mResult = areaPopClick.mResquestData();
					praseJson();
					if(mListHouse.size() > 0){
						mAdapter.notifyDataSetChanged();
					}
				}
			}
		});
		areaPopClick.setmPopupWindow01(mPopWindow,1);
		// 获取到区域的值
		mMapArea = areaPopClick.getMap();
		areaPopClick.setMap(mMap);
	}

	@SuppressLint("InlinedApi")
	private void setPopWindow(View v, View view) {
		if (mPopWindow == null) {
			mPopWindow = new PopupWindow(context);
			mPopWindow.setHeight(LayoutParams.MATCH_PARENT);
			mPopWindow.setWidth(LayoutParams.MATCH_PARENT);
			mPopWindow.setFocusable(true);
			mPopWindow.setOutsideTouchable(true);
            mPopWindow.setContentView(view);
            mPopWindow.showAsDropDown(v);
			ColorDrawable dw = new ColorDrawable(0x60000000);
			mPopWindow.setBackgroundDrawable(dw);
//            mPopWindow.setContentView(view);                                      //PopWindow显示的样式
//            mPopWindow.showAsDropDown(v);                                         //PopWindow显示的位置
		} else {
			mPopWindow.setContentView(view);
			mPopWindow.showAsDropDown(v);
		}
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mPopWindow != null) {
					mPopWindow.dismiss();
				}
				return true;
			}
		});
	}


}
