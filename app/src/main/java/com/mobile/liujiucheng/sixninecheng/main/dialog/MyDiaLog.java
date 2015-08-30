package com.mobile.liujiucheng.sixninecheng.main.dialog;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.mobile.liujiucheng.sixninecheng.main.R;

public class MyDiaLog {

	private ImageView mImageView;
	private Context context;
	private View mView;
	private PopupWindow mPopWindow;
	private AnimationDrawable ad;
	public MyDiaLog(Context context,View mView){
		this.context = context;
		this.mView = mView;
		setPopWindow();
	}
	/**
	 * 开启窗口
	 */
	@SuppressLint("InlinedApi")
	private void setPopWindow() {
		mImageView = (ImageView) mView.findViewById(R.id.loadingImageView);
		ad = (AnimationDrawable) mImageView.getBackground();
		ad.start();
		mPopWindow = new PopupWindow(context);
		mPopWindow.setHeight(LayoutParams.MATCH_PARENT);
		mPopWindow.setWidth(LayoutParams.MATCH_PARENT);
		mPopWindow.setFocusable(true);
		mPopWindow.setOutsideTouchable(true);
		mPopWindow.setContentView(mView);
		mPopWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		mPopWindow.setBackgroundDrawable(dw);
	}
	/**
	 * 关闭窗口
	 */
	public void closePopWindow(){
		if(mPopWindow != null){
			mPopWindow.dismiss();
			mPopWindow = null;
			if(ad != null){
				ad.stop();
				ad = null;
			}
		}
	}
}
