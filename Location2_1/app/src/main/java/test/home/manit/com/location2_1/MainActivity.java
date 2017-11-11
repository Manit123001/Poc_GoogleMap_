package test.home.manit.com.location2_1;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import android.location.Location;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
    private final String LOG = "LaurenceTestApp";

    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLatitudeText  = (TextView) findViewById(R.id.latitude_text);
        mLongitudeText  = (TextView) findViewById(R.id.longitude_text);

        buildGoogleApiClient();

    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        int connectPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if(connectPermission != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            return;
        }


        mLocationRequest = LocationRequest.create().create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }
    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOG, location.toString());

        mLatitudeText.setText(String.valueOf(location.getLatitude()));
        mLongitudeText.setText(String.valueOf(location.getLongitude()));
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(LOG, "Coinnect failed"+ result.getErrorCode());
    }

    public void onDisconnected(){
        Log.i(LOG, "Disconnedcted");

    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG, "Coinnect suspended");
        mGoogleApiClient.connect();
    }




//    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
//
//    @TargetApi(Build.VERSION_CODES.M)
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        mLocationRequest = LocationRequest.create().create();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(1000);
//
//        int connectPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
//        if(connectPermission != PackageManager.PERMISSION_GRANTED){
//            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
//            return;
//        }
//
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        Log.i(LOG_TAG, location.toString());
//        mLatitudeText.setText(Double.toString(location.getLatitude()));
//        mLongitudeText.setText(Double.toString(location.getLongitude()));
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.i(LOG_TAG, "GoogleApiClient connection has been suspended");
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Log.i(LOG_TAG, "GoogleApiClient connection has Failed");
//
//    }


}
