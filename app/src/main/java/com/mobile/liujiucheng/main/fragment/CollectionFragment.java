package com.mobile.liujiucheng.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.liujiucheng.sixninecheng.main.R;

/**
 * 消息
 * @author pc Mr_Gang
 * 
 */
public class CollectionFragment extends Fragment {

	private View view;
	private Context context;
	
	/*private ListView lv_main_collection;
	private MyAdapter adapter;
	private List<CollectionBean> mData;
	private PullToRefreshListView mPullLoaing;*/
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_collection, container, false);
		context = getActivity();
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}
