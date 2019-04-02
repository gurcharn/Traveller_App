package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;

/**
 * @author Gurcharn Singh Sikka
 * @version 1.0
 *
 * First activity to be shown after login.
 */
public class HomeActivity extends AppCompatActivity {

    private TextView mTextMessage;

    /**
     * @param savedInstanceState
     *
     * Method to create view while app starts
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     *  Bottom Navigation click listener
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_itinerary:
                    mTextMessage.setText(R.string.nav_itinerary_text);
                    return true;
                case R.id.navigation_places:
                    mTextMessage.setText(R.string.nav_places_text);
                    return true;
                case R.id.navigation_people:
                    mTextMessage.setText(R.string.nav_people_text);
                    return true;
                case R.id.navigation_chat:
                    mTextMessage.setText(R.string.nav_chat_text);
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.nav_profile_text);
                    return true;
            }
            return false;
        }
    };

}
