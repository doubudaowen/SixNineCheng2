package com.mobile.liujiucheng.sixninecheng.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 版本升级
 * 
 * @author pc
 * 
 */
public class VersionUpdateActivity extends BaseActivity implements
		OnClickListener {

	@ViewInject(R.id.tv_confirm)
	private TextView mConfirm;
	@ViewInject(R.id.tv_cancal)
	private TextView mCancel;
	private Context mContext;
	private String mAPPurl;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_updata_activity);
		ViewUtils.inject(this);
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		android.view.WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
		//p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的1.0
		p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.8
		p.alpha = 1.0f; // 设置本身透明度
		//p.dimAmount = 0.5f; // 设置黑暗度
		getWindow().setAttributes(p);
		getData();
		initListenterEvent();
	}
	private void getData() {
		Intent intent = getIntent();
		mAPPurl = intent.getStringExtra("mAPPurl");
	}
	private void initListenterEvent() {
		mContext = this;
		mConfirm.setOnClickListener(this);
		mCancel.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tv_cancal:// 取消升级
			finish();
			break;
		case R.id.tv_confirm:// 升级
			updataDialog();
			break;
		}
	}
	private void updataDialog() {
		Intent intent = new Intent(mContext, BeingUpdatedActivity.class);
		intent.putExtra("mAPPurl", mAPPurl);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
	}
}
