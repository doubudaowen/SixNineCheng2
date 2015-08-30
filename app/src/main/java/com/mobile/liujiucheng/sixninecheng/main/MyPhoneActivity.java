package com.mobile.liujiucheng.sixninecheng.main;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 我的电话号码
 * @author pc
 *
 */
public class MyPhoneActivity extends BaseActivity implements OnClickListener {

	private ImageView mImageView;
	private TextView mTextView;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setting_telephone);
		initView();
	}
	private void initView() {
		context = this;
		mImageView = (ImageView) findViewById(R.id.iv_exit);
		mTextView =  (TextView) findViewById(R.id.tv_myphone);
		if(!TextUtils.isEmpty(SPUtils.getPhone(context))){
			mTextView.setText(SPUtils.getPhone(context));
		}
		mImageView.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.iv_exit:
			finish();
			break;
		}
	}
}
