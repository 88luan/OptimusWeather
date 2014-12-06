package com.ntl.optimus.optimusweather;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.ntl.optimus.optimusweather.fragment.CurrentFragment;
import com.ntl.optimus.optimusweather.fragment.ForecastFragment;

import java.util.List;

import ntl.optimus.com.jazzyviewpager.JazzyViewPager;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    public static final int REQUEST_CODE_LOCATION = 27;

    private String cityId;
    private JazzyViewPager mJazzyViewpager;
    private ActionBar mActionBar;
    private String[] tabs = { "Current", "Forecast" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Weather");

        setupJazzyViewpager(JazzyViewPager.TransitionEffect.CubeOut);
        mActionBar = getActionBar();
        mActionBar.setHomeButtonEnabled(false);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //ActionBar Color
        mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.indigo_500)));
        //Tab Color
        mActionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.indigo_500)));
        for (String tab_name : tabs) {
            mActionBar.addTab(mActionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
        mJazzyViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                mActionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //return resultCode to children fragment
        for (android.support.v4.app.Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

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
            Intent intent = new Intent(getApplication(), SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
        }
        else if (id == R.id.action_locate) {
            Intent intent = new Intent(getApplication(), SearchLocationActivity.class);
            startActivityForResult(intent, REQUEST_CODE_LOCATION);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left);
        }
        else if (id==R.id.action_refresh){
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            List<android.support.v4.app.Fragment> fragments = fragmentManager.getFragments();
            for(android.support.v4.app.Fragment fragment : fragments){
                if(fragment != null && fragment.getUserVisibleHint()) {
                    if(fragment instanceof CurrentFragment)
                        ((CurrentFragment) fragment).refresh();
                    else if(fragment instanceof ForecastFragment)
                        ((ForecastFragment) fragment).refresh();
                }
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        mJazzyViewpager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int index) {

            switch (index) {
                case 0:
                    // Top Rated fragment activity
                    return new CurrentFragment();
                case 1:
                    // Games fragment activity
                    return new ForecastFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            Object obj = super.instantiateItem(container, position);
            mJazzyViewpager.setObjectForPosition(obj, position);
            return obj;
        }
    }

    private void setupJazzyViewpager(JazzyViewPager.TransitionEffect effect) {
        mJazzyViewpager = (JazzyViewPager) findViewById(R.id.main);
        mJazzyViewpager.setTransitionEffect(effect);
        mJazzyViewpager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));
//        mJazzyViewpager.setPageMargin(0);
    }
}
