package com.havefun.bhwsandroidstudy.resolve_conflict;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.imageview.ShapeableImageView;
import com.havefun.bhwsandroidstudy.R;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class ResolveConflictActivity extends AppCompatActivity {
    private static final String TAG = "ResolveConflictActivity";

    private List<String> urls;
    private Banner banner;
    private ViewPager2 pager2;
    private AppBarLayout appBarLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resolve_conflict);


        urls = new ArrayList<>();
        urls.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        urls.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        urls.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
        urls.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");


        banner = findViewById(R.id.banner);
        banner.setOrientation(Banner.VERTICAL);
        banner.setAdapter(new BannerItemAdapter(urls));

        pager2 = findViewById(R.id.viewPager2);
        pager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        pager2.setAdapter(new Pager2Adapter());

        banner.getViewPager2().setUserInputEnabled(true);

        ViewPager2 pager2 = findViewById(R.id.viewPager2);
        pager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        pager2.setAdapter(new BannerItemAdapter(urls));

        appBarLayout = findViewById(R.id.appBarLayout);


        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator);

    }

    class Pager2Adapter extends RecyclerView.Adapter<ImageHolder> {


        @NonNull
        @Override
        public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ShapeableImageView imageView = new ShapeableImageView(parent.getContext());
            //注意，必须设置为match_parent，这个是viewpager2强制要求的
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return new ImageHolder(imageView);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
            Glide.with(holder.imageView)
                    .load("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg")
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }


}