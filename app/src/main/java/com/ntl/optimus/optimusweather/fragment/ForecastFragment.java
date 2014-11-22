package com.ntl.optimus.optimusweather.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ntl.optimus.optimusweather.R;
import com.ntl.optimus.optimusweather.adapter.WeatherAdapter;
import com.ntl.optimus.optimusweather.util.WeatherContext;
import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.model.WeatherForecast;


public class ForecastFragment extends Fragment {
    private SharedPreferences prefs;
    private ListView forecastList;
    private WeatherClient weatherClient;
    private  View rootView;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        weatherClient = WeatherContext.getInstance().getClient(getActivity());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_forecast, container, false);
        forecastList = (ListView) rootView.findViewById(R.id.forecastDays);
		return rootView;
	}

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    public void refresh() {
        final ProgressDialog progressDialog;
        progressDialog = newInstance(getActivity());
        progressDialog.show();

        // Update forecast

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String cityId = prefs.getString("cityid", null);

        weatherClient.getForecastWeather(cityId, new WeatherClient.ForecastWeatherEventListener() {
            @Override
            public void onWeatherRetrieved(WeatherForecast forecast) {
                WeatherAdapter adp = new WeatherAdapter(forecast, getActivity());
                forecastList.setAdapter(adp);
                progressDialog.dismiss();
            }

            @Override
            public void onWeatherError(WeatherLibException t) {
                progressDialog.dismiss();

            }

            @Override
            public void onConnectionError(Throwable t) {
                //WeatherDialog.createErrorDialog("Error parsing data. Please try again", MainActivity.this);
                progressDialog.dismiss();

            }
        });
    }

    /**
     * custom ProgressDialog không có title, không có phần message, chỉ có icon
     * @param mContext
     * @return ProgressDialog
     */
    public static ProgressDialog newInstance(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (Exception e) {
        }
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        // dialog.setMessage(Message);
        return dialog;
    }

}
