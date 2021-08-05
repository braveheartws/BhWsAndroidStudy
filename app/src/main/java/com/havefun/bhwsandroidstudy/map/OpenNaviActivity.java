package com.havefun.bhwsandroidstudy.map;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviCommonParams;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.havefun.bhwsandroidstudy.R;

import java.util.ArrayList;
import java.util.List;

public class OpenNaviActivity extends AppCompatActivity {
    private static final String TAG = "OpenNaviActivity";
    private static final String[] authBaseArr = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int authBaseRequestCode = 1;
    private BroadcastReceiver mReceiver;
    private Button button;

    private Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                    Toast.makeText(OpenNaviActivity.this, "算路开始", Toast.LENGTH_SHORT).show();
                    break;
                case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                    Toast.makeText(OpenNaviActivity.this, "算路成功", Toast.LENGTH_SHORT).show();
                    // 躲避限行消息
                    Bundle infoBundle = (Bundle) msg.obj;
                    if (infoBundle != null) {
                        String info = infoBundle
                                .getString(BNaviCommonParams.BNRouteInfoKey.TRAFFIC_LIMIT_INFO);
                        Log.e("OnSdkDemo", "info = " + info);
                    }
                    break;
                case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                    Toast.makeText(OpenNaviActivity.this.getApplicationContext(),
                            "算路失败", Toast.LENGTH_SHORT).show();
                    break;
                case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                    Toast.makeText(OpenNaviActivity.this.getApplicationContext(),
                            "算路成功准备进入导航", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(OpenNaviActivity.this, DemoGuideActivity.class);
                    startActivity(it);
                    break;
                default:
                    // nothing
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_navi);
        startService(new Intent(this, ForegroundService.class));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                // 若未授权则请求权限
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 0);
            }
        }
        initBroadCastReceiver();

        button = findViewById(R.id.startNavi);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {

                    routePlanToNavi(null);
                }
            }
        });

        initPermission();
    }

    private void initPermission() {
        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                requestPermissions(authBaseArr, authBaseRequestCode);
            }
        }
    }

    private boolean hasBasePhoneAuth() {
        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager
                    .PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void routePlanToNavi(final Bundle bundle) {
        BNRoutePlanNode startNode = new BNRoutePlanNode.Builder()
                .latitude(40.041690)
                .longitude(116.306333)
                .name("百度大厦")
                .description("百度大厦")
                .build();
        BNRoutePlanNode endNode = new BNRoutePlanNode.Builder()
                .latitude(39.908560)
                .longitude(116.397609)
                .name("北京天安门")
                .description("北京天安门")
                .build();
        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(startNode);
        list.add(endNode);

        // 关闭电子狗
        if (BaiduNaviManagerFactory.getCruiserManager().isCruiserStarted()) {
            BaiduNaviManagerFactory.getCruiserManager().stopCruise();
        }
        BaiduNaviManagerFactory.getRoutePlanManager().routePlanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
                bundle, handler);
    }

    private void initBroadCastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.navi.ready");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive: initBroadCastReceiver");
                BNDemoFactory.getInstance().initCarInfo();
                BNDemoFactory.getInstance().initRoutePlanNode();
            }
        };
        registerReceiver(mReceiver, filter);
    }
}
