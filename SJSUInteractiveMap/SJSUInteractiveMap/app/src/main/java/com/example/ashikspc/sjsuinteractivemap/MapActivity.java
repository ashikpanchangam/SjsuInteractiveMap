package com.example.ashikspc.sjsuinteractivemap;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static android.graphics.Bitmap.Config.ARGB_8888;

public class MapActivity extends AppCompatActivity
{

    private Button studentUnionBtn, kingLibraryBtn, engineeringBtn, bbcBtn, yhuBtn, spgBtn;
    private SearchView searchView;
    private SearchManager searchManager;
    private double ux, uy, uLon, uLat, fx, fy, latRad, mercN, nx, ny, difx, dify;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 99;
    private ImageView img, marker;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public static boolean isInside = false;

    public void plotUser(double ulat, double ulon)
    {
        Bitmap bmp = Bitmap.createBitmap(100, 100, ARGB_8888);
        Canvas c = new Canvas(bmp);
        Paint p = new Paint();
        p.setColor(Color.RED);
        c.drawCircle(50, 50, 20, p);
        img = (ImageView) findViewById(R.id.imageview_circle);
        marker = (ImageView) findViewById(R.id.bldgmarker);

        fx = 412;
        fy = 732;
        uLat = ulat;
        uLon = ulon;
        ux = (uLon + 180) * (fx / 360);
        latRad = uLat * Math.PI / 180;
        mercN = Math.log(Math.tan((Math.PI / 4) + (latRad / 2)));
        uy = (fy / 2) - (fx * mercN / (2 * Math.PI));
        nx = (ux * Math.cos(38.25)) - (uy * Math.sin(38.25));
        ny = (ux * Math.sin(38.25)) + (uy * Math.cos(38.25));
        difx = 60 - ((-110.770674 - nx) * 144910);
        dify = 530 - ((307.370716 - ny) * 200678);

        if (difx >= 40 && difx <= 1270 && dify >= 500 && dify <= 1970)
        {
            img.setX((float) difx);
            img.setY((float) dify);
            img.setBackground(new BitmapDrawable(MapActivity.this.getResources(), bmp));
            isInside = true;
        }
        else
        {
            img.setBackground(null);
            isInside = false;
        }

    }

    public void startLocationTracking() throws SecurityException
    {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastLocation != null)
        {
            plotUser(lastLocation.getLatitude(), lastLocation.getLongitude());
        }
        else
        {
            Toast.makeText(this, "Need trigger GPS data point on simulator", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    startLocationTracking();
                }
                else
                {
                    Log.e("PermitResult", "location permission denied");
                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener(MapActivity.this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            startLocationTracking();
        }

        kingLibraryBtn = (Button) findViewById(R.id.king_button);
        studentUnionBtn = (Button) findViewById(R.id.su_button);
        engineeringBtn = (Button) findViewById(R.id.eng_button);
        bbcBtn = (Button) findViewById(R.id.bbc_button);
        yhuBtn = (Button) findViewById(R.id.yhu_button);
        spgBtn = (Button) findViewById(R.id.spg_button);

        studentUnionBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(uLat == 0 || uLon == 0) {
                    Toast.makeText(MapActivity.this, "Need to enter GPS Data points on simulator", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent i = BuildingDetailActivity.createIntent(MapActivity.this, uLat, uLon, BuildingDetailActivity.STUDENT_UNION);
                startActivity(i);
            }
        });
        kingLibraryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(uLat == 0 || uLon == 0) {
                    Toast.makeText(MapActivity.this, "Need to enter GPS Data points on simulator", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent i = BuildingDetailActivity.createIntent(MapActivity.this, uLat, uLon, BuildingDetailActivity.KING_LIBRARY);
                startActivity(i);
            }
        });
        engineeringBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(uLat == 0 || uLon == 0) {
                    Toast.makeText(MapActivity.this, "Need to enter GPS Data points on simulator", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent i = BuildingDetailActivity.createIntent(MapActivity.this, uLat, uLon, BuildingDetailActivity.ENGINEERING_BUILDING);
                startActivity(i);
            }
        });

        bbcBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(uLat == 0 || uLon == 0) {
                    Toast.makeText(MapActivity.this, "Need to enter GPS Data points on simulator", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent i = BuildingDetailActivity.createIntent(MapActivity.this, uLat, uLon, BuildingDetailActivity.BBC);
                startActivity(i);
            }
        });
        yhuBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(uLat == 0 || uLon == 0) {
                    Toast.makeText(MapActivity.this, "Need to enter GPS Data points on simulator", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent i = BuildingDetailActivity.createIntent(MapActivity.this, uLat, uLon, BuildingDetailActivity.YOSHIHIRO);
                startActivity(i);
            }
        });
        spgBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(uLat == 0 || uLon == 0) {
                    Toast.makeText(MapActivity.this, "Need to enter GPS Data points on simulator", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent i = BuildingDetailActivity.createIntent(MapActivity.this, uLat, uLon, BuildingDetailActivity.SOUTH_GARAGE);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        searchManager =(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =(SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener()
                {
                    @Override
                    public boolean onQueryTextSubmit(String query)
                    {
                        if ("king library".equalsIgnoreCase(query.trim()) || "king".equalsIgnoreCase(query.trim()))
                        {
                            kingLibraryBtn.setAlpha(1.0f);
                            marker.setX(170);
                            marker.setY(650);
                            marker.setAlpha(1.0f);
                        }
                        else if ("bbc".equalsIgnoreCase(query.trim()) || "bocardo business center".equalsIgnoreCase(query.trim()))
                        {
                            bbcBtn.setAlpha(1.0f);
                            marker.setX(1180);
                            marker.setY(1150);
                            marker.setAlpha(1.0f);
                        }
                        else if ("yoshihiro uchida hall".equalsIgnoreCase(query.trim()) || "yuh".equalsIgnoreCase(query.trim()))
                        {
                            yhuBtn.setAlpha(1.0f);
                            marker.setX(160);
                            marker.setY(1300);
                            marker.setAlpha(1.0f);
                        } else if ("student union".equalsIgnoreCase(query.trim()) || "su".equalsIgnoreCase(query.trim())) {
                            studentUnionBtn.setAlpha(1.0f);
                            marker.setX(850);
                            marker.setY(950);
                            marker.setAlpha(1.0f);
                        } else if ("south parking garage".equalsIgnoreCase(query.trim()) || "spg".equalsIgnoreCase(query.trim())) {
                            spgBtn.setAlpha(1.0f);
                            marker.setX(530);
                            marker.setY(1800);
                            marker.setAlpha(1.0f);
                        } else if ("engineering building".equalsIgnoreCase(query.trim()) || "eb".equalsIgnoreCase(query.trim())) {
                            engineeringBtn.setAlpha(1.0f);
                            marker.setX(800);
                            marker.setY(700);
                            marker.setAlpha(1.0f);
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String text) {
                        engineeringBtn.setAlpha(0.0f);
                        yhuBtn.setAlpha(0.0f);
                        spgBtn.setAlpha(0.0f);
                        studentUnionBtn.setAlpha(0.0f);
                        bbcBtn.setAlpha(0.0f);
                        kingLibraryBtn.setAlpha(0.0f);
                        marker.setAlpha(0.0f);
                        return false;
                    }
                }
        );
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationTracking();
        }
    }
}
