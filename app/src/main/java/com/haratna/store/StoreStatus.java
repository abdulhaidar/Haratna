package com.haratna.store;

import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.haratna.R;
import com.haratna.tool.MyPlace;

import java.util.ArrayList;
import java.util.Random;

public class StoreStatus extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<MyPlace> PLACES = new ArrayList<MyPlace>();
    private final Random random = new Random();
    Button Next_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_status);
        Next_btn = findViewById(R.id.Next_btn);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double lat = Double.valueOf(getIntent().getStringExtra("Lat"));
        double lng = Double.valueOf(getIntent().getStringExtra("Lng"));
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(sydney).title(getIntent().getStringExtra("name")));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
        if(random.nextInt()%2==0){
            setHeight(lat,lng);
        }else{
            setLow(lat,lng);
        }

    }
    private void setHeight(double lat, double lng){
        for(int i=1; i<20; i++){
            mMap.addMarker(new MarkerOptions().position(generateLocationWithinRadius(lat,lng,0.001)));
        }
        Next_btn.setText("Store is Crowded\nPlease Visit us Later\n#STAYHOME_STAYSAFE");
    }
    private void setLow(double lat, double lng){
        for(int i=1; i<5; i++){
            mMap.addMarker(new MarkerOptions().position(generateLocationWithinRadius(lat,lng,0.001)));
        }
        Next_btn.setText("We are waiting for you\nDON't FORGET YOUR GLOVES and MASK");
    }
    private LatLng generateLocationWithinRadius(double Lat, double Lng, double radius) {
        double a = Lat;
        double b = Lng;
        double r = radius;

        // x must be in (a-r, a + r) range
        double xMin = a - r;
        double xMax = a + r;
        double xRange = xMax - xMin;

        // get a random x within the range
        double x = xMin + random.nextDouble() * xRange;

        // circle equation is (y-b)^2 + (x-a)^2 = r^2
        // based on the above work out the range for y
        double yDelta = Math.sqrt(Math.pow(r,  2) - Math.pow((x - a), 2));
        double yMax = b + yDelta;
        double yMin = b - yDelta;
        double yRange = yMax - yMin;
        // Get a random y within its range
        double y = yMin + random.nextDouble() * yRange;

        // And finally return the location
        return new LatLng(x, y);
    }
}
