package com.example.tvs.googlemaps;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Iterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Boolean flag;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private PolylineOptions polylineOptions;
    private LatLngBounds latlongBounds;
    private LatLngBounds.Builder latlongBoundsBuilder;
    private ArrayList<Marker> markers = new ArrayList<>();
    private ArrayList<Polyline> polyLines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        flag = false;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        polylineOptions = new PolylineOptions().color(Color.BLACK).width(2);
        latlongBoundsBuilder =  new LatLngBounds.Builder();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if(flag) {
                    flag = false;
                    for(Marker marker : markers)
                        marker.remove();
                    markers.clear();
                    for (Polyline line : polyLines)
                        line.remove();
                    polyLines.clear();
                    polylineOptions = new PolylineOptions().color(Color.BLACK).width(2);
                    Toast.makeText(MapsActivity.this, "Tracking is disabled", Toast.LENGTH_SHORT).show();
                }
                else {
                    flag = true;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    onResume();
                    Toast.makeText(MapsActivity.this, "Tracking is enabled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(flag) {
                    LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    if (markers.size() > 1)
                        markers.get(markers.size()-1).setVisible(false);
                    markers.add(mMap.addMarker(new MarkerOptions().position(newLocation)));
                    for(Marker marker : markers)
                        latlongBoundsBuilder.include(marker.getPosition());
                    latlongBounds = latlongBoundsBuilder.build();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latlongBounds, 100));
                    polylineOptions.add(newLocation);
                    polyLines.add(mMap.addPolyline(polylineOptions));
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alertDialogue = new AlertDialog.Builder(this);
            alertDialogue
                    .setTitle("GPS is not enabled!")
                    .setMessage("Do you want to enable GPS?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    })
                    .create()
                    .show();
        }
        else {
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            } else locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 100, locationListener);
        }

    }
}
