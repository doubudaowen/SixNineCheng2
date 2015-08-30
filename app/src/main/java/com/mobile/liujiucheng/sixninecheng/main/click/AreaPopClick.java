package com.mobile.liujiucheng.sixninecheng.main.click;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mobile.liujiucheng.main.adapter.MyAdapter;
import com.mobile.liujiucheng.main.bean.ParseMyJSON;
import com.mobile.liujiucheng.main.bean.ParseMyJSON.Address;
import com.mobile.liujiucheng.main.bean.ParseMyJSON.Area;
import com.mobile.liujiucheng.main.utils.AssetManagerUtils;
import com.mobile.liujiucheng.main.utils.DialogUtils;
import com.mobile.liujiucheng.main.utils.GsonUtil;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.NetworkUtils;
import com.mobile.liujiucheng.main.utils.StreamTools;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.sixninecheng.main.R;

public class AreaPopClick {

	private Context context;
	private View mView;
	private ListView mListView;
	
	private ListView mListView02;
	private ListView mListView03;

	private ParseMyJSON bean;
	
	private List<Area> mAList;
	private List<Address> mPlist;                                                                 //位置集合
	private String stream;
	
	private MyAdapter adapter01;
	
	private MyAdapter adapter02;
	
	private PopupWindow mPopupWindowmm;
	/**
	 * 标记来判断是二手房还是出租房
	 * 0代表出租房
	 * 1代表二手房
	 */
	private int mTag = 0;
	
	public PopupWindow getmPopupWindowmm() {
		return mPopupWindowmm;
	}
	/**
	 * 记录区域的各种位置
	 */
	private Map<String,String> mMap  = new HashMap<String, String>();
	
	private Map<String,String> mMapPosition  = new HashMap<String, String>();
	
	public void setMap(Map<String,String> mMapPosition){
		this.mMapPosition = mMapPosition;
	}
	public void setmPopupWindow01(PopupWindow mPopupWindow , int tag) {
		this.mTag = tag;
		this.mPopupWindowmm = mPopupWindow;
		initData();
		praseJson();
		initView();
		//Log.e("TAG", "mPopupWindow="+mPopupWindow);
	}
	public AreaPopClick(View mView, Context context, ListView mListView) {
		this.context = context;
		this.mView = mView;
		this.mListView = mListView;
		util = new DialogUtils(context);
	}
	private void praseJson() {
		bean = GsonUtil.json2Bean(stream, ParseMyJSON.class);
		String name = bean.city.get(0).name;// 北京
		mMap.put("beijing", name);
		mAList = bean.city.get(0).tdistrict;                                                     //城市集合
	}
	private void initData() {
		InputStream is = AssetManagerUtils.getData("address.json", context);
		stream = StreamTools.readStream(is);
	}
	private int mCircle_tag = 0;//商圈的标记
	private int mNeighborhood_tag = 0;//小区的标记
	
