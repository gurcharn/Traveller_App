package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.bottomNavigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.chat.ChatContactFragment;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.people.PeopleFragment;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.people.PeopleViewFragment;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.places.PlacesFragment;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.ProfileFragment;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips.TripsFragment;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.volleyService.VolleyRequestHandler;

public class BottomNavigation extends AppCompatActivity {

    private FrameLayout fragmnetContainer;
    private BottomNavigationView bottomNavigation;

    private VolleyRequestHandler volleyRequestHandler;

    private Fragment currentFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_nav_layout);

        init();
    }

    @Override
    protected void onStop() {
        destroyCurrentFragment();
        super.onStop();
    }

    @Override
    protected void onResume() {
        if(currentFragment !=null){
            destroyCurrentFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmnetContainer, currentFragment);
            fragmentTransaction.commit();
        } else{
            startTripsFragment();
        }
        super.onResume();
    }

    private void init(){
        fragmnetContainer = (FrameLayout) findViewById(R.id.fragmnetContainer);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        volleyRequestHandler = new VolleyRequestHandler(this);
        fragmentManager = getSupportFragmentManager();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_itinerary:
                    startTripsFragment();
                    return true;
                case R.id.navigation_places:
                    startPlacesFragment();
                    return true;
                case R.id.navigation_people:
                    startPeopleFragment();
                    return true;
                case R.id.navigation_chat:
                    startChatContactFragment();
                    return true;
                case R.id.navigation_profile:
                    startProfileFragment();
                    return true;
            }
            return false;
        }
    };

    private void startTripsFragment(){
        if(currentFragment == null) {
            currentFragment = new TripsFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmnetContainer, currentFragment);
            fragmentTransaction.commit();
        } else if(!(currentFragment instanceof TripsFragment)){
            destroyCurrentFragment();
            currentFragment = new TripsFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmnetContainer, currentFragment);
            fragmentTransaction.commit();
        } else{
            destroyCurrentFragment();
            currentFragment = new TripsFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmnetContainer, currentFragment);
            fragmentTransaction.commit();
        }
    }

    private void startPlacesFragment(){
        destroyCurrentFragment();
        currentFragment = new PlacesFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmnetContainer, currentFragment);
        fragmentTransaction.commit();
    }

    private void startPeopleFragment(){
        destroyCurrentFragment();
        currentFragment = new PeopleFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmnetContainer, currentFragment);
        fragmentTransaction.commit();
    }

    private void startChatContactFragment(){
        destroyCurrentFragment();
        currentFragment = new ChatContactFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmnetContainer, currentFragment);
        fragmentTransaction.commit();
    }

    private void startProfileFragment(){
        destroyCurrentFragment();
        currentFragment = new ProfileFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmnetContainer, currentFragment);
        fragmentTransaction.commit();
    }

    private void destroyCurrentFragment(){
        try{
            volleyRequestHandler.cancelAllRequests();
            fragmentManager.beginTransaction().remove(currentFragment).commit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
