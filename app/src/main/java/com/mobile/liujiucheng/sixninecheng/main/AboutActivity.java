package com.mobile.liujiucheng.sixninecheng.main;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.liujiucheng.main.adapter.MyAdapter;
import com.mobile.liujiucheng.main.utils.NetworkUtils;
import com.mobile.liujiucheng.main.utils.StreamTools;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.main.utils.VersionUtils;
/**
 * 关于我们
 * @author pc Mr_Gang
 * 
 */
public class AboutActivity extends Activity implements OnClickListener {

	private ListView mListView;
	private List<String> mList;
	private Context context;
	private MyAdapter mAdapter;
	private ImageView mImageView;
	private static final String data = "当前是最新版本";
	private static final String mData = "暂无相关数据";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sliding_about);
		initView();
	}

	private void initView() {
		context = this;
		mListView = (ListView) findViewById(R.id.my_person);
		mImageView = (ImageView) findViewById(R.id.iv_return);
		mImageView.setOnClickListener(this);
		initData();
	}
	private void initData() {
		mList = new ArrayList<String>();
		mList.add("检查更新");
		mList.add("使用须知");
		mList.add("给我们好评");
		mList.add("推荐六九城");
		mList.add("关于六九城");
		mList.add("意见反馈");
		mList.add("联系我们");
		setMyAdapter();
	}
	private void setMyAdapter() {
		if (mAdapter == null) {
			mAdapter = new MyAdapter(mList, context) {
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View view;
					View mView;
					if (convertView == null) {
						view = View.inflate(context,
								R.layout.layout_listview_item, null);
					} else {
						view = convertView;
					}
					mView = view.findViewById(R.id.view_line);
					TextView tv = (TextView) view.findViewById(R.id.tv_item);
					tv.setText(mList.get(position));
					if (position == 0 || position == 3) {
						mView.setVisibility(View.VISIBLE);
					} else {
						mView.setVisibility(View.GONE);
					}
					return view;
				}
			};
			mListView.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					newVersion();
					break;
				case 1:
					newStart(UseInformationActivity.class);
					break;
				case 2:
					Uri uri = Uri.parse("market://details?id="+ getPackageName());
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					break;
				case 3:
					Toast.makeText(context, mData, Toast.LENGTH_SHORT).show();
					break;
				case 4:// 关于69城
					newStart(AboutLiuJiuChengActivity.class);
					break;
				case 5:// 意见反馈
					newStart(FeedBackActivity.class);
					break;
				case 6:// 联系我们
					newStart(ContactActivity.class);
					break;
				}
			}
		});
	}
	private Context mContext;
	private String mVersion;
	private String mAPPurl = null;
	protected void newVersion() {
		mContext = this;
		mVersion = VersionUtils.getVersionName(this);
		if(NetworkUtils.isNetWork(mContext)){
			//网络存在
			new UpdateAsyncTask().execute(UrlsUtils.Versionupdate);
		}else{
			Toast.makeText(mContext, StreamTools.getString(), Toast.LENGTH_SHORT).show();
		}
	}
	class UpdateAsyncTask extends AsyncTask<String , Void, String>{
		@Override
		protected String doInBackground(String... params) {
			String Url = params[0];
			//访问网络
			return getDataCode(Url);
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(mVersion.equals(result)){
				Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
			}else{
				//升级
				upDataDiaLog();
			}
		}
	}
	
	/**
	 * 开启一个新的界面
	 * @param clazz
	 */
	private void newStart(@SuppressWarnings("rawtypes") Class clazz) {
		Intent intent = new Intent(context, clazz);
		startActivity(intent);
	}

	public void upDataDiaLog() {
		Intent intent = new Intent(mContext, VersionUpdateActivity.class);
		intent.putExtra("mAPPurl", mAPPurl);
		Log.i("TAG", "mAPPurl == " + mAPPurl);
		startActivity(intent);
	}

	public String getDataCode(String url) {
		try {
			URL urls = new URL(url);
			HttpURLConnection openConnection = (HttpURLConnection) urls.openConnection();
			openConnection.setReadTimeout(3000);
			openConnection.setConnectTimeout(3000);
			openConnection.setRequestMethod("POST");
			openConnection.setDoOutput(true);// 是否输入参数
			StringBuffer params = new StringBuffer();
			params.append("type=getLatestVersion");
			byte[] bypes = params.toString().getBytes("UTF-8");
			openConnection.getOutputStream().write(bypes);// 输入参数
			int code = openConnection.getResponseCode();
			if(code == 200){
				InputStream inputStream = openConnection.getInputStream();
				String stream = StreamTools.readStream(inputStream);
				//Log.e("TAG", "stream == " + stream);
				if(!TextUtils.isEmpty(stream)){
					JSONObject jsonObject = new JSONObject(stream);
					JSONObject object = jsonObject.getJSONObject("data");
					String string = object.getString("versionCode");
					mAPPurl = object.getString("versionUrl");
					return string;
				}
			}else{
				//Log.e("TAG", "SHIBAI" + mAPPurl);
				return null;
			}
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.iv_return:
			finish();
			break;
		}
	}
}
