package com.example.clock_next_4;

import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;


import com.example.clock_next_4.databinding.ActivityMainBinding;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

/** volley */

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.VolleyError;
import com.android.volley.AuthFailureError;


/** glide */

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ImageView plotImageView;

    public static class WeatherMetrix {
        public String name;
        public String colorNow;
        public String colorForecast;

        int WeatherMetrix(String n, String c1, String c2) {
            name = n;
            colorNow = c1;
            colorForecast = c2;
            return 1;
        }

        public void showData() {
            System.out.print("EmpId = " + name + "  " + " Employee Name = " + colorNow);
            System.out.println();
        }
    }


    /**
     * "feels_like", "prec_mm", "prec_prob","wind_dir",    "wind_speed",
     * "temp_avg","temp_min", "pressure_pa", "pressure_mm", "humidity"};
     * '#cba',"#f40",'#4bf',"#ef7","#3fa", "#f5e"
     */

    TextView textDebug;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        textDebug = findViewById(R.id.debugText);

        plot("");

        Button buttonParse = findViewById(R.id.button_parse);

        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
//                Log.d("@@ 02. onClick", "01. On click");
                moveWeekDay();
                jsonParse();
                plot("");
            }
        });


        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
//                Log.d("@@ 02. onClick", "01. On click");
                moveWeekDay();
                textDebug.setText("");
                jsonParse();
                plot("");
            }
        });
    }

    /**  < ************  loop interval */

   private int mInterval = 500000; // 5 seconds by default, can be changed later
   private Timer autoUpdate;
    @Override

    public void onResume() {
        super.onResume();
        autoUpdate = new Timer();

        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        jsonParse();
                        moveWeekDay();
                        plot("");
                    }
                });
            }
        }, 0, mInterval); // updates each 40 secs
    }

/** plot draw */

    private void plot(String param) {
        plotImageView = findViewById(R.id.plot_image_view);

        String plotImageUrl = "https://gpxlab.ru/api/yw.php" + param;

        Log.d("@@ plotImageUrl", plotImageUrl);

        Glide.with(this).load(plotImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(plotImageView);
    }


    private void moveWeekDay() {

        TextView movedView = findViewById(R.id.WeekDayMoved);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int screenWidth = metrics.widthPixels;

        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) + 5;

        movedView.setX((float) ((dayOfWeek % 7 * screenWidth) / 7));

    }


    private void jsonParse() {
        setBattery();
        getMetrics();
    }

    private void setBattery() {
        BatteryManager bm = (BatteryManager) getApplicationContext().getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        TextView v = findViewById(R.id.battery);
        v.setText(String.valueOf(batLevel) + "%");
    }

    private void countDuration()
    {
        TextView v = findViewById(R.id.sunrise);
        String sunriseString = v.getText().toString();
        v = findViewById(R.id.sunset);
        String sunsetString = v.getText().toString();

        int hours = 0;
        int minutes = 0;



        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        try {
            Date sunriseStringParsed = format.parse(sunriseString);
            Date sunsetStringParsed = format.parse(sunsetString);
            long diff = sunsetStringParsed.getTime() - sunriseStringParsed.getTime();
            hours = (int) (diff / (1000 * 60 * 60));
            minutes = (int) (diff / (1000 * 60)) % 60;
        } catch (ParseException e) {
            textDebug.append("err "+ e + ">>>\n");
        }

        v = findViewById(R.id.duration);
        v.setText(String.format("%02d:%02d", hours, minutes));
    }

