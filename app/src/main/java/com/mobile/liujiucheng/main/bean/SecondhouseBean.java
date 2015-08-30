package com.mobile.liujiucheng.main.bean;

import java.util.List;

public class SecondhouseBean {
	public List<MLLData> data;
	public String msg;
	public String status;

	public class MLLData {
		public String sid;
		public String count;
		public String district;
		public String listName;
		public String zuobiaoX;
		public String zuobiaoY;
		public String bussinessarea;
		public String parentId;
	}
}
