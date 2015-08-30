package com.mobile.liujiucheng.sixninecheng.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.liujiucheng.main.adapter.MyAdapter;
import com.mobile.liujiucheng.main.utils.KeyboardUtils;

public class SearchActivity extends Activity implements OnClickListener {

	private Context context;
	private EditText et_search;
	private LinearLayout ll_select_property;
	private TextView mCancal;
	private View mView;

	//private TextView mOne;
	//private TextView mTwo;
	private TextView mPosition;
	// =============================
	private MyAdapter mAdapterOne;
	private MyAdapter mAdapterTwo;

	private ListView mHistoryTwo;// 历史记录
	private ListView mHistoryOne;// 历史记录

	private List<String> mListOne;
	private List<String> mListTwo;

	private SharedPreferences mSpOne;
	private SharedPreferences mSpTwo;

	private View mViewFooter;
	private TextView mDelete;
	private ImageView mNosearch;
	
	private ListView mListView;

	private List<String> mList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_search_housing);
		initData();
		initView();
	}

	private void initData() {
		if(mList == null){
			mList = new ArrayList<String>();
		}
		mList.add((String) this.getResources().getText(R.string.second_hand_house));
		mList.add((String) this.getResources().getText(R.string.rental_housing));
	}
	
	private void initView() {
		context = this;
		mViewFooter = View.inflate(context, R.layout.layout_listview_delete,
				null);
		mDelete = (TextView) mViewFooter.findViewById(R.id.tv_delete);

		mHistoryTwo = (ListView) findViewById(R.id.lv_history);
		mHistoryOne = (ListView) findViewById(R.id.lv_history_one);

		mNosearch = (ImageView) findViewById(R.id.iv_no_search);
		mView = View.inflate(context, R.layout.layout_popwindow_listview, null);
		mListView = (ListView) mView.findViewById(R.id.lv_popWindow);
		if (mSpOne == null) {
			mSpOne = this.getSharedPreferences("oneHistory", MODE_PRIVATE);
		}
		if (mSpTwo == null) {
			mSpTwo = this.getSharedPreferences("twoHistory", MODE_PRIVATE);
		}
		if (mListTwo == null) {
			mListTwo = new ArrayList<String>();
		}
		if (mListOne == null) {
			mListOne = new ArrayList<String>();
		}
		initData(mSpTwo,mListTwo,2,"mListTwo");
		//mOne = (TextView) mView.findViewById(R.id.tv_one);
		//mTwo = (TextView) mView.findViewById(R.id.tv_two);
		mPosition = (TextView) findViewById(R.id.tv_position);
		mCancal = (TextView) findViewById(R.id.tv_cancal);
		et_search = (EditText) findViewById(R.id.et_search);
		ll_select_property = (LinearLayout) findViewById(R.id.ll_select_property);
		ll_select_property.setOnClickListener(this);
		setPopWindow(ll_select_property);
		//mOne.setOnClickListener(this);
		//mTwo.setOnClickListener(this);
		mCancal.setOnClickListener(this);
		et_search.setOnKeyListener(onKeyListener);
	}
	private void initData(SharedPreferences mSp,List<String> list , int tag,String data) {
		int size = mSp.getInt(data, 0);
		for(int x = 0 ; x<size; x++){
			list.add(mSp.getString(x+"", ""));
		}
		if(list.size() >0){
			if(tag == 2){
				setAdapterTwo();
			}else{
				setAdapterOne();
			}
		}
	}
	private boolean bOne = true;
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.et_search:
			KeyboardUtils.keyboard(et_search);
			break;
		case R.id.ll_select_property:
			// 弹出PopWindow
			close();
			break;
		case R.id.tv_cancal:
			finish();
			break;
		case R.id.tv_one:// 出租房
			if(bOne){
				initData(mSpOne,mListOne,1,"mListOne");
				bOne = false;
			}
			mHistoryTwo.setVisibility(View.GONE);
			mHistoryOne.setVisibility(View.VISIBLE);
			setVisibility(mListOne);
			defaultColor();
			//mOne.setTextColor(Color.parseColor("#66c6f2"));
			mPosition.setText("出租房");
			close();
			break;
		case R.id.tv_two:// 二手房
			mHistoryOne.setVisibility(View.GONE);
			mHistoryTwo.setVisibility(View.VISIBLE);
			setVisibility(mListTwo);
			defaultColor();
			//mTwo.setTextColor(Color.parseColor("#66c6f2"));
			mPosition.setText("二手房");
			close();
			break;
		}
	}
	private void setVisibility(List<String> data){
		if(data.size() == 0){
			mNosearch.setVisibility(View.VISIBLE);
		}else{
			mNosearch.setVisibility(View.GONE);
		}
	}
	private void close() {
		if(popupWindow != null && popupWindow.isShowing()){
			popupWindow.dismiss();
		}else{
			popupWindow.showAsDropDown(ll_select_property);
		}
	}
	private void defaultColor() {
		//mOne.setTextColor(Color.parseColor("#999999"));
		//mTwo.setTextColor(Color.parseColor("#999999"));
	}
	private int mCurrent = 0;
	private MyAdapter adapter;
	private void setPopWindow(View v) {
		if(adapter == null){
			adapter = new MyAdapter(mList,context) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					View view = View.inflate(context, R.layout.layout_listview_text, null);
					TextView tv = (TextView) view.findViewById(R.id.tv_text);
					if(mCurrent == position){
						tv.setTextColor(Color.parseColor("#66c6f2"));
					}else {
						tv.setTextColor(Color.parseColor("#FFFFFF"));
					}
					tv.setText(mList.get(position));
					return view;
				}
			};
			mListView.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCurrent = position;
				if(position == 0){
					mHistoryOne.setVisibility(View.GONE);
					mHistoryTwo.setVisibility(View.VISIBLE);
					setVisibility(mListTwo);
					//defaultColor();
					//mTwo.setTextColor(Color.parseColor("#66c6f2"));
					mPosition.setText("二手房");
				}else{
					if(bOne){
						initData(mSpOne,mListOne,1,"mListOne");
						bOne = false;
					}
					mHistoryTwo.setVisibility(View.GONE);
					mHistoryOne.setVisibility(View.VISIBLE);
					setVisibility(mListOne);
					//defaultColor();
					//mOne.setTextColor(Color.parseColor("#66c6f2"));
					mPosition.setText("出租房");
				}
				close();
				adapter.notifyDataSetChanged();
			}
		});
		popupWindow = new PopupWindow(context);
		popupWindow.setContentView(mView);
		mListView.measure(View.MeasureSpec.UNSPECIFIED,
				View.MeasureSpec.UNSPECIFIED);
		//popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		//popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setWidth(mListView.getMeasuredWidth());
		//popupWindow.setHeight((mListView.getMeasuredHeight() + 20)*2-5);
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		Drawable d = getResources().getDrawable(R.drawable.bg_popupwindow);
		popupWindow.setBackgroundDrawable(d);
	}

	private int record = -1;
	private int one = -1;
	/**
	 * 监听软件盘，右下角的回车键
	 */
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
					if ("二手房".equals(mPosition.getText().toString().trim())) {
						newStartInterface(RentActivity.class, "two", mKeyWord);
						mListTwo.add(0, mKeyWord);
						for (int x = 0; x < mListTwo.size(); x++) {
							for (int y = x + 1; y < mListTwo.size(); y++) {
								if (mListTwo.get(x).equals(mListTwo.get(y))) {
									one = y;
								}
							}
						}
						if (one > -1) {
							mListTwo.remove(one);
						}
						setAdapterTwo();
					} else {
						newStartInterface(RetalActivity.class, "one", mKeyWord);
						mListOne.add(0, mKeyWord);
						for (int x = 0; x < mListOne.size(); x++) {
							for (int y = x + 1; y < mListOne.size(); y++) {
								if (mListOne.get(x).equals(mListOne.get(y))) {
									record = y;
								}
							}
						}
						if (record > -1) {
							mListOne.remove(record);
						}
						setAdapterOne();
					}
				} else {
					Toast.makeText(context, "没有关键字匹配", Toast.LENGTH_SHORT).show();
				}
				return true;
			}
			return false;
		}
	};

	private void newStartInterface(@SuppressWarnings("rawtypes") Class clazz,
			String tag, String data) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra("isHouse", tag);
		intent.putExtra("mParams", data);
		startActivity(intent);
	}
	private boolean b = true;
	protected void setAdapterOne() {
		if (mListOne.size() > 6) {
			mListOne.remove(mListOne.size() - 1);
		}
		//Log.e("TAG", "mListOne===" + mListOne.size());
		mAdapterOne = new MyAdapter(mListOne, context) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = View.inflate(context,
						R.layout.layout_listview_text_item, null);
				TextView mTv = (TextView) view.findViewById(R.id.tv_item);
				mTv.setText(mListOne.get(position));
				return view;
			}
		};
		if (b) {
			mHistoryOne.addFooterView(mViewFooter);
			b = false;
		}
		mHistoryOne.setAdapter(mAdapterOne);
		mNosearch.setVisibility(View.GONE);
		mHistoryOne.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int mLastPosition = mHistoryOne.getLastVisiblePosition();
				if (mLastPosition == position) {
					deleteOne();
				} else {
					newStartInterface(RetalActivity.class, "one",
							mListOne.get(position));
					// finish();
				}
			}
		});
	}
	private boolean mBoolean = true;
	protected void setAdapterTwo() {
		if (mListTwo.size() > 6) {
			mListTwo.remove(mListTwo.size() - 1);
		}
		mAdapterTwo = new MyAdapter(mListTwo, context) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = View.inflate(context,
						R.layout.layout_listview_text_item, null);
				TextView mTv = (TextView) view.findViewById(R.id.tv_item);
				mTv.setText(mListTwo.get(position));
				return view;
			}
		};
		mHistoryTwo.setAdapter(mAdapterTwo);
		if(mBoolean){
			mHistoryTwo.addFooterView(mViewFooter);
			mBoolean = false;
		}
		mNosearch.setVisibility(View.GONE);
		mHistoryTwo.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int mLastPosition = mHistoryTwo.getLastVisiblePosition();
				if (mLastPosition == position) {
					deleteTwo();
				} else {
					newStartInterface(RentActivity.class, "two",
							mListTwo.get(position));
				}
			}
		});
	}
	protected void deleteTwo() {
		mNosearch.setVisibility(View.VISIBLE);
		mHistoryTwo.removeFooterView(mViewFooter);
		mListTwo.clear();
		mBoolean = true;
		// setAdapterTwo();
		Editor edit = mSpTwo.edit();
		edit.clear();
		edit.commit();
	}
	protected void deleteOne() {
		b = true;
		mNosearch.setVisibility(View.VISIBLE);
		mHistoryOne.removeFooterView(mViewFooter);
		mListOne.clear();
		// setAdapterOne();
		Editor edit = mSpOne.edit();
		edit.clear();
		edit.commit();
	}
	private PopupWindow popupWindow;
	public boolean onTouchEvent(android.view.MotionEvent event) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager.isActive()) {
			inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), 0);
		}
		return true;
	}

	//保存数据
	@Override
	public void finish() {
		Editor edit = mSpOne.edit();
		for (int i = 0; i < mListOne.size(); i++) {
			edit.putString(i + "", mListOne.get(i));
		}
		edit.putInt("mListOne", mListOne.size());//记录总条数
		edit.commit();
		
		Editor edit2 = mSpTwo.edit();
		for (int i = 0; i < mListTwo.size(); i++) {
			edit2.putString(i + "", mListTwo.get(i));
		}
		edit2.putInt("mListTwo", mListTwo.size());//记录总条数
		edit2.commit();
		super.finish();
	}
}
