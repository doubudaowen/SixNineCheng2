package com.mobile.liujiucheng.sixninecheng.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
/**
 * 二手房的详情界面
 * @author pc
 */
public class SecondDetailActivity extends Activity{
	
	private ViewPager mViewPager;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_hand_details);
		initView();
	}
	/**
	 * 初始化控件
	 */
	private void initView() {
		context = this;
		mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
		if(adapter == null){
			adapter = new Madapter();
			mViewPager.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	private Madapter adapter;
	private class Madapter extends PagerAdapter{
		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1 ;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(context, R.layout.layout_imageview_item, null);
			ImageView image = (ImageView) view.findViewById(R.id.iv_lunbo_image);
			image.setImageResource(R.drawable.default_image);
			container.addView(view);
			return view;
		}
	}
}
