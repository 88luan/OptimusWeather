package com.ntl.optimus.optimusweather;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {

                City city = (City) parent.getItemAtPosition(pos);
                showAlertDialog(city.getId(), city.getName(), city.getCountry());
            }
        });
    }

    private void search(String input) {
        final ProgressDialog progressDialog;
        progressDialog = newInstance(SearchLocationActivity.this);
        progressDialog.show();

        client.searchCity(input, new WeatherClient.CityEventListener() {
            @Override
            public void onCityListRetrieved(List<City> cityList) {
                adp.setCityList(cityList);
                adp.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onWeatherError(WeatherLibException t) {
                progressDialog.dismiss();
            }

            @Override
            public void onConnectionError(Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    //region Show Alert Dialog
    public static class AlertDialogFragment extends DialogFragment {

        public static AlertDialogFragment newInstance(String id, String city, String country) {
            AlertDialogFragment frag = new AlertDialogFragment();
            Bundle args = new Bundle();
            args.putString("id", id);
            args.putString("city", city);
            args.putString("country", country);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final String id = getArguments().getString("id");
            final String city = getArguments().getString("city");
            final String country = getArguments().getString("country");

            return new AlertDialog.Builder(getActivity(), android.R.style.Theme_Holo_Dialog)
                    .setTitle("Your Location") //nếu không setTitle thì dialog sẽ không có thanh actionbar
                    .setIcon(R.drawable.ic_action_place)
                    .setMessage(city +", "+ country)
                    .setPositiveButton("Agree",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("cityid", id);
                                    editor.putString("cityName", city);
                                    editor.putString("country", country);
                                    editor.commit();
                                    Intent intent = new Intent();
                                    getActivity().setResult(RESULT_OK, intent);
                                    getActivity().finish();
                                }
                            }
                    )
                    .setNegativeButton("Disagree",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    //do something
                                }
                            }
                    )
                    .create();
        }
    }

    public void showAlertDialog(String id, String city, String country){
        DialogFragment newFragment = AlertDialogFragment.newInstance(id,city,country);
        newFragment.show(getFragmentManager(), "dialogAlert");
    }
    //endregion

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
