package com.example.anuj_nj.mytestapp

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
//import retrofit2.Call

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     //  Fixed Portrait orientation
        GetDataId.setOnClickListener {
           val GetData =  EmailId.text.toString();
           // NameView.text = GetData;
            //TextValue.text.clear();
            Toast.makeText(this,"Welcome " + GetData,Toast.LENGTH_LONG).show();
        }



    }
    


}
