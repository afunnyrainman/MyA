package com.example.user.mya.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 保存SharedPreferences
 * 
 * @author zzs
 * 
 */
public class SharedPreferencesUtils {
	public final static String VALUEONENAME="value";
	public final  static  String ONENAME="one";
	public final  static String HREFTOU="http://www.sbkk8.cn";//网址头
	public  final static  String MAINURL="http://www.sbkk8.cn/mingzhu/gudaicn/";

	public static  final String LOGINVALUE="loginvalue";

	public static final String TITLEJILU="zuihouyicidebiaoti";//最后一次的标题记录

	public static void SavaSharedPreferences(Context context, String name,
			String key, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				name, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getSharedPreferences(Context context, String name,
			String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				name, Context.MODE_PRIVATE);
		String value = sharedPreferences.getString(key, null);
		return value;
	}
}
