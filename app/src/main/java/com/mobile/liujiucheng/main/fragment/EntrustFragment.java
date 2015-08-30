package com.mobile.liujiucheng.main.fragment;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mobile.liujiucheng.main.bean.base.BaseFragment;
import com.mobile.liujiucheng.sixninecheng.main.R;
/**
 * 二手房委托房源的Fragment
 * @author pc
 */
public class EntrustFragment extends BaseFragment {
	private View view;
	private EditText et_name;//姓名
	private EditText et_way;//电话号码
	private EditText et_position;//地区
	private EditText et_num;//
	private EditText et_num_area;//面积
	
	private Map<String,String> map;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.listview_entrust, container, false);
		return view;
	}
	/**
	 * 填充UI
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}
	private void initData() {
		if(map == null){
			map = new HashMap<String, String>();
		}
		et_name = (EditText) view.findViewById(R.id.et_name);
		et_way = (EditText) view.findViewById(R.id.et_way);
		et_position = (EditText) view.findViewById(R.id.et_position);
		et_num = (EditText) view.findViewById(R.id.et_num);
		et_num_area = (EditText) view.findViewById(R.id.et_num_area);
	}
	public boolean getData(){
		String name = et_name.getText().toString().trim();
		String null1 = judgeNull(name,"联系人不能为空");
		if(null1 == null){
			return false;
		}
		String way = et_way.getText().toString().trim();
		String null2 = judgeNull(way,"联系方式不能为空");
		if(null2 == null){
			return false;
		}
		String position = et_position.getText().toString().trim();
		String null3 = judgeNull(position,"小区不能为空");
		if(null3 == null){
			return false;
		}
		String num = et_num.getText().toString().trim();
		String null4 = judgeNull(num,"户型不能为空");
		if(null4 == null){
			return false;
		}
		String area = et_num_area.getText().toString().trim();
		String null5 = judgeNull(area,"面积不能为空");
		if(null5 == null){
			return false;
		}
		
		//Log.e("TAG", "name=="+name);
		//Log.e("TAG", "way=="+way);
		//Log.e("TAG", "position=="+position);
		//Log.e("TAG", "num=="+num);
		//Log.e("TAG", "area=="+area);
		map.put("name", name);
		map.put("way", way);
		map.put("position", position);
		map.put("num", num);
		map.put("area", area);
		return true;
	}
	private String judgeNull(String data ,String descriptor){
		if(TextUtils.isEmpty(data)){
			Toast.makeText(getActivity(), descriptor, Toast.LENGTH_SHORT).show();
			return null;
		}
		return "name";
	}
	public Map getMap(){
		return map;
	}
}
