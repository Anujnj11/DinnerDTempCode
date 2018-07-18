package com.example.anuj_nj.mytestapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RandomFoodAdapter extends RecyclerView.Adapter<RandomFoodAdapter.RandomFoodViewHolder> {

    private ArrayList<String> objFoodType = null;
    @Override
    public RandomFoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     View objV= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_foodtype,parent,false);
        RandomFoodViewHolder ObjRandomFoodViewHolder = new  RandomFoodViewHolder(objV);
        return ObjRandomFoodViewHolder;
    }

    public RandomFoodAdapter(ArrayList<String> objarray)
    {
        objFoodType = objarray;
    }

    @Override
    public void onBindViewHolder(RandomFoodViewHolder holder, int position)
    {
        holder.ObjFoodName.setText(objFoodType.get(position));
    }

    @Override
    public int getItemCount() {
        return objFoodType.size();
    }

    public static  class RandomFoodViewHolder extends  RecyclerView.ViewHolder{
        private TextView ObjFoodName;
        public RandomFoodViewHolder(View itemView) {
            super(itemView);
            ObjFoodName = itemView.findViewById(R.id.FoodName);
        }
    }
}
