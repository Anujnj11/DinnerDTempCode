package com.example.anuj_nj.mytestapp;

import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Anuj_nj on 23-02-2018.
 */

public class RecyclerViewZomatoAdapter extends RecyclerView.Adapter<RecyclerViewZomatoAdapter.MyViewHolder>
{
    private Context mContext;
//    private List<Cuisine> mData;
    private List<FoodType> mData;

//    public RecyclerViewZomatoAdapter(Context mContext, List<Cuisine> mData) {
//        this.mContext = mContext;
//        this.mData = mData;
//    }
    public RecyclerViewZomatoAdapter(Context mContext, List<FoodType> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public RecyclerViewZomatoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_cuisines,parent,false);
        return new RecyclerViewZomatoAdapter.MyViewHolder(view);
    }

    @Override
//    public void onBindViewHolder(MyViewHolder holder,final int position) {
//        holder.objCuisines_name.setText(mData.get(position).getCuisine().getCuisineName());
//        holder.objImageViewFoodType.setImageResource(R.mipmap.temp);
//        holder.objImageViewFoodType.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        //Set Click Event Listner
//        holder.objcardCuisine.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //passing Placeid to fetch details
//                Intent objGetCusineType= new Intent(mContext,LocationBasedActivity.class);
//                objGetCusineType.putExtra("CuisineType",mData.get(position).getCuisine().getCuisineName());
//                mContext.startActivity(objGetCusineType);
//            }
//        });
//    }

    public void onBindViewHolder(MyViewHolder holder,final int position) {
        holder.objCuisines_name.setText(mData.get(position).getFoodType());
        int res = mContext.getResources().getIdentifier(mData.get(position).getImageUrl(), "mipmap", mContext.getPackageName());
        holder.objImageViewFoodType.setImageResource(res);
//        holder.objImageViewFoodType.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //Set Click Event Listner
        holder.objcardCuisine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //passing Placeid to fetch details
                Intent objGetCusineType= new Intent(mContext,LocationBasedActivity.class);
                objGetCusineType.putExtra("CuisineType",mData.get(position).getFoodTypeSearch());
                mContext.startActivity(objGetCusineType);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView objCuisines_name;
        CardView objcardCuisine;
        ImageView objImageViewFoodType;

        public MyViewHolder(View iteView){
            super(iteView);
            objCuisines_name  = (TextView) iteView.findViewById(R.id.Cuisines_name);
            objcardCuisine  = (CardView) iteView.findViewById(R.id.cardCuisine);
            objImageViewFoodType  = (ImageView) iteView.findViewById(R.id.Foodtype_ImageId);

        }
    }
}
