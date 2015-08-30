package com.mobile.liujiucheng.main.bean;

import java.util.List;

public class ParseMyJSON {
	
	public List<City> city;
	
	public class City{
		public String id;
		public String isDel;
		public String name;
		public String parentId;
		public List<Area> tdistrict;
	}
	
	public class Area {
		public List<Address> bussinessareaList;
		public String id;
		public String listName;
		public String name;
		public String parentId;
		public String zuobiaoX;
		public String zuobiaoY;
	}

	public class Address {
		public List<Position> communityList;
		public String id;
		public String listName;
		public String name;
		public String parentId;
		public List<XQ> xiaoquList;
		public String zuobiaoX;
		public String zuobiaoY;
	}
	public class Position {

	}
	public class XQ {

	}
}