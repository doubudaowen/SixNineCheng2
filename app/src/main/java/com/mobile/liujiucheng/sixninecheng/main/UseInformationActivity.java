package com.mobile.liujiucheng.sixninecheng.main;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class UseInformationActivity extends BaseActivity {

	private ImageView mExit,iv_00;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_use_the_information);
		initView();
	}
	private void initView() {
		mExit = (ImageView) findViewById(R.id.iv_exit);
        iv_00 = (ImageView) findViewById(R.id.iv_00);
		mExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
