package com.mobile.liujiucheng.sixninecheng.main;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mobile.liujiucheng.main.utils.DialogUtils;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.UrlsUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 我的名字
 * @author pc
 *
 */
public class MyNameActivity extends BaseActivity implements OnClickListener {

	private TextView mTextView;//修改昵称
	private ImageView mImageView;
	private View mView;
	private Context context;
	
	private TextView tv_cancal;
	private TextView tv_confirm;
	private TextView mName;
	private EditText et_modify;
	private AlertDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setting_name);
		initView();
	}
	private void initView() {
		context = this;
		mView = View.inflate(context, R.layout.layout_dialog_modify, null);
		mImageView = (ImageView) findViewById(R.id.iv_exit);
		mTextView = (TextView) findViewById(R.id.tv_modification);
		mName = (TextView) findViewById(R.id.tv_name);
		if(!TextUtils.isEmpty(SPUtils.getName(context))){
			mName.setText(SPUtils.getName(context));
		}
		mImageView.setOnClickListener(this);
		mTextView.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tv_confirm:
			//TODO
			String name = et_modify.getText().toString().trim();
			if(!TextUtils.isEmpty(name)){
				closeDialog();
				network(name);
			}else{
				Toast.makeText(context, this.getResources().getText(R.string.surrounding_content), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_cancal:
			closeDialog();
			break;
		case R.id.iv_exit:
			finish();
			break;
		case R.id.tv_modification:
			//修改
			AlertDialog.Builder builder = new Builder(this);
			if (dialog == null) {
				dialog = builder.create();
			}
			dialog.setView(mView, 0, 0, 0, 0);
			tv_cancal = (TextView) mView.findViewById(R.id.tv_cancal);
			tv_confirm = (TextView) mView.findViewById(R.id.tv_confirm);
			et_modify = (EditText) mView.findViewById(R.id.et_modify);
			
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
	private HttpUtils http;
	/**
	 * 修改名称
	 * @param name
	 */
	private void network(final String name) {
		
		NetHead head = new NetHead(context);
		RequestParams params = head.setHeader();
		params.addBodyParameter("sid", SPUtils.getUserID(context));
		params.addBodyParameter("name", name);
		final DialogUtils dialogUtils = new DialogUtils(context);
		dialogUtils.show();
		if (http == null) {
			http = new HttpUtils();
		}
		http.send(HttpMethod.POST, UrlsUtils.urlModifyName, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dialogUtils.closeDialog();
						Toast.makeText(context, "网络连接超时", Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						dialogUtils.closeDialog();
						Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
						mName.setText(name);
						SPUtils.dataName(name);
					}
				});
	}
	private void closeDialog() {
		if(dialog != null){
			dialog.dismiss();
			dialog = null;
		}
	}
}
