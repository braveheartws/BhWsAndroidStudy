package com.havefun.bhwsandroidstudy.resolve_conflict;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

public class CustomBanner extends FrameLayout {
    private static final String TAG = "CustomBanner";
    public CustomBanner(@NonNull Context context) {
        super(context);
    }

    public CustomBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


    }

    public CustomBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    ViewPager2 viewPager2;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        viewPager2 = (ViewPager2) getChildAt(0);
    }

    float startX = 0;
    float startY = 0;

    boolean flag = false;


    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        *//*getParent().requestDisallowInterceptTouchEvent(true);
        float x = ev.getX();
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = x;
                startY = y;
                Log.d(TAG, "dispatchTouchEvent: DOWN ");
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = Math.abs(startX - x);
                float deltaY = Math.abs(startY - y);
                Log.d(TAG, "dispatchTouchEvent: ACTION_MOVE " + deltaX + " | "+ deltaY);
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "dispatchTouchEvent: ACTION_UP ");
                break;
            default:
                break;
        }
        boolean result = super.dispatchTouchEvent(ev);
        Log.d(TAG, "dispatchTouchEvent: result :  " + result);
        return true;*//*

        float x = ev.getX();
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = x;
                startY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = Math.abs(startX - x);
                float moveY = Math.abs(startY - y);
                Log.d(TAG, "dispatchTouchEvent: moveX: "  + moveX + " moveY" + moveY);
                if (moveX < moveY) {
                    Log.d(TAG, "dispatchTouchEvent: 拦截");
                    getParent().requestDisallowInterceptTouchEvent(true);
                    viewPager2.dispatchTouchEvent(ev);

                } else {
                    Log.d(TAG, "dispatchTouchEvent: 不要拦截");
                    flag =  super.dispatchTouchEvent(ev);
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "dispatchTouchEvent: UP");
                flag = super.dispatchTouchEvent(ev);
                break;
            default:
                break;
        }
        return true;
    }*/
}