	private void initView() {
		mListView02 = (ListView) mView.findViewById(R.id.lv_line);
		mListView03 = (ListView) mView.findViewById(R.id.lv_position);
		if(adapter01 == null){
			adapter01 = new MyAdapter(mAList,context) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					View v;
					ViewHolder holder;
					if (convertView == null) {
						holder = new ViewHolder();
						v = View.inflate(context, R.layout.layout_text_item_area, null);
						holder.mTextView = (TextView) v
								.findViewById(R.id.tv_context);
						v.setTag(holder);
					} else {
						v = convertView;
						holder = (ViewHolder) v.getTag();
					}
					holder.mTextView.setText(mAList.get(position).name);
					if(mCircle_tag == position ){
						holder.mTextView.setTextColor((context.getResources().getColor(R.color.house_item_color)));
					}else{
						holder.mTextView.setTextColor((context.getResources().getColor(R.color.hidden_search_word)));
					}
					return v;
				}
			};
			mListView02.setAdapter(adapter01);
		}else{
			adapter01.notifyDataSetChanged();
		}
		mListView02.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				//点击的不限
				mCircle_tag = position;
				if(position == 0){
					mMap.put("area", null); 
					mMap.put("mPosition", null);
					//Log.e("TAG", "AreaPopClick.this.getmPopupWindowmm() =="+AreaPopClick.this.getmPopupWindowmm());
					if(AreaPopClick.this.getmPopupWindowmm() != null){
						AreaPopClick.this.getmPopupWindowmm().dismiss();
						//TODO访问服务器
						isConnection();
						util.show();
						network();
					}
				}else{
					mMap.put("area", mAList.get(position).name);
				}
				//Log.e("TAG", "mMap=============="+mMap);
				//Log.e("TAG", "mMapPosition=================="+mMapPosition);
				mPlist = mAList.get(position).bussinessareaList;
				//记录区域的值
				if(mPlist == null){
					mPlist = new ArrayList<ParseMyJSON.Address>();
				}
				adapter01.notifyDataSetChanged();                            //必须得刷新
				mListView03.setAdapter(new MyAdapter(mPlist,context) {
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						View v;
						ViewHolder holder;
						if (convertView == null) {
							holder = new ViewHolder();
							v = View.inflate(context, R.layout.layout_text_item_area, null);
							holder.mTextView = (TextView) v
									.findViewById(R.id.tv_context);
							v.setTag(holder);
						} else {
							v = convertView;
							holder = (ViewHolder) v.getTag();
						}
						holder.mTextView.setText(mPlist.get(position).name);
						if(mNeighborhood_tag == position ){
							holder.mTextView.setTextColor((context.getResources().getColor(R.color.house_item_color)));
						}else{
							holder.mTextView.setTextColor((context.getResources().getColor(R.color.hidden_search_word)));
						}
						return v;
					}
				});
				//商圈
				mListView03.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent,
							View view, int position, long id) {
						mNeighborhood_tag = position;
						if(position == 0){
							mMap.put("mPosition", null);
						}else{
							mMap.put("mPosition", mPlist.get(position).name);
						}
						//Log.e("TAG", "mMap=================="+mMap);
						//Log.e("TAG", "mMapPosition=================="+mMapPosition);
						if(AreaPopClick.this.getmPopupWindowmm() != null){
							AreaPopClick.this.getmPopupWindowmm().dismiss();
							isConnection();                               //??????????????????????????????????
							util.show();
							network();
							return;
						}
					}
				});
			}
		});//
		
		/*mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 0){
					if(adapter01 == null){
						adapter01 = new MyAdapter(mAList,context) {
							@Override
							public View getView(int position, View convertView, ViewGroup parent) {
								View v;
								ViewHolder holder;
								if (convertView == null) {
									holder = new ViewHolder();
									v = View.inflate(context, R.layout.layout_text_item, null);
									holder.mTextView = (TextView) v
											.findViewById(R.id.tv_context);
									v.setTag(holder);
								} else {
									v = convertView;
									holder = (ViewHolder) v.getTag();
								}
								holder.mTextView.setText(mAList.get(position).name);
								return v;
							}
						};
						mListView02.setAdapter(adapter01);
					}else{
						adapter01.notifyDataSetChanged();
					}
					//区域
					mListView02.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							//点击的不限
							if(position == 0){
								mMap.put("area", null); 
								mMap.put("mPosition", null);
								//Log.e("TAG", "AreaPopClick.this.getmPopupWindowmm() =="+AreaPopClick.this.getmPopupWindowmm());
								if(AreaPopClick.this.getmPopupWindowmm() != null){
									AreaPopClick.this.getmPopupWindowmm().dismiss();
									//TODO访问服务器
									isConnection();
									util.show();
									network();
								}
							}else{
								mMap.put("area", mAList.get(position).name);
							}
							//Log.e("TAG", "mMap=============="+mMap);
							//Log.e("TAG", "mMapPosition=================="+mMapPosition);
							mPlist = mAList.get(position).bussinessareaList;
							//记录区域的值
							if(mPlist == null){
								mPlist = new ArrayList<PraseMyJSON.Address>();
							}
							mListView03.setAdapter(new MyAdapter(mPlist,context) {
								@Override
								public View getView(int position, View convertView, ViewGroup parent) {
									View v;
									ViewHolder holder;
									if (convertView == null) {
										holder = new ViewHolder();
										v = View.inflate(context, R.layout.layout_text_item, null);
										holder.mTextView = (TextView) v
												.findViewById(R.id.tv_context);
										v.setTag(holder);
									} else {
										v = convertView;
										holder = (ViewHolder) v.getTag();
									}
									holder.mTextView.setText(mPlist.get(position).name);
									return v;
								}
							});
							//商圈
							mListView03.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									if(position == 0){
										mMap.put("mPosition", null);
									}else{
										mMap.put("mPosition", mPlist.get(position).name);
									}
									//Log.e("TAG", "mMap=================="+mMap);
									//Log.e("TAG", "mMapPosition=================="+mMapPosition);
									if(AreaPopClick.this.getmPopupWindowmm() != null){
										AreaPopClick.this.getmPopupWindowmm().dismiss();
										isConnection();
										util.show();
										network();
										return;
									}
								}
							});
						}
					});////
				}
			}
		});*/
	}
	/**
	 * 访问网络
	 */
	private HttpUtils http;
	private DialogUtils util;
	private String mResult = "";
	private void network() {
		RequestParams params = getHeader();
		http = new HttpUtils();
		//mMapPosition
		params.addBodyParameter("district", mMap.get("area"));
		params.addBodyParameter("bussinessarea", mMap.get("mPosition"));
		params.addBodyParameter("houseType", mMapPosition.get("mClass"));
		params.addBodyParameter("mRent", mMapPosition.get("mPrice"));
		
		http.send(HttpMethod.POST, UrlsUtils.urls, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(context, StreamTools.getString(), Toast.LENGTH_SHORT).show();
						util.closeDialog();
						sucessAndFail.isSuccessAndFail(0);
					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						mResult = arg0.result;
						util.closeDialog();
						sucessAndFail.isSuccessAndFail(1);
					}
				});
	}
	private RequestParams getHeader() {
		NetHead head = new NetHead(context);
		RequestParams params = head.setHeader();
		params.addBodyParameter("city","北京");
		if(mTag == 0){
			params.addBodyParameter("type","findHouse");
		}else{
			params.addBodyParameter("type","findSecondHouse");
		}
		return params;
	}
	/**
	 * 回调接口
	 * @author pc
	 */
	public interface RequestSucessAndFail{
		public void isSuccessAndFail(int tag);
	}
	private RequestSucessAndFail sucessAndFail;
	
	public void setRequestSucessAndFail(RequestSucessAndFail sucessAndFail){
		this.sucessAndFail = sucessAndFail;
	}
	/**
	 * 判断网络的连接情况
	 */
	private void isConnection() {
		int type = NetworkUtils.getConnctedType(context);
		if(type == -1){
			Toast.makeText(context, context.getResources().getText(R.string.net_connection),Toast.LENGTH_SHORT).show();
			return;
		}
	}
	static class ViewHolder {
		TextView mTextView;
	} 
	/**
	 * 暴露一个方法返回map集合
	 * @return
	 */
	public Map<String,String> getMap(){
		return mMap;
	}
	
	public String mResquestData(){
		return mResult;
	}
}
