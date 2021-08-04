package com.havefun.bhwsandroidstudy.resolve_conflict;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerAdapter;

public class MyBanner<T, BA extends BannerAdapter> extends Banner<T, BA> {
    private static final String TAG = "MyBanner";

    public MyBanner(Context context) {
        super(context);
    }

    public MyBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    float startX = 0;
    float startY = 0;

    boolean flag = false;
    View child;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        child = getChildAt(0);

    }

   /* @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        float x = ev.getX();
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = x;
                startY = y;
                Log.d(TAG, "dispatchTouchEvent: DOWN ");
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = Math.abs(startX - x);
                float moveY = Math.abs(startY - y);
                Log.d(TAG, "dispatchTouchEvent: moveX: "  + moveX + " moveY" + moveY);
                if (moveX < moveY) {
                    Log.d(TAG, "dispatchTouchEvent: 拦截");
//                    getParent().requestDisallowInterceptTouchEvent(true);
                    super.dispatchTouchEvent(ev);
                    return true;
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
        boolean result = super.dispatchTouchEvent(ev);
        Log.d(TAG, "dispatchTouchEvent: result: " + result);
        return result;
    }*/


}
