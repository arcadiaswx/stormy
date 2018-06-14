package com.arcadiasoftworks.stormy.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcadiasoftworks.stormy.R;
import com.arcadiasoftworks.stormy.weather.Current;
import com.arcadiasoftworks.stormy.databinding.ActivityMainBinding;
import com.arcadiasoftworks.stormy.weather.Forecast;
import com.arcadiasoftworks.stormy.weather.Hour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

  public static final String TAG = MainActivity.class.getSimpleName();
  private Forecast forecast;
  private ImageView iconImageView;

  double latitude = 37.8267;
  double longitude = -122.4233;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getForecast(latitude, longitude);
    Log.d(TAG, "Main UI code is running!");
  }

  private void getForecast(double latitude, double longitude) {
    final ActivityMainBinding binding = DataBindingUtil
            .setContentView(MainActivity.this, R.layout.activity_main);

    iconImageView = findViewById(R.id.iconImageView);

    // Setup Dark Sky Link
    TextView darkSky = findViewById(R.id.darkSkyAttribution);
    darkSky.setMovementMethod(LinkMovementMethod.getInstance());

    String apiKey = "a371e6cef5025d37a4a1039c6d26009b";
    String forecastURL = "https://api.darksky.net/forecast/" + apiKey +
            "/" + latitude + "," + longitude;

    if (isNetworkAvailable()) {
      OkHttpClient client = new OkHttpClient();

      Request request = new Request.Builder()
              .url(forecastURL)
              .build();

      Call call = client.newCall(request);
      call.enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
          try {
            String jsonData = response.body().string();
            Log.v(TAG, jsonData);
            if (response.isSuccessful()) {
              forecast = parseForecastData(jsonData);

              Current current = forecast.getCurrent();

              final Current displayWeather = new Current(
                      current.getLocationLabel(),
                      current.getIcon(),
                      current.getTime(),
                      current.getTemperature(),
                      current.getHumidity(),
                      current.getPrecipChance(),
                      current.getSummary(),
                      current.getTimeZone()
              );

              binding.setWeather(displayWeather);

              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  Drawable drawable = getResources().getDrawable(displayWeather.getIconId());
                  iconImageView.setImageDrawable(drawable);
                }
              });

            } else {
              alertUserAboutError();
            }
          } catch (IOException e) {
            Log.e(TAG, "IO Exception caught: ", e);
          } catch (JSONException e) {
            Log.e(TAG, "JSON Exception caught: ", e);
          }

        }
      });
    }
    else {
      Toast.makeText(this, getString(R.string.network_unavailable_message),
              Toast.LENGTH_LONG).show();
    }
  }

  private Forecast parseForecastData(String jsonData) throws JSONException {
      Forecast forecast = new Forecast();

      forecast.setCurrent(getCurrentDetails(jsonData));
      forecast.setHourlyForecast(getHourlyForecast(jsonData));
      return forecast;
  }

  private Hour[] getHourlyForecast(String jsonData) throws JSONException {
    JSONObject forecast = new JSONObject(jsonData);
    String timezone = forecast.getString("timezone");

    JSONObject hourly = forecast.getJSONObject("hourly");
    JSONArray data = hourly.getJSONArray("data");

    Hour[] hours = new Hour[data.length()];

    for (int i = 0; i < data.length(); i++){
      JSONObject jsonHour = data.getJSONObject(i);

      Hour hour = new Hour();

      hour.setSummary(jsonHour.getString("summary"));
      hour.setIcon(jsonHour.getString("icon"));
      hour.setTemperature(jsonHour.getDouble("temperature"));
      hour.setTime(jsonHour.getLong("time"));
      hour.setTimeZone(timezone);

      hours[i] = hour;
    }
    return hours;
  }

  private Current getCurrentDetails(String jsonData) throws JSONException {
    JSONObject forecast = new JSONObject(jsonData);

    String timezone = forecast.getString("timezone");
    Log.i(TAG, "From JSON: " + timezone);

    JSONObject currently = forecast.getJSONObject("currently");

    Current current = new Current();

    // Parse weather data from currently object
    current.setHumidity(currently.getDouble("humidity"));
    current.setTime(currently.getLong("time"));
    current.setIcon(currently.getString("icon"));
    current.setLocationLabel("Alcatraz Island, CA");
    current.setPrecipChance(currently.getDouble("precipProbability"));
    current.setSummary(currently.getString("summary"));
    current.setTemperature(currently.getDouble("temperature"));
    current.setTimeZone(timezone);

    Log.d(TAG, current.getFormattedTime());

    return current;
  }

  private boolean isNetworkAvailable() {
    ConnectivityManager manager = (ConnectivityManager)
            getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = manager.getActiveNetworkInfo();

    boolean isAvailable = false;

    if (networkInfo != null && networkInfo.isConnected()) {
      isAvailable = true;
    }

    return isAvailable;
  }

  private void alertUserAboutError() {
    AlertDialogFragment dialog = new AlertDialogFragment();
    dialog.show(getFragmentManager(), "error_dialog");
  }

  public void refreshOnClick(View view) {
    getForecast(latitude, longitude);
    Toast.makeText(this, "Refreshing data", Toast.LENGTH_LONG).show();
  }

  public void hourlyOnClick(View view){
    List<Hour> hours = Arrays.asList(forecast.getHourlyForecast());

    Intent intent = new Intent(this, HourlyForecastActivity.class);
    intent.putExtra("HourlyList", (Serializable) hours);
    startActivity(intent);

  }
}
