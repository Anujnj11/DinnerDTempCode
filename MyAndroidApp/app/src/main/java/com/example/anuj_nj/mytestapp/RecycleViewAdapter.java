package com.example.anuj_nj.mytestapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Random;

/**
 * Created by Anuj_nj on 08-02-2018.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Result> mData;
    SharedPreferences prefs = null;
    private static final String[] Key =  {"AIzaSyAkL7MteqiUjMsqjdLIkMWEMEKlmmzXpfQ","AIzaSyBoP3sh2QzKR7riXti160fShNR8SUqArVo"};

    public RecycleViewAdapter(Context mContext, List<Result> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        prefs = mContext.getSharedPreferences("LocationCoordinates", Context.MODE_PRIVATE);
        Log.i("LocationCoordinates","latitude:" + prefs.getString("latitude", "") + "longitude:" +  prefs.getString("longitude", ""));
        ;
        //Fetch it
        // The second parameter is the default value.
        if(mData.get(position).getGeometry().getLocation().getLat() !=null && mData.get(position).getGeometry().getLocation().getLng() !=null)
//            holder.resturant_away.setText(String.format("%.2f",distance(mData.get(position).getGeometry().getLocation().getLat(),mData.get(position).getGeometry().getLocation().getLng(),Double.parseDouble(prefs.getString("latitude", "")),
//                    Double.parseDouble(prefs.getString("longitude", ""))))+" KM");
        holder.resturant_away.setText(String.format("%.2f",distance(Double.parseDouble(prefs.getString("latitude", "")),
                Double.parseDouble(prefs.getString("longitude", "")),mData.get(position).getGeometry().getLocation().getLat(),mData.get(position).getGeometry().getLocation().getLng()
               ))+" Km");
        holder.resturant_title.setText(mData.get(position).getName());
        holder.resturant_formatted_address.setText(mData.get(position).getFormattedAddress());
//        holder.resturant_formatted_address.setText(mData.get(position).getVicinity());

            if(mData.get(position).getRating()!=null)
                    holder.resturant_rating.setRating(Float.parseFloat(mData.get(position).getRating()));
        if(mData.get(position).getOpeningHours()!=null && mData.get(position).getOpeningHours().getOpenNow()!=null ) {
            String Open_now =  mData.get(position).getOpeningHours().getOpenNow().toString() == "true" ? "Yes" : "No";
            holder.resturant_hours.setText(Open_now);

            if(mData.get(position).getPhotos() !=null) {
                RequestOptions requestOptions = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // because file name is always same
                        .skipMemoryCache(true)
                        .override(120, 80)
                        .centerCrop();
//                Log.i("Image_Url:", "https://maps.googleapis.com/maps/api/place/photo?sensor=true&key=" + Key[new Random().nextInt(Key.length)] +
//                        "&maxwidth=120&maxheight=80&photoreference=" + mData.get(position).getPhotos().get(0).getPhotoReference());
                Glide.with(mContext)
                        .load("https://maps.googleapis.com/maps/api/place/photo?key=" + Key[new Random().nextInt(Key.length)] +
                                "&maxwidth=150&maxheight=80&photoreference=" + mData.get(position).getPhotos().get(0).getPhotoReference())
                        .apply(requestOptions)
                        .into(holder.resturant_ImageId);
            }
        }
            //Set Click Event Listner
        holder.objCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //passing Placeid to fetch details
                Intent objGetResturanteById = new Intent(mContext,Resturante_By_Id.class);
                objGetResturanteById.putExtra("PlaceId",mData.get(position).getPlaceId());
                if(mData.get(position).getRating()!=null)
                    objGetResturanteById.putExtra("Rating",mData.get(position).getRating());

                objGetResturanteById.putExtra("LocationName",prefs.getString("LocationName", ""));
                objGetResturanteById.putExtra("ResturantName",mData.get(position).getName());
                mContext.startActivity(objGetResturanteById);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView resturant_formatted_address;
        TextView resturant_title;
        RatingBar resturant_rating;
        CardView objCardview;
        TextView resturant_hours;
        TextView resturant_away;
        ImageView resturant_ImageId;

        public MyViewHolder(View iteView){
            super(iteView);
            resturant_formatted_address  = (TextView) iteView.findViewById(R.id.Restuarnt_formatted_address);
            resturant_title  = (TextView) iteView.findViewById(R.id.Restuarnt_Name);
            resturant_rating  = (RatingBar) iteView.findViewById(R.id.Restuarnt_rating);
            resturant_hours  = (TextView) iteView.findViewById(R.id.Restuarnt_hours);
            objCardview  = (CardView) iteView.findViewById(R.id.cardRestId);
            resturant_away  = (TextView) iteView.findViewById(R.id.away_value);
            resturant_ImageId = (ImageView) iteView.findViewById(R.id.Resturant_ImageId);
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
