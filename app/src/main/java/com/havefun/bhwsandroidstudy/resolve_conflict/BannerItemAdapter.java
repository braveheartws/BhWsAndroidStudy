package com.havefun.bhwsandroidstudy.resolve_conflict;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

public class BannerItemAdapter extends BannerAdapter<String, ImageHolder> {

    public BannerItemAdapter(List<String> datas) {
        super(datas);
    }


    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    @Override
    public ImageHolder onCreateHolder(ViewGroup parent, int viewType) {
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
    public void onBindViewHolder( ImageHolder holder, int position,  List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindView(ImageHolder holder, String data, int position, int size) {
        Glide.with(holder.imageView).load(data).into(holder.imageView);
    }
}
