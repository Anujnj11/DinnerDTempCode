package com.example.anuj_nj.mytestapp;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import com.example.anuj_nj.resturantdetailsbyid.*;

/**
 * Created by Anuj_nj on 03-02-2018.
 */

public class GooglePlaceAPI {
//    private static  final  String url = "https://maps.googleapis.com/maps/api/place/";
    private static  final  String Key = "AIzaSyAkL7MteqiUjMsqjdLIkMWEMEKlmmzXpfQ";
//    private static  final  String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/";
    private static  final  String url = "https://maps.googleapis.com/maps/api/place/textsearch/";
    private static  final  String PlaceDetailsUrl = "https://maps.googleapis.com/maps/api/place/details/";
    private static  final  String Zomatourl = "https://developers.zomato.com/api/v2.1/";
    public static  PostService objPostService = null;
//    public static  PostService objPostServiceZomato = null;
    public static  PostService objPostResturantDetails= null;

    public  static  PostService getServiceData(){
        if(objPostService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            objPostService = retrofit.create(PostService.class);
        }
        return objPostService;
    }


//    public  static  PostService getCuisineLocation(){
//        if(objPostServiceZomato == null){
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(Zomatourl)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//            objPostServiceZomato = retrofit.create(PostService.class);
//        }
//        return objPostServiceZomato;
//    }

    public  static  PostService getPlaceDetails(){
        if(objPostResturantDetails == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(PlaceDetailsUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            objPostResturantDetails = retrofit.create(PostService.class);
        }
        return objPostResturantDetails;
    }

    public  interface PostService{

        @GET("textsearch/json?query=resturante+near+kandivali&radius=10000&key=" + Key)
        Call<PlaceDetails> getPlaceList();

        @GET("json?&query=Pav+bhaji&radius=5000&types=restaurant&key=" + Key)
        Call<PlaceDetails> getByLocation(@Query("location") String Cordinates);

        @GET("json?radius=6000")
        Call<PlaceDetails> getByTextSearchAndLocation(@Query("query") String LocationName,@Query("key") String Key);

//        @Headers("user-key: e34a9e5d23e6af5f3718f8e0e4746d69")
//        @GET("cuisines")
//        Call<ZomatoCuisine> getCuisineByLocation(@Query("lat") Double lat,@Query("lon") Double lon);

        @GET("json")
        Call<RestaurantsDetails> getRestaurantsDetailsById(@Query("placeid") String PlaceId,@Query("key") String Key);

    }
}
