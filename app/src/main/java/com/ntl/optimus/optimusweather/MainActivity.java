package com.ntl.optimus.optimusweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity {

    public static final int REQUEST_CODE_LOCATION = 984327;
    private SharedPreferences sharedPreferences;
    private String cityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Weather");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        cityId = sharedPreferences.getString("cityid", "");
        String city = sharedPreferences.getString("cityName","");
        String country = sharedPreferences.getString("country","");

        TextView textView = (TextView) findViewById(R.id.textViewCity);
        textView.setText( city + ", " + country);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        else if (id == R.id.action_locate) {
            Intent intent = new Intent(getApplication(), SearchLocationActivity.class);
            startActivityForResult(intent, REQUEST_CODE_LOCATION);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== REQUEST_CODE_LOCATION) {
            if(resultCode == RESULT_OK){
                cityId = sharedPreferences.getString("cityid", "");
                String city = sharedPreferences.getString("cityName","");
                String country = sharedPreferences.getString("country","");

                TextView textView = (TextView) findViewById(R.id.textViewCity);
                textView.setText(city + ", " + country);
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
