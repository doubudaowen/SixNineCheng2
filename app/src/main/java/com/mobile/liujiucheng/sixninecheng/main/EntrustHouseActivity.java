package com.mobile.liujiucheng.sixninecheng.main;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mobile.liujiucheng.main.fragment.EntrustFragment;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.PopupWindowUtils;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
/**
 * 委托房源
 * @author pc
 */
public class EntrustHouseActivity extends FragmentActivity implements OnClickListener {

	private FrameLayout mLayout;
	private TextView mTextSuccess;
	private TextView mTextFail;
	private ImageView mImageView;
	private Button mButton;
	private Map<String,String> map;
	private Map<String,String> mapData;
	private Context context;
	
	private EditText et_name;//姓名
	private EditText et_way;//电话号码
	private EditText et_position;//地区
	private EditText et_num;//
	private EditText et_num_area;//面积
	
	private ImageView mLink;
	private ImageView mLinkTwo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_entrust_person);
		initView();
	}
	private void initView() {
		map = new HashMap<String, String>();
		context = this;
		if(mapData == null){
			mapData = new HashMap<String, String>();
		}
		mLink = (ImageView) findViewById(R.id.iv_link);
		mLinkTwo = (ImageView) findViewById(R.id.iv_link_two);
		
		et_name = (EditText) findViewById(R.id.et_name);
		et_way = (EditText) findViewById(R.id.et_way);
		et_position = (EditText) findViewById(R.id.et_position);
		et_num = (EditText)findViewById(R.id.et_num);
		et_num_area = (EditText)findViewById(R.id.et_num_area);
		
		mButton = (Button) findViewById(R.id.bu_landing);
		mImageView = (ImageView) findViewById(R.id.iv_exit);
		mImageView.setOnClickListener(this);
		//mLayout = (FrameLayout) findViewById(R.id.fl_layout_content);
		mTextSuccess = (TextView) findViewById(R.id.tv_success);
		mTextFail = (TextView) findViewById(R.id.tv_fails);
		mTextSuccess.setOnClickListener(this);
		mButton.setOnClickListener(this);
		mTextFail.setOnClickListener(this);
		//chooseLayout(0);
		map.put("two", "2");
	}
	private void chooseLayout(int index) {
		Fragment fragment = (Fragment) adapter.instantiateItem(mLayout, index);
		//2,替换操作
		adapter.setPrimaryItem(mLayout, 0, fragment);
		//3,提交
		adapter.finishUpdate(mLayout);
	}
	private Fragment fragment = null;
	FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()){
		@Override
		public int getCount() {
			return 2;
		}
		@Override
		public Fragment getItem(int arg0) {
			switch (arg0) {
			case 0:
				fragment = new EntrustFragment();
				break;
			case 1:
				fragment = new EntrustFragment();
				break;
			}
			return fragment;
		}
	};
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tv_success://二手房
			defaultColor();
			map = new HashMap<String, String>();
			map.put("two", "2");
			mTextSuccess.setTextColor(Color.parseColor("#66c6f2"));
			mLink.setImageResource(R.drawable.slide_link);
			mLinkTwo.setImageResource(R.drawable.details_horizontal_line);
			//chooseLayout(0);
			break;
		case R.id.tv_fails://租房
			defaultColor();
			map = new HashMap<String, String>();
			map.put("two", "1");
			mTextFail.setTextColor(Color.parseColor("#66c6f2"));
			mLink.setImageResource(R.drawable.details_horizontal_line);
			mLinkTwo.setImageResource(R.drawable.slide_link);
			//chooseLayout(1);
			break;
		case R.id.iv_exit:
			finish();
			break;
		case R.id.bu_landing:
			boolean b = getData();
			if(b){
				netWork();
			}
			break;
		}
	}
	private HttpUtils http;
	private void netWork() {
		NetHead head = new NetHead(context);
		RequestParams params = head.setHeader();
		params.addBodyParameter("type","authorize");
		Log.e("TAG", "=========="+mapData.get("name"));
		params.addBodyParameter("linkMan",mapData.get("name"));
		params.addBodyParameter("mobilePhone",mapData.get("way"));
		params.addBodyParameter("xiaoqu",mapData.get("position"));
		params.addBodyParameter("houseType",mapData.get("num"));
		params.addBodyParameter("spec",mapData.get("area"));
		params.addBodyParameter("houseT",map.get("two"));
		Log.e("TAG", "=====two====="+map.get("two"));
		if(http == null){
			http = new HttpUtils();
		}
		http.send(HttpMethod.POST, UrlsUtils.urls, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				PopupWindowUtils.closePop();
				Toast.makeText(getApplicationContext(), "网络不给力", Toast.LENGTH_SHORT).show();
				Log.e("TAG", "解析失败");
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				PopupWindowUtils.closePop();
				Toast.makeText(getApplicationContext(), "上传成功,等待审核", Toast.LENGTH_LONG).show();
				finish();
			}
		});
	}
	private void defaultColor() {
		mTextSuccess.setTextColor(Color.parseColor("#000000"));
		mTextFail.setTextColor(Color.parseColor("#000000"));
	}
	public boolean getData(){
		String name = et_name.getText().toString().trim();
		String null1 = judgeNull(name,"联系人不能为空");
		if(null1 == null){
			return false;
		}
		String way = et_way.getText().toString().trim();
		String null2 = judgeNull(way,"联系方式不能为空");
		if(null2 == null){
			return false;
		}
		String position = et_position.getText().toString().trim();
		String null3 = judgeNull(position,"小区不能为空");
		if(null3 == null){
			return false;
		}
		String num = et_num.getText().toString().trim();
		String null4 = judgeNull(num,"户型不能为空");
		if(null4 == null){
			return false;
		}
		String area = et_num_area.getText().toString().trim();
		String null5 = judgeNull(area,"面积不能为空");
		if(null5 == null){
			return false;
		}
		Log.e("TAG", "name=="+name);
		Log.e("TAG", "way=="+way);
		Log.e("TAG", "position=="+position);
		Log.e("TAG", "num=="+num);
		Log.e("TAG", "area=="+area);
		
		mapData.put("name", name);
		mapData.put("way", way);
		mapData.put("position", position);
		mapData.put("num", num);
		mapData.put("area", area);
		
		return true;
	}
	private String judgeNull(String data, String descriptor) {
		if(TextUtils.isEmpty(data)){
			Toast.makeText(this, descriptor, Toast.LENGTH_SHORT).show();
			return null;
		}
		return "name";
	}
}
