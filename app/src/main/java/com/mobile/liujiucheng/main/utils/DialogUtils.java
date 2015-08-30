package com.mobile.liujiucheng.main.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.View;

import com.mobile.liujiucheng.sixninecheng.main.R;

public class DialogUtils {

	private View mView;
	private Context context;
	private AlertDialog dialog;
	
	public DialogUtils(Context context){
		this.context = context;
		mView = View.inflate(context, R.layout.layout_dialog_item, null);
		createDiaLog();
	}
	public void createDiaLog() {
		AlertDialog.Builder builder = new Builder(context);
		dialog = builder.create();
		dialog.setView(mView, 0, 0, 0, 0);
	}
	public void show(){
		if(dialog != null){
			dialog.show();
		}
	}
	public void closeDialog() {
		if (dialog != null) {
			dialog.hide();
		}
	}
}
