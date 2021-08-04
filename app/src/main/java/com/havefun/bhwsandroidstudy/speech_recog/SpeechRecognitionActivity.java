package com.havefun.bhwsandroidstudy.speech_recog;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.havefun.bhwsandroidstudy.R;
import com.havefun.bhwsandroidstudy.speech_recog.core.AutoCheck;
import com.havefun.bhwsandroidstudy.speech_recog.core.IStatus;
import com.havefun.bhwsandroidstudy.speech_recog.core.MyRecognizer;
import com.havefun.bhwsandroidstudy.speech_recog.core.listener.IRecogListener;
import com.havefun.bhwsandroidstudy.speech_recog.core.listener.MessageStatusRecogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SpeechRecognitionActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SpeechRecognitionActivi";

    protected Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case IStatus
                        .STATUS_FINISHED:
                    Toast.makeText(SpeechRecognitionActivity.this, "识别结果:   +" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Log.d(TAG, "handleMessage: msg: " + msg.what + " msg: " + msg.obj);
                    break;
            }

        }
    };
    protected Button btnStart;
    protected TextView tvLog;
    protected boolean enableOffline = true;

    /**
     * 识别控制器，使用MyRecognizer控制识别的流程
     */
    protected MyRecognizer myRecognizer;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recognition);

        initView();
        initListener();
        initSpeechSDK();

        initPermission();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initPermission() {
        int permission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            //同意
        } else {
            requestPermissions(new String[]{
                    Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE
            }, 100);
        }
    }

    private void initView() {
        btnStart = findViewById(R.id.btnStart);
        tvLog = findViewById(R.id.tvLog);
    }

    private void initListener() {
        btnStart.setOnClickListener(this);
    }

    private void initSpeechSDK() {
        //1.1 初始化EventManager对象

        IRecogListener listener = new MessageStatusRecogListener(handler);
        myRecognizer = new MyRecognizer(this, listener);


    }


    @Override
    public void onClick(View v) {
        start();
    }

    private void start() {
        final Map<String, Object> params = new HashMap<>();
        params.put("KEY", "VALUE");
        // params 也可以根据文档此处手动修改，参数会以json的格式在界面和logcat日志中打印
        Log.i(TAG, "设置的start输入参数：" + params);
        // 复制此段可以自动检测常规错误
        (new AutoCheck(getApplicationContext(), new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
                        tvLog.append(message + "\n");
                        ; // 可以用下面一行替代，在logcat中查看代码
                        // Log.w("AutoCheckMessage", message);
                    }
                }
            }
        }, enableOffline)).checkAsr(params);

        // 这里打印出params， 填写至您自己的app中，直接调用下面这行代码即可。
        // DEMO集成步骤2.2 开始识别
        myRecognizer.stop();
        myRecognizer.start(params);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

}
