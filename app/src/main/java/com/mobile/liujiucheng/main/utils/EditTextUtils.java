package com.mobile.liujiucheng.main.utils;

import android.text.method.NumberKeyListener;
import android.widget.EditText;

public class EditTextUtils {
	
	/**
	 *输入框支持输入数字和英文字母，数字加字母最多只能输入6位，
	 * @param inputEditText   
	 *limit 限制
	 */
	public static void limit(final EditText et){
		//安卓中限制EditText输入的内容
		et.setKeyListener(new NumberKeyListener(){
			@Override
			public int getInputType() {//用来限制输入法（就是系统自带的软键盘显示
				return 3;
			}
			@Override
			protected char[] getAcceptedChars() {//新建一个char[]，在里面添加允许输入的字符
				char[] numberChars={'1','2','3','4','5','6','7','8','9','0','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
						return numberChars;
			}
		});
	}
}
