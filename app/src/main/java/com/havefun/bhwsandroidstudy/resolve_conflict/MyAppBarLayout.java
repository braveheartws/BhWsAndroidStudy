package com.havefun.bhwsandroidstudy.resolve_conflict;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.AppBarLayout;

public class MyAppBarLayout extends AppBarLayout {
    private static final String TAG = "MyAppBarLayout";
    public MyAppBarLayout(@NonNull Context context) {
        super(context);
    }

    public MyAppBarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyAppBarLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }


    float startX = 0;
    float startY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = x;
                startY = y;
                Log.d(TAG, "onInterceptTouchEvent: startY: " + startY);
                return false;
            case MotionEvent.ACTION_MOVE:
                float moveX = Math.abs(startX - x);
                float moveY = Math.abs(startY - y);
                Log.d(TAG, "dispatchTouchEvent: moveX: "  + moveX + " moveY" + moveY);
                if (moveX < moveY && startY < 800) {
                    Log.d(TAG, "dispatchTouchEvent: 拦截");

                } else {
                    Log.d(TAG, "dispatchTouchEvent: 不要拦截");

                }

                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "dispatchTouchEvent: UP");

                break;
            default:
                break;
        }
        return true;
    }

}
