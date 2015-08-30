package com.mobile.liujiucheng.main.bean;

import android.graphics.Bitmap;

public class CollectionBean {

	private Bitmap mIcon;//收藏的房子的图片
	private String mTitle;//收藏房子的标题

	private String mSize;//房子的大小
	private String mDes;//房子的描述
	private String mRoom;//房子的厅室
	private String mRegion;//地区
	private String mPosition;//房子的位置，比如中关村

	private String mPrice;//房子的价格
	private boolean mIsCall;//是否拨打电话
	private String mDate;//发布的时间
	
	public CollectionBean() {
		
	}
	public CollectionBean(Bitmap mIcon, String mTitle, String mSize,
			String mDes, String mRoom, String mRegion, String mPosition,
			String mPrice, boolean mIsCall, String mDate) {
		this.mIcon = mIcon;
		this.mTitle = mTitle;
		this.mSize = mSize;
		this.mDes = mDes;
		this.mRoom = mRoom;
		this.mRegion = mRegion;
		this.mPosition = mPosition;
		this.mPrice = mPrice;
		this.mIsCall = mIsCall;
		this.mDate = mDate;
	}

	

	public Bitmap getmIcon() {
		return mIcon;
	}

	public void setmIcon(Bitmap mIcon) {
		this.mIcon = mIcon;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getmSize() {
		return mSize;
	}

	public void setmSize(String mSize) {
		this.mSize = mSize;
	}

	public String getmDes() {
		return mDes;
	}

	public void setmDes(String mDes) {
		this.mDes = mDes;
	}

	public String getmRoom() {
		return mRoom;
	}

	public void setmRoom(String mRoom) {
		this.mRoom = mRoom;
	}

	public String getmRegion() {
		return mRegion;
	}

	public void setmRegion(String mRegion) {
		this.mRegion = mRegion;
	}

	public String getmPosition() {
		return mPosition;
	}

	public void setmPosition(String mPosition) {
		this.mPosition = mPosition;
	}

	public String getmPrice() {
		return mPrice;
	}

	public void setmPrice(String mPrice) {
		this.mPrice = mPrice;
	}

	public boolean ismIsCall() {
		return mIsCall;
	}

	public void setmIsCall(boolean mIsCall) {
		this.mIsCall = mIsCall;
	}

	public String getmDate() {
		return mDate;
	}

	public void setmDate(String mDate) {
		this.mDate = mDate;
	}

}
