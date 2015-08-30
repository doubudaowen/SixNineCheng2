package com.mobile.liujiucheng.sixninecheng.main;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.liujiucheng.main.fragment.CollectionFragment;
import com.mobile.liujiucheng.main.fragment.HomeFragment;
import com.mobile.liujiucheng.main.fragment.HomeFragment.CloseSliding;
import com.mobile.liujiucheng.main.fragment.PersonalFragment;
import com.mobile.liujiucheng.main.service.ListenNetStateService;
import com.mobile.liujiucheng.main.slidingmenu.lib.SlidingMenu;
import com.mobile.liujiucheng.main.slidingmenu.lib.app.SlidingFragmentActivity;
import com.mobile.liujiucheng.main.utils.NetworkUtils;
import com.mobile.liujiucheng.main.utils.StreamTools;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.main.utils.VersionUtils;
import com.mobile.liujiucheng.sixninecheng.main.click.SlidingClickEvent;

public class MainActivity extends SlidingFragmentActivity implements OnClickListener {

	private LinearLayout mTabHome;
	private LinearLayout mTabCollection;
	private LinearLayout mTabPerson;

	private ImageButton mImgHome;
	private ImageButton mImgCollection;
	private ImageButton mImgPerson;

	private Fragment mHome;
	private Fragment mCollection;
	private Fragment mPerson;

	private Context mContext;
	private SlidingMenu slidingMenu;

	private View mView;

	private ImageView mImageView;

	private SharedPreferences sp;

	private TextView tv_one;
	private TextView tv_two;
	private TextView tv_three;

