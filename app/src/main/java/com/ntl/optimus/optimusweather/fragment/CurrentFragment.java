package com.ntl.optimus.optimusweather.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntl.optimus.optimusweather.MainActivity;
import com.ntl.optimus.optimusweather.R;
import com.ntl.optimus.optimusweather.SearchLocationActivity;
import com.ntl.optimus.optimusweather.util.WeatherContext;
import com.ntl.optimus.optimusweather.util.WeatherIconMapper;
import com.ntl.optimus.optimusweather.util.WeatherUtil;
import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.model.CurrentWeather;
import com.survivingwithandroid.weather.lib.model.Weather;
import com.survivingwithandroid.weather.lib.util.WindDirection;

public class CurrentFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private String cityId;
    private View mRootView;

    // UI elements
    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;
    private TextView unitTemp;
    private TextView hum;
    private ImageView imgView;
    private TextView tempMin;
    private TextView tempMax;
    private TextView sunset;
    private TextView sunrise;
    private TextView rain;

    private WeatherConfig config;
    private WeatherClient weatherClient;

    public static CurrentFragment newInstance() {
        CurrentFragment fragment = new CurrentFragment();
        return fragment;
    }

    public CurrentFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        weatherClient = WeatherContext.getInstance().getClient(getActivity());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.fragment_current, container, false);

        cityText = (TextView) mRootView.findViewById(R.id.location);
        temp = (TextView) mRootView.findViewById(R.id.temp);
        condDescr = (TextView) mRootView.findViewById(R.id.descrWeather);
        imgView = (ImageView) mRootView.findViewById(R.id.imgWeather);
        hum = (TextView) mRootView.findViewById(R.id.humidity);
        press = (TextView) mRootView.findViewById(R.id.pressure);
        windSpeed = (TextView) mRootView.findViewById(R.id.windSpeed);
        windDeg = (TextView) mRootView.findViewById(R.id.windDeg);
        tempMin = (TextView) mRootView.findViewById(R.id.tempMin);
        tempMax = (TextView) mRootView.findViewById(R.id.tempMax);
        unitTemp = (TextView) mRootView.findViewById(R.id.tempUnit);
        sunrise = (TextView) mRootView.findViewById(R.id.sunrise);
        sunset = (TextView) mRootView.findViewById(R.id.sunset);
        rain = (TextView) mRootView.findViewById(R.id.rain);

		return mRootView;
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== MainActivity.REQUEST_CODE_LOCATION) {
            if(resultCode == Activity.RESULT_OK){
                cityId = sharedPreferences.getString("cityid", "");
                String city = sharedPreferences.getString("cityName","");
                String country = sharedPreferences.getString("country","");

//                TextView textView = (TextView) mRootView.findViewById(R.id.textViewCity);
//                textView.setText(city + ", " + country);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    private void refresh() {
        config = new WeatherConfig();
        String cityId = sharedPreferences.getString("cityid", null);
        Log.w("Swa", "City Id [" + cityId + "]");

        if (cityId == null) {
            return ;
        }

        config.lang = WeatherUtil.getLanguage(sharedPreferences.getString("swa_lang", "en"));
        config.maxResult = 5;
        config.numDays = 5;

        String unit = sharedPreferences.getString("swa_temp_unit", "c");
        if (unit.equals("c"))
            config.unitSystem = WeatherConfig.UNIT_SYSTEM.M;
        else
            config.unitSystem = WeatherConfig.UNIT_SYSTEM.I;


        weatherClient.updateWeatherConfig(config);

        weatherClient.getCurrentCondition(cityId, new WeatherClient.WeatherEventListener() {
            @Override
            public void onWeatherRetrieved(CurrentWeather cWeather) {
                Weather weather = cWeather.weather;
                cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
                condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
                temp.setText("" + ((int) weather.temperature.getTemp()));
                unitTemp.setText(cWeather.getUnit().tempUnit);
                hum.setText(weather.currentCondition.getHumidity() + "%");
                tempMin.setText(weather.temperature.getMinTemp() + cWeather.getUnit().tempUnit);
                tempMax.setText(weather.temperature.getMaxTemp() + cWeather.getUnit().tempUnit);
                windSpeed.setText(weather.wind.getSpeed() + cWeather.getUnit().speedUnit);
                windDeg.setText((int) weather.wind.getDeg() + "° (" + WindDirection.getDir((int) weather.wind.getDeg()) + ")");
                press.setText(weather.currentCondition.getPressure() + cWeather.getUnit().pressureUnit);

                sunrise.setText(WeatherUtil.convertDate(weather.location.getSunrise()));

                sunset.setText(WeatherUtil.convertDate(weather.location.getSunset()));

                imgView.setImageResource(WeatherIconMapper.getWeatherResource(weather.currentCondition.getIcon(), weather.currentCondition.getWeatherId()));

                /*
                weatherClient.getDefaultProviderImage(weather.currentCondition.getIcon(), new WeatherClient.WeatherImageListener() {
                    @Override
                    public void onImageReady(Bitmap image) {
                        imgView.setImageBitmap(image);
                    }

                    @Override
                    public void onWeatherError(WeatherLibException wle) {
                    }

                    @Override
                    public void onConnectionError(Throwable t) {
                    }
                });
                */


                if (weather.rain[0].getTime() != null && weather.rain[0].getAmmount() != 0)
                    rain.setText(weather.rain[0].getTime() + ":" + weather.rain[0].getAmmount());
                else
                    rain.setText("----");

            }

            @Override
            public void onWeatherError(WeatherLibException t) {
                //WeatherDialog.createErrorDialog("Error parsing data. Please try again", MainActivity.this);
            }

            @Override
            public void onConnectionError(Throwable t) {
                //WeatherDialog.createErrorDialog("Error parsing data. Please try again", MainActivity.this);
            }
        });



    }



}
