package com.mobile.liujiucheng.main.bean;

import android.graphics.Bitmap;

public class HoseLiuJiuCheng {

	/**
	 *房源的图片
	 */
	private Bitmap bitmap;
	/**
	 * 房源的标题
	 */
	private String tilte;
	/**
	 * 房源的描述
	 */
	private String describe;
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getTilte() {
		return tilte;
	}
	public void setTilte(String tilte) {
		this.tilte = tilte;
	}
	public String getContent() {
		return describe;
	}
	public void setContent(String describe) {
		this.describe = describe;
	}
}
