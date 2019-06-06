package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.people;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.Profile;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile.ProfileRemoteDAO;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips.Trip;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips.TripLocalDAO;

import java.util.ArrayList;
import java.util.List;

public class PeopleFragment extends Fragment {

    private View peopleSuggestionView;
    private TextView errorText;
    private Spinner tripsDrowpDown;
    private ListView peopleSuggestionListView;

    private ProgressDialog progressDialog;
    private TripLocalDAO tripLocalDAO;
    private PeopleRemoteDAO peopleRemoteDAO;
    private ArrayAdapter<String> dropDownArrayAdapter;
    private List<String> dropDownItems;
    private List<Trip> tripList;
    private ArrayList<Profile> profileArrayList;
    private CustomArrayAdapter customArrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        peopleSuggestionView = inflater.inflate(R.layout.people_suggestion_layout,container,false);

        init();
        tripsDropDownHandler();
        peopleSuggestionListViewHandler();

        return peopleSuggestionView;
    }

    /**
     *  Method to initialise variable and objects of this class
     */
    private void init(){
        errorText = peopleSuggestionView.findViewById(R.id.error_text);
        tripsDrowpDown = peopleSuggestionView.findViewById(R.id.tripsDropDown);
        peopleSuggestionListView = peopleSuggestionView.findViewById(R.id.peopleSuggestionList);

        progressDialog = new ProgressDialog(this.getContext());
        tripLocalDAO = new TripLocalDAO(this.getContext());
        peopleRemoteDAO = new PeopleRemoteDAO(this.getContext(), this);
        profileArrayList = new ArrayList<Profile>();
        customArrayAdapter = new CustomArrayAdapter(this.getContext(), profileArrayList);

        setTripsDropDown();
        peopleSuggestionListView.setAdapter(customArrayAdapter);
    }

    /**
     *  Method to handle drop down list of trips
     */
    private void tripsDropDownHandler(){
        tripsDrowpDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = dropDownItems.get(pos);

                if(item.equals("No Trips to Show") || item.equals("Select a trip..."))
                    return;
                else
                     peopleRemoteDAO.peopleSuggestionRequestHandler(getTrip(pos).getTripId());
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     *  Method to handle suggested people list view handler
     */
    private void peopleSuggestionListViewHandler(){
        peopleSuggestionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openPeopleViewFragment(customArrayAdapter.getUserId(position));
            }
        });
    }

    /**
     *  Method to set list of suggested profile
     */
    public void setProfileSuggestionList(List<Profile> profileList){
        if(profileList == null || profileList.isEmpty()){
            setErrorText(getString(R.string.no_suggestions_error), Color.RED);
        } else{
            setErrorText("", Color.RED);
            profileArrayList.clear();
            profileArrayList.addAll(profileList);
        }

        customArrayAdapter.notifyDataSetChanged();
    }

    /**
     *  Method to set drop down list of trips
     */
    private void setTripsDropDown(){
        setDropDownItems();
        dropDownArrayAdapter = new ArrayAdapter<>(this.getContext(), R.layout.support_simple_spinner_dropdown_item, dropDownItems);
        dropDownArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tripsDrowpDown.setAdapter(dropDownArrayAdapter);
    }

    /**
     *  Method to set items in drop down trips
     */
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

    /**
     *  Method to open a new fragment to view profile
     */
    private void openPeopleViewFragment(String userId){
        FragmentManager fragmentManager = getFragmentManager();
        PeopleViewFragment peopleViewFragment = new PeopleViewFragment();

        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        peopleViewFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmnetContainer, peopleViewFragment);
        fragmentTransaction.commit();
    }

    /**
     *  Method to fetch trip on that position in list
     */
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
            progressDialog.setTitle("People");
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

