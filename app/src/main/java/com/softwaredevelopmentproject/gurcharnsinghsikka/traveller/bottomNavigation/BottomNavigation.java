package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.bottomNavigation;

import android.content.Intent;
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
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.chat.ChatContactActivity;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.people.PeopleFragment;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.places.PlacesFragment;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.ProfileFragment;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips.TripsFragment;

public class BottomNavigation extends AppCompatActivity {

    private FrameLayout fragmnetContainer;
    private BottomNavigationView bottomNavigation;

    private Fragment currentFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_nav_layout);

        init();
        startTripsFragment();
    }

    private void init(){
        fragmnetContainer = (FrameLayout) findViewById(R.id.fragmnetContainer);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
                    startChatContactActivity();
                    return true;
                case R.id.navigation_profile:
                    startProfileFragment();
                    return true;
            }
            return false;
        }
    };

    private void startTripsFragment(){
        if(!(currentFragment instanceof  TripsFragment)){
            currentFragment = new TripsFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmnetContainer, currentFragment);
            fragmentTransaction.commit();
        }
    }

    private void startPlacesFragment(){
        destroyCurrentFragemnt();
        currentFragment = new PlacesFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmnetContainer, currentFragment);
        fragmentTransaction.commit();
    }

    private void startPeopleFragment(){
        destroyCurrentFragemnt();
        currentFragment = new PeopleFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmnetContainer, currentFragment);
        fragmentTransaction.commit();
    }

    private void startChatContactActivity(){
        destroyCurrentFragemnt();
        Intent chatContactActivity = new Intent(BottomNavigation.this, ChatContactActivity.class);
        startActivity(chatContactActivity);
    }

    private void startProfileFragment(){
        destroyCurrentFragemnt();
        currentFragment = new ProfileFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmnetContainer, currentFragment);
        fragmentTransaction.commit();
    }

    private void destroyCurrentFragemnt(){
        fragmentManager.beginTransaction().remove(currentFragment).commit();
    }
}
