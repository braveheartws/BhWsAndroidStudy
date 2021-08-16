/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.havefun.bhwsandroidstudy.map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.baidu.navisdk.adapter.BNaviCommonParams;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRouteGuideManager;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBNaviListener;
import com.baidu.navisdk.adapter.IBNaviViewListener;
import com.baidu.navisdk.adapter.struct.BNGuideConfig;
import com.baidu.navisdk.adapter.struct.BNHighwayInfo;
import com.baidu.navisdk.adapter.struct.BNRoadCondition;
import com.baidu.navisdk.adapter.struct.BNaviInfo;
import com.baidu.navisdk.adapter.struct.BNaviLocation;
import com.baidu.navisdk.adapter.struct.BNaviResultInfo;
import com.baidu.navisdk.ui.routeguide.model.RGLineItem;

import java.util.List;

/**
 * 诱导界面
 */
public class DemoGuideActivity extends AppCompatActivity {

    private static final String TAG = DemoGuideActivity.class.getName();

    private IBNRouteGuideManager mRouteGuideManager;
    private IBNaviListener.DayNightMode mMode = IBNaviListener.DayNightMode.DAY;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean fullScreen = supportFullScreen();
        Bundle params = new Bundle();

        params.putBoolean(BNaviCommonParams.ProGuideKey.IS_SUPPORT_FULL_SCREEN, fullScreen);
        mRouteGuideManager = BaiduNaviManagerFactory.getRouteGuideManager();
        BNGuideConfig config = new BNGuideConfig.Builder()
                .params(params)
                .build();
        view = mRouteGuideManager.onCreate(this, config);

        if (view != null) {
            setContentView(view);
        }
        initTTSListener();
        routeGuideEvent();
    }

    // 导航过程事件监听
    private void routeGuideEvent() {


        BaiduNaviManagerFactory.getRouteGuideManager().setNaviViewListener(
                new IBNaviViewListener() {
                    @Override
                    public void onMainInfoPanCLick() {
                    }

                    @Override
                    public void onNaviTurnClick() {
                    }

                    @Override
                    public void onFullViewButtonClick(boolean show) {
                    }

                    @Override
                    public void onFullViewWindowClick(boolean show) {
                    }

                    @Override
                    public void onNaviBackClick() {
                        Log.e(TAG, "onNaviBackClick");
                        finish();
                    }

                    @Override
                    public void onBottomBarClick(Action action) {
                    }

                    @Override
                    public void onNaviSettingClick() {
                        Log.e(TAG, "onNaviSettingClick");
                    }

                    @Override
                    public void onRefreshBtnClick() {
                    }

                    @Override
                    public void onZoomLevelChange(int level) {
                    }

                    @Override
                    public void onMapClicked(double x, double y) {
                    }

                    @Override
                    public void onMapMoved() {
                        Log.e(TAG, "onMapMoved");
                    }

                    @Override
                    public void onFloatViewClicked() {
                        try {
                            Intent intent = new Intent();
                            intent.setPackage(getPackageName());
                            intent.setClass(DemoGuideActivity.this,
                                    Class.forName(DemoGuideActivity.class.getName()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            startActivity(intent);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
        System.gc();
    }

    private void initTTSListener() {
        // 注册同步内置tts状态回调
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(
                new IBNTTSManager.IOnTTSPlayStateChangedListener() {
                    @Override
                    public void onPlayStart() {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayStart");
                    }

                    @Override
                    public void onPlayEnd(String speechId) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayEnd");
                    }

                    @Override
                    public void onPlayError(int code, String message) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayError" + message);
                    }
                }
        );

        // 注册内置tts 异步状态消息
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.e("BNSDKDemo", "ttsHandler.msg.what=" + msg.what);
                    }
                }
        );
    }

    private void unInitTTSListener() {
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(null);
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(null);

    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if (BNDemoUtils.getBoolean(this, "float_window")) {
            mRouteGuideManager.onForeground();
        }*/
        mRouteGuideManager.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mRouteGuideManager.onResume();
    }

    protected void onPause() {
        super.onPause();
        mRouteGuideManager.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRouteGuideManager.onStop();
        /*if (BNDemoUtils.getBoolean(this, "float_window")) {
            mRouteGuideManager.onBackground();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRouteGuideManager.onDestroy(false);
        unInitTTSListener();
        mRouteGuideManager = null;
    }

    @Override
    public void onBackPressed() {
        mRouteGuideManager.onBackPressed(false, true);
    }

    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mRouteGuideManager.onConfigurationChanged(newConfig);
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {

    }

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (!mRouteGuideManager.onKeyDown(keyCode, event)) {
            return super.onKeyDown(keyCode, event);
        }
        return true;

    }

    private boolean supportFullScreen() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            int color;
            if (Build.VERSION.SDK_INT >= 23) {
                color = Color.TRANSPARENT;
            } else {
                color = 0x2d000000;
            }
            window.setStatusBarColor(color);

            if (Build.VERSION.SDK_INT >= 23) {
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                int uiVisibility = window.getDecorView().getSystemUiVisibility();
                if (mMode == IBNaviListener.DayNightMode.DAY) {
                    uiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                window.getDecorView().setSystemUiVisibility(uiVisibility);
            } else {
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        mRouteGuideManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mRouteGuideManager.onActivityResult(requestCode, resultCode, data);
    }
}
