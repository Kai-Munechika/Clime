package edu.kaimbu.stormy3.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.kaimbu.stormy3.R;
import edu.kaimbu.stormy3.weather.Current;
import edu.kaimbu.stormy3.weather.Day;
import edu.kaimbu.stormy3.weather.Forecast;
import edu.kaimbu.stormy3.weather.Hour;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String TAG = MainActivity.class.getSimpleName();
    private Forecast mForecast;

    @BindView(R.id.timeLabel) TextView mTimeLabel;
    @BindView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @BindView(R.id.humidityValue) TextView mHumidityValue;
    @BindView(R.id.precipValue) TextView mPrecipValue;
    @BindView(R.id.summaryLabel) TextView mSummaryLabel;
    @BindView(R.id.iconImageView) ImageView mIconImageView;
    @BindView(R.id.refreshImageView) ImageView mRefreshImageView;
    @BindView(R.id.realFeelLabel) TextView mRealFeelLabel;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final double latitude = 42.3501196;
        final double longitude = -71.0979029;

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude, longitude);
            }
        });


        getForecast(latitude, longitude);
    }

    private void getForecast(double latitude, double longitude) {
        final String apiKey = "033b9c41808cc735ce7d4c8c1f234d4f";
        String forecastUrl = "https://api.darksky.net/forecast/" + apiKey + "/" +
                latitude + "," + longitude;

        if (isNetworkAvailable()) {

            toggleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastUrl).build();
            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                            alertUserAboutError();
                        }
                    });

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    try {
                        String jsonData = response.body().string();
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        }
                        else {
                            alertUserAboutError();
                        }
                    }
                    catch (IOException | JSONException e) {
                        Log.v(TAG, "Error:", e);
                    }

                }
            });
        }

        else {
            Toast.makeText(this, "Network is unavailable", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick (R.id.dailyButton)
    public void startDailyActivity(View view) {
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, mForecast.getDayForecast());
        startActivity(intent);
    }


    // concerned with only current weather, since it's the main activity
    private void updateDisplay() {
        Current current = mForecast.getCurrent();
        mTemperatureLabel.setText(String.valueOf((current.getTemperature())));
        mRealFeelLabel.setText("RealFeel: " + current.getApparentTemperature() + "\u00b0");
        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mSummaryLabel.setText(current.getSummary());
        mIconImageView.setImageResource(current.getIconId());

    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();

        forecast.setCurrent(getCurrentForecast(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDayForecast(getDailyForecast(jsonData));

        return forecast;

    }

    private Day[] getDailyForecast(String jsonData) throws JSONException {
        JSONObject forecast =  new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];

        for (int i = 0; i < data.length(); i++ ) {
            JSONObject jsonDay = data.getJSONObject(i);
            days[i] = new Day();
            days[i].setTemperatureMax(jsonDay.getDouble("apparentTemperatureMin"));
            days[i].setIcon(jsonDay.getString("icon"));
            days[i].setSummary(jsonDay.getString("summary"));
            days[i].setTimezone(timezone);
            days[i].setTime(jsonDay.getLong("time"));
        }


        return days;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast =  new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];

        for (int i = 0; i < data.length(); i++ ) {
            JSONObject jsonHour = data.getJSONObject(i);
            hours[i] = new Hour();
            hours[i].setTemperature(jsonHour.getDouble("temperature"));
            hours[i].setIcon(jsonHour.getString("icon"));
            hours[i].setSummary(jsonHour.getString("summary"));
            hours[i].setTimezone(timezone);
            hours[i].setTime(jsonHour.getLong("time"));
        }

        return hours;
    }

    private Current getCurrentForecast(String jsonData) throws JSONException {
        JSONObject forecast =  new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();
        current.setTemperature(currently.getDouble("temperature"));
        current.setHumidity(currently.getDouble("humidity"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTime(currently.getLong("time"));
        current.setTimezone(timezone);
        current.setApparentTemperature(currently.getDouble("apparentTemperature"));

        return current;

    }





























    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }
        else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}
