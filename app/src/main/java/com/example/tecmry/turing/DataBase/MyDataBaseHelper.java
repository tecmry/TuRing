package com.example.tecmry.turing.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class MyDataBaseHelper extends SQLiteOpenHelper {
    private Context mContext;
    public static final String CREATE_WEATHER="create table weather ("
            + "id integer, "
            + "weather integer, "
            + "airindex integer)";

    public static final String CREATE_PROVINCE="create table province ("
            + "id integer, "
            + "province text)";
    public static final String CREATE_CITY="create table city("
            + "id integer, "
            + "provincecode integer, "
            + "city text)";
    public static final String CREATE_COUNTY="create table county("
            + "citycode integer, "
            + "weathercode integer, "
            + "id integer, "
            + "county text)";

    public MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WEATHER);
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
        Toast.makeText(mContext,"create succeed",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists weather");
        db.execSQL("drop table if exists province");
        db.execSQL("drop table if exists city");
        db.execSQL("drop table if exists county");
        onCreate(db);
    }
}
