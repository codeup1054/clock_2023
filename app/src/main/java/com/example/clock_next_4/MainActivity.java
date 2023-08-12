package com.example.clock_next_4;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.clock_next_4.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private ImageView plotImageView;

    String request_metrics = "temp,humidity,pressure_mm,wind_speed";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("@@@ onCreate","***");
        System.out.println("Oncreatetestlog onCreate() Restoring previous state");

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());


        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        plot("");


        Button updateButton = findViewById(R.id.button_parse);

        TextView pressure_mp_label_mm=(TextView)findViewById(R.id.pressure_pa);
        pressure_mp_label_mm.setText("999");


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
//                Log.d("@@ 02. onClick", "01. On click");
//                moveWeekDay();
//                jsonParse();

                pressure_mp_label_mm.setText("888");

                plot("");
                Log.d("updateButton","***");
                System.out.println("Oncreate testlog onCreate() Restoring previous state");
            }
        });


//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//
//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAnchorView(R.id.fab)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void plot(String param)
    {
        plotImageView = findViewById(R.id.plot_image_view);

        String  plotImageUrl = "https://gpxlab.ru/api/yw.php"+param;

        Log.d("@@ plotImageUrl", plotImageUrl);

        Glide.with(this).load(plotImageUrl)
//                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .thumbnail(
//                        Glide // this thumbnail request has to have the same RESULT cache key
//                                .with(this) // as the outer request, which usually simply means
//                                .load(plotImageUrl) // same size/transformation(e.g. centerCrop)/format(e.g. asBitmap)
//                                .fitCenter() // have to be explicit here to match outer load exactly
//                                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                .skipMemoryCache(true)
//                )
                .skipMemoryCache(true)
                .into(plotImageView);
    }
//
//    void plotParam(String metrics)
//    {
//        TextView view;
//        int id = getResources().getIdentifier(metrics+"_label", "id", getPackageName());
//        Log.d("@@ plotParam", metrics );
//        view = (TextView) findViewById(id);
//
//        view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                // TODO Auto-generated method stub
//
//                String color;
//
//                if (request_metrics.contains(","+metrics)) {
//                    request_metrics = request_metrics.replace(","+metrics, "");
//                    color = "#888888";
//                } else {
//                    request_metrics = request_metrics+","+metrics;
//                    color = "#cccccc";
//                }
//
//                TextView b = (TextView) v;
//
//                b.setTextColor(Color.parseColor(color));
//
//
//                Log.d("@@ metr", request_metrics);
//
//                plot("?m=" + request_metrics);
////                                    pressure_mp_label_mm.setTextColor("#1a8");
//
//                return true;
//            }
//        });
//
//    }


//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}