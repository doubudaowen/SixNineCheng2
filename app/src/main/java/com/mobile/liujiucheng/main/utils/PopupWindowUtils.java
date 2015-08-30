package com.mobile.liujiucheng.main.utils;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.mobile.liujiucheng.sixninecheng.main.R;

public class PopupWindowUtils {

	private static PopupWindow popupWindow;
	
	@SuppressLint("InlinedApi")
	public static void loading(View view,Context context){
		View mView = View.inflate(context, R.layout.layout_image_progress, null);
		popupWindow = new PopupWindow(context);
		popupWindow.setContentView(mView);
		popupWindow.setHeight(LayoutParams.MATCH_PARENT);
		popupWindow.setWidth(LayoutParams.MATCH_PARENT);
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		popupWindow.setOutsideTouchable(false);
	}
	public static void closePop(){
		if(popupWindow != null){
			popupWindow.dismiss();
			popupWindow = null;
		}
	}
	public static PopupWindow isNull(){
		return popupWindow;
	}
}
