package com.haratna;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.haratna.delivery.DeliveryItems;
import com.haratna.store.StoreStatus;
import com.haratna.tool.MyPlace;
import com.haratna.tool.SharedStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AvailableStores extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Marker> gMarkers = new ArrayList<Marker>();
    ArrayList<MyPlace> PLACES = new ArrayList<MyPlace>();
    Button Next_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_stores);
        Next_btn = findViewById(R.id.Next_btn);
        Next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AvailableStores.this, StoreStatus.class));
            }
        });
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

        double latitute = 0;
        double longtitute = 0;

        latitute = Double.valueOf(SharedStore.getInstance().getValue("CurrentLat"));
        longtitute = Double.valueOf(SharedStore.getInstance().getValue("CurrentLong"));

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng MyLocation = new LatLng(latitute, longtitute);
        mMap.addMarker(new MarkerOptions().position(MyLocation).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("ic_mylocation",200,200))));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLocation,15));
        NearbyPlaces2(latitute, longtitute);
    }
    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "mipmap", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
    private void NearbyPlaces2(double Lat, double Lng) {
//YOU Can change this type at your own will, e.g hospital, cafe, restaurant.... and see how it all works
        //mMap.clear();
        PLACES = new ArrayList<>();
        gMarkers = new ArrayList<Marker>();
        double Latitute = Lat;
        double Longtitute = Lng;
        LatLng Loction = new LatLng(Latitute,Longtitute);
        //mMap.addMarker(new MarkerOptions().position(Loction).anchor(0.5f, 0.5f));

        StringBuilder PlacesAPI =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        PlacesAPI.append("location=").append(Latitute).append(",").append(Longtitute);
        PlacesAPI.append("&types=store");
        PlacesAPI.append("&language=ar");
        PlacesAPI.append("&rankby=distance");
        PlacesAPI.append("&key=AIzaSyCXxBBipv69Sg59qMDIXWbGBPjWrZhaiNE");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, PlacesAPI.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        try {
                            JSONArray jsonArray = result.getJSONArray("results");
                            if (result.getString("status").equalsIgnoreCase("OK")) {
                                //ArrayList<MyPlace> PLACES = new ArrayList<MyPlace>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject place = jsonArray.getJSONObject(i);
                                    MyPlace PLACE = new MyPlace();
                                    //PLACE.MyPlaceID = place.getString("place_id");
                                    if (!place.isNull("name")) {
                                        PLACE.MyPlaceNameAR = place.getString("name");
                                        //PLACE.MyPlaceNameEN = place.getString("name");
                                    }
                                    PLACE.MyPlaceLat = place.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                                    PLACE.MyPlaceLng = place.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                                    PLACE.MyPlaceNameEN = place.getString("vicinity");
                                    PLACES.add(PLACE);
                                }
                                for (int i = 0; i<PLACES.size();i++){
                                    addMarker(i,PLACES.get(i).MyPlaceLat,PLACES.get(i).MyPlaceLng,PLACES.get(i).MyPlaceNameAR);

                                }
                            } else if (result.getString("status").equalsIgnoreCase("ZERO_RESULTS")) {
                                Toast.makeText(getBaseContext(), "No Mosques Near You",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("MosqAPIRequest", "parseLocationResult: Error=" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override                    public void onErrorResponse(VolleyError error) {
                        Log.e("MosqAPIRequest", "onErrorResponse: Error= " + error);
                        Log.e("MosqAPIRequest", "onErrorResponse: Error= " + error.getMessage());
                    }
                });
        App.getInstance().addToRequestQueue(request);
    }
    protected void addMarker(int ID, double latitude, double longitude, String title) {
        //Resources resource = getApplicationContext().getResources();
        //int typeicon = resource.getIdentifier("mosquee", "mipmap", getApplicationContext().getPackageName());
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("store_marker",60,80)))
        );
        marker.setTag(ID);
        //Marker marker = myMap.addMarker(new MarkerOptions().position(new LatLng(geo1Dub,geo2Dub)));
        gMarkers.add(marker);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent I = new Intent(AvailableStores.this,StoreStatus.class);
                I.putExtra("Lat",String.valueOf(marker.getPosition().latitude));
                I.putExtra("Lng",String.valueOf(marker.getPosition().longitude));
                I.putExtra("name",marker.getTitle());
                Log.e("LatLng",marker.getPosition().latitude+","+marker.getPosition().longitude+" - "+marker.getTitle());
                startActivity(I);
                return true;
            }
        });
    }
}
