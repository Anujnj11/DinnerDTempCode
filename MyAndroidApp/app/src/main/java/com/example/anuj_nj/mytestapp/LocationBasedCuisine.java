package com.example.anuj_nj.mytestapp;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.dom.DOMLocator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationBasedCuisine extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener,ResultCallback<LocationSettingsResult> {


//    private List<Cuisine> objResultZomato;
      private List<FoodType> objResultZomato;

    private RecyclerView rcObj;


    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 7172;
    private boolean mRequestingLocationUpdates = true;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private android.location.Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000; // SEC
    private static int FATEST_INTERVAL = 3000; // SEC
    private static int DISPLACEMENT = 10; // METERS
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    protected LocationSettingsRequest mLocationSettingsRequest;

    private  int ResturanteCount = 0;
    //    ProgressBar  ObjprogressBar;
    View ObjLoder,ObjNoLocationEnabled;

    TextView ObjClient_Location;

    ArrayList<FoodType> objFoodTypelist = new ArrayList<>();



    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices()) {
                        buildGoogleApiClient();
                        createLocationRequest();
//                        buildLocationSettingsRequest();
//                        checkLocationCustomSettings();
                    }
                }
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_based_cuisine);
        Init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //End this activity
        if(id==android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }

    private void Init(){

        //Set Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //RecyclerView Initization
        rcObj = (RecyclerView)findViewById(R.id.recycleView_Cuisine_Restaurant);
        rcObj.setHasFixedSize(true);
        rcObj.setLayoutManager(new LinearLayoutManager(LocationBasedCuisine.this));
//        objResultZomato = new ArrayList<>();



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //Toast.makeText(LocationBasedActivity.this,"Permission",Toast.LENGTH_LONG).show();

            //Run-time request permission
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        }
        else {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
                buildLocationSettingsRequest();
                checkLocationCustomSettings();
            }
        }
        //  ObjprogressBar = (ProgressBar)findViewById(R.id.loaderId);
        ObjLoder  = (View) findViewById(R.id.loaderIdLL);
        ObjNoLocationEnabled = (View) findViewById(R.id.locationNotAvailable);
        ObjNoLocationEnabled.setVisibility(View.INVISIBLE);
        ObjClient_Location = (TextView) findViewById(R.id.Client_Location);
//        ObjprogressBar.setVisibility(View.VISIBLE);
        GenerateListFoodType();
    }

