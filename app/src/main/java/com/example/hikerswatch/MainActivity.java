package com.example.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startListening();
        }
    }

    public void startListening(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED){
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void updateLocationInfo(Location location){
        System.out.println(location.toString());

        TextView latTextView = (TextView) findViewById(R.id.latTextView);
        TextView longTextView = (TextView) findViewById(R.id.longTextView);
        TextView altTextView = (TextView) findViewById(R.id.altTextView);
        TextView accTextView = (TextView) findViewById(R.id.accTextView);

        latTextView.setText("Latitude: "+location.getLatitude());
        longTextView.setText("Longitude: "+location.getLongitude());
        altTextView.setText("Altitude: "+location.getAltitude());
        accTextView.setText("Accuracy: "+location.getAccuracy());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            String address = "Failed to get Address";
            List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
            address = "Address: \n";
            if(listAddress != null && listAddress.size() > 0){
                if(listAddress.get(0).getSubThoroughfare()!= null){
                     address += listAddress.get(0).getSubThoroughfare()+ " ";
                }if(listAddress.get(0).getThoroughfare()!= null){
                     address += listAddress.get(0).getThoroughfare()+ "\n";
                }if(listAddress.get(0).getLocality()!= null){
                     address += listAddress.get(0).getLocality()+ "\n";
                }if(listAddress.get(0).getPostalCode()!= null){
                     address += listAddress.get(0).getPostalCode()+ "\n";
                }if(listAddress.get(0).getCountryName()!= null){
                     address += listAddress.get(0).getCountryName();
                }
            }
            TextView addressTextView = (TextView) findViewById(R.id.addTextView);
            addressTextView.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
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

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location != null){
            updateLocationInfo(location);}


        }

    }
}
