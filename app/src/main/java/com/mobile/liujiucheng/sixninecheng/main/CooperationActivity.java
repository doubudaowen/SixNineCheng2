package com.mobile.liujiucheng.sixninecheng.main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mobile.liujiucheng.main.utils.DialogUtils;
import com.mobile.liujiucheng.main.utils.StreamTools;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
/**
 * 合作申请
 * @author pc Mr_Gang
 *
 */
public class CooperationActivity extends Activity implements OnClickListener {
	
	private ImageView mImageView;
	private EditText et_company_name;//公司名称
	private EditText et_business;//主营区域
	private EditText et_numbe_person;//人员数量
	
	private EditText et_company_addrss;//公司地址
	private EditText et_company_tele;//手机号码
	
	private EditText et_name;//负责人
	
	//四张图片
	private ImageView mOne;
	private ImageView mTwo;
	private ImageView mThree;
	//private ImageView ;
	
	private Button mButton;
	private Context context;
	
	
	private TextView tv_cooperation;//保洁
	private TextView tv_move;//搬家
	private TextView tv_maintenance;//维修
	private TextView tv_intermediary;//房产中介
	
	private TextView mIntermediary;//房产中介主页面的切换
	private TextView tv_cooperation_teger;//合作须知
	
	private TextView tv_house_medium;//
	
	private ImageView iv_show_close;
	
	private LinearLayout mLinearLayout;
	