//    private  void getZomatoAPIData(Double Lat, Double Lon){
//        Call<ZomatoCuisine> objPlaceDetails = GooglePlaceAPI.getCuisineLocation().getCuisineByLocation(Lat,Lon);
//        objPlaceDetails.enqueue(new Callback<ZomatoCuisine>() {
//            @Override
//            public void onResponse(Call<ZomatoCuisine> call, Response<ZomatoCuisine> response){
//                Log.i("myApp", "ZomatoCuisine : "+response.body());
//                ZomatoCuisine objresult = response.body();
//                objResultZomato.addAll(objresult.getCuisines());
//                ResturanteCount =  objResultZomato.size();
//                Log.i("dfsdfd","Response :" + objResultZomato.get(0));
//                rcObj.setItemAnimator(new DefaultItemAnimator());
//                //  ObjprogressBar.setVisibility(View.GONE);
//                ObjLoder.setVisibility(View.GONE);
//                ObjNoLocationEnabled.setVisibility(View.GONE);
//                objResultZomato = pickNRandom(objResultZomato, 20);
//                rcObj.setAdapter(new RecyclerViewZomatoAdapter(LocationBasedCuisine.this,objResultZomato));
//                // Toast.makeText(APIActivity.this,"Success",Toast.LENGTH_LONG).show();
//            }
//            @Override
//            public void onFailure(Call<ZomatoCuisine> call, Throwable t){
//                Log.i("myApp", "error:"+t);
//                Toast.makeText(LocationBasedCuisine.this,"Error",Toast.LENGTH_LONG).show();
//            }
//        });
//    }


    private  void RenderFoodType(){
        rcObj.setItemAnimator(new DefaultItemAnimator());
        ObjLoder.setVisibility(View.GONE);
        ObjNoLocationEnabled.setVisibility(View.GONE);
        objResultZomato = pickNRandomFoodType(objFoodTypelist, 19);
        rcObj.setAdapter(new RecyclerViewZomatoAdapter(LocationBasedCuisine.this,objResultZomato));
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
        if(mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if(ResturanteCount == 0){
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (null != listAddresses && listAddresses.size() > 0) {
                        String _Location = listAddresses.get(0).getSubLocality();
                        ObjClient_Location.setText("FOOD TYPE IN AND AROUND "+_Location);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RenderFoodType();
               // getZomatoAPIData(latitude,longitude);
            }
    }
    }
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        //Fix first time run app if permission doesn't grant yet so can't get anything
        mGoogleApiClient.connect();


    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(mRequestingLocationUpdates) {
            startLocationUpdates();
            Toast.makeText(this, "ResturanteCount :"+ ResturanteCount, Toast.LENGTH_SHORT).show();
//            displayLocation();
            //mRequestingLocationUpdates = false;
            //Toast.makeText(this,"Inside onConnect",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        mLastLocation = location;
        //  displayLocation();
    }

//    public  void CheckLocationSetting(){
//        //Set Flag false as Location is not enabled
//        mRequestingLocationUpdates = false;
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setTitle("Enable Location")
//                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
//                        "use this app")
//                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivity(myIntent);
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        paramDialogInterface.dismiss();
//                        ObjLoder.setVisibility(View.GONE);
//                        ObjNoLocationEnabled.setVisibility(View.VISIBLE);
//                        //  setContentView(R.layout.location_required_view);
//                    }
//                });
//        dialog.show();
//
//    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    protected void checkLocationCustomSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        Toast.makeText(getApplicationContext(), "Inside onResult ", Toast.LENGTH_SHORT).show();
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i("", "All location settings are satisfied.");
                startLocationUpdates();
                displayLocation();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i("", "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i("", "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i("", "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case LocationBasedActivity.RESULT_OK:
                        Log.i("", "User agreed to make required location settings changes.");
                        displayLocation();
                        RenderFoodType();
                        break;
                    case LocationBasedActivity.RESULT_CANCELED:
                        Log.i("", "User chose not to make required location settings changes.");
                        Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                        ObjLoder.setVisibility(View.GONE);
                        ObjNoLocationEnabled.setVisibility(View.VISIBLE);
                        break;
                }
                break;
        }
    }

    public static List<Cuisine> pickNRandom(List<Cuisine> lst, int n) {
        List<Cuisine> copy = new LinkedList<Cuisine>(lst);
        Collections.shuffle(copy);
        return copy.subList(0, n);
    }

    public static List<FoodType> pickNRandomFoodType(List<FoodType> lst, int n) {
        List<FoodType> copy = new LinkedList<FoodType>(lst);
        Collections.shuffle(copy);
        return copy.subList(0, n);
    }

    public  void GenerateListFoodType(){

        objFoodTypelist.add(new FoodType("temp","Chinese","Chinese"));
        objFoodTypelist.add(new FoodType("temp","North Indian","North Indian"));
        objFoodTypelist.add(new FoodType("temp","Fast Food","Fast Food"));
        objFoodTypelist.add(new FoodType("temp","South Indian","South Indian"));
        objFoodTypelist.add(new FoodType("temp","Mughlai","Mughlai"));
        objFoodTypelist.add(new FoodType("temp","Seafood","Seafood"));
        objFoodTypelist.add(new FoodType("temp","Italian","Italian"));
        objFoodTypelist.add(new FoodType("temp","Continental","Continental"));
        objFoodTypelist.add(new FoodType("temp","Street Food","Street Food"));
        objFoodTypelist.add(new FoodType("temp","Biryani","Biryani"));
        objFoodTypelist.add(new FoodType("temp","BBQ","BBQ"));
        objFoodTypelist.add(new FoodType("temp","Gujarati","Gujarati"));
        objFoodTypelist.add(new FoodType("temp","Charcoal Chicken","Charcoal Chicken"));
        objFoodTypelist.add(new FoodType("temp","Pizza","Pizza"));
        objFoodTypelist.add(new FoodType("temp","Tex-Mex","Tex-Mex"));
        objFoodTypelist.add(new FoodType("temp","Vegetarian","Vegetarian"));
        objFoodTypelist.add(new FoodType("temp","Kebab","Kebab"));
        objFoodTypelist.add(new FoodType("temp","Beverages","Beverages"));
        objFoodTypelist.add(new FoodType("temp","Bar Food","Bar Food"));

    }
}
