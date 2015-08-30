package com.mobile.liujiucheng.main.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
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
import com.mobile.liujiucheng.main.bean.SecondHouse;
import com.mobile.liujiucheng.main.bean.SecondHouse.MData;
import com.mobile.liujiucheng.main.bean.base.BaseFragment;
import com.mobile.liujiucheng.main.utils.DialogUtils;
import com.mobile.liujiucheng.main.utils.GsonUtil;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.NetworkUtils;
import com.mobile.liujiucheng.main.utils.StreamTools;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.main.widget.PullToRefreshBase;
import com.mobile.liujiucheng.main.widget.PullToRefreshBase.OnRefreshListener;
import com.mobile.liujiucheng.main.widget.PullToRefreshListView;
import com.mobile.liujiucheng.sixninecheng.main.DetailsActivity;
import com.mobile.liujiucheng.sixninecheng.main.LoagingActivity;
import com.mobile.liujiucheng.sixninecheng.main.R;
import com.mobile.liujiucheng.sixninecheng.main.SPUtils;

/**
 * 收藏的二手房
 * @author pc Mr_Gang
 * 
 */
public class NewCenterFragment extends BaseFragment implements OnClickListener {

	private View mView;
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private MyAdapter adapter;
	private SecondHouse mSendHouse;// 二手房源javabean
	private Retalbean bean;// 出租房的Javabean
	private List<BData> mListBean;
	private List<MData> mListSendHouse;
	private BitmapUtils bitmapUtils;
	private TextView mTextView;
	private Context context;
	
