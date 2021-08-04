package com.havefun.bhwsandroidstudy.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.core.VehicleInfo;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.SuggestAddrInfo;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo;
import com.baidu.platform.comjni.jninative.tts.WNaviTTSPlayer;
import com.havefun.bhwsandroidstudy.R;

import java.util.ArrayList;
import java.util.List;

public class BaiduMapActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BaiduMapActivity";

    private MapView mapView;
    private BaiduMap mBaiduMap;
    private Button btnLine;
    private RoutePlanSearch routePlanSearch;
    private WalkNaviLaunchParam walkNaviLaunchParam;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_map);
        mapView = findViewById(R.id.mapView);
        mBaiduMap = mapView.getMap();
        btnLine = findViewById(R.id.btnLine);

        reqPermission();


        btnLine.setOnClickListener(this);

        findViewById(R.id.btnDriver).setOnClickListener(this);

        initWalkingNav();
    }

    private void initWalkingNav() {
        WalkNavigateHelper.getInstance().initNaviEngine(this, new IWEngineInitListener() {

            @Override
            public void engineInitSuccess() {
                //引擎初始化成功的回调
                routeWalkPlanWithParam();
            }

            @Override
            public void engineInitFail() {
                //引擎初始化失败的回调
            }
        });
    }

    private void routeWalkPlanWithParam() {
        LatLng startPt  = new LatLng(31.222008,121.4817);
        LatLng endPt = new LatLng(31.2032025932295,121.4850275789);

        //构造WalkNaviLaunchParam
        WalkRouteNodeInfo walkStartNode = new WalkRouteNodeInfo();
        walkStartNode.setLocation(startPt);
        WalkRouteNodeInfo walkEndNode = new WalkRouteNodeInfo();
        walkEndNode.setLocation(endPt);
        walkNaviLaunchParam = new WalkNaviLaunchParam().startNodeInfo(walkStartNode).endNodeInfo(walkEndNode);
    }

    private void reqPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions,123);
            ArrayList<String> permissionsList = new ArrayList<>();

            for (String perm : permissions) {
                if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(perm)) {
                    permissionsList.add(perm);
                    // 进入到这里代表没有权限.
                }
            }

            if (!permissionsList.isEmpty()) {
                String[] strings = new String[permissionsList.size()];
                requestPermissions(permissionsList.toArray(strings), 0);
            }
        }


    }

    private void walkingRoute() {

        routePlanSearch = RoutePlanSearch.newInstance();

        routePlanSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                Log.d(TAG, "onGetWalkingRouteResult = " + walkingRouteResult.error + ", status = " + walkingRouteResult.status
                + " info: " + Thread.currentThread().getName());
                WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);

                for (int i = 0; i < walkingRouteResult.getRouteLines().size(); i++) {
                    WalkingRouteLine walkingRouteLine = walkingRouteResult.getRouteLines().get(i);
                    List<WalkingRouteLine.WalkingStep> allStep = walkingRouteLine.getAllStep();
                    for (int j = 0; j < allStep.size(); j++) {
                        WalkingRouteLine.WalkingStep walkingStep = allStep.get(j);
                        Log.d(TAG, "onGetTransitRouteResult: " + " stepType | " + walkingStep.getDuration() +
                         " | " + walkingStep.getEntranceInstructions()+  " | " + walkingStep.getExitInstructions() +
                                  "||" + walkingStep.getName());
                    }

                    Log.d(TAG, "onGetTransitRouteResult: \r\n");
                }

                if (walkingRouteResult.getRouteLines() != null && walkingRouteResult.getRouteLines().size() > 0) {
                    //获取路径规划数据,(以返回的第一条数据为例)
                    //为WalkingRouteOverlay实例设置路径数据
                    overlay.setData(walkingRouteResult.getRouteLines().get(0));
                    //在地图上绘制WalkingRouteOverlay
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
                TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);

                for (int i = 0; i < transitRouteResult.getRouteLines().size(); i++) {
                    TransitRouteLine transitRouteLine = transitRouteResult.getRouteLines().get(i);
                    Log.d(TAG, "onGetTransitRouteResult: " + transitRouteLine.getTitle());
                    List<TransitRouteLine.TransitStep> allStep = transitRouteLine.getAllStep();
                    for (int j = 0; j < allStep.size(); j++) {
                        TransitRouteLine.TransitStep transitStep = allStep.get(j);
                        TransitRouteLine.TransitStep.TransitRouteStepType stepType = transitStep.getStepType();
                        int totalPrice = 0;

                        Log.d(TAG, "onGetTransitRouteResult: " + transitStep.getName() + " | "+
                                transitStep.getInstructions() + " stepType | " + stepType + " | totalPrice: " + totalPrice
                        + " | " + transitStep.getEntrance().getTitle() + " | " + transitStep.getExit().getTitle());

                        if (transitStep.getVehicleInfo() != null) {
                            VehicleInfo vehicleInfo = transitStep.getVehicleInfo();
                            Log.d(TAG, "onGetTransitRouteResult: " + vehicleInfo.getTitle() + " "
                            +vehicleInfo.getPassStationNum() + " " + vehicleInfo.getTotalPrice() + " "
                            + vehicleInfo.getZonePrice());
                        }

                    }

                    Log.d(TAG, "onGetTransitRouteResult: \r\n");
                }

                if (transitRouteResult.getRouteLines() != null && transitRouteResult.getRouteLines().size() > 0) {
                    //获取路径规划数据,(以返回的第一条数据为例)
                    //为WalkingRouteOverlay实例设置路径数据
                    overlay.setData(transitRouteResult.getRouteLines().get(0));
                    //在地图上绘制WalkingRouteOverlay
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
                Log.d(TAG, "onGetTransitRouteResult = " + transitRouteResult.error + ", status = " + transitRouteResult.status);
            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
                Log.d(TAG, "onGetMassTransitRouteResult = " + massTransitRouteResult.error + ", status = " + massTransitRouteResult.status);
            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                Log.d(TAG, "onGetDrivingRouteResult = " + drivingRouteResult.error + ", status = " + drivingRouteResult.status);
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
                Log.d(TAG, "onGetIndoorRouteResult = " + indoorRouteResult.error + ", status = " + indoorRouteResult.status);
            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
                Log.d(TAG, "onGetBikingRouteResult = " + bikingRouteResult.error + ", status = " + bikingRouteResult.status);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLine) {
            PlanNode stNode = PlanNode.withLocation(new LatLng(31.222008,121.4817));
            PlanNode enNode = PlanNode.withLocation(new LatLng(31.2032025932295,121.4850275789));
            //PlanNode stNode = PlanNode.withCityNameAndPlaceName("深圳","龙华地铁站");
            //PlanNode enNode = PlanNode.withCityNameAndPlaceName("深圳","深圳北站");
            /*routePlanSearch.transitSearch((new TransitRoutePlanOption())
                    .from(stNode)
                    .to(enNode).city("上海"));*/
            routePlanSearch.walkingSearch(new WalkingRoutePlanOption().from(stNode).to(enNode));
        } else if (v.getId() == R.id.btnDriver) {
            Log.d(TAG, "startWalkNavi");
            try {
                WalkNavigateHelper.getInstance().initNaviEngine(this, new IWEngineInitListener() {
                    @Override
                    public void engineInitSuccess() {
                        Log.d(TAG, "WalkNavi engineInitSuccess");
                        WalkNavigateHelper.getInstance().routePlanWithRouteNode(walkNaviLaunchParam, new IWRoutePlanListener() {
                            @Override
                            public void onRoutePlanStart() {
                                Log.d(TAG, "WalkNavi onRoutePlanStart");
                            }

                            @Override
                            public void onRoutePlanSuccess() {

                                Log.d(TAG, "onRoutePlanSuccess");

                                Intent intent = new Intent();
                                intent.setClass(BaiduMapActivity.this, WNaviGuideActivity.class);
                                startActivity(intent);

                            }

                            @Override
                            public void onRoutePlanFail(WalkRoutePlanError error) {
                                Log.d(TAG, "WalkNavi onRoutePlanFail");
                            }

                        });
                    }

                    @Override
                    public void engineInitFail() {
                        Log.d(TAG, "WalkNavi engineInitFail");
                        WalkNavigateHelper.getInstance().unInitNaviEngine();
                    }
                });
            } catch (Exception e) {
                Log.d(TAG, "startBikeNavi Exception");
                e.printStackTrace();
            }
        }

        //routePlanSearch.walkingSearch(new WalkingRoutePlanOption().from(stNode).to(enNode));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(this, "permision ", Toast.LENGTH_SHORT).show();
        walkingRoute();
    }
}
