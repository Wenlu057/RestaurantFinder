package com.wdu.restaurantfinder;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,OnCameraIdleListener,GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        requestMarker(40.7111580,-73.8168990);

    }

    @Override
    public void onCameraIdle() {
        LatLng target = mMap.getCameraPosition().target;
        requestMarker(target.latitude,target.longitude);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(40.7111580,  -73.8168990), 13));
    }

    public void showPins(double lat, double lon, String restName, double rating){
        Marker marker =mMap.addMarker(new MarkerOptions()
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.house_flag))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(lat,lon)).title(restName) .snippet("rating: "+rating));
    }

    public void requestMarker(double latitude, double longitude){
        HttpRequest.get( new HttpRequest.Callback() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    for(int i = 0; i< response.length();i++){
                        JSONObject object = (JSONObject) response.getJSONObject(i);
                        JSONObject coordinates =object.getJSONObject("coordinates");

                        Double latitude = coordinates.optDouble("latitude");
                        Double longitude =coordinates.optDouble("longitude");
                        String name = object.getString("name");
                        Double rating = object.optDouble("rating");
                        if(!latitude.isNaN()&&!rating.isNaN()){
                            showPins(latitude,longitude, name,rating);
                        }
                    }
                }catch (JSONException e){
                    throw new RuntimeException(e);
                }


            }
        },latitude+"",longitude+"");
    }
}
