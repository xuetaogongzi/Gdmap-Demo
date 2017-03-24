package com.wangjw.gdmapdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.wangjw.gdmapdemo.sp.AccountSp;
import com.wangjw.gdmapdemo.util.Logger;
import com.wangjw.gdmapdemo.util.ToastUtil;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int REQ_CODE_LOCATION = 100;

    private TextView mTvLocation;

    private AMapLocationClient mLocationClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        mTvLocation = (TextView) findViewById(R.id.TextView_Location);

        findViewById(R.id.Button_Map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        checkLocationPermission();
    }

    private void checkLocationPermission() {
        PermissionGen.with(MainActivity.this)
                .addRequestCode(REQ_CODE_LOCATION)
                .permissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = REQ_CODE_LOCATION)
    public void doSomething() {
        initLoc();
    }


    @PermissionFail(requestCode = REQ_CODE_LOCATION)
    public void doFailSomething() {
        ToastUtil.showToast(this, R.string.need_grant_location_permission);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止定位后，本地定位服务并不会被销毁
        mLocationClient.stopLocation();
        //销毁定位客户端，同时销毁本地定位服务。
        mLocationClient.onDestroy();
    }

    private void initLoc() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(mAMapLocationListener);

        AMapLocationClientOption option = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果，该方法默认为false。
        option.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果，该方法默认为false。
        option.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        option.setInterval(5 * 1000);
        //设置是否返回地址信息（默认返回地址信息）
        option.setNeedAddress(true);
        //设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        option.setHttpTimeOut(20000);

        mLocationClient.setLocationOption(option);
        mLocationClient.startLocation();
    }

    private AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容
                    double latitude = aMapLocation.getLatitude();
                    double longitude = aMapLocation.getLongitude();
                    AccountSp.setLatitude(MainActivity.this, String.valueOf(latitude));
                    AccountSp.setLongitude(MainActivity.this, String.valueOf(longitude));
                    Logger.d(TAG, "latitude = " + latitude + ", longitude = " + longitude);

                    String location = aMapLocation.getAddress();
                    mTvLocation.setText(location);
                    Logger.d(TAG, "location = " + location);
                } else {
                    Logger.d(TAG, "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                }
            }
        }
    };
}
