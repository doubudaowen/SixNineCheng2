package com.mobile.liujiucheng.sixninecheng.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
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
import com.mobile.liujiucheng.main.bean.Retalbean;
import com.mobile.liujiucheng.main.bean.Retalbean.BData;
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

/***
 * 出租房
 */
public class RetalActivity extends Activity implements OnClickListener {

	
	private LinearLayout mLinearLayoutArea;
	private LinearLayout mLinearLayoutClass;
	private LinearLayout mLinearLayoutPrice;
	private Context context;

	private View mViewArea;
	private View mViewClass;
	private View mViewPrice;

	private View mView;

	private ListView mListView01;
	private ListView mListView02;
	private ListView mListView03;
	/**
	 * 类型ListView
	 */
	private ListView mListViewClass;
	/**
	 * 价格ListView
	 */
	private ListView mListViewPrice;

	private List<String> mList01;
	//private List<String> mList02;
	//private List<String> mList03;
	/**
	 * 类型
	 */
	private List<String> mListClass;
	/**
	 * 价格
	 */
	private List<String> mListPrice;
	private MyAdapter mAdapter01;
	//private MyAdapter mAdapter02;
	//private MyAdapter mAdapter03;

	private ClassAdapter mAdapterClass;
	private PriceAdapter mAdapterPrice;
	/**
	 * 回显数据
	 */
	private SharedPreferences sp;
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
	//private ImageView mImageView;
	
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
	private MyOnScrollListener mScrListener;
	
