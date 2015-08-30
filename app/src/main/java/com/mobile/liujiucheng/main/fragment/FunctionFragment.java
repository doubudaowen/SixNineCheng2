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
import com.mobile.liujiucheng.main.bean.base.BaseFragment;
import com.mobile.liujiucheng.main.fragment.NewCenterFragment.ViewHolder;
import com.mobile.liujiucheng.main.utils.DialogUtils;
import com.mobile.liujiucheng.main.utils.GsonUtil;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.NetworkUtils;
import com.mobile.liujiucheng.main.utils.StreamTools;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.main.widget.PullToRefreshBase.OnRefreshListener;
import com.mobile.liujiucheng.main.widget.PullToRefreshListView;
import com.mobile.liujiucheng.sixninecheng.main.DetailsActivity;
import com.mobile.liujiucheng.sixninecheng.main.LoagingActivity;
import com.mobile.liujiucheng.sixninecheng.main.R;
import com.mobile.liujiucheng.sixninecheng.main.SPUtils;
/**
 * 收藏出租房
 * @author pc Mr_Gang
 *
 */
public class FunctionFragment extends BaseFragment implements OnClickListener {
	
	private View mView;
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private MyAdapter adapter;
	private Retalbean bean;// 出租房的Javabean
	private List<BData> mListBean;
	private BitmapUtils bitmapUtils;
	private TextView mTextView;
	
	private Context context;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = View.inflate(getActivity(), R.layout.layout_my_collection_second,
				null);
		return mView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		network();
	}
	private void showWords(String data) {
		mTextView.setText(data);
		mTextView.setVisibility(View.GONE);
	}
	private HttpUtils http;
	private String mResult;
	private DialogUtils utils;
	private String status;
	private void network() {
		boolean connected =NetworkUtils.isConnection(getActivity());
		if (connected) {
			// 访问网络
			utils = new DialogUtils(getActivity());
			utils.show();
			NetHead head = new NetHead(getActivity());
			RequestParams params = head.setHeader();
			params.addBodyParameter("type", "queryCollect");
			params.addBodyParameter("userId", SPUtils.getUserID(getActivity()));
			params.addBodyParameter("houseT", "1");
			if (http == null) {
				http = new HttpUtils();
			}
			http.send(HttpMethod.POST, UrlsUtils.urls, params,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							Toast.makeText(getActivity(), StreamTools.getString(),
									Toast.LENGTH_SHORT).show();
							utils.closeDialog();
							colseHeard();
						}
						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							colseHeard();
							mResult = arg0.result;
							utils.closeDialog();
							praseJson();
						}
					});
		} else {
			//Toast.makeText(getActivity(), "当前网络不可用,请你检查网络的情况", Toast.LENGTH_SHORT).show();
			showWords("当前网络不可用,请你检查网络的情况");
		}
	}
	OnRefreshListener mOnrefreshListener = new OnRefreshListener() {
		public void onRefresh() {
			// 访问网络数据
			network();
		}
	};
	protected void praseJson() {
		bean = GsonUtil.json2Bean(mResult, Retalbean.class);
		status = bean.status;
		if("N".equals(status)){
			//Toast.makeText(getActivity(), "你还没有收藏相关的房源", Toast.LENGTH_SHORT).show();
			showWords((String)getResources().getText(R.string.collection_house));
		}else{
			mListBean.addAll(bean.data);
			setListAdapter();
		}
	}
	private void setListAdapter() {
		if(adapter == null){
				adapter = new MyAdapter(mListBean, getActivity()) {
					@Override
					public View getView(int position, View convertView,
							ViewGroup parent) {
						View view;
						ViewHolder holder;
						if (convertView == null) {
							holder = new ViewHolder();
							view = View.inflate(getActivity(),
									R.layout.collection_item, null);
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
						holder.mDate.setText(mListBean.get(position).pubTume);
						holder.mTitle.setText(mListBean.get(position).title);

						holder.mHouse.setText("面积:"
								+ mListBean.get(position).spec + "/m² - 户型 - "
								+ mListBean.get(position).houseType + "");

						if (mListBean.get(position).rent.equals("0")) {
							holder.mPrice.setText("面议");
						} else {
							holder.mPrice.setText(""
									+ mListBean.get(position).rent + "元/月");
						}
						holder.mPosition.setText(""
								+ mListBean.get(position).city + " - "
								+ mListBean.get(position).district + " - "
								+ mListBean.get(position).bussinessarea + "");
						bitmapUtils.display(holder.mImage,
								mListBean.get(position).imgUrl);
						return view;
					}
				};
			mListView.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), DetailsActivity.class);
				// 传递数据
				intent.putExtra("sid", mListBean.get(position - 1).sid);
				intent.putExtra("tag", "1");
				getActivity().startActivity(intent);
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
	private int deleteTag = 0;
	protected void colseHeard() {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}
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
	
	protected void deletePopwindow() {
		if(popupWindow != null && popupWindow.isShowing()){
			popupWindow.dismiss();
		}
	}
	private TextView mDelete;//删除房源
	private TextView mCancle;//取消删除
	private void initData() {
		context = getActivity();
		bitmapUtils = new BitmapUtils(getActivity());
		if (mListBean == null) {
			mListBean = new ArrayList<Retalbean.BData>();
		}
		deleteView = View.inflate(context, R.layout.layout_delete_house, null);
		mDelete = (TextView) deleteView.findViewById(R.id.tv_delete);
		mCancle = (TextView) deleteView.findViewById(R.id.tv_cancal);
		mDelete.setOnClickListener(this);
		mCancle.setOnClickListener(this);
		mTextView = (TextView) mView.findViewById(R.id.tv_collection);
		mPullRefreshListView = (PullToRefreshListView) mView
				.findViewById(R.id.lv_content);
		mListView = mPullRefreshListView.getRefreshableView();
	}
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
			params.addBodyParameter("houseId", mListBean.get(deleteTag).sid);
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
							Toast.makeText(context, StreamTools.getString(),Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							utils.closeDialog();
							mListBean.remove(deleteTag);
							if(mListBean.size() == 0){
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
