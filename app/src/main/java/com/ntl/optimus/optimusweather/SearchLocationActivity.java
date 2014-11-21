package com.ntl.optimus.optimusweather;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ntl.optimus.optimusweather.adapter.CityAdapter;
import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.model.City;

import java.util.ArrayList;
import java.util.List;


public class SearchLocationActivity extends Activity {

    private ListView cityListView;
    private CityAdapter adp;
    private WeatherClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        setTitle("Search Location");

        client = WeatherContext.getInstance().getClient(this);
        cityListView = (ListView) findViewById(R.id.listViewResult);
        adp = new CityAdapter(SearchLocationActivity.this, new ArrayList<City>());
        cityListView.setAdapter(adp);

        final EditText edt = (EditText) findViewById(R.id.editText);
        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(v.getText().toString());
                    return true;
                }

                return false;
            }
        });

        Button buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(edt.getEditableText().toString());
            }
        });
    }

    private void search(String input) {
        client.searchCity(input, new WeatherClient.CityEventListener() {
            @Override
            public void onCityListRetrieved(List<City> cityList) {
                adp.setCityList(cityList);
                adp.notifyDataSetChanged();
            }

            @Override
            public void onWeatherError(WeatherLibException t) {
            }

            @Override
            public void onConnectionError(Throwable t) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
