package com.mobile.liujiucheng.sixninecheng.main;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mobile.liujiucheng.main.utils.NetworkUtils;
import com.mobile.liujiucheng.sixninecheng.main.view.NumberProgressBar;
import com.mobile.liujiucheng.sixninecheng.main.view.OnProgressBarListener;

/**
 * 升级版本
 * @author pc leigang 2015年6月12日16:17:11
 */
public class BeingUpdatedActivity extends BaseActivity implements OnClickListener ,OnProgressBarListener{

	@ViewInject(R.id.tv_cancel)
	private TextView mCancel;
	@ViewInject(R.id.numberbar)
	private NumberProgressBar progressBar;
	private String mAPPurl;
	private File file;
	private static final String NETWORKCONNECTIONFAIL = "请检查你的网络";
	private static final String UPDATEAPK = "SixNineCheng.apk";

	private static final int NARROWSACLE = 10000;
	private static final int  PERCENTTAGE= 100;
	@SuppressWarnings("rawtypes")
	private HttpHandler handler;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_updata_dialog);
		ViewUtils.inject(this);
		getView();
		getData();
	}
	private void getView() {
		mCancel.setOnClickListener(this);
		progressBar.setOnProgressBarListener(this);
	}
	private void getData() {
		Intent intent = getIntent();
		mAPPurl = intent.getStringExtra("mAPPurl");
		if (!TextUtils.isEmpty(mAPPurl)) {
			//判断网络情况
			if(NetworkUtils.isNetWork(this)){
				//网络存在
				//new UpdateAsyncTask().execute(UrlsUtils.Versionupdate);
				downloadNewApk();
			}else{
				Toast.makeText(this, NETWORKCONNECTIONFAIL, Toast.LENGTH_SHORT).show();
			}
		}
	}
	private void downloadNewApk() {
		HttpUtils httpUtils = new HttpUtils();
		File path = isSDCard();
		file = new File(path, UPDATEAPK);
		handler = httpUtils.download(mAPPurl ,file.getAbsolutePath(),
				new RequestCallBack<File>() {
					// 下载成功的时候
					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						Log.e("TAG", arg0.result.getAbsolutePath());
						finish();
						installAPk();
					}
					@Override
					public void onStart() {
						super.onStart();
					}
					// 下载失败的时候调用
					@Override
					public void onFailure(HttpException e, String arg1) {
						Toast.makeText(mContext, getResources().getString(R.string.networkfails_connection), Toast.LENGTH_SHORT).show();
						finish();
						e.printStackTrace();
					}
					// 下载的进度
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						super.onLoading(total, current, isUploading);
						int mCurrent = (int) ((current/NARROWSACLE) * PERCENTTAGE/(total/NARROWSACLE));
						progressBar.setProgress(mCurrent);
					}
				});
	}
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
	}
	/**
	 * 安装APP
	 */
	protected void installAPk() {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(Uri.fromFile(new File(file.getAbsolutePath())),
				"application/vnd.android.package-archive");
		startActivityForResult(intent, 0);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	private File isSDCard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			// 有存储的SDCard
			File file = Environment.getExternalStorageDirectory();
			return file;
		} else {
			File dir = this.getFilesDir();
			return dir;
		}
	}
	@Override
	public void onClick(View v) {
		if(handler != null){
			handler.cancel(false);//取消下载任务
		}
		finish();
	}
	@Override
	public void onProgressChange(int current, int max) {
		if(current == max) {
			//Toast.makeText(this, "下载完成", Toast.LENGTH_LONG).show();
			finish();
        }
	}
}
