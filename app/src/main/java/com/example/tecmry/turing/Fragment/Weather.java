package com.example.tecmry.turing.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tecmry.turing.Activity.MainActivity;
import com.example.tecmry.turing.R;
import com.example.tecmry.turing.Util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
public class Weather extends Fragment{
    private Uri uri;
    private HttpURLConnection  httpURLConnection;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.weather,container,false);
        return  view;
    }
    public void getData(){
    }

}
*/
public class Weather extends Fragment {
    private static URL url = null;
    private URL image_url = new URL("http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN");
    private TextView City;
    private TextView tv_Weather;
    private TextView tv_Weather_string;
    private TextView tv_weather_aqi;
    private TextView tv_suggestion_air;
    private TextView tv_suggestion_car;
    private TextView tv_suggestion_sport;
    private TextView tv_suggestion_airtitle;
    private TextView tv_suggestion_cartitle;
    private TextView tv_suggestion_sporttitle;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imageView;
    private AppBarLayout appBarLayout;
    private int weather;
    private String getContent;
    private String addUrl = "http://s.cn.bing.net";
    private String conds;
    private static String whiteurl;
    private Toolbar toolbar;
    private static final int imageurl = 1;
    private Handler handler;
    private SharedPreferences.Editor   editor;
    private Context mContext;
    public Weather() throws MalformedURLException {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_weather ,container ,false);



        City = (TextView)view.findViewById(R.id.tv_city_name);
        tv_Weather = (TextView)view.findViewById(R.id.tv_temp);
        tv_Weather_string = (TextView) view.findViewById(R.id.tv_weather_string);
        tv_suggestion_car = (TextView)view.findViewById(R.id.tv_suggestion_car_info);
        tv_suggestion_air = (TextView)view.findViewById(R.id.tv_suggestion_air_info);
        tv_suggestion_sport = (TextView) view.findViewById(R.id.tv_suggestion_out_info);
        tv_suggestion_airtitle =(TextView)view.findViewById(R.id.tv_suggestion_air);
        tv_suggestion_cartitle = (TextView)view.findViewById(R.id.tv_suggestion_car);
        tv_suggestion_sporttitle = (TextView)view.findViewById(R.id.tv_suggestion_out);
        tv_weather_aqi = (TextView)view.findViewById(R.id.tv_weather_aqi);
        collapsingToolbarLayout = (CollapsingToolbarLayout)view.findViewById(R.id.toolbar_layout);
        imageView = (ImageView)view.findViewById(R.id.bannner);
        appBarLayout= (AppBarLayout)view.findViewById(R.id.app_bar);


