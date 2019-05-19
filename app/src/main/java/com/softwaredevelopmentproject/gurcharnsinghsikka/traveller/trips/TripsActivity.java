package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;

public class TripsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private SectionsPageAdapter sectionsPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        init();
    }

    private void init(){

        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        viewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewTripsFragment(), "New");
        adapter.addFragment(new SavedTripsFragment(), "Saved");
        viewPager.setAdapter(adapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_itinerary:
                    return true;
                case R.id.navigation_places:
                    return true;
                case R.id.navigation_people:
                    return true;
                case R.id.navigation_chat:
                    return true;
                case R.id.navigation_profile:
                    return true;
            }
            return false;
        }
    };
}
