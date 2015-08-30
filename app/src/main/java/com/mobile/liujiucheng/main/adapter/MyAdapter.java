package com.mobile.liujiucheng.main.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 
 * @author pc Mr_Gang
 * 
 */
public abstract class MyAdapter extends BaseAdapter {

	@SuppressWarnings("rawtypes")
	private List data;
	@SuppressWarnings("unused")
	private Context mContext;

	public MyAdapter(@SuppressWarnings("rawtypes") List data, Context context) {
		this.data = data;
		this.mContext = context;
	}
	@Override
	public int getCount() {
		return data.size();
	}
	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView,
			ViewGroup parent);

}
