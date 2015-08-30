package com.mobile.liujiucheng.main.bean;

import java.util.List;

/**
 * 出租房的 javabean
 * @author pc Mr_Gang
 */
public class Retalbean {
	
	public List<BData> data;
	public String msg ;
	public String status;
	
	public class BData{
		
		public String aspect;
		public String bussinessarea;
		public String city ;
		public String district;
		public String houseType ;
		public String pubTume;
		public String rent;
		public String sid;
		public String spec;
		public String title;
		public String imgUrl;
		public String url;
	}
}
