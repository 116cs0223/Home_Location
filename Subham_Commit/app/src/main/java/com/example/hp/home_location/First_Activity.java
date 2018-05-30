package com.example.hp.home_location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;

public class First_Activity extends AppCompatActivity implements LocationListener,OnMapReadyCallback
{

    GoogleMap mMap;
    ArrayList<Info_Points>array = new ArrayList<Info_Points>();
    private Button btn1,btn2;
    TextView txtView=null;
    Info_Points info_points;
    private LocationManager locationManager;
    private static final int REQUEST_PERMISSION_FINE_LOCATION_RESULT=0;
    static Double latitude;
    static Double longitude;
    LatLng myLocation;
    int i=2;
    byte[] bytes ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first_);
        btn1=(Button)findViewById(R.id.button);
        btn2=(Button)findViewById(R.id.button2);
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
        checkLocationPermission();



    }

    public void onGo(View view)
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            info_points = new Info_Points(latitude, longitude, i - 1);
            array.add(info_points);
            btn1.setText("Click-" + i);
            i++;
        }
        else
        {

            Toast.makeText(this,"allow location first",Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_PERMISSION_FINE_LOCATION_RESULT)
        {
            if(grantResults[0]!=PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Application will not run without location permission",Toast.LENGTH_LONG).show();
            }
            else
                {
                getLocation();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {



            latitude=(location.getLatitude());
            longitude=(location.getLongitude());
            try
            {
                txtView=(TextView)findViewById(R.id.textView);
                txtView.setText("latitude:" + latitude +"\nlongitude :" + longitude);


            //myLocation = new LatLng(latitude,longitude);
           // mMap.addMarker(new MarkerOptions().position(myLocation).title("Marker is my location"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));

            }
        catch(Exception e)
        {

            Toast.makeText(this,txtView+"",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle)
    {


    }

    @Override
    public void onProviderEnabled(String s)
    {


    }

    @Override
    public void onProviderDisabled(String s)
    {
        Toast.makeText(this,"Please Enable GPS",Toast.LENGTH_SHORT).show();

    }


    void getLocation()
    {
        try
        {
            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,5,this);

        }
        catch(SecurityException e)
        {
            e.printStackTrace();
            Toast.makeText(this,"Some permission not available in manifest",Toast.LENGTH_SHORT).show();
        }

    }

    public void handleFinish(View view)
    {
        if(ContextCompat.checkSelfPermission
                (this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {

            if (i >= 5)
            {
                //convert arraylist into bytes

                Gson gson = new Gson();//add dependencies of gson fron file->project structure->app->dependencies->+->library->(search)gson
                bytes = gson.toJson(array).getBytes();


                Intent intent = new Intent(this, Submit_Location.class);
                intent.putExtra("bytes", bytes);
                startActivity(intent);
                //making the arraylist empty

                while (array.size() > 0)
                    array.remove(0);



            }
            else
            {
                Toast.makeText(this,"At least 3 points must be selected",Toast.LENGTH_SHORT).show();
            }


        }
        else
        {

            Toast.makeText(this,"no points selected",Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {


    }

    public class Info_Points
    {
        Double latitude;
        Double longitude;
        int sl_no;

        public Info_Points(Double latitude, Double longitude, int sl_no)
        {

            this.latitude = latitude;
            this.longitude = longitude;
            this.sl_no = sl_no;
        }
    }

    public  void checkLocationPermission()
    {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
        {

            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {

                getLocation();

            }
            else
            {

                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    Toast.makeText(this,"Application required is  access location",Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_PERMISSION_FINE_LOCATION_RESULT);
            }
        }
        else
        {
            //for android below mashmellow permissions are asked to grant
            //during installation
            getLocation();


        }
    }

}