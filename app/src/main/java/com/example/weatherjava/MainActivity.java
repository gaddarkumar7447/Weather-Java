package com.example.weatherjava;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Timer;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    EditText et_get_City_Name;
    TextView tv_date_and_time, tv_day_max_temp, tv_day_min_temp, tv_temp, tv_feels_like,
            tv_weather_type, tv_pressure, tv_humidity, tv_wind_speed, tv_sunrise, tv_sunset_speed, tv_temp_farenhite;
    ImageView iv_weather_icon, iv_weather_bg;
    ProgressBar pb_loading;
    String cityName;
    RelativeLayout rl_sub_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_get_City_Name = findViewById(R.id.et_get_City_Name);
        tv_day_max_temp = findViewById(R.id.tv_day_max_temp);
        tv_day_min_temp = findViewById(R.id.tv_day_min_temp);
        tv_temp = findViewById(R.id.tv_temp);
        tv_feels_like = findViewById(R.id.tv_feels_like);
        tv_weather_type = findViewById(R.id.tv_weather_type);
        tv_pressure = findViewById(R.id.tv_pressure);
        tv_humidity = findViewById(R.id.tv_humidity);
        tv_sunrise = findViewById(R.id.tv_sunrise);
        tv_sunset_speed = findViewById(R.id.tv_sunset_speed);
        tv_wind_speed = findViewById(R.id.tv_wind_speed);
        tv_temp_farenhite = findViewById(R.id.tv_temp_farenhite);
        tv_date_and_time = findViewById(R.id.tv_date_and_time);
        iv_weather_icon = findViewById(R.id.iv_weather_icon);
        pb_loading = findViewById(R.id.pb_loading);
        iv_weather_bg = findViewById(R.id.iv_weather_bg);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.statusBarCol));



        et_get_City_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityName = et_get_City_Name.getText().toString().trim();
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if (cityName.isEmpty()){
                    et_get_City_Name.setError("Enter cite name");
                }else {
                    //imm.hideSoftInputFromWindow(et_get_City_Name.getWindowToken(), 0);
                    fetchWeather();
                }
            }
        });
        
    }
    public void fetchWeather(){
        String apiKey = "08b62ac5227740100736c05653942275";
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid="+apiKey;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_ob = response.getJSONObject("main");
                    JSONArray jsonArray = response.getJSONArray("weather");
                    JSONObject object = jsonArray.getJSONObject(0);
                    String temp = main_ob.getString("temp");
                    String weatherType = object.getString("main");
                    String feelLikes = main_ob.getString("feels_like");
                    String minTem = main_ob.getString("temp_min");
                    String maxTemp = main_ob.getString("temp_max");
                    String pressure = main_ob.getString("pressure");
                    String huminity = main_ob.getString("humidity");


                    JSONObject aboutWind = response.getJSONObject("wind");
                    String windSpeed = aboutWind.getString("speed");

                    JSONObject aboutSun = response.getJSONObject("sys");
                    String sunRise = aboutSun.getString("sunrise");
                    String sunSet = aboutSun.getString("sunset");

                    if(weatherType.equals("Thunderstorm")) {
                        iv_weather_icon.setImageResource(R.drawable.thunderstorm);
                        iv_weather_bg.setImageResource(R.drawable.rain_bg);
                        tv_weather_type.setText("Thunderstorm");
                    }
                    else if(weatherType.equals("Drizzle") || weatherType.equals("Rain")) {
                        iv_weather_icon.setImageResource(R.drawable.raining);
                        iv_weather_bg.setImageResource(R.drawable.rain_bg);
                        tv_weather_type.setText("Raining");
                    }
                    else if(weatherType.equals("Snow")) {
                        iv_weather_icon.setImageResource(R.drawable.snowing);
                        iv_weather_bg.setImageResource(R.drawable.snow_bg);
                        tv_weather_type.setText("Snow");
                    }
                    else if(weatherType.equals("Mist") || weatherType.equals("Smoke") || weatherType.equals("Haze") || weatherType.equals("Dust")
                            || weatherType.equals("Fog") || weatherType.equals("Sand") || weatherType.equals("Ash") || weatherType.equals("Squall")
                            || weatherType.equals("Tornado")) {
                        iv_weather_icon.setImageResource(R.drawable.mist);
                        iv_weather_bg.setImageResource(R.drawable.mist_bg);
                        tv_weather_type.setText("Mist");
                    }
                    else if(weatherType.equals("Clear")) {
                        iv_weather_icon.setImageResource(R.drawable.sunny);
                        iv_weather_bg.setImageResource(R.drawable.clear_bg);
                        tv_weather_type.setText("Clear");
                    }
                    else if(weatherType.equals("Clouds")) {
                        iv_weather_icon.setImageResource(R.drawable.broken_cloud);
                        iv_weather_bg.setImageResource(R.drawable.cloud_bg);
                        tv_weather_type.setText("Cloudy");
                    }


                    double MAxTemp = Double.parseDouble(maxTemp) - 273.15;
                    int ma = (int) Math.round(MAxTemp);
                    tv_day_max_temp.setText("Max: "+String.valueOf(ma)+"°C");

                    double MinTe = Double.parseDouble(minTem) - 273.15;
                    int min = (int) Math.round(MinTe);
                    tv_day_min_temp.setText("Min: "+String.valueOf(min)+"°C");

                    LocalDateTime myDateObj = LocalDateTime.now();
                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm a");
                    String formattedDate = myDateObj.format(myFormatObj);
                    tv_date_and_time.setText(formattedDate);

                    double temInt = Double.parseDouble(temp) - 273.15;
                    int i = (int) Math.round(temInt);
                    tv_temp.setText(String.valueOf(i)+"°C");

                    tv_wind_speed.setText(windSpeed+" km/h");

                    long sunRiseTime = Long.parseLong(sunRise);
                    Date date = new Date(sunRiseTime*1000L);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat myDate = new SimpleDateFormat("HH:mm a");
                    String formatted = myDate.format(date);
                    tv_sunrise.setText(formatted);

                    long sunSetTime = Long.parseLong(sunSet);
                    Date date1 = new Date(sunSetTime*1000L);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat myDate1 = new SimpleDateFormat("hh:mm a");
                    String formatted1 = myDate.format(date1);
                    tv_sunset_speed.setText(formatted1);

                    double temInt1 = Double.parseDouble(maxTemp) - 273.15;
                    int i1 = (int) Math.round(temInt);
                    double f = ((double) i1 * 9/5) + 32;
                    int original = (int) f;
                    tv_temp_farenhite.setText(String.valueOf(original)+"°f");

                    tv_humidity.setText(huminity+"%");
                    tv_pressure.setText(pressure);

                    double feel = Double.parseDouble(feelLikes) - 273.15;
                    String s = String.format("%1.1f",feel);
                    tv_feels_like.setText(String.valueOf(s)+"°C");



                }catch (JSONException e){
                    e.getStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                et_get_City_Name.setError("Enter valid city name");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }
}