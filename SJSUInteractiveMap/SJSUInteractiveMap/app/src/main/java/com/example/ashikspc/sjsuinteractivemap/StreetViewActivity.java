package com.example.ashikspc.sjsuinteractivemap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

public class StreetViewActivity extends AppCompatActivity implements OnStreetViewPanoramaReadyCallback {

    public static final String BUILDING_LATITUDE = "BUILDING_LATITUDE";
    public static final String BUILDING_LONGITUDE = "BUILDING_LONGITUDE";
    public static final String BUILDING_NAME = "BUILDING_NAME";
    public static final String BUILDING_ADDRESS = "BUILDING_ADDRESS";
    public static final String USER_LATITUE = "USER_LATITUE";
    public static final String USER_LONGITUDE  = "USER_LONGITUDE";
    public static final String BUILDING_ID  = "BUILDING_ID";
    private double uLa, uLo, buildingLatitude, buildingLongitude;
    private int uiBuildingLocationId;
    private String addr, name;

    public static Intent createIntent(Context c, String name, String address, double uLat, double uLon, int buildingId, double buildingLat, double buildingLon){
        Intent i = new Intent(c, StreetViewActivity.class);
        i.putExtra(BUILDING_NAME, name);
        i.putExtra(BUILDING_ADDRESS, address);
        i.putExtra(USER_LATITUE, uLat);
        i.putExtra(USER_LONGITUDE, uLon);
        i.putExtra(BUILDING_ID, buildingId);
        i.putExtra(BUILDING_LATITUDE, buildingLat);
        i.putExtra(BUILDING_LONGITUDE, buildingLon);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        uLa = i.getDoubleExtra(USER_LATITUE, 0);
        uLo = i.getDoubleExtra(USER_LONGITUDE, 0);
        uiBuildingLocationId = i.getIntExtra(BUILDING_ID, 0);
        name = i.getStringExtra(BUILDING_NAME);
        addr = i.getStringExtra(BUILDING_ADDRESS);
        buildingLatitude = i.getDoubleExtra(BUILDING_LATITUDE, 0);
        buildingLongitude = i.getDoubleExtra(BUILDING_LONGITUDE, 0);

        StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getFragmentManager()
                        .findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = BuildingDetailActivity.createIntent(StreetViewActivity.this, uLa, uLo, uiBuildingLocationId);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        Log.i("PanoramaReady", buildingLatitude + "," +buildingLongitude);
        streetViewPanorama.setPosition(new LatLng(buildingLatitude, buildingLongitude), 200);
    }
}