	private TextView mDelete;//删除房源
	private TextView mCancle;//取消删除
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = View.inflate(getActivity(),
				R.layout.layout_my_collection_second, null);
		return mView;
	}
	/**
	 * 填充UI
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		network();
	}
	private void initData() {
		context = getActivity();
		deleteView = View.inflate(context, R.layout.layout_delete_house, null);
		mDelete = (TextView) deleteView.findViewById(R.id.tv_delete);
		mCancle = (TextView) deleteView.findViewById(R.id.tv_cancal);
		mDelete.setOnClickListener(this);
		mCancle.setOnClickListener(this);
		bitmapUtils = new BitmapUtils(getActivity());
		//deleteHouse();
		if (mListBean == null) {
			mListBean = new ArrayList<Retalbean.BData>();
		}
		if (mListSendHouse == null) {
			mListSendHouse = new ArrayList<SecondHouse.MData>();
		}
		mTextView = (TextView) mView.findViewById(R.id.tv_collection);
		mPullRefreshListView = (PullToRefreshListView) mView
				.findViewById(R.id.lv_content);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            public void onRefresh() {
                // 访问网络数据
                network();
            }
        });
		mListView = mPullRefreshListView.getRefreshableView();
	}

	private HttpUtils http;
	private String mResult;
	private DialogUtils utils;
	private void network() {
		boolean connected = NetworkUtils.isConnection(getActivity());
		if (connected) {
			// 访问网络
			utils = new DialogUtils(getActivity());
			utils.show();
			NetHead head = new NetHead(getActivity());
			RequestParams params = head.setHeader();
			params.addBodyParameter("type", "queryCollect");
			params.addBodyParameter("userId", SPUtils.getUserID(getActivity()));
			params.addBodyParameter("houseT", "2");
			if (http == null) {
				http = new HttpUtils();
			}
			http.send(HttpMethod.POST, UrlsUtils.urls, params,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							Toast.makeText(getActivity(),
									StreamTools.getString(), Toast.LENGTH_SHORT)
									.show();
							utils.closeDialog();
							colseHead();
						}
						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							colseHead();
							mResult = arg0.result;
							//Log.e("TAG", "mResult==" + mResult);
							utils.closeDialog();
							parseJson();
						}
					});
			mTextView.setVisibility(View.GONE);
		} else {
			//Toast.makeText(getActivity(), "当前网络不可用,请你检查网络的情况",Toast.LENGTH_SHORT).show();
			showWords("当前网络不可用,请你检查网络的情况");
		}
	}
	OnRefreshListener mOnrefreshListener = new OnRefreshListener() {
		public void onRefresh() {
			// 访问网络数据
			network();
		}
	};
	protected void setListAdapter() {
		if (adapter == null) {
			adapter = new MyAdapter(mListSendHouse, getActivity()) {
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View view;
					ViewHolder holder;
					if (convertView == null) {
						holder = new ViewHolder();
						view = View.inflate(getActivity(),
								R.layout.collection_second_item, null);
						holder.mTitle = (TextView) view
								.findViewById(R.id.tv_title);
						holder.mHouse = (TextView) view
								.findViewById(R.id.tv_house);
						holder.mPrice = (TextView) view
								.findViewById(R.id.tv_price);
						holder.mImage = (ImageView) view
								.findViewById(R.id.iv_collection_icon);
						view.setTag(holder);
					} else {
						view = convertView;
						holder = (ViewHolder) view.getTag();
					}
					holder.mTitle.setText(mListSendHouse.get(position).title);
					holder.mHouse.setText("面积:"
							+ mListSendHouse.get(position).spec + "/m² - 户型 - "
							+ mListSendHouse.get(position).houseType + "");
					if (mListSendHouse.get(position).rent.equals("0")) {
						holder.mPrice.setText("面议");
					} else {
						holder.mPrice.setText(""
								+ mListSendHouse.get(position).rent + "万");
					}
					bitmapUtils.display(holder.mImage,
							mListSendHouse.get(position).imgUrl);
					return view;
				}
			};
			mListView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),DetailsActivity.class);
				// 传递数据
				intent.putExtra("sid", mListSendHouse.get(position - 1).sid);
				intent.putExtra("tag", "2");
				context.startActivity(intent);
			}
		});
		/**
		 * 长按删除收藏房源
		 */
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				deleteTag = position - 1;
				deleteHouse();
				return true;
			}
		});
	}
	private PopupWindow popupWindow;
	private View deleteView;
	protected void deleteHouse() {
		if(popupWindow == null){
			popupWindow = new PopupWindow(deleteView);
			ColorDrawable dw = new ColorDrawable(0x60000000);
			popupWindow.setBackgroundDrawable(dw);
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
			popupWindow.setHeight(LayoutParams.MATCH_PARENT);
			popupWindow.setWidth(LayoutParams.MATCH_PARENT);
		}
		popupWindow.showAtLocation(mView, Gravity.BOTTOM , 0, 0);
		deleteView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				deletePopwindow();
				return true;
			}
		});
	}

	private void deletePopwindow() {
		if(popupWindow != null){
			popupWindow.dismiss();
		}
	}
	static class ViewHolder {
		TextView mDate;
		TextView mTitle;
		TextView mHouse;
		TextView mPrice;
		TextView mPosition;
		ImageView mImage;
	}
	private String status;
	private String tag = "2";

	protected void parseJson() {
		mSendHouse = GsonUtil.json2Bean(mResult, SecondHouse.class);
		status = mSendHouse.status;
		if ("N".equals(status)) {
			//Toast.makeText(getActivity(), "你还没有收藏相关的房源", Toast.LENGTH_SHORT).show();
			showWords((String)getResources().getText(R.string.collection_house));
		} else {
			mListSendHouse.addAll(mSendHouse.data);
			setListAdapter();
		}
	}
	private void showWords(String data) {
		mTextView.setText(data);
		mTextView.setVisibility(View.GONE);
	}
	private void colseHead() {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}
	}
	private int deleteTag = 0;
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tv_delete:
			netdelete();
			deletePopwindow();
			break;
		case R.id.tv_cancal:
			deletePopwindow();
			break;
		}
	}
	private void netdelete() {
		// 判断是否登录
		String userID = SPUtils.getUserID(context);
		if (TextUtils.isEmpty(userID)) {
			Intent intent = new Intent(context, LoagingActivity.class);
			startActivity(intent);
		} else {
			boolean connection = NetworkUtils.isConnection(context);
			if(!connection){
				showWords("当前网络不可用,请你检查网络的情况");
				return;
			}
			utils.show();
			NetHead head = new NetHead(context);
			RequestParams params = head.setHeader();
			params.addBodyParameter("secondId", mListSendHouse.get(deleteTag).sid);
			params.addBodyParameter("type", "deleteColect");
			params.addBodyParameter("userId", SPUtils.getUserID(context));
			if (http == null) {
				http = new HttpUtils();
			}
			http.send(HttpMethod.POST, UrlsUtils.urls, params,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							utils.closeDialog();
							Toast.makeText(context, "网络连接超时",Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							utils.closeDialog();
							mListSendHouse.remove(deleteTag);
							if(mListSendHouse.size() == 0){
								showWords((String)getResources().getText(R.string.collection_house));
							}
							adapter.notifyDataSetChanged();
							String result = arg0.result;
							JSONObject josn = null;
							try {
								josn = new JSONObject(result); 
								String statue = josn.getString("status");
								if (!"N".equals(statue)) {
									Toast.makeText(context, "删除成功",Toast.LENGTH_SHORT).show();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		}
	}
}
