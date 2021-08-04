package com.havefun.bhwsandroidstudy.map;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWTTSPlayer;
import com.baidu.platform.comjni.jninative.tts.WNaviTTSPlayer;

public class WNaviGuideActivity extends AppCompatActivity {
    private static final String TAG = "WNaviGuideActivity";

    private WalkNavigateHelper mNaviHelper;
    private WNaviTTSPlayer ttsPlayer;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNaviHelper = WalkNavigateHelper.getInstance();
        ttsPlayer = new WNaviTTSPlayer();
        View view = mNaviHelper.onCreate(this);
        if (view != null) {
            setContentView(view);
        }

        initSpeech();
        mNaviHelper.setTTsPlayer(new IWTTSPlayer() {
            @Override
            public int playTTSText(final String s, boolean b) {
                Log.d(TAG, "playTTSText: s: " + s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech.speak(s,TextToSpeech.QUEUE_ADD,null,"UniqueID");
                    }
                });
                return 0;
            }
        });

        boolean startResult = mNaviHelper.startWalkNavi(WNaviGuideActivity.this);
    }

    private void initSpeech() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.d(TAG, "onInit: "  + status);
                textToSpeech.speak("我爱你",TextToSpeech.QUEUE_ADD,null,"UniqueID");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNaviHelper.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNaviHelper.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNaviHelper.quit();
    }
}
