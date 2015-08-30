package com.mobile.liujiucheng.main.bean;

import java.util.List;

/**
 * 出租房的详情页
 * @author pc
 */
public class RetalbeanDetails {
	
	public RLData data;
	public String msg;
	public String status;
	
	public class RLData{
		
		public String aspect;
		public String businessArea;
		public String city;
		public String description;
		public String district;
		public String floor;
		public String houseType;
		public String interiordesign;
		
		public String rent;
		public String rentFloor;
		public String sid;
		public String spec;
		public String tenement;
		
		public String title;
		public String zuobiaoX;
		public String zuobiaoY;
		
		public String linkPhone;
		
		public List<Near> nearby;

		public class Near{
			public String bussinessarea;
			public String district;
			public String floor;
			public String rent;
			public String rentFloor;
			public String sid;
			public String spec;
			public String url;
		}
	}
}
