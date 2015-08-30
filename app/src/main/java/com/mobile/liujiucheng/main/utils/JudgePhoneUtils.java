package com.mobile.liujiucheng.main.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 判断是不是手机号码
 * @author pc
 *
 */
public class JudgePhoneUtils {

	public static boolean mPhone(String value) {
		//String regExp = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";  
		String regExp = "^1(3[0-9]|4[0-9]|5[0-9]|8[0-9]|7[0-9])\\d{8}$";  
		Pattern p = Pattern.compile(regExp);  
		Matcher m = p.matcher(value);  
		return m.find();
	}
}
