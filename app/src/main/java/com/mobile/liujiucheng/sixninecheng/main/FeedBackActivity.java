package com.mobile.liujiucheng.sixninecheng.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.liujiucheng.main.utils.PopupWindowUtils;
/**
 * 意见反馈
 * @author pc
 */
public class FeedBackActivity extends BaseActivity implements OnClickListener {

	private ImageView mImageView;
	private TextView tv_send;
	private EditText et_content;
	private Context context;
	
	private LinearLayout mRemove;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			PopupWindowUtils.closePop();
			Toast.makeText(context, "反馈成功", Toast.LENGTH_SHORT).show();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_opinion_feedback);
		initView();
	}
	private void initView() {
		context = this;
		mImageView = (ImageView) findViewById(R.id.iv_exit);
		tv_send = (TextView) findViewById(R.id.tv_send);
		et_content =  (EditText) findViewById(R.id.et_content);
		mRemove = (LinearLayout) findViewById(R.id.ll_remove);
		mImageView.setOnClickListener(this);
		mRemove.setOnClickListener(this);
		tv_send.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.iv_exit:
			finish();
			break;
		case R.id.ll_remove:
			if(!TextUtils.isEmpty(et_content.getText().toString().trim())){
				et_content.setText("");
			}
			break;
		case R.id.tv_send:
			String phone = SPUtils.getPhone(context);
			if(TextUtils.isEmpty(phone)){
				Intent intent = new Intent(context, LoagingActivity.class);
				startActivity(intent);
			}else{
				String trim = et_content.getText().toString().trim();
				if(!TextUtils.isEmpty(trim)){
					if(trim.length() > 10){
						Toast.makeText(context, "内容不能少于10字", Toast.LENGTH_SHORT).show();
					}
					else{
						PopupWindowUtils.loading(v, context);
						handler.sendEmptyMessageDelayed(1, 1000);
					}
				}else{
					Toast.makeText(context, "请输入内容", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}
}
