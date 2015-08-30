package com.mobile.liujiucheng.sixninecheng.main;

import java.util.ArrayList;
import java.util.List;

import com.mobile.liujiucheng.main.adapter.MyAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 我的委托
 * @author pc
 *
 */
public class EntrustActivity extends BaseActivity implements OnClickListener {

	private List<String> mList;
	private ListView mListView;
	private MyAdapter mAdapter;
	private Context context;
	private ImageView mImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_my_entrust);
		initView();
	}
	private void initView() {
		context = this;
		mListView = (ListView) findViewById(R.id.lv_my_entrust);
		mImageView = (ImageView) findViewById(R.id.iv_exit);
		mImageView.setOnClickListener(this);
		initData();
	}
	private void initData() {
		mList = new ArrayList<String>();
		mList.add("二手房");
		mList.add("出租房");
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
								R.layout.layout_listview_item, null);
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
				switch (position) {
				case 0:
					Toast.makeText(context, "你还没有委托相关的房源哦", Toast.LENGTH_LONG).show();
					break;
				case 1:
					Toast.makeText(context, "你还没有委托相关的房源哦", Toast.LENGTH_LONG).show();
					break;
				}
			}
		});
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
