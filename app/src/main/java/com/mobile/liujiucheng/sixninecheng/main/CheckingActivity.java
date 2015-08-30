package com.mobile.liujiucheng.sixninecheng.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.liujiucheng.main.fragment.AppointmentFragment;
import com.mobile.liujiucheng.main.fragment.NoAppointmentFragment;

public class CheckingActivity extends FragmentActivity implements OnClickListener {

	private FrameLayout mLayout;
	private TextView mTextSuccess;
	private TextView mTextFail;
	private ImageView mImageView;
	
	private ImageView mLink;
	private ImageView mLinkTwo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_checking_trip);
		initView();
	}
	private void initView() {
		mLink = (ImageView) findViewById(R.id.iv_link);
		mLinkTwo = (ImageView) findViewById(R.id.iv_link_two);
		
		mImageView = (ImageView) findViewById(R.id.iv_exit);
		mLayout = (FrameLayout) findViewById(R.id.fl_layout_content);
		mTextSuccess = (TextView) findViewById(R.id.tv_success);
		mTextFail = (TextView) findViewById(R.id.tv_fails);
		mTextSuccess.setOnClickListener(this);
		mTextFail.setOnClickListener(this);
		mImageView.setOnClickListener(this);
		chooseLayout(0);
	}
	private void chooseLayout(int index) {
		if(adapter != null){
			Fragment fragment = (Fragment) adapter.instantiateItem(mLayout, index);
			//2,替换操作
			adapter.setPrimaryItem(mLayout, 0, fragment);
			//3,提交
			adapter.finishUpdate(mLayout);
		}
	}
	FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()){
		@Override
		public int getCount() {
			return 2;
		}
		@Override
		public Fragment getItem(int arg0) {
			Fragment fragment = null;
			switch (arg0) {
			case 0:
				fragment = new AppointmentFragment();
				break;
			case 1:
				fragment = new NoAppointmentFragment();
				break;
			}
			return fragment;
		}
	};
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tv_success:
			defaultColor();
			mTextSuccess.setTextColor(Color.parseColor("#66c6f2"));
			mLink.setImageResource(R.drawable.slide_link);
			mLinkTwo.setImageResource(R.drawable.details_horizontal_line);
			chooseLayout(0);
			break;
		case R.id.tv_fails:
			defaultColor();
			mTextFail.setTextColor(Color.parseColor("#66c6f2"));
			mLink.setImageResource(R.drawable.details_horizontal_line);
			mLinkTwo.setImageResource(R.drawable.slide_link);
			chooseLayout(1);
			break;
		case R.id.iv_exit:
			finish();
			break;
		}
	}
	private void defaultColor() {
		mTextSuccess.setTextColor(Color.parseColor("#000000"));
		mTextFail.setTextColor(Color.parseColor("#000000"));
	}
}
