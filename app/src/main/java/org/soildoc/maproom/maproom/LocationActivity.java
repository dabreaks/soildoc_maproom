package org.soildoc.maproom.maproom;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import org.soildoc.maproom.maprooms.Maproom;
import org.soildoc.maproom.maprooms.MaproomFactory;


public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    public final static int MIN_TIME = 3 * 1000;
    public final static int MIN_DISTANCE = 0;
    private final static double FORECAST_BBOX_INCREMENT = 2.5;
    private final static double HISTORY_BBOX_INCREMENT = 0.0375;
    private final static double MONITOR_BBOX_INCREMENT = 0.0375;

    private static final int MAPROOM_FORECAST = 1;
    private static final int MAPROOM_HISTORY = 2;
    private static final int MAPROOM_MONITOR = 3;

    private String FLEX_FORECAST = "http://iridl.ldeo.columbia.edu/maproom/Global/Forecasts/Flexible_Forecasts/precipitation.html?";
    private String HISTORY = "http://maproom.meteo.go.tz/maproom/Climatology/Climate_Analysis/monthly.html?";
    private String MONITOR = "http://maproom.meteo.go.tz/maproom/Climatology/Climate_Monitoring/index.html?";

    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView gpsAccuracy, gpsLat, gpsLong;
    private Button btnGetGPS, btnEnterGPS, btnForecast, btnHistory, btnMonitor;
    private static final int REQUEST_FINE_LOCATION = 0;
    private Location bestLocation;

    private MapFragment mapFragment;
    private GoogleMap mMap;
    private Marker marker;

    //private String locationProvider = LocationManager.NETWORK_PROVIDER;
    private String locationProvider = LocationManager.GPS_PROVIDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);

        gpsAccuracy = (TextView) findViewById(R.id.gps_accuracy);
        gpsLat = (TextView) findViewById(R.id.gps_lat);
        gpsLong = (TextView) findViewById(R.id.gps_long);
        btnGetGPS = (Button) findViewById(R.id.btnGpsReading);
        btnEnterGPS = (Button) findViewById(R.id.btnEnterGpsReading);
        btnForecast = (Button) findViewById(R.id.btnFlexibleForecast);
        btnHistory = (Button) findViewById(R.id.btnHistorical);
        btnMonitor = (Button) findViewById(R.id.btnMonitor);
        btnForecast.setEnabled(false);
        btnHistory.setEnabled(false);
        btnMonitor.setEnabled(false);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                visitMapRoom(MAPROOM_FORECAST, bestLocation);

            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visitMapRoom(MAPROOM_HISTORY, bestLocation);
            }
        });

        btnMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                visitMapRoom(MAPROOM_MONITOR, bestLocation);

            }
        });

        btnEnterGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(LocationActivity.this);
                dialog.setTitle("Enter GPS Coordinates");
                dialog.setContentView(R.layout.dialog_enter_gps);
                dialog.show();

                Button dialog_submit = (Button) dialog.findViewById(R.id.dialog_submit);
                Button dialog_cancel = (Button) dialog.findViewById(R.id.dialog_cancel);

                dialog_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText latitude_enter = (EditText) dialog.findViewById(R.id.dialog_latitude_enter);
                        String strLatitude = latitude_enter.getText().toString();
                        gpsLat.setText(strLatitude);
                        EditText longitude_enter = (EditText) dialog.findViewById(R.id.dialog_longitude_enter);
                        String strLongitude = longitude_enter.getText().toString();
                        gpsLong.setText(strLongitude);
                        //todo check if gps coordinates are filled in
                        bestLocation = new Location("");
                        bestLocation.setLatitude(Double.parseDouble(strLatitude));
                        bestLocation.setLongitude(Double.parseDouble(strLongitude));
                        bestLocation.setAccuracy(0);

                        showLocation(bestLocation);
                        moveMap(bestLocation);

                        btnForecast.setEnabled(true);
                        btnHistory.setEnabled(true);
                        btnMonitor.setEnabled(true);
                        dialog.cancel();
                    }
                });

                dialog_cancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                if (bestLocation == null) {
                    bestLocation = location;
                    showLocation(location);
                    moveMap(location);
                    btnForecast.setEnabled(true);
                    btnHistory.setEnabled(true);
                    btnMonitor.setEnabled(true);
                } else {
                    if (bestLocation.getAccuracy() > location.getAccuracy()) {
                        bestLocation.set(location);
                        showLocation(location);
                        moveMap(location);

                    }
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        };

        if (ContextCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
            return;
        }
        btnGetGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gpsAccuracy.setText("");
                gpsLat.setText("");
                gpsLong.setText("");
                bestLocation = null;
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", MIN_TIME, MIN_DISTANCE, locationListener);
            }
        });

    }

    public void showLocation(Location location) {
        gpsLat.setText(String.format(Locale.US, "%.4f", location.getLatitude()));
        gpsLong.setText(String.format(Locale.US,"%.4f", location.getLongitude()));
        gpsAccuracy.setText(String.format(Locale.US,"%.2f", location.getAccuracy()));

    }

    public void visitMapRoom(Integer maproomtype, Location location) {

        MaproomFactory maproomFactory = new MaproomFactory(location);

        Maproom maproom = maproomFactory.makeMaproom(maproomtype);

        // check if valid
        if (!maproom.validLocation()) {
            AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
            myAlert.setMessage("This content isn't available for your area")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            myAlert.show();
        } else {

            String address = maproom.getAdress();

            Intent appBrowserIntent = new Intent(getApplicationContext(), Web.class);
            appBrowserIntent.putExtra("url", address);
            startActivity(appBrowserIntent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)

                    btnGetGPS.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //noinspection MissingPermission
                            locationManager.requestLocationUpdates("gps", MIN_TIME, MIN_DISTANCE, locationListener);
                        }
                    });
                    btnGetGPS.setEnabled(true);
        }
    }

    // Maps
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        marker = mMap.addMarker(new MarkerOptions()
                     .position(new LatLng(0,0))
                     .title("Marker"));
    }

    public void moveMap(Location location) {

        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        marker.remove();
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, 10);
        mMap.animateCamera(update);
        marker = mMap.addMarker(new MarkerOptions()
                .position(latlng));
    }

    @Override
    public void onStop() {
        super.onStop();

        if (ContextCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
            return;
        }
        locationManager.removeUpdates(locationListener);
        locationManager = null;
    }
}



