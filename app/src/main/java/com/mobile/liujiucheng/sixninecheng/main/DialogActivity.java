package com.mobile.liujiucheng.sixninecheng.main;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class DialogActivity extends Activity {
	private ImageView mImageView;
	private AnimationDrawable ad;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dialog_item);
		initView();
	}

	private void initView() {
		mImageView = (ImageView)findViewById(R.id.loadingImageView);
		ad = (AnimationDrawable) mImageView.getBackground();
		ad.start();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(ad != null){
			ad.stop();
			ad = null;
		}
	}
}
