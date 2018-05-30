package com.example.hp.home_location;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    private Button bt1,bt2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1=(Button)findViewById(R.id.btn1);
        bt2=(Button)findViewById(R.id.btn2);

    }
    public void onSetPos(View view)
    {
        Intent intent = new Intent(this,First_Activity.class);
        startActivity(intent);
    }
    public void onSeePos(View view)
    {
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
    }
}
