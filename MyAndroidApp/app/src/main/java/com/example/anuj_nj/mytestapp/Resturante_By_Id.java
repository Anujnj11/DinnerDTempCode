package com.example.anuj_nj.mytestapp;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.anuj_nj.resturantdetailsbyid.*;
import com.bumptech.glide.annotation.GlideModule;
public class Resturante_By_Id extends AppCompatActivity {
    //Google Key
    private static final String[] Key =  {"AIzaSyAkL7MteqiUjMsqjdLIkMWEMEKlmmzXpfQ","AIzaSyBoP3sh2QzKR7riXti160fShNR8SUqArVo"};

    TextView ObjresturanAdd,Objopennowvalue,ObjresturanName,ObjReview1,ObjReview1Comment,ObjReview2,ObjReview2Comment,ObjReview3,ObjReview3Comment,ObjOpenNowValue,ObjWebsitelbl;
    ImageView ObjCallMe,ObjimageView,ObjReview1Image1,ObjReview1Image2,ObjReview1Image3,ObjWebsite;
    RatingBar   ObjRestuarnt_rating;
    NestedScrollView ObjnestedDetails;
    Intent objPassedData = null;
    String PhoneNumber=null;
    Dialog myDialog;
    String ReqType,Direction,Website = null;
    ViewPager ObjviewPager;
    List<String> ResturantImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   if(Build.VERSION.SDK_INT>16){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // }
        setContentView(R.layout.activity_resturante__by__id);
        myDialog = new Dialog(this);
        //Recieve Data
        objPassedData = getIntent();
        String PlaceId = objPassedData.getExtras().getString("PlaceId");
        Log.i("PlaceId:", PlaceId);
        //Set Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Tranparent ACtion BAR
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getSupportActionBar().setTitle("");

        ObjresturanName = (TextView)findViewById(R.id.resturanName);
        ObjresturanAdd = (TextView)findViewById(R.id.resturanAdd);
        Objopennowvalue = (TextView)findViewById(R.id.opennowvalue);
        ObjReview1Comment = (TextView)findViewById(R.id.Review1Comment);
        ObjOpenNowValue = (TextView)findViewById(R.id.OpenNowValue);
        ObjReview1 = (TextView)findViewById(R.id.Review1);
        ObjReview2Comment = (TextView)findViewById(R.id.Review2Comment);
        ObjReview2 = (TextView)findViewById(R.id.Review2);
        ObjReview3Comment = (TextView)findViewById(R.id.Review3Comment);
        ObjReview3 = (TextView)findViewById(R.id.Review3);
        ObjWebsitelbl= (TextView)findViewById(R.id.Websitelbl);



        ObjresturanName.setText(objPassedData.getExtras().getString("ResturantName"));

        ObjReview1Image1  = (ImageView) findViewById(R.id.Review1Image1);
        ObjReview1Image2  = (ImageView) findViewById(R.id.Review1Image2);
        ObjReview1Image3  = (ImageView) findViewById(R.id.Review1Image3);
//        ObjimageView = (ImageView) findViewById(R.id.imageView);
        ObjCallMe= (ImageView) findViewById(R.id.CallMe);
        ObjWebsite= (ImageView) findViewById(R.id.Website);

        //ViewPager
        ObjviewPager = (ViewPager) findViewById(R.id.imageView);




        ObjRestuarnt_rating = (RatingBar)findViewById(R.id.Restuarnt_rating);
        if(objPassedData.getExtras().getString("Rating")!=null)
            ObjRestuarnt_rating.setRating(Float.parseFloat(objPassedData.getExtras().getString("Rating")));




        getPlaceDetails(PlaceId);

        ObjnestedDetails = (NestedScrollView)findViewById(R.id.nestedDetails);



        ObjnestedDetails.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = ObjnestedDetails.getScrollY(); // For ScrollView
                if(scrollY >= 240) {
                    //show ACTION BAR
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
                    getSupportActionBar().setTitle(objPassedData.getExtras().getString("ResturantName"));

                    //show  STATUS BAR
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);



                }else{
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    getSupportActionBar().setTitle("");

                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
//                int scrollX = ObjnestedDetails.getScrollX();// For HorizontalScrollView
//                int sss =     ObjnestedDetails.getHeight();
//                Log.i("scrollY", Integer.toString(scrollY));
//                Log.i("sss", Integer.toString(sss));
            }
        });
    }




    //Set Back Button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //End this activity
        if(id==android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }

    private  void getPlaceDetails(String PlaceId){
        Toast.makeText(getApplicationContext(), "Calling api", Toast.LENGTH_SHORT).show();
        Call<RestaurantsDetails> objRestaurantsDetails =
                GooglePlaceAPI.getPlaceDetails().getRestaurantsDetailsById(PlaceId,Key[new Random().nextInt(Key.length)]);
        objRestaurantsDetails.enqueue(new Callback<RestaurantsDetails>() {
            @Override
            public void onResponse(Call<RestaurantsDetails> call, Response<RestaurantsDetails> response){
                RestaurantsDetails objresult = response.body();
                ObjresturanAdd.setText(objresult.getResult().getVicinity());
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Date d = new Date();
                String dayOfTheWeek = sdf.format(d);
                int Count = dayOfTheWeek.equalsIgnoreCase("Monday") ? 0 : dayOfTheWeek.equalsIgnoreCase("Tuesday" ) ? 1
                        : dayOfTheWeek.equalsIgnoreCase("Wednesday") ? 2 : dayOfTheWeek.equalsIgnoreCase("Thursday")? 3
                        : dayOfTheWeek.equalsIgnoreCase("Friday") ?  4 : dayOfTheWeek.equalsIgnoreCase("Saturday")? 5 : 6;
                if(objresult.getResult().getOpeningHours()!=null) {
                   String IsOpen = objresult.getResult().getOpeningHours().getOpenNow().toString() == "true" ? "Yes" : "No";
                    Objopennowvalue.setText("(" + IsOpen + ") " + objresult.getResult().getOpeningHours().getWeekdayText().get(Count));
                }
                if(objresult.getResult().getReviews()!=null) {
                    int ReviewCount = objresult.getResult().getReviews().size();
                    Log.i("RESCount", Integer.toString(ReviewCount));
                    if (ReviewCount >= 3) {
                        ObjReview1.setText(objresult.getResult().getReviews().get(0).getAuthorName());
                        ObjReview1Comment.setText(objresult.getResult().getReviews().get(0).getText());
                        ObjReview2.setText(objresult.getResult().getReviews().get(1).getAuthorName());
                        ObjReview2Comment.setText(objresult.getResult().getReviews().get(1).getText());
                        ObjReview3.setText(objresult.getResult().getReviews().get(2).getAuthorName());
                        ObjReview3Comment.setText(objresult.getResult().getReviews().get(2).getText());
                    } else if (ReviewCount == 2) {
                        ObjReview1.setText(objresult.getResult().getReviews().get(0).getAuthorName());
                        ObjReview1Comment.setText(objresult.getResult().getReviews().get(0).getText());
                        ObjReview2.setText(objresult.getResult().getReviews().get(1).getAuthorName());
                        ObjReview2Comment.setText(objresult.getResult().getReviews().get(1).getText());
                        ObjReview1Image3.setVisibility(View.GONE);
                        ObjReview3.setVisibility(View.GONE);
                        ObjReview3Comment.setVisibility(View.GONE);
                    }
                    else if (ReviewCount == 1) {
                        ObjReview1.setText(objresult.getResult().getReviews().get(0).getAuthorName());
                        ObjReview1Comment.setText(objresult.getResult().getReviews().get(0).getText());
                        ObjReview1Image2.setVisibility(View.GONE);
                        ObjReview2.setVisibility(View.GONE);
                        ObjReview2Comment.setVisibility(View.GONE);
                        ObjReview1Image3.setVisibility(View.GONE);
                        ObjReview3.setVisibility(View.GONE);
                        ObjReview3Comment.setVisibility(View.GONE);
                    }
                }
                else{
                    ObjReview1Image1.setVisibility(View.GONE);
                    ObjReview1.setVisibility(View.GONE);
                    ObjReview1Comment.setVisibility(View.GONE);
                    ObjReview1Image2.setVisibility(View.GONE);
                    ObjReview2.setVisibility(View.GONE);
                    ObjReview2Comment.setVisibility(View.GONE);
                    ObjReview1Image3.setVisibility(View.GONE);
                    ObjReview3.setVisibility(View.GONE);
                    ObjReview3Comment.setVisibility(View.GONE);
                }
                if(objresult.getResult().getPhotos()!=null) {
                    Log.i("Photos", Integer.toString(objresult.getResult().getPhotos().size()));
                    ImageDownload(objresult);
                }
                Log.i("Count", Integer.toString(Count));
                if(objresult.getResult().getOpeningHours()!=null && objresult.getResult().getOpeningHours().getWeekdayText()!=null) {
                    StringBuilder sb = new StringBuilder();
                    for (int j=0; j<7; j++) {
                        // chain each string, separated with a new line
                        int IndexofY= objresult.getResult().getOpeningHours().getWeekdayText().get(j).indexOf("y");
                        String Timing = objresult.getResult().getOpeningHours().getWeekdayText().get(j).substring(IndexofY + 1
                                ,objresult.getResult().getOpeningHours().getWeekdayText().get(j).length());//Second string
                        String Day = objresult.getResult().getOpeningHours().getWeekdayText().get(j).substring(0,IndexofY);//First string
                        Day = Day.substring(0,3);
                       if(j == Count)
                           sb.append("<font color=#605e5f>" + Day + "  " + Timing.trim() +"</font>" + "<br/>");
                       else
                               sb.append(Day + "  " +Timing.trim() +"<br/>");
                    }
                    ObjOpenNowValue.setText(Html.fromHtml(sb.toString()));
                }



                PhoneNumber = objresult.getResult().getFormattedPhoneNumber() !=null ? objresult.getResult().getFormattedPhoneNumber() : "";
                Direction = objresult.getResult().getUrl() !=null ? objresult.getResult().getUrl() : "";
                Website = objresult.getResult().getWebsite() !=null ? objresult.getResult().getWebsite(): "";
                if(Website == ""){
                    ObjWebsite.setVisibility(View.GONE);
                    ObjWebsitelbl.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<RestaurantsDetails> call, Throwable t){
                Log.i("myApp", "error:"+t);
                Toast.makeText(Resturante_By_Id.this,"Error",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void ImageDownload(RestaurantsDetails objresult){
        // Creating an array of ImageDownloadTask to download photos
//        ImageDownloadTask[] imageDownloadTask = new ImageDownloadTask[objresult.getResult().getPhotos().get(0).getPhotoReference().length()];
        String url = "https://maps.googleapis.com/maps/api/place/photo?";
        String key = "key=" + Key[new Random().nextInt(Key.length)];
        String sensor = "sensor=true";
        url = url + "&" + key + "&" + sensor;
        ResturantImage = new ArrayList<String>();
        // Traversing through all the photoreferences
        for(int i=0;i<objresult.getResult().getPhotos().size();i++) {
            String maxWidth="maxwidth=" + objresult.getResult().getPhotos().get(i).getWidth();
            String maxHeight = "maxheight=" + objresult.getResult().getPhotos().get(i).getHeight();
            String photoReference = "photoreference=" + objresult.getResult().getPhotos().get(i).getPhotoReference();
            // URL for downloading the photo from Google Services
            String PhotoUrl = url +  "&" + maxWidth + "&" + maxHeight + "&" + photoReference;
//            Log.d("url", PhotoUrl);
            ResturantImage.add(PhotoUrl);
        }
        ViewPagerAdapter ObjViewPagerAdapter = new ViewPagerAdapter(ResturantImage,Resturante_By_Id.this);
        ObjviewPager.setAdapter(ObjViewPagerAdapter);
//        RequestOptions requestOptions = new RequestOptions()
//                .diskCacheStrategy(DiskCacheStrategy.ALL) // because file name is always same
//                .skipMemoryCache(true)
//                .centerCrop();
//
//        Glide.with(this)
//                    .load(url)
//                    .apply(requestOptions)
//                    .into(ObjimageView);

        // Downloading i-th photo from the above url
            //imageDownloadTask[0].execute(url);
//        }


    }

    private Bitmap downloadImage(String strUrl) throws IOException {
        Bitmap bitmap=null;
        InputStream iStream = null;
        try{
            URL url = new URL(strUrl);

            /** Creating an http connection to communcate with url */
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            /** Connecting to url */
            urlConnection.connect();

            /** Reading data from url */
            iStream = urlConnection.getInputStream();

            /** Creating a bitmap from the stream returned from the url */
            bitmap = BitmapFactory.decodeStream(iStream);

        }catch(Exception e){
            Log.d("Exception", e.toString());
        }finally{
            iStream.close();
        }
        return bitmap;
    }

    private class ImageDownloadTask extends AsyncTask<String, Integer, Bitmap> {
        Bitmap bitmap = null;
        @Override
        protected Bitmap doInBackground(String... url) {
            try{
                // Starting image download
                bitmap = downloadImage(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Creating an instance of ImageView to display the downloaded image
//            ImageView iView = new ImageView(getActivity().getBaseContext());

            // Setting the downloaded image in ImageView
//            ObjimageView.setImageBitmap(result);

            // Adding the ImageView to ViewFlipper
//            mFlipper.addView(iView);

        }
    }

    public void Submit(View v) {
        // does something very interesting
        if(ReqType.equalsIgnoreCase("CallReq")) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + PhoneNumber));
            startActivity(intent);
        }
        else if( ReqType.equalsIgnoreCase ("DirReq")){
            String uri = String.format(Locale.ENGLISH, Direction);
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }
        else if(ReqType.equalsIgnoreCase("WebReq")){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Website));
            startActivity(browserIntent);
        }
    }

//    public void ShowPopup(View v) {
//        ReqType = v.getTag().toString();
//        TextView txtclose,ObjMessage;
//        ImageView ObjMessageImage;
//        Button Objbtnfollow;
//        myDialog.setContentView(R.layout.custompopup);
//        ObjMessageImage = (ImageView) myDialog.findViewById(R.id.MessageImage);
//        Objbtnfollow = (Button) myDialog.findViewById(R.id.btnfollow);
//        ObjMessage = (TextView) myDialog.findViewById(R.id.Message);
//      int RequestType =   ReqType.equalsIgnoreCase("CallReq") ? R.mipmap.phonereceiver : ReqType.equalsIgnoreCase ("DirReq")
//                        ?  R.mipmap.direction : ReqType.equalsIgnoreCase("SavReq") ? R.mipmap.bookmark :  R.mipmap.website;
//
//      String Message = ReqType.equalsIgnoreCase("CallReq") ? "Make a call to " + objPassedData.getExtras().getString("ResturantName") +" ?" :
//              ReqType.equalsIgnoreCase ("DirReq") ?  "This require google map " : ReqType.equalsIgnoreCase("SavReq") ? ""
//                      :  "Open " + objPassedData.getExtras().getString("ResturantName") + " website?";
//
//        Message =  Message.contains("website") ? Website == "" ? "We are sorry we cannot find " + objPassedData.getExtras().getString("ResturantName") +" website"
//                    : Message : Message;
//        ObjMessage.setText(Message);
//        if(Message.contains("website") && Website == "")
//            Objbtnfollow.setVisibility(View.INVISIBLE);
//        else
//        Objbtnfollow.setVisibility(View.VISIBLE);
//        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
//        txtclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myDialog.dismiss();
//            }
//        });
//        ObjMessageImage.setImageResource(RequestType);
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog.show();
//    }


    public void ShowPopup(View v) {
        ReqType = v.getTag().toString();
        if(ReqType.equalsIgnoreCase("CallReq")) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + PhoneNumber));
            startActivity(intent);
        }
        else if( ReqType.equalsIgnoreCase ("DirReq")){
            String uri = String.format(Locale.ENGLISH, Direction);
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }
        else if(ReqType.equalsIgnoreCase("WebReq")){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Website));
            startActivity(browserIntent);
        }
    }

}
