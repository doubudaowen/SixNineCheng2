package com.mobile.liujiucheng.sixninecheng.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.liujiucheng.main.adapter.MyAdapter;
/**
 * 搜索的是出租房
 * @author pc
 */
public class CommonSearchActivity extends BaseActivity implements
		OnClickListener {
	
	private String mTag = "2";
	private Context context;
	private EditText et_search;
	private TextView tv_cancal;
	private static final int RESULT_CODE = 200;
	private MyAdapter mAdapter;
	
	private ListView mHistory;//历史记录
	private List<String> mList;
	
	private SharedPreferences mSp;
	private View mViewFooter;
	private TextView  mDelete;
	private ImageView mNosearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_secondary);
		initData();
		initView();
	}
	private void initView() {
		mViewFooter = View.inflate(context, R.layout.layout_listview_delete, null);
		mDelete = (TextView) mViewFooter.findViewById(R.id.tv_delete);
		mHistory = (ListView) findViewById(R.id.lv_history);
		mNosearch = (ImageView) findViewById(R.id.iv_no_search);
		if(mSp == null){
			mSp = this.getSharedPreferences("history", MODE_PRIVATE);
		}
		if(mList == null){
			mList = new ArrayList<String>();
		}
		int size = mSp.getInt("mList", 0);
		for(int x = 0 ; x<size; x++){
			mList.add(mSp.getString(x+"", ""));
		}
		if(mList.size() >0){
			setAdapter();
		}
		tv_cancal = (TextView) findViewById(R.id.tv_cancal);
		et_search = (EditText) findViewById(R.id.et_search);
		tv_cancal.setOnClickListener(this);
		mDelete.setOnClickListener(this);
		et_search.setOnKeyListener(onKeyListener);
	}
	/**
	 * 监听软件盘，右下角的回车键
	 */
	private int record = 0;
	private int one = -1;
	private OnKeyListener onKeyListener = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_ENTER
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				/* 隐藏软键盘 */
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (inputMethodManager.isActive()) {
					inputMethodManager.hideSoftInputFromWindow(
							v.getApplicationWindowToken(), 0);
				}
				String mKeyWord = et_search.getText().toString().trim();
				if (!TextUtils.isEmpty(mKeyWord)) {
					returnData(mKeyWord);
					mList.add(0, mKeyWord);
					for(int x = 0 ; x <mList.size() ; x ++ ){
						for(int y = x+1 ; y<mList.size() ; y++){
							if(mList.get(x).equals(mList.get(y))){
								one = y;
							}
						}
					}
					if(one > -1){
						mList.remove(one);
					}
					setAdapter();
					finish();
				} else {
					Toast.makeText(context, "没有关键字匹配", Toast.LENGTH_SHORT).show();
				}
				return true;
			}
			return false;
		}
	};
	private void returnData(String mKeyWord) {
		Intent intent = new Intent();
		intent.putExtra("params",mKeyWord);
		CommonSearchActivity.this.setResult(RESULT_CODE, intent);
	}
	@Override
	public void finish() {
		Editor edit = mSp.edit();
		for (int i = 0; i < mList.size(); i++) {
			edit.putString(i + "", mList.get(i));
		}
		edit.putInt("mList", mList.size());//记录总条数
		edit.commit();
		super.finish();
	}
	/**
	 * 获取传过来的数据
	 */
	private void initData() {
		context = this;
		Intent intent = getIntent();
		if (intent != null) {
			mTag = intent.getStringExtra("tag");
		}
	}
	protected void save(String mKeyWord) {
		Editor edit = mSp.edit();
		edit.putString("one"+(record++), mKeyWord);
		edit.commit();
	}
	protected void setAdapter() {
		if(mList.size() > 6){
			mList.remove(mList.size() - 1);
		}
		if(mAdapter == null){
			mAdapter = new MyAdapter(mList ,context) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					View view = View.inflate(context, R.layout.layout_listview_text_item, null);
					TextView mTv = (TextView) view.findViewById(R.id.tv_item); 
					mTv.setText(mList.get(position));
					return view;
				}
			};
			mHistory.setAdapter(mAdapter);
			mHistory.addFooterView(mViewFooter);
			mNosearch.setVisibility(View.GONE);
		}else{
			mAdapter.notifyDataSetChanged();
		}
		mHistory.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int mLastPosition = mHistory.getLastVisiblePosition();
				if(mLastPosition == position){
					delete();
				}else{
					returnData(mList.get(position));
					finish();
				}
			}
		});
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tv_cancal:
			finish();
			break;
		case R.id.tv_delete://删除历史
			//delete();
			break;
		}
	}
	/**
	 * 当触摸到搜索框以外的地方的时候，将软键盘隐藏
	 */
	public boolean onTouchEvent(android.view.MotionEvent event) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager.isActive()) {
			inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), 0);
		}
		return true;
	}
	private void delete(){
		mNosearch.setVisibility(View.VISIBLE);
		mHistory.removeFooterView(mViewFooter);
		mList.clear();
		setAdapter();
		Editor edit = mSp.edit();
		edit.clear();
		edit.commit();
	}
}