	private EditText et_search;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_rental_housing);
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
	/**
	 * 解析JSON字符串
	 */
	private List<BData> mData;
	
	private List<BData> mmData;
	
	private List<BData> mLoadData;
	private Retalbean bean;
	/**
	 * 状态码
	 */
	private String mStatus;
	
	private void praseJson() {
		if (!TextUtils.isEmpty(mResult)) {
			bean = GsonUtil.json2Bean(mResult, Retalbean.class);
			mStatus = bean.status;
			if (mStatus.equals("N")) {
				Toast.makeText(context, "没有更多的数据", Toast.LENGTH_SHORT).show();
			} else {
				if(mData != null){
					mData.clear();
				}
				mmData = bean.data;
				mData.addAll(mmData);
				if(adapter != null){
					adapter.notifyDataSetChanged();
				}else{
					setListAdapter();
				}
			}
		}
	}
	private int page = 2;
	private int pageNo = 2;
	private class MyOnScrollListener implements OnScrollListener {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			// 空闲的状态
			case OnScrollListener.SCROLL_STATE_IDLE:
				// adapter.notifyDataSetChanged();
				int mLastPosition = mListView.getLastVisiblePosition();
				String mUrls = UrlsUtils.urls;
				if (mData.size() == mLastPosition) {
					setHeader();
					if(TextUtils.isEmpty(mParams)){
						params.addBodyParameter("type", "findHouse");
						Log.e("TAG", "page====" + page);
						params.addBodyParameter("type", "findHouse");
						params.addBodyParameter("city", "北京");
						params.addBodyParameter("district", mMapArea.get("area"));
						params.addBodyParameter("bussinessarea",
								mMapArea.get("mPosition"));
						params.addBodyParameter("houseType", mMap.get("mClass"));
						params.addBodyParameter("mRent", mMap.get("mPrice"));
						params.addBodyParameter("page", (page++) + "");
					}else{
						params.addBodyParameter("pageNo", (pageNo++) + "");
						params.addBodyParameter("type","getData");
						params.addBodyParameter("param",mParams);
						params.addBodyParameter("htype","rentHouse");
						params.addBodyParameter("cityCode","beijing");
						mUrls = UrlsUtils.urlLoading;
					}
					if (http == null) {
						http = new HttpUtils();
					}
					mListView.addFooterView(mFootView);            //
					http.send(HttpMethod.POST, mUrls, params,
							new RequestCallBack<String>() {
								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									if(TextUtils.isEmpty(mParams)){
										page--;
									}else{
										pageNo--;
									}
									Toast.makeText(getApplicationContext(),StreamTools.getString(), Toast.LENGTH_SHORT).show();
									mListView.removeFooterView(mFootView);
								}
								@Override
								public void onSuccess(ResponseInfo<String> arg0) {
								mListView.removeFooterView(mFootView);
									if (!TextUtils.isEmpty(arg0.result)) {
										mResult = arg0.result;
										bean = GsonUtil.json2Bean(mResult,
												Retalbean.class);
										mLoadData = bean.data;
										if (bean.status.equals("N")) {
											Toast.makeText(
													getApplicationContext(),
													"没有更多的数据===", Toast.LENGTH_SHORT).show();
										} else {
											mData.addAll(mLoadData);
											if (mLoadData.size() > 0) {
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

	/**
	 * 初始化UI
	 */
	private void initView() {
		et_search = (EditText) findViewById(R.id.et_search);
		mImage = (ImageView) findViewById(R.id.iv_exit);
		mImage.setOnClickListener(this);
		et_search.setOnClickListener(this);
		mData = new ArrayList<Retalbean.BData>();
		// 传入回调接口
		context = this;
		bitmapUtils = new BitmapUtils(context);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_main_collection);
		mPullRefreshListView.setOnRefreshListener(mOnrefreshListener);
		mListView = mPullRefreshListView.getRefreshableView();
		setListItemClick();
		mScrListener = new MyOnScrollListener();
		mListView.setOnScrollListener(mScrListener);

		mView = View.inflate(context, R.layout.layout_dialog_item, null);
		mFootView = View.inflate(context, R.layout.footer, null);
		dialogUtils = new DialogUtils(context);
		createDiaLog();
		// MyDiaLog diaLog = new MyDiaLog(context, mView);
		// 开始进来就网络访问网络数据
		// 解析数据
		mMap = new HashMap<String, String>();
		if (mMapArea == null) {
			mMapArea = new HashMap<String, String>();
		}
		if(TextUtils.isEmpty(mParams)){
			network();
		}else{
			requestWork();
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
	 * 点击到详情界面
	 */
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
			}
		});
	}
	private String mResult = "";
	private void network() {
		isConnection();
		http = new HttpUtils();
		setHeader();
		if (mMapArea.size() > 0) {
			params.addBodyParameter("district", mMapArea.get("area"));
			params.addBodyParameter("bussinessarea", mMapArea.get("mPosition"));
		}
		if (mMap.size() > 0) {
			params.addBodyParameter("houseType", mMap.get("mClass"));
			params.addBodyParameter("mRent", mMap.get("mPrice"));
		}
		params.addBodyParameter("type", "findHouse");
		params.addBodyParameter("city", "北京");
		
		http.send(HttpMethod.POST, UrlsUtils.urls, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(context, StreamTools.getString(), Toast.LENGTH_SHORT).show();
						closeDialog();
						colseHeader();
					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						colseHeader();
						if (!TextUtils.isEmpty(arg0.result)) {
							mResult = arg0.result;
							//Log.e("TAG", "mResult =="+mResult);
							closeDialog();
							praseJson();
						}
					}
				});
	}
	private void colseHeader() {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}
	}
	private void setHeader() {
		params = new RequestParams();
		params.setHeader("Accept", "*/*");
		params.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		params.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
		params.setHeader("Accept-Charset", "utf-8");
	}

	private void closeDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	private AlertDialog dialog;
	//private AnimationDrawable ad;
	private void createDiaLog() {
		AlertDialog.Builder builder = new Builder(this);
		dialog = builder.create();
		dialog.setView(mView, 0, 0, 0, 0);
		dialog.show();
	}

	private void setListAdapter() {
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
					if("0".equals(mData.get(position).rent)){
						holder.mPrice.setText("面议");
					}else{
						holder.mPrice
						.setText("" + mData.get(position).rent + "元/月");
					}
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
	private static final int REQUEST_CODE = 100;
	private static final int RESULT_CODE = 200;
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.iv_exit:
			finish();
			break;
		case R.id.et_search:
			Intent intent = new Intent(context, CommonSearchActivity.class);
			intent.putExtra("tag", "1");//代表出租房
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
		isConnection();
		NetHead head = new NetHead(context);
		RequestParams params = head.setHeader();
		//params.addBodyParameter("type","getData");
		params.addBodyParameter("param",mParams);
		Log.e("TAG", mParams);
		//params.addBodyParameter("htype","rentHouse");
		//params.addBodyParameter("cityCode","beijing");
		if(http == null){
			http = new HttpUtils();
		}
		http.send(HttpMethod.POST, UrlsUtils.urlLoading, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(getApplicationContext(), StreamTools.getString(), Toast.LENGTH_SHORT).show();
				closeDialog();
				colseHeader();
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				colseHeader();
				closeDialog();
				mResult = arg0.result;
				//Log.e("TAG", "mResult=="+mResult);
				praseJson();
			}
		});
	}
	/**
	 * 设置区域的点击事件
	 */
	private void setPopClick(View view, int mId) {
		initPopWindowView(view, mId);
	}

	/**
	 * 初始化ListView的控件
	 */
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
		CommData(view, mListViewPrice, mListPrice);
		// 获取价格
		mListViewPrice.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 获取价格
				currentPositionPrice = position;
				String mPrice = mListPrice.get(position);
				if (position == 0) {
					mMap.put("mPrice", "");
				} else if (position == 1) {
					mMap.put("mPrice", "0-500");
				} else if (position == mListPrice.size() - 1) {
					mMap.put("mPrice", "3000");
				} else {
					mMap.put("mPrice", mPrice);
				}
				// Toast.makeText(context,mPrice, 0).show();
				mAdapterPrice.notifyDataSetChanged();
				//Log.e("TAG", "mMapArea==================" + mMapArea);
				//Log.e("TAG", "mMap====================" + mMap);
				closePopWindow();
				// 访问网络
				RequestParams setHeader = setReHeard();
				// 判断网络是否存在
				isConnection();
				networkClass(setHeader);
			}
		});
	}

	private void isConnection() {
		context = this;
		int type = NetworkUtils.getConnctedType(this);
		if (type == -1) {
			Toast.makeText(this, "当前网络不可用,请你检查网络的情况", Toast.LENGTH_SHORT).show();
			return;
		}
	}

	private void closePopWindow() {
		if (mPopWindow != null) {
			mPopWindow.dismiss();
		}
	}

	/**
	 * 初始化价格的数据
	 */
	private void initPriceData() {
		mListPrice.add("不限");
		mListPrice.add("500以下");
		mListPrice.add("500-1000");
		mListPrice.add("1000-2000");
		mListPrice.add("2000-3000");
		mListPrice.add("3000以上");
	}

	private void initMyData(View view) {
		mListViewClass = (ListView) view.findViewById(R.id.lv_class);
		mListClass = new ArrayList<String>();
		initClassData();
		CommDataClass(view, mListViewClass, mListClass);
		mListViewClass.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentPositionClass = position;
				// 获取类型
				String mClass = mListClass.get(position);
				if (position == 0) {
					mMap.put("mClass", "");
				} else {
					mMap.put("mClass", mClass);
				}
				mAdapterClass.notifyDataSetChanged();
				//Toast.makeText(context, "position" + position, 0).show();
				closePopWindow();
				//Log.e("TAG", "mMapArea==================" + mMapArea);
				//Log.e("TAG", "mMap====================" + mMap);
				// 请求网络
				RequestParams setHeader = setReHeard();
				isConnection();
				networkClass(setHeader);
			}
		});
	}

	private RequestParams setReHeard() {
		NetHead head = new NetHead(context);
		RequestParams setHeader = head.setHeader();
		return setHeader;
	}

	protected void networkClass(RequestParams setHeader) {
		setHeader.addBodyParameter("type", "findHouse");
		setHeader.addBodyParameter("city", "北京");
		setHeader.addBodyParameter("district", mMapArea.get("area"));
		setHeader.addBodyParameter("bussinessarea", mMapArea.get("mPosition"));
		setHeader.addBodyParameter("houseType", mMap.get("mClass"));
		setHeader.addBodyParameter("mRent", mMap.get("mPrice"));
		dialogUtils.show();
		if (http == null) {
			http = new HttpUtils();
		}
		http.send(HttpMethod.POST, UrlsUtils.urls, setHeader,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dialogUtils.closeDialog();
						Toast.makeText(context, StreamTools.getString(), Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (!TextUtils.isEmpty(arg0.result)) {
							mResult = arg0.result;
							dialogUtils.closeDialog();
							praseJson();
							if (mData.size() > 0) {
								adapter.notifyDataSetChanged();
							}
						}
					}
				});
	}

	private void CommDataClass(View view, ListView mListViewClass2,
			List<String> mListClass2) {
		if (mAdapterClass == null) {
			mAdapterClass = new ClassAdapter(mListClass2, context);
			mListViewClass2.setAdapter(mAdapterClass);
		} else {
			mAdapterClass.notifyDataSetChanged();
		}
	}

	private void CommData(View view, ListView mList, final List<String> mData) {
		if (mAdapterPrice == null) {
			mAdapterPrice = new PriceAdapter(mData, context);
			mList.setAdapter(mAdapterPrice);
		} else {
			mAdapterPrice.notifyDataSetChanged();
		}
	}

	/**
	 * 初始化类型的数据
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
		mListView02 = (ListView) view.findViewById(R.id.lv_line);
		mListView03 = (ListView) view.findViewById(R.id.lv_position);
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mList01 = new ArrayList<String>();
		//mList02 = new ArrayList<String>();
		//mList03 = new ArrayList<String>();
		mList01.add("区域");
		//mList01.add("地铁");
		setMyAdapter01();
	}

	/**
	 * 设置是匹配器
	 */
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
				if (tag == 0) {
					Toast.makeText(context, StreamTools.getString(), Toast.LENGTH_SHORT).show();
				}
				if (tag == 1) {
					mResult = areaPopClick.mResquestData();
					praseJson();
					if (mData.size() > 0) {
						adapter.notifyDataSetChanged();
					} else {
						Toast.makeText(context, "没有找到合适的数据", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		areaPopClick.setmPopupWindow01(mPopWindow, 0);
		// 获取到区域的值
		mMapArea = areaPopClick.getMap();
		areaPopClick.setMap(mMap);
	}
	/**
	 * 显示的窗口
	 * @param v
	 * @param view
	 */
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
//            mPopWindow.setContentView(view);
//            mPopWindow.showAsDropDown(v);
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
	private class PriceAdapter extends MyAdapter {
		public PriceAdapter(List data, Context context) {
			super(data, context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getView(position, convertView);
		}

		private View getView(int position, View convertView) {
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
	}

	private class ClassAdapter extends MyAdapter {

		public ClassAdapter(List<String> data, Context context) {
			super(data, context);
		}

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
				//view.setBackgroundColor(Color.parseColor("#FFFFFF"));
				tv.setTextColor(getResources().getColor(R.color.house_item_color));
			} else {
				//view.setBackgroundColor(Color.parseColor("#EEEEEE"));
				tv.setTextColor(getResources().getColor(R.color.hidden_search_word));
			}
			return view;
		}
	}
	/**
	 * 
	 */
	// private ListViewAdapter adapter;

	private MyAdapter adapter;

	private class ListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				view = View.inflate(context, R.layout.collection_item, null);
				holder.mDate = (TextView) view.findViewById(R.id.tv_data);
				holder.mTitle = (TextView) view.findViewById(R.id.tv_title);
				holder.mHouse = (TextView) view.findViewById(R.id.tv_house);

				holder.mPrice = (TextView) view.findViewById(R.id.tv_price);
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
					+ "/m² - 户型 - " + mData.get(position).houseType + "");

			if( mData.get(position).rent.equals("0")){
				holder.mPrice.setText("面议");
			}else{
				holder.mPrice.setText("" + mData.get(position).rent + "元/月");
			}
			holder.mPosition.setText("" + mData.get(position).city + " - "
					+ mData.get(position).district + " - "
					+ mData.get(position).bussinessarea + "");
			bitmapUtils.display(holder.mImage, mData.get(position).imgUrl);
			return view;
		}
	}
	private BitmapUtils bitmapUtils;
	private RequestParams params;
	private HttpUtils http;
	private DialogUtils dialogUtils;
	private String mParams;

	static class ViewHolder {
		TextView mDate;
		TextView mTitle;
		TextView mHouse;
		TextView mPrice;
		TextView mPosition;
		ImageView mImage;
	}
}
