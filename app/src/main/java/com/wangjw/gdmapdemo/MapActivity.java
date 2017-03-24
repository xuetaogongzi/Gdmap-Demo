package com.wangjw.gdmapdemo;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.wangjw.gdmapdemo.sp.AccountSp;
import com.wangjw.gdmapdemo.util.Logger;
import com.wangjw.gdmapdemo.util.ToastUtil;

/**
 * Created by wangjw on 17/3/24.
 */

public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MapActivity";

    private MapView mMapView;

    private AMap mAMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMapView = (MapView) findViewById(R.id.MapView);

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        initMap();
    }

    private void initMap() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }

        //显示实时交通状况
        //mAMap.setTrafficEnabled(true);

        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE(卫星地图模式)
        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);

        // 绑定marker拖拽事件
        mAMap.setOnMarkerDragListener(mOnMarkerDragListener);

        //绑定Marker被点击事件
        mAMap.setOnMarkerClickListener(mOnMarkerClickListener);

        //绑定信息窗点击事件
        mAMap.setOnInfoWindowClickListener(mOnInfoWindowClickListener);

        //设置缩放级别
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        Logger.d(TAG, "max = " + mAMap.getMaxZoomLevel() + ", min = " + mAMap.getMinZoomLevel());

        drawMarker();

        //绘制自定义InfoWindow
        mAMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            View infoWindow = null;
            @Override
            public View getInfoWindow(Marker marker) {
                if (infoWindow == null) {
                    infoWindow = getLayoutInflater().inflate(R.layout.layout_trace_map_window, null);
                }
                render(marker, infoWindow);

                return infoWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
    }

    private void render(Marker marker, View view) {
        TextView tvName = (TextView) view.findViewById(R.id.TextView_Name);
        TextView tvLocal = (TextView) view.findViewById(R.id.TextView_Local);

        tvName.setText(marker.getTitle());
        tvLocal.setText(marker.getSnippet());
    }

    private void drawMarker() {
        double latitude = Double.parseDouble(AccountSp.getLatitude(this));
        double longitude = Double.parseDouble(AccountSp.getLongitude(this));

        //将地图移动到定位点
        mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(latitude, longitude)));

        //绘制marker
        Marker marker = mAMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("这是标题")
                .snippet("这是内容")
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.mipmap.ic_node_label)))
                .draggable(true));//设置Marker可拖动
        marker.setObject("点击地图");

        //这句代码是设置marker在屏幕的像素坐标。这样设置了之后，maker不会随着地图的移动而移动，会一直保持在屏幕的这个坐标上。
//        marker.setPositionByPixels(360, 640);

        //绘制曲线
        mAMap.addPolyline((new PolylineOptions())
                .add(new LatLng(30.209395, 120.225742), new LatLng(32.209395, 121.225742))
                .geodesic(true).color(Color.RED).width(3));
    }

    //定义Marker点击事件监听
    private AMap.OnMarkerClickListener mOnMarkerClickListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            String str = (String) marker.getObject();
            ToastUtil.showToast(MapActivity.this, str);
            marker.showInfoWindow();
            return false;
        }
    };

    //定义InfoWindow点击事件
    private AMap.OnInfoWindowClickListener mOnInfoWindowClickListener = new AMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            if (marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
            }
            ToastUtil.showToast(MapActivity.this, marker.getTitle());
        }
    };

    //定义Marker拖拽的监听
    private AMap.OnMarkerDragListener mOnMarkerDragListener = new AMap.OnMarkerDragListener() {

        // 当marker开始被拖动时回调此方法, 这个marker的位置可以通过getPosition()方法返回。
        // 这个位置可能与拖动的之前的marker位置不一样。
        // marker 被拖动的marker对象。
        @Override
        public void onMarkerDragStart(Marker arg0) {
            // TODO Auto-generated method stub
            Logger.d(TAG, "start");
        }

        // 在marker拖动完成后回调此方法, 这个marker的位置可以通过getPosition()方法返回。
        // 这个位置可能与拖动的之前的marker位置不一样。
        // marker 被拖动的marker对象。
        @Override
        public void onMarkerDragEnd(Marker arg0) {
            // TODO Auto-generated method stub
            Logger.d(TAG, "end");
        }

        // 在marker拖动过程中回调此方法, 这个marker的位置可以通过getPosition()方法返回。
        // 这个位置可能与拖动的之前的marker位置不一样。
        // marker 被拖动的marker对象。
        @Override
        public void onMarkerDrag(Marker arg0) {
            // TODO Auto-generated method stub
            Logger.d(TAG, "move");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
}
