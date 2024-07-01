package com.example.floodaware;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class WeatherFragment extends Fragment {
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "a6e4077d32902bcd26853ec81b4ed5c1";
    DecimalFormat df = new DecimalFormat("#.#");

    private final String SHARED_PREFS_02 = "location";
    SharedPreferences wSharedPreference;

    Context ctx;

    TextView descriptionWeather, temperatureTV, feelslikeTV, humidityTV ,pressureTV, windTV, cloudTV, visibleTV, cityNameTV;
    ImageView locationIV;
    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        descriptionWeather = view.findViewById(R.id.weatherDescriptionTV);
        temperatureTV = view.findViewById(R.id.temperatureTV);
        feelslikeTV = view.findViewById(R.id.feelslikeValTV);
        humidityTV = view.findViewById(R.id.humidityValIV);
        pressureTV = view.findViewById(R.id.pressureValTV);
        windTV = view.findViewById(R.id.windValTV);
        cloudTV = view.findViewById(R.id.cloudValTV);
        visibleTV = view.findViewById(R.id.visibilityValIV);
        cityNameTV = view.findViewById(R.id.cityNameTV);

        ctx = this.getActivity().getApplicationContext();

        weatherUpdate();

        Toast.makeText(ctx,"click location icon to change the location",Toast.LENGTH_LONG).show();
        locationIV = view.findViewById(R.id.locationIV);
        locationIV.setOnClickListener(v -> {
            ((HomeScreen)getActivity()).getLocationDialog();
        });

        return view;
    }

    public void weatherUpdate(){

        wSharedPreference = this.getActivity().getSharedPreferences(SHARED_PREFS_02,MODE_PRIVATE);
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
                    Toast.makeText(ctx, error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity().getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

}