package com.havefun.bhwsandroidstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import io.reactivex.Flowable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView inner = findViewById(R.id.tvInner);
        inner.setTranslationZ(50);
        TextView inner2 = findViewById(R.id.tvInner2);
        inner2.setTranslationZ(20);
        TextView Second = findViewById(R.id.tvSecond);
        TextView third = findViewById(R.id.tvThird);

        String s = new String("abc");
        String s1 = new String("abc");
        System.out.println(s == s1);

        String s3= "abc";
        String s4 = "abc";
        System.out.println(s3 == s4);
        int a = 1; int b = 2;a=b;

        System.out.println(a);

        String c= "a";String d = "b";c=d;
        System.out.println(c);

    }
}