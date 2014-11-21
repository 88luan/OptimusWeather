package com.ntl.optimus.optimusweather.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ntl.optimus.optimusweather.MainActivity;
import com.ntl.optimus.optimusweather.R;
import com.ntl.optimus.optimusweather.SearchLocationActivity;

public class CurrentFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private String cityId;
    private View mRootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.fragment_current, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        cityId = sharedPreferences.getString("cityid", "");
        String city = sharedPreferences.getString("cityName","");
        String country = sharedPreferences.getString("country","");

        TextView textView = (TextView) mRootView.findViewById(R.id.textViewCity);
        textView.setText( city + ", " + country);
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

                TextView textView = (TextView) mRootView.findViewById(R.id.textViewCity);
                textView.setText(city + ", " + country);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
