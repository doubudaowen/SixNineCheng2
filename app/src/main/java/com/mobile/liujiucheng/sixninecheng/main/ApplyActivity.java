package com.mobile.liujiucheng.sixninecheng.main;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mobile.liujiucheng.main.utils.DialogUtils;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
/**
 * 合作申请
 * @author Administrator
 *
 */
public class ApplyActivity extends BaseActivity implements OnClickListener {

	private ImageView mImageView;
	private EditText et_area;
	private EditText et_company_name;
	private EditText et_company_addrss;
	private EditText et_company_tele;
	private EditText et_name;
	private EditText et_tele;
	private EditText et_card;
	private Button bu_loading;
	private Context context;
	private String area;
	private String name;
	private String address;
	private String tele;
	private String mName;
	private String mTele;
	private String card;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_application);
		initView();
	}
	private void initView() {
		
		context = this;
		bu_loading = (Button) findViewById(R.id.bu_loading);
		mImageView = (ImageView) findViewById(R.id.iv_exit);
		mImageView.setOnClickListener(this);
		bu_loading.setOnClickListener(this);
		
		et_area = (EditText) findViewById(R.id.et_area);
		et_company_name = (EditText) findViewById(R.id.et_company_name);
		et_company_addrss = (EditText) findViewById(R.id.et_company_addrss);
		et_company_tele = (EditText) findViewById(R.id.et_company_tele);
		
		et_name = (EditText) findViewById(R.id.et_name);
		et_tele = (EditText) findViewById(R.id.et_tele);
		et_card = (EditText) findViewById(R.id.et_card);
	}
	/**
	 * 监听EditText内容的变化
	 */
	private TextWatcher watcher = new TextWatcher() {
	    
	    @Override
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
	    }
	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
	            int after) {
	        // TODO Auto-generated method stub
	        
	    }
	    
	    @Override
	    public void afterTextChanged(Editable s) {
	        // TODO Auto-generated method stub
	        
	    }
	};
	private static final String MAREA = "区域不能为空";
	private static final String MNAME = "公司名字不能为空";
	private static final String MADDRESS = "公司地址不能为空";
	private static final String MTELE = "公司的电话不能为空";
	private static final String MMNAME = "姓名不能为空";
	
	private static final String MMTELE = "电话号码不能为空";
	private static final String MCADRE = "身份证号码不能为空";
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.iv_exit:
			finish();
			break;
		case R.id.bu_loading:
			area = et_area.getText().toString().trim();
			String null1 = judgeNull(area,MAREA);
			if(null1 == null){
				return;
			}
			name = et_company_name.getText().toString().trim();
			String null2 = judgeNull(name,MNAME);
			if(null2 == null){
				return;
			}
			address = et_company_addrss.getText().toString().trim();
			String null3 = judgeNull(address,MADDRESS);
			if(null3 == null){
				return;
			}
			tele = et_company_tele.getText().toString().trim();
			String null4 = judgeNull(tele,MTELE);
			if(null4 == null){
				return;
			}
			mName = et_name.getText().toString().trim();
			String null5 = judgeNull(mName,MMNAME);
			if(null5 == null){
				return;
			}
			mTele = et_tele.getText().toString().trim();
			String null6 = judgeNull(mTele,MMTELE);
			if(null6 == null){
				return;
			}
			card = et_card.getText().toString().trim();
			String null7 = judgeNull(card,MCADRE);
			if(null7 == null){
				return;
			}
			//访问网络
			netWork();
			break;
		}
	}
	private HttpUtils http;
	/**
	 * 访问网络
	 */
	private void netWork() {
		NetHead head = new NetHead(context);
		RequestParams params = head.setHeader();
		params.addBodyParameter("type","cooperation");
		params.addBodyParameter("firmName",name);
		params.addBodyParameter("district",area);
		
		params.addBodyParameter("address",address);
		params.addBodyParameter("phone",tele);
		params.addBodyParameter("idCard",card);
		
		params.addBodyParameter("mobilePhone",mTele);
		params.addBodyParameter("name",mName);
		
		if(http == null){
			http = new HttpUtils();
		}
		final DialogUtils dialogUtils = new DialogUtils(context);
		dialogUtils.createDiaLog();
		http.send(HttpMethod.POST, UrlsUtils.urls, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				dialogUtils.closeDialog();
				Toast.makeText(getApplicationContext(), NETWORKFAIL, Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				dialogUtils.closeDialog();
				Toast.makeText(getApplicationContext(), MSUCCESS, Toast.LENGTH_LONG).show();
				finish();
			}
		});
	}
	private static final String MSUCCESS = "上传成功,等待审核";
	private static final String NETWORKFAIL = "网络不给力";
	private String judgeNull(String data ,String descriptor){
		if(TextUtils.isEmpty(data)){
			Toast.makeText(context, descriptor, Toast.LENGTH_SHORT).show();
			return null;
		}
		return "name";
	}
}
