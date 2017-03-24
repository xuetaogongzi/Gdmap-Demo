package com.wangjw.gdmapdemo.sp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 操作SharedPrefernces的工具类
 * 
 * @author Jinyun Hou
 *
 */
public class SharedPreferUtil {

	public static SharedPreferences getSharedPreferences(Context context,
                                                         String sharedPreferencesName) {
		return context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
	}

	// ----------------------------------------------------------------
	// common method
	// ----------------------------------------------------------------
	public static int getInt(Context context, String preferencesName,
                             String key, int defValue) {
		SharedPreferences sp = getSharedPreferences(context, preferencesName);
		return sp.getInt(key, defValue);
	}

	public static void putInt(Context context, String preferencesName,
                              String key, int value) {
		SharedPreferences sp = getSharedPreferences(context, preferencesName);
		sp.edit().putInt(key, value).commit();
	}

	public static long getLong(Context context, String preferencesName,
                               String key, long defValue) {
		SharedPreferences sp = getSharedPreferences(context, preferencesName);
		return sp.getLong(key, defValue);
	}

	public static void putLong(Context context, String preferencesName,
                               String key, long value) {
		SharedPreferences sp = getSharedPreferences(context, preferencesName);
		sp.edit().putLong(key, value).commit();
	}

	public static float getFloat(Context context, String preferencesName,
                                 String key, float defValue) {
		SharedPreferences sp = getSharedPreferences(context, preferencesName);
		return sp.getFloat(key, defValue);
	}

	public static void putFloat(Context context, String preferencesName,
                                String key, float value) {
		SharedPreferences sp = getSharedPreferences(context, preferencesName);
		sp.edit().putFloat(key, value).commit();
	}

	public static String getString(Context context, String preferencesName,
                                   String key, String defValue) {
		SharedPreferences sp = getSharedPreferences(context, preferencesName);
		return sp.getString(key, defValue);
	}

	public static void putString(Context context, String preferencesName,
                                 String key, String value) {
		SharedPreferences sp = getSharedPreferences(context, preferencesName);
		sp.edit().putString(key, value).commit();
	}

	public static boolean getBoolean(Context context, String preferencesName,
                                     String key, boolean defValue) {
		SharedPreferences sp = getSharedPreferences(context, preferencesName);
		return sp.getBoolean(key, defValue);
	}

	public static void putBoolean(Context context, String preferencesName,
                                  String key, boolean value) {
		SharedPreferences sp = getSharedPreferences(context, preferencesName);
		sp.edit().putBoolean(key, value).commit();
	}

	public static void removeKey(Context context, String preferencesName, String key) {
		SharedPreferences sp = getSharedPreferences(context, preferencesName);
		sp.edit().remove(key).commit();
	}

	public static boolean containsKey(Context context, String preferencesName, String key) {
		SharedPreferences sp = getSharedPreferences(context, preferencesName);
		return sp.contains(key);
	}

	public static boolean clean(Context context, String preferencesName) {
		SharedPreferences sp = getSharedPreferences(context, preferencesName);
		return sp.edit().clear().commit();
	}

}