        getContent=((MainActivity)getActivity()).getCity();
        editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        mContext = getContext();
        if(getContent!=null) {
            getData(getContent);
           getPhoto();
            //loadImage();
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case  0:
                            City.setText(msg.obj.toString());
                            break;
                        case imageurl:
                            whiteurl = String.valueOf(msg.obj);
                            editor.putString("bing_pic",whiteurl);
                            editor.apply();
                            Glide.with(mContext).load(whiteurl).into(imageView);
                            break;
                    }
                }
            };
        }
        /*
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                toolbar.setBackgroundColor(changeAlpha(getResources().getColor(R.color.colorPrimary),Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange()));
            }
        });*/
        if(Build.VERSION.SDK_INT>=21){
            View decoreview= getActivity().getWindow().getDecorView();
            decoreview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        System.out.println(whiteurl+"url");

       /**加上之后不能联网 setHasOptionsMenu(true);*/
        return view;
    }


    public void getData(final String city){
        try {
            url = new URL("https://free-api.heweather.com/v5/weather?city="+city
                    + "&key="+"a397068ad3234664b41d47d975ff4d3a");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    int resposecode= httpURLConnection.getResponseCode();
                    Log.d( "Weather-ResponseCode",String.valueOf(resposecode));
                    if (resposecode==200){
                        InputStream inputStream= httpURLConnection.getInputStream();
                        String json = Utils.readString(inputStream);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray heweather= jsonObject.getJSONArray("HeWeather5");
                            JSONObject jsonObject1 = heweather.getJSONObject(0);
                            JSONObject aqi = jsonObject1.getJSONObject("aqi");
                            JSONObject city=aqi.getJSONObject("city");
                            final  String city_qlty = city.getString("qlty");
                            System.out.println(city_qlty);
                            JSONObject basic = jsonObject1.getJSONObject("basic");
                            JSONObject time = basic.getJSONObject("update");
                            String tv_time = time.getString("loc");
                            final String city_s=basic.getString("city");
                            JSONObject now = jsonObject1.getJSONObject("now");
                            JSONObject cond = now.getJSONObject("cond");
                            weather = now.getInt("tmp");
                            System.out.println("weather:"+ weather);
                            conds = cond.getString("txt");
                            JSONObject suggestion  = jsonObject1.getJSONObject("suggestion");
                            JSONObject carsuggestion = suggestion.getJSONObject("cw");
                            final String car_suggestion =carsuggestion.getString("txt");
                            final String car = carsuggestion.getString("brf");
                            Log.d("airSuggestion",String.valueOf(suggestion));
                            JSONObject airsuggestion = suggestion.getJSONObject("comf");
                            final String air =airsuggestion.getString("brf");
                            final String air_suggestion = airsuggestion.getString("txt");
                            JSONObject outsuggestion = suggestion.getJSONObject("sport");
                            final String out = outsuggestion.getString("brf");
                            final String out_suggestion = outsuggestion.getString("txt");
                            tv_suggestion_cartitle.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_suggestion_cartitle.setText("空气指数 —— "+car);
                                }
                            });
                            tv_suggestion_sporttitle.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_suggestion_sporttitle.setText("运动指数 —— "+out);
                                }
                            });
                            tv_suggestion_airtitle.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_suggestion_airtitle.setText("空气指数 —— "+air);
                                }
                            });
                            tv_suggestion_sport.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_suggestion_sport.setText(out_suggestion);                                }
                            });
                            tv_suggestion_car.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_suggestion_car.setText(car_suggestion);
                                }
                            });
                            tv_suggestion_air.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_suggestion_air.setText(air_suggestion);
                                }
                            });
                            tv_weather_aqi.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_weather_aqi.setText(city_qlty);
                                }
                            });
                            tv_Weather_string.post(new Runnable() {
                                @Override
                                public void run() {
                                tv_Weather_string.setText(conds);
                                }
                            });

                            collapsingToolbarLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("City_name"+city_s);
                                    collapsingToolbarLayout.setTitle(city_s);
                                }
                            });
                            tv_Weather.post(new Runnable() {
                                @Override
                                public void run() {
                                    String w = String.valueOf(weather);
                                    tv_Weather.setText(w);
                                }
                            });
                            Message message =Message.obtain();
                            message.obj=city_s;
                            message.what = 0;
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(getContext(),"检查下是不是城市输入错误呢",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(getContext(),"服务器可能懵逼了",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void getPhoto(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection)image_url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    if (httpURLConnection.getResponseCode()==200){
                        InputStream inputStream= httpURLConnection.getInputStream();
                        String json = Utils.readString(inputStream);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray jsonArray = jsonObject.getJSONArray("images");
                            JSONObject url = jsonArray.getJSONObject(0);
                            whiteurl = addUrl+url.getString("url");
                            Message message = Message.obtain();
                            message.obj=whiteurl;
                            System.out.println(message.obj+"obj");
                            message.what = imageurl;
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /** 根据百分比改变颜色透明度 */
        /*
    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }*/
        /**
    public void loadImage(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                switch (message.what){
                    case imageurl:
                        whiteurl = String.valueOf(message.obj);
                        editor.putString("bing_pic",whiteurl);
                        editor.apply();
                        Glide.with(mContext).load(whiteurl).into(imageView);
                        break;
                }
            }
        };

    }*/

}
