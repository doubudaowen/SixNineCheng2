package com.mobile.liujiucheng.sixninecheng.main;

import java.util.ArrayList;
import java.util.List;

import com.mobile.liujiucheng.main.adapter.MyAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 房子的类型
 * @author Administrator
 *
 */
public class ClassHouseActivity extends BaseActivity implements OnClickListener {

	private List<String> mList;
	private ListView mListView;
	private MyAdapter mAdapter;
	private Context context;
	private ImageView mImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_house_table);
		initView();
	}
	private void initView() {
		context = this;
		mListView = (ListView) findViewById(R.id.lv_year);
		mImageView = (ImageView) findViewById(R.id.iv_exit);
		mImageView.setOnClickListener(this);
		initData();
	}
	private void initData() {
		mList = new ArrayList<String>();
		mList.add("住宅");
		mList.add("写字楼");
		mList.add("商铺");
		mList.add("别墅");
		setMyAdapter();
	}
	private void setMyAdapter() {
		if (mAdapter == null) {
			mAdapter = new MyAdapter(mList, context) {
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View view;
					if (convertView == null) {
						view = View.inflate(context,
								R.layout.layout_listview_text_item, null);
					} else {
						view = convertView;
					}
					TextView tv = (TextView) view.findViewById(R.id.tv_item);
					tv.setText(mList.get(position));
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
				returnUp(position);
			}
		});
	}
	protected void returnUp(int position) {
		Intent intent = new Intent();
		intent.putExtra("data", mList.get(position));
		this.setResult(400, intent);
		finish();
	}
	/**
	 * 开启一个新的界面
	 * @param clazz
	 */
	private void newStart(Class clazz){
		Intent intent = new Intent(context, clazz);
		startActivity(intent);
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.iv_exit:
			finish();
			break;
		}
	}
}
