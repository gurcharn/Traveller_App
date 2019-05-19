package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.home;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Gurcharn Singh Sikka
 * @version 1.0
 *
 * First activity to be shown after login.
 */
public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private AutoCompleteTextView placeAutoCompleteText;
    private EditText fromDate;
    private EditText toDate;
    private Button saveTripButton;

    private Geocoder geocoder;
    private ArrayList<String> placesSuggestions;

    /**
     * @param savedInstanceState
     *
     * Method to create view while app starts
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        placeTextEditHandler();
        fromDateHandler();
        toDateHandler();
    }

    private void init(){
        fromDate = (EditText) findViewById(R.id.fromDate);
        toDate = (EditText) findViewById(R.id.toDate);
        saveTripButton = (Button) findViewById(R.id.saveTrip);
        placeAutoCompleteText = (AutoCompleteTextView) findViewById(R.id.place);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);

        placesSuggestions = new ArrayList<String>();
        geocoder = new Geocoder(this);

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void placeTextEditHandler(){
        placeAutoCompleteText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    placesSuggestions = new ArrayList<String>();
                    System.out.println(s.toString());
                    searchPlace(s.toString());
                }
            }
        });
    }

    private void fromDateHandler(){
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatePickerDialog(fromDate);
            }
        });
    }

    private void toDateHandler(){
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatePickerDialog(toDate);
            }
        });
    }

    private void getDatePickerDialog(final EditText editText){
        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                editText.setText(date);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(HomeActivity.this,
                                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                                dateListener, year, month, day);

        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    private void updatePlaceSuggestion(){
        placeAutoCompleteText.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, placesSuggestions));
    }

    private void searchPlace(String place){
        try{
            List<Address> addresses = geocoder.getFromLocationName(place, 5);
            if(!addresses.isEmpty()){
                placesSuggestions = new ArrayList<String>();

                for(Address address : addresses){
                    placesSuggestions.add(address.getFeatureName() +", " + address.getCountryName());
                }

                updatePlaceSuggestion();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
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
