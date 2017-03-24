package com.wangjw.gdmapdemo.sp;

import android.content.Context;

/**
 * Created by Administrator on 2016/5/26.
 */
public class AccountSp {

    public static String PREFERENCE_ACCOUNT_NAME = "account";

    private static final String KEY_LATITUDE = "latitude";

    private static final String KEY_LONGITUDE = "longitude";

    //获取、保存纬度
    public static String getLatitude(Context context) {
        return SharedPreferUtil.getString(context, PREFERENCE_ACCOUNT_NAME, KEY_LATITUDE, "");
    }

    public static void setLatitude(Context context, String latitude) {
        SharedPreferUtil.putString(context, PREFERENCE_ACCOUNT_NAME, KEY_LATITUDE, latitude);
    }

    //获取、保存精度
    public static String getLongitude(Context context) {
        return SharedPreferUtil.getString(context, PREFERENCE_ACCOUNT_NAME, KEY_LONGITUDE, "");
    }

    public static void setLongitude(Context context, String longitude) {
        SharedPreferUtil.putString(context, PREFERENCE_ACCOUNT_NAME, KEY_LONGITUDE, longitude);
    }
}
