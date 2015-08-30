package com.mobile.liujiucheng.sixninecheng.main;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mobile.liujiucheng.main.bean.RetalbeanDetails;
import com.mobile.liujiucheng.main.bean.RetalbeanDetails.RLData;
import com.mobile.liujiucheng.main.bean.RetalbeanDetails.RLData.Near;
import com.mobile.liujiucheng.main.utils.DialogUtils;
import com.mobile.liujiucheng.main.utils.GsonUtil;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.NetworkUtils;
import com.mobile.liujiucheng.main.utils.StreamTools;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.sixninecheng.main.view.MyScollview;

/**
 * 出租房的详情页
 * @author pc Mr_Gang
 */
public class DetailsActivity extends Activity implements OnClickListener {

	private MyScollview scollview;
	private Context context;
	private DialogUtils dialogUtils;
	private RetalbeanDetails bean;

	private ViewPager viewPager;

	private TextView tv_context;

	private TextView tv_area;// 地区

	private TextView tv_describe;// 详情描述
	
	private TextView tv_price;
	private TextView tv_room;
	private TextView tv_square;// 平方

	private TextView tv_floor;// 高城
	private TextView tv_decorate;// 精装
	private TextView tv_direction;// 朝向

	private ImageView tv_telephone;// 电话号码

	private TextView tv_position; // 小区
	private TextView tv_class_price;// 详情描述

	private GridView gv_home;
	private BitmapUtils bitmapUtils;

	private ImageView mMessage;// 发送短信
	private ImageView mCall;// 拨打电话
	private ImageView mShare;// 分享

	private ImageView mExit;// 退出
	private ImageView mCollection;// 收藏
	private ImageView iv_map;// 地图
	
	private TextView mTelePhone;
    private TextView mTelePhone2;

	private TextView mCallPhone;
	
