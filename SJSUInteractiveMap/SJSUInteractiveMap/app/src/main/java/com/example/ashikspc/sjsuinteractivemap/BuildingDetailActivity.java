package com.example.ashikspc.sjsuinteractivemap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BuildingDetailActivity extends AppCompatActivity {

    private static final String TAG = "BDetail_Activity";
    private static final String USER_LATITUE = "USER_LATITUE";
    private static final String USER_LONGITUDE  = "USER_LONGITUDE";
    private static final String BUILDING_ID  = "BUILDING_ID";
    public static final String []BUILDING_NAMES = new String[] {"King Library", "Engineering Building", "Yoshihiro Uchida Hall", "Student Union", "Bocardo Business Center", "South Parking Garage"};
    public static final String []BUILDING_ADDRESSES = new String[]
            {
                "Dr. Martin Luther King, Jr. Library, 150 East San Fernando Street, San Jose, CA 95112",
                "San José State University Charles W. Davidson College of Engineering, 1 Washington Square, San Jose, CA 95112",
                "Yoshihiro Uchida Hall, San Jose, CA 95112",
                "Student Union Building, San Jose, CA 95112",
                "Boccardo Business Complex, San Jose, CA 95112",
                "San Jose State University South Garage, 330 South 7th Street, San Jose, CA 95112"
            };
    public static final double [][]BUILDINGLATLONGS = new double[][]
            {
                {37.335729, -121.885474},
                {37.337452, -121.881847},
                {37.333646, -121.883732},
                {37.336539, -121.880657},
                {37.336645, -121.878725},
                {37.333075, -121.880798}
            };

    public static final int KING_LIBRARY = 0;
    public static final int ENGINEERING_BUILDING = 1;
    public static final int YOSHIHIRO = 2;
    public static final int STUDENT_UNION = 3;
    public static final int BBC = 4;
    public static final int SOUTH_GARAGE = 5;
    private TextView bldgName, bldgAddr, bldgTravel, bldgTravelTime;
    private ImageView buildingImage;
    private double userLon, userLat;
    private int buildingLocationId;

    public static Intent createIntent(Context c, double uLat, double uLon, int buildingId){
        Intent i = new Intent(c, BuildingDetailActivity.class);
        i.putExtra(USER_LATITUE, uLat);
        i.putExtra(USER_LONGITUDE, uLon);
        i.putExtra(BUILDING_ID, buildingId);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bldgName = (TextView) findViewById(R.id.name);
        bldgAddr = (TextView) findViewById(R.id.addr);
        bldgTravel = (TextView) findViewById(R.id.travel);
        bldgTravelTime = (TextView) findViewById(R.id.traveltime);
        buildingImage = (ImageView) findViewById(R.id.buildingImage);

        Intent in = getIntent();
        buildingLocationId = in.getIntExtra(BUILDING_ID, 0);
        bldgName.setText(BUILDING_NAMES[buildingLocationId]);
        bldgAddr.setText(BUILDING_ADDRESSES[buildingLocationId]);
        userLat = in.getDoubleExtra(USER_LATITUE, 0);
        userLon = in.getDoubleExtra(USER_LONGITUDE, 0);
        switch (buildingLocationId){
            case 0:
                buildingImage.setBackgroundResource(R.drawable.sjsu_king);
                break;
            case 1:
                buildingImage.setBackgroundResource(R.drawable.sjsu_engineering);
                break;
            case 2:
                buildingImage.setBackgroundResource(R.drawable.sjsu_yuh);
                break;
            case 3:
                buildingImage.setBackgroundResource(R.drawable.sjsu_student_union);
                break;
            case 4:
                buildingImage.setBackgroundResource(R.drawable.sjsu_bbc);
                break;
            case 5:
                buildingImage.setBackgroundResource(R.drawable.sjsu_south_garage);
                break;
            default:
                break;
        }

        Log.d("AfterIntent", bldgName.getText().toString() + bldgAddr.getText().toString() + userLat + userLon + buildingLocationId);

        new DistanceBackground().execute();

        ImageButton streetViewButton = (ImageButton) findViewById(R.id.street_view);
        streetViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = StreetViewActivity.createIntent(BuildingDetailActivity.this, bldgName.getText().toString(), bldgAddr.getText().toString(), userLat, userLon, buildingLocationId, BUILDINGLATLONGS[buildingLocationId][0], BUILDINGLATLONGS[buildingLocationId][1]);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(this) != null) {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DistanceBackground extends AsyncTask< Void, Void, String[] > {

        @Override
        protected String[] doInBackground(Void...voids) {
            return new calculateDistance().getDistance(userLat, userLon, buildingLocationId);
        }

        @Override
        protected void onPostExecute(String []result) {
            bldgTravel.setText(result[0] + ": " + result[1]);
            bldgTravelTime.setText("Time: " + result[2]);
        }
    }

    private class calculateDistance {
        private static final String DISTANCEURL = "https://maps.googleapis.com/maps/api/distancematrix/json";

        public String[] getDistance(double uLat, double uLon, int buildingLocationId) {
            String urlMode;
            if(MapActivity.isInside)
                urlMode = "Walking";
            else
                urlMode = "Driving";
            String url = Uri.parse(DISTANCEURL).buildUpon()
                    .appendQueryParameter("units", "imperial")
                    .appendQueryParameter("mode", urlMode.toLowerCase())
                    .appendQueryParameter("origins", uLat + "," + uLon)
                    .appendQueryParameter("destinations", "" + BUILDINGLATLONGS[buildingLocationId][0] + ", " + BUILDINGLATLONGS[buildingLocationId][1])
                    .appendQueryParameter("key", "AIzaSyAp1pIC-QuownmunjD1GegEgyFbkChh9ls")
                    .build().toString();
            String mode = urlMode + " distance";
            try {
                String jsonResult = new String(getUrlBytes(url));
                Log.i(TAG, jsonResult);
                JSONObject reader = new JSONObject(jsonResult);
                JSONObject element = reader.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);
                String distance = element.getJSONObject("distance").getString("text");
                String time = element.getJSONObject("duration").getString("text");
                return new String[] {mode, distance, time};
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        byte[] getUrlBytes(String finalurl) throws IOException {
            URL finalUrl = new URL(finalurl);
            HttpURLConnection con = (HttpURLConnection) finalUrl.openConnection();
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = con.getInputStream();
                if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }
                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in .read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                return out.toByteArray();
            } finally {
                con.disconnect();
            }
        }
    }
}