	private ScrollView my_scrollview;
	private ScrollView home_scrollview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_application);
		initView();
	}
	private void initView() {
		context = this;
		if(map == null){
			map = new HashMap<String, String>();
		}
		if(mAddress ==null){
			mAddress = new HashMap<String, String>();
		}
		
		my_scrollview = (ScrollView) findViewById(R.id.my_scrollview);
		home_scrollview = (ScrollView) findViewById(R.id.home_scrollview);
		
		tv_house_medium = (TextView) findViewById(R.id.tv_house_medium);
		tv_cooperation_teger = (TextView) findViewById(R.id.tv_cooperation_teger);
		mIntermediary = (TextView) findViewById(R.id.tv_my_application);//合作申请
		
		tv_cooperation = (TextView) findViewById(R.id.tv_cooperation);
		tv_move = (TextView) findViewById(R.id.tv_move);
		tv_maintenance = (TextView) findViewById(R.id.tv_maintenance);
		tv_intermediary = (TextView) findViewById(R.id.tv_intermediary);
		
		iv_show_close = (ImageView) findViewById(R.id.iv_show_close);
		bitmapUtils = new BitmapUtils(context);
		et_company_name = (EditText) findViewById(R.id.et_company_name);
		et_business = (EditText) findViewById(R.id.et_business);
		et_numbe_person = (EditText) findViewById(R.id.et_numbe_person);
		et_company_addrss = (EditText) findViewById(R.id.et_company_addrss);
		et_company_tele = (EditText) findViewById(R.id.et_company_tele);
		et_name = (EditText) findViewById(R.id.et_name);
		
		mOne = (ImageView) findViewById(R.id.iv_one);
		mTwo = (ImageView) findViewById(R.id.iv_two);
		mThree = (ImageView) findViewById(R.id.iv_three);
		
		//mFour = (ImageView) findViewById(R.id.iv_four);
		
		mLinearLayout = (LinearLayout) findViewById(R.id.ll_choose_show);
		
		mLinearLayout.setOnClickListener(this);
		
		tv_cooperation_teger.setOnClickListener(this);
		mIntermediary.setOnClickListener(this);
		
		tv_cooperation.setOnClickListener(this);
		tv_move.setOnClickListener(this);
		tv_maintenance.setOnClickListener(this);
		tv_intermediary.setOnClickListener(this);
		
		mOne.setOnClickListener(this);
		mTwo.setOnClickListener(this);
		mThree.setOnClickListener(this);
		//mFour.setOnClickListener(this);
		iv_show_close.setOnClickListener(this);
		
		mImageView = (ImageView) findViewById(R.id.iv_exit);
		mButton = (Button) findViewById(R.id.bu_loading);
		mButton.setOnClickListener(this);
		mImageView.setOnClickListener(this);
	}
	/**
	 * 判断字段是不是为空
	 * @param data
	 * @param descriptor
	 * @return
	 */
	private String judgeNull(String data ,String descriptor){
		if(TextUtils.isEmpty(data)){
			Toast.makeText(context, descriptor, Toast.LENGTH_SHORT).show();
			return null;
		}
		return "name";
	}
	private void mClose(){
		mLinearLayout.setVisibility(View.GONE);
		b = true;
	}
	boolean b = true;
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.iv_show_close:
			if(b){
				mLinearLayout.setVisibility(View.VISIBLE);
				b = false;
			}else{
				mLinearLayout.setVisibility(View.GONE);
				b = true;
			}
			break;
		case R.id.tv_cooperation://业务类型
			tv_house_medium.setText(tv_cooperation.getText().toString());
			mClose();
			break;
		case R.id.tv_move:
			tv_house_medium.setText(tv_move.getText().toString());
			mClose();
			break;
		case R.id.tv_maintenance:
			tv_house_medium.setText(tv_maintenance.getText().toString());
			mClose();
			break;
		case R.id.tv_intermediary:
			tv_house_medium.setText(tv_intermediary.getText().toString());
			mClose();
			break;
		case R.id.iv_exit:
			finish();
			break;
		case R.id.tv_cooperation_teger://合作须知
			defaultColor();
			home_scrollview.setVisibility(View.GONE);
			my_scrollview.setVisibility(View.VISIBLE);
			tv_cooperation_teger.setTextColor(getResources().getColor(R.color.application_color_text_choose));
			break;
		case R.id.tv_my_application://合作申请
			defaultColor();
			my_scrollview.setVisibility(View.GONE);
			home_scrollview.setVisibility(View.VISIBLE);
			mIntermediary.setTextColor(getResources().getColor(R.color.application_color_text_choose));
			break;
		case R.id.iv_one://营业执照
			getImage("100");
			break;
		case R.id.iv_two://身份证的正面
			getImage("200");
			break;
		case R.id.iv_three://身份证的背面
			getImage("300");
			break;
		/*case R.id.iv_four://法人照片
			getImage("400");
			break;*/
		case R.id.bu_loading:
			boolean b = mWhetherEmpty();
			if(b){
				if(mOne.getDrawable() == null  || mTwo.getDrawable() == null || mThree.getDrawable() == null){
					Toast.makeText(context, "图片不能为空", Toast.LENGTH_SHORT).show();
				}else{
					//访问网络
					network();
				}
			}
			break;
		}
	}
	private void defaultColor() {
		// TODO Auto-generated method stub
		tv_cooperation_teger.setTextColor(Color.parseColor("#999999"));
		mIntermediary.setTextColor(Color.parseColor("#999999"));
	}
	private Map<String,String> map;
	private void getImage(String resquestCode) {
		map.put("resquestCode", resquestCode);
		Intent i = new Intent(
				Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}
	private Map<String,String> mAddress;
	private static int RESULT_LOAD_IMAGE = 1;
	//回显图片
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			if(cursor != null){
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				picturePath = cursor.getString(columnIndex);
				//Log.e("TAG", "picturePath=="+picturePath);
				cursor.close();
			}
		}
		/**
		 * case R.id.iv_one://营业执照
			getImage("100");
			break;
		case R.id.iv_two://身份证的正面
			getImage("200");
			break;
		case R.id.iv_three://身份证的背面
			getImage("300");
			break;
		case R.id.iv_four://法人照片
			getImage("400");
			break;
		 */
		if(map.get("resquestCode").equals("100")){
			//mOne.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			bitmapUtils.display(mOne, picturePath);
			mAddress.put("one",picturePath );
		}
		if(map.get("resquestCode").equals("200")){
			//mOne.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			bitmapUtils.display(mTwo, picturePath);
			mAddress.put("two",picturePath );
		}
		if(map.get("resquestCode").equals("300")){
			//mOne.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			bitmapUtils.display(mThree, picturePath);
			mAddress.put("three",picturePath );
		}
		if(map.get("resquestCode").equals("400")){
			//mOne.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			//bitmapUtils.display(mFour, picturePath);
			mAddress.put("four",picturePath );
		}
	}
	private HttpUtils http;
	private String picturePath;
	private BitmapUtils bitmapUtils;
	private String company_name;
	private String business;
	private String numbe_person;
	private String company_addrss;
	private String company_tele;
	private String name;
	/**
	 * 发布到服务器
	 */
	private void network() {
		final DialogUtils dialogUtils = new DialogUtils(context);
		dialogUtils.createDiaLog();
		dialogUtils.show();
		RequestParams params = new RequestParams();
		
		if(!TextUtils.isEmpty(mAddress.get("one"))){
			params.addBodyParameter("licenseImage", new File(mAddress.get("one")));
		}
		
		if(!TextUtils.isEmpty(mAddress.get("two"))){
			params.addBodyParameter("cardFront",  new File(mAddress.get("two")));
		}
		
		if(!TextUtils.isEmpty(mAddress.get("three"))){
			params.addBodyParameter("cardBack", new File(mAddress.get("three")));
		}
		if(!TextUtils.isEmpty(mAddress.get("four"))){
			params.addBodyParameter("legalPersonPhotos", new File(mAddress.get("four")));
		}
		params.addBodyParameter("companyName", company_name);
		params.addBodyParameter("mainArea", business);
		params.addBodyParameter("personNumber", numbe_person);
		
		params.addBodyParameter("companyAddrss", company_addrss);
		params.addBodyParameter("phoneNumber", company_tele);
		params.addBodyParameter("contactPerson", name);
		params.addBodyParameter("serviceType", tv_house_medium.getText().toString().trim());
		
		if(http == null){
			http = new HttpUtils();
		}
		String url = UrlsUtils.urlApplication;
		http.send(HttpMethod.POST,url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				dialogUtils.closeDialog();
				Toast.makeText(getApplicationContext(), StreamTools.getString(), Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				dialogUtils.closeDialog();
				Toast.makeText(getApplicationContext(), "申请成功,等待审核", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
	private boolean mWhetherEmpty() {
		company_name = et_company_name.getText().toString().trim();
		String null1 = judgeNull(company_name, "公司的名称不能为空");
		if(null1 == null){
			return false;
		}
		business = et_business.getText().toString().trim();
		String null2 = judgeNull(business, "主营区域不能为空");
		if(null2 == null){
			return false;
		}
		numbe_person = et_numbe_person.getText().toString().trim();
		String null3 = judgeNull(numbe_person, "人员数量不能为空");
		if(null3 == null){
			return false;
		}
		company_addrss = et_company_addrss.getText().toString().trim();
		 String null4 = judgeNull(company_addrss, "公司地址不能为空");
		if (null4 == null) {
				return false;
			}
		company_tele = et_company_tele.getText().toString().trim();
		 String null5 = judgeNull(company_tele, "手机号码不能为空");
		 if(null5 == null){
			 return false;
		 }
		name = et_name.getText().toString().trim();
		String null6= judgeNull(name, "负责人不能为空");
		if(null6 == null){
			return false;
		}
		return true;
	}
}