	private int choose = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.main_side_bar);
		newVersion();
		initData();
		initView();
		initEvent();
		setSelect(choose);
	}
	/**
	 * 判断是否有新版本
	 */
	private void newVersion() {
		mContext = this;
		mVersion = VersionUtils.getVersionName(this);
		if(NetworkUtils.isNetWork(mContext)){
			//网络存在
			new UpdateAsyncTask().execute(UrlsUtils.Versionupdate);
		}
	}

	class UpdateAsyncTask extends AsyncTask<String , Void, String>{
		@Override
		protected String doInBackground(String... params) {
			String Url = params[0];
			//访问网络
			return getDataCode(Url);
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(mVersion.equals(result)){
				//不升级
			}else{
				//升级
				upDataDiaLog();
			}
		}
	}
	public void upDataDiaLog() {
		Intent intent = new Intent(mContext, VersionUpdateActivity.class);
		intent.putExtra("mAPPurl", mAPPurl);
		Log.i("TAG", "mAPPurl == " + mAPPurl);
		startActivity(intent);
	}
	/**
	 * 获取版本号
	 * @param url
	 * @param
	 * @return
	 */
	public String getDataCode(String url) {
		try {
			URL urls = new URL(url);
			HttpURLConnection openConnection = (HttpURLConnection) urls.openConnection();
			openConnection.setReadTimeout(3000);
			openConnection.setConnectTimeout(3000);
			openConnection.setRequestMethod("POST");
			openConnection.setDoOutput(true);// 是否输入参数
			StringBuffer params = new StringBuffer();
			params.append("type=getLatestVersion");
			byte[] bypes = params.toString().getBytes("UTF-8");
			openConnection.getOutputStream().write(bypes);// 输入参数
			int code = openConnection.getResponseCode();
			if(code == 200){
				InputStream inputStream = openConnection.getInputStream();
				String stream = StreamTools.readStream(inputStream);
				//Log.e("TAG", "stream == " + stream);
				if(!TextUtils.isEmpty(stream)){
					JSONObject jsonObject = new JSONObject(stream);
					JSONObject object = jsonObject.getJSONObject("data");
					String string = object.getString("versionCode");
					mAPPurl = object.getString("versionUrl");
					return string;
				}
			}else{
				//Log.e("TAG", "SHIBAI" + mAPPurl);
				return null;
			}
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		return null;
	}
	private void initData() {
		Intent intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
		Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据
		if(bundle != null){
			String sid=bundle.getString("sid");//getString()返回指定key的值
			choose = Integer.parseInt(sid);
		}
	}
	/**
	 * 默认显示首页
	 */
	private void setSelect(int i) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		hideFragment(transaction);
		switch (i) {
		case 0:
			if (mHome == null) {
				mHome = new HomeFragment();
				transaction.add(R.id.fl_main_home, mHome,"HOME");
			} else {
				transaction.show(mHome);
			}
			((HomeFragment) mHome).setCloseSliding(closeSliding);
			mImgHome.setImageResource(R.drawable.home_house_click);
			mTabHome.setBackgroundResource(R.drawable.home_sliding_block);
			tv_one.setTextColor(Color.parseColor("#43b022"));
			break;
		case 1:
			if (mCollection == null) {
				mCollection = new CollectionFragment();
				transaction.add(R.id.fl_main_home, mCollection);
			} else {
				transaction.show(mCollection);
			}
			tv_two.setTextColor(Color.parseColor("#43b022"));
			mImgCollection.setImageResource(R.drawable.home_message_click);
			mTabCollection.setBackgroundResource(R.drawable.home_sliding_block);
			break;
		case 2:
			if (mPerson == null) {
				mPerson = new PersonalFragment();
				transaction.add(R.id.fl_main_home, mPerson);
			} else {
				transaction.show(mPerson);
			}
			tv_three.setTextColor(Color.parseColor("#43b022"));
			mImgPerson.setImageResource(R.drawable.home_personal_click);
			mTabPerson.setBackgroundResource(R.drawable.home_sliding_block);
			break;
		}
		// 提交事物
		transaction.commit();
	}
	/**
	 * 将显示出来的界面，进行隐藏(不隐藏的话，会出现，界面重叠)
	 * @param transaction
	 */
	private void hideFragment(FragmentTransaction transaction) {

		if (mHome != null) {
			transaction.hide(mHome);
		}
		if (mCollection != null) {
			transaction.hide(mCollection);
		}
		if (mPerson != null) {
			transaction.hide(mPerson);
		}
	}

	CloseSliding closeSliding = new CloseSliding() {
		@Override
		public void close() {
			slidingMenu.toggle();
		}
	};
	/**
	 * 初始化事件
	 */
	private void initEvent() {
		//开启服务,监听网路的情况
		Intent intent = new Intent(mContext, ListenNetStateService.class);
		startService(intent);
		mTabHome.setOnClickListener(this);
		mTabCollection.setOnClickListener(this);
		mTabPerson.setOnClickListener(this);
	}
	/**
	 * 初始化数据
	 */
	private void initView() {
		if(sp == null){
			sp = this.getSharedPreferences("load", MODE_PRIVATE);
		}
		mView = View.inflate(getApplicationContext(), R.layout.main_side_bar, null);
		SlidingClickEvent clickEvent = new SlidingClickEvent(this, mView);
		slidingMenu = getSlidingMenu();
		slidingMenu.setMode(SlidingMenu.LEFT);
		// 设置侧拉栏目宽的大小
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);

		slidingMenu.setShadowDrawable(R.drawable.shadow);
		// 设置边线的大小
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		//slidingMenu.setMenu(R .layout .menu_frame );
		//当左右都可以侧滑的时候，slidingMenu.setSecondaryMenu是增加右边的侧滑栏的布局
		//slidingMenu.setSecondaryMenu (R .layout .menu_frame_one);// 设置菜单内容
		// 设置滑动模式
		slidingMenu.setTouchModeAbove(SlidingMenu.LEFT);

		mContext = this;

		mTabHome = (LinearLayout) findViewById(R.id.id_tab_weixin);
		mTabCollection = (LinearLayout) findViewById(R.id.id_tab_frd);
		mTabPerson = (LinearLayout) findViewById(R.id.id_tab_settings);

		mImgHome = (ImageButton) findViewById(R.id.id_tab_weixin_img);
		mImgCollection = (ImageButton) findViewById(R.id.id_tab_frd_img);
		mImgPerson = (ImageButton) findViewById(R.id.id_tab_settings_img);

		tv_one = (TextView) findViewById(R.id.tv_one);
		tv_two = (TextView) findViewById(R.id.tv_two);
		tv_three = (TextView) findViewById(R.id.tv_three);

	}

	public void onClick(View v) {
		resetImgs();
		int id = v.getId();
		switch (id) {
		// 首页
		case R.id.id_tab_weixin:
			setSelect(0);
			break;
		// 收藏页面
		case R.id.id_tab_frd:
			//判断有没有登录
			setSelect(1);
			break;
		// 个人页面
		case R.id.id_tab_settings:
			setSelect(2);
			break;
		}
	}
	/**
	 * 重置颜色
	 */
	private void resetImgs() {

		tv_one.setTextColor(Color.parseColor("#FFFFFF"));
		tv_two.setTextColor(Color.parseColor("#FFFFFF"));
		tv_three.setTextColor(Color.parseColor("#FFFFFF"));
		mImgHome.setImageResource(R.drawable.home_house);
		mImgCollection.setImageResource(R.drawable.home_message);
		mImgPerson.setImageResource(R.drawable.home_personal);

		mTabHome.setBackgroundColor(Color.parseColor("#434343"));
		mTabCollection.setBackgroundColor(Color.parseColor("#434343"));
		mTabPerson.setBackgroundColor(Color.parseColor("#434343"));
	}
	//处理返回键的效果
	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
	        if((System.currentTimeMillis()-exitTime) > 2000){
	            Toast.makeText(mContext, "再按一次退出程序", Toast.LENGTH_SHORT).show();
	            exitTime = System.currentTimeMillis();
	        } else {
	            finish();
	            System.exit(0);
	        }
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public void finish() {
		super.finish();
		delete();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(values != null){
			values.mValues();
		}
	}
	/**
	 * 回调接口，登录成功后显示登录数据
	 * @author Administrator
	 */
	public interface PersonalValues{
		public void mValues();
	}
	public void setPersonalValues(PersonalValues values){
		this.values = values;
	}
	private PersonalValues values;
	private String mVersion;
	private String mAPPurl = null;
	private void delete(){
		Editor edit = sp.edit();
		if(TextUtils.isEmpty(sp.getString("keepPassword", null))){
			edit.putString("phone", null);
			edit.commit();
		}
	}
}
