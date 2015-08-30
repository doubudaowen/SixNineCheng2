package com.mobile.liujiucheng.sixninecheng.main.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class MyScollview extends ScrollView {

	public MyScollview(Context context) {
		super(context);
		initData();
	}

	@SuppressWarnings("deprecation")
	private void initData() {
		gestureDetector = new GestureDetector(new Yscroll());
		setFadingEdgeLength(0);
	}

	// 添加了一个手势选择器
	GestureDetector gestureDetector;
	View.OnTouchListener onTouchListener;

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev)
				&& gestureDetector.onTouchEvent(ev);
	}

	public MyScollview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData();
	}

	public MyScollview(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData();
	}

	class Yscroll extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// 控制手指滑动的距离
			if (Math.abs(distanceY) >= Math.abs(distanceX)) {
				return true;
			}
			return false;
		}
	}
}
