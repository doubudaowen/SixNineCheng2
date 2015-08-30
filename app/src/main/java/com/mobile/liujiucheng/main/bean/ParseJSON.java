package com.mobile.liujiucheng.main.bean;

import java.util.List;

public class ParseJSON {

    public String msg;
	public String success;
	public List<City> mCity;

	// 北京
	
	public class City {
		public String id;
		public String isDel;
		public String name;
		public String parentId;
		public List<Position> mPosition;
	}
	// 朝阳
	public class Position {
		public String id;
		public String listName;
		public String name;
		public String parentId;
		public String zuobiaoX;
		public String zuobiaoY;
		public List<Area> mArea;
	}
	// 双井
	public class Area {
		public String id;
		public String listname;
		public String name;
		public String parentId;
		public String zuobiaoX;
		public String zuobiaoY;
	}
}