/**
    JSONObject jsonSample =
    {
        "now":1692097529,
        "now_dt":"2023-08-15T11:05:29.550232Z",
        "info":
        {
        "url":"https://yandex.ru/pogoda/213?lat=55.692\u0026lon=37.347",
        "lat":55.692,
        "lon":37.347
         },
    "fact":{
        "obs_time":1692097200, "temp":25, "feels_like":25, "icon":"bkn_d",
        "condition": "cloudy", "wind_speed":3, "wind_dir":"nw", "pressure_mm":750, "pressure_pa":999,
        "humidity": 56, "daytime":"d", "polar":false, "season":"summer", "wind_gust":9.4
    },
    "forecast":{
        "date":"2023-08-15",
        "date_ts":1692046800,
        "week":33,
        "sunrise":"05:01",
        "sunset": "20:08",
        "moon_code":7,
        "moon_text":"moon-code-7",
        "parts":[{
            "part_name":"evening", "temp_min":20, "temp_avg":23, "temp_max":25, "wind_speed":
            2.5, "wind_gust":7.3, "wind_dir":"nw", "pressure_mm":750, "pressure_pa":999, "humidity":
            65, "prec_mm":0, "prec_prob":0, "prec_period":240, "icon":"skc_d", "condition":
            "partly-cloudy", "feels_like":23, "daytime":"d", "polar":false
           },{
            "part_name":"night",
            "temp_min":14,
            "temp_avg":16,
            "temp_max":18,
            "wind_speed":1.4,
            "wind_gust":4.3,
            "wind_dir":"nw",
            "pressure_mm":751,
            "pressure_pa":1001,
            "humidity":86,
            "prec_mm":0,
            "prec_prob":0,
            "prec_period":480,
            "icon":"skc_n",
            "condition":"clear",
            "feels_like":16,
            "daytime":"n",
            "polar":false
}]}
    }
*/

    private void getMetrics() {
        String JSON_URL = "https://gpxlab.ru/api/clock.php?lat=55.692&lon=37.347";

//        textDebug.append("\nurl:" + JSON_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion

                        String[] dataMapper = new String[]{
                                "sunrise:forecast/sunrise",
                                "sunset:forecast/sunset",
                                "fact_temp:fact/temp",
                                "fact_humidity:fact/humidity",
                                "fact_pressure_mm:fact/pressure_mm",
                                "forecast_temp_avg:forecast/parts/1/temp_avg",
                                "forecast_temp_max:forecast/parts/1/temp_max",
                                "forecast_temp_min:forecast/parts/1/temp_min",
                                "forecast_feels_like:forecast/parts/1/feels_like",
                                "forecast_humidity:forecast/parts/1/humidity",
                                "forecast_pressure_mm:forecast/parts/1/pressure_mm",
                                "forecast_prec_prob:forecast/parts/1/prec_prob",
                                "forecast_prec_mm:forecast/parts/1/prec_mm",
                                "forecast_wind_dir:forecast/parts/1/wind_dir",
                                "forecast_wind_speed:forecast/parts/1/wind_speed",
                                "update_request_time:now_dt"
                        };

                        TextView LayoutView = null;

                        try {
                            JSONObject resPobj = new JSONObject(response);
                            JSONObject obj = null;
                            JSONArray forecastArray ;
                            String stringMetricValue = null;

                            for (int i = 0; i < dataMapper.length; i++) {

                                obj = resPobj;
                                String[] rule = dataMapper[i].split(":");
                                String viewName = rule[0];
                                String[] jsonPath = rule[1].split("/");

//                                textDebug.append(jsonPath.length + ":" + Arrays.toString( jsonPath) + ">>>\n");

                                for (int ii = 0; ii < jsonPath.length; ii++) {

                                    if (obj.get(jsonPath[ii]) instanceof JSONArray) {
                                        forecastArray = obj.getJSONArray(jsonPath[ii]);
                                        ii++;
                                        int arrIndex = Integer.parseInt(jsonPath[ii]);
                                        obj = forecastArray.getJSONObject(arrIndex);
                                    }
                                    else if (obj.get(jsonPath[ii]) instanceof JSONObject)
                                        obj = obj.getJSONObject(jsonPath[ii]);
                                    else
                                        stringMetricValue = obj.getString(jsonPath[ii]);
                                }


//                                textDebug.append("\n" + viewName + "," + stringMetricValue);
/** post procceccing for View */
                                switch (viewName){
                                    case ("update_request_time"):
                                        stringMetricValue = stringMetricValue.substring(0,19);
                                        break;
                                }

                                int id = getResources().getIdentifier(viewName, "id", getPackageName());
                                LayoutView = (TextView) findViewById(id);
                                LayoutView.setText(stringMetricValue);
                            }

                            countDuration();

                        } catch (JSONException e) {

                            textDebug.append("e:" + e);

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        textDebug.append("getMetrics:" + error);
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

}