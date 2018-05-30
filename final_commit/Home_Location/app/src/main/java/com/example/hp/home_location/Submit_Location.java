package com.example.hp.home_location;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Submit_Location extends AppCompatActivity
{


    EditText editText;
    byte[] bytes;
    String location;
    DBhelper dbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit__location);
        Intent intent = getIntent();
        bytes = intent.getByteArrayExtra("bytes");
        editText=(EditText)findViewById(R.id.editText);
        dbhelper = new DBhelper(this);


    }
    public void submit(View view)
    {
        location = editText.getText().toString();
       Boolean check= dbhelper.db_insert(location,bytes);
        Toast.makeText(this,check.toString(),Toast.LENGTH_SHORT).show();
    }
}
