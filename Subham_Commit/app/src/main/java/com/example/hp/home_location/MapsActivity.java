package com.example.hp.home_location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener
{
    static Cursor cursor = null;
    private GoogleMap mMap;
    private static TextView txtView;
    public static final int REQUEST_PERMISSION_FINE_LOCATION_RESULT = 0;
    private LocationManager locationManager;
    Double latitude;
    Double longitude;
    LatLng myLocation;
    DBhelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        try {

            dBhelper = new DBhelper(this);
            cursor = dBhelper.getData();
        } catch (Exception e)
        {
            Toast.makeText(this, " Exception in fetching data", Toast.LENGTH_LONG).show();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
        txtView = (TextView) findViewById(R.id.textView2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLocation();

            } else {

                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                {

                    Toast.makeText(this, "Application requires to  access location", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FINE_LOCATION_RESULT);

            }
        } else
            {
            getLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            getLocation();
        }
        else
        {
            Toast.makeText(this,"No permission for location access",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {

        try
        {

            mMap.clear();
            latitude = (location.getLatitude());
            longitude = (location.getLongitude());
            txtView.setText("latitude:" + latitude + "\n" + "longitude :" + longitude + "\n" + cursor.toString());
            myLocation = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(myLocation).title("Marker is my location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
            String final_output = getPlace_Name(latitude, longitude);
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();

            txtView.setText(final_output);
            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();


        }
        catch (Exception e)
        {

            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
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


        Toast.makeText(MapsActivity.this, "Please Enable GPS", Toast.LENGTH_SHORT).show();
    }

    void getLocation() {
        try
        {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);


        }
        catch (SecurityException e)
        {

            e.printStackTrace();
            Toast.makeText(this, "Some permission not available in manifest", Toast.LENGTH_SHORT).show();
        }

    }

    String getPlace_Name(double lati, double longi)
    {

        int count = 0;
        boolean check = false;
        String build_name = null;
        byte[] bytearray;
        while (cursor.moveToNext()) {
            Toast.makeText(this,"inside while loop",Toast.LENGTH_LONG).show();
            //int k=5/0;
            build_name = cursor.getString(cursor.getColumnIndex("building_name"));
            bytearray = cursor.getBlob(cursor.getColumnIndex("info_points"));
            Toast.makeText(this, "3", Toast.LENGTH_LONG).show();

            String json = new String(bytearray);
            Gson gson = new Gson();
            Toast.makeText(this, "4", Toast.LENGTH_LONG).show();

            ArrayList<First_Activity.Info_Points> arrayList = gson.fromJson(json, new TypeToken<ArrayList<First_Activity.Info_Points>>() {
            }.getType());


            //First_Activity.Info_Points info_points_array[]=new  First_Activity.Info_Points[arrayList.size()];
            Toast.makeText(this, "5", Toast.LENGTH_LONG).show();
           /* First_Activity.Info_Points i_array[]=null;
            try {
                 i_array = (First_Activity.Info_Points[]) arrayList.toArray();

                //First_Activity.Info_Points i_array[] = (First_Activity.Info_Points[]) arrayList.toArray();
            }

            catch (Exception ase)
            {
                Toast.makeText(this, "ArrayStoreException", Toast.LENGTH_SHORT).show();

            }*/
            First_Activity.Info_Points i_array[] = new First_Activity.Info_Points[arrayList.size()];
            for (int i=0;i<arrayList.size();i++)
            {
                i_array[i]=arrayList.get(i);
            }

            Toast.makeText(this, "6", Toast.LENGTH_LONG).show();

            for (int i = 0; i < i_array.length; i++) {
                double[] p1 = {i_array[i].latitude, i_array[i].longitude};
                double[] p2 = {i_array[(i + 1) % i_array.length].latitude, i_array[(i + 1) % i_array.length].longitude};
                int return_value = check_point_line(lati, longi, p1, p2);
                if(return_value==-1)
                {
                    cursor.moveToPosition(-1);
                    return "present inside "+build_name;
                }
                else if(return_value==1)
                {
                    count++;
                }

            }
            Toast.makeText(this, "7", Toast.LENGTH_LONG).show();

            if(count%2==1)
            {
                cursor.moveToPosition(-1);
                return "present inside "+build_name;
            }




        }

        cursor.moveToPosition(-1);
        return "Not inside your saved areas";
    }

     int check_point_line(double x, double y, double p1[], double p2[])
    {
        Toast.makeText(this,"inside check_point ",Toast.LENGTH_LONG).show();
        double x_diff1 = p1[0] - x;
        double x_diff2 = x - p2[0];
        double y_diff1 = p1[1] - y;
        double y_diff2 = y - p2[1];
        if ((x == p1[0] && y == p1[1]) || (x == p2[0] && y == p2[1])) {
            return -1;
        }
        if ((p2[1] - y) * (x - p1[0]) == (y - p1[1]) * (p2[0] - x)) {

            if ((x_diff1 * x_diff2) > 0)
                return -1;
            else if ((x_diff1 * x_diff2) < 0)
                return 0;
            else {
                if (y_diff1 * y_diff2 > 0)
                    return -1;
                else if (y_diff1 * y_diff2 < 0)
                    return 0;

            }
        } else {
            if (y_diff1 * y_diff2 >= 0)
            {

                // for line extending to right  of the given point
                // merges to this line
                if (p1[1] == p2[1]) {
                    if (y == p1[0])
                        return -1;
                    else
                        return 0;

                } else {
                    double x_prediction = p1[0] + (y - p1[1]) * ((p1[0] - p2[0]) / (p1[1] - p2[1]));
                    if (x_prediction > x)
                        return 1;
                    else if (x_prediction == x)
                        return -1;
                    else
                        return 0;

                }

            } else
                return 0;
        }
        return 0;
    }



}
