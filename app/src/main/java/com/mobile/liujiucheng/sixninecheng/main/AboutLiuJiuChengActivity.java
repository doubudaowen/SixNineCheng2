package com.mobile.liujiucheng.sixninecheng.main;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * 关于69城
 * @author Administrator
 *
 */
public class AboutLiuJiuChengActivity extends BaseActivity implements OnClickListener {

	private ImageView mImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_about_us);
		initView();
	}
	private void initView() {
		mImageView = (ImageView) findViewById(R.id.iv_exit);
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