	private View mView;
	private LinearLayout ll_collection_data;
	private String UrlBaiDuImage = "http://api.map.baidu.com/staticimage?&width=334&height=253&zoom=18";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_item);
		initView();
		Intent intent = getIntent();// getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
		Bundle bundle = intent.getExtras();// .getExtras()得到intent所附带的额外数据
		if (bundle != null) {
			sid = bundle.getString("sid");
			tag = bundle.getString("tag");
			boolean b = isConnection();
			// 访问网络
			if (b) {
				mDialog();
				network(sid);
			}
		}
	}
	private void initView() {
		context = this;
		mCollection = (ImageView) findViewById(R.id.iv_collection);
		ll_collection_data = (LinearLayout) findViewById(R.id.ll_collection_data);
		mView = View.inflate(context, R.layout.layout_dialog, null);
		bitmapUtils = new BitmapUtils(this);
		mExit = (ImageView) findViewById(R.id.iv_exit);
		mMessage = (ImageView) findViewById(R.id.iv_message);
		mCall = (ImageView) findViewById(R.id.iv_call);
		mShare = (ImageView) findViewById(R.id.iv_share);
		iv_map = (ImageView) findViewById(R.id.iv_map);
		mCallPhone = (TextView) mView.findViewById(R.id.tv_make_call);
		mTelePhone = (TextView) findViewById(R.id.tv_my_telphone);
        mTelePhone2 = (TextView) findViewById(R.id.tv_my_telphone2);
		tv_class_price = (TextView) findViewById(R.id.tv_class_price);
		
		iv_map.setOnClickListener(this);
		mMessage.setOnClickListener(this);
		mCall.setOnClickListener(this);
		mShare.setOnClickListener(this);
		mCollection.setOnClickListener(this);
		mExit.setOnClickListener(this);
		ll_collection_data.setOnClickListener(this);

		scollview = (MyScollview) findViewById(R.id.my_scollview);

        tv_context = (TextView) findViewById(R.id.tv_context);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_room = (TextView) findViewById(R.id.tv_room);

		tv_square = (TextView) findViewById(R.id.tv_square);
		tv_floor = (TextView) findViewById(R.id.tv_floor);

		tv_decorate = (TextView) findViewById(R.id.tv_decorate);
		tv_telephone = (ImageView) findViewById(R.id.tv_telephone);

		tv_position = (TextView) findViewById(R.id.tv_position);
		tv_area = (TextView) findViewById(R.id.tv_area);
		tv_direction = (TextView) findViewById(R.id.tv_direction);
		tv_describe = (TextView) findViewById(R.id.tv_describe);

		gv_home = (GridView) findViewById(R.id.gv_home);
		viewPager = (ViewPager) findViewById(R.id.main_view_pager);


	}
	/**
	 * 展示数据
	 */
	private void network(String id) {
		NetHead head = new NetHead(context);
		RequestParams params = head.setHeader();
		if ("1".equals(tag)) {
			params.addBodyParameter("type", "detailsHouse");
		} else {
			params.addBodyParameter("type", "detailsSecondHouse");
		}
		params.addBodyParameter("sid", id);
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, UrlsUtils.urls, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(context, StreamTools.getString(),
								Toast.LENGTH_SHORT).show();
						if (dialogUtils != null) {
							dialogUtils.closeDialog();
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (dialogUtils != null) {
							dialogUtils.closeDialog();
						}
						mResult = arg0.result;
						Log.e("TAGAAAAAAAAAAAAAAA", "mResult==" + mResult);
						parseJson();
					}
				});
	}
	private String mResult = "";
	private String mStatus;
	private RLData mData;

	private MyAdapter mGridAdapter;
	private List<Near> mNear;

	protected void parseJson() {
		if (mNear == null) {
			mNear = new ArrayList<RetalbeanDetails.RLData.Near>();
		}
		bean = GsonUtil.json2Bean(mResult, RetalbeanDetails.class);
		mStatus = bean.status;
		if (mStatus.equals("N")) {
			Toast.makeText(context, "没有更多的数据", Toast.LENGTH_SHORT).show();
		} else {
			if (bean.data.nearby != null) {
				mNear.addAll(bean.data.nearby);
			}
			mData = bean.data;
			Log.e("TAG", "mData--X" + mData.zuobiaoX);
			Log.e("TAG", "mData--Y" + mData.zuobiaoY);
			String mX = mData.zuobiaoX;
			String mY = mData.zuobiaoY;
			tv_context.setText(mData.title);
			if (mData.rent.equals("0")) {
				tv_price.setText("面议");
			} else {
				if("1".equals(tag)){
					tv_price.setText(mData.rent+"元/月");
					tv_class_price.setText("租价");
				}else{
					tv_price.setText(mData.rent+"万");
					tv_class_price.setText("售价");
				}
			}
			tv_room.setText(mData.houseType);
			tv_square.setText(mData.spec+"m²");

			tv_floor.setText(mData.floor);
			tv_decorate.setText(mData.interiordesign);
			tv_direction.setText(mData.aspect);
			// tv_telephone.setText(mData.title);
			tv_position.setText(mData.businessArea);
//            if("null".equals(mData.businessArea)){
                tv_area.setText(mData.city + "-" + mData.district);
//            }else {
//                tv_area.setText(mData.city + "-" + mData.district + "-"
//                        + mData.businessArea);
//            }

			tv_describe.setText(mData.description);
			setGridAdapter();
			phone = mData.linkPhone;
			if (phone.length() > 16 ) {
				mTelePhone.setVisibility(View.GONE);
				bitmapUtils.display(tv_telephone, phone);
			}else{

				mTelePhone.setVisibility(View.VISIBLE);
				tv_telephone.setVisibility(View.GONE);
				mTelePhone.setText(phone);
                mTelePhone2.setText(phone);
			}
			AccessToImages(mX, mY);
		}

        //添加图片
        try {
            JSONObject obj1 = new JSONObject(mResult);
            JSONObject data = obj1.getJSONObject("data");
            JSONObject imageJSON = data.getJSONObject("imageJSON");
            img0 = imageJSON.getString("img0");

            if (adapter == null) {
                adapter = new Madapter();
                viewPager.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String img0;
    private String phone;
	private void setGridAdapter() {
		if (mNear == null) {
			return;
		}
		if (mGridAdapter == null) {
			mGridAdapter = new MyAdapter(mNear, context) {
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View view;
					if (convertView == null) {
						view = View.inflate(context, R.layout.layout_near_area,
								null);
					} else {
						view = convertView;
					}
					ImageView mImage = (ImageView) view
							.findViewById(R.id.iv_image);
					TextView mNearPosition = (TextView) view
							.findViewById(R.id.tv_position);
					TextView mNearPrice = (TextView) view
							.findViewById(R.id.tv_price);
					mNearPosition.setText(mNear.get(position).district + "/ "
							+ mNear.get(position).bussinessarea);
					if ("1".equals(tag)) {
						mNearPrice.setText(mNear.get(position).spec + "m²"
								+ " | " + mNear.get(position).floor + " / "
								+ mNear.get(position).rentFloor + "层" + "  "
								+ mNear.get(position).rent + "元/月");
					} else {
						mNearPrice.setText(mNear.get(position).spec + "m²"
								+ " | " + mNear.get(position).floor + " / "
								+ mNear.get(position).rentFloor + "层" + "  "
								+ mNear.get(position).rent + "万");
					}
					if (bitmapUtils == null) {
						bitmapUtils = new BitmapUtils(context);
					}
					bitmapUtils.display(mImage, mNear.get(position).url);
					return view;
				}
			};
			gv_home.setAdapter(mGridAdapter);
		} else {
			mGridAdapter.notifyDataSetChanged();
		}
	}

	// Y>X
	private void AccessToImages(String mX, String mY) {
		if (TextUtils.isEmpty(mY) || TextUtils.isEmpty(mX)) {
			return;
		}
		String URLS = UrlBaiDuImage + "center=" + mY + "," + mX + ""
				+ "&markers=" + mY + "," + "" + mX + "";
		netWorkImage(URLS);
        //http://api.map.baidu.com/staticimage?&width=334&height=253&zoom=18center=116.340393,39.725306&markers=116.340393,39.725306
	}

	private static final String MRESQUESTWAY = "GET";
	private void netWorkImage(final String uRLS) {
		new Thread() {
			public void run() {
				try {
					URL url = new URL(uRLS);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(3000); // 请求超时时间
					conn.setReadTimeout(3000); // 读取超时时间
					conn.setRequestMethod(MRESQUESTWAY);
					int code = conn.getResponseCode();
					if (code == 200) {
						// 连接服务器成功
						InputStream inputStream = conn.getInputStream();
						final Bitmap bitmap = BitmapFactory
								.decodeStream(inputStream);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								iv_map.setImageBitmap(bitmap);
                                scollview.scrollTo(0,0);     //让scroll滑动到最上部
							}
						});
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),StreamTools.getString(), Toast.LENGTH_SHORT).show();
				}
			};
		}.start();
	}

	private void mDialog() {
		context = this;
		dialogUtils = new DialogUtils(context);
		dialogUtils.show();
	}

	private boolean isConnection() {
		context = this;
		int type = NetworkUtils.getConnctedType(this);
		if (type == -1) {
			Toast.makeText(this,this.getResources().getText(R.string.net_connection),Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	private Madapter adapter;
	private String tag;
	private String sid;
	private AlertDialog dialog;

	private class Madapter extends PagerAdapter {
		@Override
		public int getCount() {
			return 1;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(context, R.layout.layout_imageview_item,
					null);
			ImageView image = (ImageView) view
					.findViewById(R.id.iv_lunbo_image);
            new BitmapUtils(context).display(image,img0);
//			image.setImageResource(R.drawable.default_image);
			container.addView(view);
			return view;
		}
	}

	private TextView tv_cancal;
	private TextView tv_confirm;
	private boolean delete = true;

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tv_cancal:
			if (dialog != null) {
				dialog.dismiss();
			}
			break;
		case R.id.tv_confirm:
			phoneCall();
			break;
		case R.id.ll_collection_data:// 收藏房源
			if (delete) {
				netWorkCollection();
			} else {
				netdelete();
			}
			break;
		case R.id.iv_exit:
			finish();
			break;
		case R.id.iv_map:
			Intent intent = new Intent(context, DetailMapActivity.class);
			if (mData != null) {
				intent.putExtra("latitude", mData.zuobiaoX);
				intent.putExtra("longitude", mData.zuobiaoY);
			}
			startActivity(intent);
			break;
		case R.id.iv_collection:
			// 访问网络
			break;
		case R.id.iv_share:
			sendShare();
			break;
		case R.id.iv_message:
			sendMessage();
			break;
		case R.id.iv_call:
			AlertDialog.Builder builder = new Builder(this);
			if (dialog == null) {
				dialog = builder.create();
			}
			dialog.setView(mView, 0, 0, 0, 0);
			tv_cancal = (TextView) mView.findViewById(R.id.tv_cancal);
			tv_confirm = (TextView) mView.findViewById(R.id.tv_confirm);
			String phone = mTelePhone.getText().toString().trim();
			if(!TextUtils.isEmpty(phone)){
				mCallPhone.setText("是否拨打电话 : "+phone);
			}
			tv_cancal.setOnClickListener(this);
			tv_confirm.setOnClickListener(this);
			dialog.show();
			WindowManager.LayoutParams params = dialog.getWindow()
					.getAttributes();
			params.width = LayoutParams.MATCH_PARENT;
			params.height = LayoutParams.WRAP_CONTENT;
			dialog.getWindow().setAttributes(params);
			break;
		}
	}
	/**
	 * 删除收藏
	 */
	private void netdelete() {
		// 判断是否登录
		String userID = SPUtils.getUserID(context);
		if (TextUtils.isEmpty(userID)) {
			Intent intent = new Intent(context, LoagingActivity.class);
			startActivity(intent);
		} else {
			boolean b = isConnection();
			if (!b) {
				return;
			}
			dialogUtils.show();
			NetHead head = new NetHead(context);
			RequestParams params = head.setHeader();
			if ("2".equals(tag)) {
				params.addBodyParameter("secondId", sid);
			} else {
				params.addBodyParameter("houseId", sid);
			}
			params.addBodyParameter("type", "deleteColect");
			params.addBodyParameter("userId", SPUtils.getUserID(context));
			if (http == null) {
				http = new HttpUtils();
			}
			http.send(HttpMethod.POST, UrlsUtils.urls, params,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							dialogUtils.closeDialog();
							Toast.makeText(context, "网络连接超时",
									Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							delete = true;
							dialogUtils.closeDialog();
							String result = arg0.result;
							JSONObject josn = null;
							try {
								josn = new JSONObject(result);
								String statue = josn.getString("status");
								if (!"N".equals(statue)) {
									Toast.makeText(context, "取消收藏成功",
											Toast.LENGTH_SHORT).show();
									mCollection
											.setImageResource(R.drawable.details_collect);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		}
	}
	private HttpUtils http;
	@Override
	protected void onStart() {
		super.onStart();
		// 判断用户登录过后是不是收藏了该房源
	}
	/**
	 * 收藏
	 */
	private void netWorkCollection() {
		// 判断是否登录
		String userID = SPUtils.getUserID(context);
		if (TextUtils.isEmpty(userID)) {
			Intent intent = new Intent(context, LoagingActivity.class);
			startActivity(intent);
		} else {
			boolean b = isConnection();
			if (!b) {
				return;
			}
			dialogUtils.show();
			NetHead head = new NetHead(context);
			RequestParams params = head.setHeader();
			if ("2".equals(tag)) {
				params.addBodyParameter("type", "collectSecond");
				params.addBodyParameter("secondId", sid);
			} else {
				params.addBodyParameter("type", "collectHouse");
				params.addBodyParameter("houseId", sid);
			}
			params.addBodyParameter("userId", SPUtils.getUserID(context));
			if (http == null) {
				http = new HttpUtils();
			}
			http.send(HttpMethod.POST, UrlsUtils.urls, params,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							dialogUtils.closeDialog();
							Toast.makeText(context, StreamTools.getString(),Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							delete = false;
							dialogUtils.closeDialog();
							String result = arg0.result;
							JSONObject josn = null;
							try {
								josn = new JSONObject(result);
								String statue = josn.getString("status");
								if ("N".equals(statue)) {
									mCollection
											.setImageResource(R.drawable.details_collect_link);
									Toast.makeText(context, "此房源已经收藏",
											Toast.LENGTH_SHORT).show();
								} else {
									mCollection
											.setImageResource(R.drawable.details_collect_link);
									Toast.makeText(context, "收藏成功",
											Toast.LENGTH_SHORT).show();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		}
	}

	private void sendShare() {
		// 分享代码
		Intent intent = new Intent("android.intent.action.SEND");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
        if(tag.equals("1")){
            intent.putExtra(Intent.EXTRA_TEXT, "http://www.69cheng.com/houseSearch/"+sid+"/thouseInfo");
        }
        if(tag.equals("2")) {
            intent.putExtra(Intent.EXTRA_TEXT, "http://www.69cheng.com/secondSearch/"+sid+"/secondInfo");
        }
		startActivity(intent);
	}

	protected void phoneCall() {
		String phone = mTelePhone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			phone = "01057490090";
		}
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ phone));
		this.startActivity(intent);
	}

	//01057490090
	private void sendMessage() {
		Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
		sendIntent.setData(Uri.parse("smsto:" +phone));
		if (mData != null) {
			sendIntent.putExtra("sms_body","您好，我从六九城网站上看到您在"+mData.city+"-"+mData.district+"有房子出售，请问您的房子卖出去了吗？我方便看一下吗？");
		}
		context.startActivity(sendIntent);
	}
}
