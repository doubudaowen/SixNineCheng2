package com.mobile.liujiucheng.main.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {
	/**
	 *点击输入框弹出键盘工具包
	 */
	public static void keyboard(final View edittext) {
		edittext.setFocusable(true);//获取焦点
		edittext.setFocusableInTouchMode(true);
		edittext.requestFocus();// 对指定的输入框请求焦点
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				// 弹出键盘的操作
				InputMethodManager inputManager = (InputMethodManager) edittext
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(edittext, 0);
				/**
				 *隐藏软键盘的方法
				 */
				inputManager.hideSoftInputFromWindow(edittext.getWindowToken() , 0);
				/**
				 * ((InputMethodManager)getSystemService(Context.
				 * INPUT_METHOD_SERVICE
				 * )).hideSoftInputFromWindow(input.getWindowToken(), 0);
				 */
			}
		}, 998);

	}
}
