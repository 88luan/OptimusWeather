package com.ntl.optimus.optimusweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ntl.optimus.optimusweather.R;
import com.survivingwithandroid.weather.lib.model.City;

import java.util.List;

/**
 * Created by N56 on 11/21/2014.
 */
public class CityAdapter extends ArrayAdapter<City> {

private Context ctx;
private List<City> cityList;

public CityAdapter(Context ctx, List<City> cityList) {
        super(ctx, R.layout.row_city_layout, cityList);
        this.cityList = cityList;
        this.ctx = ctx;
        }


@Override
public int getCount() {
        return cityList.size();
        }


@Override
public City getItem(int position) {

        return cityList.get(position);
        }


@Override
public long getItemId(int position) {
        City city = cityList.get(position);
        return city.getId().hashCode();
        }


@Override
public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_city_layout, parent, false);
        }

        City city = cityList.get(position);
        TextView cityText = (TextView) convertView.findViewById(R.id.cityName);
        cityText.setText(city.getName() + ", " + city.getCountry());

        return convertView;
        }

public void setCityList(List<City> cityList) {
        this.cityList = cityList;
        }

        }
