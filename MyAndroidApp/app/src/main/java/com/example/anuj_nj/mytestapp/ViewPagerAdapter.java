package com.example.anuj_nj.mytestapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by anuj on 18/3/18.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> ResturantImage;
//    private  Integer []images = {R.mipmap.chinese,R.mipmap.food,R.mipmap.chinese,R.mipmap.food};


    public ViewPagerAdapter(List<String>  ResturantImage,Context context) {
        this.ResturantImage = ResturantImage;
        this.context = context;

    }

    @Override
    public int getCount() {
        return ResturantImage.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
       layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View view  = layoutInflater.inflate(R.layout.viewpagerview,null);



        ImageView ObjImageView = (ImageView) view.findViewById(R.id.imageView1);

        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL) // because file name is always same
                .skipMemoryCache(true)
                .centerCrop();

        Glide.with(context)
                    .load(ResturantImage.get(position))
                    .apply(requestOptions)
                    .into(ObjImageView);


//        ObjImageView.setImageResource(images[position]);
        ViewPager ObjViewPager = ( ViewPager ) container;
        ObjViewPager.addView(view,0);
        return  view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager ObjViewPager = ( ViewPager ) container;
        View ObjView = (View) object;
        ObjViewPager.removeView(ObjView);
    }
}
