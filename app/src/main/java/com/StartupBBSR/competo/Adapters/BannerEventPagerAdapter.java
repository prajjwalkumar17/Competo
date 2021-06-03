package com.StartupBBSR.competo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.StartupBBSR.competo.Fragments.FeedMainFragment;
import com.StartupBBSR.competo.Models.BannerEvent;
import com.StartupBBSR.competo.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class BannerEventPagerAdapter extends PagerAdapter {


    Context context;
    List<BannerEvent> FeedBannerList;

    public BannerEventPagerAdapter(Context context, List<BannerEvent> FeedEventList) {
        this.context = context;
        this.FeedBannerList = FeedEventList;
    }

    @Override
    public int getCount() {
        return FeedBannerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_event, null);


        ImageView bannerImage = view.findViewById(R.id.imageView);
        //here we are using glide library for fetching image from url and set it to large view
        //lets add Glide dependency

        Glide.with(context).load(FeedBannerList.get(position).getImageUrl()).into(bannerImage);
        container.addView(view);
        return view;
    }
}
