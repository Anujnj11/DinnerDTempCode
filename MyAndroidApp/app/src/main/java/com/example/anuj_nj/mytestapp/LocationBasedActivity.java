package com.example.anuj_nj.mytestapp;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.*;
import android.location.Location;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationBasedActivity extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener,ResultCallback<LocationSettingsResult> {

    //API Response Bind
    private List<GoogleResturanteResponse> objGoogle;
    private List<Result> objResultGoogle;
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
//    private LocationManager lm;
//    boolean gps_enabled = false;
//    boolean network_enabled = false;

//    ProgressBar  ObjprogressBar;
    View ObjLoder,ObjNoLocationEnabled;

    private  int ResturanteCount = 0;

    Intent objPassedData = null;
    TextView ObjClient_Location;
    private  String CuisineType = "Biryani";
    //Google Key
    private static final String[] Key =  {"AIzaSyAkL7MteqiUjMsqjdLIkMWEMEKlmmzXpfQ","AIzaSyBoP3sh2QzKR7riXti160fShNR8SUqArVo"};
    @Override
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
        setContentView(R.layout.activity_location_based);

        //Recieve Data
        objPassedData  = getIntent();
        CuisineType =  objPassedData.getExtras().getString("CuisineType");
        Toast.makeText(this,"CuisineType: " + objPassedData.getExtras().getString("CuisineType"),Toast.LENGTH_LONG).show();
        Init();

//        btnGetCoordinates.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                displayLocation();
//            }
//        });
//
//        btnLocationUpdates.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tooglePeriodicLoctionUpdates();
//            }
//        });
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

    private void Init(){

        //Set Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //RecyclerView Initization
        rcObj = (RecyclerView)findViewById(R.id.recycleView_Resturant);
        rcObj.setHasFixedSize(true);
        rcObj.setLayoutManager(new LinearLayoutManager(LocationBasedActivity.this));
        objResultGoogle = new ArrayList<>();

//        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
////        Toast.makeText(this,"gps_enabled :" + gps_enabled +" network_enabled :" + network_enabled,Toast.LENGTH_LONG).show();
//        if(!gps_enabled && !network_enabled ){
//          //  CheckLocationSetting();
//            buildLocationSettingsRequest();
//            checkLocationCustomSettings();
//        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //Toast.makeText(LocationBasedActivity.this,"Permission",Toast.LENGTH_LONG).show();

            //Run-time request permission
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
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
    }

    private  void getAPIData(String _Location){
        Toast.makeText(getApplicationContext(), "Calling api", Toast.LENGTH_SHORT).show();
        Call<PlaceDetails> objPlaceDetails = GooglePlaceAPI.getServiceData().getByTextSearchAndLocation(CuisineType +"+restaurants+near+"+_Location,Key[new Random().nextInt(Key.length)]);
        objPlaceDetails.enqueue(new Callback<PlaceDetails>() {
            @Override
            public void onResponse(Call<PlaceDetails> call, Response<PlaceDetails> response){
                PlaceDetails objresult = response.body();
                objResultGoogle.addAll(objresult.getResults());
                ResturanteCount =  objResultGoogle.size();
                rcObj.setItemAnimator(new DefaultItemAnimator());
              //  ObjprogressBar.setVisibility(View.GONE);
                ObjLoder.setVisibility(View.GONE);
                ObjNoLocationEnabled.setVisibility(View.GONE);
                rcObj.setAdapter(new RecycleViewAdapter(LocationBasedActivity.this,objResultGoogle));
                // Toast.makeText(APIActivity.this,"Success",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<PlaceDetails> call, Throwable t){
                Log.i("myApp", "error:"+t);
                Toast.makeText(LocationBasedActivity.this,"Error",Toast.LENGTH_LONG).show();
            }
        });
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if(ResturanteCount == 0){
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();
                SharedPreferences preferences = getSharedPreferences("LocationCoordinates", Context.MODE_PRIVATE);
                preferences.edit().putString("latitude",Double.toString(latitude)).commit();
                preferences.edit().putString("longitude",Double.toString(longitude)).commit();
            Toast.makeText(this,"latitude :" + latitude +" longitude:" + longitude,Toast.LENGTH_LONG).show();
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (null != listAddresses && listAddresses.size() > 0) {
                        String _Location = listAddresses.get(0).getSubLocality();
                        Toast.makeText(this, "_Location:" + _Location, Toast.LENGTH_SHORT).show();
                        preferences.edit().putString("LocationName",_Location).commit();
                        ObjClient_Location.setText(CuisineType + " IN AND AROUND "+_Location);
                        getAPIData(_Location);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //txtCoordinates.setText("Couldn't get the location. Make sure location is enable on the device");
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
    public void onLocationChanged(Location location) {
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
//        Toast.makeText(getApplicationContext(), "INside checkLocationSettings "+ result, Toast.LENGTH_SHORT).show();
        result.setResultCallback(this);
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
//        Toast.makeText(getApplicationContext(), "Inside onResult ", Toast.LENGTH_SHORT).show();

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
}
