package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationListenerCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
LocationManager locationManager ;
LocationListener locationListener ;
TextView result ;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.hikeView);
     locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE) ;

     locationListener = new LocationListener() {
         @Override
         public void onLocationChanged(@NonNull Location location) {
           Geocoder  geocoder = new Geocoder(getApplicationContext(), Locale.getDefault()) ;

             String address = "" ;
             try {
               List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1) ;
               if(addresses.size()>0)
               {
                   if(addresses.get(0).getThoroughfare()!=null)
                   {
                       address+=addresses.get(0).getThoroughfare()+" ";
                   }
                   if(addresses.get(0).getLocality()!=null)
                   {
                       address+=addresses.get(0).getLocality()+ " ";
                   }
                   if(addresses.get(0).getPostalCode()!=null)
                   {
                       address += addresses.get(0).getPostalCode()+" ";
                   }
                   if(addresses.get(0).getAdminArea()!=null)
                   {
                       address+=addresses.get(0).getAdminArea();
                   }
                 // Log.d("address",address);
               }


             } catch (Exception e) {
                 e.printStackTrace() ;
             }


             result.setText( "Latitude: "+location.getLatitude()+"\t\n" +"Longitude: "+location.getLongitude()+"\t\n"+
                     "Altitude: "+location.getAltitude()+"\t\n"+"Accuracy: "+ location.getAccuracy() + "\t\nAddresses: " + address );

//             Log.d("location ",location.getLatitude()+" "+location.getLongitude());
         }
     };
    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
       ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
    }
    else{
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
    }
    }
}