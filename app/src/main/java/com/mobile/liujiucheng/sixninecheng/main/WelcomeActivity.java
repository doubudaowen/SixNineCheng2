package com.mobile.liujiucheng.sixninecheng.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.mobile.liujiucheng.sixninecheng.application.MyApplication;

/**
 * 引导界面
 * @author Administrator LeiGang
 * 
 */
public class WelcomeActivity extends Activity {

	private MyApplication app;
	private ViewPager mViewPager;
	private Myadapter adapter;
	private Context context;
	private Button mButton;
	private  SharedPreferences mSp; 
	
	private int[] ResID = { R.drawable.home_start_one,
			R.drawable.home_start_two, R.drawable.home_start_three};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_welcome);
		initView();
	}

	private void initView() {
		context = this;
		app = (MyApplication) getApplication();
		mSp = app.getSp();
		mButton = (Button) findViewById(R.id.bu_start);
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//记录
				Intent intent = new Intent(context, MainActivity.class);
				context.startActivity(intent);
				Editor edit = mSp.edit();
				edit.putBoolean("is_frist", false);
				edit.commit();
				finish();
			}
		});
		mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
		if(adapter == null){
			adapter = new Myadapter();
			mViewPager.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if(position == ResID.length - 1){
					mButton.setVisibility(View.VISIBLE);
				}else{
					mButton.setVisibility(View.GONE);
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
	}
	private class Myadapter extends PagerAdapter {

		@Override
		public int getCount() {
			return ResID.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(context, R.layout.layout_imageview_welcome, null);
			RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.rl_welcome);
			layout.setBackgroundResource(ResID[position]);
			container.addView(view);
			return view;
		}
	}
}
