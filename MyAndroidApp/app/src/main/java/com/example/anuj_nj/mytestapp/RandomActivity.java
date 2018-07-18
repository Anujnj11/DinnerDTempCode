package com.example.anuj_nj.mytestapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class RandomActivity extends AppCompatActivity {
    private ArrayList<String> objFoodType = new ArrayList<String>();
    private RecyclerView objRecyclerView;
    private RecyclerView.Adapter objAdapter;
    private RecyclerView.LayoutManager objLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);
        objFoodType.add("Chinese");
        objFoodType.add("Mexican");
        objFoodType.add("Veg Food");
        objFoodType.add("KFC");
        objFoodType.add("McDonald's");


        buildRecyclerView();


    }


    public  void AddFoodItem(View v){
        objFoodType.add(objFoodType.size(),"Added New");
//        objAdapter.notifyDataSetChanged();
        objAdapter.notifyItemInserted(objFoodType.size() + 1 );
    }

    public  void InsertFood(String foodname){

    }

    private void buildRecyclerView() {
        objRecyclerView=(RecyclerView)findViewById(R.id.foodTypeId);
        objRecyclerView.setHasFixedSize(true);
        objLayoutManager = new LinearLayoutManager(this);
        objAdapter = new RandomFoodAdapter(objFoodType);
        objRecyclerView.setLayoutManager(objLayoutManager);
        objRecyclerView.setAdapter(objAdapter);
    }
}
