package com.example.anuj_nj.mytestapp;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class HomeActivity extends AppCompatActivity implements  View.OnClickListener {

    private CardView LocationCard,RandomCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LocationCard = (CardView) findViewById(R.id.ByLocation);
        RandomCard = (CardView) findViewById(R.id.RandomCuisine);
        LocationCard.setOnClickListener(this);
        RandomCard.setOnClickListener(this);

    }

    @Override
    public void  onClick(View v){
    Intent i;
    switch (v.getId()){
//        case R.id.ByLocation : i = new Intent(this,LocationBasedActivity.class); startActivity(i); break;
        case R.id.ByLocation : i = new Intent(this,LocationBasedCuisine.class); startActivity(i); break;
        case R.id.RandomCuisine : i = new Intent(this,RandomActivity.class); startActivity(i); break;
        default:break;
    }
    }
}
