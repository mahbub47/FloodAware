package com.example.floodaware;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class WeatherUpdate extends AppCompatActivity {

    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "a6e4077d32902bcd26853ec81b4ed5c1";
    DecimalFormat df = new DecimalFormat("#.#");

    TextView descriptionWeather, temperatureTV, feelslikeTV, humidityTV ,pressureTV, windTV, cloudTV, visibleTV, cityNameTV;
    ImageView backWeather;
    ImageView locationIV;

    private final String SHARED_PREFS_02 = "location";
    SharedPreferences wSharedPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        descriptionWeather = findViewById(R.id.weatherDescriptionTV);
        temperatureTV = findViewById(R.id.temperatureTV);
        feelslikeTV = findViewById(R.id.feelslikeValTV);
        humidityTV = findViewById(R.id.humidityValIV);
        pressureTV = findViewById(R.id.pressureValTV);
        windTV = findViewById(R.id.windValTV);
        cloudTV = findViewById(R.id.cloudValTV);
        visibleTV = findViewById(R.id.visibilityValIV);
        cityNameTV = findViewById(R.id.cityNameTV);
        backWeather = findViewById(R.id.backIconWeather);

        weatherUpdate();

        backWeather.setOnClickListener(v -> {
            startActivity(new Intent(WeatherUpdate.this,HomeScreen.class));
            finish();
        });

        locationIV = findViewById(R.id.locationIV);
        locationIV.setOnClickListener(v -> {
            getLocationDialog();
        });
        Toast.makeText(WeatherUpdate.this,"click location icon to change the location",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(WeatherUpdate.this, HomeScreen.class));
        finish();
    }


    private void getLocationDialog() {
        Dialog dialog = new Dialog(WeatherUpdate.this,R.style.DialogStyle);
        dialog.setContentView(R.layout.setlocation_popup_screen);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.popup_back_shape);
        dialog.show();
        ImageView cancelIV = dialog.findViewById(R.id.cancelIV);
        cancelIV.setOnClickListener(v -> {
            dialog.dismiss();
        });

        Button setLocationBtn = dialog.findViewById(R.id.setLocationBtn);
        EditText setLocationET = dialog.findViewById(R.id.setLocationET);
        setLocationBtn.setOnClickListener(v -> {
            String city = setLocationET.getText().toString();
            wSharedPreference = getSharedPreferences(SHARED_PREFS_02, MODE_PRIVATE);
            SharedPreferences.Editor editor = wSharedPreference.edit();
            editor.putString(SHARED_PREFS_02, city);
            editor.apply();

            weatherUpdate();

            dialog.dismiss();
        });
    }

    public void weatherUpdate(){

        wSharedPreference = getSharedPreferences(SHARED_PREFS_02,MODE_PRIVATE);
        String city = wSharedPreference.getString(SHARED_PREFS_02,"London");
        String tempUrl = "";
        String country = "";
        if (city.equals("")){

        }else{
            if(!country.equals("")){
                tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid;
            }else{
                tempUrl = url + "?q=" + city + "&appid=" + appid;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);

                        String description = jsonObjectWeather.getString("description");
                        descriptionWeather.setText(description);

                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        String tempVal = df.format(temp) + "º";
                        temperatureTV.setText(tempVal);
                        double fellslike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        String feelslikeVal = df.format(fellslike) + "º";
                        feelslikeTV.setText(feelslikeVal);
                        double humidity = jsonObjectMain.getDouble("humidity");
                        String humidityVal = df.format(humidity) + "%";
                        humidityTV.setText(humidityVal);
                        double pressure = jsonObjectMain.getDouble("pressure");
                        String pressureVal = df.format(pressure) + " hPa";
                        pressureTV.setText(pressureVal);

                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        double speed = jsonObjectWind.getDouble("speed");
                        String speedVal = df.format(speed) + " m/s";
                        windTV.setText(speedVal);

                        JSONObject jsonObjectCloud = jsonResponse.getJSONObject("clouds");
                        double cloud = jsonObjectCloud.getDouble("all");
                        String cloudVal = df.format(cloud) + "%";
                        cloudTV.setText(cloudVal);

                        double visibility = jsonResponse.getDouble("visibility");
                        String visible = df.format(visibility) + " m";
                        visibleTV.setText(visible);

                        String cityName = jsonResponse.getString("name");
                        cityNameTV.setText(cityName);


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}