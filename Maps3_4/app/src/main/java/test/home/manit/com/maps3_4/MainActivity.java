package test.home.manit.com.maps3_4;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap m_map;
    boolean mapReady=false;

    MarkerOptions renton;
    MarkerOptions kirkland;
    MarkerOptions everrett;
    MarkerOptions lynnwoon;
    MarkerOptions montlake;
    MarkerOptions kent;
    MarkerOptions showare;

    static final CameraPosition SEATTLE = CameraPosition.builder()
            .target(new LatLng(47.6204, -122.2491))
            .zoom(10)
            .bearing(0)
            .tilt(45)
            .build();

    @Override
    public Resources getResources(){return super.getResources();}
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        renton = new MarkerOptions()
                .position(new LatLng(47.489805, -122.120502))
                .title("Renton")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));

        kirkland = new MarkerOptions()
                .position(new LatLng(47.7301986, -122.1768858))
                .title("Kirklnd")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));

        everrett = new MarkerOptions()
                .position(new LatLng(47.978748, -122.202001))
                .title("Everett")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));


        lynnwoon = new MarkerOptions()
                .position(new LatLng(47.819533, -122.32288))
                .title("Lynnwood")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));


        montlake = new MarkerOptions()
                .position(new LatLng(47.7973733, -122.3281771))
                .title("Montlake Terrace")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));


        kent = new MarkerOptions()
                .position(new LatLng(47.385938, -122.258212))
                .title("Kent Valley");
        showare = new MarkerOptions()
                .position(new LatLng(47.38702, -122.23986))
                .title("Showare Center");

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapReady = true;
        m_map = map;
        m_map.addMarker(renton);
        m_map.addMarker(kirkland);
        m_map.addMarker(everrett);
        m_map.addMarker(lynnwoon);
        m_map.addMarker(montlake);
        m_map.addMarker(kent);
        m_map.addMarker(showare);
        flyTo(SEATTLE);

    }

    private void flyTo(CameraPosition target){
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }
}
