package com.manit.home.example.android.androidmap;

import android.*;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Double lat= 27.00, lng=78.00;
    private GoogleMap mMap;
    String info;
    Marker marker;
    UiSettings uisetting;
    MapFragment mapFragment;
    int[] arrayicon = {
            R.drawable.iconred,
            R.drawable.icongreen,
            R.drawable.icongrey,
            R.drawable.iconblue};


    // my Location
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private GoogleMap googlemap;

    private LocationRequest mLocationRequest;
    private static int UPDATE_INTERVAL = 10000; //10 sec
    private static int FATEST_INTERVAL = 5000; // 5sec
    private static int DISPLACEMENT = 10; // 10 meters


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initilizeMap();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mapoptions();
        mapoptions();
        try {
            info = mapcoder( lat,lng);
        } catch (IOException e) {
            e.printStackTrace();
        }
        putmarker(lat, lng, "Location Address", info, 1);
        cameraposition(1);

        // display my location lan-lng
        googlepiclient();
        createLocationRequest();
        displaylocation();

    }

    private void initilizeMap() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);
    }

    private void mapoptions(){
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
                .compassEnabled(true)
                .rotateGesturesEnabled(true)
                .mapToolbarEnabled(true)
                .tiltGesturesEnabled(true)
                .zoomControlsEnabled(true);
        MapFragment.newInstance(options);
    }

    private void putmarker(Double lat, Double lng, String mtitle, String msnippet, int markerno){
        Bitmap micon = resize(arrayicon[markerno], 150);
        marker = mMap.addMarker(new MarkerOptions()
        .position(new LatLng(lat, lng)).title(mtitle).snippet(msnippet)
        .draggable(true)
        .icon(BitmapDescriptorFactory.fromBitmap(micon)));
    }

    private void cameraposition(float i){
        LatLng latLng = new LatLng(lat, lng);
        //googleMap.moveCamera(cameraUpdateFactory.newLatLngBounds(laLng,));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)// Center Set
                .zoom(1.0f) //Zoom
                .bearing(0+i)   //Orientation of the cammera to east
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

//    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
//    @TargetApi(Build.VERSION_CODES.M)
    private void mapsetting(){
        uisetting = mMap.getUiSettings();
        uisetting.setZoomControlsEnabled(true);
        uisetting.setCompassEnabled(true);
        uisetting.setMyLocationButtonEnabled(true);

//        int hasWriteContactsPermission = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
//        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
////            requestPermissions(new String[] {Manifest.permission.WRITE_CONTACTS},
////                    REQUEST_CODE_ASK_PERMISSIONS);
//            return;
//        }
        mMap.setMyLocationEnabled(true);
    }


    private Bitmap resize(int image, int size){
        Bitmap b = BitmapFactory.decodeResource(getResources(), image);
        Bitmap bitmapresize = Bitmap.createScaledBitmap(b, size, size, false);
        return bitmapresize;
    }

    private String mapcoder(double lat, double lng) throws IOException{
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<android.location.Address> addresses = geocoder.getFromLocation(lat, lng, 1);
        String citynm = addresses.get(0).getAddressLine(0);
        String statenm = addresses.get(0).getAddressLine(1);
        String countrynm = addresses.get(0).getAddressLine(2);
        return citynm+'\n'+statenm+'\n'+countrynm;

    }


    // My Location
    public void googlepiclient(){
        if(mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    //display present location - lat and lng
//    @TargetApi(Build.VERSION_CODES.M)
    public void displaylocation() {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        String lngg = null;
        String latt = null;
        if (mLastLocation != null) {
            latt = String.valueOf(mLastLocation.getLatitude());
            lngg = String.valueOf(mLastLocation.getLongitude());
            Toast.makeText(this, "lat:" + latt + " / " + "lng" + lngg, Toast.LENGTH_LONG).show();

        }
        Toast.makeText(this, "lat:" + latt + " / " + "lng" + lngg, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
