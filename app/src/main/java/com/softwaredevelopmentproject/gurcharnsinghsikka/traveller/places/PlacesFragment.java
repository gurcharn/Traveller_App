package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.places;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips.Trip;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips.TripLocalDAO;

import java.util.ArrayList;
import java.util.List;

public class PlacesFragment extends Fragment {

    private View placesSuggestionView;
    private TextView errorText;
    private Spinner tripsDrowpDown;
    private ListView placeSuggestionListView;

    private ProgressDialog progressDialog;
    private PlacesRemoteDAO placesRemoteDAO;
    private TripLocalDAO tripLocalDAO;
    private ArrayAdapter<String> dropDownArrayAdapter;
    private List<String> dropDownItems;
    private List<Trip> tripList;
    private ArrayList<Place> placeArrayList;
    private CustomArrayAdapter customArrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        placesSuggestionView = inflater.inflate(R.layout.places_suggestions_layout,container,false);

        init();
        tripsDropDownHandler();
        placeSuggestionListViewHandler();

        return placesSuggestionView;
    }

    private void init(){
        errorText = placesSuggestionView.findViewById(R.id.error_text);
        tripsDrowpDown = placesSuggestionView.findViewById(R.id.tripsDropDown);
        placeSuggestionListView = placesSuggestionView.findViewById(R.id.placeSuggestionList);

        placesRemoteDAO = new PlacesRemoteDAO(this.getContext(), this);
        progressDialog = new ProgressDialog(this.getContext());
        tripLocalDAO = new TripLocalDAO(this.getContext());
        placeArrayList = new ArrayList<Place>();
        customArrayAdapter = new CustomArrayAdapter(this.getContext(), placeArrayList);

        setTripsDropDown();
        placeSuggestionListView.setAdapter(customArrayAdapter);
    }

    private void tripsDropDownHandler(){
        tripsDrowpDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = dropDownItems.get(pos);

                if(item.equals("No Trips to Show") || item.equals("Select a trip..."))
                    return;
                else
                    placesRemoteDAO.placesTextSearchHandler(getTrip(pos));
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void placeSuggestionListViewHandler(){
        placeSuggestionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String placeId = customArrayAdapter.getPlaceId(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query= &query_place_id=" + placeId));
                System.out.println("https://www.google.com/maps/search/?api=1&query= &query_place_id=" + placeId);
                startActivity(intent);
            }
        });
    }

    public void setPlacesSuggestionList(List<Place> placeList){
        if(placeList == null || placeList.isEmpty()){
            setErrorText(getString(R.string.no_suggestions_error), Color.RED);
        } else{
            setErrorText("", Color.RED);
            placeArrayList.clear();
            placeArrayList.addAll(placeList);
        }

        customArrayAdapter.notifyDataSetChanged();
    }

    private void setTripsDropDown(){
        setDropDownItems();
        dropDownArrayAdapter = new ArrayAdapter<>(this.getContext(), R.layout.support_simple_spinner_dropdown_item, dropDownItems);
        dropDownArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tripsDrowpDown.setAdapter(dropDownArrayAdapter);
    }

    private void setDropDownItems(){
        dropDownItems = new ArrayList<>();
        tripList = tripLocalDAO.getAllTrips();

        if(tripList == null){
            dropDownItems.add("No Trips to Show");
        } else{
            dropDownItems.clear();
            dropDownItems.add("Select a trip...");
            for(Trip trip : tripList)
                dropDownItems.add(trip.getPlace() + "\n" + trip.getArrival() + " " + trip.getDeparture());
        }
    }

    private Trip getTrip(int position){
        if(tripList == null)
            return null;
        else if(tripList.isEmpty())
            return null;
        else if(position <= 0)
            return null;
        else
            return tripList.get(position - 1);
    }

    /**
     * Method to set error text and its color in view
     * @param error
     * @param color
     */
    public void setErrorText(String error, int color){
        errorText.setTextColor(color);
        errorText.setText(error);
    }

    /**
     * Method to show progress dialog box
     * @param message
     */
    public void showProgressDialog(String message){
        if(progressDialog.isShowing())
            progressDialog.setMessage(message);
        else{
            progressDialog.setTitle("Places");
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    /**
     * Method to dismiss progress dialog
     */
    public void dismissProgressDialog(){
        progressDialog.dismiss();
    }

}
