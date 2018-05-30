package com.example.hp.home_location;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public  class DBhelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    public static  final String DatabaseName="mydatabase_ex";
    public static final int Database_Version=1;
    public static final String Table_Name="area_info";


    DBhelper(Context context)

    {

        super(context,DatabaseName,null,Database_Version);

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("Create table if  not exists "  + Table_Name + "(building_name varchar PRIMARY KEY,info_points blob)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)

    {
        db.execSQL("drop table if exists " + Table_Name);
        onCreate(db);

    }
    public boolean db_insert(String building_name,byte[] info_points)
    {

        /*String str="Insert into "+Table_Name+" values (" + building_name +"," +info_points+ ")";
        db=getWritableDatabase();
        db.execSQL(str);*/
        db = getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("Building_name",building_name);
        values.put("info_points",info_points);
        Long no_of_rows_affected = db.insert(Table_Name,null,values);
        db.close();
        if(no_of_rows_affected==-1)
            return false;
        return true;
    }
    public Cursor getData()
    {
        db=getReadableDatabase();
        /*String str="select ?,? from "+Table_Name;
        String select[]={"Building_name","info_points"};*/
        String str="select * from "+Table_Name;
        Cursor cursor=db.rawQuery(str,null);
        return cursor;

    }
}
