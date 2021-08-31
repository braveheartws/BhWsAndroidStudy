package com.havefun.bhwsandroidstudy.alphaanim;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.havefun.bhwsandroidstudy.R;

public class AlphaAnimActivity extends AppCompatActivity {

    private Button btnShow;
    private Button btnHide;
    private ImageView imageView;
    private EditText editText;
    private AlphaAnimation machineBgAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alpha_anim);

        imageView = findViewById(R.id.imageView);

        btnShow = findViewById(R.id.btnShow);
        btnHide = findViewById(R.id.btnHide);
        editText = findViewById(R.id.editText);

        initAnim();

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(AlphaAnimActivity.this);
            }
        });

        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.clearFocus();
            }
        });

    }

    private void initAnim() {
        machineBgAnim = new AlphaAnimation(1.0f, 0.5f);
        //设置动画无限次播放
        machineBgAnim.setRepeatCount(Animation.INFINITE);
        //不透明 -》 透明 -》 不透明 的方式
        machineBgAnim.setRepeatMode(Animation.REVERSE);
        machineBgAnim.setDuration(1000);
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }
}