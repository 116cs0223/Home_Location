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
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback ,LocationListener
{

    private GoogleMap mMap;
    ArrayList<MapsActivity2.Info_Points> array = new ArrayList<MapsActivity2.Info_Points>();
    private Button btn1,btn2;
    TextView txtView=null;
    MapsActivity2.Info_Points info_points;
    private LocationManager locationManager;
    private static final int REQUEST_PERMISSION_FINE_LOCATION_RESULT=0;
    static Double latitude;
    static Double longitude;
    LatLng myLocation;
    static int i=2;
    byte[] bytes ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        btn1=(Button)findViewById(R.id.map2_click);
        btn2=(Button)findViewById(R.id.map2_finish);
        txtView=(TextView)findViewById(R.id.map2_textview);
        // Obtain the SupportMapFragment and get notified when the map is ready tof be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
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
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        checkLocationPermission();


    }
    public void onGo(View view)
    {


        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            info_points = new MapsActivity2.Info_Points(latitude, longitude, i - 1);
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
    public void onLocationChanged(Location location)
    {

        latitude=(location.getLatitude());
        longitude=(location.getLongitude());
        mMap.clear();
        LatLng myloc=new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(myloc).title("Marker in mylocation"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myloc,18));

        try
        {
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
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s)
    {


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
    public  class Info_Points
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
                //before inserting into database
                i=2;
                btn1.setText("Click-" + 1);

                //making the array empty for another input i.e setPosition
                while (array.size() > 0)
                    array.remove(0);

                startActivity(intent);
                //making the arraylist empty




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

}
